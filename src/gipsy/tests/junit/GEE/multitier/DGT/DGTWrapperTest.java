package gipsy.tests.junit.GEE.multitier.DGT;

import gipsy.Configuration;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JiniDemandDispatcher;
import gipsy.GEE.IDP.DemandGenerator.jms.JMSDemandDispatcher;
import gipsy.GEE.IDP.demands.Demand;
import gipsy.GEE.IDP.demands.IntensionalDemand;
import gipsy.GEE.multitier.DGT.DGTWrapper;
import gipsy.interfaces.GIPSYProgram;
import gipsy.interfaces.LocalDemandStore;
import gipsy.interfaces.LocalGEERPool;
import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;


/**
 * Make sure start the JINI/JMS service before running the test case.
 * 
 * @author Bin Han
 * @version $Id: DGTWrapperTest.java,v 1.4 2010/12/08 18:39:14 mokhov Exp $
 * @since
 */
public class DGTWrapperTest extends TestCase
{
	LocalGEERPool oLocalGEERPool_1 = new LocalGEERPool();
	LocalGEERPool oLocalGEERPool_2 = new LocalGEERPool();
	LocalDemandStore oLocalDemandStore_1 = new LocalDemandStore();
	LocalDemandStore oLocalDemandStore_2 = new LocalDemandStore();

	IDemandDispatcher oJiniDemandDispatcher;
	IDemandDispatcher oJMSDemandDispatcher;
	Configuration oConfiguration = new Configuration();
	
	DGTWrapper oDGTWrapper_1;
	DGTWrapper oDGTWrapper_2;
	
	Demand oIntensional_1 = new IntensionalDemand();
	Demand oIntensional_2 = new IntensionalDemand();

	GIPSYProgram oGIPSYProgram_1 = new GIPSYProgram();
	GIPSYProgram oGIPSYProgram_2 = new GIPSYProgram();
	
