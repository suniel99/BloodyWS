package gipsy.lang;

/**
 * @author serguei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GIPSYDouble
extends GIPSYType
{
	protected Double oDoubleValue;
	
	public GIPSYDouble()
	{
		this(0.0);
	}
	
	public GIPSYDouble(double pdValue)
	{
		this(new Double(pdValue));
	}

	public GIPSYDouble(Double poDoubleValue)
	{
		this.strLexeme = "double";
		this.iType = TYPE_DOUBLE;
		
		if(poDoubleValue == null)
			throw new NullPointerException("null GIPSYDouble parameter");
		
		this.oDoubleValue = poDoubleValue;
	}

	public Object getEnclosedTypeOject()
	{
		return this.oDoubleValue;
	}

	public Double getValue()
	{
		return this.oDoubleValue;
	}
	
	public String toString()
	{
		return this.strLexeme + " : " + this.oDoubleValue;
	}
}

// EOF
