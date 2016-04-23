package gipsy.lang.comparators;

import gipsy.lang.GIPSYDouble;
import gipsy.lang.GIPSYFloat;
import gipsy.lang.GIPSYInteger;
import gipsy.lang.GIPSYType;
import gipsy.lang.IArithmeticOperatorsProvider;
import gipsy.util.GIPSYRuntimeException;
import marf.Stats.StatisticalObject;
import marf.util.SortComparator;


/**
 * <p>Compares numeric kind types for sorting, etc..</p>
 *
 * @author Serguei Mokhov
 * @version $Id: NumericComparator.java,v 1.2 2013/08/25 02:54:39 mokhov Exp $
 * @since 1.0.0
 */
public class NumericComparator
extends SortComparator
{
    /**
	 * For serialization versioning.
	 * When adding new members or make other structural
	 * changes regenerate this number with the
	 * <code>serialver</code> tool that comes with JDK.
	 */
	private static final long serialVersionUID = 1366398005638869154L;

	/**
	 * Constructs a frequency comparator with the specified sort mode.
	 * @param piSortMode ASCENDING or DESCENDING
	 */
	public NumericComparator(int piSortMode)
	{
		super(piSortMode);
	}

	/**
	 * Implementation of the Comparator interface for the StatisticalObjects.
	 * To decide on inequality of the <code>StatisticalObject</code> objects we
	 * compare their frequencies only.
	 *
	 * @param poLHSNumber first object to compare
	 * @param poRHSNumber second object to compare
	 * @return 0 of the frequencies are equal. Depending on the sort mode; a negative
	 * value may mean poLHSNumber &lt; poRHSNumber if ASCENDING; or otherwise if DESCENDING
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 * @see StatisticalObject
	 */
	public int compare(Object poLHSNumber, Object poRHSNumber)
	{
		GIPSYType oLHSNumber = (GIPSYType)poLHSNumber;
		GIPSYType oRHSNumber = (GIPSYType)poRHSNumber;
		
		if
		(
			oLHSNumber instanceof IArithmeticOperatorsProvider
			&& oRHSNumber instanceof IArithmeticOperatorsProvider
		)
		{
			//IArithmeticOperatorsProvider oLHSNumberType = (IArithmeticOperatorsProvider)poLHSNumber;
			//IArithmeticOperatorsProvider oRHSNumberType = (IArithmeticOperatorsProvider)poRHSNumber;
			
			double dLHSValue = 0;
			double dRHSValue = 0;
			
			// LHS
			
			if(oLHSNumber instanceof GIPSYInteger)
			{
				dLHSValue = ((GIPSYInteger)oLHSNumber).getValue().doubleValue();  
			}

			if(oLHSNumber instanceof GIPSYFloat)
			{
				dLHSValue = ((GIPSYFloat)oLHSNumber).getValue().doubleValue();  
			}

			if(oLHSNumber instanceof GIPSYDouble)
			{
				dLHSValue = ((GIPSYDouble)oLHSNumber).getValue().doubleValue();  
			}
			
			// RHS
			
			if(oRHSNumber instanceof GIPSYInteger)
			{
				dRHSValue = ((GIPSYInteger)oRHSNumber).getValue().doubleValue();  
			}

			if(oRHSNumber instanceof GIPSYFloat)
			{
				dRHSValue = ((GIPSYFloat)oRHSNumber).getValue().doubleValue();  
			}

			if(oRHSNumber instanceof GIPSYDouble)
			{
				dRHSValue = ((GIPSYDouble)oRHSNumber).getValue().doubleValue();  
			}
			
			switch(this.iSortMode)
			{
				case DESCENDING:
				{
					if(dLHSValue < dRHSValue)
					{
						return 1;
					}

					if(dLHSValue > dRHSValue)
					{
						return -1;
					}
					
					return 0;
				}
	
				case ASCENDING:
				default:
				{
					if(dLHSValue < dRHSValue)
					{
						return -1;
					}

					if(dLHSValue > dRHSValue)
					{
						return 1;
					}
					
					return 0;
				}
			}
		}
		
		throw new GIPSYRuntimeException
		(
			"Run-time type mismatch when comparing: "
			+ oLHSNumber.getClass()
			+ " and " + oRHSNumber.getClass()
		);
	}

	/**
	 * Returns source code revision information.
	 * @return revision string
	 */
	public static String getMARFSourceCodeRevision()
	{
		return "$Revision: 1.2 $";
	}
}

// EOF
