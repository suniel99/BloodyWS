package gipsy.tests.GEE.simulator;

import gipsy.Configuration;
import gipsy.GIPSY;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcher;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.TimeLine;

import java.awt.Dialog.ModalityType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JOptionPane;


/**
 * This class holds all the global definitions.
 * 
 * @author Emil Vassev
 * @version $Id: GlobalDef.java,v 1.24 2011/01/26 05:11:36 ji_yi Exp $
 * @since
 */
public class GlobalDef 
{
	/**
	 * Configuration setting constants.
	 */ 
	private static final String DEMAND_CLASS_FILE = "demandClasses.in";
	private static final String CONFIG_FILE = "simulation.config";
	private static final String DEMAND_DISPATCHER_CLASS_KEY = "ca.concordia.cse.gipsy.tests.gee.dms.simulator.demanddispatcher";
	
	// Metric recording facilities
	
	/**
	 * The cache keeping time lines generated from the DGT side
	 */
	public static final Map<DemandSignature, TimeLine> TIMELINES = new HashMap<DemandSignature, TimeLine>();
	
	public static final String DGT_SEND = "DGT-SEND";
	public static final String DGT_RECEIVE = "DGT-RECEIVE";
	public static final String WORKER_SEND = "WORKER-SEND";
	public static final String WORKER_RECEIVE = "WORKER-RECEIVE";
	
	/**
	 * Profile directory name.	
	 */ 
	public static final String PROFILE_DIRECTORY = "profiles";
	public static final String PROFILE_EXTENSION = ".dgt";
	public static final String SHORT_PROFILE_EXTENSION = "dgt";

	/**
	 * Control symbols.
	 * XXX: redundant -- defined in Java.	
	 */ 
	public static final char CR = (char)13;
	public static final char LF = (char)10;
	
	/**
	 * Holds the current profile directory. 
	 */
	public static String sstrProfileDir;

	/**
	 * "Pause sign" for the entire system.
	 */
	public static Semaphore soSynchronizer;

	/**
	 * "Pause sign" for the DemandReceiver if the mode option below
	 * supports asynchronous and user-controlled demand receiving.
	 */
	public static Semaphore soReceiverControl;
	
	/**
	 * 0 - indicates sending and receiving demands asynchronously
	 * and concurrently. <br>
	 * 1 - indicates sending and receiving demands asynchronously
	 * under user control. <br>
	 * 2 - indicates sending and receiving demand synchronously. 
	 */
	public static int siMode = 0;
	
	public static final int OLD_MODE = 0;
	public static final int ASYN_CONTROL_MODE = 1;
	public static final int RESPONSE_TEST_MODE = 2;
	public static final int SPACE_SCALABILITY_TEST = 3;
	public static final int SPACE_TIME_TEST = 4;
	/**
	 * "End sign" for the entire system.
	 */
	public static boolean sbEnd;
	
	/**
	 * The time used by the threads to sleep in each cycle. 
	 */
	public static long siSleepTime;

	/**
	 * The Demand Dispatcher proxy. 
	 */	
	public	static IDemandDispatcher soDemandDispatcher;

	/**
	 * The demand class buffer.
	 * XXX: should be just a List interface, so can
	 * use any list, e.g. ArrayList, Vector, or others.
	 */ 
	public static LinkedList<String> soDemandClasses;

	/**
	 * The "run profiles" buffer.
	 * XXX: should be just a List interface, so can
	 * use any list, e.g. ArrayList, Vector, or others.
	 */ 
	public static LinkedList<String> soRunProfiles;

	/**
	 * The DGT dialog.
	 */ 	
	public static DGTDialog soDGTDialog;
	
	public static GUIThread soStatisticsUpdator;
	
	/*
	 * Demand statistics variables.
	 */
	public static volatile long slNumPendingDemands;
	public static volatile long slNumComputedDemands;
	public static volatile long slNumProcessedDemands;
	
	public static byte[] satDemandPayload = null;
	
	/*
	 * Initializes all the global static variables. 
	 */
//	static
//	{
//		sbEnd = false;
//		siSleepTime = 0;
//		slNumPendingDemands = 0;
//		slNumComputedDemands = 0;
//		slNumProcessedDemands = 0;
//		
//		sstrProfileDir = System.getProperty("user.dir") + "/" + GlobalDef.PROFILE_DIRECTORY;
//			
//		soSynchronizer = new Semaphore(false);
//		
//		soReceiverController = new Semaphore(false);
//		
//		soDemandClasses = new LinkedList<String>();
//		soRunProfiles = new LinkedList<String>();
//	
//		refreshClasses();
//		
//		createProfileDir();
//		
		// Determine which Demand Dispatcher to use.
//		try
//		{
//			Configuration oConfig = GIPSY.getConfugration();
//
//			// Load the configuration file.
//        	FileInputStream oFileIn = new FileInputStream(
//        			oConfig.getProperty(Configuration.CONFIGURATION_ROOT_PATH_KEY) + CONFIG_FILE);
//        	oConfig.loadFromXML(oFileIn);
//			soDemandDispatcher = 
//				(IDemandDispatcher) Class.forName(oConfig.getProperty(DEMAND_DISPATCHER_CLASS_KEY)).newInstance();
////			goDemandDispatcher = new JiniDemandDispatcher("","");
////			goDemandDispatcher = new JMSDemandDispatcher("","");
//		}
//		catch(Exception ex)
//		{
//			GlobalDef.handleCriticalException(ex);
//		}

//		try
//		{
//			soDGTDialog = new DGTDialog(null, ModalityType.MODELESS);
//		}
//		catch(Exception ex)
//		{
//			GlobalDef.handleCriticalException(ex);
//		}
//	}
	
