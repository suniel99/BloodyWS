package gipsy.lang;

import java.lang.reflect.Array;

/**
 * Array is an object of elements of the same type.
 *
 * @author Serguei Mokhov
 * @since GIPSY Type System Inception, 1.0.0, 2005
 * @version $Id: GIPSYArray.java,v 1.9 2013/08/12 12:03:55 mokhov Exp $
 */
public class GIPSYArray
extends GIPSYObject
{
	/**
	 * For serialization versioning. 
	 */
	private static final long serialVersionUID = 4552065238617149150L;
	
	/**
	 * Base type of all elements in the array.
	 * The members and value are inherited
	 * from GIPSYObject.
	 * @see GIPSYObject
	 */
	protected GIPSYType oBaseType = null;

	/**
	 * 
	 */
	public GIPSYArray()
	{
		this(Object.class, 0);
	}
	
	/**
	 * @param poArrayClassType
	 * @param piLength
	 */
	public GIPSYArray(Class<?> poArrayClassType, int piLength)
	{
		this(Array.newInstance(poArrayClassType, piLength));
	}

	/**
	 * @param poArrayClassType
	 * @param paiDimensions
	 */
	public GIPSYArray(Class<?> poArrayClassType, int[] paiDimensions)
	{
		this(Array.newInstance(poArrayClassType, paiDimensions));
	}

	/**
	 * @param poArrayValue
	 */
	public GIPSYArray(Object poArrayValue)
	{
		this.strLexeme = "array";
		this.iType = TYPE_ARRAY;
		
		if(poArrayValue == null)
			throw new NullPointerException("null GIPSYArray parameter");
		
		this.oObjectValue = poArrayValue;
	}

	public int length()
	{
		return this.aoMembers.length;
	}

	public GIPSYType getBaseType()
	{
		return this.oBaseType;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return this.strLexeme + " : " + this.oObjectValue;
	}
}

// EOF
