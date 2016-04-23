package gipsy.GIPC.DFG.DFGGenerator;

import gipsy.GIPC.DFG.DFGException;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.GIPC.intensional.GIPL.GIPLParserTreeConstants;
import gipsy.GIPC.intensional.SIPL.IndexicalLucid.IndexicalLucidParserTreeConstants;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

import marf.util.Debug;


/**
 * This class is for DFG code generating.
 * 
 * @version 1.0, by GIPSY project team.
 * @author <a href="mailto:dingyimin@zworg.com">Yimin Ding </a>
 */
public class DFGCodeGenerator
{
	private SimpleNode snode; // my parse tree

	private SimpleNode nbak; // backup subtree for sgraph

	private RandomAccessFile rf1; // output file handle

	private String outfilename; // output file name

	private int ncluster = 0; // count cluster

	private int nucluster = 0; // clean cluster "}"

	private int nchild = 0; // child number --> def the num on each line

	Stack<String> SKrankmax = new Stack<String>(); // some node for max rank together

	Stack<String> SKrankmin = new Stack<String>(); // some node for min rank together

	private String lname = "0"; // "Y X" full node name with scope by space ; start from "0"

	private String sname = ""; // "X" short node name

	DFGSubtree sbt = new DFGSubtree();

	private int scope = 0;

	private int scope0 = 0;

	private String lname0 = "0";

	private Hashtable<String, Integer> ntable = new Hashtable<String, Integer>(); // store name list-("name",Int)

	private Hashtable<String, String> dtable = new Hashtable<String, String>(); // store dimension name ("name","status")

	private int nterm = 0;

	Stack<String> SKtoname = new Stack<String>();

	private String toname = ""; // a23 when write code, hold non-terminal node name of last node

	private int flagassign = 0; // if 1 then left assign

	private String flagdim = ""; // if dimension begin then = dim or dimension or paras

	private int funnum = 1;

	private int parserType = 0;

	private int flag_d = 0; // in cyc1 : if dimension then 1

	private int flagstart = 0; // if this object is "start" than =1, if function =0 or 2(drawn start part)

	public DFGCodeGenerator()
	{
	}

	public String getSname(String pstrLname)
	{
		int ind = pstrLname.lastIndexOf(' ');
		if (ind != -1)
		{
			return pstrLname.substring(ind + 1);
		}
		else
		{
			return " ";
		}
	}

	public String getPname(String pstrLname)
	{
		int ind = pstrLname.lastIndexOf(' ');
		if (ind != -1)
		{
			return pstrLname.substring(0, ind);
		}
		else
		{
			return " ";
		}
	}

	public void drawStart(SimpleNode n)
	{
		try
		{
			rf1.writeBytes("digraph G {\n");
			rf1.writeBytes("rankdir = LR;\n");
			rf1.writeBytes("edge [arrowsize=0.7];\n");
			rf1.writeBytes("edge [arrowtail=\"none\"];\n\n");
			nterm = sbt.reqN();
			toname = "aa" + nterm;
			rf1.writeBytes(toname + " [shape=house,orientation=270,label=\"\"];\n");
			SKrankmax.push(toname);
			toname = "w" + toname;
		}
		catch(Exception e)
		{
			Debug.debug(this.getClass(), e.toString());
		}

		drawRankgroup();
		try
		{
			rf1.writeBytes("\nsubgraph cluster" + itos(ncluster) + "{\n");
			rf1.writeBytes("node [shape=box];\n");
			if (flagstart == 1)
			{
				rf1.writeBytes("label = \"START\";\n");
			}
			else
			{
				rf1.writeBytes("label = \"FUNCTION\";\n");
			}
			rf1.writeBytes("color = hot_pink;\n");
			rf1.writeBytes("fontcolor = hot_pink;\n\n");
		}
		catch(Exception e)
		{
			Debug.debug(this.getClass(), e.toString());
		}
	}

	public void drawWhere()
	{
		Debug.debug(this.getClass(), "drawWhere() not implemented");
	}

	public void drawAssign()
	{
		drawRankgroup();
		try
		{
			ncluster = ncluster + 1; // new sub cluster
			nucluster = nucluster + 1;
			this.rf1.writeBytes("\nsubgraph cluster" + itos(ncluster) + "{\n");
			this.rf1.writeBytes("node [shape=box];\n\n");
		}
		catch(Exception e)
		{
			Debug.debug(this.getClass(), e.toString());
		}
	}

