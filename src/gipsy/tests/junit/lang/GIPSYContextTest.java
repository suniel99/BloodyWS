package gipsy.tests.junit.lang;

import gipsy.lang.GIPSYContext;
import gipsy.lang.GIPSYIdentifier;
import gipsy.lang.GIPSYInteger;
import gipsy.lang.GIPSYType;
import gipsy.lang.context.Dimension;
import gipsy.lang.context.OrderedFiniteNonPeriodicTagSet;
import gipsy.lang.context.TagSet;

import java.util.Vector;

import marf.util.FreeVector;
import junit.framework.Assert;
import junit.framework.TestCase;


public class GIPSYContextTest extends TestCase {
	GIPSYIdentifier idd=new GIPSYIdentifier("d");
	GIPSYIdentifier ide=new GIPSYIdentifier("e");
	GIPSYIdentifier idf=new GIPSYIdentifier("f");
	GIPSYIdentifier idg=new GIPSYIdentifier("g");
	GIPSYIdentifier idh=new GIPSYIdentifier("h");
	
	
	GIPSYInteger int1=new GIPSYInteger(1);
	GIPSYInteger int2=new GIPSYInteger(2);
	GIPSYInteger int3=new GIPSYInteger(3);
	GIPSYInteger int4=new GIPSYInteger(4);
	GIPSYInteger int5=new GIPSYInteger(5);
	
	GIPSYContext testObject1;
	GIPSYContext testObject2;
	
	OrderedFiniteNonPeriodicTagSet universalTagSet;
	

	
	/**
	 * For simplicity, now we only consider the OrderedFiniteNonPeriodicTagSet, which is of the enumerated expression
	 * This is the one-element tag set
	 * @param pTag1
	 * @return
	 *//*
	public OrderedFiniteNonPeriodicTagSet buildTagSet(GIPSYInteger pTag1)
	{
		Vector enumeratedElements=new Vector();
		enumeratedElements.add(pTag1);
		return new OrderedFiniteNonPeriodicTagSet(enumeratedElements);
		
	}
	
	*//**
	 * For simplicity, now we only consider the OrderedFiniteNonPeriodicTagSet, which is of the enumerated expression
	 * This is the two-element tag set
	 * @param pTag1
	 * @param pTag2
	 * @return
	 *//*
	public OrderedFiniteNonPeriodicTagSet buildTagSet(GIPSYInteger pTag1, GIPSYInteger pTag2)
	{
		Vector enumeratedElements=new Vector();
		enumeratedElements.add(pTag1);
		enumeratedElements.add(pTag2);
		return new OrderedFiniteNonPeriodicTagSet(enumeratedElements);
		
	}
	
	*//**
	 * For simplicity, now we only consider the OrderedFiniteNonPeriodicTagSet, which is of the enumerated expression
	 * This is the three-element tag set
	 * @param pTag1
	 * @param pTag2
	 * @param pTag3
	 * @return
	 *//*
	public OrderedFiniteNonPeriodicTagSet buildTagSet(GIPSYInteger pTag1, GIPSYInteger pTag2, GIPSYInteger pTag3)
	{
		Vector enumeratedElements=new Vector();
		enumeratedElements.add(pTag1);
		enumeratedElements.add(pTag2);
		enumeratedElements.add(pTag3);
		return new OrderedFiniteNonPeriodicTagSet(enumeratedElements);
		
	}
	
	*//**
	 * For simplicity, now we only consider the OrderedFiniteNonPeriodicTagSet, which is of the enumerated expression
	 * This is the four-element tag set
	 * @param pTag1
	 * @param pTag2
	 * @param pTag3
	 * @param pTag4
	 * @return
	 *//*
	public OrderedFiniteNonPeriodicTagSet buildTagSet(GIPSYInteger pTag1, GIPSYInteger pTag2, GIPSYInteger pTag3, GIPSYInteger pTag4)
	{
		Vector enumeratedElements=new Vector();
		enumeratedElements.add(pTag1);
		enumeratedElements.add(pTag2);
		enumeratedElements.add(pTag3);
		enumeratedElements.add(pTag4);
		return new OrderedFiniteNonPeriodicTagSet(enumeratedElements);
		
	}
	*/
	
	
	
