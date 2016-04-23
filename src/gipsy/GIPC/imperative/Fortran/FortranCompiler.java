package gipsy.GIPC.imperative.Fortran;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.imperative.ImperativeCompiler;

import gipsy.interfaces.AbstractSyntaxTree;
import gipsy.util.NotImplementedException;

/**
 * Main Fortran Compiler.
 *
 * $Id: FortranCompiler.java,v 1.2 2005/06/15 04:28:51 mokhov Exp $
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @version $Revision: 1.2 $
 */
public class FortranCompiler
extends ImperativeCompiler
{
	public FortranCompiler()
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
