package gipsy.tests.GEE.simulator;


/**
 * DGT Simulator. 
 * This is the main class. It does nothing, but spawns all the threads and joins them.
 * 
 * @author Emil Vassev
 * @version $Id: DGT.java,v 1.7 2010/12/09 04:31:00 mokhov Exp $
 * @since
 */
public class DGT 
{
	
	/**
	 * XXX
	 */
	private static ResultProcessor soThreadResultProcessor;
	
	/**
	 * XXX
	 */
	private static ResultReceiver soThreadResultReceiver;
	
	/**
	 * XXX
	 */
	private static DemandSender soThreadDemandSender;
	
	/**
	 * XXX
	 * @param argv XXX
	 */
	public static void main(String[] argv)
	{
		try
		{
			soThreadDemandSender = new DemandSender();
			soThreadResultReceiver = new ResultReceiver();
			soThreadResultProcessor = new ResultProcessor();
		
			soThreadDemandSender.setPriority(Thread.MIN_PRIORITY);
			soThreadResultReceiver.setPriority(Thread.MIN_PRIORITY);
			soThreadResultProcessor.setPriority(Thread.MIN_PRIORITY);
			
			GlobalDef.soDGTDialog.setVisible(true);
			
			soThreadDemandSender.start();
			soThreadResultReceiver.start();
			soThreadResultProcessor.start();

			soThreadDemandSender.join();
			soThreadResultReceiver.join();
			soThreadResultProcessor.join();

			
			System.out.println("All the threads have finished successfully.");
			
			System.exit(0);
		}
		catch(Exception ex)
		{
			GlobalDef.handleCriticalException(ex);	
		}
	}
}

// EOF
