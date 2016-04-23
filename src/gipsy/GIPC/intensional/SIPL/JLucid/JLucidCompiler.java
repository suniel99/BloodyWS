package gipsy.GIPC.intensional.SIPL.JLucid;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.imperative.Java.JavaCompiler;
import gipsy.GIPC.intensional.IntensionalCompiler;
import gipsy.interfaces.AbstractSyntaxTree;

import java.io.InputStream;


/**
 * <p>Main JLucid Compiler.</p>
 *
 * @author Serguei Mokhov
 * @version $Id: JLucidCompiler.java,v 1.10 2013/01/09 14:52:50 mokhov Exp $
 * @since 1.0.0
 */
public class JLucidCompiler
extends IntensionalCompiler
{
	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = -489438957131352656L;
	
	protected JLucidPreprocessor oPreprocessor;
	protected JLucidParser oParser;
	protected JavaSource oJavaSource;
	protected JavaCompiler oJavaCompiler;

	/**
	 * @throws GIPCException
	 */
	public JLucidCompiler()
	throws GIPCException
	{
		super();
	}

	/**
	 * @param poInputStream
	 * @throws GIPCException
	 */
	public JLucidCompiler(InputStream poInputStream)
	throws GIPCException
	{
		super(poInputStream);
	}

	/**
	 * @param pstrFilename
	 * @throws GIPCException
	 */
	public JLucidCompiler(String pstrFilename)
	throws GIPCException
	{
		super(pstrFilename);
	}

	/* (non-Javadoc)
	 * @see gipsy.GIPC.ICompiler#init()
	 */
	public void init()
	throws GIPCException
	{
		//this.oPreprocessor = new JLucidPreprocessor(this.oSourceCodeStream);
		this.oParser = new JLucidParser(this.oSourceCodeStream);
		this.oJavaCompiler = new JavaCompiler();
	}

	/* (non-Javadoc)
	 * @see gipsy.GIPC.ICompiler#parse()
	 */
	public AbstractSyntaxTree parse()
	throws GIPCException
	{
		try
		{
			return this.oParser.parse();
		}
		catch(Exception e)
		{
			this.oLastException = e;

			if(e instanceof GIPCException)
			{
				throw (GIPCException)e;
			}
			else
			{
				throw new GIPCException(e.getMessage(), e);
			}
		}
		catch(Throwable e)
		{
			this.oLastException = new GIPCException("throwable choke and has thrown up: " + e);
			throw (GIPCException)this.oLastException;
		}
	}
}

// EOF
