package gipsy.lang.context;

import java.io.Serializable;

import marf.util.FreeVector;
import gipsy.lang.GIPSYType;

/**
 * This is the super class for all the tagset classes.
 * @author Xin Tong
 * @author Serguei Mokhov
 * @version $Id$
 */
public abstract class TagSet
implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5789664431802607354L;
	
	/*
	 * All possible fields.
	 */

	private int iExpressionType;
	private int iTimes;
	private FreeVector<GIPSYType> oPeriod;
	private GIPSYType oUpper;
	private GIPSYType oLower;
	private int iStep;
	
	private FreeVector<GIPSYType> oEnumeratedElement;
	
    /*public static final int ORDERED_FINITE_PERIODIC=0;
	
	public static final int ORDERED_FINITE_NONPERIODIC_ENUMERATED=1;
	public static final int ORDERED_FINITE_NONPERIODIC_UPPERLOWER=2;
	public static final int ORDERED_FINITE_NONPERIODIC_UPPERLOWER_STEP=3;
	
	public static final int ORDERED_INFINITE_PERIODIC=4;
	
	public static final int ORDERED_INFINITE_NONPERIODIC_UPPER=5;
	public static final int ORDERED_INFINITE_NONPERIODIC_UPPER_STEP=6;
	public static final int ORDERED_INFINITE_NONPERIODIC_LOWER=7;
	public static final int ORDERED_INFINITE_NONPERIODIC_LOWER_STEP=8;
	public static final int ORDERED_INFINITE_NONPERIODIC_INT=9;
	
	public static final int UNORDERED_FINITE_PERIODIC=10;
	
	public static final int UNORDERED_FINITE_NONPERIODIC=11;
	
	public static final int UNORDERED_INFINITE_PERIODIC=12;
	
	public static final int UNORDERED_INFINITE_NONPERIODIC=13;
	*/

	
	public TagSet()
	{
		this.iExpressionType=0;
		this.iTimes=0;
		this.oPeriod=null;
		this.oUpper=null;
		this.oLower=null;
		this.iStep=0;
		this.oEnumeratedElement=null;
	}
	
	public TagSet(TagSet poTagSet)
	{
		this.iExpressionType=poTagSet.iExpressionType;
		this.iTimes=poTagSet.iTimes;
		this.oPeriod=poTagSet.oPeriod;
		this.oUpper=poTagSet.oUpper;
		this.oLower=poTagSet.oLower;
		this.iStep=poTagSet.iStep;
		this.oEnumeratedElement=poTagSet.oEnumeratedElement;
	}

	/**
	 * This is to set different tag set expressions: eg. OrderedFiniteNonPeriodic-->UpperLower, UpperLowerStep...
	 * @param piExpressionType
	 */
	public void setExpressionType(int piExpressionType)
	{
		this.iExpressionType=piExpressionType;
	}

	public void setTimes(int piTimes)
	{
		this.iTimes=piTimes;
	}
	
	public void setPeriod(FreeVector<GIPSYType> poPeriod)
	{
		this.oPeriod= poPeriod;
	}
	
	public void setUpper(GIPSYType poUpper)
	{
		this.oUpper=poUpper;
	}
	
	public void setLower(GIPSYType poLower)
	{
		this.oLower=poLower;
	}
	
	public void setStep(int piStep)
	{
		this.iStep=piStep;
	}
	
	public void setEnmuneratedElement(FreeVector<GIPSYType> poEnmuneratedElement)
	{
		this.oEnumeratedElement=poEnmuneratedElement;
	}
	
	
	
	public int getExpressionType()
	{
		return this.iExpressionType;
	}
	
	public int getTimes()
	{
		return this.iTimes;
	}
	
	public FreeVector<GIPSYType> getPeriod()
	{
		return this.oPeriod;
	}
	
	public GIPSYType getUpper()
	{
		return this.oUpper;
	}
	
	public GIPSYType getLower()
	{
		return this.oLower;
	}
	
	public int getStep()
	{
		return this.iStep;
	}
	
	public FreeVector<GIPSYType> getEnmuneratedElement()
	{
		return this.oEnumeratedElement;
	}

	
	
	public abstract boolean equals(Object otherObject);
	

	
	public abstract boolean isInTagSet(GIPSYType pTag);
	
	public abstract GIPSYType getNext(GIPSYType pCurrentTag);
	
	//@Override
	protected Object clone()
//	throws CloneNotSupportedException
	{
		try
		{
			TagSet oCopy = (TagSet)super.clone();
			iExpressionType=oCopy.iExpressionType;
			iTimes=oCopy.iTimes;
			oPeriod=oCopy.oPeriod;
			oUpper=oCopy.oUpper;
			oLower=oCopy.oLower;
			iStep=oCopy.iStep;
			oEnumeratedElement=oCopy.oEnumeratedElement;
		
			return oCopy;
		}
		catch(CloneNotSupportedException e)
		{
			throw new InternalError(e.getMessage());
		}
		 
	}
}

// EOF
