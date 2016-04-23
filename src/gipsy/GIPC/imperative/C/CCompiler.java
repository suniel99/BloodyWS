package gipsy.GIPC.imperative.C;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.imperative.ImperativeCompiler;

import gipsy.interfaces.AbstractSyntaxTree;
import gipsy.util.NotImplementedException;

/**
 * Main C Compiler.
 *
 * $Id: CCompiler.java,v 1.4 2005/06/15 04:28:49 mokhov Exp $
 * $Revision: 1.4 $
 * $Date: 2005/06/15 04:28:49 $
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 */
public class CCompiler
extends ImperativeCompiler
{
	public CCompiler()
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
