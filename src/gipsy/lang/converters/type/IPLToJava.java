package gipsy.lang.converters.type;

import gipsy.lang.GIPSYDouble;
import gipsy.lang.GIPSYFloat;
import gipsy.lang.GIPSYInteger;
import gipsy.lang.GIPSYType;
import gipsy.util.GIPSYRuntimeException;


/**
 * Implements a type conversion table from intensional programming languages
 * (IPLs), e.g. various Lucid dialects into Java.
 * 
 * @author Serguei Mokhov
 * @since November 9, 2008
 * @version $Id: IPLToJava.java,v 1.3 2009/03/06 07:38:41 mokhov Exp $
 */
public class IPLToJava
{
	/**
	 * Converts any numerical GIPSYType to Integer. There can be
	 * a precision loss.
	 * 
	 * @param poTypeValue GIPSY type value to convert
	 * @return Integer
	 */
	public static Integer convertToInteger(GIPSYType poTypeValue)
	{
		return convertToNumber(poTypeValue).intValue();
	}
	
	/**
	 * More of a helper method to get a numerical value out of
	 * a GIPSY type.
	 * 
	 * @param poTypeValue
	 * @return
	 * 
	 * @throws GIPSYRuntimeException if poTypeValue is not a number
	 */
	public static Number convertToNumber(GIPSYType poTypeValue)
	{
		switch(poTypeValue.getTypeEnumeration())
		{
			case GIPSYType.TYPE_INT:
			{
				Object oValue = poTypeValue.getEnclosedTypeOject();
				
				if(oValue instanceof Integer)
				{
					return (Integer)oValue;
				}

				if(oValue instanceof Long)
				{
					return ((Long)oValue).intValue();
				}

				throw new GIPSYRuntimeException("Expected an integer, got " + oValue);
				//XXX: return (Integer)poTypeValue.getEnclosedTypeOject();
			}
			
			case GIPSYType.TYPE_FLOAT:
			{
				return (Float)poTypeValue.getEnclosedTypeOject();
			}
			
			case GIPSYType.TYPE_DOUBLE:
			{
				return (Double)poTypeValue.getEnclosedTypeOject();
			}
			
			default:
			{
				throw new GIPSYRuntimeException("Not a number: " +  poTypeValue);
			}
		}
	}

	/**
	 * Converts GIPSYInteger to int.
	 * @param poInteger
	 * @return
	 */
	public static int convert(GIPSYInteger poInteger)
	{
		return poInteger.getValue().intValue();
	}

	public static float convert(GIPSYFloat poFloat)
	{
		return poFloat.getValue().floatValue();
	}

	public static double convert(GIPSYDouble poDouble)
	{
		return poDouble.getValue().doubleValue();
	}
}

// EOF
