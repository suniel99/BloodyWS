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

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Instant;

import marf.Storage.ResultSet;
import marf.apps.MARFCAT.MARFCATApp;
import marf.apps.MARFCAT.Storage.FileItem;
import marf.util.BaseThread;
import marf.util.Debug;
import marf.util.MARFException;


/**
 * PS-DWT for MARFCAT.
 * @author Serguei Mokhov
 * @version $Id: MARFCATDWT.java,v 1.9 2012/06/18 02:13:35 mokhov Exp $
 * @since April-June 2012
 */
public class MARFCATDWT
extends DWTWrapper
{
    private static final String CONFIG_MARFCAT_META_BASEDIR_DEFAULT = "./";

    public static final String CONFIG_MARFCAT_META_BASEDIR  = "marfcat.meta.basedir";
    public static final String CONFIG_MARFCAT_META_FILENAME = "marfcat.meta.filename"; // comma separated list of filename ??
    public static final String CONFIG_MARFCAT_PARAMS  = "marfcat.args";
    
    private String metaBaseDir;
    private String metaFilename;
	private String marfcatConfig;

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
	public MARFCATDWT()
	{
		super();
		this.oDelegate = new MARFCATDWTApp(this.oLocalDemandStore);

		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MARFCATDWT.class.getName() + ": MARFCATDWT()"
			);
		}

		init();
	}

	/**
	 * Configuration constructor. The most commonly
	 * called via reflection and GMT management.
	 * @param poConfiguration custom tier configuration
	 */
	public MARFCATDWT(Configuration poTierConfig)
	{
		super(poTierConfig);
		
		//Debug.enableDebug();
		
		this.oLocalDemandStore = new LocalDemandStore();
		this.oDelegate = new MARFCATDWTApp(this.oLocalDemandStore);
		
		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MARFCATDWT.class.getName() + ": MARFCATDWT(Configuration) "
				+ poTierConfig
			);
		}

		init();
	}

	/**
	 * Multi-settings constructor.
	 * @param poLocalGEERPool GEER pool to work with
	 * @param poLocalDemandStore local DWT demand store reference
	 * @param poDMFImplementation selected DMS to communicate over
	 * @param poConfiguration custom tier configuration
	 */
	public MARFCATDWT
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
				MARFCATDWT.class.getName() + ": MARFCATDWT(LocalGEERPool, LocalDemandStore, EDMFImplementation, Configuration) "
				+ poLocalGEERPool
				+ poLocalDemandStore
				+ poDMFImplementation
				+ poConfiguration
			);
		}

		init();
	}

	/**
	 * GEER pool constructor.
	 * @param poGEERPool the GEER pool to work with.
	 */
	public MARFCATDWT(LocalGEERPool poGEERPool)
	{
		super(poGEERPool);
		
		this.oLocalDemandStore = new LocalDemandStore();
		this.oDelegate = new MARFCATDWTApp(this.oLocalDemandStore);

		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MARFCATDWT.class.getName() + ": MARFCATDWT(LocalGEERPool) "
				+ poGEERPool
			);
		}
		
		init();
	}

	private void init()
	{
	    this.metaBaseDir = this.oConfiguration.getProperty(CONFIG_MARFCAT_META_BASEDIR, CONFIG_MARFCAT_META_BASEDIR_DEFAULT);
	    this.metaFilename = this.oConfiguration.getProperty(CONFIG_MARFCAT_META_FILENAME);
	    this.marfcatConfig = this.oConfiguration.getProperty(CONFIG_MARFCAT_PARAMS);
    
	    if(Debug.isDebugOn())
        {
            Debug.debug(MARFCATDWT.class.getName() + ": metaBaseDir=" + this.metaBaseDir);
            Debug.debug(MARFCATDWT.class.getName() + ": metaFilename=" + this.metaFilename);
            Debug.debug(MARFCATDWT.class.getName() + ": config string=" + this.marfcatConfig);
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
		
		MARFCATDWT oWorkerTier = new MARFCATDWT(); 

		BaseThread oWorkerThread = new BaseThread(oWorkerTier);
		oWorkerThread.start();
		oWorkerThread.join();
	}


	/*
	 * --------------
	 * DWTWrapper API
	 * --------------
	 */
	
	/* 
	 * Note : The MARFCATDWT thread is started from within the super.startTier method.
	 * (non-Javadoc)
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
		 * @throws MultiTierException 
		 */
		public void execute() throws MultiTierException
		{
		    if (MARFCATDWT.this.metaFilename == null) {
		        throw new MultiTierException("You didn't provide the " 
                            +  CONFIG_MARFCAT_META_FILENAME  
                            + " property in your marfcatDGT.config");
		    }

            if (MARFCATDWT.this.marfcatConfig == null) {
		        throw new MultiTierException("You didn't provide the " 
                            +  CONFIG_MARFCAT_PARAMS
                            + " property in your marfcatDGT.config");
            }

			if(Debug.isDebugOn())
			{
				Debug.debug
				(
					MARFCATDWTApp.class.getName() + ": oTransportAgent "
					+ oTransportAgent
				);
			}

            String filename = MARFCATDWT.this.metaBaseDir + "/" + MARFCATDWT.this.metaFilename;
            String[] astrArgv = StringUtils.split(MARFCATDWT.this.marfcatConfig + " " + filename, " ");

            if(Debug.isDebugOn())
            {
                Debug.debug("[" + MARFCATDWTApp.class.getName() + "] astrArgv=" + StringUtils.join(astrArgv, " "));
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

    		System.out.println("[" +  new Date() + "] MARFCATDWT Worker Delegate WAITING for demands...");
			IDemand oDemand = MARFCATDWT.this.oTransportAgent.getDemand();

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
				MARFCATDWT.this.oTransportAgent.setResult(oDemand);
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
			
			MARFCATDWT.this.oTransportAgent.setResult(oResult);
			
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
    		    try {
    		        System.out.println("[" +  Instant.now() + "] MARFCATDWT Worker Delegate STARTED");
    		        execute();
    		        System.out.println("[" +  Instant.now() + "] MARFCATDWT Worker Delegate COMPLETED");
    		    } catch (MultiTierException e) {
    			    System.out.println("MARFCAT Delegate error.");
    			    e.printStackTrace();
    		    }
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
