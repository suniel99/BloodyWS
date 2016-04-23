package gipsy.tests.GEE.simulator;

import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcher;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcherException;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.TimeLine;
import gipsy.GEE.multitier.TAExceptionHandler;
import marf.util.BaseThread;


/**
 * Thread for sending demands.
 * This thread operates on both DemandIDPool and DemandPool pools.
 * 
 * @author Emil Vassev
 * @version $Id: DemandSender.java,v 1.16 2011/01/26 05:11:36 ji_yi Exp $
 * @since
 */
public class DemandSender
extends BaseThread
{
	/**
	 * XXX
	 */
	private boolean bStopThread;
	
	/**
	 * XXX
	 */
	private DemandIDPool oIDPool;
	
	/**
	 * XXX
	 */
	private DemandPool oDemandPool;

	
	private ITransportAgent oTA = null;
	private TAExceptionHandler oHandler = null;
	
	private TimeLine oPreviousTimeLine = null;
	
	/**
	 * 
	 */
	public DemandSender() 
	{
		super();
		this.bStopThread = false;
		this.oIDPool = DemandIDPool.instance();
		this.oDemandPool = DemandPool.getInstance();
		DemandDispatcher oDispatcher = (DemandDispatcher)GlobalDef.soDemandDispatcher;
		this.oTA = oDispatcher.getTA();
		this.oHandler = oDispatcher.getTAExceptionHandler();
	}
	
	/**
	 * XXX
	 */
	private void sendNextDemand()
	{
		IDemand oWorkDemand;
		DemandSignature oSignature;
		
		String strThreadID = "DGT" + this.getId();
		String strBeforeSend = strThreadID + "-PrevBSend";
		String strAfterSend = strThreadID + "-PrevASend";
		
		oWorkDemand = this.oDemandPool.get();
		
		if(oWorkDemand != null)
		{
			try
			{
				// Create a time-line record, stamp and keep it before send
				//TimeLine oTimeLine = new TimeLine();
				//oTimeLine.addTimeLine(GlobalDef.DGT_SEND);
				//GlobalDef.TIMELINES.put(oWorkDemand.getSignature(), oTimeLine);
				
				// Now send it to the store
				if(this.oPreviousTimeLine != null)
				{
					oWorkDemand.addTimeLine(this.oPreviousTimeLine);
				}
				this.oPreviousTimeLine = new TimeLine();
				this.oPreviousTimeLine.addTimeLine(strBeforeSend);
				oSignature = GlobalDef.soDemandDispatcher.writeDemand(oWorkDemand);
				this.oPreviousTimeLine.addTimeLine(strAfterSend);
				
				if(oSignature != null)
				{
				//	System.out.println();
				//	System.out.println("Pending " + oWorkDemand.getSignature());
				//	System.out.println();

					this.oIDPool.put(oSignature);
				}
			}
			catch(DemandDispatcherException ex)
			{
				GlobalDef.handleCriticalException(ex);
			}
		}
	}

	/**
	 * XXX
	 * @see java.lang.Thread#run()
	 */
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
			
			sendNextDemand();
			GlobalDef.soStatisticsUpdator.updateStatisticsInfo();
			//**** gives a chance to others to run on green threads Java
			yield();
		}
	}
}

// EOF
