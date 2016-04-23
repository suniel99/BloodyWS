package gipsy.GIPC.imperative.Cpp;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.imperative.ImperativeCompiler;

import gipsy.interfaces.AbstractSyntaxTree;
import gipsy.util.NotImplementedException;

/**
 * Main C++ Compiler.
 *
 * $Id: CPPCompiler.java,v 1.5 2005/06/15 04:28:50 mokhov Exp $
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @version $Revision: 1.5 $
 */
public class CPPCompiler
extends ImperativeCompiler
{
	public CPPCompiler()
	{
	}

	public void init()
	throws GIPCException
	{
		throw new NotImplementedException(this, "init()");
	}

	public AbstractSyntaxTree parse()
	throws GIPCException
	{
		throw new NotImplementedException(this, "parse()");
	}
}

// EOF
