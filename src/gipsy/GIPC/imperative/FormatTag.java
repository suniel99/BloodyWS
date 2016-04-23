package gipsy.GIPC.imperative;

import java.util.Properties;

/**
 * Contains the format specficiation a given Sequential Thread is in.
 * This includes whether it is binary or source, operating system,
 * compiler used, and file on disk format. This is a general-purpose
 * class for all of the GICF for any imperative language.
 * 
 * $Id: FormatTag.java,v 1.6 2009/08/25 18:46:33 mokhov Exp $
 * 
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @version $Revision: 1.6 $
 * @since 1.0.0
 */
public class FormatTag
{
	public static final int CODE_UNKNOWN = -1;
	public static final int CODE_BINARY = 0;
	public static final int CODE_TEXT = 1;
	public static final int CODE_BINARY_AND_TEXT = 2;

	public static final int CODE_FORMAT = 3; 
	public static final int OS = 4; 
	public static final int COMPILER = 5; 
	public static final int COMPILER_FLAGS = 6; 

	protected Properties oFormatSpecifications = new Properties();

	public static final FormatTag UNKNOWN = new FormatTag();
	public static final FormatTag JAVA = new FormatTag(CODE_BINARY_AND_TEXT, "JVM", "javac", "");
	
	public FormatTag
	(
		int piCodeFormat,
		String pstrOS,
		String pstrCompiler,
		String pstrCompilerFlags
	)
	{
		// TODO: param validation
		this.oFormatSpecifications.put(new Integer(CODE_FORMAT), new Integer(piCodeFormat));
		this.oFormatSpecifications.put(new Integer(OS), pstrOS);
		this.oFormatSpecifications.put(new Integer(COMPILER), pstrCompiler);
		this.oFormatSpecifications.put(new Integer(COMPILER_FLAGS), pstrCompilerFlags);
	}

	public FormatTag()
	{
		this(CODE_UNKNOWN, System.getProperty("os.name"), "UNKNWON", "NONE");
	}
	
	public String toString()
	{
		return oFormatSpecifications.toString();
	}
	
	public boolean equals(Object poFormatTag)
	{
		if(this.oFormatSpecifications == null)
			return false;

		if(poFormatTag == null)
			return false;
		
		return toString().equals(poFormatTag.toString());
	}
}

// EOF