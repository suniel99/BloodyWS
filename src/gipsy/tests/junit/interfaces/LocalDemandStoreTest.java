package gipsy.tests.junit.interfaces;

import java.util.Hashtable;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.interfaces.LocalDemandStore;
import gipsy.GEE.IDP.demands.*;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.ThisExpr;

public class LocalDemandStoreTest extends TestCase {
	LocalDemandStore oStore_1 = new LocalDemandStore();
	LocalDemandStore oStore_2 = new LocalDemandStore();
	
	Demand oIntensional_1 = new IntensionalDemand();
	Demand oIntensional_2 = new IntensionalDemand();
	
	Demand oProcedural = new ProceduralDemand();
	Demand oResource = new ResourceDemand();
	
	public void testPut() {
		this.oStore_1.put(this.oIntensional_1.getSignature(), this.oIntensional_1);
		this.oStore_2.put(this.oIntensional_1.getSignature(), this.oIntensional_1);
		
		Assert.assertTrue(oStore_1.equals(oStore_2));
		
		this.oStore_1.put(this.oIntensional_2.getSignature(), this.oIntensional_2);
		Assert.assertFalse(oStore_1.equals(oStore_2));
	}
	
	public void testContains() {
		this.oStore_1.put(this.oIntensional_1.getSignature(), this.oIntensional_1);
		
		Assert.assertTrue(this.oStore_1.contains(oIntensional_1.getSignature()));
		Assert.assertFalse(this.oStore_1.contains(oIntensional_2.getSignature()));
	}
	
	public void testGet() {
		this.oStore_1.put(this.oIntensional_1.getSignature(), this.oIntensional_1);
		
		Assert.assertTrue(oIntensional_1.equals(this.oStore_1.get(this.oIntensional_1.getSignature())));
	}
	
	public void testRemove() {
		this.oStore_1.put(this.oIntensional_1.getSignature(), this.oIntensional_1);
		this.oStore_1.put(this.oIntensional_2.getSignature(), this.oIntensional_2);
		Assert.assertTrue(oStore_1.contains(oIntensional_2.getSignature()));
		
		this.oStore_1.remove(this.oIntensional_1.getSignature());
		Assert.assertFalse(oStore_1.contains(oIntensional_1.getSignature()));
	}
	
	public static void main(String[] args) {
//		Demand iD = new IntensionalDemand();
//		iD.setSignature(new DemandSignature("HelloWorld_1"));
		
//		Demand pD = new ProceduralDemand();
//		pD.setSignature(new DemandSignature("HelloWorld_2"));
		
//		System.out.println("Constructed sig i: " + iD.getSignature());
//		System.out.println("Constructed sig d: " + pD.getSignature());
		
//		System.out.println("Sig i identity hashcode: " + System.identityHashCode(iD));
//		System.out.println("Sig i          hashcode: " + iD.hashCode());

//		System.out.println("Sig p identity hashcode: " + System.identityHashCode(pD));
//		System.out.println("Sig p          hashcode: " + pD.hashCode());

//		System.out.println("Sig i get sig get sig  : " + iD.getSignature().getSignature());
//		System.out.println("Sig p get sig hashcode : " + pD.getSignature().hashCode());
		
//		System.out.println("    i ==     p         : " + iD.equals(pD));
//		System.out.println("Sig i == Sig p         : " + iD.getSignature().equals(pD.getSignature()));
		
		LocalDemandStore lds_1 = new LocalDemandStore();
		LocalDemandStore lds_2 = new LocalDemandStore();
		
		System.out.println(lds_1);
		System.out.println(lds_2);
		System.out.println(lds_1.equals(lds_2));
		
		Hashtable<DemandSignature, IDemand> map_1 = new Hashtable();
		Hashtable<DemandSignature, IDemand> map_2 = new Hashtable();
		System.out.println(map_1);
		System.out.println(map_2);
		System.out.println(map_1.equals(map_2));
	}
}