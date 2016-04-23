package gipsy.tests.junit.lang.context;

import gipsy.lang.GIPSYString;
import gipsy.lang.GIPSYType;
import gipsy.lang.context.UnorderedInfinitePeriodicTagSet;
import junit.framework.Assert;
import junit.framework.TestCase;
import marf.util.FreeVector;

public class UnorderedInfinitePeriodicTagSetTest extends TestCase {
	public void testEqualObjects()
	{
		GIPSYString str1=new GIPSYString("black");
		GIPSYString str2=new GIPSYString("white");
		GIPSYString str3=new GIPSYString("red");
		FreeVector<GIPSYType> period1=new FreeVector<GIPSYType>();
		period1.addElement(str1);
		period1.addElement(str2);
		period1.addElement(str3);
		UnorderedInfinitePeriodicTagSet testObject1=new UnorderedInfinitePeriodicTagSet(period1);
		
		GIPSYString str4=new GIPSYString("white");
		GIPSYString str5=new GIPSYString("red");
		GIPSYString str6=new GIPSYString("black");
		FreeVector<GIPSYType> period2=new FreeVector<GIPSYType>();
		period2.addElement(str4);
		period2.addElement(str5);
		period2.addElement(str6);
		UnorderedInfinitePeriodicTagSet testObject2=new UnorderedInfinitePeriodicTagSet(period2);
		
		Assert.assertEquals(testObject1, testObject2);
	}
	
	public void testIsInTagSet()
	{
		GIPSYString str1=new GIPSYString("black");
		GIPSYString str2=new GIPSYString("white");
		GIPSYString str3=new GIPSYString("red");
		GIPSYString str4=new GIPSYString("yellow");
		FreeVector<GIPSYType> period1=new FreeVector<GIPSYType>();
		period1.addElement(str1);
		period1.addElement(str2);
		period1.addElement(str3);
		UnorderedInfinitePeriodicTagSet testObject1=new UnorderedInfinitePeriodicTagSet(period1);
		Assert.assertEquals(true, testObject1.isInTagSet(str1));
		Assert.assertEquals(false, testObject1.isInTagSet(str4));
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
		UnorderedInfinitePeriodicTagSet testObject1=new UnorderedInfinitePeriodicTagSet(period1);
		Assert.assertEquals(null, testObject1.getNext(str2));		
	}
}
