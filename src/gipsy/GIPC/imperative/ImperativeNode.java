package gipsy.GIPC.imperative;

import gipsy.GIPC.util.Node;
import gipsy.interfaces.ICommunicationProcedure;
import gipsy.interfaces.ISequentialThread;
import gipsy.storage.FunctionItem;


/**
 * @author Serguei Mokhov
 * @version $Id: ImperativeNode.java,v 1.7 2013/08/20 19:45:55 mokhov Exp $
 * @since 1.0.0
 */
public class ImperativeNode
implements Node
{
	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = 7741983628081409738L;

	protected Node oParentNode = null;
	
	protected FormatTag oFormatTag = null;
	protected ICommunicationProcedure[] aoCP = null;
	protected ISequentialThread[] aoST = null;
	
	protected FunctionItem oFunctionDef = null;
	
	/**
	 * 
	 */
	public ImperativeNode()
	{
	}
	
	/**
	 * @param poParentNode
	 */
	public ImperativeNode(Node poParentNode)
	{
		this.oParentNode = poParentNode;
	}

	public ImperativeNode
	(
		FormatTag poFormatTag,
		ISequentialThread[] paoST,
		ICommunicationProcedure[] paoCP,
		Node poParentNode
	)
	{
		this.oFormatTag = poFormatTag;
		this.aoST = paoST;
		this.aoCP = paoCP;
		this.oParentNode = poParentNode;
	}

	public ImperativeNode
	(
		FormatTag poFormatTag,
		ISequentialThread[] paoST,
		ICommunicationProcedure[] paoCP
	)
	{
		this(poFormatTag, paoST, paoCP, null);
	}
	
	/**
	 * @see gipsy.GIPC.util.Node#dump(java.lang.String)
	 */
	public void dump(String pstrPrefix)
	{
		System.out.println(pstrPrefix + this);
	}
	
	/**
	 * @see gipsy.GIPC.util.Node#jjtAddChild(gipsy.GIPC.util.Node, int)
	 */
	public void jjtAddChild(Node poChildNode, int piIndex)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * @see gipsy.GIPC.util.Node#jjtClose()
	 */
	public void jjtClose()
	{
	}

	/**
	 * This node never has children.
	 * @return always null
	 * @see gipsy.GIPC.util.Node#jjtGetChild(int)
	 */
	public Node jjtGetChild(int piIndex)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This node never has children.
	 * @return always 0
	 * @see gipsy.GIPC.util.Node#jjtGetNumChildren()
	 */
	public int jjtGetNumChildren()
	{
		return 0;
	}

	/**
	 * @return parent Node reference
	 * @see gipsy.GIPC.util.Node#jjtGetParent()
	 */
	public Node jjtGetParent()
	{
		return this.oParentNode;
	}

	/**
	 * @see gipsy.GIPC.util.Node#jjtOpen()
	 */
	public void jjtOpen()
	{
		// TODO Auto-generated method stub
	}

	/**
	 * @see gipsy.GIPC.util.Node#jjtSetParent(gipsy.GIPC.util.Node)
	 */
	public void jjtSetParent(Node poParentNode)
	{
		this.oParentNode = poParentNode;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return
			this.oFormatTag + "\n" +
			this.aoCP + "\n" +
			this.aoCP + "\n";
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}
}

// EOF
