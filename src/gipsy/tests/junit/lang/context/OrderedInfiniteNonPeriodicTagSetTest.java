package gipsy.tests.junit.lang.context;


import gipsy.lang.GIPSYInteger;
import gipsy.lang.context.OrderedInfiniteNonPeriodicTagSet;
import junit.framework.Assert;
import junit.framework.TestCase;

public class OrderedInfiniteNonPeriodicTagSetTest extends TestCase {
	public void testEqualObjects()
	{
		GIPSYInteger int1=new GIPSYInteger(2);
		GIPSYInteger int2=new GIPSYInteger(6);
		
		
	    
	    GIPSYInteger int3=new GIPSYInteger(2);
		GIPSYInteger int4=new GIPSYInteger(6);
		
	    //{...6 /2}
	    OrderedInfiniteNonPeriodicTagSet testObject3=new OrderedInfiniteNonPeriodicTagSet();
	    testObject3.setExpressionType(OrderedInfiniteNonPeriodicTagSet.UPPER_STEP);
	    testObject3.setUpper(int2);
	    testObject3.setStep(2);
	    

	    OrderedInfiniteNonPeriodicTagSet testObject4=new OrderedInfiniteNonPeriodicTagSet();
	    testObject4.setExpressionType(OrderedInfiniteNonPeriodicTagSet.UPPER_STEP);
	    testObject4.setUpper(int4);
	    testObject4.setStep(2);
	    Assert.assertEquals(testObject3, testObject4);
	    
	    //{2.../2}
	    OrderedInfiniteNonPeriodicTagSet testObject7=new OrderedInfiniteNonPeriodicTagSet();
	    testObject7.setExpressionType(OrderedInfiniteNonPeriodicTagSet.LOWER_STEP);
	    testObject7.setLower(int1);
	    testObject7.setStep(2);
	    

	    OrderedInfiniteNonPeriodicTagSet testObject8=new OrderedInfiniteNonPeriodicTagSet();
	    testObject8.setExpressionType(OrderedInfiniteNonPeriodicTagSet.LOWER_STEP);
	    testObject8.setLower(int3);
	    testObject8.setStep(2);
	    Assert.assertEquals(testObject7, testObject8);
	    
	    //int

	    OrderedInfiniteNonPeriodicTagSet testObject9=new OrderedInfiniteNonPeriodicTagSet();
	    testObject9.setExpressionType(OrderedInfiniteNonPeriodicTagSet.INFINITY);
	    
	    OrderedInfiniteNonPeriodicTagSet testObject10=new OrderedInfiniteNonPeriodicTagSet();
	    testObject10.setExpressionType(OrderedInfiniteNonPeriodicTagSet.INFINITY);
	    Assert.assertEquals(testObject9, testObject10);

	}
	
	public void testIsInTagSet()
	{
		GIPSYInteger int1=new GIPSYInteger(2);
		GIPSYInteger int2=new GIPSYInteger(4);
		GIPSYInteger int3=new GIPSYInteger(6);
		
		
		//{...6 /2}
		OrderedInfiniteNonPeriodicTagSet testObject3=new OrderedInfiniteNonPeriodicTagSet(int3, 2,OrderedInfiniteNonPeriodicTagSet.UPPER_STEP);
		Assert.assertEquals(true, testObject3.isInTagSet(int2));
		
		//{2... /2}
		OrderedInfiniteNonPeriodicTagSet testObject4=new OrderedInfiniteNonPeriodicTagSet(int1, 2,OrderedInfiniteNonPeriodicTagSet.LOWER_STEP);
		Assert.assertEquals(true, testObject4.isInTagSet(int2));
		
		//int
		OrderedInfiniteNonPeriodicTagSet testObject5=new OrderedInfiniteNonPeriodicTagSet(OrderedInfiniteNonPeriodicTagSet.INFINITY);
		Assert.assertEquals(true, testObject5.isInTagSet(int2));
	}
	
	public void testGetNext()
	{
		GIPSYInteger int1=new GIPSYInteger(2);
		GIPSYInteger int2=new GIPSYInteger(4);
		GIPSYInteger int3=new GIPSYInteger(6);
		
		GIPSYInteger int4=new GIPSYInteger(3);
		
		
		//{...6 /2}
		OrderedInfiniteNonPeriodicTagSet testObject3=new OrderedInfiniteNonPeriodicTagSet(int3, 2,OrderedInfiniteNonPeriodicTagSet.UPPER_STEP);
		Assert.assertEquals(int2, testObject3.getNext(int1));
		
		//{2... /2}
		OrderedInfiniteNonPeriodicTagSet testObject4=new OrderedInfiniteNonPeriodicTagSet(int1, 2,OrderedInfiniteNonPeriodicTagSet.LOWER_STEP);
		Assert.assertEquals(int2, testObject4.getNext(int1));
		
		//int
		OrderedInfiniteNonPeriodicTagSet testObject5=new OrderedInfiniteNonPeriodicTagSet(OrderedInfiniteNonPeriodicTagSet.INFINITY);
		Assert.assertEquals(int4, testObject5.getNext(int1));

		
	}

}
