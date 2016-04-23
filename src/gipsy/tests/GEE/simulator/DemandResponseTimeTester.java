/**
 * 
 */
package gipsy.tests.GEE.simulator;

import java.io.IOException;

import gipsy.GEE.IDP.DMSException;
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
 * A single thread who sends demands and receives results synchronously,
 * i.e. before sending the next demand, it has to receive the result of
 * the last demand. The average response time of demand migration will
 * be calculated.
 * 
 * @author Yi Ji
 * @version $Id: DemandResponseTimeTester.java,v 1.7 2011/01/26 05:11:36 ji_yi Exp $
 */
public class DemandResponseTimeTester
extends BaseThread
{
	DemandPool oDemandPool = null;
	ResultPool oResultPool = null;
	
	private boolean bStopThread = false;
	
	
	private ITransportAgent oTA = null;
	private TAExceptionHandler oHandler = null;
	
	
	public DemandResponseTimeTester()
	{
		this.oDemandPool = DemandPool.getInstance();
		this.oResultPool = ResultPool.getInstance();
		this.bStopThread = false;
		DemandDispatcher oDispatcher = (DemandDispatcher)GlobalDef.soDemandDispatcher;
		this.oTA = oDispatcher.getTA();
		this.oHandler = oDispatcher.getTAExceptionHandler();
	}
	
	
	@Override
	public void run() 
	{
		String strThreadID = "DGT" + this.getId();
		String strBeforeSend = strThreadID + "-BSend";
		String strAfterSend = strThreadID + "-ASend";
		String strAfterReceive = strThreadID + "-ARecv";
		
		while(!this.bStopThread)
		{
			if(GlobalDef.sbEnd)
			{
				this.bStopThread = true;
				break;
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
			
			
			IDemand oDemand = this.oDemandPool.get();
			DemandSignature oSignature = null;
			
			if(oDemand != null)
			{
				long lGUIUpdateCount = 0;
				
				
				while(oDemand != null && !this.bStopThread)
				{
					if(GlobalDef.sbEnd)
					{
						this.bStopThread = true;
						break;
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
					
					for(int i = 0; i<4 && !this.bStopThread; i++)
					{
						if(GlobalDef.sbEnd)
						{
							this.bStopThread = true;
							break;
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
						
						try
						{	
							TimeLine oTimeLine = new TimeLine();
							oTimeLine.addTimeLine(strBeforeSend);
							oSignature = oTA.setDemand(oDemand);
							oTimeLine.addTimeLine(strAfterSend);
							oDemand = oTA.getResult(oSignature);
							oTimeLine.addTimeLine(strAfterReceive);
							
							oDemand.addTimeLine(oTimeLine);
							//System.out.println("Result " + oDemand.getSignature() + " received!");
							break;
						}
						catch(DMSException oException)
						{
							if(oHandler != null && i<3)
							{
								try 
								{
									oTA = oHandler.fixTA(oTA, oException);
									Thread.sleep(3000);
								} 
								catch (InterruptedException oInterrupted) 
								{
									oInterrupted.printStackTrace(System.err);
								}
							}
							else
							{
								oException.printStackTrace(System.err);
								return;
							}
						}
					}
					
					if(oDemand instanceof SizeAdjustableDemand)
					{
						((SizeAdjustableDemand)oDemand).clearPayload();
					}
					
					this.oResultPool.put(oDemand);
					oDemand = this.oDemandPool.get();
					lGUIUpdateCount++;
					GlobalDef.soStatisticsUpdator.updateStatisticsInfo();
				}
				//oResultPool.updateGUI();
				//oDemandPool.updateGUI();
			}
			
			//**** gives a chance to others to run on green threads Java
			yield();
		}
		
		
		
	}
	
	
}
