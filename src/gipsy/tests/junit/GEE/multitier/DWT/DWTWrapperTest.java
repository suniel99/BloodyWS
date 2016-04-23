package gipsy.tests.junit.GEE.multitier.DWT;

import static org.junit.Assert.*;
import junit.framework.Assert;
import gipsy.Configuration;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JiniDemandDispatcher;
import gipsy.GEE.IDP.DemandGenerator.jms.JMSDemandDispatcher;
import gipsy.GEE.IDP.demands.Demand;
import gipsy.GEE.IDP.demands.IntensionalDemand;
import gipsy.GEE.IDP.demands.ProceduralDemand;
import gipsy.GEE.multitier.DGT.DGTWrapper;
import gipsy.GEE.multitier.DWT.DWTWrapper;
import gipsy.interfaces.GIPSYProgram;
import gipsy.interfaces.LocalDemandStore;
import gipsy.interfaces.LocalGEERPool;

import org.junit.Test;

public class DWTWrapperTest {
	
	LocalGEERPool oLocalGEERPool_1 = new LocalGEERPool();
	LocalGEERPool oLocalGEERPool_2 = new LocalGEERPool();
	LocalDemandStore oLocalDemandStore_1 = new LocalDemandStore();
	LocalDemandStore oLocalDemandStore_2 = new LocalDemandStore();

	IDemandDispatcher oJiniDemandDispatcher;
	IDemandDispatcher oJMSDemandDispatcher;
	Configuration oConfiguration = new Configuration();
	
	DWTWrapper oDWTWrapper_1;
	DWTWrapper oDWTWrapper_2;
	
	Demand oProcedural_1 = new ProceduralDemand();
	Demand oProcedural_2 = new ProceduralDemand();

	GIPSYProgram oGIPSYProgram_1 = new GIPSYProgram();
	GIPSYProgram oGIPSYProgram_2 = new GIPSYProgram();
	
