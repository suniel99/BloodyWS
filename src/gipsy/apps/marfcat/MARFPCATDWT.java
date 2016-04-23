package gipsy.apps.marfcat;

import gipsy.Configuration;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandWorker.DemandWorker;
import gipsy.GEE.IDP.DemandWorker.IDemandWorker;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.ProceduralDemand;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.TAExceptionHandler;
import gipsy.GEE.multitier.DWT.DWTWrapper;
import gipsy.interfaces.LocalDemandStore;
import gipsy.interfaces.LocalGEERPool;
import gipsy.lang.GIPSYIdentifier;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import marf.Storage.ResultSet;
import marf.apps.MARFCAT.MARFCATApp;
import marf.apps.MARFCAT.Storage.FileItem;
import marf.util.BaseThread;
import marf.util.Debug;
import marf.util.MARFException;


/**
 * PS-DWT for MARPFCAT.
 * @author Serguei Mokhov
 * @version $Id: MARFPCATDWT.java,v 1.2 2013/07/19 15:37:13 mokhov Exp $
 * @since April-July 2012
 */
public class MARFPCATDWT
extends DWTWrapper
{
	/**
	 * Stopping condition. 
	 */
	protected boolean bIsWorking = false;

	/**
	 * Delegate for the majority of the business logic.
	 */
	protected MARFCATDWTApp oDelegate = null; 

	/**
	 * Default settings constructor. 
	 */
	public MARFPCATDWT()
	{
		super();
		this.oDelegate = new MARFCATDWTApp(this.oLocalDemandStore);

		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MARFPCATDWT.class.getName() + ": MARFCATDWT()"
			);
		}
	}

	/**
	 * Configuration constructor. The most commonly
	 * called via reflection and GMT management.
	 * @param poConfiguration custom tier configuration
	 */
	public MARFPCATDWT(Configuration poTierConfig)
	{
		super(poTierConfig);
		
		//Debug.enableDebug();
		
		this.oLocalDemandStore = new LocalDemandStore();
		this.oDelegate = new MARFCATDWTApp(this.oLocalDemandStore);
		
		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MARFPCATDWT.class.getName() + ": MARFCATDWT(Configuration) "
				+ poTierConfig
			);
		}
	}

	/**
	 * Multi-settings constructor.
	 * @param poLocalGEERPool GEER pool to work with
	 * @param poLocalDemandStore local DWT demand store reference
	 * @param poDMFImplementation selected DMS to communicate over
	 * @param poConfiguration custom tier configuration
	 */
	public MARFPCATDWT
	(
		LocalGEERPool poLocalGEERPool,
		LocalDemandStore poLocalDemandStore,
		EDMFImplementation poDMFImplementation,
		Configuration poConfiguration
	)
	{
		super(poLocalGEERPool, poLocalDemandStore, poDMFImplementation, poConfiguration);
		this.oDelegate = new MARFCATDWTApp(poLocalDemandStore);

		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MARFPCATDWT.class.getName() + ": MARFCATDWT(LocalGEERPool, LocalDemandStore, EDMFImplementation, Configuration) "
				+ poLocalGEERPool
				+ poLocalDemandStore
				+ poDMFImplementation
				+ poConfiguration
			);
		}
	}

	/**
	 * GEER pool constructor.
	 * @param poGEERPool the GEER pool to work with.
	 */
	public MARFPCATDWT(LocalGEERPool poGEERPool)
	{
		super(poGEERPool);
		
		this.oLocalDemandStore = new LocalDemandStore();
		this.oDelegate = new MARFCATDWTApp(this.oLocalDemandStore);

		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MARFPCATDWT.class.getName() + ": MARFCATDWT(LocalGEERPool) "
				+ poGEERPool
			);
		}
	}

	/**
	 * For testing and main bootstrapping.
	 * Enables debug mode by default.
	 * @param argv command-line arguments
	 * @throws Exception any exception that may be thrown
	 */
	public static void main(String[] argv)
	throws Exception
	{
		Debug.enableDebug();
		
		MARFPCATDWT oWorkerTier = new MARFPCATDWT(); 
		
		// Temporary test at the local worker tier
		{
		}

		BaseThread oWorkerThread = new BaseThread(oWorkerTier);
		oWorkerThread.start();
		oWorkerThread.join();
	}


	/*
	 * --------------
	 * DWTWrapper API
	 * --------------
	 */
	
	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.DWT.DWTWrapper#run()
	 */
	@Override
	public void run()
	{
		// The main business logic is at the delegate
		this.oDelegate.run();
	}
	
	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.DGT.DGTWrapper#startTier()
	 */
	@Override
	public void startTier()
	throws MultiTierException
	{
		this.oWorker = this.oDelegate;
		super.startTier();
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.DGT.DGTWrapper#stopTier()
	 */
	@Override
	public void stopTier()
	throws MultiTierException
	{
		this.bIsWorking = false;
		this.oDelegate.stopWorker();
		super.stopTier();
	}

	/*
	 * --------
	 * Delegate
	 * --------
	 */
	
	/**
	 * A DGT-specific subclass of MARFCATApp.
	 * @author Serguei Mokhov
	 */
	public class MARFCATDWTApp
	extends MARFCATApp
	implements IDemandWorker
	{
		/**
		 * Reference to the local store at DWT for temporary
		 * demand copies.
		 */
		private LocalDemandStore oDemandStore = null;
		
		/**
		 * Thread state as a demand worker.
		 */
		private boolean bIsWorking = false;
		
		/**
		 * Encapsulation of the generic demand worker functionality.
		 */
		private IDemandWorker oDemandWorker = new DemandWorker
		(
			MARFCATDWTApp.class.getName() + ": " + MARFCATDWTApp.getVersion()
		);

		/**
		 * Demand-store constructor.
		 * @param poDemandStore reference to the local DWT demand store.
		 */
		public MARFCATDWTApp(LocalDemandStore poDemandStore)
		{
			this.oDemandStore = poDemandStore;
		}

		/**
		 * Run the main delegate task.
		 * XXX: fix a hardcoded config to be configurable and scripted.
		 */
		public void execute()
		{
			// XXX:
			String[] astrArgv = 
			{
				"--batch-ident",
				"-nopreprep", "-raw", "-fft", "-cheb",
				"--debug",
				"--dwt",
				"/local/data/marfcat/app/wireshark-1.2.0_train.xml"
			};

			if(Debug.isDebugOn())
			{
				Debug.debug
				(
					MARFCATDWTApp.class.getName() + ": oTransportAgent "
					+ oTransportAgent
				);
			}
			
			process(astrArgv);
		}

		/*
		 * --------------
		 * MARFCATApp API
		 * --------------
		 */
		
		@Override
		public void doTestingWork(String[] pastrArgv, Integer piExpectedID)
		throws MARFException
		{
			if(Debug.isDebugOn())
			{
				Debug.debug
				(
					MARFCATDWTApp.class.getName() + ": doTestingWork() "
				);
			}

			IDemand oDemand = MARFPCATDWT.this.oTransportAgent.getDemand();

			if(Debug.isDebugOn())
			{
				Debug.debug
				(
					MARFCATDWTApp.class.getName() + ": demand read: "
					+ oDemand
				);
			}
	
			Serializable oProcessValue = oDemand.getResult();

			if(oProcessValue instanceof FileItem)
			{
				FileItem oFileItem = (FileItem)oProcessValue;
				
				this.oDemandStore.put(oDemand.getSignature(), oDemand);
			
				if(Debug.isDebugOn())
				{
					Debug.debug
					(
						MARFCATDWTApp.class.getName() + ": about to run real pipeline for: "
						+ oFileItem
					);
				}

				String strConfig = getConfigString(pastrArgv);
				ident(strConfig, oFileItem, piExpectedID);
			}
			else
			{
				MultiTierException oException = new MultiTierException
				(
					"Demand's value " + oProcessValue
					+ " is of type " + (oProcessValue == null ? "null" : oProcessValue.getClass().getName())
					+ " and not of FileItem."
				);
				
				if(Debug.isDebugOn())
				{
					Debug.debug
					(
						MARFCATDWTApp.class.getName() + ": returning exception as result: "
						+ oException
					);
				}
				
				oDemand.storeResult(oException);
				MARFPCATDWT.this.oTransportAgent.setResult(oDemand);
			}
		}

		/**
		 * Override to to demand driven classification results collection.
		 * @param poFileItem
		 * @param poExpectedIDs
		 * @return
		 * @throws MARFException
		 */
		@Override
		public ResultSet doClassificationWork(FileItem poFileItem, List<Integer> poExpectedIDs)
		throws MARFException
		{
			if(Debug.isDebugOn())
			{
				Debug.debug
				(
					MARFCATDWTApp.class.getName() + ": doing classification work for "
					+ poFileItem
				);
			}
			
			// Delegate the actual classification work to MARF
			ResultSet oMARFCATResultSet = super.doClassificationWork(poFileItem, poExpectedIDs);

			// Recover the original signature of the demand on arrival
			DemandSignature oOriginalSig = new DemandSignature();
			
			for(DemandSignature oSig: this.oDemandStore.getKeySet())
			{
				IDemand oCurrentDemand = this.oDemandStore.get(oSig);

				if(Debug.isDebugOn())
				{
					Debug.debug
					(
						MARFCATDWTApp.class.getName() + ": looking up original sig for "
						+ oCurrentDemand
					);
				}
				
				if(poFileItem.getPath().equals(((FileItem)oCurrentDemand.getResult()).getPath()))
				{
					oOriginalSig = oSig;

					if(Debug.isDebugOn())
					{
						Debug.debug
						(
							MARFCATDWTApp.class.getName() + ": found original sig "
							+ oOriginalSig
						);
					}

					// Clean up local store
					this.oDemandStore.remove(oSig);
					
					break;
				}
			}
			
			// Compose and send the result(set) back
			ProceduralDemand oResult = new ProceduralDemand(poFileItem.getPath());
			oResult.setSignature(oOriginalSig);
			oResult.storeResult(oMARFCATResultSet);
			oResult.setState(DemandState.COMPUTED);
			oResult.setProgramID(new GIPSYIdentifier(MARFCATDWTApp.class.getName() + ": " + getVersion()));

			if(Debug.isDebugOn())
			{
				Debug.debug
				(
					MARFCATDWTApp.class.getName() + ": writing result: "
					+ oResult
				);
			}
			
			MARFPCATDWT.this.oTransportAgent.setResult(oResult);
			
			return oMARFCATResultSet;
		}
		
		/*
		 * -----------------
		 * IDemandWorker API
		 * -----------------
		 */
		
		@Override
		public void run()
		{
			if(Debug.isDebugOn())
			{
				Debug.debug
				(
					MARFCATDWTApp.class.getName() + ": run() "
				);
			}
			
			this.bIsWorking = true;
			
			while(this.bIsWorking)
			{
				execute();
				
				System.out.println
				(
					"MARFCAT Worker Delegate Completed a round on "
					+ new Date()
				);
			}
		}

		@Override
		public void setTransportAgent(EDMFImplementation poDMFImp)
		{
			this.oDemandWorker.setTransportAgent(poDMFImp);
		}

		@Override
		public void setTransportAgent(ITransportAgent poTA)
		{
			this.oDemandWorker.setTransportAgent(poTA);
		}

		@Override
		public void startWorker()
		{
			this.oDemandWorker.startWorker();
			this.bIsWorking = true;
		}

		@Override
		public void stopWorker()
		{
			this.oDemandWorker.stopWorker();
			this.bIsWorking = false;
		}

		@Override
		public void setTAExceptionHandler(TAExceptionHandler poTAExceptionHandler)
		{
			this.oDemandWorker.setTAExceptionHandler(poTAExceptionHandler);
		}
	}
}

// EOF
