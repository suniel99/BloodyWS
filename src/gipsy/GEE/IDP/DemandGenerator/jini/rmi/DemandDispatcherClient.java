package gipsy.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.ProceduralDemand;


/**
 * Jini Demand Dispatcher Client.
 * 
 * XXX Please move this class to the test folder
 * 
 * @author Emil Vassev
 * @author Serguei Mokhov
 * @author Yi Ji
 * 
 * @since 1.0.0
 * @version $Id: DemandDispatcherClient.java,v 1.11 2010/09/12 00:59:17 ji_yi Exp $
 */
public class DemandDispatcherClient
{
	private static final String MSG_PREFIX = "DemandDispactherClient message: ";
	private static final String ERR_PREFIX = "DemandDispactherClient error: ";

	private static void printOut(String pstrMsg)
	{
		System.out.println(MSG_PREFIX + pstrMsg);
	}

	private static void printOutError(String pstrError)
	{
		System.err.println(ERR_PREFIX + pstrError);
	}

	/**
	 * Method main() - the entry point of the the program. 
	 */
	public static void main(String args[])
	{
		try
		{
			//WorkTask oTask = new WorkTask("My name");
			//WorkResult oResult = null;
			//IDemand oTask = new WorkTask("My name");
			IDemand oTask = new ProceduralDemand("My name");
			IDemand oResult = null;

			DemandSignature oSignature;
			printOut("JMS Demand Dispatcher Client is running ...");
			IDemandDispatcher oDemandDispatcher = new JiniDemandDispatcher("", "");
			oSignature = oDemandDispatcher.writeDemand(oTask);
			printOut("A WorkTask has been dispatched");
			printOut("Entry ID: " + oSignature.toString());
			printOut("Reading a result ....");
			
			//oResult = (WorkResult)oDemandDispatcher.readResult(oUniqueID);
			oResult = (IDemand)oDemandDispatcher.readResult(oSignature);
			
			printOut("A WorkResult with this ID has been received: " + oResult);
		}
		catch (Exception e)
		{
			printOutError(e.getMessage());
		}
	}
}

// EOF