	private OrderedFiniteNonPeriodicTagSet buildUniversalTagSet()
	{
		FreeVector<GIPSYType> enumeratedElements=new FreeVector<GIPSYType>();
		enumeratedElements.add(int1);
		enumeratedElements.add(int2);
		enumeratedElements.add(int3);
		enumeratedElements.add(int4);
		enumeratedElements.add(int5);
		universalTagSet=new OrderedFiniteNonPeriodicTagSet(enumeratedElements);
		return universalTagSet;
		
	}
	/**
	 * Returns an Object of Dimension with the current tag specified.
	 * @param pDimName
	 * @param pTagSet
	 * @param pCurrentTag
	 * @return
	 */
	public Dimension buildDimension(GIPSYIdentifier pDimName, TagSet pTagSet, GIPSYType pCurrentTag)
	{
		Dimension result=new Dimension(pDimName, pTagSet);
		result.setCurrentTag(pCurrentTag);
		return result;
	}
	
	/**
	 * Returns an Object of Dimension without the current tag specified.
	 * @param pDimName
	 * @param pTagSet
	 * @return
	 */
	public Dimension buildDimension(GIPSYIdentifier pDimName, TagSet pTagSet)
	{
		Dimension result=new Dimension(pDimName, pTagSet);
		return result;
	}

	
	
	
	/**
	 * Returns an one-element simple context
	 * @param pDimension1
	 * @return
	 */
	public GIPSYContext buildSimpleContext(Dimension pDimension1)
	{
		FreeVector<GIPSYType> set=new FreeVector<GIPSYType>();
		set.add(pDimension1);
		GIPSYContext result=new GIPSYContext(set);
		result.setContextType(GIPSYContext.SIMPLE_CONTEXT);
		return result;
	}
	
	/**
	 * Returns a two-element simple context
	 * @param pDimension1
	 * @param pDimension2
	 * @return
	 */
	public GIPSYContext buildSimpleContext(Dimension pDimension1, Dimension pDimension2)
	{
		FreeVector<GIPSYType> set=new FreeVector<GIPSYType>();
		set.add(pDimension1);
		set.add(pDimension2);
		GIPSYContext result=new GIPSYContext(set);
		result.setContextType(GIPSYContext.SIMPLE_CONTEXT);
		return result;
	}
	
	/**
	 * Returns a three-element simple context
	 * @param pDimension1
	 * @param pDimension2
	 * @param pDimension3
	 * @return
	 */
	public GIPSYContext buildSimpleContext(Dimension pDimension1, Dimension pDimension2, Dimension pDimension3)
	{
		FreeVector<GIPSYType> set=new FreeVector<GIPSYType>();
		set.add(pDimension1);
		set.add(pDimension2);
		set.add(pDimension3);
		GIPSYContext result=new GIPSYContext(set);
		result.setContextType(GIPSYContext.SIMPLE_CONTEXT);
		return result;
	}
	
	/**
	 * Returns a four-element simple context
	 * @param pDimension1
	 * @param pDimension2
	 * @param pDimension3
	 * @param pDimension4
	 * @return
	 */
	public GIPSYContext buildSimpleContext(Dimension pDimension1, Dimension pDimension2, Dimension pDimension3, Dimension pDimension4 )
	{
		FreeVector<GIPSYType> set=new FreeVector<GIPSYType>();
		set.add(pDimension1);
		set.add(pDimension2);
		set.add(pDimension3);
		set.add(pDimension4);
		GIPSYContext result=new GIPSYContext(set);
		result.setContextType(GIPSYContext.SIMPLE_CONTEXT);
		return result;
	}
	
	
	
	/**
	 * Returns an one-element context set
	 * @param pSimpleContext1
	 * @return
	 */
	public GIPSYContext buildContextSet(GIPSYContext pSimpleContext1)
	{
		FreeVector<GIPSYType> set=new FreeVector<GIPSYType>();
		set.add(pSimpleContext1);
		GIPSYContext result=new GIPSYContext(set);
		result.setContextType(GIPSYContext.CONTEXT_SET);
		return result;
	}
	
