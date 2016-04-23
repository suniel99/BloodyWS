/**
 * 
 */
package gipsy.tests.GEE.IDP.DemandGenerator.jms;

import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.IJINITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jms.JMSTransportAgent;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.tests.GEE.IDP.DemandGenerator.jini.rmi.DemandSender;
import gipsy.tests.GEE.IDP.DemandGenerator.jini.rmi.DemandWorker;
import gipsy.tests.GEE.IDP.demands.DemandTest;
import junit.framework.Assert;
import junit.framework.TestCase;
/**
 * @author Yi Ji
 *
 */
public class JMSTransportAgentTest extends TestCase {

	ITransportAgent oJTA = null;
	IDemand oDemand = null;
	static final String sstrDemandBase = "gipsy.GEE.IDP.DemandGenerator.simulator.demands.";
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() 
	throws Exception 
	{
		oDemand = new DemandTest("jms");
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
	 * Test method for {@link gipsy.GEE.IDP.DemandGenerator.jms.JMSTransportAgent}.
	 */
	public final void testAllFlow() 
	{
		// Get a TA:
		try 
		{
			oJTA = new JMSTransportAgent();
		} 
		catch (DMSException e1) 
		{
			e1.printStackTrace(System.err);
			fail("Exception found during JINITA initialization!");
		}
		// Get a DemandSender instance from this test package
		DemandSender oSender = new DemandSender(oDemand, oJTA);
	
		// Get a DemandWorker instance from this test package
		DemandWorker oWorker = new DemandWorker(oJTA);
		
		// Now run!
		oSender.start();
		oWorker.start();
		// Wait until all finish
		try 
		{
			oSender.join(6000);
			oWorker.join(6000);
			//oJTA.release();
		} 
		catch (Exception e) 
		{
			e.printStackTrace(System.err);
		}
		
		Assert.assertEquals(oSender.getoResult().getSignature(),
				oDemand.getSignature());
		
		if(oWorker.getoResult() != null)
		{
			// By writing this test, although the field should be
			// the same, the object is a different object once it
			// gets through the JavaSpace. So the signature is used
			// instead of comparing the object directly.
			Assert.assertEquals(oSender.getoResult().getSignature(), 
					oWorker.getoResult().getSignature());
		}
	}
}
