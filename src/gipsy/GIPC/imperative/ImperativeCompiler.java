package gipsy.GIPC.imperative;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.imperative.CommunicationProcedureGenerator.CommunicationProcedureGenerator;
import gipsy.GIPC.imperative.SequentialThreadGenerator.SequentialThreadGenerator;
import gipsy.GIPC.util.Node;
import gipsy.interfaces.AbstractSyntaxTree;
import gipsy.interfaces.ICommunicationProcedure;
import gipsy.interfaces.ISequentialThread;

import java.io.InputStream;


/**
 * <p>Provide most common implementation of IImperativeCompiler.
 * If a given class cannot inherit from this, then they should
 * implement IImperativeCompiler themselves.</p>
 *
 * $Id: ImperativeCompiler.java,v 1.20 2005/09/12 01:24:36 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.20 $
 * @since 1.0.0
 */
public abstract class ImperativeCompiler
implements IImperativeCompiler
{
	/**
	 * Local references to the produced AST.
	 */
	protected AbstractSyntaxTree oAST = null;

	/**
	 * Input source code stream. 
	 */
	protected InputStream oSourceCodeStream = null;

	/**
	 * Reference to the concrete ST Generator.
	 * Must be initialized by the derivatives.
	 */
	protected SequentialThreadGenerator oSTGenerator = null;

	/**
	 * Reference to the concrete CP Generator.
	 * Must be initialized by the derivatives.
	 */
	protected CommunicationProcedureGenerator oCPGenerator = null;

	//protected GIPCException oLastException = null;
	protected Exception oLastException = null;

	protected FormatTag oFormatTag = null;
	
	/**
	 * A set of generated STs.
	 */
	protected ISequentialThread[] aoST = null;

	/**
	 * A set of generated CPs.
	 */
	protected ICommunicationProcedure[] aoCP = null;

	/**
	 * Common implementation of ICompiler.
	 * @param poExtraArgs unused
	 */
	public AbstractSyntaxTree compile(Object poExtraArgs)
	throws GIPCException
	{
		init();
		/*this.oAST =*/ parse();
		
		this.aoST = generateSequentialThreads(poExtraArgs);
		//this.aoCP = generateCommuncationProcedures(poExtraArgs);
		
		ImperativeNode oImperativeNode = null;
		
		if(poExtraArgs != null && poExtraArgs instanceof Node)
		{
			oImperativeNode = new ImperativeNode(this.oFormatTag, this.aoST, this.aoCP, (Node)poExtraArgs);
		}
		else
		{
			oImperativeNode = new ImperativeNode(this.oFormatTag, this.aoST, this.aoCP);
		}
		
		// Imperative AST consists only of a single node.
		this.oAST = new AbstractSyntaxTree(oImperativeNode);
		
		return this.oAST;
	}

	/* (non-Javadoc)
	 * @see gipsy.GIPC.ICompiler#compile()
	 */
	public AbstractSyntaxTree compile()
	throws GIPCException
	{
		return compile(null);
	}

	/**
	 * Implementation of ICompiler.
	 */
	public AbstractSyntaxTree getAbstractSyntaxTree()
	{
		return this.oAST;
	}

	/**
	 * Generates one or more STs.
	 * @param poExtraArgs extra arguments if necessary
	 * @throws GICFException if the generation was unsuccessful
	 */
	public ISequentialThread[] generateSequentialThreads(Object poExtraArgs)
	throws ImperativeCompilerException
	{
		this.oSTGenerator.generate();
		return this.oSTGenerator.getSequentialThreads();
	}

	/**
	 * Generates one or more CPs.
	 * @param poExtraArgs extra arguments if necessary
	 * @throws GICFException if the generation was unsuccessful
	 */
	public ICommunicationProcedure[] generateCommuncationProcedures(Object poExtraArgs)
	throws ImperativeCompilerException
	{
		this.oCPGenerator.generate();
		return this.oCPGenerator.getCommunicationProcedures();
	}

	/**
	 * Body of the compiler thread.
	 * Implements Runnable.
	 */
	public void run()
	{
		try
		{
			compile(null);
		}
		catch(GIPCException e)
		{
			System.err.println(e);
			e.printStackTrace(System.err);
			this.oLastException = e;
		}
	}
	
	//public GIPCException getLastException()
	public Exception getLastException()
	{
		return this.oLastException;
	}

	/**
	 */
	public void setSourceCodeStream(InputStream poSourceCodeStream)
	{
		this.oSourceCodeStream = poSourceCodeStream;
	}
}

// EOF