	/**
	 * Returns a two-element context set
	 * @param pSimpleContext1
	 * @param pSimpleContext2
	 * @return
	 */
	public GIPSYContext buildContextSet(GIPSYContext pSimpleContext1, GIPSYContext pSimpleContext2)
	{
		FreeVector<GIPSYType> set=new FreeVector<GIPSYType>();
		set.add(pSimpleContext1);
		set.add(pSimpleContext2);
		GIPSYContext result=new GIPSYContext(set);
		result.setContextType(GIPSYContext.CONTEXT_SET);
		return result;
	}
	
	/**
	 * Returns a three-element context set
	 * @param pSimpleContext1
	 * @param pSimpleContext2
	 * @param pSimpleContext3
	 * @return
	 */
	public GIPSYContext buildContextSet(GIPSYContext pSimpleContext1, GIPSYContext pSimpleContext2, GIPSYContext pSimpleContext3)
	{
		FreeVector<GIPSYType> set=new FreeVector<GIPSYType>();
		set.add(pSimpleContext1);
		set.add(pSimpleContext2);
		set.add(pSimpleContext3);
		GIPSYContext result=new GIPSYContext(set);
		result.setContextType(GIPSYContext.CONTEXT_SET);
		return result;
	}
	
	/**
	 * Returns a four-element context set
	 * @param pSimpleContext1
	 * @param pSimpleContext2
	 * @param pSimpleContext3
	 * @param pSimpleContext4
	 * @return
	 */
	public GIPSYContext buildContextSet(GIPSYContext pSimpleContext1, GIPSYContext pSimpleContext2, GIPSYContext pSimpleContext3, GIPSYContext pSimpleContext4)
	{
		FreeVector<GIPSYType> set=new FreeVector<GIPSYType>();
		set.add(pSimpleContext1);
		set.add(pSimpleContext2);
		set.add(pSimpleContext3);
		set.add(pSimpleContext4);
		GIPSYContext result=new GIPSYContext(set);
		result.setContextType(GIPSYContext.CONTEXT_SET);
		return result;
	}
	
	public GIPSYContext buildContextSet(GIPSYContext pSimpleContext1, GIPSYContext pSimpleContext2, GIPSYContext pSimpleContext3, GIPSYContext pSimpleContext4, GIPSYContext pSimpleContext5)
	{
		FreeVector<GIPSYType> set=new FreeVector<GIPSYType>();
		set.add(pSimpleContext1);
		set.add(pSimpleContext2);
		set.add(pSimpleContext3);
		set.add(pSimpleContext4);
		set.add(pSimpleContext5);
		GIPSYContext result=new GIPSYContext(set);
		result.setContextType(GIPSYContext.CONTEXT_SET);
		return result;
	}
	
	public GIPSYContext buildContextSet(GIPSYContext pSimpleContext1, GIPSYContext pSimpleContext2, GIPSYContext pSimpleContext3, GIPSYContext pSimpleContext4, GIPSYContext pSimpleContext5, GIPSYContext pSimpleContext6)
	{
		FreeVector<GIPSYType> set=new FreeVector<GIPSYType>();
		set.add(pSimpleContext1);
		set.add(pSimpleContext2);
		set.add(pSimpleContext3);
		set.add(pSimpleContext4);
		set.add(pSimpleContext5);
		set.add(pSimpleContext6);
		GIPSYContext result=new GIPSYContext(set);
		result.setContextType(GIPSYContext.CONTEXT_SET);
		return result;
	}
	
