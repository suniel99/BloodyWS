package gipsy.lang;


/**
 * @author Serguei Mokhov
 * @version $id$
 */
public class GIPSYString
extends GIPSYType
{
//	protected String oStringValue;
	protected StringBuffer oStringValue;
	
	public GIPSYString()
	{
		this("");
	}
	
	public GIPSYString(String poStringValue)
	{
		this.strLexeme = "string";
		this.iType = TYPE_STRING;
		
		if(poStringValue == null)
		{
			throw new NullPointerException("null GIPSYString parameter");
		}
		
		this.oStringValue = new StringBuffer(poStringValue);
	}

	public Object getEnclosedTypeOject()
	{
		return this.oStringValue;
	}

	public String getValue()
	{
		return this.oStringValue.toString();
	}
	
	public String toString()
	{
		return this.strLexeme + " : " + this.oStringValue;
	}

	public boolean equals(Object poOtherObject)
	{
		if(poOtherObject instanceof StringBuffer)
		{
			//return this.getValue().equals(((GIPSYString)poOtherObject).getValue());
			return this.oStringValue.equals(poOtherObject);
		}
		else if(poOtherObject instanceof String)
		{
			return this.oStringValue.toString().equals(poOtherObject);
		}
		else if(poOtherObject instanceof GIPSYString)
		{
			return this.oStringValue.toString().equals(((GIPSYString)poOtherObject).toString());
		}
		
		return false;
	}
}

// EOF
