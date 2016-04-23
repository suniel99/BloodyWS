 package gipsy.lang.context;

import gipsy.lang.GIPSYInteger;
import gipsy.lang.GIPSYType;
import marf.util.FreeVector;


/**
 * Ordered finite non-periodic tag set realization.
 * @author Xin Tong
 * @version $Id: OrderedFiniteNonPeriodicTagSet.java,v 1.3 2013/08/25 02:51:49 mokhov Exp $
 */
public class OrderedFiniteNonPeriodicTagSet
extends TagSet
implements IOrdered, IFinite, INonPeriodic
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7456711837080446270L;
	
	public static final int ENUMERATED = 1;
	public static final int UPPERLOWER_STEP = 2;
	
	public OrderedFiniteNonPeriodicTagSet()
	{
		super();
	}
	
	/**
	 * expressionType=ORDERED_FINITE_NONPERIODIC_ENUMERATED
	 * @param poEnumeratedElement
	 */
	public OrderedFiniteNonPeriodicTagSet(FreeVector<GIPSYType> poEnumeratedElement)
	{
		super.setExpressionType(ENUMERATED);
		super.setEnmuneratedElement(poEnumeratedElement);
	}
	
	/**
	 * expressionType=ORDERED_FINITE_NONPERIODIC_UPPERLOWER_STEP : if the step node is null, by default, the step should be 1
	 * @param poLower
	 * @param poUpper
	 * @param piStep
	 */
	public OrderedFiniteNonPeriodicTagSet(GIPSYType poLower, GIPSYType poUpper, int piStep)
	{
		super.setExpressionType(UPPERLOWER_STEP);

		super.setLower(poLower);
		super.setUpper(poUpper);
		super.setStep(piStep);
	}
	
	/* (non-Javadoc)
	 * @see gipsy.lang.context.TagSet#equals(java.lang.Object)
	 */
	public boolean equals(Object poOtherObject)
	{
		boolean bResult=false;
		
		if(this.getClass() != poOtherObject.getClass())
			return false;
		else
		{
			switch(this.getExpressionType())
			{
			case ENUMERATED:
			{
				if(((OrderedFiniteNonPeriodicTagSet)poOtherObject).getExpressionType()!=ENUMERATED)
					return false;
				else
				{
//					The sizes of the two collections of enumerated elements are not the same
					if(this.getEnmuneratedElement().size()!=((OrderedFiniteNonPeriodicTagSet)poOtherObject).getEnmuneratedElement().size())
					bResult= false;
				    //The tags are ordered, so the order of the tags have to be the same.
					else
					{
						bResult=this.getEnmuneratedElement().equals(((OrderedFiniteNonPeriodicTagSet)poOtherObject).getEnmuneratedElement());
				    }	
				}
				
				break;
			}

			case UPPERLOWER_STEP:
			{
				if(((OrderedFiniteNonPeriodicTagSet)poOtherObject).getExpressionType()!=UPPERLOWER_STEP)
					return false;
				else{
					bResult=(this.getUpper().equals(((OrderedFiniteNonPeriodicTagSet)poOtherObject).getUpper()) && this.getLower().equals(((OrderedFiniteNonPeriodicTagSet)poOtherObject).getLower())&& this.getStep()==((OrderedFiniteNonPeriodicTagSet)poOtherObject).getStep());
				}
				break;
			}
			
			}
			return bResult;
		}
	}
	
	/**
	 * The given poTag must be of the same type with either the enumerated
	 * tag set or the upper and lower boundaries.
	 * @see gipsy.lang.context.TagSet#isInTagSet(gipsy.lang.GIPSYType)
	 */
	public boolean isInTagSet(GIPSYType poTag)
	{
		boolean bResult=false;

		switch (this.getExpressionType())
		{
		case ENUMERATED:
		{
			for(int i = 0; i < getEnmuneratedElement().size(); i++)
			{
				if(this.getEnmuneratedElement().elementAt(i).equals(poTag))
				{
					bResult=true;
				    break;
				}
		    }
			break;
		}
		
//		In this situation, upper and lower boundaries and step should all be considered.
		case UPPERLOWER_STEP:
		{
			if(poTag.getClass() == getUpper().getClass() && poTag.getClass() == getLower().getClass())
			{
				//For now, only consider GIPSYInteger
				if(poTag instanceof GIPSYInteger)
				{
					if(((GIPSYInteger)poTag).getValue()>=((GIPSYInteger)this.getLower()).getValue()&& ((GIPSYInteger)poTag).getValue()<=((GIPSYInteger)this.getUpper()).getValue()&& ((GIPSYInteger)poTag).getValue()%this.getStep()==0)
						bResult=true;					
				}
				
				// TODO:
				/*****************************************************************************/
			    /*FUTURE POSSIBLE TYPES THAT HAS LESS THAN AND GREATER THAN FUNCTIONS DEFINED*/
				/*****************************************************************************/
			}
			break;
		
		}
		}
		return bResult;
	}
	
	
	/* (non-Javadoc)
	 * @see gipsy.lang.context.TagSet#getNext(gipsy.lang.GIPSYType)
	 */
	public GIPSYType getNext(GIPSYType poTag)
	{
		// null means eod
		GIPSYType oResult = null;

		switch(getExpressionType())
		{
			// The next element is the one after pTag in the enumerated tag set. First, it's necessary to call isInTagSet function
			case ENUMERATED:
			{
				if(isInTagSet(poTag))
				{
					int iCurrentIndex = getEnmuneratedElement().indexOf(poTag);
					
					if(iCurrentIndex != getEnmuneratedElement().size() - 1)
					{
						oResult = getEnmuneratedElement().elementAt(iCurrentIndex + 1);					
					}
					
					// reached the EOD
				}
				
				break;
			}
		
			case UPPERLOWER_STEP:
			{
				if(isInTagSet(poTag))
				{
					if(poTag instanceof GIPSYInteger)
					{	    
						if(poTag.equals(getUpper()) == false)
						{
		    				oResult = ((GIPSYInteger)poTag).add(new GIPSYInteger(getStep()));
						}
						
						// reached the EOD
				    }
	
					// TODO:
					/*****************************************************************************/
				    /*FUTURE POSSIBLE TYPES THAT HAS PLUS FUNCTIONS DEFINED*/
					/*****************************************************************************/
				}

				break;
			}

		}
		
		return oResult;
	}
}

// EOF
