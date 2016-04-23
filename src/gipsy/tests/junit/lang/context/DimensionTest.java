package gipsy.tests.junit.lang.context;
import gipsy.lang.GIPSYIdentifier;
import gipsy.lang.GIPSYString;
import gipsy.lang.GIPSYType;
import gipsy.lang.context.Dimension;
import gipsy.lang.context.TagSet;
import gipsy.lang.context.UnorderedFiniteNonPeriodicTagSet;
import junit.framework.Assert;
import junit.framework.TestCase;
import marf.util.FreeVector;
public class DimensionTest extends TestCase{



	public void testEqualsObject() {
		
		GIPSYIdentifier id1=new GIPSYIdentifier("d");
		GIPSYString tag1=new GIPSYString("red");
		GIPSYString tag2=new GIPSYString("green");
		FreeVector<GIPSYType> pset1=new FreeVector<GIPSYType>();
		pset1.add(tag1);
		pset1.add(tag2);
		TagSet ptagset1=new UnorderedFiniteNonPeriodicTagSet(pset1);
		Dimension testObject1=new Dimension(id1,ptagset1);
		testObject1.setCurrentTag(tag1);
		

		GIPSYIdentifier id2=new GIPSYIdentifier("d");
		GIPSYString tag3=new GIPSYString("red");
		GIPSYString tag4=new GIPSYString("green");
		FreeVector<GIPSYType> pset2=new FreeVector<GIPSYType>();
		pset2.add(tag3);
		pset2.add(tag4);
		TagSet ptagset2=new UnorderedFiniteNonPeriodicTagSet(pset2);
		Dimension testObject2=new Dimension(id1,ptagset2);
		testObject1.setCurrentTag(tag3);
		
		Assert.assertTrue(testObject1.equals(testObject2));
		
	}

}
