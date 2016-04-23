package gipsy.lang;


/**
 * <p>Represents an identifier value and type.</p>
 * 
 * @author Serguei Mokhov
 * @version $Id: GIPSYInteger.java,v 1.10 2013/08/25 02:51:46 mokhov Exp $
 * @since GIPSY Type System Inception
 */
public class GIPSYInteger
extends GIPSYType
implements IArithmeticOperatorsProvider
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3246778977169250615L;
	
	/**
	 * Encapsulated Java's longest integer value. 
	 */
//	protected Integer oIntegerValue;
	protected Long oIntegerValue;

	/**
	 * Constructs a integer initially set to 0. 
	 */
	public GIPSYInteger()
	{
		this(0);
	}

	/**
	 * Constructs an integer given a primitive Java int.
	 * @param piValue the int value
	 */
	public GIPSYInteger(int piValue)
	{
		this(new Integer(piValue));
	}

	/**
	 * Constructs an integer given a Java Integer object.
	 * Sets the lexeme to "int".
	 *
	 * @param piValue the integer value
	 * @see TYPE_INT
	 * 
	 * @throws NullPointerException if the integer object is null
	 */
	public GIPSYInteger(Integer poIntegerValue)
	{
		this(new Long(poIntegerValue));
	}

	public GIPSYInteger(Long poIntegerValue)
	{
		this.strLexeme = "int";
		this.iType = TYPE_INT;
		
		if(poIntegerValue == null)
		{
			throw new NullPointerException("null GIPSYInteger parameter");
		}
		
		this.oIntegerValue = poIntegerValue;
	}

	/**
	 * @see gipsy.lang.GIPSYType#getEnclosedTypeOject()
	 */
	public Object getEnclosedTypeOject()
	{
		return this.oIntegerValue;
	}

	/**
	 * Returns contained value.
	 * @return the encapsulated integer
	 */
