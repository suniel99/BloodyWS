package gipsy.GIPC;

import gipsy.util.GIPSYException;

/**
 * <p>GIPCException - an exceptional situation in GIPC in general.</p>
 * 
 * @author Serguei Mokhov
 * @version $Id: GIPCException.java,v 1.4 2013/08/25 02:57:02 mokhov Exp $
 * @since 1.0.0
 */
public class GIPCException
extends GIPSYException
{
	/**
	 * For serialization versioning.
	 */
	private static final long serialVersionUID = -1616113964047750496L;

	/**
	 * @param pstrMessage
	 * @param poException
	 */
	public GIPCException(String pstrMessage, Exception poException)
	{
		super(pstrMessage, poException);
	}
	
	/**
	 * Default.
	 */
	public GIPCException()
	{
		super();
	}

	/**
	 * Generic exception.
	 * @param pstrMessage Error message string
	 */
	public GIPCException(String pstrMessage)
	{
		super(pstrMessage);
	}

	/**
	 * Wrapper around other than GIPSY exceptions.
	 * @param poException Exception object to report
	 */
	public GIPCException(Exception poException)
	{
		super(poException.getMessage(), poException);
	}
}

// EOF
