package gipsy.tests.GEE.simulator;

import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.TimeLine;
import marf.util.BaseThread;


/**
 * Thread for processing demand results.
 * This thread operates on the ResultPool pool.
 * 
 * @author Emil Vassev
 * @version $Id: ResultProcessor.java,v 1.12 2011/01/10 16:51:10 ji_yi Exp $
 * @since
 */
public class ResultProcessor
extends BaseThread
{
	private boolean bStopThread;
	private ResultPool oResultPool;
	
	public ResultProcessor() 
	{
		super();
		this.bStopThread = false;
		this.oResultPool = ResultPool.getInstance();
	}
	
	private void processNextResult()
	{
		IDemand oWorkResult;
		oWorkResult = this.oResultPool.get();
		
		if(oWorkResult != null)
		{
			try
			{
				// XXX: fix hardcoding with a constant
				sleep(1000);
			}
			catch(InterruptedException ex)
			{
				GlobalDef.handleCriticalException(ex);	
			}
			
			// Get the local time-line record and add it to the Demand instance
			//TimeLine oTimeLine = GlobalDef.TIMELINES.get(oWorkResult.getSignature());
			//oWorkResult.addTimeLine(oTimeLine);
			
			// Now store the result
			oWorkResult.storeResult(System.getProperty("user.dir") + "/" +oWorkResult.getClass().getSimpleName());
			GlobalDef.slNumProcessedDemands++;
			// Update the statistics info
			GlobalDef.soStatisticsUpdator.updateStatisticsInfo();
		}
	}
	
	public void run()
	{
		while(!this.bStopThread)
		{
			if(GlobalDef.sbEnd)
			{
				this.bStopThread = true;
			}
			
			try
			{
				GlobalDef.soSynchronizer.waitOn();
				sleep(GlobalDef.siSleepTime);
			}
			catch(InterruptedException ex)
			{
				GlobalDef.handleCriticalException(ex);	
			}
			
			processNextResult();
			
			// Gives a chance to others to run on green threads Java
			yield();
		}
	}
}

// EOF
