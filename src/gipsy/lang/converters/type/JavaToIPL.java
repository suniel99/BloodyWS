package gipsy.lang.converters.type;

import gipsy.lang.GIPSYBoolean;
import gipsy.lang.GIPSYCharacter;
import gipsy.lang.GIPSYDouble;
import gipsy.lang.GIPSYFloat;
import gipsy.lang.GIPSYFunction;
import gipsy.lang.GIPSYInteger;
import gipsy.lang.GIPSYObject;
import gipsy.lang.GIPSYString;

import java.lang.reflect.Method;


/**
 * Implements a type conversion table from Java to 
 * intensional programming languages (IPLs), e.g. various
 * Lucid dialects.
 * 
 * @author Serguei Mokhov
 * @since November 9, 2008
 * @version $Id: JavaToIPL.java,v 1.2 2008/11/09 23:39:47 mokhov Exp $
 */
public class JavaToIPL
{
	public static GIPSYInteger convert(Integer poInteger)
	{
		return new GIPSYInteger(poInteger);
	}
	
	public static GIPSYFloat convert(Float poFloat)
	{
		return new GIPSYFloat(poFloat);
	}

	public static GIPSYDouble convert(Double poDouble)
	{
		return new GIPSYDouble(poDouble);
	}

	public static GIPSYCharacter convert(Character poCharacter)
	{
		return new GIPSYCharacter(poCharacter);
	}

	public static GIPSYString convert(String poString)
	{
		return new GIPSYString(poString);
	}

	public static GIPSYObject convert(Object poObject)
	{
		return new GIPSYObject(poObject);
	}

	public static GIPSYFunction convert(Method poFunction)
	{
		return new GIPSYFunction(poFunction);
	}

	public static GIPSYBoolean convert(Boolean poBoolean)
	{
		return new GIPSYBoolean(poBoolean);
	}
}

// EOF
