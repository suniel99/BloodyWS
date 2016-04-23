package gipsy.GIPC.imperative;

import gipsy.GIPC.ICompiler;

import gipsy.interfaces.ISequentialThread;
import gipsy.interfaces.ICommunicationProcedure;
import gipsy.interfaces.ICommunicationProceduresEnum;

/**
 * All imperative compilers should implement this interface
 * if they cannot extend the ImperativeCompiler class.
 *
 * $Id: IImperativeCompiler.java,v 1.8 2005/06/15 04:28:48 mokhov Exp $
 * $Revision: 1.8 $
 * $Date: 2005/06/15 04:28:48 $
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 */
public interface IImperativeCompiler
extends ICompiler, ICommunicationProceduresEnum
{
	/**
	 * Generates one or more STs.
	 * @param poExtraArgs extra arguments if necessary
	 * @throws GICFException if the generation was unsuccessful
	 */
	public ISequentialThread[] generateSequentialThreads(Object poExtraArgs)
	throws ImperativeCompilerException;

	/**
	 * Generates one or more CPs.
	 * @param poExtraArgs extra arguments if necessary
	 * @throws GICFException if the generation was unsuccessful
	 */
	public ICommunicationProcedure[] generateCommuncationProcedures(Object poExtraArgs)
	throws ImperativeCompilerException;
}

// EOF
