package gipsy.GEE.multitier.DST.jini;

import gipsy.Configuration;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITA;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.IJiniServiceDiscoverer;
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
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.Set;

import marf.util.Debug;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.Transaction.Created;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.lookup.entry.Name;
import net.jini.space.JavaSpace;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

/**
 * A DST Wrapper who starts Jini services that are exported by Jini ERI 
 * (Extensible Remote Invocation, JERI for short). JERI allows programmer
 * to set the service port and the UUID of the service proxy so that when
 * the service is restarted, the old service proxy is still valid to locate
 * the service, which may allow the DSTWrapper to export a simpler TA. 
 * However, the speed of the JavaSpace operations invoked via JERI is mush 
 * slower than that invoked via JRMP (classic Java RMI, see JiniDSTManager). 
 * Note that only the transaction manager (mahalo) and javaspace (outrigger) 
 * services are using Jini ERI exporter; the lookup service (reggie) still
 * uses JRMP because it is not duplicated within a GIPSY node and JERI 
 * is not needed in this case.
 * 
 * @author Yi Ji
 * @version $Id: JiniERIDSTWrapper.java,v 1.1 2011/01/04 16:32:07 ji_yi Exp $
 */
public class JiniERIDSTWrapper 
extends DSTWrapper 
{
	// Constant configuration properties
	private static final String STORE_STARUP_CMD = "gipsy.GEE.multitier.DST.jini.start.cmd";
	private static final String THICK_STARTUP_CONFIG = "gipsy.GEE.multitier.DST.jini.start.config.thick";
	private static final String THIN_STARTUP_CONFIG = "gipsy.GEE.multitier.DST.jini.start.config.thin";
	private static final String LOOKUP_SERVICE_PORT = "gipsy.GEE.multitier.DST.jini.unicast.port";
	private static final String JAVASPACE_CONFIGFILE = "gipsy.GEE.multitier.DST.jini.javaspace.fileName";
	private static final String TRANSACTION_CONFIGFILE = "gipsy.GEE.multitier.DST.jini.transaction.fileName";
	
	/** 
	 * The prepared content of the configuration files of 
	 * JavaSpace implementation (outrigger) and TransactionManager 
	 * implementation (mahalo)
	 */
	private static final String PREPARED_SERVICE_CONFIG = 
		"//import com.sun.jini.outrigger.snaplogstore.LogStore;\r\n" +
		"import net.jini.core.entry.Entry;\r\n" +
		"import net.jini.lookup.entry.Name;\r\n" +
		"import net.jini.id.UuidFactory;\r\n" + 
		"import net.jini.jeri.BasicILFactory;\r\n" + 
		"import net.jini.jeri.BasicJeriExporter;\r\n" + 
		"import net.jini.jeri.tcp.TcpServerEndpoint;\r\n\r\n" + 
		"? {\r\n" +
		"    initialLookupAttributes = new Entry[] {new Name(\"?\")};\r\n" + 
		"    initialLookupGroups = new String[] { \"gipsy\" };\r\n" + 
		"//    store = new LogStore(this);\r\n" +
		"//    persistenceDirectory = \"?.log\";\r\n" + 
		"    serverExporter = new BasicJeriExporter(\r\n" +
		"        TcpServerEndpoint.getInstance(?),\r\n" +
		"        new BasicILFactory(),\r\n" +
		"        false,\r\n" +
		"        true,\r\n" +
		"        UuidFactory.create(\"?\"));\r\n" + 
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
	 * The JavaSpace service proxy ID. Note that this is
	 * not the service ID, but the service proxy's ID.
	 */
	private String strJavaspaceProxyID = null;
	
	/**
	 * The TransactionManager service proxy ID. Note that this is
	 * not the service ID, but the service proxy's ID.
	 */
	private String strTXManagerProxyID = null;
	
	private int iJavaspacePort = 0;
	private int iTXManagerPort = 0;
	
	/**
	 * For logging
	 */
	private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";
	
	
	/**
	 * Create an instance based on configuration.
	 * 
	 * @param poDSTConfig - The DST configuration.
	 */
	public JiniERIDSTWrapper(Configuration poDSTConfig)
	{
		this.oConfiguration = poDSTConfig;
	}
	
	
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
					return;
				}
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
			catch(java.net.ConnectException e)
			{
				/* 
				 * If the lookup service is not available, use the thick startup 
				 * configuration file to start up a httpd, a lookup service, 
				 * a javaspace and a transaction service.
				 */
				strStartupConfig = strThickStartConfig;
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
			
			// Generate service ports
			
			if(this.iJavaspacePort == 0)
			{
				ServerSocket oPortFinder = new ServerSocket(0);
				this.iJavaspacePort = oPortFinder.getLocalPort();
				oPortFinder.close();
			}
			
			if(this.iTXManagerPort == 0)
			{
				ServerSocket oPortFinder = new ServerSocket(0);
				this.iTXManagerPort = oPortFinder.getLocalPort();
				oPortFinder.close();
			}
			
			// Generate service UUID
			if(this.strJavaspaceProxyID == null)
			{
				this.strJavaspaceProxyID = UUID.randomUUID().toString();
			}
			
			if(this.strTXManagerProxyID == null)
			{
				this.strTXManagerProxyID = UUID.randomUUID().toString();
			}
			
			// Get the starup command
			strDSTLaunchCMD = strDSTLaunchCMD + " " + strStartupConfig;
			String[] astrCommand = strDSTLaunchCMD.split("\\s+");
			
			// Build the process.
			ProcessBuilder oProcBuilder = new ProcessBuilder(astrCommand);
			//ProcessBuilder oProcBuilder = new ProcessBuilder("cmd", "/c", "start", "java", "-version");
			File oDSTWorkingDir = new File(strDSTWorkingDir);
			oProcBuilder.directory(oDSTWorkingDir);
			
			//Create a config file with auto-generated java space name
			strJavaSpaceConfigFileName = oDSTWorkingDir.getAbsolutePath() 
										+ File.separator
										+ strJavaSpaceConfigFileName;
			
			strTransactionConfigFileName = oDSTWorkingDir.getAbsolutePath() 
										+ File.separator
										+ strTransactionConfigFileName;
			
			// Read the start up configuration file to see if persistent is required.
			strStartupConfig = oDSTWorkingDir.getAbsolutePath() 
									+ File.separator
									+ strStartupConfig;
			
			boolean bIsPersistenceRequired = this.isPersistenceRequired(strStartupConfig);
			
			this.generateServiceImplConfig(strJavaSpaceConfigFileName, 
					JAVASPACE_IMPL_CLASS, 
					this.strServiceLookupName,
					this.iJavaspacePort,
					this.strJavaspaceProxyID,
					bIsPersistenceRequired);
			
			this.generateServiceImplConfig(strTransactionConfigFileName, 
					TRANSACTION_IMPL_CLASS, 
					this.strServiceLookupName,
					this.iTXManagerPort,
					this.strTXManagerProxyID,
					bIsPersistenceRequired);
			
			
			// Snapshot the current JVM process running in the system.
			//String strDisplayName = STORE_PROCESS_NAME + " " + strStartupArg;

			List<VirtualMachineDescriptor> vms = VirtualMachine.list();
			Map<String, Object> oPIDs = new HashMap<String, Object>();
			
			for(VirtualMachineDescriptor vmd: vms) 
			{
				oPIDs.put(vmd.id(), vmd.displayName());
			}
			
			// Start the process
			Process oDSTStarter = oProcBuilder.start();
			
			Thread.yield();
			
			// Get the newly launched JVM process ID
            for(int i = 0; i<10; i++)
            {
            	vms = VirtualMachine.list();
				for (VirtualMachineDescriptor vmd: vms) 
				{
					if(!oPIDs.containsKey(vmd.id()))
					{
						this.strPID = vmd.id();
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
            
            
            // Now ensure the Jini services are fully prepared.
            Thread.sleep(1500);
            
            Debug.debug(MSG_PREFIX + "Testing javaspace");
            
            Class<?>[] aoJavaSpaceTypes = new Class[] {JavaSpace.class};
            Class<?>[] aoTXManagerTypes = new Class[] {TransactionManager.class};
			
            net.jini.core.entry.Entry[] aoAttributes = 
				new net.jini.core.entry.Entry[] {new Name(this.strServiceLookupName)};
			
			ServiceTemplate oJavaspaceTemplate = 
				new ServiceTemplate(null, aoJavaSpaceTypes, aoAttributes);
			ServiceTemplate oTXManagerTemplate = 
				new ServiceTemplate(null, aoTXManagerTypes, aoAttributes);
			
            PingSignal oPing = new PingSignal();
            oPing.oID = UUID.randomUUID();
			
            for(int i = 0; i<10; i++)
            {
            	try
            	{
            		IJiniServiceDiscoverer oDiscoverer = new UnicastJiniServiceDiscoverer();
            		
            		TransactionManager oTXManager = 
            			(TransactionManager) oDiscoverer.getService(oTXManagerTemplate, strLookupServiceURI);
            		JavaSpace oJavaspace = 
            			(JavaSpace) oDiscoverer.getService(oJavaspaceTemplate, strLookupServiceURI);
            		
            		if(oTXManager == null || oJavaspace == null)
            		{
            			Debug.debug(MSG_PREFIX + "Testing javaspace: TxnManager = " + oTXManager + " JavaSpace = " + oJavaspace);
            			throw new MultiTierException("Jini services could not be found!");
            		}
            		else
            		{  
            			Created oCreated = null;
            			oCreated = TransactionFactory.create(oTXManager, 5000);
            			Transaction oTransaction = oCreated.transaction;
            			Debug.debug(MSG_PREFIX + "Testing javaspace: txn created!");
            			oJavaspace.write(oPing, oTransaction, 5000);
            			Debug.debug(MSG_PREFIX + "Testing javaspace: pinged!");
            			PingSignal oFeedback = (PingSignal)oJavaspace.take(oPing, oTransaction, 2000);
            			
            			if(oFeedback == null)
            			{
            				Debug.debug(MSG_PREFIX + "Testing javaspace: ping failed!");
            				throw new MultiTierException("Ping failed!");
            			}
            			else
            			{
            				oTransaction.commit();
            				Debug.debug(MSG_PREFIX + "Testing javaspace: ping done!");
            				break;
            			}
            		}
            	}
            	catch(Exception oException)
            	{
            		if(i<9)
            		{
            			Thread.sleep(2000);
            		}
            		else
            		{
            			oException.printStackTrace(System.err);
            			throw new MultiTierException("Javaspace could not be prepared!");
            		}
            	}
            }
                   
            // Update this configuration with service lookup name
        	System.out.println(MSG_PREFIX + "Jini DST started, PID = " + this.strPID);
        	
        	this.oConfiguration.setProperty(JINITA.DST_SERVICE_NAME, 
        			this.strServiceLookupName);
        	
            // Generate the TA configuration for the new DST instance.
            synchronized(this)
            {
            	this.oTAConfig = new Configuration();
                this.oTAConfig.setProperty(ITransportAgent.TA_IMPL_CLASS, 
                		JINITA.class.getCanonicalName());
                
                // Transfer all TA relevant properties to the TA configuration.
                
                Set<Entry<Object, Object>> oProperties = 
                	this.oConfiguration.getConfigurationSettings().entrySet();
                
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
                this.oTAConfig.setProperty(JINITA.UNICAST_DISCOVERY_URI, 
                		strLookupServiceURI);
                this.oTAConfig.setProperty(JINITA.IS_JERI, Boolean.TRUE.toString());
            }
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
				strKillProcessCMD = "kill " + this.strPID;
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
			catch(MultiTierException oException)
			{
				throw oException;
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
	
	private void generateServiceImplConfig(String pstrConfigFileName, 
			String pstrServiceImplClassName, 
			String pstrServiceName,
			int piServiceProxyPort,
			String pstrServiceID,
			boolean pbIsPersistent) 
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
		
		// Set the service proxy port
		iParamPosition = oPreparedConfig.indexOf("?", iParamPosition + 1);
		oPreparedConfig.replace(iParamPosition, iParamPosition + 1, piServiceProxyPort + "");
		
		// Set the service UUID
		iParamPosition = oPreparedConfig.indexOf("?", iParamPosition + 1);
		oPreparedConfig.replace(iParamPosition, iParamPosition + 1, pstrServiceID);
		
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
