package gipsy.lang;

import gipsy.util.GIPSYRuntimeException;


/**
 * <p>Generic implementation of the arithmetic operators is delegated to here.</p>
 * 
 * @author Serguei Mokhov
 * @version $Id: GenericArithmeticOperatorsDelegate.java,v 1.2 2008/11/06 00:34:31 mokhov Exp $
 * @since GIPSY Type System Inception
 */
public class GenericArithmeticOperatorsDelegate
implements IArithmeticOperatorsProvider
{
	public GenericArithmeticOperatorsDelegate()
	{
	}

	// -- arithmetic API

	/* (non-Javadoc)
	 * @see gipsy.lang.IArithmeticOperatorsProvider#add(gipsy.lang.GIPSYType, gipsy.lang.GIPSYType)
	 */
	public GIPSYType add(GIPSYType poLHS, GIPSYType poRHS)
	{
		try
		{
			if(poLHS instanceof GIPSYInteger)
			{
				if(poRHS instanceof GIPSYInteger)
				{
					//return ((GIPSYInteger)poLHS).add(poLHS, (GIPSYInteger)poRHS);
					return GIPSYInteger.add((GIPSYInteger)poLHS, (GIPSYInteger)poRHS);
				}

				if(poRHS instanceof GIPSYCharacter)
				{
					//return ((GIPSYInteger)poRHS).add(new GIPSYInteger((int)((GIPSYCharacter)poLHS).getValue().charValue()));
					return GIPSYInteger.add((GIPSYInteger)poLHS, (GIPSYCharacter)poLHS);
				}

				if(poRHS instanceof GIPSYFloat)
				{
					return GIPSYInteger.add((GIPSYInteger)poLHS, (GIPSYFloat)poRHS);
				}

				if(poRHS instanceof GIPSYDouble)
				{
					return GIPSYInteger.add((GIPSYInteger)poLHS, (GIPSYDouble)poRHS);
				}
			}
			
			
			if(poLHS instanceof GIPSYInteger && poRHS instanceof GIPSYInteger)
			{
				return ((GIPSYInteger)poLHS).add(poLHS, (GIPSYInteger)poRHS);
			}

			if(poLHS instanceof GIPSYInteger && poRHS instanceof GIPSYCharacter)
			{
				return ((GIPSYInteger)poRHS).add(new GIPSYInteger((int)((GIPSYCharacter)poLHS).getValue().charValue()));
			}

			if(poLHS instanceof GIPSYCharacter && poRHS instanceof GIPSYCharacter)
			{
				return ((GIPSYInteger)poLHS).add(new GIPSYInteger((int)((GIPSYCharacter)poRHS).getValue().charValue()));
			}

			if(poLHS instanceof GIPSYDouble || poRHS instanceof GIPSYDouble)
			{
				//return add((GenericArithmeticOperatorsDelegate)poLHS, (GenericArithmeticOperatorsDelegate)poRHS);
				return new GIPSYDouble();
			}

			throw new ClassCastException();
		}
		catch(ClassCastException e)
		{
			throw new GIPSYRuntimeException("Unsupported arithmetic operator + for arguments [" + poLHS + "], [" + poRHS + "]");
		}
	}

	
	public GIPSYType subtract(GIPSYType poLHS, GIPSYType poRHS)
	{
		try
		{
			if(poLHS instanceof GIPSYInteger)
			{
				if(poRHS instanceof GIPSYInteger)
				{
					//return ((GIPSYInteger)poLHS).subtract(poLHS, (GIPSYInteger)poRHS);
					return GIPSYInteger.subtract((GIPSYInteger)poLHS, (GIPSYInteger)poRHS);
				}

				if(poRHS instanceof GIPSYCharacter)
				{
					//return ((GIPSYInteger)poRHS).subtract(new GIPSYInteger((int)((GIPSYCharacter)poLHS).getValue().charValue()));
					return GIPSYInteger.subtract((GIPSYInteger)poLHS, (GIPSYCharacter)poLHS);
				}

				if(poRHS instanceof GIPSYFloat)
				{
					return GIPSYInteger.subtract((GIPSYInteger)poLHS, (GIPSYFloat)poRHS);
				}

				if(poRHS instanceof GIPSYDouble)
				{
					return GIPSYInteger.subtract((GIPSYInteger)poLHS, (GIPSYDouble)poRHS);
				}
			}
			
			
			if(poLHS instanceof GIPSYInteger && poRHS instanceof GIPSYInteger)
			{
				return ((GIPSYInteger)poLHS).subtract(poLHS, (GIPSYInteger)poRHS);
			}

			if(poLHS instanceof GIPSYInteger && poRHS instanceof GIPSYCharacter)
			{
				return ((GIPSYInteger)poRHS).subtract(new GIPSYInteger((int)((GIPSYCharacter)poLHS).getValue().charValue()));
			}

			if(poLHS instanceof GIPSYCharacter && poRHS instanceof GIPSYCharacter)
			{
				return ((GIPSYInteger)poLHS).subtract(new GIPSYInteger((int)((GIPSYCharacter)poRHS).getValue().charValue()));
			}

			if(poLHS instanceof GIPSYDouble || poRHS instanceof GIPSYDouble)
			{
				//return subtract((GenericArithmeticOperatorsDelegate)poLHS, (GenericArithmeticOperatorsDelegate)poRHS);
				return new GIPSYDouble();
			}

			throw new ClassCastException();
		}
		catch(ClassCastException e)
		{
			throw new GIPSYRuntimeException("Unsupported arithmetic operator - for arguments [" + poLHS + "], [" + poRHS + "]");
		}
	}	
	/*
	public GIPSYType add(GIPSYType poRHS)
	{
		return add((GenericArithmeticOperatorsDelegate)poRHS);
	}
	public GIPSYType subtract(GIPSYType poLHS, GIPSYType poRHS)
	{
		return subtract((GenericArithmeticOperatorsDelegate)poLHS, (GenericArithmeticOperatorsDelegate)poRHS);
	}

	public GIPSYType subtract(GIPSYType poRHS)
	{
		return subtract((GenericArithmeticOperatorsDelegate)poRHS);
	}

	public GIPSYType multiply(GIPSYType poLHS, GIPSYType poRHS)
	{
		return multiply((GenericArithmeticOperatorsDelegate)poLHS, (GenericArithmeticOperatorsDelegate)poRHS);
	}

	public GIPSYType multiply(GIPSYType poRHS)
	{
		return multiply((GenericArithmeticOperatorsDelegate)poRHS);
	}

	public GIPSYType divide(GIPSYType poLHS, GIPSYType poRHS)
	{
		return divide((GenericArithmeticOperatorsDelegate)poLHS, (GenericArithmeticOperatorsDelegate)poRHS);
	}

	public GIPSYType divide(GIPSYType poRHS)
	{
		return divide((GenericArithmeticOperatorsDelegate)poRHS);
	}
	
	public GIPSYType mod(GIPSYType poLHS, GIPSYType poRHS)
	{
		return mod((GenericArithmeticOperatorsDelegate)poLHS, (GenericArithmeticOperatorsDelegate)poRHS);
	}

	public GIPSYType mod(GIPSYType poRHS)
	{
		return mod((GenericArithmeticOperatorsDelegate)poRHS);
	}

	// -- type-specific API
	
	public GenericArithmeticOperatorsDelegate add(final GenericArithmeticOperatorsDelegate poRHS)
	{
		return add(this, poRHS);
	}

	public static final GenericArithmeticOperatorsDelegate add(final GenericArithmeticOperatorsDelegate poLHS, final GenericArithmeticOperatorsDelegate poRHS)
	{
		return new GenericArithmeticOperatorsDelegate(poLHS.oIntegerValue.intValue() + poRHS.oIntegerValue.intValue());
	}

	public GenericArithmeticOperatorsDelegate subtract(final GenericArithmeticOperatorsDelegate poRHS)
	{
		return subtract(this, poRHS);
	}

	public static final GenericArithmeticOperatorsDelegate subtract(final GenericArithmeticOperatorsDelegate poLHS, final GenericArithmeticOperatorsDelegate poRHS)
	{
		return new GenericArithmeticOperatorsDelegate(poLHS.oIntegerValue.intValue() - poRHS.oIntegerValue.intValue());
	}

	public GenericArithmeticOperatorsDelegate multiply(final GenericArithmeticOperatorsDelegate poRHS)
	{
		return multiply(this, poRHS);
	}

	public static final GenericArithmeticOperatorsDelegate multiply(final GenericArithmeticOperatorsDelegate poLHS, final GenericArithmeticOperatorsDelegate poRHS)
	{
		return new GenericArithmeticOperatorsDelegate(poLHS.oIntegerValue.intValue() * poRHS.oIntegerValue.intValue());
	}

	public GenericArithmeticOperatorsDelegate divide(final GenericArithmeticOperatorsDelegate poRHS)
	{
		return divide(this, poRHS);
	}

	public static final GenericArithmeticOperatorsDelegate divide(final GenericArithmeticOperatorsDelegate poLHS, final GenericArithmeticOperatorsDelegate poRHS)
	{
		return new GenericArithmeticOperatorsDelegate(poLHS.oIntegerValue.intValue() / poRHS.oIntegerValue.intValue());
	}

	public static final GenericArithmeticOperatorsDelegate divide(final GenericArithmeticOperatorsDelegate poLHS, final GIPSYType poRHS)
	{
		if(poRHS instanceof GenericArithmeticOperatorsDelegate)
		{
			return new GenericArithmeticOperatorsDelegate(poLHS.oIntegerValue.intValue() / ((GenericArithmeticOperatorsDelegate)poRHS).oIntegerValue.intValue());
		}
		else if(poRHS instanceof GIPSYCharacter)
		{
			return new GenericArithmeticOperatorsDelegate(poLHS.oIntegerValue.intValue() / ((GIPSYCharacter)poRHS).oCharacterValue.charValue());
		}
		
		int a = 1;
//		boolean b = false; // NOPE
		char b = 'b'; // YEP
//		float b = 12.3f; // NOPE
//		double b = 12.3; // NOPE
		int c = a / b;
		
		throw new IllegalArgumentException("Right-hand side of the / operator must be either integer or character.");
	}
	
	public GenericArithmeticOperatorsDelegate mod(final GenericArithmeticOperatorsDelegate poRHS)
	{
		return mod(this, poRHS);
	}

	public static final GenericArithmeticOperatorsDelegate mod(final GenericArithmeticOperatorsDelegate poLHS, final GenericArithmeticOperatorsDelegate poRHS)
	{
		return new GenericArithmeticOperatorsDelegate(poLHS.oIntegerValue.intValue() % poRHS.oIntegerValue.intValue());
	}

	public String toString()
	{
		return this.strLexeme + " : " + this.oIntegerValue;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
}

// EOF
