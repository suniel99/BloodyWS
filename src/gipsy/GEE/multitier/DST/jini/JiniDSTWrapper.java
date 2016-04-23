package gipsy.GEE.multitier.DST.jini;

import gipsy.Configuration;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.IJiniServiceDiscoverer;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITA;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.UnicastJiniServiceDiscoverer;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.DST.DSTWrapper;
import gipsy.util.Trace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import marf.util.Debug;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceID;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceMatches;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.lookup.entry.Name;
import net.jini.space.JavaSpace;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.spi.AttachProvider;


/**
 * A DST Wrapper who starts Jini services that are exported by JRMP 
 * (explained as classic Java RMI but no idea why called JRMP). JRMP
 * does not allow user to determine service port, therefore when a
 * service is restarted, the old proxy and service ID becomes invalid, 
 * therefore this DSW Wrapper must report GMT the new service ID and
 * the DWT or DGT who holds the old remote reference must retrieve the 
 * updated service ID from GMT to connect to the restarted service. 
 * Although more complex in dealing with service restart, JRMP offers
 * faster speed in the JavaSpace operations than JERI.
 * 
 * @author Yi Ji
 * @version $Id: JiniDSTWrapper.java,v 1.28 2012/06/14 03:24:59 mokhov Exp $
 */
