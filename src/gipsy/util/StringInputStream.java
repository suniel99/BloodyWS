package gipsy.util;


/**
 * @author Serguei Mokhov
 * @since November 13, 2008
 * @version $Id: StringInputStream.java,v 1.1 2008/11/13 18:51:08 mokhov Exp $
 */
public class StringInputStream
extends org.apache.maven.util.StringInputStream
{
	/**
	 * @param poSource
	 */
	public StringInputStream(String poSource)
	{
		super(poSource);
	}
}

// EOF