	public void drawAssignLable(String image, String outpoint)
	{
		try
		{
			rf1.writeBytes("label = \"" + image + "\";\n");
			rf1.writeBytes("color = hot_pink;\n");
			rf1.writeBytes("fontcolor = hot_pink;\n");
			if (flagstart == 2)
			{
				flagstart = 3;
			}
			else
			{
				toname = outpoint;
				SKrankmax.push(toname);
				rf1.writeBytes(outpoint + " [shape=point, label=\"\"];\n\n");
				toname = "w" + toname;
			}
		}
		catch(Exception e)
		{
			Debug.debug(this.getClass(), e.toString());
		}
	}

	public void drawOperator(SimpleNode n, String oper, String filename1)
	{
		try
		{
			String popname = "";
			if (filename1 != null)
			{
				popname = ", pop=\"" + filename1 + "\"";
			}
			String tonamebk = toname;
			nterm = sbt.reqN();
			toname = "op" + nterm;
			rf1.writeBytes("\n" + toname + " [label=\"" + oper + "\"" + popname + "];\n");
			if (!tonamebk.substring(0, 1).equals("w"))
			{
				rf1.writeBytes(toname + " ->" + tonamebk + " [headlabel=\"" + nchild + "\"];\n");
			}
			else
			{
				rf1.writeBytes(toname + " ->" + tonamebk.substring(1) + ";\n");
			}
		}
		catch(Exception e)
		{
			Debug.debug(this.getClass(), e.toString());
		}
	}

	public void drawVar(String iname, String outpoint)
	{
		try
		{
			if (!toname.substring(0, 1).equals("w"))
			{
				rf1.writeBytes(outpoint + " -> " + toname + " [headlabel=\"" + nchild + "\"];\n");
			}
			else
			{
				rf1.writeBytes(outpoint + " -> " + toname.substring(1) + ";\n");
			}
			//         if(dtable.containsKey(iname))
			if (sbt.findVar(iname))
			{ // if it is a dim
				SKrankmin.push(outpoint);
			}
		}
		catch(Exception e)
		{
			Debug.debug(this.getClass(), e.toString());
		}
	}

	public void drawDIM(String fl, int ndim2)
	{
		//fl:from lable ndim2: nterm
		try
		{
			rf1.writeBytes("aa" + itos(ndim2) + " [label=\"" + fl + "\", shape=ellipse];\n");
			//  rf1.writeBytes(outpoint+" [shape=point, label=\"\"];\n\n");
		}
		catch(Exception e)
		{
			Debug.debug(this.getClass(), e.toString());
		}
		if (flagstart != 1)
		{
			SKrankmin.push("aa" + itos(ndim2));
		}
	}

	public void drawTerm2(String nimage)
	{
		////////////////////////////
		nterm = sbt.reqN();
		SKrankmin.push("aa" + itos(nterm));
		drawTerm("aa" + itos(nterm), nimage, toname, itos(nchild));
	}

	public void drawTerm(String fn, String fl, String tn, String hl)
	{
		//fn: from name fl:from lable tn: to name hl:head lable
		try
		{
			if (fl != "")
			{
				rf1.writeBytes(fn + " [label=\"" + fl + "\"];\n");
			}
			if (!tn.substring(0, 1).equals("w"))
			{
				rf1.writeBytes(fn + " -> " + tn + " [headlabel=\"" + hl + "\"];\n");
			}
			else
			{
				rf1.writeBytes(fn + " -> " + tn.substring(1) + ";\n");
			}
		}
		catch(Exception e)
		{
			Debug.debug(this.getClass(), e.toString());
		}
	}

	private String itos(int num)
	{
		String nstring = String.valueOf(num);
		return nstring;
	}

	public void drawRankgroup()
	{
		try
		{
			if (!SKrankmax.empty())
			{
				rf1.writeBytes("{rank=\"max\";");
				while(!SKrankmax.empty())
				{
					rf1.writeBytes(SKrankmax.pop().toString() + ";");
				}
				rf1.writeBytes("}\n\n");
			}
			if (!SKrankmin.empty())
			{
				rf1.writeBytes("{rank=\"min\";");
				while(!SKrankmin.empty())
				{
					rf1.writeBytes(SKrankmin.pop().toString() + ";");
				}
				rf1.writeBytes("}\n\n");
			}
		}
		catch(Exception e)
		{
			Debug.debug(this.getClass(), e.toString());
		}
	}

