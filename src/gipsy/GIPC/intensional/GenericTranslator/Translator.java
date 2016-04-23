package gipsy.GIPC.intensional.GenericTranslator;

import gipsy.GIPC.DFG.DFGException;
import gipsy.GIPC.DFG.DFGGenerator.DFGTranCodeGenerator;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.GIPC.intensional.SIPL.IndexicalLucid.IndexicalLucidParser;
import gipsy.GIPC.util.ParseException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 * Main() program for translator generation.
 *
 * $Id: Translator.java,v 1.21 2005/06/21 11:59:52 mokhov Exp $
 *
 * @author Aihua Wu (original coding)
 * @author Serguei Mokhov (refactoring)
 *
 * @version $Revision: 1.21 $
 * @since Jan, 2002
 * @see TranslationLexer implements T
 * @see TranslationParser implements T, G, ParserConstants, ParserTreeConstants
 */
public class Translator
implements G
{
	static boolean bFileEnd = false;

	static boolean bRight = true;

	static int iErrorCount = 0;

	static int iWarningCount = 0;

	public static void main(String args[])
	throws IOException, DFGException
	{
		InputStream source;
		PrintStream errFile;
		TranslationLexer FileLex;
		TranslationParser FileSyn;

		Hashtable oDictionaryHashtable = new Hashtable();

		String s = args[0];

		File sourceFile = new File(s);

		if(s.indexOf('.') == -1)
		{
			System.err.println("Invalid input.");
			System.exit(1);
		}

		String suffix = s.substring(s.lastIndexOf('.'));

		if(!TranslationLexer.isReserved(suffix, RULE_FILE_EXTENSION))
		{
			System.err.println
			(
				"The Source File must have the "
				+ RULE_FILE_EXTENSION + " extension."
			);

			System.exit(1);
		}

		if(!sourceFile.exists())
		{
			System.err.println("File not found.");
			System.exit(1);
		}

		source = new BufferedInputStream(new FileInputStream(sourceFile));
		String errfilename = (s.substring(0, s.indexOf('.'))).concat(ERROR_FILE_EXTENSION);

		//String tokenfilename=(s.substring(0,s.indexOf('.'))).concat(TOKEN_FILE_EXTENSION);
		//String Productfilename=(s.substring(0,s.indexOf('.'))).concat(PRODUCTION_FILE_EXTENSION);

		errFile = new PrintStream(new FileOutputStream(errfilename));

		//TokenFile=new PrintStream(new FileOutputStream(tokenfilename));
		//ProductFile=new PrintStream(new FileOutputStream(Productfilename));

		FileLex = new TranslationLexer(source, errFile);
		FileSyn = new TranslationParser(FileLex, errFile);

		oDictionaryHashtable = FileSyn.Parse();

		System.out.println
		(
			"There are " + oDictionaryHashtable.size()
			+ " operators in the HashTable."
		);

		errFile.println("total " + iErrorCount + " errors.");
		System.err.println("total " + iErrorCount + " errors.");
		errFile.println("total " + iWarningCount + " warnings.");
		System.err.println("total " + iWarningCount + " warnings.");

		String ekey;
		TranslationItem eItem;

		for(Enumeration e = oDictionaryHashtable.keys(); e.hasMoreElements(); )
		{
			ekey = e.nextElement().toString();
			eItem = (TranslationItem)oDictionaryHashtable.get(ekey);

			//     System.out.println(eItem.TranName);
			DFGTranCodeGenerator gt1 = new DFGTranCodeGenerator();
			gt1.generateDFG(eItem.TranEntry, 1, eItem.TranName);

			System.out.println("----------- translation tree -----------");
			eItem.TranEntry.dump(" ");
		}
	}

	/**
	 * The following programs realize the translation only from Indexical Lucid
	 * to GIPL. It is not a general method for translation. In our project, this
	 * method will not be adopted.
	 *
	 * @author Chun Lei Ren
	 */
	public static class SimpleTranslator
	{
		public static void translate(SimpleNode simpleNode)
		{

		  if (simpleNode.children !=null) {
			 for (int i = 0; i < simpleNode.children.length; i++) {
			  SimpleNode node = (SimpleNode) simpleNode.children[i];
			  switch (node.id) {
				case 21:  // asa
				SimpleNode n4 = (SimpleNode) simpleNode.children[i];
				SimpleNode node4 = new SimpleNode();
				node4 = asa((SimpleNode) n4.children[0], (SimpleNode) n4.children[1], (SimpleNode) n4.children[2]);
				simpleNode.jjtAddChild(node4, i);
				node4.jjtSetParent(simpleNode);
				translate(node4);
				break;

				case 18:  // fby
				SimpleNode n5 = (SimpleNode) simpleNode.children[i];
				SimpleNode node5 = new SimpleNode();
				node5 = fby((SimpleNode) n5.children[0], (SimpleNode) n5.children[1], (SimpleNode) n5.children[2]);
				simpleNode.jjtAddChild(node5, i);
				node5.jjtSetParent(simpleNode);
				translate(node5);
				break;

				case 22:  // upon
				SimpleNode n6 = (SimpleNode) simpleNode.children[i];
				SimpleNode node6 = new SimpleNode();
				node6 = upon((SimpleNode) n6.children[0], (SimpleNode) n6.children[1], (SimpleNode) n6.children[2]);
				simpleNode.jjtAddChild(node6, i);
				node6.jjtSetParent(simpleNode);
				translate(node6);
				break;

				case 20:  // wvr
				SimpleNode n7 = (SimpleNode) simpleNode.children[i];
				SimpleNode node7 = new SimpleNode();
				node7 = wvr((SimpleNode) n7.children[0], (SimpleNode) n7.children[1], (SimpleNode) n7.children[2]);
				simpleNode.jjtAddChild(node7, i);
				node7.jjtSetParent(simpleNode);
				translate(node7);
				break;

				case 29:  // first
				SimpleNode n1 = (SimpleNode) simpleNode.children[i];
				SimpleNode node1 = new SimpleNode();
				node1 = first((SimpleNode) n1.children[0], (SimpleNode) n1.children[1]);
				simpleNode.jjtAddChild(node1, i);
				node1.jjtSetParent(simpleNode);
				translate(node1);
				break;

				case 30:  // next
				SimpleNode n2 = (SimpleNode) simpleNode.children[i];
				SimpleNode node2 = new SimpleNode();
				node2 = next((SimpleNode) n2.children[0], (SimpleNode) n2.children[1]);
				simpleNode.jjtAddChild(node2, i);
				node2.jjtSetParent(simpleNode);
				translate(node2);
				break;

				case 31:  // prev
				SimpleNode n3 = (SimpleNode) simpleNode.children[i];
				SimpleNode node3 = new SimpleNode();
				node3 = prev((SimpleNode) n3.children[0], (SimpleNode) n3.children[1]);
				simpleNode.jjtAddChild(node3, i);
				node3.jjtSetParent(simpleNode);
				translate(node3);
				break;

				default:
				translate(node);
			}

			}
		  }

		}


	public static SimpleNode first(SimpleNode node1, SimpleNode node2) // id, dim
	{

		SimpleNode at=new SimpleNode(15);      // create a new 'at' node
		SimpleNode const0=new SimpleNode(35);  // create a new '0' node (integer)
		const0.setImage("0");
		const0.setType(0);		       // set value of '0' node

		node1.jjtSetParent(at);
		node2.jjtSetParent(at);
		const0.jjtSetParent(at);
		at.jjtAddChild(node1, 0);
		at.jjtAddChild(node2, 1);
		at.jjtAddChild(const0, 2);

		return at;

	}

	public static SimpleNode next(SimpleNode node1, SimpleNode node2)  // node1:id, node2:dim
	{
		SimpleNode sn = new SimpleNode();

		SimpleNode hash=new SimpleNode(3);   // create a new 'hash' node
		SimpleNode add=new SimpleNode(6);     // create a new 'add' node
		SimpleNode const1=new SimpleNode(35); // create a new '1' node
		SimpleNode dim1=new SimpleNode();
		dim1.copyNode(node2);	      // make a copy of node2 (dimension)
		const1.setImage("1");
		const1.setType(0);		      // set value of '1' node
		SimpleNode at=new SimpleNode(15);     // create a new 'at' node
	try {
		sn = IndexicalLucidParser.at(IndexicalLucidParser.addOp(const1, add, IndexicalLucidParser.hash(dim1, hash)), node2, at, node1);
	  } catch (ParseException e) { }

		return sn;

	}

	public static SimpleNode prev(SimpleNode node1, SimpleNode node2)  // node1:id, node2:dim
	{
		SimpleNode sn = new SimpleNode();

		SimpleNode hash=new SimpleNode(3);   // create a new 'hash' node
		SimpleNode min=new SimpleNode(7);     // create a new 'minus' node
		SimpleNode const1=new SimpleNode(35); // create a new '1' node
		SimpleNode dim1=new SimpleNode();
		dim1.copyNode(node2);	      // make a copy of node2 (dimension)
		const1.setImage("1");
		const1.setType(0);		      // set value of '1' node
		SimpleNode at=new SimpleNode(15);     // create a new 'at' node
	 try {
		sn = IndexicalLucidParser.at(IndexicalLucidParser.addOp(const1, min, IndexicalLucidParser.hash(dim1, hash)), node2, at, node1);
	 } catch (ParseException e) { }
		return sn;
	}

	public static SimpleNode ifClause(SimpleNode node1, SimpleNode node2, SimpleNode node3, SimpleNode node4)  // this is for sipl, gipl doesn't need this function
	{                                // node1-4: if, 1st, 2nd, 3rd paras

		node2.jjtSetParent(node1);
		node3.jjtSetParent(node1);
		node4.jjtSetParent(node1);
		node1.jjtAddChild(node2, 0);
		node1.jjtAddChild(node3, 1);
		node1.jjtAddChild(node4, 2);
		return node1;
	}

	public static SimpleNode fby(SimpleNode node1, SimpleNode node2, SimpleNode node3)  // node1:id-right, node2:dim, node3:id-left
	{
		SimpleNode sn = new SimpleNode();
		/**************************************************
		 X fby.d Y = if #.d <= 0 then X else Y @.d (#.d-1)
		***************************************************/

		SimpleNode ifNode=new SimpleNode(2);   // create a new 'if-clause' node
		SimpleNode hash1=new SimpleNode(3);   // create a new 'hash' node
		SimpleNode hash2=new SimpleNode(3);   // create another new 'hash' node
		SimpleNode at=new SimpleNode(15);   // create a new 'at' node
		SimpleNode min=new SimpleNode(7);     // create a new 'minus' node
		SimpleNode lessEqual=new SimpleNode(12);   // create another new '<=' node
		SimpleNode const0=new SimpleNode(35); // create a new '0' node
		SimpleNode const1=new SimpleNode(35); // create a new '1' node
		SimpleNode dim1=new SimpleNode();
		dim1.copyNode(node2);	      // make a copy of node2 (dimension)
		SimpleNode dim2=new SimpleNode();
		dim2.copyNode(node2);	      // make another copy of node2 (dimension)
		const0.setImage("0");
		const0.setType(0);			// set value of '0' node
		const1.setImage("1");
		const1.setType(0);		      // set value of '1' node
	 try {
		sn = ifClause(ifNode, IndexicalLucidParser.relOp(const0, lessEqual, IndexicalLucidParser.hash(node2,hash1)), node3, IndexicalLucidParser.at(IndexicalLucidParser.addOp(const1, min, IndexicalLucidParser.hash(dim2, hash2)), dim1, at, node1));
	 } catch (ParseException e) { }

	   return sn;
	}

	public static SimpleNode makeQlist(SimpleNode child, SimpleNode parent)
	{

		child.jjtSetParent(parent);
		if (parent.children!=null)
			parent.jjtAddChild(child, parent.children.length);
		else
			parent.jjtAddChild(child, 0);
		return parent;

	}

	public static SimpleNode assign(SimpleNode node1, SimpleNode node2, SimpleNode node3)  // left, =, right
	{

		node1.jjtSetParent(node2);
		node3.jjtSetParent(node2);
		node2.jjtAddChild(node1, 0);
		node2.jjtAddChild(node3, 1);

		return node2;

	}

	public static SimpleNode wvr(SimpleNode node1, SimpleNode node2, SimpleNode node3)  // node1:id-right, node2:dim, node3:id-left
	{
		SimpleNode sn = new SimpleNode();
		/****************************************
		 X wvr.d Y = X @.d T
			where
			T = U fby.d U @.d (T+1);
			U = if Y then #.d else next.d U;
			end
		*****************************************/

		SimpleNode where=new SimpleNode(16);   // create a 'where' node
		SimpleNode ifNode=new SimpleNode(2);   // create an 'if-clause' node
		SimpleNode hash1=new SimpleNode(3);   // create a 'hash' node
		SimpleNode at1=new SimpleNode(15);
		SimpleNode at2=new SimpleNode(15);   // create 2 'at' nodes
		SimpleNode add=new SimpleNode(6);     // create an 'add' node
		SimpleNode const1=new SimpleNode(35); // create a new '1' node
		const1.setImage("1");
		const1.setType(0);		      // set value of '1' node

		SimpleNode dim1=new SimpleNode();
		dim1.copyNode(node2);
		SimpleNode dim2=new SimpleNode();
		dim2.copyNode(node2);
		SimpleNode dim3=new SimpleNode();
		dim3.copyNode(node2);
		SimpleNode dim4=new SimpleNode();
		dim4.copyNode(node2);	       // make 4 copies of node2 (dimension)


		SimpleNode t1=new SimpleNode(34);   // create 3 intermediate id nodes: T
		t1.setImage("t");
		SimpleNode t2=new SimpleNode();
		t2.copyNode(t1);
		SimpleNode t3=new SimpleNode();
		t3.copyNode(t1);

		SimpleNode u1=new SimpleNode(34);   // create 4 intermediate id nodes: U
		u1.setImage("u");
		SimpleNode u2=new SimpleNode();
		u2.copyNode(u1);
		SimpleNode u3=new SimpleNode();
		u3.copyNode(u1);
		SimpleNode u4=new SimpleNode();
		u4.copyNode(u1);

		SimpleNode assign1=new SimpleNode(33);   // create 2 'assign' nodes
		SimpleNode assign2=new SimpleNode(33);

		SimpleNode qlist=new SimpleNode(17);
	 try {
			makeQlist(assign(t2, assign1, IndexicalLucidParser.at(IndexicalLucidParser.addOp(const1, add, t3), dim2, at2, fby(u2, dim1, u1))), qlist);
		makeQlist(assign(u3, assign2, ifClause(ifNode, node1, IndexicalLucidParser.hash(dim3,hash1), next(u4, dim4))), qlist);
		// need 'QList' reference, to child T and U

		sn = IndexicalLucidParser.where(qlist, where, IndexicalLucidParser.at(t1, node2, at1, node3));
	 } catch (ParseException e) { }

		return sn;
	}

	public static SimpleNode asa(SimpleNode node1, SimpleNode node2, SimpleNode node3)  // node1:id-right, node2:dim, node3:id-left
	{

		/****************************************
		  X aaa.d Y = first.d (X wvr.d Y);
		*****************************************/


		SimpleNode dim1=new SimpleNode();
		dim1.copyNode(node2);	      // make a copy of node2 (dimension)


		return first(wvr(node1, dim1, node3), node2);
	}

	public static SimpleNode upon(SimpleNode node1, SimpleNode node2, SimpleNode node3)  // node1:id-right, node2:dim, node3:id-left
	{
		SimpleNode sn = new SimpleNode();
		/****************************************
		  X upon.d Y = X @.d W
			 where
			W = 0 fby.d if Y then (W+1) else W;
			 end;
		*****************************************/


		SimpleNode where=new SimpleNode(16);   // create a 'where' node
		SimpleNode ifNode=new SimpleNode(2);   // create a new 'if' node
		SimpleNode at=new SimpleNode(15);   // create a new 'at' node
		SimpleNode const0=new SimpleNode(35); // create a new '0' node
		SimpleNode const1=new SimpleNode(35); // create a new '1' node
		const0.setImage("0");
		const0.setType(0);			// set value of '0' node
		const1.setImage("1");
		const1.setType(0);		      // set value of '1' node
		SimpleNode add=new SimpleNode(6);     // create a new 'add' node
		SimpleNode assign=new SimpleNode(33);     // create a new 'assign' node
		SimpleNode dim1=new SimpleNode();
		dim1.copyNode(node2);	      // make a copy of node2 (dimension)
		SimpleNode w1=new SimpleNode(34);   // create 4 intermediate id nodes: U
		w1.setImage("w");
		SimpleNode w2=new SimpleNode();
		w2.copyNode(w1);
		SimpleNode w3=new SimpleNode();
		w3.copyNode(w1);
		SimpleNode w4=new SimpleNode();
		w4.copyNode(w1);			// create 4 intermediate id nodes: W

		SimpleNode qlist=new SimpleNode(17);

	  try {
		makeQlist(fby(ifClause(ifNode, node1, IndexicalLucidParser.addOp(const1, add, w2), w3), dim1, assign(w4, assign, const0)), qlist);

		sn = IndexicalLucidParser.where(qlist, where, IndexicalLucidParser.at(w1, node2, at, node3));
	 } catch (ParseException e) { }
		return sn;
	}

	}

}

// EOF
