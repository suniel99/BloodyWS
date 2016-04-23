package gipsy.GIPC;

import java.util.Stack;

import gipsy.GEE.CONFIG;
import gipsy.storage.*;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.GIPC.intensional.GIPL.GIPLParserTreeConstants;


/**
 * Generate codes from the dictionary produced by semantic checking. TODO: Problem: the position of demanding variables
 * needs to be adjusted manually
 * 
 * @author Paula
 * @version $Revision: 1.17 $
 * @since Nov 17
 * @see CONFIG
 * @see ParserTreeConstants
 */
public class IdentifierContextCodeGenerator
implements CONFIG, GIPLParserTreeConstants
{
	private Dictionary dict = new Dictionary();

	private Stack idstack = new Stack();

	private StringBuffer code = new StringBuffer();

	/**
	 * Generates the head of code String.
	 * 
	 * @param id each identifier has a ic class.
	 */
	public void generateHead(int id)
	{
		code.append("public final class ic" + id + " extends IdentifierContext {\n");
		code.append("  public ic" + id + "(int iccode, int icname, int[] k) { \n");
		code.append("    super(iccode, icname,k);" + "\n");
		code.append("  } " + "\n");
		code.append("  public void run() { ");
		newline(4);
	}

	/**
	 * Generates the tail of code String.
	 */
	public void generateTail()
	{
		code.append("\n");
		code.append("}\n\n");
		code.append("/*********** code for demanding the value of identifiers ***********/\n");
	}

	/**
	 * Judges if a operator is arithmatic operator.
	 * 
	 * @param op the Integer value of operator.
	 * @return <code>true</code> operator is arithmatic. <code>false</code> operator is not arithmatic.
	 */
	public boolean isArith(int op)
	{
		return (op == JJTADD || op == JJTMIN || op == JJTTIMES || op == JJTDIV || op == JJTMOD);
	}

	/**
	 * Judges if a operator is relation operator.
	 * 
	 * @param op the Integer value of operator.
	 * @return <code>true</code> operator is relation operator. <code>false</code> operator is not relation
	 *         opeartor.
	 */
	boolean isRel(int op)
	{
		return (op == JJTLT || op == JJTGT || op == JJTLE || op == JJTGE || op == JJTEQ || op == JJTNE);
	}

	/**
	 * Judges if a operator is logical operator.
	 * 
	 * @param op the Integer value of operator.
	 * @return <code>true</code> operator is logical. <code>false</code> operator is not logical.
	 */
	boolean isLog(int op)
	{
		return (op == JJTAND || op == JJTOR);
	}

	/**
	 * Judges if a operator is arithmatic or relation or logical operator.
	 * 
	 * @param op the Integer value of operator.
	 * @return <code>true</code> op is an operator. <code>false</code> op is not an operator.
	 */
	boolean isOp(int op)
	{
		return (isLog(op) || isRel(op) || isArith(op));
	}

	/**
	 * Adds indents for code String. a neat looking for nested scope.
	 * 
	 * @param length the Integer value of indents added.
	 */
	void addindent(int length)
	{
		for(int i = 0; i < length; i++)
			code.append(' ');
	}

	/**
	 * Adds a new line.
	 * 
	 * @param length the Integer value of indents added.
	 * @see addindent(int length).
	 */
	void newline(int length)
	{
		code.append('\n');
		addindent(length);
	}

	/**
	 * Initializes. Be called before generating each identifier.
	 */
	void clearcode()
	{
		code = new StringBuffer("");
		idstack = new Stack();
	}

	/**
	 * Adjusts the position of demand.
	 * 
	 * @param indent the Integer value of blank number.
	 */
	void transid(int indent)
	{
		while(!idstack.isEmpty())
		{
			Integer idx = (Integer)idstack.pop();
			newline(indent);
			code.append("/* Position need to be adjusted */" + '\n');
			addindent(indent);
			code.append(" int h" + idx.intValue() + " = demand (" + idx.intValue() + ",cont);");
			newline(indent);
			code.append(" int v" + idx.intValue() + " = getValue (h" + idx.intValue() + ");");
			newline(indent);
		}
		newline(0);
		code.append("/*************************************************************/");
	}

	/**
	 * Translates back arithmatic, relation, or logical operators to symbols.
	 * 
	 * @param op the Integer value of operators.
	 */
	String transbackOp(int op)
	{
		switch(op)
		{
			case JJTADD:
				return "+";
			case JJTMIN:
				return "-";
			case JJTTIMES:
				return "*";
			case JJTDIV:
				return "/";
			case JJTMOD:
				return "%";
			// rel_op
			case JJTLT:
				return "<";
			case JJTGT:
				return ">";
			case JJTLE:
				return "<=";
			case JJTGE:
				return ">=";
			case JJTEQ:
				return "==";
			case JJTNE:
				return "!=";
			// log_op
			case JJTAND:
				return "&&";
			case JJTOR:
				return "||";
			default:
				return (" bad Operator");
		}
	}

	/**
	 * Translates each identifier into unique codes.
	 * 
	 * @param expr SimpleNode used to be translated.
	 * @param indent the blank number
	 */
	void trans(SimpleNode expr, int indent) throws Exception
	{
		int dim;
		switch(expr.id)
		{
			case JJTAT:
				dim = ((SimpleNode)expr.children[1]).ID;
				code.append("cont [" + dim + "] = ");
				trans((SimpleNode)expr.children[2], indent + 1);
				trans((SimpleNode)expr.children[0], indent + 1);
				break;

			case JJTHASH:
				dim = ((SimpleNode)expr.children[0]).ID;
				code.append("cont[" + dim + "] ");
				break;

			case JJTADD:
			case JJTMIN:
			case JJTTIMES:
			case JJTDIV:
			case JJTMOD:
			// rel_op
			case JJTLT:
			case JJTGT:
			case JJTLE:
			case JJTGE:
			case JJTEQ:
			case JJTNE:
			// log_op
			case JJTAND:
			case JJTOR:
				if (isArith(expr.id))
					if (((SimpleNode)expr.parent).id == JJTIF || ((SimpleNode)expr.parent).id == JJTAT
					&& ((SimpleNode)expr.children[0]).id != JJTHASH)
						code.append(" value = ");
				trans((SimpleNode)expr.children[0], indent + 1);
				code.append(' ');
				code.append(transbackOp(expr.id) + ' ');
				trans((SimpleNode)expr.children[1], indent + 1);
				if (isArith(expr.id))
				{
					code.append(";" + '\n');
					for(int i = 0; i < indent; i++)
						code.append(' ');
				}
				break;
			case JJTIF:
				code.append("if ( ");
				trans((SimpleNode)expr.children[0], indent + 1);
				code.append(") {");
				newline(indent);
				trans((SimpleNode)expr.children[1], indent + 1);
				newline(indent);
				code.append("}");
				newline(indent);
				code.append("else {");
				newline(indent);
				trans((SimpleNode)expr.children[2], indent + 1);
				newline(indent);
				code.append("}");
				//before close the else, check the stack to see if any ids have been used
				break;
			case JJTID:
				if (((SimpleNode)expr.parent).id == JJTIF)
					code.append("value = ");
				code.append("v" + expr.ID + " ");
				if (((SimpleNode)expr.parent).id == JJTAT)
				{
					code.append(";");
					newline(indent);
				}
				if (((SimpleNode)expr.parent).id == JJTIF)
					code.append(";");
				if (idstack.search(new Integer(expr.ID)) == -1)
					idstack.push(new Integer(expr.ID));
				if (expr.ID == -1)
					System.out.println("Generated code has mistakes: Unknown ID: __" + expr.getImage());
				break;
			case JJTCONST:
				if (((SimpleNode)expr.parent).id == JJTIF)
					code.append("value = ");
				code.append(expr.getImage());
				if (((SimpleNode)expr.parent).id == JJTIF)
					code.append(";");
				break;
			default:
				throw new GIPCException("Bad node type");
		}
	}

	/**
	 * Generates code.
	 * 
	 * @param dictSemantic the dictionary produced by SemanticAnalyzer.
	 * @see trans ( SimpleNode expr, int indent ).
	 */
	public void generate(Dictionary dictSemantic)
	{
		DictionaryItem item;

		boolean fini = false;

		for(int k = 0; k < dictSemantic.size(); k++)
		{
			item = (DictionaryItem)dictSemantic.elementAt(k);
			dict.add(item.getID(), item);
		}

		System.out.println("\n" + "~~~~~~~~~~~~Code Generator is coming to Town~~~~~~~~~~~~ ");

		try
		{
			for(int k = 0; k < dict.size(); k++)
			{
				item = (DictionaryItem)dict.get(k);

				if (item.getKind().equals("identifier"))
				{
					clearcode();
					System.out.println("\n======= code for " + item.getName() + "========");
					generateHead(item.getID());
					trans((SimpleNode)(item.getEntry()), 4);
					generateTail();
					transid(2);
					System.out.println(code);
				}
			}
		}
		catch(Exception e)
		{
			System.err.print(e.getMessage());
			e.printStackTrace(System.err);
		}
	}
}

// EOF
