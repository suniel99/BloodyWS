package gipsy.GIPC.intensional.SIPL.Onyx;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.intensional.IntensionalCompiler;
import gipsy.interfaces.AbstractSyntaxTree;
import gipsy.util.NotImplementedException;

import java.io.InputStream;

/**
 * Main Onyx Compiler.
 *
 * @author Serguei Mokhov
 * @version $Id: OnyxCompiler.java,v 1.5 2013/01/09 14:54:10 mokhov Exp $
 * @since
 */
public class OnyxCompiler
extends IntensionalCompiler
{
	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = 5872414427757040403L;

	/**
	 * @throws GIPCException
	 */
	public OnyxCompiler() throws GIPCException
	{
		super();
	}

	/**
	 * @param poInputStream
	 * @throws GIPCException
	 */
	public OnyxCompiler(InputStream poInputStream) throws GIPCException
	{
		super(poInputStream);
	}

	/**
	 * @param pstrFilename
	 * @throws GIPCException
	 */
	public OnyxCompiler(String pstrFilename) throws GIPCException
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
