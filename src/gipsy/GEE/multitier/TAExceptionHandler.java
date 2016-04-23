package gipsy.GEE.multitier;

import gipsy.Configuration;
import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.multitier.DST.TAFactory;
import gipsy.GEE.multitier.GMT.demands.DSTIssueReport;
import gipsy.util.Trace;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import marf.util.BaseThread;

/**
 * The class who handles TA exceptions. It provides a method for GIPSY tiers to
 * report the problematic TA and tries to fix the TA.
 * 
 * @author Yi Ji
 * @version $Id: TAExceptionHandler.java,v 1.12 2012/03/31 00:08:32 mokhov Exp $
 */
public class TAExceptionHandler 
extends BaseThread 
{
	private Map<Configuration, Long> oIssueLog = new HashMap<Configuration, Long>();
	private Map<Configuration, DMSException> oIssueReport = new HashMap<Configuration, DMSException>();
	private Map<Configuration, Configuration> oIssueFix = new WeakHashMap<Configuration, Configuration>();
	private Object oIssueMonitor = new Object();
	
	private Configuration oRegDSTTAConfig = null;
	private ITransportAgent oRegDSTTA = null;
	
	private String strNodeID = null;
	private String strGMTTierID = null;
	
	/**
	 * For logging
	 */
	private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";
	
	TAExceptionHandler(String pstrNodeID, String pstrGMTTierID, ITransportAgent poRegDSTTA)
	{
		this.strNodeID = pstrNodeID;
		this.strGMTTierID = pstrGMTTierID;
		this.oRegDSTTA = poRegDSTTA;
		this.oRegDSTTAConfig = this.oRegDSTTA.getConfiguration();
	}
	
	@Override
	public void run() 
	{
		try 
		{
			while(true)
			{
				synchronized(this.oIssueMonitor)
				{
					this.oIssueMonitor.wait(20000);
				}
				
				int iNoIssueCounter = 0;
				
				while(true)
				{
					Thread.sleep(1000); // Allow sometime to collect more reports.
					
					iNoIssueCounter++;
					
					synchronized(this.oIssueLog)
					{
						//System.err.println("IssueHandler waken up, now searching reports... ");
						
						// Whenever waken up, it has to work.
						Set<Entry<Configuration, Long>> oIssueReports = 
							this.oIssueLog.entrySet();
						
						Iterator<Entry<Configuration, Long>> oIterator = 
							oIssueReports.iterator();
						
						while(oIterator.hasNext())
						{
							Entry<Configuration, Long> oReport = oIterator.next();
							
							// When a newly reported problem found;
							if(oReport.getValue() == null)
							{
								iNoIssueCounter = 0;
								
								System.err.println(MSG_PREFIX + "An TA issue report found, fixing ... ");
								
								Configuration oIssueConfig = oReport.getKey();
								
								// If it is the registration DST
								if(oIssueConfig.equals(this.oRegDSTTAConfig))
								{
									/*
									 * Wait for the DST to recover.
									 */
									Thread.sleep(5000);								
									
									try 
									{
										// Test if the TA has recovered.
										this.oRegDSTTA.setConfiguration(oRegDSTTAConfig);
										this.oIssueLog.put(oIssueConfig, System.currentTimeMillis());
										this.oIssueFix.put(oIssueConfig, oRegDSTTAConfig);
										System.err.println(MSG_PREFIX + "A fix (old TA config) was provided.");
									} 
									catch (DMSException oException) 
									{
										/* 
										 * Do nothing and hope that the problem will be solved in
										 * next rounds.
										 */
										System.err.println(MSG_PREFIX + "No fix (TA config) found in this round.");
									}
								}
								else 
								{
									DMSException oTAException = this.oIssueReport.remove(oIssueConfig);
									
									/*
									 * Currently all issue reports go to the registration DST
									 * so if it is not working, simply wait for next rounds 
									 * until it becomes working.
									 */
									DSTIssueReport oCrashReport = 
										new DSTIssueReport(this.strNodeID, 
												this.strGMTTierID, 
												oIssueConfig,
												oTAException);
									
									try 
									{
										// Generate a DST crash report and send it to GMT
										System.err.println(MSG_PREFIX + "Sending crash report to GMT ...");
										
										DemandSignature oSig = this.oRegDSTTA.setDemand(oCrashReport);
										oCrashReport = (DSTIssueReport) this.oRegDSTTA.getResult(oSig);
										System.err.println(MSG_PREFIX + "Received a fix (TA config) from GMT");
										
										Configuration oFixedTAConfig = oCrashReport.getCorrectedTAConfig();
										this.oIssueLog.put(oIssueConfig, System.currentTimeMillis());
										this.oIssueFix.put(oIssueConfig, oFixedTAConfig);
										
										System.err.println(MSG_PREFIX + "A fix (new TA config) was provided.");
									} 
									catch (DMSException oException) 
									{
										System.err.println(MSG_PREFIX + "No fix (TA config) found in this round.");
									}
									
								}
							}
							
							//System.err.println("IssueHandler: report processing finished a round");
						}
						this.oIssueLog.notifyAll();
					}
				}
			}
			
		} 
		catch (InterruptedException oException) 
		{
			oException.printStackTrace();
		}
			
	}
	
	public ITransportAgent fixTA(ITransportAgent poTA, DMSException poException)
	throws InterruptedException
	{	
		long lNowTime = System.currentTimeMillis();
		Configuration oTAConfig = poTA.getConfiguration();
		String strOriginalTAClassName = 
			poTA.getConfiguration().getProperty(ITransportAgent.TA_IMPL_CLASS);
		long lFixTimeout = 15000;
		
		String strCaller = sun.reflect.Reflection.getCallerClass(2).getSimpleName();
		strCaller = "[" + strCaller + "] with TA " + poTA.hashCode() + "";
		
		System.err.println(strCaller + ": Got a TA exception!");
		
		synchronized(this.oIssueLog)
		{
			// If someone has reported this problem
			if(this.oIssueLog.containsKey(oTAConfig))
			{
				Long oLastFixTime = this.oIssueLog.get(oTAConfig);
				
				// If the problem has been fixed
				if(oLastFixTime != null)
				{
					// If it has been a while since last fixed time
					if((lNowTime - oLastFixTime) > lFixTimeout)
					{
						// Report the problem again
						this.oIssueLog.put(oTAConfig, null);
						this.oIssueReport.put(oTAConfig, poException);
						
						Thread.yield();
						
						synchronized(oIssueMonitor)
						{
							oIssueMonitor.notify();
						}
						
						System.err.println(strCaller + ": Reported the TA exception, now waiting ...");
						this.oIssueLog.wait(2*lFixTimeout);
						// When waken up, goto -> *
					}
					else
					{
						// If the fix is still fresh, pause then goto -> *
						Thread.sleep(3000);
					}
					
				}
				// If the problem has not been fixed
				else
				{
					// Simply wait for the fix
					System.err.println(strCaller + ": The TA exception already reported by someone else, now waiting ...");
					this.oIssueLog.wait(2*lFixTimeout);
					// When waken up, goto -> *
				}
			}
			// If no one has reported this problem 
			else
			{
				// Report the problem
				this.oIssueLog.put(oTAConfig, null);
				this.oIssueReport.put(oTAConfig, poException);
				
				Thread.yield();
				
				synchronized(oIssueMonitor)
				{
					oIssueMonitor.notify();
				}
				System.err.println(strCaller + ": Reported the TA exception, now waiting ...");
				this.oIssueLog.wait(2*lFixTimeout);
				// When waken up, goto -> *
			}
			
			// -> * Try the fix
			System.err.println(strCaller + ": Found a fix (TA config), now trying!");
			oTAConfig = oIssueFix.get(oTAConfig);
			
			try 
			{
				// Check if the TA configuration is for the same TA implementation
				String strNewTAClassName = oTAConfig.getProperty(ITransportAgent.TA_IMPL_CLASS);
				if(strNewTAClassName.equals(strOriginalTAClassName))
				{
					poTA.setConfiguration(oTAConfig);
				}
				else
				{
					poTA = TAFactory.getInstance().createTA(oTAConfig);
				}
				System.err.println(strCaller + ": The fix (TA config) passed the trail!");
			} 
			catch (Exception oException) 
			{
				System.err.println(strCaller + ": The fix (TA config) did not pass the trail!");
				/* 
				 * Even if the TA is not working, simply return so it can be 
				 * reported again
				 */
			}
			
			return poTA;
		}
	}
}
