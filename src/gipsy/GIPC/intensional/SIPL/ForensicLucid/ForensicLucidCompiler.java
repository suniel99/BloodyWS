package gipsy.GIPC.intensional.SIPL.ForensicLucid;

import java.io.InputStream;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.intensional.IntensionalCompiler;
import gipsy.interfaces.AbstractSyntaxTree;


/**
 * Forensic Lucid PoC compiler.
 * @author Serguei Mokhov
 * @version $Id: ForensicLucidCompiler.java,v 1.3 2013/07/15 19:44:09 mokhov Exp $
 * @since 
 */
public class ForensicLucidCompiler
extends IntensionalCompiler
{
	/**
	 * For serialization versionning.
	 */
	private static final long serialVersionUID = -200321946372739355L;

	/**
	 * Parser instance.
	 */
	private ForensicLucidParser oParser = null;

	/**
	 * @throws GIPCException
	 */
	public ForensicLucidCompiler()
	throws GIPCException
	{
		super();
	}

	/**
	 * @param pstrFilename
	 * @throws GIPCException
	 */
	public ForensicLucidCompiler(String pstrFilename)
	throws GIPCException
	{
		super(pstrFilename);
	}

	/**
	 * @param poInputStream
	 * @throws GIPCException
	 */
	public ForensicLucidCompiler(InputStream poInputStream)
	throws GIPCException
	{
		super(poInputStream);
	}

	/* (non-Javadoc)
	 * @see gipsy.GIPC.ICompiler#init()
	 */
	@Override
	public void init()
	throws GIPCException
	{
		this.oParser = new ForensicLucidParser(this.oSourceCodeStream);
	}

	/* (non-Javadoc)
	 * @see gipsy.GIPC.ICompiler#parse()
	 */
	@Override
	public AbstractSyntaxTree parse()
	throws GIPCException
	{
		return this.oParser.parse();
	}

	/**
	 * @param argv
	 */
	public static void main(String[] argv)
	{
		// TODO Auto-generated method stub
	}
}

// EOF
