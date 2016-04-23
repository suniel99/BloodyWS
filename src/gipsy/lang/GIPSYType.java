package gipsy.lang;

import java.io.Serializable;


/**
 * <p>Base GIPSY Type.</p>
 * 
 * @author Serguei Mokhov
 * @version $Id: GIPSYType.java,v 1.11 2013/08/25 02:51:46 mokhov Exp $
 * @since 1.0.0
 */
public abstract class GIPSYType
implements Serializable
{
	/**
	 * For serialization versioning.
	 */
	private static final long serialVersionUID = 4266726511574894125L;
	
	public static final int TYPE_UNKNOWN    = -1;
	
	public static final int TYPE_INT        = 0;
	public static final int TYPE_DOUBLE     = 1;
	public static final int TYPE_STRING     = 2;
	public static final int TYPE_BOOLEAN    = 3;
	public static final int TYPE_CHARACTER  = 4;
	public static final int TYPE_ARRAY      = 5;
	public static final int TYPE_OBJECT     = 6;
	public static final int TYPE_VOID       = 7;
	public static final int TYPE_EMBED      = 8;
	public static final int TYPE_IDENTIFIER = 9;
	public static final int TYPE_FLOAT      = 10;
	public static final int TYPE_FUNCTION   = 11;
	public static final int TYPE_OPERATOR   = 12;
	public static final int TYPE_CONTEXT    = 13;

	/**
	 * Data type's "spelling".
	 */
	protected String strLexeme = "<abstract>";

	/**
	 * Internal identifier (variable or function name). 
	 */
	protected String strID = "<anonymous>";

	/**
	 * Enumeration type. 
	 * @see TYPE_INT
	 * @see TYPE_DOUBLE
	 * @see TYPE_STRING
	 * @see TYPE_BOOLEAN
	 * @see TYPE_CHARACTER
	 * @see TYPE_ARRAY
	 * @see TYPE_OBJECT
	 * @see TYPE_VOID
	 * @see TYPE_EMBED
	 * @see TYPE_IDENTIFIER
	 * @see TYPE_FLOAT
	 * @see TYPE_FUNCTION
	 * @see TYPE_OPERATOR
	 * @see TYPE_CONTEXT
	 */
	protected int iType = TYPE_UNKNOWN;
	
	
	public String getLexeme()
	{
		return this.strLexeme;
	}
	
	public String setLexeme(String pstrLexeme)
	{
		String strOldLexeme = this.strLexeme;
		this.strLexeme = pstrLexeme;
		return strOldLexeme;
	}

	public String getID()
	{
		return this.strID;
	}
	
	/**
	 * @param pstrID
	 * @return old ID that was previous assigned to this typed instance
	 */
	public String setID(String pstrID)
	{
		String strOldID = this.strID;
		this.strID = pstrID;
		return strOldID;
	}

	/**
	 * Every concrete type must implement this by returning
	 * the value of whatever internal type they are encapsulating
	 * or themselves.
	 * @return containing type value
	 */
	public abstract Object getEnclosedTypeOject();

	/**
	 * Convoluted factory method.
	 * @param piTypeEnum desired type of to be constructed
	 * @return the corresponding GIPSYType object
	 * @see #iType
	 */
	public static final GIPSYType getType(final int piTypeEnum)
	{
		switch(piTypeEnum)
		{
			case TYPE_INT:
				return new GIPSYInteger();

			case TYPE_DOUBLE:
				return new GIPSYDouble();

			case TYPE_STRING:
				return new GIPSYString();

			case TYPE_BOOLEAN:
				return new GIPSYBoolean();

			case TYPE_CHARACTER:
				return new GIPSYCharacter();

			case TYPE_ARRAY:
				return new GIPSYArray();

			case TYPE_OBJECT:
				return new GIPSYObject();

			case TYPE_VOID:
				return new GIPSYVoid();

			case TYPE_EMBED:
				return new GIPSYEmbed();

			case TYPE_IDENTIFIER:
				return new GIPSYIdentifier();

			case TYPE_FLOAT:
				return new GIPSYFloat();

			case TYPE_FUNCTION:
				return new GIPSYFunction();

			case TYPE_OPERATOR:
				return new GIPSYOperator();

			case TYPE_CONTEXT:
				return new GIPSYContext();

			default:
			{
				assert false : "Unknown type: " + piTypeEnum;
			}
		}
		
		// Must never reach here; instead the default assert
		// should be triggered.
		return null;
	}
	
	public int getTypeEnumeration()
	{
		return this.iType;
	}
	
	/**
	 * Returns lexeme of the type.
	 * @see java.lang.Object#toString()
	 * @see #strLexeme
	 */
	public String toString()
	{
		return this.strLexeme;
	}
}

// EOF