	/**
	 * Test case for DSTWrapper constructor.
	 * Make sure start the JINI/JMS service before running the test case.
	 */
	@Test
	public void testDWTWrapper() {
		try
		{
			oJiniDemandDispatcher = new JiniDemandDispatcher();
			oJMSDemandDispatcher = new JMSDemandDispatcher();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		oDWTWrapper_1 = new DWTWrapper(oLocalGEERPool_1, oLocalDemandStore_1, oJiniDemandDispatcher, oConfiguration);
		oDWTWrapper_2 = new DWTWrapper(oLocalGEERPool_1, oLocalDemandStore_1, oJiniDemandDispatcher, oConfiguration);
		
		Assert.assertTrue(oDWTWrapper_1.equals(oDWTWrapper_2));
		
		oDWTWrapper_2.setDemandDispatcher(oJMSDemandDispatcher);
		Assert.assertFalse(oDWTWrapper_1.equals(oDWTWrapper_2));
	}
	@Test
	public void testPutDemand()
	{
		try
		{
			oJiniDemandDispatcher = new JiniDemandDispatcher();
			oJMSDemandDispatcher = new JMSDemandDispatcher();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		oLocalDemandStore_2.put(oProcedural_1.getSignature(), oProcedural_1);
		
		oDWTWrapper_1 = new DWTWrapper(oLocalGEERPool_1, oLocalDemandStore_1, oJiniDemandDispatcher, oConfiguration);
		oDWTWrapper_2 = new DWTWrapper(oLocalGEERPool_1, oLocalDemandStore_2, oJiniDemandDispatcher, oConfiguration);
		
		oDWTWrapper_1.putDemand(oProcedural_1.getSignature(), oProcedural_1);
		
		Assert.assertTrue(oDWTWrapper_1.equals(oDWTWrapper_2));
		
		oDWTWrapper_1.putDemand(oProcedural_2.getSignature(), oProcedural_2);
		Assert.assertFalse(oDWTWrapper_1.equals(oDWTWrapper_2));
	}
	
	@Test
	public void testContainsDemand()
	{
		try
		{
			oJiniDemandDispatcher = new JiniDemandDispatcher();
			oJMSDemandDispatcher = new JMSDemandDispatcher();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		oDWTWrapper_1 = new DWTWrapper(oLocalGEERPool_1, oLocalDemandStore_1, oJiniDemandDispatcher, oConfiguration);
		oDWTWrapper_1.putDemand(oProcedural_1.getSignature(), oProcedural_1);
		
		Assert.assertTrue(oDWTWrapper_1.containsDemand(oProcedural_1.getSignature()));
		Assert.assertFalse(oDWTWrapper_1.containsDemand(oProcedural_2.getSignature()));
	}
	
	@Test
	public void testGetDemand()
	{
		try
		{
			oJiniDemandDispatcher = new JiniDemandDispatcher();
			oJMSDemandDispatcher = new JMSDemandDispatcher();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		oDWTWrapper_1 = new DWTWrapper(oLocalGEERPool_1, oLocalDemandStore_1, oJiniDemandDispatcher, oConfiguration);	
		oDWTWrapper_1.putDemand(oProcedural_1.getSignature(), oProcedural_1);
		
		Assert.assertTrue(oProcedural_1.equals(oDWTWrapper_1.getDemand(oProcedural_1.getSignature())));
		Assert.assertFalse(oProcedural_2.equals(oDWTWrapper_1.getDemand(oProcedural_1.getSignature())));
	}	
	
	@Test
	public void testPutGEER()
	{
		try
		{
			oJiniDemandDispatcher = new JiniDemandDispatcher();
			oJMSDemandDispatcher = new JMSDemandDispatcher();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		oLocalGEERPool_2.put(oGIPSYProgram_1.getSignature(), oGIPSYProgram_1);
		
		oDWTWrapper_1 = new DWTWrapper(oLocalGEERPool_1, oLocalDemandStore_1, oJiniDemandDispatcher, oConfiguration);
		oDWTWrapper_2 = new DWTWrapper(oLocalGEERPool_2, oLocalDemandStore_1, oJiniDemandDispatcher, oConfiguration);
		
		oDWTWrapper_1.putGEER(oGIPSYProgram_1.getSignature(), oGIPSYProgram_1);
		
		Assert.assertTrue(oDWTWrapper_1.equals(oDWTWrapper_2));
		
		oDWTWrapper_1.putGEER(oGIPSYProgram_2.getSignature(), oGIPSYProgram_2);
		Assert.assertFalse(oDWTWrapper_1.equals(oDWTWrapper_2));
	}
	
	@Test
	public void testContainsGEER()
	{
		try
		{
			oJiniDemandDispatcher = new JiniDemandDispatcher();
			oJMSDemandDispatcher = new JMSDemandDispatcher();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		oDWTWrapper_1 = new DWTWrapper(oLocalGEERPool_1, oLocalDemandStore_1, oJiniDemandDispatcher, oConfiguration);
		oDWTWrapper_1.putGEER(oGIPSYProgram_1.getSignature(), oGIPSYProgram_1);
		
		Assert.assertTrue(oDWTWrapper_1.containsGEER(oGIPSYProgram_1.getSignature()));
		Assert.assertFalse(oDWTWrapper_1.containsGEER(oGIPSYProgram_2.getSignature()));
	}
	
	@Test
	public void testGetGEER() {
		try
		{
			oJiniDemandDispatcher = new JiniDemandDispatcher();
			oJMSDemandDispatcher = new JMSDemandDispatcher();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		oDWTWrapper_1 = new DWTWrapper(oLocalGEERPool_1, oLocalDemandStore_1, oJiniDemandDispatcher, oConfiguration);
		oDWTWrapper_1.putGEER(oGIPSYProgram_1.getSignature(), oGIPSYProgram_1);
		
		Assert.assertTrue(oGIPSYProgram_1.equals(oDWTWrapper_1.getGEER(oGIPSYProgram_1.getSignature())));
		Assert.assertFalse(oGIPSYProgram_1.equals(oDWTWrapper_1.getGEER(oGIPSYProgram_2.getSignature())));
	}
}
