package gipsy.GIPC.intensional.SIPL.ObjectiveLucid;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.intensional.IntensionalCompiler;
import gipsy.GIPC.intensional.SIPL.JLucid.JLucidCompiler;
import gipsy.interfaces.AbstractSyntaxTree;

import java.io.InputStream;


/**
 * <p>Main Objective Lucid Compiler.</>
 *
 * @author Serguei Mokhov
 * @version $Id: ObjectiveLucidCompiler.java,v 1.12 2013/01/09 14:54:02 mokhov Exp $
 * @since 1.0.0
 */
public class ObjectiveLucidCompiler
extends IntensionalCompiler
{
	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = 1891937826576078968L;

	/**
	 * Underlying reference to the JLucid Compiler
	 * to do most legwork.
	 */
	private JLucidCompiler oJLucidCompiler = null;

	/**
	 * Local preprocessor.
	 */
	private ObjectiveLucidPreprocessor oPreprocessor = null;

	private ObjectiveLucidParser oParser = null;

	/**
	 * @throws GIPCException
	 */
	public ObjectiveLucidCompiler()
	throws GIPCException
	{
		super();
		//this.oJLucidCompiler = new JLucidCompiler();
	}

	/**
	 * @param poInputStream
	 * @throws GIPCException
	 */
	public ObjectiveLucidCompiler(InputStream poInputStream)
	throws GIPCException
	{
		super(poInputStream);
		//this.oJLucidCompiler = new JLucidCompiler(poInputStream);
	}

	/**
	 * @param pstrFilename
	 * @throws GIPCException
	 */
	public ObjectiveLucidCompiler(String pstrFilename) throws GIPCException
	{
		super(pstrFilename);
		//this.oJLucidCompiler = new JLucidCompiler(pstrFilename);
	}

	/**
	 *
	 */
	public void init()
	throws GIPCException
	{
		//this.oPreprocessor = new ObjectiveLucidPreprocessor(this.oSourceCodeStream);
		this.oParser = new ObjectiveLucidParser(this.oSourceCodeStream);
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
	}
}

// EOF
