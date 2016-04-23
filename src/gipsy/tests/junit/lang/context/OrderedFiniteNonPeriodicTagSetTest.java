package gipsy.tests.junit.lang.context;
import gipsy.lang.GIPSYInteger;
import gipsy.lang.GIPSYType;
import gipsy.lang.context.OrderedFiniteNonPeriodicTagSet;

import java.util.Vector;

import marf.util.FreeVector;
import junit.framework.Assert;
import junit.framework.TestCase;

public class OrderedFiniteNonPeriodicTagSetTest extends TestCase{
	
	
	public void testEqualObjects()
	{
		//enumerated expression: {1,2,3}
        
		GIPSYInteger int1=new GIPSYInteger(1);
		GIPSYInteger int2=new GIPSYInteger(2);
		GIPSYInteger int3=new GIPSYInteger(3);
		FreeVector<GIPSYType> enumeratedElement1=new FreeVector<GIPSYType>();
		enumeratedElement1.add(int1);
		enumeratedElement1.add(int2);
		enumeratedElement1.add(int3);
		OrderedFiniteNonPeriodicTagSet testObject1=new OrderedFiniteNonPeriodicTagSet(enumeratedElement1);
		
		GIPSYInteger int4=new GIPSYInteger(1);
		GIPSYInteger int5=new GIPSYInteger(2);
		GIPSYInteger int6=new GIPSYInteger(3);
		FreeVector<GIPSYType> enumeratedElement2=new FreeVector<GIPSYType>();
		enumeratedElement2.add(int4);
		enumeratedElement2.add(int5);
		enumeratedElement2.add(int6);
		OrderedFiniteNonPeriodicTagSet testObject2=new OrderedFiniteNonPeriodicTagSet(enumeratedElement2);
		
		//upper and lower and step expression {1..3 /1}
		OrderedFiniteNonPeriodicTagSet testObject5=new OrderedFiniteNonPeriodicTagSet(int1, int3, 2);
		OrderedFiniteNonPeriodicTagSet testObject6=new OrderedFiniteNonPeriodicTagSet(int4, int6, 2);
		
		Assert.assertEquals(testObject1, testObject2);
		
		Assert.assertEquals(testObject5, testObject6);
		
	}
	
	public void testIsInTagSet()
	{
		//enumerated{2,4,6}
		GIPSYInteger int1=new GIPSYInteger(2);
		GIPSYInteger int2=new GIPSYInteger(4);
		GIPSYInteger int3=new GIPSYInteger(6);
		FreeVector<GIPSYType> enumeratedElement1=new FreeVector<GIPSYType>();
		enumeratedElement1.add(int1);
		enumeratedElement1.add(int2);
		enumeratedElement1.add(int3);
		OrderedFiniteNonPeriodicTagSet testObject1=new OrderedFiniteNonPeriodicTagSet(enumeratedElement1);
		
		Assert.assertEquals(true, testObject1.isInTagSet(int2));

		//Upper Lower and Step
		OrderedFiniteNonPeriodicTagSet testObject3=new OrderedFiniteNonPeriodicTagSet(int1,int3,2);
		Assert.assertEquals(true, testObject3.isInTagSet(int2));
	}
	
	
	public void testGetNext()
	{
		//enumerated {2,4,6}
		GIPSYInteger int1=new GIPSYInteger(2);
		GIPSYInteger int2=new GIPSYInteger(4);
		GIPSYInteger int3=new GIPSYInteger(6);
		FreeVector<GIPSYType> enumeratedElement1=new FreeVector<GIPSYType>();
		enumeratedElement1.add(int1);
		enumeratedElement1.add(int2);
		enumeratedElement1.add(int3);
		OrderedFiniteNonPeriodicTagSet testObject1=new OrderedFiniteNonPeriodicTagSet(enumeratedElement1);
		Assert.assertEquals(int3, testObject1.getNext(int2));
	   
		//upper lower step{2...6 /2}
		GIPSYInteger int5=new GIPSYInteger(4);
		OrderedFiniteNonPeriodicTagSet testObject3=new OrderedFiniteNonPeriodicTagSet(int1,int3,2);
		Assert.assertEquals(int5, testObject3.getNext(int1));
		
	}

	
}
