package gipsy.GEE.IDP.DemandGenerator.jms;

/*
 * The following commented imports are those which are not used in the second version of the worker. They
 * were previously used in the version 1.0 but not in the JMS- enabled ones.
 */
//import java.net.MalformedURLException;
//import java.util.Vector;
//import java.util.Hashtable;
//import java.rmi.RemoteException;
//import java.rmi.RMISecurityManager;
//import java.io.Serializable;

import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandWorker.DemandWorker;
import gipsy.util.NetUtils;

import java.io.IOException;


/**
 * Class WorkerJTA.
 * It implements Runnable and causes a simple thread to be started.
 * This version of the GIPSY JINI client will halt if no service is found.
 * 
 * @author Emil Vassev
 * @author Serguei Mokhov
 * @author Yi Ji
 * 
 * @version 2.o, $Id: JMSDemandWorker.java,v 1.11 2010/12/20 03:44:36 ji_yi Exp $
 * @since
 */
public class JMSDemandWorker
extends DemandWorker
implements Runnable
{
	protected static String sstrIPLocalAddress = "";
    protected static String sstrIPAddress = "";

    /**
     * XXX: move to gipsy.util.PrintUtils
     * @param sError
     */
    public static void printOutError(String sError)
    {
        System.err.println("WorkerJMS error: " + sError);
    }
    
    /**
     * XXX: move to gipsy.util.PrintUtils
     * @param sMsg
     */
    public static void printOut(String sMsg)
    {
        System.out.println(sMsg);
    }

	/**
	 * The method readStringFromKeybord() reads a string from the keyboard. 
	 * The keyboard reading terminates when "Return" is pressed.
	 * XXX: refactor to an utility class.
	 */
    private static String readStringFromKeybord()
    throws IOException  
    {
		StringBuffer sBuffer = new StringBuffer(0); 
		char ch;
		
		/*				
		 * Reads the input stream into a string buffer
		 * The terminal symbol is Carriage Return
		 */
        sBuffer.setLength(0);
        while ((ch=(char)System.in.read()) != '\n')
	    if ((ch != 10) && (ch != 13))
	    	sBuffer.append(ch); 
    	return sBuffer.toString();
    }

	/**
	 * The method getIPAddress() checks if the IP address of 
	 * the Lookup service is passed as a parameter. 
	 * If not, read the IP address from the keyboard. 
	 * The method returns TRUE if the IP address is not an
	 * empty string. Otherwise, it returns FALSE.
	 * 
	 * XXX: refactor to an utility class.
	 */
    private static boolean getIPAddress(String[] argv)
    {
		try
		{
		    if(argv.length == 0)
		    {
		        System.out.print("Enter the IP address of a JMS Lookup service: ");
		        sstrIPAddress = readStringFromKeybord();
	            if (sstrIPAddress.length() == 0)
	                return false;
		     }
		     else		  
		     {
		    	 sstrIPAddress = argv[0];
		     }		
		}
		catch(Exception e)
		{
		     printOutError(e.getMessage()); 
		     printOutError(e.getLocalizedMessage());
		     printOutError(e.getCause().toString());
		     return false;
		}
		return true;
    } 
    
    
	/** 
	 * The method runSpecifiedIP() performs Unicast Discovery
	 * For unicast discovery, the device must know the IP address and the 
	 * port number where the lookup service is running. The device sends a 
	 * unicast discovery protocol packet to the pre-known IP and port. In 
	 * response, the lookup service will send a unicast announcement packet 
	 * to the device after the discovery packet is recognized and accepted. 
	 * The Jini Framework uses the net.jini.core.discovery.LookupLocator class 
	 * to specify the pre-known IP and Port using a string called Jini URL.
	 */
    private void runSpecifiedIP ()
//        throws IOException, ClassNotFoundException, MalformedURLException
    {
    	getResult();
    }
    
/** 
 */
    private void runNotSpecifiedIP ()
//       throws IOException, ClassNotFoundException
    {
    	getResult();
    }

	/**
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    public JMSDemandWorker() 
    throws IOException, ClassNotFoundException
    {
        try 
		{
			sstrIPLocalAddress = NetUtils.getLocalIPAddress();
			printOut("WorkerJMS IP: " + sstrIPLocalAddress); 
            if (sstrIPAddress.length() == 0)
		        runNotSpecifiedIP();
		    else
                runSpecifiedIP();
        }
         catch(Exception e)  
         {
               printOutError(e.getMessage());
               System.exit(1);
         }
    }
/** 
  */
    protected void getResult() 
    {
    	while (true)
    	{
		
    	//WorkDemand oDemand = null; 
		//WorkResult oResult = null; working properly
    	DemandController  oResult ; 
		//Synchronous --> false 
		//DemandJMS oJMS = new DemandJMS(false);
        //Asynchronous --> true 
		ITransportAgent oJMS = null;
		try 
		{
			oJMS = new JMSTransportAgent();
		} 
		catch (DMSException e1) 
		{
			e1.printStackTrace(System.err);
		}
		
		//oJMS.jmsConnect("GIPSY/Topic1");
        printOut("***********************************************");
        printOut("WorkerJMS is working ...");
        printOut("***********************************************");
		printOut("An attempt to read the demand ... ");
		Object o2 = null;
		///
		while (o2 == null)
		{
			try 
			{
				o2 = oJMS.getDemand();
			} 
			catch (DMSException e) 
			{
				e.printStackTrace(System.err);
			}
		
		}
		
		
		//o = oJMS.readResult();
		DemandController ComputedDemand = new DemandController();
		//DemandController ComputedResult ; 
		ComputedDemand = (DemandController)o2;
		
		//oJMS
		printOut("Demand received: name: " + ComputedDemand.getSignature());
		// Changed: commentted off
		//ComputedDemand.computeDemand(ComputedDemand.strDemandName,GetLocalIPAddress());
		// Changed: added
		ComputedDemand = (DemandController) ComputedDemand.execute();
		printOut("The Demand is executed.");
		//Synchronous = false 
		///oJMS = new DemandJMS(false);
		//Asynchronous = true
		//oJMS.jmsConnect("GIPSY/Topic2");
		//oJMS.writeDemand(oResult); working properly
		try 
		{
			oJMS.setResult(ComputedDemand);
		} 
		catch (DMSException e) 
		{
			e.printStackTrace(System.err);
		}
		//////
		printOut("The Result is  dispatched.");
		
    	}
    }
/**
 * Method run() - inherid from Runnable
 * The purpose of the thread is to keep the client alive for some time
 * while it is doing the Multicast Discovering 
*/
   public void run() 
    {
        while (true) 
        {
            try 
            {
/** 
 * Set the thread sleep time to of the thread to 60000 milliseconds (1 min.). 
 * This sleep time may not be sufficient sometimes when the lookup service
 * if very far. 
 */
            Thread.sleep(60000L);
            } 
            catch (InterruptedException e) 
            {
            	printOutError(e.getMessage());
            }
        }
    }
/**
 * Method main() - the entry point of the the program. 
 * It creates the WorkerJTA and starts its thread 
 * if the Lookup locator's IP is not provided
 */  
   public static void main(String args[]) 
    {
        try 
        {
/**
 * Get the IP address of the Lookup locator if it is not 
 * provided as a parameter. If the IP address is still 
 * not provided the program performs Unicast Discovery
 * of the Lookup locator. Otherwise - Multicast Discovery.
 */
            if (!getIPAddress(args))
            {
               printOut("Search for GIPSY Lookup service all around the network ...");
               JMSDemandWorker oWorkerJTA = new JMSDemandWorker(); 
               new Thread(oWorkerJTA).start();
            }
            else
            {
               printOut("Search for GIPSY Lookup service on IP address: " + sstrIPAddress); 
               JMSDemandWorker oGipsyClient = new JMSDemandWorker();
            }
        } 
		catch (Exception e) 
		{
		    printOutError("Couldn't create client: " + e.getMessage());
	        System.exit(1);
	    }
    }
}