	public void drawDFG()
	throws DFGException
	{
		// write down start code
		drawDFG(snode);
	}

	public int drawDFG(SimpleNode n)
	throws DFGException
	{
		////////////////// every node
		String treecon;
		if (parserType == 0)
		{
			treecon = GIPLParserTreeConstants.jjtNodeName[n.id];
		}
		else
		{
			treecon = IndexicalLucidParserTreeConstants.jjtNodeName[n.id];
		}

		if (treecon == "START")
		{ // start
			drawStart(n);
		}
		else
		{
			if (flagdim != "" && treecon == "ID")
			{ // is dimesion var
				//           flagdim=0;
				int nd = sbt.accVar(n.getImage(), "L");
				drawDIM(n.getImage(), nd); // add a lable
				if (flagdim == "DIM" && flagstart == 1)
				{ // dim in function in start
					int nv;
					nv = sbt.accVar(n.getImage(), "R");
					drawVar(n.getImage(), "aa" + itos(nv)); // add a direction
				}
			}
			else
			{
				flagdim = "";
				if (treecon == "DIMENSION" || treecon == "DIM" || treecon == "PARAS")
				{ // start dimesion defin
					flagdim = treecon;
				}
				else
				{
					if (treecon == "WHERE")
					{ // node is WHERE
						drawWhere();
						scope = scope + 1;
						lname = lname + " " + itos(scope);
					}
					else
					{
						if (treecon == "ASSIGN")
						{ // node is ASSIGN
							flagassign = 1;
							//           drawAssign();
							nbak = n; // backup subtree
						}
						else
						{
							if (n.getImage() != null && treecon != "FUNCTION")
							{ // node is terminal term (N,d,2)
								if (flagassign == 1)
								{ // assign left
									flagassign = 0;
									drawAssign();
									int nl = sbt.accVar(lname + " " + n.getImage(), "L");
									drawAssignLable(n.getImage(), "aa" + itos(nl));
								}
								else
								{
									if (treecon == "ID")
									{ // is id or dimesion at assign right
										int nv;
										if (sbt.findVar(n.getImage()))
										{ // is dim
											nv = sbt.accVar(n.getImage(), "R");
										}
										else
										{ // is id
											nv = sbt.accVar(lname + " " + n.getImage(), "R");
										}
										drawVar(n.getImage(), "aa" + itos(nv)); // add a direction
									}
									else
									{
										drawTerm2(n.getImage());
									}
								}
							}
							else
							{ // node is operator or function
								if (treecon != "FUNCTION")
								{
									drawOperator(n, treecon, null);
								}
								else
								{
									if (flagassign == 1)
									{ // assign left
										flagassign = 0;
										n.id = 27;
										DFGCodeGenerator sgt1 = new DFGCodeGenerator();
										sgt1.generateDFG(nbak, parserType, n.getImage() + ".dot");
										return 1;
										//                  int nl=sbt.accVar(lname+" "+n.getImage(), "L");
										//                  drawAssignLable(n.getImage(), "aa"+itos(nl));
									}
									else
									{
										drawOperator(n, n.getImage(), n.getImage() + ".dot");
									}
								}

							}
						}
					}
				}
			}
		}
		/////////////////
		if (n.children != null)
		{
			// push stack here
			//        SKtoname.push("aa"+itos(nterm));
			SKtoname.push(toname);
			for(int i = 0; i < n.children.length; ++i)
			{ // process all children
				nchild = i + 1;
				SimpleNode n2 = (SimpleNode)n.children[i];
				if (n2 != null)
				{
					if (drawDFG(n2) == 1)
					{ // process each child
						i = n.children.length; // if is function then exit
					}
					String treecon2;
					if (parserType == 0)
					{
						treecon2 = GIPLParserTreeConstants.jjtNodeName[n2.id];
					}
					else
					{
						treecon2 = IndexicalLucidParserTreeConstants.jjtNodeName[n2.id];

					}
					if (treecon2 == "ASSIGN")
					{
						try
						{
							drawRankgroup();
							rf1.writeBytes("}");
							rf1.writeBytes("\n");
							nucluster--;
						}
						catch(Exception e)
						{
							Debug.debug(this.getClass(), e.toString());
						}
					}
				}
			}
			// pop stack here

			toname = SKtoname.pop().toString();
			if (!SKtoname.empty())
			{
				toname = SKtoname.peek().toString();
			}
		}

		if (treecon == "DIMENSION" || treecon == "DIM" || treecon == "PARAS")
		{ // a dim branch end
			flagdim = "";

		}
		if (n.getImage() == getSname(lname))
		{ // a term end
			lname = getPname(lname); // cut a child

		}
		if (treecon == "WHERE")
		{ // a "where" branch end
			// find not be used id -> draw empty box
			String Ename;
			for(Enumeration e = sbt.listKeys(); e.hasMoreElements();)
			{
				Ename = e.nextElement().toString();
				if (getPname(Ename).compareTo(lname) == 0)
				{
					if (!sbt.IsUsed(Ename))
					{
						// not used -> draw empty box
						drawAssign();
						int nl = sbt.accVar(Ename, "L");
						drawAssignLable(getSname(Ename), "aa" + itos(nl));
					}
				}
			}
			lname = getPname(lname); // cut a child scope
		}
		return 0;
	}

