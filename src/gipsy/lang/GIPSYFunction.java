package gipsy.lang;

import gipsy.GEE.Executor;
import gipsy.GEE.GEEException;
import gipsy.storage.FunctionItem;
import gipsy.util.GIPSYRuntimeException;

import java.lang.reflect.Method;


/**
 * Represents a function type.
 *
 * @author Serguei Mokhov
 * @version $Id: GIPSYFunction.java,v 1.4 2009/08/25 18:44:01 mokhov Exp $
 * @since 1.0.0
 */
public class GIPSYFunction
extends GIPSYType
{
	public static final int FUNCTION_TYPE_FUNCTIONAL = 1;
	public static final int FUNCTION_TYPE_ST = 2;

	public static final int FUNCTION_STATE_IMMUTABLE = 3;
	public static final int FUNCTION_STATE_VOLATILE = 4;

	/**
	 * The function item in the tree that may be either
	 * intensional or procedural.
	 */
	protected FunctionItem oFunctionValue = null;

	protected Method oFunctionMethod = null;
	
	protected int iFunctionState = FUNCTION_STATE_IMMUTABLE;
	protected int iFunctionType = FUNCTION_TYPE_FUNCTIONAL;
	
	public GIPSYFunction()
	{
		this(new FunctionItem());
	}
	
	public GIPSYFunction(FunctionItem poFunctionValue)
	{
		if(poFunctionValue == null)
		{
			throw new NullPointerException("null GIPSYFunction parameter");
		}

		this.strLexeme = poFunctionValue.getFunctionEntry().getImage();
		this.iType = TYPE_FUNCTION;

		this.oFunctionValue = poFunctionValue;
	}

	public GIPSYFunction(Method poMethodValue)
	{
		if(poMethodValue == null)
		{
			throw new NullPointerException("null GIPSYFunction parameter");
		}

		this.strLexeme = poMethodValue.getName();
		this.iType = FUNCTION_TYPE_ST;

		this.oFunctionMethod = poMethodValue;
	}

	public Object getEnclosedTypeOject()
	{
		return this.oFunctionValue;
	}

//	public FunctionItem getValue()
	public Object getValue()
	{
		switch(this.iType)
		{
			case TYPE_FUNCTION:
			{
				return this.oFunctionValue;
			}

			case FUNCTION_TYPE_ST:
			{
				return this.oFunctionMethod;
			}
			
			default:
			{
				return null;
			}
		}
	}
	
	public String toString()
	{
		return this.strLexeme + " : " + this.oFunctionValue;
	}

	/**
	 * Invokes the underlying function.
	 * @return
	 * @throws GEEException
	 * @throws GIPSYRuntimeException
	 */
	public GIPSYType eval()
	throws GEEException
	{
		switch(this.iFunctionType)
		{
			case FUNCTION_TYPE_FUNCTIONAL:
			{
				// XXX: review
				return new Executor().eval
				(
					this.oFunctionValue.getFunctionEntry(),
					new GIPSYContext[] { new GIPSYContext() },
					0
				);
			}
			
			case FUNCTION_TYPE_ST:
			{
				try
				{
					// XXX: fix params
					// XXX: fix return type per matching table
					return new GIPSYObject(this.oFunctionValue.getMethod().invoke(null, (Object[])null));
				}
				catch(Exception e)
				{
					throw new GEEException(e);
				}
			}
			
			default:
			{
				throw new GIPSYRuntimeException("Function type: " + this.iFunctionType + " is unsupported.");
			}
		}
	}
	
	/**
	 * @return Returns the iFunctionState.
	 */
	public int getFunctionState()
	{
		return this.iFunctionState;
	}
	
	/**
	 * @param piFunctionState The iFunctionState to set.
	 */
	public void setFunctionState(int piFunctionState)
	{
		this.iFunctionState = piFunctionState;
	}
	
	/**
	 * @return Returns the iFunctionType.
	 */
	public int getFunctionType()
	{
		return this.iFunctionType;
	}
	
	/**
	 * @param piFunctionType The iFunctionType to set.
	 */
	public void setFunctionType(int piFunctionType)
	{
		this.iFunctionType = piFunctionType;
	}
}

// EOF
