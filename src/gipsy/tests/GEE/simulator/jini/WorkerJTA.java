package gipsy.tests.GEE.simulator.jini;

import gipsy.Configuration;
import gipsy.GIPSY;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.IJINITransportAgent;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.util.NetUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Hashtable;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.DiscoveryEvent;
import net.jini.discovery.DiscoveryListener;
import net.jini.discovery.LookupDiscovery;


/**
 * A GIPSY-Simulator JINI client class to use the GIPSY-Simulator JINI service(s).
 * It implements Runnable and causes a simple thread to be started.
 * This version of the GIPSY JINI client will halt if no service is found.
 *
 * @author Emil Vassev, inspired by the book "Core JINI" by W. Keith Edwards
 * @since 1.1.1
 * @version 2.0.0, $Id: WorkerJTA.java,v 1.24 2010/12/08 17:36:16 mokhov Exp $
 */
public class WorkerJTA
implements Runnable 
{
	/**
	 * Setup the lookup group's name.
	 */
    protected final String[] DISCOVERY_GROUP_NAMES = {"gipsy"};
    
    /**
     * XXX: document and type-template.
     */
    protected Hashtable oRegistrations = new Hashtable();
    
    /**
     * XXX.
     */
    protected ServiceTemplate oTemplate;
    
	/**
	 * XXX.
	 */
	protected static String sstrIPLocalAddress = "";
    
	/**
     * XXX.
     */
    protected static String sstrIPAddress = "";

    /*
	 * Configuration settings.
	 * XXX: should these be public maybe?
	 */
	
    private static final String CONFIG_FILE = "simulation.config";
	private static final String DEMAND_DISPATCHER_CLASS_KEY = "ca.concordia.cse.gipsy.tests.gee.dms.simulator.transportagent";
	private static final String SECURITY_FILE = "jini.policy";

	/**
	 * Transport Agent.
	 */
	private ITransportAgent oTA = null;
	
	/**
	 * Class Listener
	 * Inner class to listen for discovery events.   
	 * @author Emil Vassev & W. Keith Edwards
	 * @version $Id: WorkerJTA.java,v 1.24 2010/12/08 17:36:16 mokhov Exp $
	 * @since 1.0.0
	 */
    class Listener
    implements DiscoveryListener 
    {
    	WorkerJTA oHostWorker = null;
    	
    	Listener(WorkerJTA poWorker)
    	{
    		this.oHostWorker = poWorker;
    	}

		/**
		 * Called when found a new lookup service.
         * @see net.jini.discovery.DiscoveryListener#discovered(net.jini.discovery.DiscoveryEvent)
         */
        public void discovered(DiscoveryEvent poEvent) 		
        {
            printOut("Discovered a lookup service!");
            
            ServiceRegistrar[] aoRegistrars = poEvent.getRegistrars();
            
            for(int i = 0; i < aoRegistrars.length; i++) 
            {
                if(!oRegistrations.containsKey(aoRegistrars[i])) 
                {
                	printOut("GIPSY lookup service has been found.");        
                    lookForService(aoRegistrars[i]);
                    
                    synchronized(this.oHostWorker)
                    {
                    	this.oHostWorker.notify();
                    }
                }
            }
        }
        
		/**
		 * Called ONLY when we explicitly discard  
		 * lookup service, not "automatically" when a 
		 * lookup service goes down.  Once discovered, 
		 * there is NO ongoing communication with a 
		 * lookup service.
		 */
        public void discarded(DiscoveryEvent poEvent) 
        {
	 	    System.out.println("Listener.discarded()");            
		    ServiceRegistrar[] aoRegistrars = poEvent.getRegistrars();
		    
	        for(int i = 0; i < aoRegistrars.length; i++) 
		    {
	        	oRegistrations.remove(aoRegistrars[i]);
	        }
        }
    }

    /**
     * XXX: to PrintUtils
     * @param pstrError
     */
    public static void printOutError(String pstrError)
    {
        System.err.println("WorkerJTA error: " + pstrError);
    }

    /**
     * XXX: to PrintUtils
     * @param pstrMsg
     */
    public static void printOut(String pstrMsg)
    {
        System.out.println(pstrMsg);
    }

	/**
	 * The method readStringFromKeybord() reads a string from the keyboard. 
	 * The keyboard reading terminates when "Return" is pressed.
	 * XXX: move to a utility class
	 */
    private static String readStringFromKeyboard()
    throws IOException
    {
		StringBuffer oBuffer = new StringBuffer(0); 
		char cCh;

		/*
		 * Reads the input stream into a string buffer
		 * The terminal symbol is Carriage Return
		 */
        oBuffer.setLength(0);

        while((cCh = (char)System.in.read()) != '\n')
        {
	        // XXX: hardcoding; EOLS not necessarily both chars
	        if((cCh != 10) && (cCh != 13))
	        {
		    	oBuffer.append(cCh);
	        }
        }
        
		return oBuffer.toString();
    }

	/**
	 * The method getIPAddress() checks if the IP address of 
	 * the Lookup service is passed as a parameter. 
	 * If not, read the IP address from the keyboard. 
	 * The method returns TRUE if the IP address is not an
	 * empty string. Otherwise, it returns FALSE.
	 * XXX: move to a utility class
	 */
    private static boolean getIPAddress(String[] argv)
    {
		try
		{
		    if(argv.length == 0)
		    {
		    	System.out.print("Enter the IP address of a Lookup service: ");
		    	
		    	sstrIPAddress = readStringFromKeyboard();
                
		    	if(sstrIPAddress.length() == 0)
		    	{
                   return false;
		    	}
		    }
		    else		  
		    {
		    	sstrIPAddress = argv[0];
		    }		
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
		    printOutError(e.getMessage()); 
		    return false;
		}
		
		return true;
    }
 
	/** 
	 * The method runSpecifiedIP() performs Unicast Discovery.
	 * For unicast discovery, the device must know the IP address and the 
	 * port number where the lookup service is running. The device sends a 
	 * unicast discovery protocol packet to the pre-known IP and port. In 
	 * response, the lookup service will send a unicast announcement packet 
	 * to the device after the discovery packet is recognized and accepted. 
	 * The Jini Framework uses the net.jini.core.discovery.LookupLocator class 
	 * to specify the pre-known IP and Port using a string called Jini URL.
	 */
    private void runSpecifiedIP()
    throws IOException, ClassNotFoundException, MalformedURLException
    {
        LookupLocator oLookup = null;
        ServiceRegistrar oRegistrar = null;
        
        printOut("Perform Unicast Discovery on IP: " + sstrIPAddress);

		/*
		 * Prepare for Unicast Discovery	
		 * the parameter should be like that: "jini://www.gipsy.com"
		 */
        oLookup = new LookupLocator("jini://" + sstrIPAddress);
        if(oLookup == null)
        {
        	printOut("The Lookup Service cannot be found on IP:" + sstrIPAddress);
        } 
        else 
        {
			/*
			 * Discovering a lookup service 
			 */
        	try
        	{
        		oRegistrar = oLookup.getRegistrar();
        	}
        	catch(MalformedURLException e)
        	{
        		e.printStackTrace(System.err);
        		printOutError("Malformed URL.  Use a valid jini URL.");
        		System.exit(1);
        	}
        	
        	printOut("The Lookup Service has been found:");
        	printOut("Lookup ID: " + oRegistrar.getServiceID());
        	printOut("Lookup URL: " + oRegistrar.getLocator().toString());
        	lookForService(oRegistrar);
        }
    }

	/** 
	 * The method runNotSpecifiedIP() provides Multicast Discovery.
	 * This kind of discovery is used when the locations of Jini Lookup 
	 * services are not known beforehand. The network must be multicast 
	 * enabled. Once discovered the Lookup service(s) will send multicast
	 * announcement packets, which may reach our application which acts
	 * as a Discovery Listener - listen for Lookup announcements.   
	 */
    private void runNotSpecifiedIP()
    throws IOException, ClassNotFoundException
    {
		LookupDiscovery oDiscovery = null;
        
		/*
		 * Search for the GIPSY group
		 */
 		printOut("Search for GIPSY LookupDiscovery service ....");	   
//        disco = new LookupDiscovery(DISCOVERY_GROUP_NAMES);
 		oDiscovery = new LookupDiscovery(LookupDiscovery.ALL_GROUPS);
 		
		/*
		 * Run a listener
		 */
 		printOut("Run a listener ....");	   
		oDiscovery.addDiscoveryListener(new Listener(this));

		synchronized(this)
		{
			try 
			{
				wait();
			} 
			catch(InterruptedException e)
			{
				e.printStackTrace(System.err);
			}
		}
    }

    /**
     * XXX
     * @throws IOException XXX
     * @throws ClassNotFoundException XXX
     */
    public WorkerJTA() 
    throws IOException, ClassNotFoundException
    {
    	try 
		{
    		Class<?>[] aoTypes = new Class[] { IJINITransportAgent.class };
			this.oTemplate = new ServiceTemplate(null, aoTypes, null);
      
			sstrIPLocalAddress = NetUtils.getLocalIPAddress();
			printOut("WorkerJTA IP: " + sstrIPLocalAddress);

			/*
			 * Set a security manager
			 */
			if(System.getSecurityManager() == null) 
			{
				// Get the current security file.
				String strPath = System.getProperty(Configuration.JAVA_SECURITY_POLICY_KEY);
				if(strPath == null || strPath.trim().isEmpty())
				{
	   				System.setProperty
	   				(
	   					Configuration.JAVA_SECURITY_POLICY_KEY, 
	   					GIPSY.getConfugration().getProperty(Configuration.CONFIGURATION_ROOT_PATH_KEY) + SECURITY_FILE
	   				);
				}

				System.setSecurityManager(new RMISecurityManager());
			}
         }
         catch(Exception e)  
         {
        	 e.printStackTrace(System.err);
        	 printOutError(e.getMessage());
        	 System.exit(1);
         }
    }

	/** 
	 * The method lookForService search for proxies
	 * that implement IJINITransportAgent. 
	 * If such a proxy is found, executes interface
	 * methods implemented by this proxy.
     * @param poRegistrar XXX
     */
    protected void lookForService(ServiceRegistrar poRegistrar) 
    {
        try 
	    {
            this.oTA = (ITransportAgent)poRegistrar.lookup(this.oTemplate);
        } 
        catch(RemoteException e) 
        {
        	e.printStackTrace(System.err);
            printOutError("Remote error: " + e.getMessage());
        }
    }
    
	/**
	 * Method run() - inherited from Runnable
	 * The purpose of the thread is to keep the client alive for some time
	 * while it is doing the Multicast Discovering 
	 */
    public void run() 
    {
        while(true) 
        {
            try 
            {
				/*
				 * Set the thread sleep time to of the thread to 60000 milliseconds (1 min.). 
				 * This sleep time may not be sufficient sometimes when the lookup service
				 * if very far.
				 * 
				 * XXX: hardcoding
				 */
                Thread.currentThread().sleep(60000L);
            } 
            catch(InterruptedException e) 
            {
            	e.printStackTrace(System.err);
            	printOutError(e.getMessage());
            }
        }
    }

	/**
	 * Method main() - the entry point of the the program. 
	 * It creates the WorkerJTA and starts its thread 
	 * if the Lookup locator's IP is not provided
     * @param argv XXX
     */
    public static void main(String argv[]) 
    {
    	try 
        {
			/*
			 * Get the IP address of the Lookup locator if it is not 
			 * provided as a parameter. If the IP address is still 
			 * not provided the program performs Unicast Discovery
			 * of the Lookup locator. Otherwise - Multicast Discovery.
			 */
            if(!getIPAddress(argv))
            {
               printOut("Search for Transport Agent all around the network ...");
		    }
		    else
            {
               printOut("Search for Transport Agent on IP address: " + sstrIPAddress); 
            }
            
            WorkerJTA oWorkerJTA = new WorkerJTA(); 
            oWorkerJTA.attachTransportAgent();
            oWorkerJTA.startWork();
        } 
		catch (Exception e) 
		{
			e.printStackTrace(System.err);
		    printOutError("Couldn't create client: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Find a TA for this worker. Currently using the member oJTA
     * to avoid external behavioral changes to the original methods.
     */
    public void attachTransportAgent()
    {	
    	// Determine which TA to use.
    	while(this.oTA == null)
    	{
			try
			{
				Configuration oConfig = GIPSY.getConfugration();

				// Load the configuration file.
	        	FileInputStream oFileIn = new FileInputStream
	        	(
	        		oConfig.getProperty(Configuration.CONFIGURATION_ROOT_PATH_KEY) + CONFIG_FILE
	        	);
	        	
	        	oConfig.loadFromXML(oFileIn);
	        	String strClassName = oConfig.getProperty(DEMAND_DISPATCHER_CLASS_KEY);
	        	
	        	if(strClassName.contains("JINITransportAgentProxy"))
	        	{
	        		if(sstrIPAddress.length() == 0)
	        		{
	        			runNotSpecifiedIP();
	        		}
	        		else
	        		{
	        			runSpecifiedIP();
	        		}
	        	}
	        	else
	        	{
	        		this.oTA = (ITransportAgent)Class.forName(strClassName).newInstance();
	        	}

	//			goDemandDispatcher = new JiniDemandDispatcher("","");
	//			goDemandDispatcher = new JMSDemandDispatcher("","");
			}
			catch(Exception ex)
			{
				ex.printStackTrace(System.err);
			}
			if(this.oTA == null)
			{
				System.out.println("Cannot find the TA, try again later");
				
				try 
				{
					// XXX: hardcoding
					Thread.sleep(3000);
				} 
				catch(InterruptedException e) 
				{
					e.printStackTrace(System.err);
				}
			}
    	}
    }
    
    /**
     * Start the working process.
     * Entry condition: the TA has been set.
     */
    public void startWork()
    {
    	// Check entry condition:
    	if(this.oTA != null)
    	{
    		IDemand oDemand, oResult;
            
            printOut("A Transport Agent has been found.");
            printOut("");
            printOut("***********************************************");
            printOut("WorkerJTA is working ...");
            printOut("***********************************************");

			while(true)
			{
				try 
				{
					printOut("");
					oDemand = (IDemand)this.oTA.getDemand();
					oDemand.addTimeLine("WK-Receive");
					printOut("Demand received: name: " + oDemand.getSignature());
					oResult = oDemand.execute();
					printOut("Demand computed");
					oResult.getResult();
					oResult.addTimeLine("WK-Send");
					this.oTA.setResult(oResult);
					printOut("Result dispatched");
    			} 
				catch(Exception e) 
				{
					// If anything wrong happens
					e.printStackTrace(System.err);
					// Try to obtain the TA again.
					attachTransportAgent();
				} 
			}
		}
		else
		{
			printOut("Cannot find a Transport Agent");
		}
    }
}

// EOF
