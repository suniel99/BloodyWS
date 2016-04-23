package gipsy.GIPC.intensional.GenericTranslator;

import gipsy.GIPC.intensional.SimpleNode;
import gipsy.GIPC.intensional.GIPL.GIPLParserConstants;
import gipsy.GIPC.intensional.GIPL.GIPLParserTreeConstants;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Stack;


/**
 * <p>Syntactic Analyzer for Translation Rules. 
 * Realizes the translation from any SIPL to GIPL automatically.<p>
 * 
 * <p>Users can provide different translation rules for new operators. 
 * TranslationParser will according to these rules, generate the translation 
 * tree for each operator. The tree will be used by method AutoTran() which 
 * translates SIPL to GIPL.</p>
 *
 * @author Aihua Wu
 * @author Serguei Mokhov
 * 
 * @version 2.0
 * @since June, 2002
 * 
 * @see T
 * @see G
 * @see GIPLParserConstants
 * @see GIPLParserTreeConstants
 */
class TranslationParser
implements T, G, GIPLParserConstants, GIPLParserTreeConstants
{
	private int iTerminalNum = 0;

	protected int iSymbolTableLine, iSymbolTableColumn;

	private String strTemp = "";

	private Stack SytacStack = new Stack();

	boolean bErrorsPresent = false;

	private PrintStream errFile, TokenFile, ProductFile;

	private TranslationLexer tempLex, FileLex;

	/**
	 * Stores the operator translation.
	 */
	private Hashtable oTranslationTable = new Hashtable();

	private TranslationItem oSymbol;
	
	/**
	 * Record the current node.
	 */
	private SimpleNode CurrentNode;

	private SimpleNode C0, C1, C2, C3;

	private Stack TranStack = new Stack();

	private Stack TranTemp = new Stack();

	private boolean bIsBrace = false;


	/**
	 * Constructor.
	 *
	 * @param FileLex  the file with translation rules
	 * @param errFile  the file with error messages
	 */
	public TranslationParser(TranslationLexer FileLex, PrintStream errFile)
	{
		this.FileLex = FileLex;
		this.errFile = errFile;
		//this.TokenFile=TokenFile;
		//this.ProductFile=ProductFile;
	}

	/**
	 * All Creat+name() methods creat a new branch of a sub AST of the corresponding operator and connect it with the
	 * original AST. Creats the table for storing translation items.
	 * 
	 * @return the String after the equal in a rule.
	 * @throws IOException
	 */
	private String CreateOpe()
	throws IOException
	{
		/* code generate for creat table */
		String s5 = "";
		String s6;
		boolean skip = false;

		s6 = RULES[SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn]];
		s5 = s6.substring(s6.indexOf('=') + 2);

		s6 = tempLex.Id;
		oSymbol = new TranslationItem(s6, null);
		oTranslationTable.put(oSymbol.TranName, oSymbol);
		CurrentNode = oSymbol.TranEntry;

		return s5;
	}


  /**
   * Creats the branch for if node.
   * 
   * @return the String after the equal in a rule.
   */
	String CreateIf() throws IOException
	{ /* code generate for if */
		String s5 = "";
		String s6;
		boolean skip = false;

		s6 = RULES[SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn]];
		s5 = s6.substring(s6.indexOf('=') + 2);

		C0 = new SimpleNode(JJTIF); // "if" node
		if (CurrentNode == null)
		{
			C0.parent = null;
			oSymbol.TranEntry = C0;
			oTranslationTable.put(oSymbol.TranName, oSymbol);
			CurrentNode = C0;
		}
		else
		{
			if (!TranStack.empty())
			{
				C1 = (SimpleNode)TranStack.pop();
				C0.parent = C1;
				C1.jjtAddChild(C0, 1);
				CurrentNode = C0;
			}
		}

		return s5;
	}

  /**
   * Creats the branch for # node.
   * 
   * @return the String after the equal in a rule.
   */  
  String CreateHash() throws IOException
	{ /* code generate for # */
		String s5 = "";
		String s6;
		boolean skip = false;

		s6 = RULES[SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn]];
		s5 = s6.substring(s6.indexOf('=') + 2);

		C0 = new SimpleNode(JJTHASH); //create # node
		C1 = new SimpleNode(JJTID); //create D node;
		C1.setImage("D");
		C1.parent = C0;
		C0.jjtAddChild(C1, 0);
		if (CurrentNode == null)
		{
			C0.parent = null;
			oSymbol.TranEntry = C0;
			oTranslationTable.put(oSymbol.TranName, oSymbol);
		}
		else
		{
			TranStack.push(C0); //push the "#" node
		}

		return s5;
	}


  /**
   * Creats the branch for @ node.
   * @return the String after the equal in a rule.
   */
	String CreateAt() throws IOException
	{ /* code generate for @ */
		String s5 = "";
		String s6;
		boolean skip = false;

		s6 = RULES[SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn]];
		s5 = s6.substring(s6.indexOf('=') + 2);

		C0 = new SimpleNode(JJTAT); //create @ node
		if (!TranStack.empty())
		{
			C1 = (SimpleNode)TranStack.pop();
		}
		C1.parent = C0;
		C2 = new SimpleNode(JJTID); //create D node
		C2.setImage("D");
		C2.parent = C0;
		C0.jjtAddChild(C1, 0);
		C0.jjtAddChild(C2, 1);

		if ((CurrentNode == null) && (!bIsBrace))
		{
			C0.parent = null;
			oSymbol.TranEntry = C0;
			oTranslationTable.put(oSymbol.TranName, oSymbol);
			CurrentNode = C0;
		}
		else
		{
			TranStack.push(C0); //push the "@" node
		}

		return s5;
	}

	/**
	 * Creats the branch for where node.
	 * 
	 * @return the String after the equal in a rule.
	 */
	String CreateWhere() throws IOException
	{ /* code generate for where */
		String s5 = "";
		String s6;
		int Len = 0;
		boolean skip = false;

		s6 = RULES[SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn]];
		s5 = s6.substring(s6.indexOf('=') + 2);

		if (!TranStack.empty())
		{
			C0 = (SimpleNode)TranStack.pop();
			C0.parent = CurrentNode;
			Len = CurrentNode.children.length;
			CurrentNode.jjtAddChild(C0, Len);
		}
		C0 = new SimpleNode(JJTWHERE); //creat where node
		if (oSymbol.TranEntry.id == JJTWHERE)
		{
			C1 = oSymbol.TranEntry;
			Len = C1.children.length;
			C2 = (SimpleNode)C1.children[Len - 1];
			C2.parent = C0;
			C0.jjtAddChild(C2, 0);
			C0.parent = C1;
			C1.children[Len - 1] = C0;
			CurrentNode = C0;
		}
		else
		{ //put "where" on the top level
			C1 = oSymbol.TranEntry;
			C1.parent = C0;
			C0.jjtAddChild(C1, 0);
			oSymbol.TranEntry = C0;
			oTranslationTable.put(oSymbol.TranName, oSymbol);
			CurrentNode = C0;
		}

		return s5;
	}

	/**
	 * Creats the branch for equal node.
	 * 
	 * @return the String after the equal in a rule.
	 */
	String CreateEquel() throws IOException
	{ /* code generate for == */
		String s5 = "";
		String s6;
		int Len = 0;
		boolean skip = false;

		s6 = RULES[SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn]];
		s5 = s6.substring(s6.indexOf('=') + 2);

		Len = CurrentNode.children.length;
		C0 = new SimpleNode(JJTASSIGN); //= node
		C1 = new SimpleNode(JJTID); //operand
		C1.setImage(tempLex.Id);
		C1.parent = C0;
		C0.jjtAddChild(C1, 0);
		C0.parent = CurrentNode;
		CurrentNode.jjtAddChild(C0, Len);
		TranStack.push(C0);
		CurrentNode = C0; //????

		return s5;
	}

	/**
	 * Creats the branch for +, -, and or node.
	 * 
	 * @return the String after the equal in a rule.
	 */
	String CreateAdd() throws IOException
	{ /* code generate for + */
		String s5 = "";
		String s6;
		boolean skip = false;

		s6 = RULES[SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn]];
		s5 = s6.substring(s6.indexOf('=') + 2);

		if (tempLex.Id.equals("+"))
		{
			C0 = new SimpleNode(JJTADD); //+ node
		}
		else if (tempLex.Id.equals("-"))
		{
			C0 = new SimpleNode(JJTMIN); //- node
		}
		else
		{
			C0 = new SimpleNode(JJTOR); //or node

		}
		C1 = (SimpleNode)TranStack.pop();

		if ((!TranStack.empty()) && (!IsOpe(C1.id)))
		{
			C1 = (SimpleNode)TranStack.pop();
		}
		C1.parent = C0;
		C0.jjtAddChild(C1, 0);
		TranStack.push(C0);

		return s5;
	}

	/**
	 * Creats the branch for relational operator node.
	 * 
	 * @return the String after the equal in a rule.
	 */
	String CreateRel() throws IOException
	{
		/* code generate for logical operator */
		String s5 = "";
		String s6;
		boolean skip = false;

		s6 = RULES[SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn]];
		s5 = s6.substring(s6.indexOf('=') + 2);

		if (tempLex.Id.equals("<"))
		{
			C0 = new SimpleNode(JJTLT); //< node
		}
		else if (tempLex.Id.equals(">"))
		{
			C0 = new SimpleNode(JJTGT); //> node
		}
		else if (tempLex.Id.equals("<="))
		{
			C0 = new SimpleNode(JJTLE); //<= node
		}
		else if (tempLex.Id.equals(">="))
		{
			C0 = new SimpleNode(JJTGE); //>= node
		}
		else if (tempLex.Id.equals("=="))
		{
			C0 = new SimpleNode(JJTEQ); //== node
		}
		else
		{
			C0 = new SimpleNode(JJTNE); //!= node

		}
		C1 = (SimpleNode)TranStack.pop();
		if ((!TranStack.empty()) && (!IsOpe(C1.id)))
		{
			C1 = (SimpleNode)TranStack.pop();
		}
		C1.parent = C0;
		C0.jjtAddChild(C1, 0);
		TranStack.push(C0);

		return s5;
	}

	/**
	 * Creats the branch for *, /, %, and <code>and</code> node.
	 * 
	 * @return the String after the equal in a rule.
	 */
	String CreateMul() throws IOException
	{ /* code generate for */
		String s5 = "";
		String s6;
		boolean skip = false;

		s6 = RULES[SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn]];
		s5 = s6.substring(s6.indexOf('=') + 2);

		if (tempLex.Id.equals("*"))
		{
			C0 = new SimpleNode(JJTTIMES); //* node
		}
		else if (tempLex.Id.equals("/"))
		{
			C0 = new SimpleNode(JJTDIV); /// node
		}
		else if (tempLex.Id.equals("%"))
		{
			C0 = new SimpleNode(JJTMOD); //mod node
		}
		else
		{
			C0 = new SimpleNode(JJTAND); //and node

		}
		C1 = (SimpleNode)TranStack.pop();
		if (!IsOpe(C1.id))
		{
			((SimpleNode)C1.parent).children[1] = C0;
		}
		C1.parent = C0;
		C0.jjtAddChild(C1, 0);
		TranStack.push(C0);

		return s5;
	}

	/**
	 * Creats the branch for postive and negetive operators node.
	 * 
	 * @return the String after the equal in a rule.
	 */
	String CreateSign() throws IOException
	{ /* code generate for */
		String s5 = "";
		String s6;
		boolean skip = false;

		s6 = RULES[SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn]];
		s5 = s6.substring(s6.indexOf('=') + 2);

		if (tempLex.Id.equals("+"))
		{
			C0 = new SimpleNode(JJTPOSI); //'+' node
		}
		else if (tempLex.Id.equals("-"))
		{
			C0 = new SimpleNode(JJTNEGE); //'-' node

		}
		if (!TranStack.empty())
		{
			C1 = (SimpleNode)TranStack.lastElement();
			if (IsOpe(C1.id))
			{ //the factor is the right factor
				C0.parent = C1;
				C1.jjtAddChild(C0, 1);
				if (C1.parent != null)
				{
					TranStack.pop();
				}
				TranStack.push(C0);
			}
			else
			{
				TranStack.push(C0);
			}
		}
		else
		{
			TranStack.push(C0);

		}
		return s5;
	}

	/**
	 * Creats the branch for <code>factor</code> node.
	 * 
	 * @return the String after the equal in a rule.
	 */
	String CreateFac() throws IOException
	{ /* code generate for factor */
		String s5 = "";
		String s6;
		boolean skip = false;

		s6 = RULES[SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn]];
		s5 = s6.substring(s6.indexOf('=') + 2);

		if (tempLex.TokenType == IdType)
		{
			C0 = new SimpleNode(JJTID); //id node
			C0.setImage(tempLex.Id);
		}
		else if (tempLex.TokenType == IntType)
		{
			C0 = new SimpleNode(JJTCONST); //const node
			C0.setImage(Integer.toString(tempLex.IntVal));
			C0.type = 0;
		}
		else if (tempLex.TokenType == RealType)
		{
			C0 = new SimpleNode(JJTCONST); //const node
			C0.setImage(Double.toString(tempLex.RealVal));
			C0.type = 1;
		}
		else if (tempLex.Id.equals("L"))
		{
			C0 = new SimpleNode(JJTID); //id node
			C0.setImage("L");
		}
		else if (tempLex.Id.equals("R"))
		{
			C0 = new SimpleNode(JJTID); //id node
			C0.setImage("R");
		}

		if (tempLex.Id.equals("!"))
		{
			//C0=new SimpleNode(22); //not node
			bIsBrace = true;
			while(!TranStack.empty())
			{
				TranTemp.push(TranStack.pop());
			}
		}
		else if (tempLex.Id.equals("("))
		{
			bIsBrace = true;
			while(!TranStack.empty())
			{
				TranTemp.push(TranStack.pop());
			}
		}
		else
		{
			if (!TranStack.empty())
			{
				C1 = (SimpleNode)TranStack.lastElement();
				if (IsOpe(C1.id))
				{ //the factor is the right factor
					C0.parent = C1;
					C1.jjtAddChild(C0, 1);
					if (C1.parent != null)
					{
						TranStack.pop();
					}
					TranStack.push(C0);
				}
				else if ((C1.id == JJTPOSI) || (C1.id == JJTNEGE))
				{ //is the sign
					C0.parent = C1;
					C1.jjtAddChild(C0, 0);
					if (C1.parent != null)
					{
						TranStack.pop();
					}
					TranStack.push(C0);
				}
				else
				{
					TranStack.push(C0);
				}
			}
			else
			{
				TranStack.push(C0);
			}
		}

		return s5;
	}

	/**
	 * Judges if it is an operator.
	 * 
	 * @param st Integer value of an operand.
	 * @return <code>true</code> it is an operator. <code>false</code> it is not an operator.
	 */
	public boolean IsOpe(int st)
	{
		int OpeNum = 13;
		int Operator[] =
		{
				6, 7, 8, 9, 10, 11, 12, 13, 14, 20, 21, 22, 23
		}; //is +,-,*,/,
		boolean ForT = false;
		for(int i = 0; i < OpeNum; i++)
		{
			if (st == Operator[i])
			{
				ForT = true;
				return (ForT);
			}
		}
		return (ForT);
	}

	/**
	 * All Do+name() methods deal with some medial conditions. After finishing the condition analysis in the if
	 * expression, calls DoCond() method to pop the value in the stack, to connect the current node with the original
	 * AST, to make an integrated AST for the corresponding operator.
	 */
	void DoCond()
	{
		SimpleNode T1;

		T1 = (SimpleNode)TranStack.pop();
		if ((!TranStack.empty()) && (!IsOpe(T1.id)))
		{
			T1 = (SimpleNode)TranStack.pop();
		}
		T1.parent = CurrentNode;
		CurrentNode.jjtAddChild(T1, 0);

		if (!TranStack.empty())
		{
			TranStack.clear();
		}
	}

	/**
	 * Similiar to DoCond() method. Deals with <code>then</code> operation.
	 */
	void DoThen()
	{
		SimpleNode T1;

		T1 = (SimpleNode)TranStack.pop();
		if ((!TranStack.empty()) && (!IsOpe(T1.id)))
		{
			T1 = (SimpleNode)TranStack.pop();
		}
		T1.parent = CurrentNode;
		CurrentNode.jjtAddChild(T1, 1);

		if (!TranStack.empty())
		{
			TranStack.clear();
		}
	}

	/**
	 * Similiar to DoCond() method. Deals with <code>else</code> operation.
	 */
	void DoElse()
	{ //??
		SimpleNode T1;

		T1 = (SimpleNode)TranStack.pop();
		if ((!TranStack.empty()) && (!IsOpe(T1.id)))
		{
			T1 = (SimpleNode)TranStack.pop();
		}
		T1.parent = CurrentNode;
		CurrentNode.jjtAddChild(T1, 2);
		if (CurrentNode.parent != null)
		{
			CurrentNode = (SimpleNode)CurrentNode.parent;

		}
		if (!TranStack.empty())
		{
			TranStack.clear();
		}
	}

	/**
	 * Similiar to DoCond() method. Deals with @ operation.
	 */
	void DoAt()
	{
		SimpleNode T1, T0;
		if (!TranStack.empty())
		{
			T1 = (SimpleNode)TranStack.pop();
			if (!TranStack.empty())
			{
				T0 = (SimpleNode)TranStack.pop();
			}
			else
			{
				T0 = CurrentNode;
			}
			T1.parent = T0;
			T0.jjtAddChild(T1, 2);
			if (T0 != CurrentNode)
			{
				TranStack.push(T0);
			}
		}
	}

	/**
	 * Similiar to DoCond() method. Deals with <code>equal</code> operation.
	 */
	void DoEqual()
	{
		SimpleNode T1, T0;

		if (!TranStack.empty())
		{
			T1 = (SimpleNode)TranStack.pop();
			if ((T1.id == JJTID) || (T1.id == JJTCONST))
			{
				T1 = (SimpleNode)TranStack.pop();
			}
			T0 = (SimpleNode)TranStack.pop();
			T1.parent = T0;
			T0.jjtAddChild(T1, 1);
		}
		if (CurrentNode.parent != null)
		{
			CurrentNode = (SimpleNode)CurrentNode.parent;

		}
		if (!TranStack.empty())
		{
			TranStack.clear();
		}
	}

	/**
	 * Similiar to DoCond() method. Deals with <code>brace</code> operation.
	 */
	void DoBrace()
	{
		SimpleNode T1, T2;

		T1 = (SimpleNode)TranStack.pop();
		if (!TranStack.empty())
		{
			if ((T1.id == JJTID) || (T1.id == JJTCONST))
			{
				T1 = (SimpleNode)TranStack.pop();
			}
			else
			{
				TranStack.clear();
			}
		}
		while(!TranTemp.empty())
		{
			TranStack.push(TranTemp.pop());
		}
		TranStack.push(T1);
		bIsBrace = false;

	}

	/**
	 * Similiar to DoCond() method. Deals with no <code>brace</code> operation.
	 */
	void DoNotBrace()
	{
		SimpleNode T1, T2;

		T1 = (SimpleNode)TranStack.pop();
		if (!TranStack.empty())
		{
			if ((T1.id == JJTID) || (T1.id == JJTCONST))
			{
				T1 = (SimpleNode)TranStack.pop();
			}
			else
			{
				TranStack.clear();
			}
		}
		T2 = new SimpleNode(JJTNOT);
		T1.parent = T2;
		T2.jjtAddChild(T1, 0);
		while(!TranTemp.empty())
		{
			TranStack.push(TranTemp.pop());
		}
		TranStack.push(T2);
		bIsBrace = false;

	}

	/**
	 * Core part of the TranslationParser() method. Builds operators translation table, generates a forest AST for
	 * operators.
	 * 
	 * @return a Hashtable with translation AST for each operator.
	 */
	public Hashtable Parse() throws IOException
	{
		String sub = "";
		String substr = "";
		SytacStack.push("$");
		SytacStack.push("<TranRule>");
		tempLex = new TranslationLexer();
		tempLex = FileLex.NextToken();
		//WriteToken(tempLex);

		while(!SytacStack.empty()) /* &&(tempLex.TokenType!=-2)) */
		{
			strTemp = SytacStack.pop().toString();
			while(strTemp.indexOf('~') == 0)
			{
				if (strTemp.charAt(1) == '1')
				{
					DoCond();
				}
				else if (strTemp.charAt(1) == '2')
				{
					DoThen();
				}
				else if (strTemp.charAt(1) == '3')
				{
					DoElse();
				}
				else if (strTemp.charAt(1) == '4')
				{
					DoAt();
				}
				else if (strTemp.charAt(1) == '5')
				{
					DoEqual();
				}
				else if (strTemp.charAt(1) == '6')
				{
					DoBrace();
				}
				else if (strTemp.charAt(1) == '7')
				{
					DoNotBrace();
				}
				strTemp = SytacStack.pop().toString();
			}
			if (IsTerminal(strTemp))
			{
				if (((tempLex.TokenType == IdType) && (strTemp.toString().equals("Id")))
				|| ((tempLex.TokenType == IntType) && (strTemp.toString().equals("IntType")))
				|| ((tempLex.TokenType == RealType) && (strTemp.toString().equals("RealType")))
				|| (tempLex.Id.toString().equals(strTemp)))
				{
					tempLex = FileLex.NextToken();
					if (tempLex.TokenType == -2)
					{
						tempLex.Id = "$";
						tempLex.Ln = tempLex.Ln - 1;
					}
					//WriteToken(tempLex);
				}
				else
				{
					SkipErrors();
				}
			}
			else
			{
				iSymbolTableLine = LieStep(strTemp);
				if (tempLex.TokenType == IdType)
				{
					iSymbolTableColumn = 24;
				}
				else if (tempLex.TokenType == IntType)
				{
					iSymbolTableColumn = 25;
				}
				else if (tempLex.TokenType == RealType)
				{
					iSymbolTableColumn = 26;
				}
				else
				{
					iSymbolTableColumn = LieTerm(tempLex.Id);
				}
				if ((iSymbolTableColumn >= (TERMINALS.length - 1)) || (iSymbolTableLine >= STEPS.length))
				{
					SkipErrors();
				}
				else
				{
					if (SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn] > 0)
					{
						//System.out.println("Line: "+tempLex.Ln+" "+Rule[SYMBOL_TABLE[SyTableLn][SyTableCo]]);
						//ProductFile.println("Line: "+tempLex.Ln+" "+Rule[SYMBOL_TABLE[SyTableLn][SyTableCo]]);
						sub = RULES[SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn]];

						if (sub.indexOf('~') == 0)
						{
							if (sub.charAt(1) == '0')
							{
								substr = CreateOpe(); //increase an operator
							}
							else if (sub.charAt(1) == '1')
							{
								substr = CreateIf(); /* if statement */
							}
							else if (sub.charAt(1) == '2')
							{
								substr = CreateHash(); /* # statement */
							}
							else if (sub.charAt(1) == '3')
							{
								substr = CreateAt(); /* @ statement */
							}
							else if (sub.charAt(1) == '4')
							{
								substr = CreateWhere(); /* where statement */
							}
							else if (sub.charAt(1) == '5')
							{
								substr = CreateEquel(); /* = statement under where */
							}
							else if (sub.charAt(1) == '6')
							{
								substr = CreateAdd(); /* Addop statement */
							}
							else if (sub.charAt(1) == '7')
							{
								substr = CreateRel(); /* Relop statement */
							}
							else if (sub.charAt(1) == '8')
							{
								substr = CreateFac(); /* Factor statement */
							}
							else if (sub.charAt(1) == '9')
							{
								substr = CreateMul(); /* Mulop statement */
							}
							else if (sub.charAt(1) == 'A')
							{
								substr = CreateSign(); /* sign statement */
							}
						}
						else
						{
							substr = sub.substring(sub.indexOf('=') + 2);
						}
						if (!(substr.toString().equals("empty")))
						{
							InverPush(substr);
						}
					}
					else if (SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn] == 0)
					{
						SkipErrors();
					}
				}
			}
		}

		/*
		 * C0=Setup(); System.out.println("__________________________________"); System.out.println("This is the original AST
		 * with fby!"); System.out.println("__________________________________"); C0.dump(" ");
		 * System.out.println("___________________________"); C1=AutoTran(C0,0); System.out.println("This is the translated
		 * AST!"); System.out.println("___________________________"); C1.dump(" "); System.out.println("There are
		 * "+TranTable.size()+" operators in the HashTable.");
		 */

		return oTranslationTable;

		/*
		 * if (TranTable.containsKey("fby")) { System.out.println("The following is example of operator: 'fby'.");
		 * Sym=(TranslationItem)TranTable.get("fby"); C0=(SimpleNode)Sym.TranEntry; C0.dump(" "); C1=new SimpleNode(C0.id);
		 * C1.getImage()=C0.getImage(); C1.parent=null; if (C0.children!=null) for (int dup=0; dup <C0.children.length;
		 * dup++) Duplicate(C1,(SimpleNode)C0.children[dup],dup); C1.dump(" "); }
		 */
	}

	/**
	 * Judges if a string is terminal.
	 *
	 * @param st the String.
	 * @return <code>true</code> the String is a terminal.
	 *         <code>false</code> the String is not a terminal.
	 */
	public boolean IsTerminal(String st)
	{
		boolean ForT = false;
		for(int i = 0; i < TERMINALS.length; i++)
		{
			if (st.toString().equals(TERMINALS[i]))
			{
				ForT = true;
				iTerminalNum = i;
				return (ForT);
			}
		}
		return (ForT);
	}

	/**
	 * Checks a String's position in STEPS-array.
	 *
	 * @param st the String.
	 * @return  Integer value for the String's position in step-array.
	 */
	public int LieStep(String st)
	{
		int i = 0;
		for(i = 0; i < STEPS.length; i++)
		{
			if (st.toString().equals(STEPS[i]))
			{
				return (i);
			}
		}
		return (i);
	}

	/**
	 * Checks a String's position in TERMINALS-array.
	 *
	 * @param st the String.
	 * @return  Integer value for the String's position in TERMINALS-array.
	 */
	public int LieTerm(String st)
	{
		int i = 0;
		for(i = 0; i < TERMINALS.length; i++)
		{
			if (st.toString().equals(TERMINALS[i]))
			{
				return (i);
			}
		}
		return (i);
	}

	/**
	 * Inverses a String, pushes it on to a syntactic stack.
	 *
	 * @param st the String.
	 */
	public void InverPush(String st)
	{
		String temp[];
		temp = new String[11];
		int j = 0, i = 0;
		while(st.length() > 0)
		{
			temp[j] = st.substring(0, st.indexOf(' '));
			i = temp[j].length() + 1;
			st = st.substring(i);
			j++;
		}
		j = j - 1;
		for(i = j; i >= 0; i--)
		{
			SytacStack.push(temp[j]);
			j--;
		}
	}

	/**
	 * Deals with errors during parsing.
	 */
	public void SkipErrors() throws IOException
	{
		String s1 = "";
		String s2 = "";
		boolean con = true;
		if (tempLex.TokenType != -2)
		{
			System.out.println("Line " + tempLex.Ln + "  Sytactic error: " + tempLex.Id);
			errFile.println("Line " + tempLex.Ln + "  Sytactic error: " + tempLex.Id);
			Translator.iWarningCount++;
		}
		while(con && (tempLex.TokenType != -2))
		{
			tempLex = FileLex.NextToken();
			if (tempLex.TokenType == -2)
			{
				tempLex.Id = "$";
				tempLex.Ln = tempLex.Ln - 1;
			}
			//WriteToken(tempLex);
			if ((IsTerminal(strTemp)) && (tempLex.toString().equals(strTemp)))
			{
				con = false;
			}
			else if (!(IsTerminal(strTemp)))
			{
				iSymbolTableLine = LieStep(strTemp);
				if (tempLex.TokenType == IdType)
				{
					iSymbolTableColumn = 24;
				}
				else if (tempLex.TokenType == IntType)
				{
					iSymbolTableColumn = 25;
				}
				else if (tempLex.TokenType == RealType)
				{
					iSymbolTableColumn = 26;
				}
				else
				{
					iSymbolTableColumn = LieTerm(tempLex.Id);
					/*System.out.println(SyTableLn);
					 System.out.println(SyTableCo);*/
				}
				if ((iSymbolTableColumn < (TERMINALS.length - 1)) && (iSymbolTableLine < STEPS.length))
				{
					if ((SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn] > 0)
					|| (SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn] == -1))
					{
						con = false;
						System.out.println("Line " + tempLex.Ln + "  Parsing resume at: " + tempLex.Id);
						errFile.println("Line " + tempLex.Ln + "  Parsing resume at: " + tempLex.Id);
						errFile.println();
						if (SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn] > 0)
						{
							//System.out.println("Line: "+tempLex.Ln+"  "+Rule[SYMBOL_TABLE[SyTableLn][SyTableCo]]);
							//ProductFile.println("Line: "+tempLex.Ln+"  "+Rule[SYMBOL_TABLE[SyTableLn][SyTableCo]]);
							s1 = RULES[SYMBOL_TABLE[iSymbolTableLine][iSymbolTableColumn]];
							s2 = s1.substring(s1.indexOf('=') + 2);
							if (!(s2.toString().equals("empty")))
							{
								InverPush(s2);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Finishes the translation automatically according to the translation table.
	 * At first, the nest condition is not permitted, the parameter is simple type.
	 *
	 * @param SiplNode  entry SimpleNode of a SIPL AST.
	 * @param Num       Integer value of the number of children
	 * @return          the entry SimpleNode of a GIPL AST translated.
	 * @see Duplicate(SimpleNode root, SimpleNode Ori_tree, int Child_Num)
	 * @see Replace(SimpleNode ReTree, String para)
	 * @see Replace(SimpleNode ReTree, SimpleNode Dim)
	 * @see Replace(SimpleNode ReTree, String para, SimpleNode Dim)
	 * @see Replace(SimpleNode ReTree, String Left, String bRight, SimpleNode Dim)
	 */
	SimpleNode AutoTran(SimpleNode SiplNode, int Num)
	{
		SimpleNode Rp, Rp1, Rp2, Rp3, Rp4;
		TranslationItem HT = new TranslationItem();
		int Child_Num = 0;

		if ((SiplNode.id > 28) || (SiplNode.id < 1))
		{ //not the general operators
			if (oTranslationTable.containsKey(SiplNode.getImage()))
			{
				//duplicate a small tree
				HT = (TranslationItem)oTranslationTable.get(SiplNode.getImage());
				Rp = HT.TranEntry;
				Rp1 = new SimpleNode(Rp.id);
				Rp1.setImage(Rp.getImage());
				Rp1.parent = null;
				if (Rp.children != null)
				{
					for(int dup = 0; dup < Rp.children.length; dup++)
					{
						Duplicate(Rp1, (SimpleNode)Rp.children[dup], dup);

						//Rp1.dump(" ");

					}
				}
				Child_Num = SiplNode.children.length;
				if ((Child_Num < 1) || (Child_Num > 3))
				{
					System.out.println("Translation error: Error parameters.");
					errFile.println("Translation error: Error parameters.");
					Translator.iWarningCount++;
				}
				else
				{ //pay attention to in sipl ast, the left and the right child
					if (Child_Num == 1)
					{
						Rp2 = (SimpleNode)SiplNode.children[0];
						if (Rp2.id == JJTDIMENSION)
						{ //dimension
							Replace(Rp1, Rp2);
						}
						else
						{
							Replace(Rp1, Rp2.getImage());
						}
					}
					else if (Child_Num == 2)
					{
						Rp2 = (SimpleNode)SiplNode.children[0];
						Rp3 = (SimpleNode)SiplNode.children[1];
						if (Rp2.id == JJTDIMENSION)
						{
							Replace(Rp1, Rp3.getImage(), Rp2);
						}
						else if (Rp3.id == JJTDIMENSION)
						{
							Replace(Rp1, Rp2.getImage(), Rp3);
						}
						else
						{
							Replace(Rp1, Rp2.getImage(), Rp3.getImage(), null);
						}
					}
					else if (Child_Num == 3)
					{
						Rp2 = (SimpleNode)SiplNode.children[0];
						Rp3 = (SimpleNode)SiplNode.children[1];
						Rp4 = (SimpleNode)SiplNode.children[2];
						Replace(Rp1, Rp2.getImage(), Rp4.getImage(), Rp3);
					}

					Rp1.parent = SiplNode.parent;
					((SimpleNode)SiplNode.parent).children[Num] = Rp1;

				}
			}
			else
			{
				System.out.println("Translation error: No such operator.");
				errFile.println("Translation error: No such operator.");
				Translator.iWarningCount++;
				return null;
			}
		}
		else
		{
			if (SiplNode.children != null)
			{
				for(int r = 0; r < SiplNode.children.length; r++)
				{
					AutoTran((SimpleNode)SiplNode.children[r], r);
				}
			}
		}

		return SiplNode;
	}

	/**
	 * Set up a small SIPL AST
	 */
	SimpleNode Setup()
	{
		SimpleNode Set1, Set2, Set3, Set4, Set5, Set6, Set7, Set8, Set9, Set10, Set11, Set12, Set13, Set14, Set15;

		Set7 = new SimpleNode(JJTWHERE);
		Set7.parent = null;

		Set3 = new SimpleNode(29);
		Set3.setImage("fby");
		Set3.parent = Set7;
		Set7.jjtAddChild(Set3, 0);

		Set4 = new SimpleNode(JJTID);
		Set4.setImage("x");
		Set4.parent = Set3;
		Set5 = new SimpleNode(JJTDIMENSION);
		Set5.setImage("d");
		Set5.parent = Set3;
		Set6 = new SimpleNode(JJTID);
		Set6.setImage("y");
		Set6.parent = Set3;
		Set3.jjtAddChild(Set4, 0);
		Set3.jjtAddChild(Set5, 1);
		Set3.jjtAddChild(Set6, 2);

		Set8 = new SimpleNode(JJTDIMENSION);
		Set8.parent = Set7;
		Set7.jjtAddChild(Set8, 1);

		Set9 = new SimpleNode(JJTID);
		Set9.setImage("d");
		Set9.parent = Set8;
		Set8.jjtAddChild(Set9, 0);

		Set10 = new SimpleNode(JJTASSIGN);
		Set10.parent = Set7;
		Set7.jjtAddChild(Set10, 2);

		Set11 = new SimpleNode(JJTID);
		Set11.setImage("x");
		Set11.parent = Set10;
		Set12 = new SimpleNode(JJTCONST);
		Set12.setImage("1.5");
		Set12.parent = Set10;
		Set10.jjtAddChild(Set11, 0);
		Set10.jjtAddChild(Set12, 1);

		Set13 = new SimpleNode(JJTASSIGN);
		Set13.parent = Set7;
		Set7.jjtAddChild(Set13, 3);

		Set14 = new SimpleNode(JJTID);
		Set14.setImage("y");
		Set14.parent = Set13;
		Set15 = new SimpleNode(JJTCONST);
		Set15.setImage("1");
		Set15.parent = Set13;
		Set13.jjtAddChild(Set14, 0);
		Set13.jjtAddChild(Set15, 1);

		return Set7;
	}

	/**
	 * Duplicates the AST of the corresponding operator through the "library".
	 *
	 * In the library, the small tree of each operator is with L, R, or D general operands. 
	 * However, each specific node in the SIPL AST has real operands, so we need to change 
	 * the original small tree with different oprends. To prevent the basic operator 
	 * translation tree from changing, we should duplicate the tree before using it.
	 */
	void Duplicate(SimpleNode root, SimpleNode Ori_tree, int Child_Num)
	{
		SimpleNode Dup_tree, Dup_root;

		Dup_tree = new SimpleNode(Ori_tree.id); //= node
		Dup_tree.setImage(Ori_tree.getImage());
		Dup_tree.parent = root;
		root.jjtAddChild(Dup_tree, Child_Num);
		if (Ori_tree.children != null)
		{
			for(int r = 0; r < Ori_tree.children.length; r++)
			{
				Duplicate((SimpleNode)root.children[Child_Num], (SimpleNode)Ori_tree.children[r], r);
			}
		}
	}

	/**
	 * All these Replace() methods produce a specific tree with parameters.
	 * The replace is not simple replacing the node, think about where node!
	 *
	 * Replaces one operand - except dimension. 
	 * 
	 * @param ReTree the entry SimpleNode for the AST
	 * @param para   the parameter String 
	 */
	void Replace(SimpleNode ReTree, String para)
	{ //for one parameter except dimension

		if (ReTree.children == null)
		{
			if ((ReTree.getImage().equals("L")) || (ReTree.getImage().equals("R")))
			{
				ReTree.setImage(para);
			}
			else
			{
				for(int r = 0; r < ReTree.children.length; r++)
				{
					Replace((SimpleNode)ReTree.children[r], para);
				}
			}
		}

	}

	/**
	 * Replaces one operand - only dimension
	 *
	 * @param ReTree the entry SimpleNode for the AST
	 * @param Dim   the dimension  
	 */
	void Replace(SimpleNode ReTree, SimpleNode Dim)
	{ //for one parameter is dimension

		SimpleNode Rep;

		if (ReTree.children == null)
		{
			Rep = (SimpleNode)ReTree.parent;
			Dim.parent = Rep.parent;
			if (Rep.id == JJTHASH)
			{
				Rep.children[0] = Dim;
			}
			else if (Rep.id == JJTAT)
			{
				Rep.children[1] = Dim;
			}
		}
		else
		{
			for(int r = 0; r < ReTree.children.length; r++)
			{
				Replace((SimpleNode)ReTree.children[r], Dim);
			}
		}

	}

	/**
	 * Be used under the condition that there is one operand and one dimension.
	 *
	 * @param ReTree the entry SimpleNode for the AST
	 * @param para   the parameter String 
	 * @param Dim    the dimension  
	 */
	void Replace(SimpleNode ReTree, String para, SimpleNode Dim)
	{ //for one parameter is dimension

		SimpleNode Rep;

		if (ReTree.children == null)
		{
			if ((ReTree.getImage().equals("L")) || (ReTree.getImage().equals("R")))
			{
				ReTree.setImage(para);
			}
			else
			{
				Rep = (SimpleNode)ReTree.parent;
				Dim.parent = Rep.parent;
				if (Rep.id == JJTHASH)
				{
					Rep.children[0] = Dim;
				}
				else if (Rep.id == JJTAT)
				{
					Rep.children[1] = Dim;
				}
			}
		}
		else
		{
			for(int r = 0; r < ReTree.children.length; r++)
			{
				Replace((SimpleNode)ReTree.children[r], para, Dim);
			}
		}

	}

	/**
	 * Be used under many normal conditions that there are left and right operands and dimension.
	 *
	 * @param ReTree the entry SimpleNode for the AST
	 * @param left   the left operand String 
	 * @param bRight  the right operand String
	 * @param Dim    the dimension  
	 */
	void Replace(SimpleNode ReTree, String Left, String Right, SimpleNode Dim)
	{

		SimpleNode Rep;

		if (ReTree.children == null)
		{

			if (ReTree.getImage().equals("L"))
			{
				ReTree.setImage(Left);
			}
			else if (ReTree.getImage().equals("R"))
			{
				ReTree.setImage(Right);
			}
			else if (ReTree.getImage().equals("D"))
			{
				Rep = (SimpleNode)ReTree.parent;
				if (Rep.id == JJTHASH)
				{
					Rep.children[0] = Dim;
				}
				else if (Rep.id == JJTAT)
				{
					Rep.children[1] = Dim;
				}
				if (Dim != null)
				{
					Dim.parent = Rep.parent;
				}
			}

		}
		else
		{
			for(int r = 0; r < ReTree.children.length; r++)
			{
				Replace((SimpleNode)ReTree.children[r], Left, Right, Dim);
			}
		}

	}
}

// EOF
