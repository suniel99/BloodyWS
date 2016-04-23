/**
 * 
 */
package gipsy.tests.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITA;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Before running this test, please make sure that the Jini services
 * are already running.
 * 
 * @author ji_yi
 * @version $Id: JiniTransportAgentTest.java,v 1.6 2010/09/11 23:54:42 ji_yi Exp $
 */
public class JiniTransportAgentTest extends TestCase {

	ITransportAgent oJTA = null;
	IDemand oDemand = null;
	static final String sstrDemandBase = "gipsy.GEE.IDP.DemandGenerator.simulator.demands.";
	static final int iNum = 2;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() 
	throws Exception 
	{
		// Get a Demand;
		String strClassName = "WorkDemandHD";
		oDemand = (IDemand)Class.forName(sstrDemandBase + strClassName).newInstance();
		oDemand.setSignature(new DemandSignature(strClassName));
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
	 * Test method for {@link gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITA} in a roll
	 */
	public void testAllFlow()
	{
		// Get a TA:
		try 
		{
			oJTA = new JINITA();
		} 
		catch (Exception e1) 
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
			oSender.join(3000);
			oWorker.join(3000);
		} 
		catch (InterruptedException e) 
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
