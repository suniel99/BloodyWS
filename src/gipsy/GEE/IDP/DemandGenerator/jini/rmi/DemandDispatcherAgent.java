package gipsy.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.demands.IDemand;


/**
 * Jini Demand Dispatcher Agent.
 * 
 * XXX Please move this class to the test folder.
 * 
 * @author Emil Vassev
 * @author Serguei Mokhov
 * @author Yi Ji
 * 
 * @since 1.0.0
 * @version $Id: DemandDispatcherAgent.java,v 1.14 2010/09/12 00:59:17 ji_yi Exp $
 */
public class DemandDispatcherAgent
{
	private static final String MSG_PREFIX = "DemandDispactherAgent message: ";
	private static final String ERR_PREFIX = "DemandDispactherAgent error: ";
	
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
			//JiniDispatcherEntry oEntry = null;
			
			//WorkTask oTask = null;
			//WorkResult oResult = null;
			IDemand oTask = null;
			IDemand oResult = null;

			printOut("GIPSY Demand Dispatcher Agent is running ...");
			IDemandDispatcher oDemandDispatcher = new JiniDemandDispatcher("", "");
			printOut("Reading a task ....");
			//oEntry = oDemandDispatcher.readDemandEntry();
			
//			oTask = (WorkTask)oEntry.oObject;
			//oTask = (IDemand)oEntry.oDemand;
			oTask = oDemandDispatcher.readDemand();
			
			printOut("A WorkTask has been received");
			printOut("Entry ID " + oTask.getSignature().toString());
			
			//oResult = oTask.work();
			oResult = oTask.execute();
			
			oDemandDispatcher.writeResult(oTask.getSignature(), oResult);
			printOut("A WorkResult with this ID has been dispatched");
		}
		catch (Exception e)
		{
			printOutError(e.getMessage());
		}
	}
}

// EOF
