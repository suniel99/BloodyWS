package gipsy.apps.marfcat;

import gipsy.Configuration;
import gipsy.GEE.GEEException;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.ProceduralDemand;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.interfaces.LocalDemandStore;
import gipsy.interfaces.LocalGEERPool;
import gipsy.lang.GIPSYIdentifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import marf.Storage.ResultSet;
import marf.apps.MARFCAT.MARFPCATApp;
import marf.apps.MARFCAT.Storage.FileItem;
import marf.apps.MARFCAT.Storage.GenericMetaXMLLoader;
import marf.util.Debug;
import marf.util.MARFException;


/**
 * PS-DGT for MARFPCAT.
 * @author Serguei Mokhov
 * @version $Id: MARFPCATDGT.java,v 1.3 2014/06/09 14:34:24 mokhov Exp $
 * @since April-July 2012
 */
public class MARFPCATDGT
extends MARFCATDGT
{
	/**
	 * Default settings constructor. 
	 * @throws GEEException XXX
	 */
	public MARFPCATDGT()
	throws GEEException
	{
		super();
		this.oDelegate = new MARFPCATDGTApp(this.oDemandDispatcher, this.oLocalDemandStore);

		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MARFPCATDGT.class.getName() + ": MARFPCATDGT()"
			);
		}
	}

	/**
	 * Configuration constructor. The most commonly
	 * called via reflection and GMT management.
	 * @param poConfiguration custom tier configuration
	 * @throws MultiTierException 
	 */
	public MARFPCATDGT(Configuration poConfiguration) throws MultiTierException
	{
		super(poConfiguration);
		
		this.oLocalDemandStore = new LocalDemandStore();
		this.oDelegate = new MARFPCATDGTApp(this.oDemandDispatcher, this.oLocalDemandStore);
		
		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MARFPCATDGT.class.getName() + ": MARFPCATDGT(Configuration) "
				+ poConfiguration
			);
		}
	}

	/**
	 * @param poLocalGEERPool
	 * @param poLocalDemandStore
	 * @param poDMFImplementation
	 * @param poConfiguration
	 * @throws GEEException
	 */
	public MARFPCATDGT
	(
		LocalGEERPool poLocalGEERPool,
		LocalDemandStore poLocalDemandStore,
		EDMFImplementation poDMFImplementation,
		Configuration poConfiguration
	)
	throws GEEException
	{
		super(poLocalGEERPool, poLocalDemandStore, poDMFImplementation, poConfiguration);

		this.oDelegate = new MARFPCATDGTApp(this.oDemandDispatcher, poLocalDemandStore);

		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MARFPCATDGT.class.getName() + ": MARFPCATDGT(LocalGEERPool, LocalDemandStore, EDMFImplementation, Configuration) "
				+ poLocalGEERPool
				+ poLocalDemandStore
				+ poDMFImplementation
				+ poConfiguration
			);
		}
	}

	/**
	 * Multi-settings constructor.
	 * @param poLocalGEERPool GEER pool to work with
	 * @param poLocalDemandStore local DWT demand store reference
	 * @param poDemandDispatcher the specific demand dispatcher to set
	 * @param poConfiguration custom tier configuration
	 * @throws GEEException XXX
	 */
	public MARFPCATDGT
	(
		LocalGEERPool poLocalGEERPool,
		LocalDemandStore poLocalDemandStore,
		IDemandDispatcher poDemandDispatcher,
		Configuration poConfiguration
	)
	throws GEEException
	{
		super(poLocalGEERPool, poLocalDemandStore, poDemandDispatcher, poConfiguration);

		this.oDelegate = new MARFPCATDGTApp(poDemandDispatcher, poLocalDemandStore);

		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MARFPCATDGT.class.getName() + ": MARFPCATDGT(LocalGEERPool, LocalDemandStore, IDemandDispatcher, Configuration)"
				+ poLocalGEERPool
				+ poLocalDemandStore
				+ poDemandDispatcher
				+ poConfiguration
			);
		}
	}

	/**
	 * For local testing purposes.
	 * Enables debug by default.
	 * @param argv command-line arguments
	 * @throws Exception any exception that may be thrown
	 */
	public static void main(String[] argv)
	throws Exception
	{
		Debug.enableDebug();
		
		MARFPCATDGT oDGT = new MARFPCATDGT();
		oDGT.startTier();
		oDGT.wait();
	}

	/*
	 * --------------
	 * DGTWrapper API
	 * --------------
	 */
	
	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.DGT.DGTWrapper#run()
	 */
	/*
	@Override
	public void run()
	{
		this.bIsWorking = true;

		while(this.bIsWorking)
		{
			this.oDelegate.execute();
			
			System.out.println("MARFCAT Delegate Completed.");
			
			this.bIsWorking = false;
		}
	}
	*/

	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.DGT.DGTWrapper#startTier()
	 */
	/*
	@Override
	public void startTier()
	throws MultiTierException
	{
		try
		{
			// Create a TA instance using the configuration
			Configuration oTAConfig = (Configuration)this.oConfiguration.getObjectProperty(DGTWrapper.TA_CONFIG);
			ITransportAgent oTA = TAFactory.getInstance().createTA(oTAConfig);
			
			// Create a DemandDispatcher instance using the configuration
			String strImplClassName = this.oConfiguration.getProperty(DGTWrapper.DEMAND_DISPATCHER_IMPL);
			Class<?> oImplClass = Class.forName(strImplClassName);
			Class<?>[] aoParamTypes = new Class[] { ITransportAgent.class };
			Constructor<?> oImplConstructor = oImplClass.getConstructor(aoParamTypes);
			Object[] aoArgs = new Object[] { oTA };
			
			this.oDemandDispatcher = (DemandDispatcher)oImplConstructor.newInstance(aoArgs);
			this.oDemandDispatcher.setTAExceptionHandler(this.oTAExceptionHandler);
			
			new BaseThread(this).start();
		}
		catch(MultiTierException oException)
		{
			oException.printStackTrace(System.err);
			throw oException;
		}
		catch(Exception oException)
		{
			oException.printStackTrace(System.err);
			throw new MultiTierException(oException);
		}
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.DGT.DGTWrapper#stopTier()
	 */
	/*
	@Override
	public void stopTier()
	throws MultiTierException
	{
		this.bIsWorking = false;
	}
	*/
	
	/*
	 * -------------
	 * MARFPCATDGTApp
	 * -------------
	 */
	
	/**
	 * A DGT-specific subclass of MARFCATApp.
	 * @author Serguei Mokhov
	 */
	public class MARFPCATDGTApp
	extends MARFPCATApp
	implements IMARFCATDelegateApp
	{
		/**
		 * The local DGT demand dispatcher reference.
		 */
		private IDemandDispatcher oDispatcher = null;
		
		/**
		 * The local DGT demand store reference.
		 */
		private LocalDemandStore oDemandStore = null;
		
		/**
		 * Demand dispatcher constructor.
		 * @param poDispatcher the local dispatcher reference
		 */
		public MARFPCATDGTApp(IDemandDispatcher poDispatcher)
		{
			this.oDispatcher = poDispatcher;
		}

		/**
		 * The demand dispatcher and store constructor.  
		 * @param poDispatcher the local dispatcher reference
		 * @param poDemandStore the local demand store reference
		 */
		public MARFPCATDGTApp(IDemandDispatcher poDispatcher, LocalDemandStore poDemandStore)
		{
			this.oDispatcher = poDispatcher;
			this.oDemandStore = poDemandStore;
		}
		
		/**
		 * Local demand store constructor.
		 * @param poDemandStore the local demand store reference
		 */
		public MARFPCATDGTApp(LocalDemandStore poDemandStore)
		{
			this.oDemandStore = poDemandStore;
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
					MARFCATDGTApp.class.getName() + ": Testing collection size: "
					+ GenericMetaXMLLoader.getInstance().getFileItems().size()
				);
	
				Debug.debug
				(
					MARFCATDGTApp.class.getName() + ": Demand store: "
					+ this.oDemandStore
				);
			}

			// Process all the file items for the purpose of the identification
			ArrayList<FileItem> oFileItems = GenericMetaXMLLoader.getInstance().getFileItems();

			// Throw out our demands first, and then start gradually harvesting
			// each corresponding results
			for(FileItem oFileItem: oFileItems)
			{
				//IDemand oDemand = null; 
				ProceduralDemand oDemand = null; 
				
				DemandSignature oSignature = new DemandSignature(oFileItem.hashCode());
				
				oDemand = new ProceduralDemand(oFileItem.getPath());
				oDemand.setProgramID(new GIPSYIdentifier(getClass().getName() + ": " + getVersion()));
				oDemand.storeResult(oFileItem);
				oDemand.setSignature(oSignature);
				oDemand.setState(DemandState.PENDING);

				if(Debug.isDebugOn())
				{
					Debug.debug
					(
						MARFCATDGTApp.class.getName() + ": Demand prepared: "
						+ oDemand
					);
				}

				this.oDemandStore.put(oSignature, oDemand);

				if(Debug.isDebugOn())
				{
					Debug.debug
					(
						MARFCATDGTApp.class.getName() + ": Demand about to dispatch... "
					);
				}
				
				this.oDispatcher.writeDemand(oDemand);
			}
			
			// ... some time passes ...
			if(Debug.isDebugOn())
			{
				Debug.debug
				(
					MARFCATDGTApp.class.getName() + ": Done generating demands... "
					+ "Let's start result collection..."
				);
			}
	
			// Run the demand-driven result collection with the rest of
			// the reporting business logic 
			for(FileItem oFileItem: oFileItems)
			{
				String strConfig = getConfigString(pastrArgv);
				ident(strConfig, oFileItem, piExpectedID);
			}
		}

		/**
		 * Override to do demand driven classification results collection.
		 * @param poFileItem the file item constitutes a demand
		 * @param poExpectedIDs a collection of expected IDs (may be empty)
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
					MARFCATDGTApp.class.getName() + ": doing classification work for "
					+ poFileItem
					+ " with sig " + poFileItem.hashCode()
				);
			}
			
			// Obtain demand result (a MARF ResultSet) from the DST.
			// Block if unavailable.
			IDemand oResult = null;
			DemandSignature oSignature = new DemandSignature(poFileItem.hashCode());
			oResult = this.oDispatcher.readResult(oSignature);
			this.oDemandStore.remove(oSignature);
	
			Serializable oResultValue = oResult.getResult();

			if(Debug.isDebugOn())
			{
				Debug.debug
				(
					MARFCATDGTApp.class.getName() + ": Demand result... "
					+ oResult
				);
			}
						
			ResultSet oMARFCATResultSet = null;
			
			if(oResultValue instanceof ResultSet)
			{
				oMARFCATResultSet = (ResultSet)oResultValue;
			}
			else
			{
				System.err.println
				(
					"Returned result is not of a type: " + ResultSet.class.getName()
					+ ", but of type " + (oResultValue == null ? null : oResultValue.getClass().getName())
					+ ", and of toString() value " + oResultValue
					+ ". Ignoring..."
				);
			}
			
			return oMARFCATResultSet;
		}
		
		/**
		 * Delegate the execution of the business logic mostly to MARFCAT.
		 * XXX: hardcoded config should be automated/scripted/parametrized
		 */
		public void execute()
		{
			// XXX:
			String[] astrArgv = 
			{
				"--batch-ident",
				"-nopreprep", "-raw", "-fft", "-cheb",
				"--debug",
				"--dgt",
				"/local/data/marfcat/app/wireshark-1.2.0_train.xml"
			};
			
			// XXX: have to do it here because at the construction time
			//      the dispatcher is not yet available. Perhaps should move
			//      it elsewhere or use the outer class's dispatcher directly.
			this.oDispatcher = MARFPCATDGT.this.oDemandDispatcher;

			process(astrArgv);
		}
	} // Inner class MARFCATDGTApp
}

// EOF
