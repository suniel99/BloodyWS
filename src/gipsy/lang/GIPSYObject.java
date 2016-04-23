package gipsy.lang;

/**
 * @author Serguei
 */
public class GIPSYObject
extends GIPSYType
{
	protected Object oObjectValue;

	/**
	 * Flattened array of reference to
	 * object members.
	 */
	protected GIPSYType[] aoMembers; 
	
	public GIPSYObject()
	{
		this(new Object());
	}
	
	public GIPSYObject(Object poObjectValue)
	{
		this.strLexeme = "object";
		this.iType = TYPE_OBJECT;
		
		if(poObjectValue == null)
			throw new NullPointerException("null GIPSYObject parameter");
		
		this.oObjectValue = poObjectValue;
	}

	public Object getEnclosedTypeOject()
	{
		return this.oObjectValue;
	}

	public Object getValue()
	{
		return this.oObjectValue;
	}
	
	public String toString()
	{
		return this.strLexeme + " : " + this.oObjectValue;
	}
}

// EOF
