package gipsy.GIPC.intensional.GIPL;

import java.io.InputStream;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.intensional.IntensionalCompiler;
import gipsy.interfaces.AbstractSyntaxTree;

/**
 * Main GIPL Compiler.
 *
 * @author Serguei Mokhov
 * @version $Id: GIPLCompiler.java,v 1.8 2013/01/09 14:52:29 mokhov Exp $
 * @since 1.0.0
 */
public class GIPLCompiler
extends IntensionalCompiler
{
	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = 4880184644617811195L;
	
	/**
	 * A reference to the GIPL parser.
	 */
	private GIPLParser oParser;

	/**
	 * @throws GIPCException
	 */
	public GIPLCompiler()
	throws GIPCException
	{
		super();
	}
	
	/**
	 * @param poInputStream
	 * @throws GIPCException
	 */
	public GIPLCompiler(InputStream poInputStream)
	throws GIPCException
	{
		super(poInputStream);
	}
	
	/**
	 * @param pstrFilename
	 * @throws GIPCException
	 */
	public GIPLCompiler(String pstrFilename)
	throws GIPCException
	{
		super(pstrFilename);
	}

	/**
	 * Initialized the GIPL parser with the source code input stream.
	 * Implementation of ICompiler.
	 * @throws GIPCException if there is any error constructing GIPLParser
	 */
	public void init()
	throws GIPCException
	{
		this.oParser = new GIPLParser(this.oSourceCodeStream);
	}

	/**
	 * Invokes GIPLParser.
	 * Implementation of ICompiler.
	 * @return AbstractSyntaxTree of the parse.
	 * @throws GIPCException if there is any error constructing GIPLParser
	 */
	public AbstractSyntaxTree parse()
	throws GIPCException
	{
		return this.oParser.parse();
	}
}

// EOF
