package gipsy.GEE;

import gipsy.util.GIPSYException;


/**
 * <p>GEEException - an exceptional situation in GEE.
 * All other exceptions should be descendants of this
 * one within the GEE package and subpackages. 
 * </p>
 * 
 * @author Serguei Mokhov
 * @version $Id: GEEException.java,v 1.7 2010/12/06 13:38:36 mokhov Exp $
 * @since 1.0.0
 */
public class GEEException
extends GIPSYException
{
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 8364742916487908785L;

	/**
	 * Generic exception.
	 * @param pstrMessage Error message string
	 */
	public GEEException(String pstrMessage)
	{
		super(pstrMessage);
	}

	/**
	 * Wrapper around other than GIPSY exceptions.
	 * @param poException Exception object to report
	 */
	public GEEException(Exception poException)
	{
		super(poException.getMessage(), poException);
	}

	/**
	 * Default exception. 
	 * @since November 5, 2008
	 */
	public GEEException()
	{
		super();
	}

	/**
	 * Message and wrapper around other GIPSY exceptions.
	 * @param pstrMessage Error message string
	 * @param poException Exception object to report
	 * @since November 5, 2008
	 */
	public GEEException(String pstrMessage, Exception poException)
	{
		super(pstrMessage, poException);
	}
}

// EOF
