package gipsy.GEE.multitier.DST.rmi;


import java.rmi.*;
import java.rmi.server.*;
import java.rmi.Naming;










import gipsy.Configuration;


import gipsy.GEE.logger.Logger;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.DST.DSTWrapper;
import gipsy.util.Trace;




import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;






/**
 * A DST Wrapper who starts RMI services,
 * starts the tier
 */

public class RMIDSTWrapper extends DSTWrapper
{	
	// Constant configuration properties
//	private static final String STORE_STARUP_CMD = "gipsy.GEE.multitier.DST.jini.start.cmd";
//	private static final String THICK_STARTUP_CONFIG = "gipsy.GEE.multitier.DST.jini.start.config.thick";
//	private static final String THIN_STARTUP_CONFIG = "gipsy.GEE.multitier.DST.jini.start.config.thin";
//	private static final String JAVASPACE_CONFIGFILE = "gipsy.GEE.multitier.DST.jini.javaspace.fileName";
	private static final String LOOKUP_SERVICE_PORT = "gipsy.GEE.multitier.DST.rmi.port";
	private static final String HOSTNAME = "gipsy.GEE.multitier.DST.rmi.hostname";

	private Logger log = new Logger();
	private RMIDST obj=null;
	private RMITransportAgent oIRMITA =null;
	private Registry registry=null;
	/**
	 * The TA configuration exposed by this DST.
	 */
	private Configuration oTAConfig = null;

	/**
	 * The PID of the DST process started.
	 */
	private String strPID = null;



	/**
	 * For logging.
	 */
	private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";

	/**
	 * Create an instance based on configuration.
	 * 
	 * @param poDSTConfig - The DST configuration.
	 */
	public RMIDSTWrapper(Configuration poDSTConfig)
	{
		this.oConfiguration = poDSTConfig;
	}
    
	//Allocates RMI DST 
	public void startTier() 
	throws MultiTierException 
	{
		synchronized(this)
		{
			if(this.strPID != null)
			{   log.debug("Checking whether any RMI DST is running ");
				if(this.isDSTRunning())
				{
					// Simply return.
					System.out.println(MSG_PREFIX +" RMI DST is still running!");
					log.info(MSG_PREFIX + " RMI DST is still running");
					return;
				}
				else
				{
					System.out.println(MSG_PREFIX + " Old RMI DST dead, allocating new RMI DST ...");
					log.info(MSG_PREFIX + " Old RMI DST dead, allocating new RMI DST ... ");
				}
			 }
			 else
			 { 
				log.debug("No running RMI DST is found");
				System.out.println(MSG_PREFIX + "Allocating new RMI DST ...");
				log.info(MSG_PREFIX + " Allocating new RMI DST ... ") ; 
	   		 }
	     }
		
	  
	    try
		{
			 //String strDSTWorkingDir = this.oConfiguration.getProperty(DSTWrapper.DST_WORKING_DIR);
			//String strLookupServicePort = this.oConfiguration.getProperty(LOOKUP_SERVICE_PORT);
			int RMIPortNum = 1099;
			
			registry = LocateRegistry.createRegistry(RMIPortNum);
			System.out.println(registry.toString());
			System.out.println( "RMI registry successfully started.");
				 
			log.info("RMI started successfully");
				 
			obj = new RMIDST();
			log.info(obj.getClass().toString() + " " + "Object created");
			oIRMITA = new RMITransportAgent();
			log.info(oIRMITA.getClass().toString() + " " + "Object created");
				
				 
			Naming.rebind("lookUp", obj); //Re-binding the object for lookUp in registry.
			log.info(obj.getClass().toString() + " " + "Object bind to lookUp");
			Naming.rebind("oTA", oIRMITA );
			log.info(oIRMITA.getClass().toString() + " " + "Object bind to oTA");
				 			
		}
		catch(Exception e)
		{
			log.error(e.getClass().toString() + "Problem in starting registry for RMI");
		}
		
		

	}
	
	
	
	
	//Stops the tier
	
	public void stopTier()
	throws MultiTierException
	{
		
		synchronized(this)
		{
			// Assume we are only run in Windows or Linux like systems
			String strOSName = System.getProperty("os.name");
			
			String strKillProcessCMD = null;
			
			if(strOSName.contains("Windows") || strOSName.startsWith("Windows"))
			{
				strKillProcessCMD = "cmd /c taskkill /PID " + this.strPID;
				log.info(" storing Windows command for task kill");
			}
			else
			{
				strKillProcessCMD = "kill -9 " + this.strPID;
				log.info("storing  Linux command for task kill ");
			}
			
			try 
			{	
				Runtime.getRuntime().exec(strKillProcessCMD);
				
				// Only try 3 times
				int iCounter = 0;
				
				while(this.isDSTRunning())
				{
					if(iCounter < 3)
					{
						Thread.sleep(5000);
						log.info(" Make DST to sleep if it is more than three count ");
					}
					else
					{

						log.debug(" MultiTier Exception is thrown, since DST still running more than 3 times");
						throw new MultiTierException("The DST is still running!");
					}
					
					iCounter++;
				}
			} 
			catch(IOException oException) 
			{  
			
				oException.printStackTrace(System.err);
				log.error(oException.getClass().toString() +" throwing IO exception");
				throw new MultiTierException(oException);
			} 
			catch(InterruptedException oException) 
			{
				oException.printStackTrace(System.err);
				log.error(oException.getClass().toString() +" throwing Interrupted exception");
				throw new MultiTierException(oException);
			}
			
			this.strPID = null;
		}
	}

	public synchronized Configuration exportTAConfig() 
	{
		return this.oTAConfig;
	}
	
	/** check whether or not DST is running
	 * @return boolean 
	 */
	private boolean isDSTRunning()
	{
		boolean bIsDSTRunning = false;
		    	bIsDSTRunning = true;
			
		log.info("DST running :" + String.valueOf(bIsDSTRunning) );
		
		return bIsDSTRunning;
	}
	
}


