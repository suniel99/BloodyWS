package gipsy.tests.junit.lang.context;

import gipsy.lang.GIPSYString;
import gipsy.lang.GIPSYType;
import gipsy.lang.context.UnorderedFiniteNonPeriodicTagSet;
import junit.framework.Assert;
import junit.framework.TestCase;
import marf.util.FreeVector;

public class UnorderedFiniteNonPeriodicTagSetTest extends TestCase {
	public void testEqualObjects()
	{
		GIPSYString str1=new GIPSYString("black");
		GIPSYString str2=new GIPSYString("white");
		GIPSYString str3=new GIPSYString("red");
		FreeVector<GIPSYType> enum1=new FreeVector<GIPSYType>();
		enum1.addElement(str1);
		enum1.addElement(str2);
		enum1.addElement(str3);
		UnorderedFiniteNonPeriodicTagSet testObject1=new UnorderedFiniteNonPeriodicTagSet(enum1);
		
		GIPSYString str4=new GIPSYString("white");
		GIPSYString str5=new GIPSYString("red");
		GIPSYString str6=new GIPSYString("black");
		FreeVector<GIPSYType> enum2=new FreeVector<GIPSYType>();
		enum2.addElement(str4);
		enum2.addElement(str5);
		enum2.addElement(str6);
		UnorderedFiniteNonPeriodicTagSet testObject2=new UnorderedFiniteNonPeriodicTagSet(enum2);
		
		Assert.assertEquals(testObject1, testObject2);
	}
	public void testIsInTagSet()
	{
		GIPSYString str1=new GIPSYString("black");
		GIPSYString str2=new GIPSYString("white");
		GIPSYString str3=new GIPSYString("red");
		FreeVector<GIPSYType> enum1=new FreeVector<GIPSYType>();
		enum1.addElement(str1);
		enum1.addElement(str2);
		enum1.addElement(str3);
		UnorderedFiniteNonPeriodicTagSet testObject1=new UnorderedFiniteNonPeriodicTagSet(enum1);
		Assert.assertEquals(true, testObject1.isInTagSet(str2));
	}
	public void testGetNext()
	{
		GIPSYString str1=new GIPSYString("black");
		GIPSYString str2=new GIPSYString("white");
		GIPSYString str3=new GIPSYString("red");
		FreeVector<GIPSYType> enum1=new FreeVector<GIPSYType>();
		enum1.addElement(str1);
		enum1.addElement(str2);
		enum1.addElement(str3);
		UnorderedFiniteNonPeriodicTagSet testObject1=new UnorderedFiniteNonPeriodicTagSet(enum1);
		Assert.assertEquals(null, testObject1.getNext(str3));
	}

}