	public void drawDFGclose()
	throws DFGException
	{

		drawRankgroup();
		try
		{
			for(int i = 0; i <= nucluster + 1; i++)
			{
				rf1.writeBytes("}");
				//      rf1.writeChar(13);
			}
			rf1.writeBytes("\n");
			//    rf1.writeBytes("\f");
		}
		catch(Exception e)
		{
			Debug.debug(e.getClass(), e.getMessage());
		}

		try
		{
			rf1.close();
		}
		catch(IOException e)
		{
			System.err.println(e.toString());
			//System.exit(1);
			throw new DFGException(e);
		}
	}

	/**
	 * Generate a dim index table.
	 */
	public void generateDimensionIndexTable()
	{ 
		generateDimensionIndexTable(snode);
	}

	public void generateDimensionIndexTable(SimpleNode n)
	{
		////////////////// every node
		String treecon;
		if(parserType == 0)
		{
			treecon = GIPLParserTreeConstants.jjtNodeName[n.id];
		}
		else
		{
			treecon = IndexicalLucidParserTreeConstants.jjtNodeName[n.id];
		}

		if(treecon == "WHERE")
		{ // node is WHERE
			scope0 = scope0 + 1;
			lname0 = lname0 + " " + itos(scope0);
		}

		if (flagstart == 0)
		{
			drawStart(n);
			flagstart = 2;
		}

		/////////////////
		if (flag_d == 1)
		{
			// dtable.put(n.getImage(),sbt.reqN());
			if (treecon == "ID")
			{
				int nd = sbt.accVar(n.getImage(), "L");
				if (flagstart != 1)
				{
					drawDIM(n.getImage(), nd); // add a lable
				}
			}
		}

		if (treecon == "DIMENSION" || treecon == "DIM" || (treecon == "PARAS" && flagstart != 1))
		{
			flag_d = 1;
		}
		if (n.children != null)
		{
			for(int i = 0; i < n.children.length; ++i)
			{ // process all children
				nchild = i + 1;
				SimpleNode n2 = (SimpleNode)n.children[i];
				if (n2 != null)
				{
					// save (first assign left var) to table
					if (nchild == 1 && treecon == "ASSIGN")
					{
						sbt.accVar(lname0 + " " + n2.getImage(), "L");
					}
					generateDimensionIndexTable(n2); // process each child
				}
			}
		}
		if (treecon == "DIMENSION" || treecon == "DIM" || (treecon == "PARAS" && flagstart != 1))
		{
			flag_d = 0;

		}
		if (treecon == "WHERE")
		{ // node is WHERE
			lname0 = getPname(lname0); // cut a child scope
		}
	}

	public void genTableclose()
	{
		drawRankgroup();
	}

	//////////////////////////////////////////////////////////////////////
	public void generateDFG(SimpleNode simplenode, int parserType1, String filename)
	throws DFGException
	{
		snode = simplenode;
		parserType = parserType1;
		
		if(filename == null)
		{
			outfilename = "../tests/lucid/testout1.dot";
			flagstart = 1;
		}
		else
		{
			outfilename = "../tests/lucid/" + filename;
			flagstart = 0;
		}
		
		try
		{
			rf1 = new RandomAccessFile(outfilename, "rw");
		}
		catch(Exception e)
		{
			Debug.debug(this.getClass(), e.toString());
		}

		generateDimensionIndexTable();
		genTableclose();
		drawDFG();
		drawDFGclose();
	}
}

// EOF