	/**
	 * Test case for DSTWrapper constructor.
	 * Make sure start the JINI/JMS service before running the test case.
	 */
	@Test
	public void testDGTWrapper()
	{
		try
		{
			this.oJiniDemandDispatcher = new JiniDemandDispatcher();
			this.oJMSDemandDispatcher = new JMSDemandDispatcher();
		
			this.oDGTWrapper_1 = new DGTWrapper(oLocalGEERPool_1, oLocalDemandStore_1, oJiniDemandDispatcher, oConfiguration);
			this.oDGTWrapper_2 = new DGTWrapper(oLocalGEERPool_1, oLocalDemandStore_1, oJiniDemandDispatcher, oConfiguration);
			
			Assert.assertTrue(this.oDGTWrapper_1.equals(this.oDGTWrapper_2));
			
			this.oDGTWrapper_2.setDemandDispatcher(this.oJMSDemandDispatcher);
			Assert.assertFalse(oDGTWrapper_1.equals(this.oDGTWrapper_2));
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}
	
	@Test
	public void testPutDemand()
	{
		try
		{
			this.oJiniDemandDispatcher = new JiniDemandDispatcher();
			this.oJMSDemandDispatcher = new JMSDemandDispatcher();
			
			this.oLocalDemandStore_2.put(this.oIntensional_1.getSignature(), this.oIntensional_1);
			
			this.oDGTWrapper_1 = new DGTWrapper(this.oLocalGEERPool_1, this.oLocalDemandStore_1, this.oJiniDemandDispatcher, this.oConfiguration);
			this.oDGTWrapper_2 = new DGTWrapper(this.oLocalGEERPool_1, this.oLocalDemandStore_2, this.oJiniDemandDispatcher, this.oConfiguration);
			
			this.oDGTWrapper_1.putDemand(this.oIntensional_1.getSignature(), this.oIntensional_1);
			
			Assert.assertTrue(this.oDGTWrapper_1.equals(this.oDGTWrapper_2));
			
			this.oDGTWrapper_1.putDemand(this.oIntensional_2.getSignature(), this.oIntensional_2);
			Assert.assertFalse(this.oDGTWrapper_1.equals(this.oDGTWrapper_2));
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}
	
	@Test
	public void testContainsDemand()
	{
		try
		{
			this.oJiniDemandDispatcher = new JiniDemandDispatcher();
			this.oJMSDemandDispatcher = new JMSDemandDispatcher();

			this.oDGTWrapper_1 = new DGTWrapper(this.oLocalGEERPool_1, this.oLocalDemandStore_1, this.oJiniDemandDispatcher, this.oConfiguration);
			this.oDGTWrapper_1.putDemand(this.oIntensional_1.getSignature(), this.oIntensional_1);
			
			Assert.assertTrue(this.oDGTWrapper_1.containsDemand(this.oIntensional_1.getSignature()));
			Assert.assertFalse(this.oDGTWrapper_1.containsDemand(this.oIntensional_2.getSignature()));
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}
	
	@Test
	public void testGetDemand()
	{
		try
		{
			this.oJiniDemandDispatcher = new JiniDemandDispatcher();
			this.oJMSDemandDispatcher = new JMSDemandDispatcher();

			this.oDGTWrapper_1 = new DGTWrapper(this.oLocalGEERPool_1, this.oLocalDemandStore_1, this.oJiniDemandDispatcher, this.oConfiguration);	
			this.oDGTWrapper_1.putDemand(this.oIntensional_1.getSignature(), this.oIntensional_1);
			
			Assert.assertTrue(this.oIntensional_1.equals(this.oDGTWrapper_1.getDemand(this.oIntensional_1.getSignature())));
			Assert.assertFalse(this.oIntensional_2.equals(this.oDGTWrapper_1.getDemand(this.oIntensional_1.getSignature())));
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}	
	
	@Test
	public void testPutGEER()
	{
		try
		{
			this.oJiniDemandDispatcher = new JiniDemandDispatcher();
			this.oJMSDemandDispatcher = new JMSDemandDispatcher();

			this.oLocalGEERPool_2.put(oGIPSYProgram_1.getSignature(), oGIPSYProgram_1);
			
			this.oDGTWrapper_1 = new DGTWrapper(this.oLocalGEERPool_1, this.oLocalDemandStore_1, this.oJiniDemandDispatcher, this.oConfiguration);
			this.oDGTWrapper_2 = new DGTWrapper(this.oLocalGEERPool_2, this.oLocalDemandStore_1, this.oJiniDemandDispatcher, this.oConfiguration);
			
			this.oDGTWrapper_1.putGEER(oGIPSYProgram_1.getSignature(), oGIPSYProgram_1);
			
			Assert.assertTrue(this.oDGTWrapper_1.equals(this.oDGTWrapper_2));
			
			this.oDGTWrapper_1.putGEER(oGIPSYProgram_2.getSignature(), this.oGIPSYProgram_2);
			Assert.assertFalse(oDGTWrapper_1.equals(oDGTWrapper_2));
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}
	
	@Test
	public void testContainsGEER()
	{
		try
		{
			this.oJiniDemandDispatcher = new JiniDemandDispatcher();
			this.oJMSDemandDispatcher = new JMSDemandDispatcher();

			this.oDGTWrapper_1 = new DGTWrapper(this.oLocalGEERPool_1, this.oLocalDemandStore_1, this.oJiniDemandDispatcher, this.oConfiguration);
			this.oDGTWrapper_1.putGEER(this.oGIPSYProgram_1.getSignature(), this.oGIPSYProgram_1);
			
			Assert.assertTrue(this.oDGTWrapper_1.containsGEER(this.oGIPSYProgram_1.getSignature()));
			Assert.assertFalse(this.oDGTWrapper_1.containsGEER(this.oGIPSYProgram_2.getSignature()));
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}
	
	@Test
	public void testGetGEER()
	{
		try
		{
			this.oJiniDemandDispatcher = new JiniDemandDispatcher();
			this.oJMSDemandDispatcher = new JMSDemandDispatcher();

			this.oDGTWrapper_1 = new DGTWrapper(this.oLocalGEERPool_1, this.oLocalDemandStore_1, this.oJiniDemandDispatcher, this.oConfiguration);
			this.oDGTWrapper_1.putGEER(this.oGIPSYProgram_1.getSignature(), this.oGIPSYProgram_1);
			
			Assert.assertTrue(this.oGIPSYProgram_1.equals(this.oDGTWrapper_1.getGEER(this.oGIPSYProgram_1.getSignature())));
			Assert.assertFalse(this.oGIPSYProgram_1.equals(this.oDGTWrapper_1.getGEER(this.oGIPSYProgram_2.getSignature())));
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}
}
