package gipsy.tests.junit.lang.context;

import gipsy.lang.GIPSYInteger;
import gipsy.lang.GIPSYType;
import gipsy.lang.context.OrderedFinitePeriodicTagSet;
import gipsy.lang.context.OrderedInfinitePeriodicTagSet;
import junit.framework.Assert;
import junit.framework.TestCase;
import marf.util.FreeVector;

public class OrderedInfinitePeriodicTagSetTest extends TestCase {
	public void testEqualObjects()
	{
//		{<1,2,3>}
		GIPSYInteger int1=new GIPSYInteger(1);
		GIPSYInteger int2=new GIPSYInteger(2);
		GIPSYInteger int3=new GIPSYInteger(3);
		
	    FreeVector<GIPSYType> period1=new FreeVector<GIPSYType>();
	    period1.add(int1);
	    period1.add(int2);
	    period1.add(int3);
	    OrderedInfinitePeriodicTagSet testObject1=new OrderedInfinitePeriodicTagSet(period1);
	    
	    GIPSYInteger int4=new GIPSYInteger(1);
		GIPSYInteger int5=new GIPSYInteger(2);
		GIPSYInteger int6=new GIPSYInteger(3);
		
	    FreeVector<GIPSYType> period2=new FreeVector<GIPSYType>();
	    period2.add(int4);
	    period2.add(int5);
	    period2.add(int6);

	    OrderedInfinitePeriodicTagSet testObject2=new OrderedInfinitePeriodicTagSet(period2);
	    Assert.assertEquals(testObject1, testObject2);

	}
	
	public void testIsInTagSet()
	{
		GIPSYInteger int1=new GIPSYInteger(1);
		GIPSYInteger int2=new GIPSYInteger(2);
		GIPSYInteger int3=new GIPSYInteger(3);
		
	    FreeVector<GIPSYType> period1=new FreeVector<GIPSYType>();
	    period1.add(int1);
	    period1.add(int2);
	    period1.add(int3);
	    OrderedInfinitePeriodicTagSet testObject1=new OrderedInfinitePeriodicTagSet(period1);
	    Assert.assertEquals(true, testObject1.isInTagSet(int2));
	    
	}
	
	public void testGetNext()
	{
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

}
