package gipsy.lang;

/**
 * <p>Represents a character value and type.</p>
 * 
 * @author Serguei Mokhov
 * @version $Id: GIPSYCharacter.java,v 1.5 2013/08/12 12:03:55 mokhov Exp $
 * @since GIPSY Type System Inception
 */
public class GIPSYCharacter
extends GIPSYType
{
	/**
	 * For serialization versioning. 
	 */
	private static final long serialVersionUID = 261829820632691717L;
	
	protected Character oCharacterValue;
	
	public GIPSYCharacter()
	{
		this(' ');
	}
	
	public GIPSYCharacter(char pcValue)
	{
		this(new Character(pcValue));
	}

	public GIPSYCharacter(Character poCharacterValue)
	{
		this.strLexeme = "char";
		this.iType = TYPE_CHARACTER;
		
		if(poCharacterValue == null)
		{
			throw new NullPointerException("null GIPSYCharacter parameter");
		}
		
		this.oCharacterValue = poCharacterValue;
	}

	public Object getEnclosedTypeOject()
	{
		return this.oCharacterValue;
	}

	public Character getValue()
	{
		return this.oCharacterValue;
	}
	
	public String toString()
	{
		return this.strLexeme + " : " + this.oCharacterValue;
	}
}

// EOF