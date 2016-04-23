package gipsy.interfaces;

import java.io.Serializable;

import gipsy.storage.Dictionary;
import gipsy.interfaces.IIdentifierContext;
import gipsy.lang.GIPSYContext;


/**
 * Represents a compiled version of a GIPSY Program.
 * An instance of this can be serialized into a file
 * for later execution/interpretation by GEE. Essentially,
 * this is the GEER.
 *
 * The contents of this class is usually set up by GIPC.
 *
 * @author Serguei Mokhov
 * @version $Id: GIPSYProgram.java,v 1.10 2013/08/25 02:54:42 mokhov Exp $
 * @since 1.0.0
 */
public class GIPSYProgram
implements Serializable
{
	/**
	 * For serialization versioning. 
	 */
	private static final long serialVersionUID = 623518175800368673L;

	/**
	 * AST for GEE's execution.
	 */
	//protected AbstractSyntaxTree oAST = null;
	protected AbstractSyntaxTree[] aoASTs = null;
	
	/**
	 * Dictionary of identifiers.
	 */
	protected Dictionary oDictionary = null;
	
	/**
	 * A collection of communication procedures.
	 */
	@Deprecated
	protected ICommunicationProcedure[] aoCP = null;
	
	/**
	 * A collection of sequential threads.
	 */
	protected ISequentialThread[] aoST = null;
	
	/**
	 * A collection of identifier-context classes.
	 */
	@Deprecated
	protected IIdentifierContext[] aoIC = null;

	/**
	 * Name of this program.
	 * Usually an original program's filename or can be
	 * generated. Used by Value Warehouse.
	 */
	protected String strName = null;
	
	/**
	 * Context for this program.
	 * @see #oContext
	 */
	@Deprecated
	protected String strContext = null;

	/**
	 * Context for this program.
	 */
	protected GIPSYContext oContext = null;

	/**
	 * The signature of the GEER for GEER resource
	 * migration/querying.
	 */
	protected GEERSignature oSignature = null;
	
	/**
	 * Defines an empty GIPSY program for testing.
	 * TODO: determine how useful is that and document it.
	 */
	public GIPSYProgram()
	{
		this.aoASTs = new AbstractSyntaxTree[] {null};
		this.oDictionary = new Dictionary();
		this.aoCP = new ICommunicationProcedure[] {};
		this.aoST = new ISequentialThread[] {};
		this.aoIC = new IIdentifierContext[] {};

		this.strName = "Empty-GIPSYProgram-" + Math.random();
		this.strContext = "d=0";
		
		this.oContext = new GIPSYContext();

		resetGEERSignature();
	}

	/**
	 * @since Feb 4, 2010 
	 */
	private void resetGEERSignature()
	{
		// Initialize the signature the program name and top-level context
		// XXX: Refactor, for all constructors and update methods to use
		this.oSignature = new GEERSignature();
		this.oSignature.setSignature(this);
	}
	
	/**
	 * Constructs an instance of a GIPSYProgram from
	 * prepared parameters.
	 * 
	 * @param poAST AST ready for GEE's execution
	 * @param poDictionary dictionary of identifiers
	 * @param paoCP a collection of communication procedures
	 * @param paoST a collection of sequential threads
	 * @param paoIC a collection of identifier-context classes
	 * 
	 * @throws NullPointerException if poAST or poDictionary is null 
	 */
	@Deprecated
	public GIPSYProgram
	(
		AbstractSyntaxTree poAST,
		Dictionary poDictionary,
		ICommunicationProcedure[] paoCP,
		ISequentialThread[] paoST,
		IIdentifierContext[] paoIC
	)
	{
		setAbstractSyntaxTree(poAST);
		setDictionary(poDictionary);
		setCommunicationProcedures(paoCP);
		setSequentialThreads(paoST);
		setIdentifierContexts(paoIC);
		
		this.strName = "GIPSYProgram-" + Math.random();
		this.strContext = "d=1";

		this.oContext = new GIPSYContext();
		
		resetGEERSignature();
	}

	/**
	 * Constructs an instance of a GIPSYProgram from
	 * prepared parameters.
	 * 
	 * @param poAST AST ready for GEE's execution
	 * @param poDictionary dictionary of identifiers
	 * @param paoST a collection of sequential threads
	 * @since November 5, 2008
	 */
	public GIPSYProgram
	(
		AbstractSyntaxTree poAST,
		Dictionary poDictionary,
		ISequentialThread[] paoST
	)
	{
		setAbstractSyntaxTree(poAST);
		setDictionary(poDictionary);
		setSequentialThreads(paoST);
		
		this.strName = "GIPSYProgram-" + Math.random();
		this.oContext = new GIPSYContext();
		
		resetGEERSignature();
	}

	/**
	 * Initializes local AST reference with the parameter.
	 * @param poAST AST to set
	 * @throws NullPointerException if poAST is null
	 */
	public void setAbstractSyntaxTree(AbstractSyntaxTree poAST)
	{
		if(poAST == null)
		{
			throw new NullPointerException("Setting null AST is not allowed for GIPSYProgram.");
		}

		if(this.aoASTs.length == 0)
		{
			this.aoASTs = new AbstractSyntaxTree[1];
		}
		
		this.aoASTs[0] = poAST;
	}

	/**
	 * Retrieves local reference to the AST.
	 * @return the AbstractSyntaxTree object
	 */
	public AbstractSyntaxTree[] getAbstractSyntaxTrees()
	{
		return this.aoASTs;
	}

	/**
	 * Initializes local dictionary reference with the parameter.
	 * @param poDictionary dictionary to set
	 * @throws NullPointerException if poDictionary is null
	 */
	public void setDictionary(Dictionary poDictionary)
	{
		if(poDictionary == null)
		{
			throw new NullPointerException("Setting null dictionary is not allowed for GIPSYProgram.");
		}

		this.oDictionary = poDictionary;
	}

	/**
	 * Retrieves local reference to the dictionary.
	 * @return the Dictionary object
	 */
	public Dictionary getDictionary()
	{
		return this.oDictionary;
	}

	/**
	 * Initializes local reference to a collection of CPs with the parameter.
	 * @param paoCP collection to set
	 */
	@Deprecated
	public void setCommunicationProcedures(ICommunicationProcedure[] paoCP)
	{
		this.aoCP = paoCP;
	}

	/**
	 * Retrieves local references all the CPs.
	 * @return a collection of CPs
	 */
	@Deprecated
	public ICommunicationProcedure[] getCommunicationProcedures()
	{
		return this.aoCP;
	}

	/**
	 * Initializes local reference to a collection of STs with the parameter.
	 * @param paoST collection to set
	 */
	public void setSequentialThreads(ISequentialThread[] paoST)
	{
		this.aoST = paoST;
	}

	/**
	 * Retrieves local references all the STs.
	 * @return a collection of STs
	 */
	public ISequentialThread[] getSequentialThreads()
	{
		return this.aoST;
	}

	/**
	 * Initializes local reference to a collection of ICs with the parameter.
	 * @param paoIC collection to set
	 */
	@Deprecated
	public void setIdentifierContexts(IIdentifierContext[] paoIC)
	{
		this.aoIC = paoIC;
	}

	/**
	 * Retrieves local references all the ICs.
	 * @return a collection of ICs
	 */
	@Deprecated
	public IIdentifierContext[] getIdentifierContexts()
	{
		return this.aoIC;
	}
	
	/**
	 * Initializes local program name with the parameter.
	 * @param pstrName program name to set
	 * @throws NullPointerException if pstrName is null
	 */
	public void setName(String pstrName)
	{
		if(pstrName == null)
		{
			throw new NullPointerException("Setting null program name is not allowed for GIPSYProgram.");
		}

		this.strName = pstrName;
	}

	/**
	 * Retrieves local reference to the program name.
	 * @return the name string
	 */
	public String getName()
	{
		return this.strName;
	}
	
	/**
	 * Initializes local program context with the parameter.
	 * @param pstrContext program context to set
	 * @throws NullPointerException if pstrContext is null
	 */
	@Deprecated
	public void setContext(String pstrContext)
	{
		if(pstrContext == null)
		{
			throw new NullPointerException("Setting null context is not allowed for GIPSYProgram.");
		}

		this.strContext = pstrContext;
	}
	
	/**
	 * Initializes local program context with the parameter.
	 * @param pstrContext program context to set
	 * @throws NullPointerException if pstrContext is null
	 */
	public void setContext(GIPSYContext poContext)
	{
		if(poContext == null)
		{
			throw new NullPointerException("Setting null context is not allowed for GIPSYProgram.");
		}

		this.oContext = poContext;
	}

	/**
	 * Retrieves local reference to the context.
	 * @return the context string 
	 */
	@Deprecated
	public String getContext()
	{
		return this.strContext;
	}

	/**
	 * Retrieves local reference to the context.
	 * @return the current context 
	 */
	public GIPSYContext getContextValue()
	{
		return this.oContext;
	}

	/**
	 * @return
	 */
	public GEERSignature getSignature()
	{
		return this.oSignature;
	}

	/**
	 * @param poSignature
	 */
	public void setSignature(GEERSignature poSignature)
	{
		this.oSignature = poSignature;
	}

	/**
	 * Overrides Object's toString() for debugging purposes.
	 * @return String representation of this GIPSYProgram instance
	 * including its name, context of evaluation, AST, dictionary,
	 * number of CPs, STs, and ICs.
	 */
	public String toString()
	{
		String strASTs = "";

		for(int i = 0; i < this.aoASTs.length; i++)
		{
			strASTs += "AST " + i + ":\n" + this.aoASTs[i] + "\n\n";
		}

		return
			getName() + ":\n" +
			"Context: " + this.strContext + "\n" +
			"Context: " + this.oContext + "\n" +
			"ASTs: " + strASTs + "\n" +
			"Dictionary: " + this.oDictionary + "\n" +
			"CP#: " + (this.aoCP == null ? 0 : this.aoCP.length) + "\n" +
			"ST#: " + (this.aoST == null ? 0 : this.aoST.length) + "\n" +
			"IC#: " + (this.aoIC == null ? 0 : this.aoIC.length) + "\n" +
			"";
	}
}

// EOF
