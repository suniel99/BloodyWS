package gipsy.lang;
import gipsy.lang.context.Dimension;
import gipsy.util.GIPSYException;
import gipsy.util.GIPSYRuntimeException;

import java.util.Random;
import java.util.Vector;

import marf.util.FreeVector;


/**
 * Representation of the context as a first-class value.
 * @author Xin Tong
 * @author Serguei Mokhov
 * @version $Id$
 */
public class GIPSYContext
extends GIPSYType
{
	/**
	 * For serialization versioning. 
	 */
	private static final long serialVersionUID = -4272466348985894921L;

	/**
	 * Denotes a simple context type (a set of dimensions).
	 */
	public static final int SIMPLE_CONTEXT = 0;

	/**
	 * Denotes a complex context type (a set of contexts). 
	 */
	public static final int CONTEXT_SET = 1;

	/**
	 * A set of dimensions or entire contexts.
	 */
	private FreeVector<GIPSYType> oSet = null;

	/**
	 * Default is a simple context type (a set of dimensions with their tags).
	 */
	protected int iContextType = SIMPLE_CONTEXT;

	
	/**
	 * Constructs an empty simple context.
	 */
	public GIPSYContext()
	{
		this.oSet = new FreeVector<GIPSYType>();
		this.iType = TYPE_CONTEXT;
	}
	
	/**
	 * Constructs an empty context of specified type.
	 * @param piContextType
	 */
	public GIPSYContext(int piContextType)
	{
		this();
		this.iContextType = piContextType;
	}

	/**
	 * Constructs presumed simple non-empty context.
	 * @param poSet
	 */
	public GIPSYContext(FreeVector<GIPSYType> poSet)
	{
		this.oSet = poSet;
		this.iType = TYPE_CONTEXT;
	}

	public GIPSYContext(int piContextType, FreeVector<GIPSYType> poSet)
	{
		this.oSet = poSet;
		this.iContextType = piContextType;
		this.iType = TYPE_CONTEXT;
	}
	
	public GIPSYContext(GIPSYContext poOtherContext)
	{
		this.oSet = poOtherContext.getSet();
		this.iContextType = poOtherContext.iContextType;
		this.iType = poOtherContext.iType;
	}
	
	public void setContextType(int piContextType)
	{
		this.iContextType = piContextType;
	}
	
	public void setSet(FreeVector<GIPSYType> poSet)
	{
		this.oSet = poSet;
	}
	
	/**
	 * For reference safety, return a copy of this.oSet.
	 * @return
	 */
	public FreeVector<GIPSYType> getSet()
	{
		return new FreeVector<GIPSYType>(this.oSet); 
	}
	
	/**
	 * @return
	 */
	public int size()
	{
		return this.oSet.size();
	}
	
	/**
	 * This is the representation of an empty context.
	 * A context whose oSet.size()=0 is considered to be an empty context.
	 * @return
	 */
	public boolean isEmptyContext()
	{
		return size() == 0;
	}
	
	/**
	 * @return the copy of the set
	 * @see gipsy.lang.GIPSYType#getEnclosedTypeOject()
	 */
	public Object getEnclosedTypeOject()
	{
		return getSet();
	}

	/**
	 * If the order of the elements in these two contexts are different,
	 * they are not equal.
	 */
	public boolean equals(Object poOtherObject)
	{
		if(this.getClass()!=poOtherObject.getClass()||this.iContextType!=((GIPSYContext)poOtherObject).iContextType)
			return false;
		else
		{
			return this.oSet.equals(((GIPSYContext)poOtherObject).getSet());
		}
	}
	
	/*public String toString()
	{
		String result=new String();
		if(this.iContextType==SIMPLECONTEXT)
		{
			for(int i=0; i<this.oSet.size(); i++)
			{
				Dimension tempCE=(Dimension)this.getSet().elementAt(i);
				if(i==0)
					result=tempCE.toString();
				else
					result=result+" , "+tempCE.toString(); 
			}
			result="[ "+result+" ]";
		}
		if(this.iContextType==CONTEXTSET)
		{
			for(int i=0; i<this.oSet.size(); i++)
			{
				// Get next simple context out of a collection of such contexts
				GIPSYContext tempSC=(GIPSYContext)this.getSet().elementAt(i);				
			    for(int j=0; j<tempSC.size(); j++)
			    {
			    	Dimension tempCE=(Dimension)tempSC.getSet().elementAt(j);
			        if(j==0)
			        {
			        	result=tempCE.toString();
			        }
			        else
			        {
			        	result=result+" , "+tempCE.toString();
			        }
			    }
			    result="[ "+result+" ]";
			    if(i==0)
			    	result=result;
			    else
			    	result=result+" , "+tempSC.toString();
			}
			result="{" + result+"}";
		}
		return result;
	}
	*/

	/**
	 * @param poDimension
	 * @since Serguei, November 13, 2008
	 */
	public void addDimension(Dimension poDimension)
	{
		addElement(poDimension);
	}
	
	/**
	 * @param poContext
	 * @since Serguei, November 13, 2008
	 */
	public void addContext(GIPSYContext poContext)
	{
		addElement(poContext);
	}

	/**
	 * Including adding the objects of Dimension and GIPSYContext.
	 * Semantic checking is;
	 * 1. simple context: the element to be added is of type Dimension;
	 * 2. context set: the element to be added is of type GIPSYContext.
	 * @param poElement
	 */
	//public void addElement(Object pelement)
	public void addElement(GIPSYType poElement)
	{
		switch(this.iContextType)
		{
			case SIMPLE_CONTEXT:
			{
				if(poElement instanceof Dimension)
				{
					break;
				}
				else
				{
					throw new GIPSYRuntimeException("Simple contexts can only consist of values of Dimension type; encountered: " + poElement);
				}
			}

			case CONTEXT_SET:
			{
				if(poElement instanceof GIPSYContext)
				{
					break;
				}
				else
				{
					throw new GIPSYRuntimeException("Simple contexts can only consist of values of Dimension type; encountered: " + poElement);
				}
			}
			
			// Should not happen
			default:
			{
				throw new GIPSYRuntimeException("Invalid context type: " + this.iContextType);
			}
		}

		this.oSet.add(poElement);
	}

	public void removeElement(int piIndex)
	{
		this.oSet.remove(piIndex);
	}
	
	public void removeElement(Object poElement)
	{
		this.oSet.remove(poElement);
	}
	
	public void clear()
	{
		this.oSet.clear();
	}

	//public void insertElementAt(Object obj, int i)
	public void insertElementAt(GIPSYType poElement, int piIndex)
	{
		this.oSet.insertElementAt(poElement, piIndex);
	}
	
	
	
	/**
	 * @return the collection of dimensions within this gipsy.lang.context
	 */
	//public Vector<Dimension> getDemensions()
	public FreeVector<GIPSYType> getDemensions()
	{
		//Vector<Dimension> tempV=new Vector<Dimension>();
		FreeVector<GIPSYType> tempV=new FreeVector<GIPSYType>();
		if(this.iContextType==SIMPLE_CONTEXT)
		{
			for(int i=0; i<oSet.size(); i++)
			{
				Dimension tempCE=(Dimension)oSet.elementAt(i);  //Type casting which assumes that the elements in simplecontext oSet are all of Dimension type
			    tempV.add(tempCE);
			}
		}
		if(this.iContextType==CONTEXT_SET)
		{
			for(int j=0; j<oSet.size(); j++)
			{
				GIPSYContext tempSC=(GIPSYContext)oSet.elementAt(j); //Type casting, assume all the elements in ContextSet are of type SimpleContext.
			    for(int k=0; k<tempSC.size(); k++)
			    {
			    	Dimension tempCE=(Dimension)tempSC.getSet().elementAt(k);  //Type casting which assumes that the elements in simplecontext oSet are all of Dimension type
				    tempV.add(tempCE);
			    }
			}
		}
		removeDuplicate(tempV);
		return tempV;
	}
    
	/**
	 * Return all the tags in a gipsy.lang.context(TagSet is the collection of tags within one dimension).
	 * The type of tags is still an issue.
	 * @return
	 */
	public FreeVector<GIPSYType> getTags()
    {
    	//Vector<GIPSYType> resultV=new Vector<GIPSYType>();
    	FreeVector<GIPSYType> resultV=new FreeVector<GIPSYType>();
		for(int i=0; i<oSet.size(); i++)
    	{
    		if(this.iContextType==SIMPLE_CONTEXT)
    		{
    			GIPSYType tempTag=((Dimension)oSet.elementAt(i)).getCurrentTag();    	
    		    resultV.add(tempTag);
    		}
    		if(this.iContextType==CONTEXT_SET)
    		{
    			GIPSYContext tempSC=(GIPSYContext)oSet.elementAt(i);
    			for(int j=0; j<tempSC.size(); j++)
    			{
    				Dimension tempCE=(Dimension)tempSC.getSet().elementAt(j);
    				GIPSYType tempTag=tempCE.getCurrentTag();    	
        		    resultV.add(tempTag);     				
    			}
    						
    		}
    	}

		removeDuplicate(resultV);
   
		return resultV;
    }
	
	/**
	 * Retrieving the given dimension in this gipsy.lang.context.
	 * @param pdimension
	 * @return
	 */
	public boolean isDimensionInContext(Dimension pdimension)
	{
		//Vector<Dimension> tempV= getDemensions();
		FreeVector<GIPSYType> tempV= getDemensions();
		boolean result=false;
		for(int i=0; i<tempV.size(); i++)
		{
			result=pdimension.equals(tempV.elementAt(i));
			if(result==true)
				break;
		}
		
		return result;
	}
	
	/**
	 * Determines if the parameter is part of any tags in the context.
	 * @param ptag
	 * @return
	 */
	public boolean isTagInContext(GIPSYType ptag)
	{
		Vector<GIPSYType> tempV=this.getTags();
		boolean result=false;
		for(int i=0; i<tempV.size(); i++)
		{
			result=ptag.equals(tempV.elementAt(i));
			if(result==true)
				break;
		}
		
		return result;
			
	}
	
	/**
	 * returns true if the pContext.oSet contains all the elements in this.oSet, regardless of the order. Empty context is the sub context of any context
	 * @param pContext
	 * @return
	 */
	public boolean isSubContext(GIPSYContext pContext)
	{
		if(isEmptyContext())
			return true;
		else
		{
			/*
			 *because oSet.containsAll(Collection c) will throw a 
			 *NullPointerException if c is null, then system halts. 
			 *But this does not affect the concept of context operators. 
			 *
			 */
			try{
				return pContext.oSet.containsAll(this.oSet);
			}
			catch(NullPointerException e)
			{
				return false;
			}
		}
	}
	
	/**
	 * return a gipsy.lang.context whose dimensions are in this gipsy.lang.context but not in the parameter gipsy.lang.context.
	 * @param poContext
	 * @return
	 */
	public GIPSYContext extract(GIPSYContext poContext)
	{
		if(this.iContextType == SIMPLE_CONTEXT && poContext.iContextType == SIMPLE_CONTEXT)
		{
			GIPSYContext oCxt1 = new GIPSYContext(this);
			GIPSYContext oCxt2 = new GIPSYContext(poContext);
			GIPSYContext oResult = extractSimpleContext(oCxt1, oCxt2);
			return oResult;
		}

		if(this.iContextType == CONTEXT_SET && poContext.iContextType == CONTEXT_SET)
		{
			GIPSYContext oCxt1 = new GIPSYContext(this);
			GIPSYContext oCxt2 = new GIPSYContext(poContext);
		
			FreeVector<GIPSYType> oResultV = new FreeVector<GIPSYType>();
		
			for(int i = 0; i < oCxt2.size(); i++)
			{
				GIPSYContext oTempSC2 = (GIPSYContext)oCxt2.getSet().elementAt(i);
				
				for(int j = 0; j < oCxt1.size(); j++)
				{
					GIPSYContext oTempSC1 = (GIPSYContext)oCxt1.getSet().elementAt(j);
					GIPSYContext oTempResult = extractSimpleContext(oTempSC1, oTempSC2);
					
					if(oTempResult != null)
					{
						oResultV.add(oTempResult);
					}
				}
			}

			if(oResultV.size() == 0)
			{
				return null;
			}
			else
			{
				removeDuplicate(oResultV);
				return(new GIPSYContext(CONTEXT_SET, oResultV));
			}
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * This gipsy.lang.context is overridden by the pcontext()
	 * @param poContext
	 * @return 
	 */
	public GIPSYContext override(GIPSYContext poContext)
	{	
		if(this.iContextType==SIMPLE_CONTEXT && poContext.iContextType==SIMPLE_CONTEXT)
		{
			GIPSYContext op1=new GIPSYContext(this);
			GIPSYContext op2=new GIPSYContext(poContext);
			GIPSYContext result=overrideSimpleContext(op1, op2);
			return result;			
		}
		if(this.iContextType==CONTEXT_SET && poContext.iContextType==CONTEXT_SET)
		{
			GIPSYContext op1=new GIPSYContext(this);
			GIPSYContext op2=new GIPSYContext(poContext);
			FreeVector<GIPSYType> resultV=new FreeVector<GIPSYType>();
			
			for(int p=0; p<op1.size(); p++)
			{
				GIPSYContext tempSC1=(GIPSYContext)op1.getSet().elementAt(p);
				for(int q=0; q<op2.size(); q++)
				{
					GIPSYContext tempSC2=(GIPSYContext)op2.getSet().elementAt(q);
					GIPSYContext tempResult=overrideSimpleContext(tempSC1,tempSC2);
				    if(tempResult.size()!=0)
				    {
					    resultV.add(tempResult);	
				    }
				}
			}
			if(resultV.size()==0)
				return new GIPSYContext(CONTEXT_SET);
			else
			{
				removeDuplicate(resultV);
				return new GIPSYContext(CONTEXT_SET, resultV);
			}
		}
		else 
			return null;
	}
	/**
	 * This gipsy.lang.context is projected by the pdimension.
	 * @param pcontext
	 * @return a gipsy.lang.context whose elements' dimensions are in the pdimset. 
	 */
	public GIPSYContext projection(Vector<Dimension> pDimensionSet)
	{
		if(this.iContextType==SIMPLE_CONTEXT)
		{
			GIPSYContext scOp=new GIPSYContext(this);
			Vector<Dimension> dimOp= new Vector<Dimension>(pDimensionSet);
			GIPSYContext resultSC=projectSimpleContext(scOp, dimOp);
			return resultSC;
		}
		
		if(this.iContextType==CONTEXT_SET)
		{
			GIPSYContext csOp=new GIPSYContext(this);
			Vector<Dimension> dimOp=new Vector<Dimension>(pDimensionSet);
			FreeVector<GIPSYType> resultV=new FreeVector<GIPSYType>();
			for(int i=0; i<csOp.size(); i++)
			{
				GIPSYContext tempSC=(GIPSYContext)(csOp.getSet().elementAt(i));
				GIPSYContext tempResult=projectSimpleContext(tempSC, dimOp);
			    //if the result of projecting simple context and dimension set is an empty simple context, then do not add.
				if(tempResult.size()!=0)
			    {
			    	resultV.addElement(tempResult);
			    }
			}
			//empty context set
			if(resultV.size()==0)
				return new GIPSYContext(CONTEXT_SET);
			else
			{
				removeDuplicate(resultV);
				return(new GIPSYContext(CONTEXT_SET, resultV));
			}
		}
		else
			//semantic error
			return null;
	}
	
	/**
	 * Hide the elements whose dimensions are in pdim.
	 * @param pDimensionSet
	 * @return
	 */
	public GIPSYContext hiding(Vector<Dimension> pDimensionSet)
	{
		if(this.iContextType==SIMPLE_CONTEXT)
		{
			GIPSYContext scOp=new GIPSYContext(this);
			Vector<Dimension> dimOp =new Vector<Dimension>(pDimensionSet);
			GIPSYContext result=hideSimpleContext(scOp,dimOp);
			return result;
		
		}
		if(this.iContextType==CONTEXT_SET)
		{
			GIPSYContext csOp=new GIPSYContext(this);
			Vector<Dimension> dimOp =new Vector<Dimension>(pDimensionSet);
			//Vector<GIPSYContext> resultV=new Vector<GIPSYContext>();
			FreeVector<GIPSYType> resultV=new FreeVector<GIPSYType>();
			for(int i=0; i<csOp.size();i++)
			{
				GIPSYContext tempSC=(GIPSYContext)csOp.getSet().elementAt(i);
				GIPSYContext tempResult=hideSimpleContext(tempSC, dimOp);
				if(tempResult.size()!=0)
				{
					resultV.addElement(tempResult);
				}			
			}
			
			if(resultV.size()==0)
				return null;
			else
			{
				removeDuplicate(resultV);
				return(new GIPSYContext(CONTEXT_SET,resultV));
			}
		}
		else
			return null;
	}
	public GIPSYContext difference(GIPSYContext pcontext)
	{
		if(this.iContextType==SIMPLE_CONTEXT && pcontext.iContextType==SIMPLE_CONTEXT)
		{
			GIPSYContext op1=new GIPSYContext(this);
			GIPSYContext op2=new GIPSYContext(pcontext);
			GIPSYContext result=differentiateSimpleContext(op1, op2);
		    return result;
		}
		if(this.iContextType==CONTEXT_SET && pcontext.iContextType==CONTEXT_SET)
		{
			GIPSYContext op1=new GIPSYContext(this);
			GIPSYContext op2=new GIPSYContext(pcontext);
//			Vector<GIPSYContext>resultV=new Vector<GIPSYContext>();
			FreeVector<GIPSYType>resultV=new FreeVector<GIPSYType>();
			
			//the result is an empty context set
			if(op1.isSubContext(op2))
				return(new GIPSYContext(GIPSYContext.CONTEXT_SET));
			else
			{
				for(int i=0; i<op1.size(); i++)
				{
					GIPSYContext tempSC1=(GIPSYContext)op1.getSet().elementAt(i);
				    for(int j=0; j<op2.size(); j++)
				    {
				    	GIPSYContext tempSC2=(GIPSYContext)op2.getSet().elementAt(j);
				    	GIPSYContext tempResult=differentiateSimpleContext(tempSC1, tempSC2);
				       
				        if(!tempResult.isEmptyContext())
				        {
					    	resultV.addElement(tempResult);
				        }
				    }
				}
				
					removeDuplicate(resultV);
					return (new GIPSYContext(CONTEXT_SET, resultV));
				
			}
			
		}
		//This should be an semantic error, leave for now
		else
			return null;
	}
	
	public GIPSYContext substitution(Dimension pDimension)
	{
		if(this.iContextType==SIMPLE_CONTEXT)
		{
			GIPSYContext scOp=new GIPSYContext(this);
			Dimension ceOp=new Dimension(pDimension);
			GIPSYContext result=substituteSimpleContext(scOp, ceOp);
		    return result;
		}
		if(this.iContextType==CONTEXT_SET)
		{
			GIPSYContext csOp=new GIPSYContext(this);
			Dimension ceOp=new Dimension(pDimension);
			//Vector<GIPSYContext> resultV=new Vector<GIPSYContext>();
			FreeVector<GIPSYType> resultV=new FreeVector<GIPSYType>();
			for(int i=0; i<csOp.size();i++)
			{
				GIPSYContext tempSC=(GIPSYContext)csOp.getSet().elementAt(i);
				GIPSYContext tempResult=substituteSimpleContext(tempSC, ceOp);
				if(tempResult!=null)
				{
					resultV.add(tempResult);		
				}
				
			}
			if(resultV.size()==0)
				return null;
			else
			{
				removeDuplicate(resultV);
				return(new GIPSYContext(CONTEXT_SET,resultV));
				
			}
		}
		else
			return null;
	}
	public GIPSYContext intersection(GIPSYContext pcontext)
	{
		if(this.iContextType==SIMPLE_CONTEXT)
		{
			GIPSYContext op1=new GIPSYContext(this);
			GIPSYContext op2=new GIPSYContext(pcontext);
			GIPSYContext result=intersectSimpleContext(op1, op2);
		    return result;
		}
		if(this.iContextType==CONTEXT_SET)
		{
			GIPSYContext op1=new GIPSYContext(this);
			GIPSYContext op2=new GIPSYContext(pcontext);
			//Vector<GIPSYContext>resultV=new Vector<GIPSYContext>();
			FreeVector<GIPSYType>resultV=new FreeVector<GIPSYType>();
 			for(int i=0; i<op1.size(); i++)
			{
 				GIPSYContext tempSC1=(GIPSYContext)op1.getSet().elementAt(i);
			    for(int j=0; j<op2.size(); j++)
			    {
			    	GIPSYContext tempSC2=(GIPSYContext)op2.getSet().elementAt(j);
			    	GIPSYContext tempResult=intersectSimpleContext(tempSC1, tempSC2);
			    	if(tempResult.size()!=0)
			    	{
			    		resultV.add(tempResult);
			    	}
			        
			    }
			}
			//Empty context set 
 			if(resultV.size()==0)
			{
				return new GIPSYContext(CONTEXT_SET);
			}
			else
			{

	 			removeDuplicate(resultV);
				return (new GIPSYContext(CONTEXT_SET,resultV));
				
			}
		}
		else
			return null;
	
	}
	public GIPSYContext union(GIPSYContext pcontext)
	{
		if(this.iContextType==SIMPLE_CONTEXT)
		{
			GIPSYContext op1=new GIPSYContext(this);
			GIPSYContext op2=new GIPSYContext(pcontext);
			GIPSYContext result=unionSimpleContext(op1, op2);
		    return result;
		}
		if(this.iContextType==CONTEXT_SET)
		{
			GIPSYContext op1=new GIPSYContext(this);
			GIPSYContext op2=new GIPSYContext(pcontext);
			//Vector<GIPSYContext>resultV=new Vector<GIPSYContext>();
			FreeVector<GIPSYType>resultV=new FreeVector<GIPSYType>();
 			for(int i=0; i<op2.size(); i++)
			{
 				GIPSYContext tempSC2=(GIPSYContext)op2.getSet().elementAt(i);
			    for(int j=0; j<op1.size(); j++)
			    {
			    	GIPSYContext tempSC1=(GIPSYContext)op1.getSet().elementAt(j);
			    	GIPSYContext tempResult=unionSimpleContext(tempSC1, tempSC2);
			    	if(tempResult!=null)
			    	{
			    		resultV.add(tempResult);
			    	}
			        
			    }
			}
			
 			if(resultV.size()==0)
 				return null;
 			else
 			{
 				removeDuplicate(resultV);
 				return (new GIPSYContext(CONTEXT_SET,resultV));
 			}
 			
		}
		else
			return null;
	
		
	}
	
	/**
	 * This method takes a finite number of contexts and nondeterministically returns one of the contexts.
	 * @param pcontext
	 * @return
	 */
	public GIPSYContext choose(GIPSYContext[] pcontext)
	{
		Random generator=new Random();
		int seed=generator.nextInt(pcontext.length-1);
		
		if(this.iContextType==SIMPLE_CONTEXT || this.iContextType==CONTEXT_SET)
		{
			GIPSYContext result=pcontext[seed];
			return result;
		}
		else
			return null;
	}
	
    /** c1 -> c2 
     * The only difference between this one and computeUndirectedRange is that if the tag in c1 is smaller than c2, ignore
     * @param pSimpleContext
     * @return
     */
	public GIPSYContext computeDirectedRange(GIPSYContext pSimpleContext)
	{
		GIPSYContext op1=new GIPSYContext(this);
		GIPSYContext op2=new GIPSYContext(pSimpleContext);
		
		Vector<Dimension> cePicked1=new Vector<Dimension>(); //collection of the Dimensions that have been picked up(share same dimensions)to construct Y
		Vector<Dimension> cePicked2=new Vector<Dimension>();
		
		Vector<Dimension> ceLeft1=new Vector<Dimension>(); //contains the Dimensions that have not been picked up(share same dimensions)
		Vector<Dimension> ceLeft2=new Vector<Dimension>();
		
		GIPSYContext Yi=new GIPSYContext(); //[e:1, e:2]...
		GIPSYContext YiCollection=new GIPSYContext(CONTEXT_SET); //{[e:1, e:2],[d:1, d:2] }
		
		GIPSYContext Y=new GIPSYContext(CONTEXT_SET); //{[e:1, d:1], [e:1, d:2], [e:2, d:1], [e:2, d:2]}
		GIPSYContext Z=new GIPSYContext(CONTEXT_SET); //{[f:1, e:1, d:1, t:4]....}
		
		for(int i=0; i<op1.size(); i++)
		{
			Dimension tempCE1=(Dimension)op1.getSet().elementAt(i);
			Dimension tempCE2=new Dimension();
		    for(int j=0; j<op2.size(); j++)
		    {
		    	tempCE2=(Dimension)op2.getSet().elementAt(j);
		    	if(tempCE1.getDimensionName().equals(tempCE2.getDimensionName())&& tempCE1.getDimensionTags().equals(tempCE2.getDimensionTags()))
		    	{
		    		
		    		cePicked2.add(tempCE2);
		    		cePicked1.add(tempCE1);	 
		    		if(((GIPSYInteger)tempCE1.getCurrentTag()).getValue()<((GIPSYInteger)tempCE2.getCurrentTag()).getValue())
			    	{
			    		Yi=buildYi(tempCE1, tempCE2);  //[e:1, e:2]
				        YiCollection.addElement(Yi);
			    	}
		    		break;
		    	}
		    }
		  }
		
		//Start building {[e:1, d:1],[e:1, d:2]...}
		int iniposition=0;
		GIPSYContext midY=new GIPSYContext();
		buildY(YiCollection, Y, midY, iniposition );    
		
		ceLeft1=getRemainingDimensions(op1, cePicked1);
		ceLeft2=getRemainingDimensions(op2, cePicked2);
		
		
		//Start building Z {[f:1, e:1, d:1, t:4]...}
		
		Z=buildZ(ceLeft1, ceLeft2, Y);
		
	    return Z;
	
	}
	
	/**
	 * @param pSimpleContext
	 * @return
	 */
	public GIPSYContext computeUndirectedRange(GIPSYContext pSimpleContext)
	{
		GIPSYContext op1=new GIPSYContext(this);
		GIPSYContext op2=new GIPSYContext(pSimpleContext);
		
		Vector<Dimension> cePicked1=new Vector<Dimension>(); //collection of the Dimensions(the notion of context element is the thing really matters) that have been picked up(share same dimensions)to construct Y
		Vector<Dimension> cePicked2=new Vector<Dimension>();
		
		Vector<Dimension> ceLeft1=new Vector<Dimension>(); //contains the Dimensions that have not been picked(there's no same dimension name for them in the other operand)
		Vector<Dimension> ceLeft2=new Vector<Dimension>();
		
		GIPSYContext Yi=new GIPSYContext(); //[e:1, e:2]...
		GIPSYContext YiCollection=new GIPSYContext(CONTEXT_SET); //{[e:1, e:2],[d:1, d:2] }
		
		GIPSYContext Y=new GIPSYContext(CONTEXT_SET); //{[e:1, d:1], [e:1, d:2], [e:2, d:1], [e:2, d:2]}
		GIPSYContext Z=new GIPSYContext(CONTEXT_SET); //{[f:1, e:1, d:1, t:4]....} added those left things as well
		
		try{
			
			for(int i=0; i<op1.size(); i++)
			{
				Dimension tempCE1=(Dimension)op1.getSet().elementAt(i);
				Dimension tempCE2=new Dimension();
				if(!(tempCE1.getCurrentTag() instanceof GIPSYInteger))
					throw new GIPSYException();
			    for(int j=0; j<op2.size(); j++)
			    {
			    	tempCE2=(Dimension)op2.getSet().elementAt(j);
		    	    if(!(tempCE2.getCurrentTag() instanceof GIPSYInteger))
		    	    	throw new GIPSYException();
			    	
		    	    if(tempCE1.getDimensionName().equals(tempCE2.getDimensionName()) && tempCE1.getDimensionTags().equals(tempCE2.getDimensionTags()))
			    	{
			    		cePicked2.add(tempCE2);
			    		cePicked1.add(tempCE1);	    
				        Yi=buildYi(tempCE1, tempCE2);  //[e:1, e:2]
				        YiCollection.addElement(Yi);
				        break;
			    	}
		    	
		    }
		}
		
		//Start building {[e:1, d:1],[e:1, d:2]...}
		int iniposition=0;
		GIPSYContext midY=new GIPSYContext(SIMPLE_CONTEXT);
		buildY(YiCollection, Y, midY, iniposition );    
		
		ceLeft1=getRemainingDimensions(op1, cePicked1);
		ceLeft2=getRemainingDimensions(op2, cePicked2);
		
		
		//Start building Z {[f:1, e:1, d:1, t:4]...}
		
		Z=buildZ(ceLeft1, ceLeft2, Y);	    
		
		return Z;
		}
		
		catch(GIPSYException e)
		{
			System.err.println(e.getMessage());
			return null;
		}
		

	}
	//e:3 e:1 => e:1 e:2 e:3
	private static GIPSYContext buildYi(Dimension pDimension1, Dimension pDimension2)
	{
		
		Dimension dimension1=new Dimension(pDimension1);
		dimension1.setCurrentTag(pDimension1.getCurrentTag());
		Dimension dimension2=new Dimension(pDimension2);
		dimension2.setCurrentTag(pDimension2.getCurrentTag());
		
		GIPSYContext resultSC=new GIPSYContext();
		try{
			
			//Dimension tempCEini=v1.elementAt(0);
			if(!(dimension1.getCurrentTag() instanceof GIPSYInteger))
				throw new GIPSYException();
			
			// XXX: Not 100% safe as there may be a rare loss of precision from long->int
			int max=((GIPSYInteger)dimension1.getCurrentTag()).getValue().intValue();
		    int min=((GIPSYInteger)dimension1.getCurrentTag()).getValue().intValue();
		   
		    	if(max<((GIPSYInteger)dimension2.getCurrentTag()).getValue() | min>((GIPSYInteger)dimension2.getCurrentTag()).getValue())
		    	{
		    		if(max<((GIPSYInteger)dimension2.getCurrentTag()).getValue())
				{
	    			// XXX: Not 100% safe as there may be a rare loss of precision from long->int
					max=((GIPSYInteger)dimension2.getCurrentTag()).getValue().intValue();
				}
				
				if(min>((GIPSYInteger)dimension2.getCurrentTag()).getValue())
				{
	    			// XXX: Not 100% safe as there may be a rare loss of precision from long->int
					min=((GIPSYInteger)dimension2.getCurrentTag()).getValue().intValue();
				}
			}			
		
		
		for(int t=min; t<=max; t++)
		{
			
			Dimension tempCE=new Dimension(dimension1); 
			tempCE.setCurrentTag(new GIPSYInteger(t));
			resultSC.addElement(tempCE);
		}
		
		return new GIPSYContext(resultSC);
		}
		catch(GIPSYException e)
		{
			System.err.println(e.getMessage());
			return null;
		}
		
	}
	
	
//	join tempYi1 and tempYi2 {[e:1, d:1], [e:1, d:2], [e:1, d:3], [e:2, d:1], [e:2, d:2], [e:2, d:3], [e:3, d:1], [e:3, d:2], [e:3, d:3]}
    
	private static void buildY(GIPSYContext pYicollection, GIPSYContext result,  GIPSYContext midResult, int position)
	
	{
		
		if(position==pYicollection.size()) //reach the end of Yicollections
		{
			result.addElement(new GIPSYContext(midResult));
			midResult.removeElement(midResult.getSet().lastElement());
			return;
		}
		else
		{
			
			GIPSYContext tempSC=(GIPSYContext)pYicollection.getSet().elementAt(position);
			position++;
			for(int i=0; i<tempSC.size(); i++) //control changing the outer most ces
			{
				Dimension tempCE=(Dimension)tempSC.getSet().elementAt(i);
				midResult.addElement(tempCE);
				
				buildY(pYicollection, result, midResult, position);
			}
			
			if(midResult.size()!=0)
			{
				midResult.removeElement(midResult.getSet().lastElement()); //pop out the inner most ces, if no ce left, the recursive call ends.
			}
		}
		
	}
	
	private static GIPSYContext buildZ(Vector<Dimension> pleft1, Vector<Dimension> pleft2, GIPSYContext pY)
	{
		Vector<Dimension>left1=new Vector<Dimension>(pleft1);
		Vector<Dimension>left2=new Vector<Dimension>(pleft2);
		GIPSYContext Y=new GIPSYContext(pY);
		GIPSYContext Z=new GIPSYContext(CONTEXT_SET);
		for(int i=0; i<Y.size();i++)
		{
			GIPSYContext tempSC=(GIPSYContext)Y.getSet().elementAt(i);
		    for(int p=0; p<left1.size(); p++)
		    {
		    	Dimension tempCE=left1.elementAt(p);
		    	tempSC.insertElementAt(tempCE, p);
		    }
		    
		    for(int q=0; q<left2.size(); q++)
		    {
		    	Dimension tempCE=left2.elementAt(q);
		    	tempSC.addElement(tempCE);		    	
		    }
		    Z.addElement(tempSC);
		}
		
		return Z;
	}
	
	private Vector getRemainingDimensions(GIPSYContext op, Vector cePicked)
	{

		Vector<Dimension> ceLeft=new Vector<Dimension>();
		boolean picked=false;
		for(int p=0; p<op.size(); p++)
		{
			Dimension tempCE1=(Dimension)op.getSet().elementAt(p);
			for(int q=0; q<cePicked.size(); q++)
			{
				Dimension tempCE2=(Dimension)cePicked.elementAt(q);
				if(tempCE1.equals(tempCE2))
				{
					picked=true;
					break;
				}
			}
			
			if(picked==false)
			{
				ceLeft.add(tempCE1);
			}
			else
				picked=false;
		}
		
		return ceLeft;
	}
		

	
	
	
	
	
	
	
	/**
	 * Help method to remove the duplicate elements in a Vector.
	 */
	private static FreeVector<GIPSYType> removeDuplicate(FreeVector<GIPSYType> pVector)
	{
		for(int i=0; i<pVector.size(); i++)
		{
			Object temp1=pVector.elementAt(i);
			for(int j=i+1; j<pVector.size(); j++)
			{
				Object temp2=pVector.elementAt(j);
				if(temp1.equals(temp2))
				//if(pVector.elementAt(i).equals(pVector.elementAt(j)))
				{
					pVector.remove(j);
				}
			}
		}
		
		return pVector;		
	}
	
	private static GIPSYContext extractSimpleContext(GIPSYContext psimple1, GIPSYContext psimple2)
	{
		GIPSYContext op1=new GIPSYContext(psimple1);
		GIPSYContext op2=new GIPSYContext(psimple2);
		//Vector<Dimension> resultV=op1.getSet();
		FreeVector<GIPSYType> resultV=op1.getSet();
		Vector<Dimension> tempV=new Vector<Dimension>();
		for(int i=0; i<op1.size(); i++)
		{
			Dimension tempCE1=(Dimension)op1.getSet().elementAt(i);
			Dimension tempDim1=((Dimension)(op1.getSet().elementAt(i)));
			if(op2.isDimensionInContext(tempDim1))
			{
				tempV.add(tempCE1);				
			}
		}
		
		for(int t=0; t< tempV.size(); t++)
		{
			resultV.remove(tempV.elementAt(t));
		}
		
		if(resultV.size()==0)
			return null;
		else
			return(new GIPSYContext(SIMPLE_CONTEXT,resultV));
	
	}
	
	
	private static GIPSYContext overrideSimpleContext(GIPSYContext pSimpleContext1, GIPSYContext pSimpleContext2)
	{
		//Vector<Dimension> resultV=new Vector<Dimension>();
		FreeVector<GIPSYType> resultV=new FreeVector<GIPSYType>();
		boolean flag=false; //This is used to keep record of the Dimensions in op1, whose dimensions are not in op2
		//This is used to contain the Dimensions left in op2, whose dimensions are not in op1
		//Vector<Dimension> leftIn2=pSimpleContext2.getSet(); 
		FreeVector<GIPSYType> leftIn2=pSimpleContext2.getSet(); 
		for(int i=0; i<pSimpleContext1.size(); i++)
		{
			Dimension tempCE1=(Dimension)pSimpleContext1.getSet().elementAt(i);
		    for(int j=0; j<pSimpleContext2.size(); j++)
		    {
		    	Dimension tempCE2=(Dimension)pSimpleContext2.getSet().elementAt(j);
		        if(tempCE1.getDimensionName().equals(tempCE2.getDimensionName()) && tempCE1.getDimensionTags().equals(tempCE2.getDimensionTags()))
		        {
		           flag=true;
		           resultV.add(tempCE2);
		           leftIn2.remove(tempCE2);
		        }
		    }
		    
		    if(flag==false)//It indicates that the dimension for the current Dimension in SimpleContext1 has no match in SimpleContext2
		    {
		    	resultV.add(tempCE1);
		    }
		    flag=false;
		}
		for(int p=0; p<leftIn2.size();p++)
		{
			resultV.add(leftIn2.elementAt(p));
		}
		
		if(resultV.size()==0)
			return new GIPSYContext(SIMPLE_CONTEXT);
		else
		{
			return(new GIPSYContext(SIMPLE_CONTEXT,resultV));
		}
	}
	
	private static GIPSYContext projectSimpleContext(GIPSYContext pSimpleContext, Vector<Dimension> pDimensionSet)
	{
		//Vector<Dimension> resultV=new Vector<Dimension>();
		FreeVector<GIPSYType> resultV=new FreeVector<GIPSYType>();
		for(int i=0; i<pDimensionSet.size(); i++)
		{
			Dimension tempDim=(Dimension)pDimensionSet.elementAt(i);
			for(int j=0; j<pSimpleContext.size();j++)
			{
				Dimension tempCE=(Dimension)pSimpleContext.getSet().elementAt(j);
			    if(tempDim.getDimensionName().equals(tempCE.getDimensionName()) && tempDim.getDimensionTags().equals(tempCE.getDimensionTags()))
			    	resultV.addElement(tempCE);			    
			}
		}
		//empty context if there's no dimension in common
		if(resultV.size()==0)
			return new GIPSYContext(SIMPLE_CONTEXT);
		else
		{
			return(new GIPSYContext(SIMPLE_CONTEXT,resultV));	
		}
		
	}
	
	private static GIPSYContext hideSimpleContext(GIPSYContext pSimpleContext, Vector<Dimension> pDimensionSet)
	{
		return pSimpleContext.difference(pSimpleContext.projection(pDimensionSet));
	}
	
	private static GIPSYContext differentiateSimpleContext(GIPSYContext pSimpleContext1, GIPSYContext pSimpleContext2)
	{
		
		//Every micro context in 1 is part of 2, so substraction should be empty simplecontext
		if(pSimpleContext1.isSubContext(pSimpleContext2))
		{
			return new GIPSYContext(GIPSYContext.SIMPLE_CONTEXT);
		}
		else
		{
			//Vector<Dimension> resultV=pSimpleContext1.getSet();
			FreeVector<GIPSYType> resultV=pSimpleContext1.getSet();
			for(int i=0; i<pSimpleContext1.size();i++)
			{
				Dimension tempCE1=(Dimension)pSimpleContext1.getSet().elementAt(i);
				for(int j=0; j<pSimpleContext2.size();j++)
				{
					Dimension tempCE2=(Dimension)pSimpleContext2.getSet().elementAt(j);
				    if(tempCE2.equals(tempCE1))
				    {
				    	resultV.remove(tempCE1);
				    }
				}
			}
			removeDuplicate(resultV);
			
			return(new GIPSYContext(SIMPLE_CONTEXT, resultV));	
			
		}
	}
	
	private static GIPSYContext substituteSimpleContext(GIPSYContext psimple, Dimension pce)
	{
		GIPSYContext simple=new GIPSYContext(psimple);
		//GIPSYContext enclosingOuter=new GIPSYContext();
		//Dimension ce=simple.new Dimension(pce);
		Dimension ce=new Dimension(pce);
		//Vector<Dimension> resultV=simple.getSet();
		FreeVector<GIPSYType> resultV=simple.getSet();
		for(int i=0; i<simple.size(); i++)
		{
			Dimension tempCE=(Dimension)simple.getSet().elementAt(i);
			if(tempCE.equals(ce))
			{
				resultV.remove(tempCE);
				resultV.insertElementAt(ce, i);
			}
		}
		if(resultV.size()==0)
			return null;
		else
		{
			removeDuplicate(resultV);
			return (new GIPSYContext(SIMPLE_CONTEXT,resultV));	
		}
	}
	
	private static GIPSYContext intersectSimpleContext(GIPSYContext pSimpleContext1,GIPSYContext pSimpleContext2)
	{
		
		//Vector<Dimension> resultV=new Vector<Dimension>();
		FreeVector<GIPSYType> resultV=new FreeVector<GIPSYType>();
		for(int i=0; i<pSimpleContext2.size();i++)
		{
			Dimension tempCE2=(Dimension)pSimpleContext2.getSet().elementAt(i);
			for(int j=0; j<pSimpleContext1.size();j++)
			{
				Dimension tempCE1=(Dimension)pSimpleContext1.getSet().elementAt(j);
			    if(tempCE1.equals(tempCE2))
			    {
			    	resultV.add(tempCE1);
			    }
			}
		}
		if(resultV.size()==0)
			return new GIPSYContext(SIMPLE_CONTEXT);
		else
		{
			return(new GIPSYContext(SIMPLE_CONTEXT,resultV));
		}
		    
	}
	
	private static GIPSYContext unionSimpleContext(GIPSYContext psimple1,GIPSYContext psimple2)
	{
		GIPSYContext simple1=new GIPSYContext(psimple1);
		GIPSYContext simple2=new GIPSYContext(psimple2);
		//Vector<Dimension> resultV=simple1.getSet();
		FreeVector<GIPSYType> resultV=simple1.getSet();
		boolean flag=false;
		for(int i=0; i<simple2.size();i++)
		{
			Dimension tempCE2=(Dimension)simple2.getSet().elementAt(i);
			for(int j=0; j<simple1.size();j++)
			{
				Dimension tempCE1=(Dimension)simple1.getSet().elementAt(j);
			    if(tempCE1.equals(tempCE2))
			    {
			    	flag=true;
			    	break;
			    	//resultV.add(tempCE2);
			    }
			}
			if(flag==false)
			{
				resultV.add(tempCE2);
			}
			flag=false;
		}
		if(resultV.size()==0)
			return null;
		else
		{

		    return(new GIPSYContext(SIMPLE_CONTEXT, resultV));	
		}
	}
	
	
}

// EOF
