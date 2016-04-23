package gipsy.lang;

/**
 * @author serguei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GIPSYFloat
extends GIPSYType
{
	protected Float oFloatValue;
	
	public GIPSYFloat()
	{
		this(0);
	}
	
	public GIPSYFloat(float pdValue)
	{
		this(new Float(pdValue));
	}

	public GIPSYFloat(Float poFloatValue)
	{
		this.strLexeme = "float";
		this.iType = TYPE_FLOAT;
		
		if(poFloatValue == null)
			throw new NullPointerException("null GIPSYFloat parameter");
		
		this.oFloatValue = poFloatValue;
	}

	public Object getEnclosedTypeOject()
	{
		return this.oFloatValue;
	}

	public Float getValue()
	{
		return this.oFloatValue;
	}
	
	public String toString()
	{
		return this.strLexeme + " : " + this.oFloatValue;
	}
}

// EOF
