package gipsy.tests.junit.lang.context;

import gipsy.lang.GIPSYInteger;
import gipsy.lang.GIPSYType;
import gipsy.lang.context.OrderedFinitePeriodicTagSet;
import junit.framework.Assert;
import junit.framework.TestCase;
import marf.util.FreeVector;


public class OrderedFinitePeriodicTagSetTest extends TestCase{
	public void testEqualsObject()
	{
		//{<1,2,3>:3}
		GIPSYInteger int1=new GIPSYInteger(1);
		GIPSYInteger int2=new GIPSYInteger(2);
		GIPSYInteger int3=new GIPSYInteger(3);
		
	    FreeVector<GIPSYType> period1=new FreeVector<GIPSYType>();
	    period1.add(int1);
	    period1.add(int2);
	    period1.add(int3);
	    OrderedFinitePeriodicTagSet testObject1=new OrderedFinitePeriodicTagSet(period1,3);
	    
	    GIPSYInteger int4=new GIPSYInteger(1);
		GIPSYInteger int5=new GIPSYInteger(2);
		GIPSYInteger int6=new GIPSYInteger(3);
		
	    FreeVector<GIPSYType> period2=new FreeVector<GIPSYType>();
	    period2.add(int4);
	    period2.add(int5);
	    period2.add(int6);

	    OrderedFinitePeriodicTagSet testObject2=new OrderedFinitePeriodicTagSet(period2,3);
	    Assert.assertEquals(testObject1, testObject2);
				
	}
	
	public void testisInTagSet()
	{
		GIPSYInteger pTag=new GIPSYInteger(2);
//		{<1,2,3>:3}
		GIPSYInteger int1=new GIPSYInteger(1);
		GIPSYInteger int2=new GIPSYInteger(2);
		GIPSYInteger int3=new GIPSYInteger(3);
		
	    FreeVector<GIPSYType> period1=new FreeVector<GIPSYType>();
	    period1.add(int1);
	    period1.add(int2);
	    period1.add(int3);
	    OrderedFinitePeriodicTagSet testObject=new OrderedFinitePeriodicTagSet(period1,3);
	    Assert.assertEquals(true, testObject.isInTagSet(pTag));
	}
	
	public void testgetNext()
	{
//		{<1,2,3>:3}
		GIPSYInteger int1=new GIPSYInteger(1);
		GIPSYInteger int2=new GIPSYInteger(2);
		GIPSYInteger int3=new GIPSYInteger(3);
		
	    FreeVector<GIPSYType> period1=new FreeVector<GIPSYType>();
	    period1.add(int1);
	    period1.add(int2);
	    period1.add(int3);
	    OrderedFinitePeriodicTagSet testObject=new OrderedFinitePeriodicTagSet(period1,3);
		
	    Assert.assertEquals(int3, testObject.getNext(int2)); //current:2 next:3
	    Assert.assertEquals(int1, testObject.getNext(int3));//current:3 next:1
	}
	public void testtoString()
	{
//		{<1,2,3>:3}
		GIPSYInteger int1=new GIPSYInteger(1);
		GIPSYInteger int2=new GIPSYInteger(2);
		GIPSYInteger int3=new GIPSYInteger(3);
		
	    FreeVector<GIPSYType> period1=new FreeVector<GIPSYType>();
	    period1.add(int1);
	    period1.add(int2);
	    period1.add(int3);
	    OrderedFinitePeriodicTagSet testObject1=new OrderedFinitePeriodicTagSet(period1,3);
	    System.out.println(testObject1.toString());
	}


}
