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
 * Thread for sending demands.
 * This thread operates on both DemandIDPool and DemandPool pools.
 * 
 * @author Emil Vassev
 * @version $Id: SpaceTimeTester.java,v 1.2 2011/01/26 05:11:36 ji_yi Exp $
 * @since
 */
public class SpaceTimeTester
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
	private ResultPool oResultPool;
	
	
	private ITransportAgent oTA = null;
	private TAExceptionHandler oHandler = null;
	
	private TimeLine oPreviousTimeLine = null;
	
	private int iBatchSize = 100;
	
	/**
	 * 
	 */
	public SpaceTimeTester(int piBatchSize) 
	{
		super();
		this.bStopThread = false;
		this.oIDPool = DemandIDPool.instance();
		this.oDemandPool = DemandPool.getInstance();
		this.oResultPool = ResultPool.getInstance();
		DemandDispatcher oDispatcher = (DemandDispatcher)GlobalDef.soDemandDispatcher;
		this.oTA = oDispatcher.getTA();
		this.oHandler = oDispatcher.getTAExceptionHandler();
		this.iBatchSize = piBatchSize;
	}

	/**
	 * XXX
	 * @see java.lang.Thread#run()
	 */
	public void run()
	{
		ResultAnalyst oAnalyst = new ResultAnalyst("SpaceTime");
		
		int iCounter = 0;
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
			
			
			IDemand oWorkDemand;
			DemandSignature oSignature;
			
			String strThreadID = "DGT" + this.getId();
			String strBeforeSend = strThreadID + "-PrevBSend";
			String strAfterSend = strThreadID + "-PrevASend";
			
			
			
			// Send next 100 demands
			
			for(int i = 0; i<this.iBatchSize; i++)
			{
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
						oSignature = GlobalDef.soDemandDispatcher.writeResult(oWorkDemand.getSignature(), oWorkDemand);
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
					GlobalDef.soStatisticsUpdator.updateStatisticsInfo();
				}
				
			}
			
			// Receive next 100 demands
			for(int i = 0; i<this.iBatchSize; i++)
			{
				IDemand oWorkResult;
				
				oSignature = this.oIDPool.get();
				
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
					GlobalDef.soStatisticsUpdator.updateStatisticsInfo();
				}
			}
			
			// Process the 100 demands received
			
			Thread oRecorder = new Thread(oAnalyst);
			oRecorder.start();
			try {
				oRecorder.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//**** gives a chance to others to run on green threads Java
			
			
			//yield();
			iCounter ++;
			if(iCounter%200 == 0)
			{
				System.gc();
			}
		}
	}
}

// EOF
