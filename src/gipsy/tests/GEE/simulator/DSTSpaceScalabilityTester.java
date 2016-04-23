package gipsy.tests.GEE.simulator;

import java.util.Hashtable;

import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcher;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.TimeLine;
import gipsy.GEE.multitier.TAExceptionHandler;
import gipsy.tests.GEE.simulator.demands.SizeAdjustableDemand;
import marf.util.BaseThread;

/**
 * A single thread who writes computed demands and reads them synchronously. 
 * The demands are of specified size. This tester is for testing the space 
 * scalabiliby of the DST.
 * 
 * @author Yi Ji
 * @version $Id: DSTSpaceScalabilityTester.java,v 1.7 2011/01/10 21:34:10 ji_yi Exp $
 */
public class DSTSpaceScalabilityTester
extends BaseThread
{
	DemandPool oDemandPool = null;
	ResultPool oResultPool = null;
	
	private boolean bStopThread = false;
	private int iDemandSize = 0;
	
	
	private ITransportAgent oTA = null;
	private TAExceptionHandler oHandler = null;
	
	public static Hashtable<Long, Long> oRecord = new Hashtable<Long, Long>();
	
	public int iTesterID = 0;
	
	public DSTSpaceScalabilityTester()
	{
		this.oDemandPool = DemandPool.getInstance();
		this.oResultPool = ResultPool.getInstance();
		this.bStopThread = false;
		
		DemandDispatcher oDispatcher = (DemandDispatcher)GlobalDef.soDemandDispatcher;
		this.oTA = oDispatcher.getTA();
		this.oHandler = oDispatcher.getTAExceptionHandler();
		oRecord.clear();
	}
	
	
	@Override
	public void run() 
	{
		System.gc();
		
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
			
			
			IDemand oDemand = this.oDemandPool.get();
			DemandSignature oSignature = null;
			
			if(oDemand != null)
			{
				
				long lErrorCount = 0;
				long lSendingCount = 0;
				
				while(oDemand != null && !this.bStopThread)
				{
					
					//oDemand.addTimeLine("before");
					for(int i = 0; i<4 && !this.bStopThread; i++)
					{
						try
						{	
							long lBefore = System.currentTimeMillis();
							oSignature = oTA.setResult(oDemand);
							long lAfter = System.currentTimeMillis();
							TimeLine oTimeLine = new TimeLine();
							oTimeLine.addTimeLine("" + (lAfter - lBefore));
							oDemand.addTimeLine(oTimeLine);
							this.oResultPool.put(oDemand);
//							synchronized(this.oHandler)
//							{
//								GlobalDef.slNumComputedDemands++;
//							}
						//	oDemand.addTimeLine("after");
							lSendingCount++;
							break;
						}
						catch(DMSException oException)
						{
							if(oHandler != null && i<3)
							{
								if(i == 0)
								{
									oRecord.put(GlobalDef.slNumComputedDemands, lErrorCount);
									lErrorCount ++;
								}
															
								try 
								{
									oTA = oHandler.fixTA(oTA, oException);
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
					
					//oDemand = GlobalDef.soDemandDispatcher.readResult(oSignature);
					
					if(oDemand instanceof SizeAdjustableDemand)
					{
						((SizeAdjustableDemand)oDemand).clearPayload();
					}
					
					//this.oResultPool.put(oDemand);
					
					if(this.iTesterID == 0)
					{
						if(lSendingCount % 100 == 0)
						{
							//oResultPool.updateGUI();
							//oDemandPool.updateGUI();
							System.gc();
							
							GlobalDef.soStatisticsUpdator.updateStatisticsInfo();
						}
						
					}
					
					
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
					
					oDemand = this.oDemandPool.get();
				}
				
				
			}
			
			//**** gives a chance to others to run on green threads Java
			yield();
		}
		
		
		
	}
	
	
}
