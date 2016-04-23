package gipsy.GIPC;

import java.io.InputStream;

import gipsy.interfaces.AbstractSyntaxTree;


/**
 * <p>The most generic interface all compilers must adhere to.</p>
 * <p>It extens <code>Runnable</code>, so a compiler may possibly be a thread.</p>
 *
 * $Id: ICompiler.java,v 1.10 2009/08/25 18:47:05 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.10 $
 * @since 1.0.0
 *
 * @see Runnable
 */
public interface ICompiler
extends Runnable
{
	/**
	 * Initialization of a compiler.
	 * @throws GIPCException if the initialization was unsuccessful
	 */
	public void init()
	throws GIPCException;

	/**
	 * Parsing of the source code.
	 * @return AbstractSyntaxTree of the parse
	 * @throws GIPCException if the parsing was unsuccessful
	 */
	public AbstractSyntaxTree parse()
	throws GIPCException;

	/**
	 * Compilation usually includes the initialization and parsing
	 * and producing an AST.
	 *
	 * @param poExtraArgs any sorts of extra arguments a given application
	 * might want to pass on to a compiler (e.g. command line options).
	 *
	 * @return AbstractSyntaxTree of the entire compilation process
	 *
	 * @throws GIPCException if the compilation was unsuccessful
	 */
	public AbstractSyntaxTree compile(Object poExtraArgs)
	throws GIPCException;

	/**
	 * Compilation usually includes the initialization and parsing
	 * and producing an AST. Typical implementation should be
	 * <code>return compile(null);</code>.
	 *
	 * @return AbstractSyntaxTree of the entire compilation process
	 *
	 * @throws GIPCException if the compilation was unsuccessful
	 */
	public AbstractSyntaxTree compile()
	throws GIPCException;

	/**
	 * Allows to retrieve the copy of stored AST.
	 * @return current AbstractSyntaxTree
	 */
	public AbstractSyntaxTree getAbstractSyntaxTree();

	/**
	 * Allows callers to retrieve last exception
	 * occurred within a compiler.
	 */
	public Exception getLastException();

	/**
	 * Allows external compilers setting input source code stream.
	 * @param poSourceCodeStream
	 */
	public void setSourceCodeStream(InputStream poSourceCodeStream);
}

// EOF
