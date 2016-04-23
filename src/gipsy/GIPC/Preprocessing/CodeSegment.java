package gipsy.GIPC.Preprocessing;

import gipsy.util.NotImplementedException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;


/**
 * <p>Represents GIPSY program code segment.</p>
 * 
 * $Id: CodeSegment.java,v 1.5 2009/08/25 18:47:06 mokhov Exp $
 * 
 * @author Serguei Mokhov
 * @version $Revision: 1.5 $
 * @since 1.0.0
 */
public class CodeSegment
{
	/**
	 * Programming language this code segment is in.
	 */
	private String strLanguage = null;
	
	/**
	 * The actual body, the code.
	 */
	private String strCode = null;
	
	
	/**
	 * @param pstrLanguage
	 * @param pstrCode
	 */
	public CodeSegment(String pstrLanguage, String pstrCode)
	{
		this.strLanguage = pstrLanguage;
		this.strCode = pstrCode;
	}
	
	public CodeSegment(String pstrLanguage, File poFile)
//		throws FileNotFoundException
	{
		this.strLanguage = pstrLanguage;
		
		this.strCode = "";
		
		throw new NotImplementedException("File");
	}

	/**
	 * @return
	 */
	public String getLanguageName()
	{
		return this.strLanguage;
	}
	
	/**
	 * @return
	 */
	public InputStream getSourceCodeStream()
	{
		return new ByteArrayInputStream(this.strCode.getBytes());
	}
	
	/**
	 * @return
	 */
	public String getSourceCode()
	{
		return this.strCode;
	}
	
	/**
	 * Overridden to reproduce the textual
	 * form of the code segment.
	 */
	public String toString()
	{
		return
			this.strLanguage + "\n\n" +
			this.strCode + "\n\n";
	}
}

// EOF
