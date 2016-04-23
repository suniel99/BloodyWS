package gipsy.tests.junit.lang.context;

import gipsy.lang.GIPSYString;
import gipsy.lang.GIPSYType;
import gipsy.lang.context.UnorderedFinitePeriodicTagSet;
import junit.framework.Assert;
import junit.framework.TestCase;
import marf.util.FreeVector;


public class UnorderedFinitePeriodicTagSetTest extends TestCase{
	public void testEqualObject()
	{
		GIPSYString str1=new GIPSYString("black");
		GIPSYString str2=new GIPSYString("white");
		GIPSYString str3=new GIPSYString("red");
		FreeVector<GIPSYType> period1=new FreeVector<GIPSYType>();
		period1.addElement(str1);
		period1.addElement(str2);
		period1.addElement(str3);
		UnorderedFinitePeriodicTagSet testObject1=new UnorderedFinitePeriodicTagSet(period1,3);
		
		GIPSYString str4=new GIPSYString("white");
		GIPSYString str5=new GIPSYString("red");
		GIPSYString str6=new GIPSYString("black");
		FreeVector<GIPSYType> period2=new FreeVector<GIPSYType>();
		period2.addElement(str4);
		period2.addElement(str5);
		period2.addElement(str6);
		UnorderedFinitePeriodicTagSet testObject2=new UnorderedFinitePeriodicTagSet(period2,3);
		
		Assert.assertEquals(testObject1, testObject2);
	}
	
	public void testIsInTagSet()
	{
		GIPSYString str1=new GIPSYString("black");
		GIPSYString str2=new GIPSYString("white");
		GIPSYString str3=new GIPSYString("red");
		FreeVector<GIPSYType> period1=new FreeVector<GIPSYType>();
		period1.addElement(str1);
		period1.addElement(str2);
		period1.addElement(str3);
		UnorderedFinitePeriodicTagSet testObject1=new UnorderedFinitePeriodicTagSet(period1,3);
		Assert.assertEquals(true, testObject1.isInTagSet(str3));	
	}
	
	public void testGetNext()
	{
		GIPSYString str1=new GIPSYString("black");
		GIPSYString str2=new GIPSYString("white");
		GIPSYString str3=new GIPSYString("red");
		FreeVector<GIPSYType> period1=new FreeVector<GIPSYType>();
		period1.addElement(str1);
		period1.addElement(str2);
		period1.addElement(str3);
		UnorderedFinitePeriodicTagSet testObject1=new UnorderedFinitePeriodicTagSet(period1,3);
	    Assert.assertEquals(null, testObject1.getNext(str3));
		
	}
	
	

}
