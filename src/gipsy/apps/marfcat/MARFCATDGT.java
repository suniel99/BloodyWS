package gipsy.apps.marfcat;

import gipsy.Configuration;
import gipsy.GEE.GEEException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcher;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.ProceduralDemand;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.DGT.DGTWrapper;
import gipsy.GEE.multitier.DST.TAFactory;
import gipsy.interfaces.LocalDemandStore;
import gipsy.interfaces.LocalGEERPool;
import gipsy.lang.GIPSYIdentifier;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Instant;

import marf.Storage.ResultSet;
import marf.apps.MARFCAT.MARFCATApp;
import marf.apps.MARFCAT.Storage.FileItem;
import marf.apps.MARFCAT.Storage.GenericMetaXMLLoader;
import marf.util.BaseThread;
import marf.util.Debug;
import marf.util.MARFException;


/**
 * PS-DGT for MARFCAT.
 * @author Serguei Mokhov
 * @version $Id: MARFCATDGT.java,v 1.10 2014/06/09 14:34:24 mokhov Exp $
 */
public class MARFCATDGT
extends DGTWrapper
{
    private static final String CONFIG_MARFCAT_META_BASEDIR_DEFAULT = "./";

    public static final String CONFIG_MARFCAT_META_BASEDIR  = "marfcat.meta.basedir";
    public static final String CONFIG_MARFCAT_META_FILENAME = "marfcat.meta.filename";
    public static final String CONFIG_MARFCAT_PARAMS  = "marfcat.args";
    
    private String strMetaBaseDir;
    private String strMetaFilename;
	private String strMarfcatConfig;
    
	/**
	 * Stopping condition. 
	 */
	protected boolean bIsWorking = false;
	
	/**
	 * Delegate for the majority of the business logic.
	 */
	protected IMARFCATDelegateApp oDelegate = null;
	
	/**
	 * Default settings constructor. 
	 * @throws GEEException XXX
	 */
	public MARFCATDGT()
	throws GEEException
	{
		super();
		this.oDelegate = new MARFCATDGTApp(this.oDemandDispatcher, this.oLocalDemandStore);

		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MARFCATDGT.class.getName() + ": MARFCATDGT()"
			);
		}

		init();
	}

	/**
	 * Configuration constructor. The most commonly
	 * called via reflection and GMT management.
	 * @param poConfiguration custom tier configuration
	 */
	public MARFCATDGT(Configuration poConfiguration) throws MultiTierException
	{
		super(poConfiguration);
		
		this.oLocalDemandStore = new LocalDemandStore();
		this.oDelegate = new MARFCATDGTApp(this.oDemandDispatcher, this.oLocalDemandStore);
		
		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MARFCATDGT.class.getName() + ": MARFCATDGT(Configuration) "
				+ poConfiguration
			);
		}

		init();
	}

	/**
	 * @param poLocalGEERPool
	 * @param poLocalDemandStore
	 * @param poDMFImplementation
	 * @param poConfiguration
	 * @throws GEEException
	 */
	public MARFCATDGT
	(
		LocalGEERPool poLocalGEERPool,
		LocalDemandStore poLocalDemandStore,
		EDMFImplementation poDMFImplementation,
		Configuration poConfiguration
	)
	throws GEEException
	{
		super(poLocalGEERPool, poLocalDemandStore, poDMFImplementation, poConfiguration);

		this.oDelegate = new MARFCATDGTApp(this.oDemandDispatcher, poLocalDemandStore);

		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MARFCATDGT.class.getName() + ": MARFCATDGT(LocalGEERPool, LocalDemandStore, EDMFImplementation, Configuration) "
				+ poLocalGEERPool
				+ poLocalDemandStore
				+ poDMFImplementation
				+ poConfiguration
			);
		}

		init();
	}

	/**
	 * Multi-settings constructor.
	 * @param poLocalGEERPool GEER pool to work with
	 * @param poLocalDemandStore local DWT demand store reference
	 * @param poDemandDispatcher the specific demand dispatcher to set
	 * @param poConfiguration custom tier configuration
	 * @throws GEEException XXX
	 */
	public MARFCATDGT
	(
		LocalGEERPool poLocalGEERPool,
		LocalDemandStore poLocalDemandStore,
		IDemandDispatcher poDemandDispatcher,
		Configuration poConfiguration
	)
	throws GEEException
	{
		super(poLocalGEERPool, poLocalDemandStore, poDemandDispatcher, poConfiguration);

		this.oDelegate = new MARFCATDGTApp(poDemandDispatcher, poLocalDemandStore);

		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MARFCATDGT.class.getName() + ": MARFCATDGT(LocalGEERPool, LocalDemandStore, IDemandDispatcher, Configuration)"
				+ poLocalGEERPool
				+ poLocalDemandStore
				+ poDemandDispatcher
				+ poConfiguration
			);
		}

		init();
	}


	private void init() throws MultiTierException 
	{
	    this.strMetaBaseDir = this.oConfiguration.getProperty(CONFIG_MARFCAT_META_BASEDIR, CONFIG_MARFCAT_META_BASEDIR_DEFAULT);
	    this.strMetaFilename = this.oConfiguration.getProperty(CONFIG_MARFCAT_META_FILENAME);
	    this.strMarfcatConfig = this.oConfiguration.getProperty(CONFIG_MARFCAT_PARAMS);
   
	    if(Debug.isDebugOn())
        {
            Debug.debug(MARFCATDWT.class.getName() + ": strMetaBaseDir=" + this.strMetaBaseDir);
            Debug.debug(MARFCATDWT.class.getName() + ": strMetaFilename=" + this.strMetaFilename);
            Debug.debug(MARFCATDWT.class.getName() + ": config string=" + this.strMarfcatConfig);
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
		
		MARFCATDGT oDGT = new MARFCATDGT();
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
	@Override
	public void run()
	{
		this.bIsWorking = true;

		while(this.bIsWorking)
		{
		    try {
    			System.out.println("[" + Instant.now().getMillis() + "] MARFCATDGT Worker Delegate STARTED");
		        this.oDelegate.execute();
    			System.out.println("[" + Instant.now().getMillis() + "] MARFCATDGT Worker Delegate COMPLETED");
		    } catch (MultiTierException e) {
			    System.out.println("MARFCAT Delegate error.");
			    e.printStackTrace();
		    }
			
			this.bIsWorking = false;
		}
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.DGT.DGTWrapper#startTier()
	 */
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
	@Override
	public void stopTier()
	throws MultiTierException
	{
		this.bIsWorking = false;
	}
	
	/*
	 * -------------
	 * MARFCATDGTApp
	 * -------------
	 */
	
	/**
	 * A DGT-specific subclass of MARFCATApp.
	 * @author Serguei Mokhov
	 */
	public class MARFCATDGTApp
	extends MARFCATApp
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
		public MARFCATDGTApp(IDemandDispatcher poDispatcher)
		{
			this.oDispatcher = poDispatcher;
		}

		/**
		 * The demand dispatcher and store constructor.  
		 * @param poDispatcher the local dispatcher reference
		 * @param poDemandStore the local demand store reference
		 */
		public MARFCATDGTApp(IDemandDispatcher poDispatcher, LocalDemandStore poDemandStore)
		{
			this.oDispatcher = poDispatcher;
			this.oDemandStore = poDemandStore;
		}
		
		/**
		 * Local demand store constructor.
		 * @param poDemandStore the local demand store reference
		 */
		public MARFCATDGTApp(LocalDemandStore poDemandStore)
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
		 * @throws MultiTierException 
		 */
		public void execute() throws MultiTierException
		{
		    if (MARFCATDGT.this.strMetaFilename == null) {
		        throw new MultiTierException("You didn't provide the " 
                            +  CONFIG_MARFCAT_META_FILENAME  
                            + " property in your marfcatDGT.config");
		    }

            if (MARFCATDGT.this.strMarfcatConfig == null) {
		        throw new MultiTierException("You didn't provide the " 
                            +  CONFIG_MARFCAT_PARAMS
                            + " property in your marfcatDGT.config");
            }

			// XXX: have to do it here because at the construction time
			//      the dispatcher is not yet available. Perhaps should move
			//      it elsewhere or use the outer class's dispatcher directly.
			this.oDispatcher = MARFCATDGT.this.oDemandDispatcher;

            String filename = MARFCATDGT.this.strMetaBaseDir + "/" + MARFCATDGT.this.strMetaFilename;
            String[] astrArgv = StringUtils.split(MARFCATDGT.this.strMarfcatConfig + " " + filename, " ");

            if(Debug.isDebugOn())
            {
                Debug.debug(MARFCATDGTApp.class.getName() + "astrArgv=" + StringUtils.join(astrArgv, " "));
            }

            process(astrArgv);
		}
	} // Inner class MARFCATDGTApp
}

// EOF
