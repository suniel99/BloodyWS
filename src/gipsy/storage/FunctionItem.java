package gipsy.storage;

import java.io.Serializable;
import java.lang.reflect.Method;

import gipsy.GIPC.intensional.SimpleNode;
import gipsy.lang.GIPSYType;


/**
 * Function item entry represents a bean holding all the information pertaining
 * to a function or a procedure in the tree.
 *  
 * @author Aihua Wu
 * @author Serguei Mokhov
 * 
 * @version $Id: FunctionItem.java,v 1.8 2007/11/30 15:20:02 mokhov Exp $
 * @since September 25, 2002, 12:12 PM
 */
public class FunctionItem 
implements Serializable
{
	/**
	 * Function identifier. 
	 */
	protected String strFunctionName;
	
	protected int iDimensions;

	/**
	 * Amount of parameters passed to the function. 
	 */
	protected int iParamCount;

	/**
	 * Root node in the AST corresponding to the entry
	 * point to this function.
	 */
	protected SimpleNode oFunctionEntry;
	
	/**
	 * Function return type.
	 */
	protected GIPSYType oReturnType;

	/**
	 * A collection of function parameter types. 
	 */
	protected GIPSYType[] oParameterTypes;
	
	/**
	 * For convenience if the encapsulated function
	 * happened to be a Java method we keep a reference.
	 */
	protected Method oMethod;

	/** 
	 * Creates new FunctionItem.
	 */
	public FunctionItem() 
	{
		this("", 0, 0, null);
	}
	
	public FunctionItem(Method poMethod) 
	{
		this("", 0, 0, null, poMethod);
	}

	public FunctionItem(String pstrFunctionName) 
	{
		this(pstrFunctionName, 0, 0, null);
	}

	public FunctionItem(String pstrFunctionName, Method poMethod) 
	{
		this(pstrFunctionName, 0, 0, null, poMethod);
	}

	public FunctionItem(String pstrFunctionName, SimpleNode poEntryNode) 
	{
		this(pstrFunctionName, 0, 0, poEntryNode);
	}

	public FunctionItem(String pstrFunctionName, SimpleNode poEntryNode, Method poMethod) 
	{
		this(pstrFunctionName, 0, 0, poEntryNode, poMethod);
	}

	/**
	 * @param pstrFunctionName
	 * @param piDimensions
	 * @param piParamCount
	 * @param poFunctionEntry
	 */
	public FunctionItem
	(
		String pstrFunctionName,
		int piDimensions,
		int piParamCount,
		SimpleNode poFunctionEntry
	)
	{
		this(pstrFunctionName, piDimensions, piParamCount, poFunctionEntry, null);
	}

	/**
	 * @param pstrFunctionName
	 * @param piDimensions
	 * @param piParamCount
	 * @param poFunctionEntry
	 * @param poMethod
	 */
	public FunctionItem
	(
		String pstrFunctionName,
		int piDimensions,
		int piParamCount,
		SimpleNode poFunctionEntry,
		Method poMethod
	)
	{
		this.strFunctionName = pstrFunctionName;
		this.iDimensions     = piDimensions;
		this.iParamCount     = piParamCount;
		this.oFunctionEntry  = poFunctionEntry;
		this.oMethod         = poMethod;
	}
	
	public int getDimensions()
	{
		return this.iDimensions;
	}

	public int getParamCount()
	{
		return this.iParamCount;
	}
	
	public SimpleNode getFunctionEntry()
	{
		return this.oFunctionEntry;
	}

	/**
	 * Allows querying for TODO.
	 * @return returns the value oMethod field.
	 */
	public Method getMethod()
	{
		return this.oMethod;
	}
}

// EOF
