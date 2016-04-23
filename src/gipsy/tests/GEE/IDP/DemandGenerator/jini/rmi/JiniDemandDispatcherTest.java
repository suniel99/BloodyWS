package gipsy.tests.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcher;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.IJINITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITA;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JiniDemandDispatcher;
import gipsy.GEE.IDP.DemandGenerator.jms.JMSDemandDispatcher;
import gipsy.GEE.IDP.DemandGenerator.jms.JMSTransportAgent;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.tests.GEE.IDP.demands.DemandTest;
import junit.framework.Assert;
import junit.framework.TestCase;


/**
 * JUnit-test of the Jini and JMS Demand Migration Systems.
 * The precondition of running this test is that the Jini
 * and JMS services are properly configured and running.
 * Otherwise it is deemed to fail.
 * 
 * @author Yi Ji
 * @version $Id: JiniDemandDispatcherTest.java,v 1.8 2010/12/20 03:44:33 ji_yi Exp $
 * @since
 */
public class JiniDemandDispatcherTest
extends TestCase
{
	private IDemand oDemand = null;
	private DemandDispatcher oDispatcher = null;
	private ITransportAgent oJTA = null;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp()
	throws Exception
	{
		
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown()
	throws Exception
	{
		super.tearDown();
	}

	/**
	 * Test method for {@link gipsy.GEE.IDP.DemandDispatcher#getValue(gipsy.GEE.IDP.IDemand)}.
	 */
	public final void testJiniDMS()
	{
		this.oDemand = new DemandTest("_1");
		try 
		{
			this.oJTA = new JINITA();
			this.oDispatcher = new JiniDemandDispatcher("", "");
		} 
		catch (Exception e) 
		{
			e.printStackTrace(System.err);
		}
	
		
		DemandDispatcherClientTest oClient = new DemandDispatcherClientTest(this.oDemand, this.oDispatcher);
		// Get a DemandWorker instance from this test package
		DemandWorker oWorker = new DemandWorker(oJTA);
		
		oClient.start();
		oWorker.start();
		
		// Wait until all finish
		try
		{
			oClient.join();
			oWorker.join(5000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace(System.err);
		}
		
		Assert.assertEquals(oClient.getoResult().getSignature(), this.oDemand.getSignature());
		
		if(oWorker.getoResult() != null)
		{
			// By writing this test, although the field should be
			// the same, the object is a different object once it
			// gets through the JavaSpace. So the signature is used
			// instead of comparing the object directly.
			Assert.assertEquals
			(
				oClient.getoResult().getSignature(),
				oWorker.getoResult().getSignature()
			);
		}
	}
	
	/**
	 * Test method for {@link gipsy.GEE.IDP.DemandDispatcher#getValue(gipsy.GEE.IDP.IDemand)}.
	 */
	public final void testJMSDMS()
	{
		this.oDemand = new DemandTest("_2");
		try 
		{
			this.oJTA = new JMSTransportAgent();
			this.oDispatcher = new JMSDemandDispatcher();
		} 
		catch (Exception e) 
		{
			e.printStackTrace(System.err);
		}
	
		
		DemandDispatcherClientTest oClient = new DemandDispatcherClientTest(this.oDemand, this.oDispatcher);
		// Get a DemandWorker instance from this test package
		DemandWorker oWorker = new DemandWorker(oJTA);
		
		oClient.start();
		oWorker.start();
		
		// Wait until all finish
		try
		{
			oClient.join();
			oWorker.join(5000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace(System.err);
		}
		
		Assert.assertEquals(oClient.getoResult().getSignature(), this.oDemand.getSignature());
		
		if(oWorker.getoResult() != null)
		{
			// By writing this test, although the field should be
			// the same, the object is a different object once it
			// gets through the JavaSpace. So the signature is used
			// instead of comparing the object directly.
			Assert.assertEquals
			(
				oClient.getoResult().getSignature(),
				oWorker.getoResult().getSignature()
			);
		}
	}
	
}

// EOF
