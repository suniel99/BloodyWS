package gipsy.lang;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * Holds remotely embedable code.
 * Originally appeared for JLucid.
 * 
 * @author Serguei Mokhov
 * @since 1.0.0
 * @version $Id: GIPSYEmbed.java,v 1.4 2007/11/30 15:20:00 mokhov Exp $
 */
public class GIPSYEmbed
extends GIPSYType
{
	/**
	 * In general, the data and the method of the
	 * embedded code. 
	 */
	protected Object oEmbedValue;

	/**
	 * Location of the embedded code following syntactically
	 * the most common Internet protocols. 
	 */
	protected URI oValueURI;
	
	public GIPSYEmbed()
	//throws URISyntaxException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		this.strLexeme = "embed";
		this.iType = TYPE_EMBED;
		//this("");
	}
	
	public GIPSYEmbed(String pstrURI)
	throws URISyntaxException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		this(new URI(pstrURI));
	}

	public GIPSYEmbed(URI poURI)
	throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		this.strLexeme = "embed";
		this.iType = TYPE_EMBED;
		
		if(poURI == null)
		{
			throw new NullPointerException("null GIPSYEmbed parameter");
		}

		this.oValueURI = poURI;
		this.oEmbedValue = Class.forName(poURI.toASCIIString()).newInstance();
	}

	public Object getEnclosedTypeOject()
	{
		return this.oEmbedValue;
	}

	public Object getValue()
	{
		return this.oEmbedValue;
	}
	
	public String toString()
	{
		return this.strLexeme  + " : " + this.oValueURI + " : " + this.oEmbedValue;
	}
}

// EOF
