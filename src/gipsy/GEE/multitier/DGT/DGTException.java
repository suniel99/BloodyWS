package gipsy.GEE.multitier.DGT;

import gipsy.GEE.multitier.MultiTierException;


/**
 * Exception class for exceptions raised by a generator tier during 
 * execution. 
 * 
 * @author Bin Han
 * @version $Id: DGTException.java,v 1.4 2010/12/09 04:30:56 mokhov Exp $
 * @since
 */
public class DGTException
extends MultiTierException
{
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -1784139101099777093L;

	/**
	 * @param pstrMessage
	 */
	public DGTException(String pstrMessage)
	{
		super(pstrMessage);
	}

	/**
	 * @param poException
	 */
	public DGTException(Exception poException)
	{
		super(poException);
	}

	/**
	 * 
	 */
	public DGTException()
	{
		super();
	}

	/**
	 * @param pstrMessage
	 * @param poException
	 */
	public DGTException(String pstrMessage, Exception poException)
	{
		super(pstrMessage, poException);
	}
}

// EOF
