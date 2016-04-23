package gipsy.GIPC.intensional.SIPL.TensorLucid;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.intensional.IntensionalCompiler;
import gipsy.interfaces.AbstractSyntaxTree;
import gipsy.util.NotImplementedException;

import java.io.InputStream;

/**
 * Main Tensor Lucid Compiler.
 *
 * @author Serguei Mokhov
 * @version $Id: TensorLucidCompiler.java,v 1.5 2013/01/09 14:54:18 mokhov Exp $
 * @since
 */
public class TensorLucidCompiler
extends IntensionalCompiler
{
	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = 1544697777537478527L;

	/**
	 * @throws GIPCException
	 */
	public TensorLucidCompiler() throws GIPCException
	{
		super();
	}

	/**
	 * @param poInputStream
	 * @throws GIPCException
	 */
	public TensorLucidCompiler(InputStream poInputStream) throws GIPCException
	{
		super(poInputStream);
	}

	/**
	 * @param pstrFilename
	 * @throws GIPCException
	 */
	public TensorLucidCompiler(String pstrFilename) throws GIPCException
	{
		super(pstrFilename);
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
