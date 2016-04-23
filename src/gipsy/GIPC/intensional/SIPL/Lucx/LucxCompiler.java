package gipsy.GIPC.intensional.SIPL.Lucx;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.intensional.IntensionalCompiler;
import gipsy.interfaces.AbstractSyntaxTree;

import java.io.InputStream;


/**
 * Main Lucx Compiler.
 *
 * @author Serguei Mokhov
 * @author Xin Tong
 * @version $Id: LucxCompiler.java,v 1.5 2013/03/12 17:07:10 mokhov Exp $
 * @since 1.0.0
 */
public class LucxCompiler
extends IntensionalCompiler
{
	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = 3912099678167054017L;
	
	/**
	 * @author Xin Tong
	 */
	private LucxParser oParser = null;
	
	/**
	 * @throws GIPCException
	 */
	public LucxCompiler()
	throws GIPCException
	{
		super();
	}

	/**
	 * @param poInputStream
	 * @throws GIPCException
	 */
	public LucxCompiler(InputStream poInputStream)
	throws GIPCException
	{
		super(poInputStream);
	}

	/**
	 * @param pstrFilename
	 * @throws GIPCException
	 */
	public LucxCompiler(String pstrFilename)
	throws GIPCException
	{
		super(pstrFilename);
	}

	public void init()
	throws GIPCException
	{
		this.oParser = new LucxParser(this.oSourceCodeStream);
	}

	public AbstractSyntaxTree parse()
	throws GIPCException
	{
		return this.oParser.parse();
	}
}

// EOF