public class JiniDSTWrapper 
extends DSTWrapper 
{	
	// Constant configuration properties
	private static final String STORE_STARUP_CMD = "gipsy.GEE.multitier.DST.jini.start.cmd";
	private static final String THICK_STARTUP_CONFIG = "gipsy.GEE.multitier.DST.jini.start.config.thick";
	private static final String THIN_STARTUP_CONFIG = "gipsy.GEE.multitier.DST.jini.start.config.thin";
	private static final String JAVASPACE_CONFIGFILE = "gipsy.GEE.multitier.DST.jini.javaspace.fileName";
	private static final String LOOKUP_SERVICE_PORT = "gipsy.GEE.multitier.DST.jini.unicast.port";
	private static final String TRANSACTION_CONFIGFILE = "gipsy.GEE.multitier.DST.jini.transaction.fileName";

	/* 
	 * The prepared content of the configuration file of 
	 * Jini service implementation such as outrigger or mahalo
	 */
	private static final String PREPARED_SERVICE_CONFIG = 
		"//import com.sun.jini.outrigger.snaplogstore.LogStore;\r\n" +
		"import net.jini.jrmp.JrmpExporter;\r\n" +
		"import net.jini.core.entry.Entry;\r\n" +
		"import net.jini.lookup.entry.Name;\r\n\r\n" + 
		"? {\r\n" +
		"    initialLookupAttributes = new Entry[] {new Name(\"?\")};\r\n" + 
		"    initialLookupGroups = new String[] { \"gipsy\" };\r\n" + 
		"    //store = new LogStore(this);\r\n" +
		"    //persistenceDirectory = \"?.log\";\r\n" +
		"    serverExporter = new JrmpExporter();\r\n" +
		"}";

	private static final String JAVASPACE_IMPL_CLASS = "com.sun.jini.outrigger";
	private static final String TRANSACTION_IMPL_CLASS = "com.sun.jini.mahalo";

	/**
	 * The TA configuration exposed by this DST.
	 */
	private Configuration oTAConfig = null;

	/**
	 * The PID of the DST process started.
	 */
	private String strPID = null;

	/**
	 * The lookup name of the services of the DST.
	 */
	private String strServiceLookupName = null;

	/**
	 * The service ID allocated by the lookup service (reggie)
	 */
	private Map<ServiceID, Boolean> oOldJavaspaceServiceIDs = new HashMap<ServiceID, Boolean>();
	private Map<ServiceID, Boolean> oOldTXManagerServiceIDs = new HashMap<ServiceID, Boolean>();

	/**
	 * For logging.
	 */
	private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";

	/**
	 * Create an instance based on configuration.
	 * 
	 * @param poDSTConfig - The DST configuration.
	 */
	public JiniDSTWrapper(Configuration poDSTConfig)
	{
		this.oConfiguration = poDSTConfig;
	}


	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.DST.DSTWrapper#startTier()
	 */
	public void startTier() 
	throws MultiTierException 
	{
		synchronized(this)
		{
			if(this.strPID != null)
			{
				if(this.isDSTRunning())
				{
					// Simply return.
					System.out.println(MSG_PREFIX + "Jini DST is still running!");
					return;
				}
				else
				{
					System.out.println(MSG_PREFIX + "Old Jini DST dead, allocating new Jini DST ...");
				}
			}
			else
			{
				System.out.println(MSG_PREFIX + "Allocating new Jini DST ...");
			}
		}
		
		try
		{
			String strDSTWorkingDir = this.oConfiguration.getProperty(DSTWrapper.DST_WORKING_DIR);
			String strDSTLaunchCMD = this.oConfiguration.getProperty(STORE_STARUP_CMD);
			String strThickStartConfig = this.oConfiguration.getProperty(THICK_STARTUP_CONFIG);
			String strThinStartConfig = this.oConfiguration.getProperty(THIN_STARTUP_CONFIG);
			String strJavaSpaceConfigFileName = this.oConfiguration.getProperty(JAVASPACE_CONFIGFILE);
			String strTransactionConfigFileName = this.oConfiguration.getProperty(TRANSACTION_CONFIGFILE);
			String strLookupServicePort = this.oConfiguration.getProperty(LOOKUP_SERVICE_PORT);
			this.strServiceLookupName = this.oConfiguration.getProperty(JINITA.DST_SERVICE_NAME);
			String strLookupServiceURI = "jini://" + InetAddress.getLocalHost().getHostName();
			
			//XXX TODO Configuration value validation
			
			ServiceID oJavaspaceServiceID = null;
			ServiceID oTXManagerServiceID = null;
			
			String strStartupConfig = null;
			
			//Test to see if the lookup service in this host is already running
			
			if(strLookupServicePort != null && strLookupServicePort.length() > 0)
			{
				strLookupServiceURI = strLookupServiceURI + ":" + strLookupServicePort + "/";
			}
			else
			{
				strLookupServiceURI = strLookupServiceURI + "/";
			}
			
			
			LookupLocator oLocator = new LookupLocator(strLookupServiceURI);
			
			boolean bIsReggieProvider = false;
			
			try
			{
				ServiceRegistrar oRegistrar = oLocator.getRegistrar();

				if(oRegistrar == null)
				{
					throw new java.net.ConnectException("Reggie is not running!");
				}
				
				/*
				 * If the registrar can be found -> lookup service is running,
				 * thin config file will be used to start only javaspace and
				 * transaction
				 */
				
				strStartupConfig = strThinStartConfig;
			}
			//catch(java.net.ConnectException e)
			catch(Exception e)
			{
				/* 
				 * If the lookup service is not available, use the thick startup 
				 * configuration file to start up a httpd, a lookup service, 
				 * a javaspace and a transaction service.
				 */
				strStartupConfig = strThickStartConfig;
				bIsReggieProvider = true;
			}
			
			// If a lookup name is specified in the configuration or used before
			if(this.strServiceLookupName == null || this.strServiceLookupName.trim().isEmpty())
			{
				/* 
				 * A new name is generated for the services based on tier ID. 
				 * This is based on the assumption that the tier ID has already 
				 * been designated before startTier() is called.
				 */
				this.strServiceLookupName = InetAddress.getLocalHost().getHostName() + "-" + this.strTierID;
			}
			
			Debug.debug(MSG_PREFIX + "Jini Service lookup name = " + this.strServiceLookupName);
			
			// Get the startup command
			strDSTLaunchCMD = strDSTLaunchCMD + " " + strStartupConfig;
			String[] astrCommand = strDSTLaunchCMD.split("\\s+");

			Debug.debug(MSG_PREFIX + "Jini startup command = " + strDSTLaunchCMD);
			
			// Build the process.
			ProcessBuilder oProcBuilder = new ProcessBuilder(astrCommand);
			//ProcessBuilder oProcBuilder = new ProcessBuilder("cmd", "/c", "start", "java", "-version");
			
			File oDSTWorkingDir = new File(strDSTWorkingDir);
			oProcBuilder.directory(oDSTWorkingDir);
			
			// Create a config file with auto-generated java space name
			strJavaSpaceConfigFileName
				= oDSTWorkingDir.getAbsolutePath() 
				+ File.separator
				+ strJavaSpaceConfigFileName;
			
			strTransactionConfigFileName
				= oDSTWorkingDir.getAbsolutePath() 
				+ File.separator
				+ strTransactionConfigFileName;
			
			// Read the start up configuration file to see if persistent is required.
			strStartupConfig
				= oDSTWorkingDir.getAbsolutePath() 
				+ File.separator
				+ strStartupConfig;
			
			boolean bIsPersistenceRequired = isPersistenceRequired(strStartupConfig);

			Debug.debug(MSG_PREFIX + "bIsPersistenceRequired = " + bIsPersistenceRequired);

			generateServiceImplConfig
			(
				strJavaSpaceConfigFileName, 
				JAVASPACE_IMPL_CLASS, 
				this.strServiceLookupName,
				bIsPersistenceRequired
			);
			
			Debug.debug(MSG_PREFIX + "strJavaSpaceConfigFileName = " + strJavaSpaceConfigFileName);
			Debug.debug(MSG_PREFIX + "JAVASPACE_IMPL_CLASS = " + JAVASPACE_IMPL_CLASS);
			Debug.debug(MSG_PREFIX + "this.strServiceLookupName = " + this.strServiceLookupName);

			generateServiceImplConfig
			(
				strTransactionConfigFileName, 
				TRANSACTION_IMPL_CLASS, 
				this.strServiceLookupName,
				bIsPersistenceRequired
			);
			
			Debug.debug(MSG_PREFIX + "2 strTransactionConfigFileName = " + strTransactionConfigFileName);
			Debug.debug(MSG_PREFIX + "2 TRANSACTION_IMPL_CLASS = " + TRANSACTION_IMPL_CLASS);
			Debug.debug(MSG_PREFIX + "2 this.strServiceLookupName = " + this.strServiceLookupName);
			
			Debug.debug(MSG_PREFIX + "settings: ");
			System.getProperties().list(System.err);

			// Snapshot the current JVM process running in the system.
			//String strDisplayName = STORE_PROCESS_NAME + " " + strStartupArg;
	
			Debug.debug(MSG_PREFIX + "providers = " + AttachProvider.providers());
			
			List<VirtualMachineDescriptor> oVMs = VirtualMachine.list();

			Debug.debug(MSG_PREFIX + "VMs = " + oVMs);
			
			Map<String, Object> oPIDs = new HashMap<String, Object>();
			
			for(VirtualMachineDescriptor oVMDescriptor: oVMs) 
			{
				Debug.debug(MSG_PREFIX + "VM ID = " + oVMDescriptor.id() + ", VM name = " + oVMDescriptor.displayName());
				oPIDs.put(oVMDescriptor.id(), oVMDescriptor.displayName());
			}
			
			// Start the process
			// Runtime.getRuntime().exec("cmd");
			Process oDSTStarter = oProcBuilder.start();
			
			Thread.yield();
			
			// Get the newly launched JVM process ID (as Jini services are a JVM process)
	        for(int i = 0; i < 10; i++)
	        {
	        	oVMs = VirtualMachine.list();
	        	
				for(VirtualMachineDescriptor oVMDescriptor: oVMs) 
				{
					if(!oPIDs.containsKey(oVMDescriptor.id()))
					{
						this.strPID = oVMDescriptor.id();
						break;
					}
				}
			
				if(this.strPID == null)
				{
					Thread.sleep(2000);
				}
				else
				{
					break;
				}
	        }
	        
	        if(this.strPID == null)
	        {
	        	oDSTStarter.destroy();
	        	throw new MultiTierException("Javaspace could not be started!");
	        }
	        else
	        {
	        	Debug.debug(MSG_PREFIX + "DST " + this.strTierID + " PID: " + this.strPID);
	        }
	        
	        
	        // Now ensure the Javaspace is fully prepared.
	        
	        /* 
	         * Prepare the old service id list to record if it has been found
	         * in this round.
	         */
	        
	        Thread.sleep(1500);
	        
	        
	        Class<?>[] aoServiceTypes = new Class[] {JavaSpace.class};
			
			net.jini.core.entry.Entry[] aoAttributes = 
				new net.jini.core.entry.Entry[] {new Name(this.strServiceLookupName)};
			
			ServiceTemplate oTemplate = new ServiceTemplate(null, aoServiceTypes, aoAttributes);
	        
	        // XXX: hardcoded 10 is a number of retries?
	        for(int i = 0; i < 10; i++)
	        {
	        	try
	        	{
	        		IJiniServiceDiscoverer oDiscoverer = new UnicastJiniServiceDiscoverer();
	        		
	        		ServiceMatches oMatches = oDiscoverer.getServices(oTemplate, strLookupServiceURI);
	        		
	        		ServiceItem[] oServiceItems = oMatches.items;
	    			
	        		boolean bIsNewDSTFound = false;
	        		
	    			for(int j = 0; j < oServiceItems.length; j++)
	    			{
	    				ServiceItem oServiceItem = oServiceItems[j];
	    				
	    				if(oServiceItem == null || oServiceItem.serviceID == null)
	    				{
	    					continue;
	    				}
	    				
	    				ServiceID oDiscoveredServiceID = oServiceItem.serviceID;
	    				
	    				if(this.oOldJavaspaceServiceIDs.containsKey(oDiscoveredServiceID))
	    				{
	    					// Record this ID has been found in this round
	    					this.oOldJavaspaceServiceIDs.put(oDiscoveredServiceID, true);
	    					continue;
	    				}
	    				
	    				bIsNewDSTFound = true;
						
	    				oJavaspaceServiceID = oDiscoveredServiceID;
	    			} // end of service items
	        		
	    			if(bIsNewDSTFound == true)
	    			{
	    				break;
	    			}
	    			else
	    			{
	    				// XXX: hardcoded sleep time between retries?
	    				Thread.sleep(2000);
	    			}
				}
				catch(Exception oException)
				{
					// XXX: hardcoded 4 sleeps for retries when exceptions occur?
					if(i < 4)
					{
						// XXX: another hardcoded sleep
						Thread.sleep(2000);
					}
					else
					{
						//System.getProperties().list(System.err);
						oException.printStackTrace(System.err);
						throw new MultiTierException("Javaspace could not be prepared!");
					}
				}
			}
	        
	        aoServiceTypes = new Class[] {TransactionManager.class};
	        oTemplate = new ServiceTemplate(null, aoServiceTypes, aoAttributes);
	        
	        for(int i = 0; i < 5; i++)
	        {
	        	try
	        	{
	        		IJiniServiceDiscoverer oDiscoverer = new UnicastJiniServiceDiscoverer();
	        		ServiceMatches oMatches = oDiscoverer.getServices(oTemplate, strLookupServiceURI);
	
	        		ServiceItem[] oServiceItems = oMatches.items;
	    			
	        		boolean bIsNewDSTFound = false;
	        		
	    			for(int j = 0; j < oServiceItems.length; j++)
	    			{
	    				ServiceItem oServiceItem = oServiceItems[j];
	
	    				if(oServiceItem == null || oServiceItem.serviceID == null)
	    				{
	    					continue;
	    				}
	    				
	    				ServiceID oDiscoveredServiceID = oServiceItem.serviceID;
	    				
	    				if(this.oOldTXManagerServiceIDs.containsKey(oDiscoveredServiceID))
	    				{
	    					// Record this ID has been found in this round
	    					this.oOldTXManagerServiceIDs.put(oDiscoveredServiceID, true);
	    					continue;
	    				}
	    				
	    				bIsNewDSTFound = true;
						
	    				oTXManagerServiceID = oDiscoveredServiceID;
	    			}
	        		
	    			if(bIsNewDSTFound)
	    			{
	    				break;
	    			}
	    			else
	    			{
	    				Thread.sleep(2000);
	    			}
	        	}
	        	catch(Exception oException)
	        	{
	        		if(Debug.isDebugOn())
	        		{
	        			oException.printStackTrace(System.err);
	        		}
	        		
	        		if(i < 4)
	        		{
	        			Thread.sleep(2000);
	        		}
	        		else
	        		{
	        			System.getProperties().list(System.err);
	        			oException.printStackTrace(System.err);
	        			throw new MultiTierException("Javaspace could not be started!");
	        		}
	        	}
	        }
	        
	        // Now clear the undiscovered old service IDs
	        Set<Entry<ServiceID, Boolean>> oRecords = this.oOldJavaspaceServiceIDs.entrySet();
	        Iterator<Entry<ServiceID, Boolean>> oRecordIterator = oRecords.iterator();
	        
	        while(oRecordIterator.hasNext())
	        {
	        	Entry<ServiceID, Boolean> oRecord = oRecordIterator.next();
	        	
	        	if(oRecord.getValue().equals(Boolean.FALSE))
	        	{
	        		oRecordIterator.remove();
	        	}
	        	else
	        	{
	        		oRecord.setValue(Boolean.FALSE);
	        	}
	        }
	        
	        oRecords = this.oOldTXManagerServiceIDs.entrySet();
	        oRecordIterator = oRecords.iterator();
	        
	        while(oRecordIterator.hasNext())
	        {
	        	Entry<ServiceID, Boolean> oRecord = oRecordIterator.next();
	        	
	        	if(oRecord.getValue().equals(Boolean.FALSE))
	        	{
	        		oRecordIterator.remove();
	        	}
	        	else
	        	{
	        		oRecord.setValue(Boolean.FALSE);
	        	}
	        }
	        
	        
	        Debug.debug(MSG_PREFIX + "JiniDSTManager: New javaspace id " + oJavaspaceServiceID);
	        Debug.debug(MSG_PREFIX + "JiniDSTManager: New txmanager id " + oTXManagerServiceID);
	        
	        // Update this configuration with service lookup name
	    	System.out.println(MSG_PREFIX + "Jini DST started, PID = " + this.strPID);
	    	this.oConfiguration.setProperty(JINITA.DST_SERVICE_NAME, this.strServiceLookupName);
	        
	    	if(!bIsReggieProvider && !bIsPersistenceRequired)
	    	{
	    		this.oOldJavaspaceServiceIDs.put(oJavaspaceServiceID, false);
	    		this.oOldTXManagerServiceIDs.put(oTXManagerServiceID, false);
	    		this.oConfiguration.setProperty(JINITA.DST_JAVASPACE_SERVICE_ID, oJavaspaceServiceID.toString());
	            this.oConfiguration.setProperty(JINITA.DST_TXMANAGER_SERVICE_ID, oTXManagerServiceID.toString());
	    	}
	    	
	        // Generate the TA configuration for the new DST instance.
	        synchronized(this)
	        {
	        	this.oTAConfig = new Configuration();
	            this.oTAConfig.setProperty(ITransportAgent.TA_IMPL_CLASS, JINITA.class.getCanonicalName());
	            
	            // Transfer all TA relevant properties to the TA configuration.
	            
	            Set<Entry<Object, Object>> oProperties = this.oConfiguration.getConfigurationSettings().entrySet();
	            
	            Iterator<Entry<Object, Object>> oIter = oProperties.iterator();
	            
	            while(oIter.hasNext())
	            {
	            	Entry<Object, Object> oEntry = oIter.next();
	            	
	            	String strProperty = (String)oEntry.getKey();
	            	
	            	if(strProperty.startsWith("gipsy.GEE.TA.jini"))
	            	{
	            		this.oTAConfig.setProperty(strProperty, (String)oEntry.getValue());
	            	}
	            }
	            
	            // Overwrite or add updated properties.
	            this.oTAConfig.setProperty(JINITA.UNICAST_DISCOVERY_URI, strLookupServiceURI);
	            
	            if(bIsPersistenceRequired)
	            {
	            	this.oTAConfig.setProperty(JINITA.IS_JERI, Boolean.TRUE.toString());
	            }
	        }
		}
		catch(Exception oException)
		{
			oException.printStackTrace(System.err);
			throw new MultiTierException(oException);
		}
	}

	public void stopTier()
	throws MultiTierException
	{
		/*
		 * The logic below kills any process using this PID
		 */
		synchronized(this)
		{
			// Assume we are only run in Windows or Linux like systems
			String strOSName = System.getProperty("os.name");
			
			String strKillProcessCMD = null;
			
			if(strOSName.contains("Windows") || strOSName.startsWith("Windows"))
			{
				strKillProcessCMD = "cmd /c taskkill /PID " + this.strPID;
			}
			else
			{
				strKillProcessCMD = "kill -9 " + this.strPID;
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
					}
					else
					{
						throw new MultiTierException("The DST is still running!");
					}
					
					iCounter++;
				}
			} 
			catch(IOException oException) 
			{
				oException.printStackTrace(System.err);
				throw new MultiTierException(oException);
			} 
			catch(InterruptedException oException) 
			{
				oException.printStackTrace(System.err);
				throw new MultiTierException(oException);
			}
			
			this.strPID = null;
		}
	}

	public synchronized Configuration exportTAConfig() 
	{
		return this.oTAConfig;
	}
	
	private void generateServiceImplConfig
	(
		String pstrConfigFileName, 
		String pstrServiceImplClassName, 
		String pstrServiceName,
		boolean pbIsPersistent
	) 
	throws IOException
	{
		// Prepare the content of the configuration file
		StringBuilder oPreparedConfig = new StringBuilder(PREPARED_SERVICE_CONFIG);
		
		// Set the implementation class
		int iParamPosition = oPreparedConfig.indexOf("?");
		oPreparedConfig.replace(iParamPosition, iParamPosition + 1, pstrServiceImplClassName);
		
		// Set the service name
		iParamPosition = oPreparedConfig.indexOf("?", iParamPosition + 1);
		oPreparedConfig.replace(iParamPosition, iParamPosition + 1, pstrServiceName);
		
		// Set the persistence log file name
		File oFile = new File(pstrConfigFileName);
		String strLogFileName = oFile.getName() + pstrServiceName; 
		iParamPosition = oPreparedConfig.indexOf("?", iParamPosition + 1);
		oPreparedConfig.replace(iParamPosition, iParamPosition + 1, strLogFileName);
		
		// Now if persistence required
		
		if(pbIsPersistent)
		{
			Debug.debug(MSG_PREFIX + "Generate persistence properties.");
			
			// Find the first comment mark "//"
			iParamPosition = oPreparedConfig.indexOf("//");
			if(pstrServiceImplClassName.equals(JAVASPACE_IMPL_CLASS))
			{
				// Remove the first comments
				oPreparedConfig.replace(iParamPosition, iParamPosition + 2, "");
			}
			
			// Now the second comment mark "//"
			iParamPosition = oPreparedConfig.indexOf("//", iParamPosition + 2);
			if(pstrServiceImplClassName.equals(JAVASPACE_IMPL_CLASS))
			{
				// Remove the second comments
				oPreparedConfig.replace(iParamPosition, iParamPosition + 2, "");
			}
			
			// Now the third comment mark "//"
			iParamPosition = oPreparedConfig.indexOf("//", iParamPosition + 1);
			oPreparedConfig.replace(iParamPosition, iParamPosition + 2, "");
		}
		
		// Now write the file.
		FileWriter oFileWriter = new FileWriter(pstrConfigFileName + ".config");
		oFileWriter.write(oPreparedConfig.toString());
		oFileWriter.flush();
		oFileWriter.close();
	}

	private boolean isPersistenceRequired(String pstrConfigFileName)
	throws IOException
	{
		BufferedReader oReader = new BufferedReader(new FileReader(pstrConfigFileName));
		
		String strLine = null;
		
		while((strLine = oReader.readLine()) != null)
		{
			if(strLine.contains("com.sun.jini.outrigger.PersistentOutriggerImpl"))
			{
				Debug.debug(MSG_PREFIX + "Starting persitent DST.");
				oReader.close();
				return true;
			}
		}
	
		oReader.close();
		Debug.debug(MSG_PREFIX + "Starting transient DST.");
		return false;
	}
	
	private boolean isDSTRunning()
	{
		boolean bIsDSTRunning = false;
		
		List<VirtualMachineDescriptor> vms = VirtualMachine.list();
		
		for(VirtualMachineDescriptor vmd: vms) 
		{
			if(vmd.id().equals(this.strPID))
			{
				bIsDSTRunning = true;
				break;
			}
		}
		
		return bIsDSTRunning;
	}
	
}

// EOF
