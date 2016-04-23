package gipsy.tests.junit.interfaces;

import junit.framework.Assert;
import junit.framework.TestCase;
import gipsy.interfaces.LocalGEERPool;
import gipsy.interfaces.GIPSYProgram;
import gipsy.interfaces.GEERSignature;

public class LocalGEERPoolTest extends TestCase {

	LocalGEERPool oGEERPool_1 = new LocalGEERPool();
	LocalGEERPool oGEERPool_2 = new LocalGEERPool();
	
	GIPSYProgram oGIPSYProgram_1 = new GIPSYProgram();
//	oGIPSYProgram_1.setOSignature(new GEERSignature("GEER_1"));
	
	GIPSYProgram oGIPSYProgram_2 = new GIPSYProgram();
//	oGIPSYProgram_2.setOSignature(new GEERSignature("GEER_2"));
	
	public void testPut()
	{	
		oGEERPool_1.put(oGIPSYProgram_1.getSignature(), oGIPSYProgram_1);
		oGEERPool_2.put(oGIPSYProgram_1.getSignature(), oGIPSYProgram_1);
		
		Assert.assertTrue(oGEERPool_1.equals(oGEERPool_2));
		
		oGEERPool_2.put(oGIPSYProgram_2.getSignature(), oGIPSYProgram_2);
		
		Assert.assertFalse(oGEERPool_1.equals(oGEERPool_2));
	}
	
	public void testContains() {
		oGEERPool_1.put(oGIPSYProgram_1.getSignature(), oGIPSYProgram_1);
		
		Assert.assertTrue(oGEERPool_1.contains(oGIPSYProgram_1.getSignature()));
		Assert.assertFalse(oGEERPool_1.contains(oGIPSYProgram_2.getSignature()));
	}
	
	public void testIsEmpty()
	{
		Assert.assertTrue(oGEERPool_1.isEmpty());
		
		oGEERPool_1.put(oGIPSYProgram_1.getSignature(), oGIPSYProgram_1);
		Assert.assertFalse(oGEERPool_1.isEmpty());
	}
	
	public void testGet()
	{
		oGEERPool_1.put(oGIPSYProgram_1.getSignature(), oGIPSYProgram_1);
		
		Assert.assertTrue(oGIPSYProgram_1.equals(oGEERPool_1.get(oGIPSYProgram_1.getSignature())));
		Assert.assertFalse(oGIPSYProgram_2.equals(oGEERPool_1.get(oGIPSYProgram_2.getSignature())));
	}
	
	public void testRemove()
	{
		oGEERPool_1.put(oGIPSYProgram_1.getSignature(), oGIPSYProgram_1);
		oGEERPool_1.put(oGIPSYProgram_2.getSignature(), oGIPSYProgram_2);
		
		oGEERPool_2.put(oGIPSYProgram_2.getSignature(), oGIPSYProgram_2);
		
		Assert.assertFalse(oGEERPool_1.equals(oGEERPool_2));
		
		oGEERPool_1.remove(oGIPSYProgram_1.getSignature());
		Assert.assertTrue(oGEERPool_1.equals(oGEERPool_2));
	}
}