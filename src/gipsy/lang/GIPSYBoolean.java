package gipsy.lang;

/**
 * @author serguei
 */
public class GIPSYBoolean
extends GIPSYType
{
	/**
	 * For serialization versioning. 
	 */
	private static final long serialVersionUID = 2825596064783951L;

	protected Boolean oBooleanValue;
	
	public GIPSYBoolean()
	{
		this(false);
	}
	
	public GIPSYBoolean(boolean pbValue)
	{
		this(new Boolean(pbValue));
	}

	public GIPSYBoolean(Boolean poBooleanValue)
	{
		this.strLexeme = "bool";
		this.iType = TYPE_BOOLEAN;
		
		if(poBooleanValue == null)
			throw new NullPointerException("null GIPSYBoolean parameter");
		
		this.oBooleanValue = poBooleanValue;
	}

	public Object getEnclosedTypeOject()
	{
		return this.oBooleanValue;
	}

	public Boolean getValue()
	{
		return this.oBooleanValue;
	}
	
	public String toString()
	{
		return this.strLexeme + " : " + this.oBooleanValue;
	}
}

// EOF