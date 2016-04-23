package gipsy.interfaces;

import gipsy.GIPC.util.Node;
import gipsy.util.NotImplementedException;

import java.io.Serializable;


/**
 * <p>Abstract Syntax Tree.
 * Creates our own abstraction over JavaCC-generated AST.</p>
 * 
 * @version $Id: AbstractSyntaxTree.java,v 1.15 2013/08/25 02:54:42 mokhov Exp $
 * 
 * @author Serguei Mokhov
 * @since Inception
 */
public class AbstractSyntaxTree
implements Cloneable, Serializable
{
	/**
	 * For serialization versioning. 
	 */
	private static final long serialVersionUID = -1215996289465577517L;
	
	/**
	 * Root Node of the AST.
	 */
	protected Node oNodeRoot = null;

	/**
	 * Constructs an empty AST with the null root.
	 */
	public AbstractSyntaxTree()
	{
	}

	/**
	 * Constructs an AST with the root node set from the parameter.
	 * Note, this is <b>not</b> a copy-constructor.
	 * 
	 * @param poNodeStart root AST node
	 */
	public AbstractSyntaxTree(Node poNodeStart)
	{
		setRoot(poNodeStart);
	}

	/**
	 * Constructs an AST from another AbstractSyntaxTree.
	 * Note, this is <b>not</b> a copy-constructor.
	 * 
	 * @param poAbstractSyntaxTree another AST we take the root from
	 */
	public AbstractSyntaxTree(AbstractSyntaxTree poAbstractSyntaxTree)
	{
		setRoot(poAbstractSyntaxTree.getRoot());
	}

	/**
	 * Retrieves local reference to the root node.
	 * @return local Node 
	 */
	public Node getRoot()
	{
		return this.oNodeRoot;
	}

	/**
	 * Sets local reference to the root node.
	 * @param poNode node to become root of the AST 
	 */
	public final void setRoot(Node poNode)
	{
		this.oNodeRoot = poNode;
	}

	/**
	 * Displays the AST to STDOUT.
	 */
	public void showTree()
	{
		dump(" ");
	}

	/**
	 * Displays the AST to STDOUT with the specified prefix.
	 * @param pstrPrefix prefix to stick in before the printout
	 */
	public void dump(String pstrPrefix)
	{
		this.oNodeRoot.dump(pstrPrefix);
	}
	
	/**
	 * Creates a deep copy of this tree.
	 * Not implemented.
	 */
	public Object clone()
	{
		throw new NotImplementedException(this, "clone()");
	}
	
	public String toString()
	{
		if(this.oNodeRoot == null)
		{
			return "NIL";
		}
		else
		{
			String strAST = this.oNodeRoot.toString() + "\n";

			Node oNodeParent = this.oNodeRoot;
			
			for(int i = 0; i < oNodeParent.jjtGetNumChildren(); i++)
			{
				Node oNode = oNodeParent.jjtGetChild(i);
				
				if(oNode != null)
					strAST += oNode + "\n";
			}
			
			return strAST;
		}
	}
}

// EOF
