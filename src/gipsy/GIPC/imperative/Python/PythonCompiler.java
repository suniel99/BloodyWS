package gipsy.GIPC.imperative.Python;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.imperative.ImperativeCompiler;

import gipsy.interfaces.AbstractSyntaxTree;
import gipsy.util.NotImplementedException;

/**
 * Main C Compiler.
 *
 * $Id: PythonCompiler.java,v 1.4 2005/06/15 04:28:53 mokhov Exp $
 * $Revision: 1.4 $
 * $Date: 2005/06/15 04:28:53 $
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 */
public class PythonCompiler
extends ImperativeCompiler
{
	public PythonCompiler()
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
