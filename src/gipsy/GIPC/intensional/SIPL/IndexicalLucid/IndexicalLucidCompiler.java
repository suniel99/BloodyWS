package gipsy.GIPC.intensional.SIPL.IndexicalLucid;

import java.io.InputStream;

import marf.util.Debug;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.intensional.IntensionalCompiler;
import gipsy.GIPC.intensional.SIPL.IndexicalLucid.IndexicalLucidParser;
import gipsy.interfaces.AbstractSyntaxTree;


/**
 * Main Indexical Lucid Compiler.
 *
 * @author Serguei Mokhov
 * @version $Id: IndexicalLucidCompiler.java,v 1.7 2013/01/09 14:52:42 mokhov Exp $
 * @since 1.0.0
 */
public class IndexicalLucidCompiler
extends IntensionalCompiler
{
	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = -4413838566826702416L;
	
	/**
	 * A reference to the Indexical Lucid parser.
	 */
	private IndexicalLucidParser oParser = null;

	/**
	 * @throws GIPCException
	 */
	public IndexicalLucidCompiler()
	throws GIPCException
	{
		super();
	}

	/**
	 * @param poInputStream
	 * @throws GIPCException
	 */
	public IndexicalLucidCompiler(InputStream poInputStream)
	throws GIPCException
	{
		super(poInputStream);
	}

	/**
	 * @param pstrFilename
	 * @throws GIPCException
	 */
	public IndexicalLucidCompiler(String pstrFilename)
	throws GIPCException
	{
		super(pstrFilename);
	}
	
	/**
	 * Initialized the Indexical Lucid parser with the source code input stream.
	 * Implementation of ICompiler.
	 * @throws GIPCException if there is any error constructing GIPLParser
	 */
	public void init()
	throws GIPCException
	{
		Debug.debug(this.getClass(), " source " + this.oSourceCodeStream);
		this.oParser = new IndexicalLucidParser(this.oSourceCodeStream);
		Debug.debug(this.getClass(), " init done.");
	}

	/**
	 * Invokes IndexicalLucidParser.
	 * Implementation of ICompiler.
	 * @return AbstractSyntaxTree of the parse.
	 * @throws GIPCException if there is any error constructing GIPLParser
	 */
	public AbstractSyntaxTree parse()
	throws GIPCException
	{
		Debug.debug(this.getClass(), " parsing.");
		return this.oParser.parse();
	}
}

// EOF