	/**
	 * Reloads the class names.
	 */
	public static void refreshClasses()
	{
		loadClasses(DEMAND_CLASS_FILE, soDemandClasses);
		
		createResultDirs();
	}
	
	/**
	 * Loads the class names from the input file.
	 * 
	 * @param pstrClassFile is the input file
	 * @param poClassPool is the storage pool
	 */
	private static void loadClasses(String pstrClassFile, LinkedList<String> poClassPool)
	{
		FileInputStream oFileIn;
		BufferedReader oReaderFile;
		String strCurrLine;
		
		try
		{
			poClassPool.clear();

			// Load the demand class list.
        	oFileIn = new FileInputStream(GIPSY.getConfugration().getProperty(Configuration.CONFIGURATION_ROOT_PATH_KEY) + pstrClassFile);
			oReaderFile = new BufferedReader(new InputStreamReader(oFileIn, "ASCII"));
			
			strCurrLine = oReaderFile.readLine();

			while(strCurrLine != null)
			{
				poClassPool.add(strCurrLine);
				strCurrLine = oReaderFile.readLine();
			}

			oReaderFile.close(); 
			oFileIn.close();
		}
		catch(IOException ex)
		{
			handleCriticalException(ex);
		}
	}
	
	/**
	 * Creates the directories for storing the demands' result.
	 */
	private static void createResultDirs()
	{
		for(int i = 0; i < soDemandClasses.size(); ++i)
		{
			String strDirName = soDemandClasses.get(i);
		
			File oTD = new File(strDirName);

			if(!oTD.exists() && !oTD.isDirectory())
			{
				oTD.mkdir();
			}
		}
	}

	/**
	 * Creates the profile directory.
	 */
	private static void createProfileDir()
	{
		File oTD = new File(PROFILE_DIRECTORY);
		
		if(!oTD.exists() && !oTD.isDirectory())
		{
			oTD.mkdir();
		}
	}
	
	/**
	 * This method is called when the program throws an exception not allowing to continue.
	 * XXX: move to gipsy.util.ExceptionUtils?
	 * @param ex Throwable
	 */
	public static void handleCriticalException(Throwable ex) 
	{
		try
		{
			String strTrace = ex.getMessage();
			Object[] aoOptions = {"CLOSE"};

			JOptionPane.showOptionDialog
			(
				null, 
				"The program will terminate abnormally due to the critical error: " + strTrace, 
				"Error",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.ERROR_MESSAGE,
				null,
				aoOptions,
				aoOptions[0]
			);
			
			ex.printStackTrace(System.err);
			
			// XXX: hardcoded value; code repetition with catch()
			System.exit(1);
		}
		catch(Exception e)
		{
			// XXX: hardcoded value; code repetition
			System.exit(1);
		}
	}
	
	/**
	 * This method is called when the program throws a non-critical exception.
	 * XXX: move to gipsy.util.ExceptionUtils?
	 * @param ex Throwable
	 */
	public static void handleNonCriticalException(Throwable ex) 
	{
		try
		{
			String strTrace = ex.getMessage();
			Object[] aoOptions = {"CLOSE"};
			
			JOptionPane.showOptionDialog
			(
				null,
				strTrace,
				"Error", 
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.ERROR_MESSAGE,
				null,
				aoOptions,
				aoOptions[0]
			);
		}
		catch(Exception e)
		{
			// XXX: document why nothing handled or handle
		}
	}
	
	public static void setDemandDispatcher(DemandDispatcher poDemandDispatcher)
	{
		soDemandDispatcher = poDemandDispatcher;
	}
	
	public static void reset()
	{
		sbEnd = false;
		siSleepTime = 0;
		slNumPendingDemands = 0;
		slNumComputedDemands = 0;
		slNumProcessedDemands = 0;
		
		sstrProfileDir = System.getProperty("user.dir") + "/" + GlobalDef.PROFILE_DIRECTORY;
		
		if(soSynchronizer == null)
		{
			soSynchronizer = new Semaphore(false);
		}
		else
		{
			soSynchronizer.lock();
		}
		
		if(soReceiverControl == null)
		{
			soReceiverControl = new Semaphore(false);
		}
		else if(soReceiverControl != null)
		{
			soReceiverControl.lock();
		}
		
		
		soDemandClasses = new LinkedList<String>();
		soRunProfiles = new LinkedList<String>();
	
		refreshClasses();
		
		createProfileDir();

		try
		{
			soDGTDialog = new DGTDialog(null, ModalityType.MODELESS);
			soStatisticsUpdator = new GUIThread(soDGTDialog);
		}
		catch(Exception ex)
		{
			GlobalDef.handleCriticalException(ex);
		}
		
		// Clear the payload;
		satDemandPayload = null;
		
		// Clear all the pools as well
		DemandPool.getInstance().clear();
		DemandIDPool.instance().clear();
		ResultPool.getInstance().clear();
		
		System.gc();
	}
}

// EOF
