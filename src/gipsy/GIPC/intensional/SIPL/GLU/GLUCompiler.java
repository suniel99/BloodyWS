package gipsy.GIPC.intensional.SIPL.GLU;

import java.io.InputStream;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.intensional.IntensionalCompiler;
import gipsy.interfaces.AbstractSyntaxTree;
import gipsy.util.NotImplementedException;

/**
 * Main GLU Compiler.
 * 
 * @author Serguei Mokhov
 * @version $Id: GLUCompiler.java,v 1.3 2013/01/09 14:52:35 mokhov Exp $
 * @since
 */
public class GLUCompiler
extends IntensionalCompiler
{
	/**
	 * For serialization versionning.
	 */
	private static final long serialVersionUID = 4023585530781742755L;

	/**
	 * @throws GIPCException
	 */
	public GLUCompiler() throws GIPCException
	{
		super();
	}

	/**
	 * @param poInputStream
	 * @throws GIPCException
	 */
	public GLUCompiler(InputStream poInputStream) throws GIPCException
	{
		super(poInputStream);
	}

	/**
	 * @param pstrFilename
	 * @throws GIPCException
	 */
	public GLUCompiler(String pstrFilename) throws GIPCException
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
