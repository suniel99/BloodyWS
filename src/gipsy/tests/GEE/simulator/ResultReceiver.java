package gipsy.tests.GEE.simulator;

import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcher;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcherException;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.TimeLine;
import gipsy.GEE.multitier.TAExceptionHandler;
import gipsy.tests.GEE.simulator.demands.SizeAdjustableDemand;
import marf.util.BaseThread;


/**
 * TODO: document.
 *
 * @author 
 * @since
 * @version $Id: ResultReceiver.java,v 1.16 2011/01/12 22:03:17 ji_yi Exp $
 */
public class ResultReceiver
extends BaseThread
{
	private boolean bStopThread;
	private DemandIDPool oIDPool;
	private ResultPool oResultPool;
	
	private ITransportAgent oTA = null;
	private TAExceptionHandler oHandler = null;
	/**
	 * 
	 */
	public ResultReceiver() 
	{
		super();
		this.bStopThread = false;
		this.oIDPool = DemandIDPool.instance();
		this.oResultPool = ResultPool.getInstance();
		DemandDispatcher oDispatcher = (DemandDispatcher)GlobalDef.soDemandDispatcher;
		this.oTA = oDispatcher.getTA();
		this.oHandler = oDispatcher.getTAExceptionHandler();
	}
	
	/**
	 * 
	 */
	private void receiveNextResult()
	{
		IDemand oWorkResult;
		DemandSignature oSignature;
		
		oSignature = this.oIDPool.get();
		
		String strThreadID = "DGT" + this.getId();
		String strBeforeReceive = strThreadID + "-BRecv";
		String strAfterReceive = strThreadID + "-ARecv";
		
		if(oSignature != null)
		{
			try
			{
				// Now send it to the store
				
				TimeLine oTimeLine = new TimeLine();
				oTimeLine.addTimeLine(strBeforeReceive);
				oWorkResult = GlobalDef.soDemandDispatcher.readResultIfExists(oSignature);
				oTimeLine.addTimeLine(strAfterReceive);
				
				if(oWorkResult != null)
				{
					// Get the local time-line record and stamp it
					//TimeLine oTimeLine = GlobalDef.TIMELINES.get(oSignature);
					//oTimeLine.addTimeLine(GlobalDef.DGT_RECEIVE);

					// Carry on with the original process
					//System.out.println();
					//System.out.println("Computed " + oWorkResult.getSignature());
					//System.out.println();
					oWorkResult.addTimeLine(oTimeLine);
					if(oWorkResult instanceof SizeAdjustableDemand)
					{
						((SizeAdjustableDemand)oWorkResult).clearPayload();
					}
					this.oResultPool.put(oWorkResult);
				}
				else
				{
					this.oIDPool.put(oSignature);
					//System.out.print(".");
				}
			}
			catch(DemandDispatcherException ex)
			{
				GlobalDef.handleCriticalException(ex);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run()
	{
		while(!this.bStopThread)
		{
			if(GlobalDef.sbEnd)
			{
				this.bStopThread = true;
				break;
			}
			
			try
			{
				GlobalDef.soReceiverControl.waitOn();
				sleep(GlobalDef.siSleepTime);
			}
			catch(InterruptedException ex)
			{
				GlobalDef.handleCriticalException(ex);	
			}
			
			receiveNextResult();
			GlobalDef.soStatisticsUpdator.updateStatisticsInfo();
			//**** gives a chance to others to run on green threads Java
			yield();
		}
	}
}

// EOF
