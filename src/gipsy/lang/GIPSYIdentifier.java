package gipsy.lang;


/**
 * <p>Represents an identifier value and type.</p>
 * 
 * @author Serguei Mokhov
 * @version $Id: GIPSYIdentifier.java,v 1.5 2007/11/30 15:20:00 mokhov Exp $
 * @since GIPSY Type System Inception
 */
public class GIPSYIdentifier
extends GIPSYType
{
	/**
	 * User-set identifier name, e.g. variable.
	 */
	protected String strIdentifierValue;

	/**
	 * Resulting data type of the identifier (e.g. a variable).
	 */
	protected GIPSYType oIdentifierDataType;
	
	public GIPSYIdentifier()
	{
		this("<anonymous>");
	}
	
	public GIPSYIdentifier(String pstrIdentifierValue)
	{
		this(pstrIdentifierValue, null);
	}

	public GIPSYIdentifier(String pstrIdentifierValue, GIPSYType poIdentifierType)
	{
		if(pstrIdentifierValue == null)
		{
			throw new NullPointerException("null GIPSYIdentifier parameter");
		}

		this.strLexeme = "id";
		this.iType = TYPE_IDENTIFIER;
		this.strIdentifierValue = pstrIdentifierValue;
		this.oIdentifierDataType = poIdentifierType;
	}

	public Object getEnclosedTypeOject()
	{
		return this.strIdentifierValue;
	}

	public String getValue()
	{
		return this.strIdentifierValue;
	}
	
	public String toString()
	{
		return this.strLexeme + " : " + this.strIdentifierValue + " : " + this.oIdentifierDataType;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @since Xin
	 */
	public boolean equals(Object poOtherObject)
	{
		if(getClass() != poOtherObject.getClass())
		{
			return false;
		}
		else
		{
			return getValue().equals(((GIPSYIdentifier)poOtherObject).getValue());
		}
	}
}

// EOF
