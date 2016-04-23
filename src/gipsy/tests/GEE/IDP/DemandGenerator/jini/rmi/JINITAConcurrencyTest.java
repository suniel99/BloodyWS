package gipsy.tests.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITA;
import gipsy.GEE.IDP.demands.IDemand;
import junit.framework.TestCase;

public class JINITAConcurrencyTest extends TestCase {

	ITransportAgent oJTA = null;
	IDemand oDemand = null;
	static final String sstrDemandBase = "gipsy.GEE.IDP.DemandGenerator.simulator.demands.";
	static final int iNum = 2;
	
	protected void setUp() 
	throws Exception 
	{
		super.setUp();
	}

	protected void tearDown() 
	throws Exception 
	{
		super.tearDown();
	}

	/**
	 * Test the concurrency
	 */
	public void testConcurrency()
	{
		DemandSender[] aoSenders = new DemandSender[iNum];
		DemandWorker[] aoWorkers = new DemandWorker[iNum];
		
		for(int i = 0; i<iNum; i++)
		{
			// Get a TA:
			ITransportAgent oJTA = null;
			try 
			{
				oJTA = new JINITA();
			} 
			catch (Exception e1) 
			{
				e1.printStackTrace(System.err);
				fail("Exception found during JINITA initialization!");
			}
			aoSenders[i] = new DemandSender(oDemand, oJTA);
			aoWorkers[i] = new DemandWorker(oJTA);
		}
		
		for(int i = 0; i<iNum; i++)
		{
			aoSenders[i].start();
			aoWorkers[i].start();
		}
		
		for(int i = 0; i<iNum; i++)
		{
			try 
			{
				aoSenders[i].join(3000);
				aoWorkers[i].join(3000);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace(System.err);
			}
		}
	}
}
