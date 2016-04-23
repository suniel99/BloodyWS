package gipsy.lang;

import gipsy.storage.FunctionItem;


/**
 * A function type operator.
 * 
 * @author Serguei Mokhov
 * @version $Id: GIPSYOperator.java,v 1.3 2009/08/25 18:44:01 mokhov Exp $
 * @since 1.0.0
 */
public class GIPSYOperator
extends GIPSYFunction
{
	public GIPSYOperator()
	{
		super();
	}
	
	public GIPSYOperator(FunctionItem poFunctionValue)
	{
		super(poFunctionValue);
		this.iType = TYPE_OPERATOR;
		this.iFunctionState = FUNCTION_STATE_IMMUTABLE;
		this.iFunctionType = FUNCTION_TYPE_FUNCTIONAL;
	}
}

// EOF