//	public Integer getValue()
	public Long getValue()
	{
		return this.oIntegerValue;
	}

	// -- arithmetic API

	public GIPSYType add(GIPSYType poLHS, GIPSYType poRHS)
	{
		return add((GIPSYInteger)poLHS, (GIPSYInteger)poRHS);
	}

	public GIPSYType add(GIPSYType poRHS)
	{
		return add((GIPSYInteger)poRHS);
	}

	public GIPSYType subtract(GIPSYType poLHS, GIPSYType poRHS)
	{
		return subtract((GIPSYInteger)poLHS, (GIPSYInteger)poRHS);
	}

	public GIPSYType subtract(GIPSYType poRHS)
	{
		return subtract((GIPSYInteger)poRHS);
	}

	public GIPSYType multiply(GIPSYType poLHS, GIPSYType poRHS)
	{
		return multiply((GIPSYInteger)poLHS, (GIPSYInteger)poRHS);
	}

	public GIPSYType multiply(GIPSYType poRHS)
	{
		return multiply((GIPSYInteger)poRHS);
	}

	public GIPSYType divide(GIPSYType poLHS, GIPSYType poRHS)
	{
		return divide((GIPSYInteger)poLHS, (GIPSYInteger)poRHS);
	}

	public GIPSYType divide(GIPSYType poRHS)
	{
		return divide((GIPSYInteger)poRHS);
	}
	
	public GIPSYType mod(GIPSYType poLHS, GIPSYType poRHS)
	{
		return mod((GIPSYInteger)poLHS, (GIPSYInteger)poRHS);
	}

	public GIPSYType mod(GIPSYType poRHS)
	{
		return mod((GIPSYInteger)poRHS);
	}

	// -- type-specific API
	
	public GIPSYInteger add(final GIPSYInteger poRHS)
	{
		return add(this, poRHS);
	}

	/**
	 * i = i + i
	 * @param poLHS i
	 * @param poRHS i
	 * @return
	 */
	public static final GIPSYInteger add(final GIPSYInteger poLHS, final GIPSYInteger poRHS)
	{
		return new GIPSYInteger(poLHS.oIntegerValue.intValue() + poRHS.oIntegerValue.intValue());
	}

	public static final GIPSYInteger add(final GIPSYInteger poLHS, final GIPSYCharacter poRHS)
	{
		return new GIPSYInteger(poLHS.oIntegerValue.intValue() + poRHS.oCharacterValue.charValue());
	}
	
	public static final GIPSYFloat add(final GIPSYInteger poLHS, final GIPSYFloat poRHS)
	{
		return new GIPSYFloat(poLHS.oIntegerValue.intValue() + poRHS.oFloatValue.floatValue());
	}

	public static final GIPSYDouble add(final GIPSYInteger poLHS, final GIPSYDouble poRHS)
	{
		return new GIPSYDouble(poLHS.oIntegerValue.intValue() + poRHS.oDoubleValue.doubleValue());
	}

	
	public GIPSYInteger subtract(final GIPSYInteger poRHS)
	{
		return subtract(this, poRHS);
	}

	public static final GIPSYInteger subtract(final GIPSYInteger poLHS, final GIPSYInteger poRHS)
	{
		return new GIPSYInteger(poLHS.oIntegerValue.intValue() - poRHS.oIntegerValue.intValue());
	}

	public static final GIPSYInteger subtract(final GIPSYInteger poLHS, final GIPSYCharacter poRHS)
	{
		return new GIPSYInteger(poLHS.oIntegerValue.intValue() - poRHS.oCharacterValue.charValue());
	}
	
	public static final GIPSYFloat subtract(final GIPSYInteger poLHS, final GIPSYFloat poRHS)
	{
		return new GIPSYFloat(poLHS.oIntegerValue.intValue() - poRHS.oFloatValue.floatValue());
	}
	
	public static final GIPSYDouble subtract(final GIPSYInteger poLHS, final GIPSYDouble poRHS)
	{
		return new GIPSYDouble(poLHS.oIntegerValue.intValue() - poRHS.oDoubleValue.doubleValue());
	}

	
	public GIPSYInteger multiply(final GIPSYInteger poRHS)
	{
		return multiply(this, poRHS);
	}

	public static final GIPSYInteger multiply(final GIPSYInteger poLHS, final GIPSYInteger poRHS)
	{
		return new GIPSYInteger(poLHS.oIntegerValue.intValue() * poRHS.oIntegerValue.intValue());
	}

	public GIPSYInteger divide(final GIPSYInteger poRHS)
	{
		return divide(this, poRHS);
	}

	public static final GIPSYInteger divide(final GIPSYInteger poLHS, final GIPSYInteger poRHS)
	{
		return new GIPSYInteger(poLHS.oIntegerValue.intValue() / poRHS.oIntegerValue.intValue());
	}

	public static final GIPSYInteger divide(final GIPSYInteger poLHS, final GIPSYType poRHS)
	{
		if(poRHS instanceof GIPSYInteger)
		{
			return new GIPSYInteger(poLHS.oIntegerValue.intValue() / ((GIPSYInteger)poRHS).oIntegerValue.intValue());
		}
		else if(poRHS instanceof GIPSYCharacter)
		{
			return new GIPSYInteger(poLHS.oIntegerValue.intValue() / ((GIPSYCharacter)poRHS).oCharacterValue.charValue());
		}
		
		int a = 1;
//		boolean b = false; // NOPE
		char b = 'b'; // YEP
//		float b = 12.3f; // NOPE
//		double b = 12.3; // NOPE
		int c = a / b;
		
		throw new IllegalArgumentException("Right-hand side of the / operator must be either integer or character.");
	}
	
	public GIPSYInteger mod(final GIPSYInteger poRHS)
	{
		return mod(this, poRHS);
	}

	public static final GIPSYInteger mod(final GIPSYInteger poLHS, final GIPSYInteger poRHS)
	{
		return new GIPSYInteger(poLHS.oIntegerValue.intValue() % poRHS.oIntegerValue.intValue());
	}

	/* (non-Javadoc)
	 * @see gipsy.lang.GIPSYType#toString()
	 */
	public String toString()
	{
		return this.strLexeme + " : " + this.oIntegerValue;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @since Xin Tong
	 */
	public boolean equals(Object poOtherObject)
	{
		if(getClass() != poOtherObject.getClass())
		{
			return false;
		}
		else
		{
			return this.oIntegerValue.equals(((GIPSYInteger)poOtherObject).oIntegerValue);
		}
	}
}

// EOF