	public GIPSYContext buildContextSet(GIPSYContext pSimpleContext1, GIPSYContext pSimpleContext2, GIPSYContext pSimpleContext3, GIPSYContext pSimpleContext4, GIPSYContext pSimpleContext5, GIPSYContext pSimpleContext6, GIPSYContext pSimpleContext7, GIPSYContext pSimpleContext8, GIPSYContext pSimpleContext9)
	{
		FreeVector<GIPSYType> set=new FreeVector<GIPSYType>();
		set.add(pSimpleContext1);
		set.add(pSimpleContext2);
		set.add(pSimpleContext3);
		set.add(pSimpleContext4);
		set.add(pSimpleContext5);
		set.add(pSimpleContext6);
		set.add(pSimpleContext7);
		set.add(pSimpleContext8);
		set.add(pSimpleContext9);
		GIPSYContext result=new GIPSYContext(set);
		result.setContextType(GIPSYContext.CONTEXT_SET);
		return result;
	}
	/**
	 * Test for the simple context
	 *
	 */
	
	/**
	 * [d:1, e:2, f:3]
	 */
	public void testEqualSimpleContexts()
	{
		testObject1=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), buildDimension(ide, buildUniversalTagSet(), int2), buildDimension(idf, buildUniversalTagSet(), int3));
		testObject2=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), buildDimension(ide, buildUniversalTagSet(), int2), buildDimension(idf, buildUniversalTagSet(), int3));
		Assert.assertEquals(testObject1, testObject2);
	}
	
	
	
	/**
	 * Test for the context set.
	 *
	 */
	
	/**
	 * {[d:1, e:2, f:3], [g:4, h:5]}
	 */
	public void testEqualContextSets()
	{
		
		testObject1=buildContextSet(buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
				                                       buildDimension(ide, buildUniversalTagSet(), int2), 
				                                       buildDimension(idf, buildUniversalTagSet(), int3)), 
				                    buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
				                    		           buildDimension(idh, buildUniversalTagSet(), int5)));
		testObject2=buildContextSet(buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                                                       buildDimension(ide, buildUniversalTagSet(), int2), 
                                                       buildDimension(idf, buildUniversalTagSet(), int3)), 
                                    buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
		                                               buildDimension(idh, buildUniversalTagSet(), int5)));
		
	    
	    Assert.assertEquals(testObject1, testObject2);
	}
	
	/**
	 * [d:1, e:2]isSubContext[d:1, f:3, e:2]
	 * []isSubContext[d:1, e:2]
	 * [d;1, e:2]isSubContext[]
	 */
	public void testIsSubContext_SimpleContext()
	{
		testObject1=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), buildDimension(ide, buildUniversalTagSet(), int2));
		testObject2=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), buildDimension(idf, buildUniversalTagSet(), int3), buildDimension(ide, buildUniversalTagSet(), int2));
		Assert.assertTrue(testObject1.isSubContext(testObject2));
		
		testObject1=new GIPSYContext();
		Assert.assertTrue(testObject1.isSubContext(testObject2));
		
		testObject1=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), buildDimension(ide, buildUniversalTagSet(), int2));
		testObject2=new GIPSYContext();
		Assert.assertEquals(false, testObject1.isSubContext(testObject2));
	}
	
	/**
	 * {[d:1, e:2, f:3], [g:4, h:5]} isSubContext {[d:1, e:2, f:3], [g:4, h:5]}
	 * {}isSubContext {[d:1, e:2, f:3], [g:4, h:5]}
	 * {[d:1, e:2, f:3], [g:4, h:5]} isSubContext{}
	 */
	public void testIsSubContext_ContextSet()
	{
		testObject1=buildContextSet(buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                                                       buildDimension(ide, buildUniversalTagSet(), int2), 
                                                       buildDimension(idf, buildUniversalTagSet(), int3)), 
                                    buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
		                                               buildDimension(idh, buildUniversalTagSet(), int5)));
		testObject2=buildContextSet(buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                                                       buildDimension(ide, buildUniversalTagSet(), int2), 
                                                       buildDimension(idf, buildUniversalTagSet(), int3)), 
                                    buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
		                                               buildDimension(idh, buildUniversalTagSet(), int5)));
       
		Assert.assertTrue(testObject1.isSubContext(testObject2));
		
		testObject1=new GIPSYContext();
		testObject1.setContextType(GIPSYContext.CONTEXT_SET);
		Assert.assertTrue(testObject1.isSubContext(testObject2));
		
		testObject1=buildContextSet(buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                                                       buildDimension(ide, buildUniversalTagSet(), int2), 
                                                       buildDimension(idf, buildUniversalTagSet(), int3)), 
                                    buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
                                                       buildDimension(idh, buildUniversalTagSet(), int5)));

		testObject2=new GIPSYContext();
		testObject2.setContextType(GIPSYContext.CONTEXT_SET);
		Assert.assertEquals(false, testObject1.isSubContext(testObject2));
	
	}
	
	/**
	 * [d:1, e:2] differentiate [d:1, f:3]=[e:2]
       [d:1, e:2] differentiate [d:1, e:2, f:3] = �0�1
       [d:1, e:2] differentiate [g:4, h:5] =[d:1, e:2]

	 */
	public void testDifferentiate_SimpleContext()
	{
		//[d:1, e:2]
		testObject1=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), buildDimension(ide, buildUniversalTagSet(), int2));
		//[d:1, f:3]
		testObject2=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), buildDimension(idf, buildUniversalTagSet(), int3));
		GIPSYContext result1=buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int2));
		Assert.assertEquals(result1, testObject1.difference(testObject2));
		
		testObject2=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), buildDimension(ide, buildUniversalTagSet(), int2),buildDimension(idf, buildUniversalTagSet(), int3));
		GIPSYContext result2=new GIPSYContext(GIPSYContext.SIMPLE_CONTEXT);
		Assert.assertEquals(result2, testObject1.difference(testObject2));
	    
		testObject2=buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), buildDimension(idh, buildUniversalTagSet(), int5));
		Assert.assertEquals(testObject1, testObject1.difference(testObject2));
	}
	
	/**
	 * {[d:1, e:2, f:3], [g:4, h:5]} differentiate {[g:4, h:5], [e:2]}={[d:1, e:2, f:3], [d:1, f:3], [g:4, h:5]}
	 */
	public void testDifferentiate_ContextSet()
	{
		testObject1=buildContextSet(buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                                                       buildDimension(ide, buildUniversalTagSet(), int2), 
                                                       buildDimension(idf, buildUniversalTagSet(), int3)), 
                                    buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
                                                       buildDimension(idh, buildUniversalTagSet(), int5)));
		testObject2=buildContextSet(buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
                                                       buildDimension(idh, buildUniversalTagSet(), int5)), buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int2)));
        
		GIPSYContext result=buildContextSet(buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                                                               buildDimension(ide, buildUniversalTagSet(), int2), 
                                                               buildDimension(idf, buildUniversalTagSet(), int3)), 
                                            buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                                            		           buildDimension(idf, buildUniversalTagSet(), int3)), 
                                            buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
                                                               buildDimension(idh, buildUniversalTagSet(), int5)));
		
		Assert.assertEquals(result, testObject1.difference(testObject2));
	}
	
	/**
	 * [d:1, e:2] intersect [d:1] =[d:1]
	 * [d:1, e:2] intersect [g:4, h:5] = �0�1
	 */
	public void testIntersect_SimpleContext()
	{
		//[d:1, e:2]
		testObject1=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), buildDimension(ide, buildUniversalTagSet(), int2));
		testObject2=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1));
		Assert.assertEquals(testObject2, testObject1.intersection(testObject2));
		
		testObject2=buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), buildDimension(idh, buildUniversalTagSet(), int5));
	    GIPSYContext result=new GIPSYContext(GIPSYContext.SIMPLE_CONTEXT);
	    Assert.assertEquals(result, testObject1.intersection(testObject2));
	    
	}
	
	
	/**
	 * {[d:1, e:2, f:3], [g:4, h:5]} intersect {[g:4, h:5], [e:2]}= {[e:2], [g:4, h:5]}
	 */
	public void testIntersect_ContextSet()
	{
		testObject1=buildContextSet(buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                                                       buildDimension(ide, buildUniversalTagSet(), int2), 
                                                       buildDimension(idf, buildUniversalTagSet(), int3)), 
                                    buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
                                                       buildDimension(idh, buildUniversalTagSet(), int5)));
        testObject2=buildContextSet(buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
                                                       buildDimension(idh, buildUniversalTagSet(), int5)), 
                                    buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int2)));

        GIPSYContext result=buildContextSet(buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int2)), 
                                            buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
                                                               buildDimension(idh, buildUniversalTagSet(), int5)));

       Assert.assertEquals(result, testObject1.intersection(testObject2));

	}
	
	/**
	 * [d:1, e:2,  f:3] project {d, f}=[d:1, f:3]
	 */
	public void testProject_SimpleContext()
	{
		testObject1=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                buildDimension(ide, buildUniversalTagSet(), int2), 
                buildDimension(idf, buildUniversalTagSet(), int3));
		Vector<Dimension> dimensionSet=new Vector<Dimension>();
		dimensionSet.add(buildDimension(idd, buildUniversalTagSet()));
		dimensionSet.add(buildDimension(idf, buildUniversalTagSet()));
		GIPSYContext result=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
             
                                               buildDimension(idf, buildUniversalTagSet(), int3));
		Assert.assertEquals(result, testObject1.projection(dimensionSet));

	}
	
	/**
	 * {[d:1, e:2, f:3], [g:4, h:5], [f:4]} project{e, h}={[e:2],[h:5]}
	 */
	public void testProject_ContextSet()
	{
		testObject1=buildContextSet(buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                                                       buildDimension(ide, buildUniversalTagSet(), int2), 
                                                       buildDimension(idf, buildUniversalTagSet(), int3)), 
                                    buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
                                                       buildDimension(idh, buildUniversalTagSet(), int5)),
                                    buildSimpleContext(buildDimension(idf, buildUniversalTagSet(), int4)));
		Vector<Dimension> dimensionSet=new Vector<Dimension>();
		//Here the currentTag should not be specified.
		dimensionSet.add(buildDimension(ide, buildUniversalTagSet()));
		dimensionSet.add(buildDimension(idh, buildUniversalTagSet()));
		
		GIPSYContext result=buildContextSet(buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int2)), 
				                            buildSimpleContext(buildDimension(idh, buildUniversalTagSet(), int5)));
		Assert.assertEquals(result, testObject1.projection(dimensionSet));
	}
	
	/**
	 *  [d:1, e:2, f:3] hide {d, e}=[f:3]
        [d:1, e:2, f:3] hide {g, h}= [d:1, e:2, f:3]
        [d:1, e:2, f:3] hide {d, e, f}= �0�1
	 */
	public void testHide_SimpleContext()
	{
		testObject1=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                                       buildDimension(ide, buildUniversalTagSet(), int2), 
                                       buildDimension(idf, buildUniversalTagSet(), int3));
		Vector<Dimension> dimensionSet1=new Vector<Dimension>();
		dimensionSet1.add(buildDimension(idd, buildUniversalTagSet()));
		dimensionSet1.add(buildDimension(ide, buildUniversalTagSet()));
		GIPSYContext result1=buildSimpleContext(buildDimension(idf, buildUniversalTagSet(), int3));
		Assert.assertEquals(result1, testObject1.hiding(dimensionSet1));
		
		Vector<Dimension> dimensionSet2=new Vector<Dimension>();
		dimensionSet2.add(buildDimension(idg, buildUniversalTagSet()));
		dimensionSet2.add(buildDimension(idh, buildUniversalTagSet()));
		Assert.assertEquals(testObject1, testObject1.hiding(dimensionSet2));
		
		Vector<Dimension> dimensionSet3=new Vector<Dimension>();
		dimensionSet3.add(buildDimension(idd, buildUniversalTagSet()));
		dimensionSet3.add(buildDimension(ide, buildUniversalTagSet()));
		dimensionSet3.add(buildDimension(idf, buildUniversalTagSet()));
		GIPSYContext result3=new GIPSYContext(GIPSYContext.SIMPLE_CONTEXT);
		Assert.assertEquals(result3, testObject1.hiding(dimensionSet3));
	}
	
	/**
	 * {[d:1, e:2, f:3], [g:4, h:5], [e:3]} hide {d, e}={[f:3], [g:4, h:5]}
	 */
	public void testHide_ContextSet()
	{
		testObject1=buildContextSet(buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                                                                    buildDimension(ide, buildUniversalTagSet(), int2), 
                                                                    buildDimension(idf, buildUniversalTagSet(), int3)),
                                                 buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
                                                                    buildDimension(idh, buildUniversalTagSet(), int5)),
                                                 buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int3)));

		Vector<Dimension> dimensionSet=new Vector<Dimension>();
		dimensionSet.add(buildDimension(idd, buildUniversalTagSet()));
		dimensionSet.add(buildDimension(ide, buildUniversalTagSet()));
		
		GIPSYContext result=buildContextSet(buildSimpleContext(buildDimension(idf, buildUniversalTagSet(), int3)),
				                            buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
				                            (buildDimension(idh, buildUniversalTagSet(), int5))));
	    Assert.assertEquals(result, testObject1.hiding(dimensionSet));
	}
	
	/**
	 * [d:1, e:2, f:3] override [e:3, g:4] = [d:1, e:3, f:3, g:4]
	 * [d:1, e:2, f:3] override [e:3, g:4] = [d:1, e:3, f:3, g:4]
	 */
	public void testOverride_SimpleContext()
	{
		testObject1=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                buildDimension(ide, buildUniversalTagSet(), int2), 
                buildDimension(idf, buildUniversalTagSet(), int3));
		testObject2=buildSimpleContext(buildDimension(ide,buildUniversalTagSet(), int3),
				                       buildDimension(idg, buildUniversalTagSet(), int4));
		
		GIPSYContext result1=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                            buildDimension(ide, buildUniversalTagSet(), int3), 
                            buildDimension(idf, buildUniversalTagSet(), int3),
                            buildDimension(idg, buildUniversalTagSet(), int4));
		Assert.assertEquals(result1, testObject1.override(testObject2));
		
		testObject2=buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int3), buildDimension(idg,buildUniversalTagSet(), int4));
		GIPSYContext result2=buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                buildDimension(ide, buildUniversalTagSet(), int3), 
                buildDimension(idf, buildUniversalTagSet(), int3),
                buildDimension(idg, buildUniversalTagSet(), int4));

		Assert.assertEquals(result2, testObject1.override(testObject2));
	
	}
	
	/**
	 * {[d:1, e:2],[f:3],[g:4, h:5]} override {[d:3], [h:1]} = {[d:3, e:2], [d:1, e:2, h:1], [f:3, d:3],[f:3, h:1], [g:4, h:5, d:3], [g:4, h:1]}
	 */
	public void testOverride_ContextSet()
	{
		testObject1=buildContextSet(buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1), 
                                                       buildDimension(ide, buildUniversalTagSet(), int2)), 
                                    buildSimpleContext(buildDimension(idf, buildUniversalTagSet(), int3)),
                                    buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
                                                       buildDimension(idh, buildUniversalTagSet(), int5)));
		testObject2=buildContextSet(buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int3)),buildSimpleContext(buildDimension(idh, buildUniversalTagSet(), int1)));
        
		GIPSYContext result=buildContextSet(buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int3), 
				                                               buildDimension(ide, buildUniversalTagSet(), int2)), 
				                            buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1),
				                            		           buildDimension(ide, buildUniversalTagSet(), int2),
				                            		           buildDimension(idh, buildUniversalTagSet(), int1)), 
				                            buildSimpleContext(buildDimension(idf, buildUniversalTagSet(), int3), 
		                                                       buildDimension(idd, buildUniversalTagSet(), int3)), 
				                            buildSimpleContext(buildDimension(idf, buildUniversalTagSet(), int3), 
                                                               buildDimension(idh, buildUniversalTagSet(), int1)), 
				                            buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4),
		                            		                   buildDimension(idh, buildUniversalTagSet(), int5),
		                            		                   buildDimension(idd, buildUniversalTagSet(), int3)), 
				                            buildSimpleContext(buildDimension(idg, buildUniversalTagSet(), int4), 
                                                               buildDimension(idh, buildUniversalTagSet(), int1)));
	    Assert.assertEquals(result, testObject1.override(testObject2));
	
	}
	
	/**
	 * [e: 3, d: 1] directedRange = [e: 1, d: 3, f:4]={[e:1, d:1, f:4], [e:1, d:2, f:4], [e:1, d:3, f:4], [e:2, d:1, f:4], [e:2, d:2, f:4], [e:2, d:3, f:4], [e:3, d:1, f:4], [e:3, d:2, f:4], [e:3, d:3, f:4]}
	 */
	public void testUndirectedRange()
	{
		testObject1=buildSimpleContext(buildDimension(ide,buildUniversalTagSet(), int3),
                                       buildDimension(idd, buildUniversalTagSet(), int1));
		testObject2=buildSimpleContext(buildDimension(ide,buildUniversalTagSet(), int1),
                                       buildDimension(idd, buildUniversalTagSet(), int3),
                                       buildDimension(idf, buildUniversalTagSet(), int4));
        GIPSYContext result=buildContextSet(buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int1),
        		                                               buildDimension(idd, buildUniversalTagSet(), int1),
        		                                               buildDimension(idf, buildUniversalTagSet(), int4)),
        		                            buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int1),
                		                                       buildDimension(idd, buildUniversalTagSet(), int2),
                		                                       buildDimension(idf, buildUniversalTagSet(), int4)),
                		                    buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int1),
                        		                               buildDimension(idd, buildUniversalTagSet(), int3),
                        		                               buildDimension(idf, buildUniversalTagSet(), int4)),
                        		                                            
                        		           buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int2),
                                		                      buildDimension(idd, buildUniversalTagSet(), int1),
                                		                      buildDimension(idf, buildUniversalTagSet(), int4)),
                                		   buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int2),
                                        		              buildDimension(idd, buildUniversalTagSet(), int2),
                                        		              buildDimension(idf, buildUniversalTagSet(), int4)),
                                           buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int2),
                                                		      buildDimension(idd, buildUniversalTagSet(), int3),
                                                		      buildDimension(idf, buildUniversalTagSet(), int4)),
                                                		                                               
                                            buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int3),
                                                        	   buildDimension(idd, buildUniversalTagSet(), int1),
                                                        	   buildDimension(idf, buildUniversalTagSet(), int4)),
                                            buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int3),
                                                               buildDimension(idd, buildUniversalTagSet(), int2),
                                                               buildDimension(idf, buildUniversalTagSet(), int4)),
                                            buildSimpleContext(buildDimension(ide, buildUniversalTagSet(), int3),
                                                               buildDimension(idd, buildUniversalTagSet(), int3),
                                                               buildDimension(idf, buildUniversalTagSet(), int4)));
	
	                                        Assert.assertEquals(result, testObject1.computeUndirectedRange(testObject2));
	}
	
	public void testDirectedRange()
	{
		testObject1=buildSimpleContext(buildDimension(ide,buildUniversalTagSet(), int3),
                                       buildDimension(idd, buildUniversalTagSet(), int1));
        testObject2=buildSimpleContext(buildDimension(ide,buildUniversalTagSet(), int1),
                                       buildDimension(idd, buildUniversalTagSet(), int3),
                                       buildDimension(idf, buildUniversalTagSet(), int4));
        GIPSYContext result=buildContextSet(buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int1),
                                                               buildDimension(idf, buildUniversalTagSet(), int4)),
                                            buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int2),
                                                               buildDimension(idf, buildUniversalTagSet(), int4)), 
                                            buildSimpleContext(buildDimension(idd, buildUniversalTagSet(), int3),
                                                               buildDimension(idf, buildUniversalTagSet(), int4)));
        Assert.assertEquals(result, testObject1.computeDirectedRange(testObject2));
    
	}
	
	

}
