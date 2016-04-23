package gipsy.GIPC.intensional;

import java.io.FileInputStream;
import java.io.InputStream;

import marf.Storage.StorageManager;
import marf.util.Debug;

import gipsy.GIPC.GIPCException;
import gipsy.interfaces.AbstractSyntaxTree;


/**
 * All intensional compilers should try their best
 * to sublcass this class. If not possible they must
 * implement IIntensionalCompiler.
 * 
 * @author Serguei Mokhov
 * @version $Id: IntensionalCompiler.java,v 1.19 2013/01/07 19:16:10 mokhov Exp $
 * @since 1.0.0 
 */
public abstract class IntensionalCompiler
extends StorageManager
implements IIntensionalCompiler
{
	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = -5300392505455737835L;

	/**
	 * Local references to the produced AST.
	 */
	protected AbstractSyntaxTree oAST = null;
	
	/**
	 * Input source code stream. 
	 */
	protected InputStream oSourceCodeStream = null;
	
	/**
	 * Reference to the latest exception happened.
	 */
	protected Exception oLastException = null;
	
	/**
	 * Default constructor. Sets default input stream to STDIN.
	 * @throws GIPCException if System.in is null
	 */
	public IntensionalCompiler()
	throws GIPCException
	{
		if(System.in == null)
		{
			throw (GIPCException)(this.oLastException =
				new GIPCException("Cannot read from System.in as it appears null."));
		}

		this.oSourceCodeStream = System.in;
	}

	/**
	 * Constructor with a filename argument.
	 * @param pstrFilename filename of an input source code file
	 * @throws GIPCException if there is any error of associating a
	 * FileInputStream with the given filename
	 */
	public IntensionalCompiler(String pstrFilename)
	throws GIPCException
	{
		try
		{
			this.oSourceCodeStream = new FileInputStream(pstrFilename);
		}
		catch(Exception e)
		{
			throw (GIPCException)(this.oLastException = new GIPCException(e));
		}
	}

	/**
	 * Constructor with an InputStream argument.
	 * @param pstrFilename filename of an input source code file
	 * @throws GIPCException if poInputStream is null
	 */
	public IntensionalCompiler(InputStream poInputStream)
	throws GIPCException
	{
		if(poInputStream == null)
		{
			throw (GIPCException)(this.oLastException =
				new GIPCException("Cannot read from InputStream as it appears null."));
		}
		
		this.oSourceCodeStream = poInputStream;
	}

	/**
	 * Implementation of ICompiler.
	 * @param poExtraArgs unused
	 */
	public AbstractSyntaxTree compile(Object poExtraArgs)
	throws GIPCException
	{
		init();
		Debug.debug(this.getClass(), " init done (1).");
		this.oAST = parse();
		Debug.debug(this.getClass(), " parsing done (2).");
		this.oAST = translate();
		Debug.debug(this.getClass(), " translation done (3).");
		return this.oAST;
	}

	public AbstractSyntaxTree compile()
	throws GIPCException
	{
		return compile(null);
	}

	/**
	 * Implementation of ICompiler.
	 * @return AST reference
	 */
	public AbstractSyntaxTree getAbstractSyntaxTree()
	{
		return this.oAST;
	}

	/**
	 * For default IPL compiler no actual translation is required,
	 * so we simply return the original AST. This is pertinent
	 * for example in GIPLCompiler; the others might want to
	 * override this.
	 * 
	 * @return original AST
	 */
	public AbstractSyntaxTree translate()
	throws IntensionalCompilerException
	{
		return this.oAST;
	}
	
	/**
	 * Body of the compiler thread.
	 * Implements Runnable.
	 */
	public void run()
	{
		try
		{
			Debug.debug("Intensional Compiler thread: " + this + " has begun.");
			compile();
			Debug.debug("Intensional Compiler thread: " + this + " compilation successful.");
		}
		catch(GIPCException e)
		{
			System.err.println(e);
			e.printStackTrace(System.err);
			this.oLastException = e;
		}

		Debug.debug("Intensional Compiler thread: " + this + " terminated.");
	}
	
	//public GIPCException getLastException()
	public Exception getLastException()
	{
		return this.oLastException;
	}

	/**
	 * 
	 */
	public void setSourceCodeStream(InputStream poSourceCodeStream)
	{
		this.oSourceCodeStream = poSourceCodeStream;
	}
	
	public String toString()
	{
		return getClass().getName();
	}
}

// EOF
