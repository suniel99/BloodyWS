package gipsy.GEE.multitier;

import gipsy.GEE.GEEException;


/**
 * Exception super class for exceptions raised during the execution 
 * in multi-tier execution mode. Each of the tier packages 
 * (DST, DGT, DWT and GMT) have their own subclass. 
 * 
 * @author Bin Han
 * @since
 * @version $Id: MultiTierException.java,v 1.4 2010/12/09 04:30:59 mokhov Exp $
 *
 * @see DGTException
 * @see DSTException
 * @see DWTException
 */
public class MultiTierException
extends GEEException
{
	/**
	 * Serial UID. 
	 */
	private static final long serialVersionUID = -6195320385652857639L;

	/**
	 * Constructor: default constructor
	 */
	public MultiTierException()
	{
		super();
	}

	/**
	 * Constructor: receiving a string as exception message. 
	 * @param pstrMessage
	 */
	public MultiTierException(String pstrMessage)
	{
		super(pstrMessage);
	}

	/**
	 * Constructor: receiving an exception object. 
	 * @param poException
	 */
	public MultiTierException(Exception poException)
	{
		super(poException);
	}

	/**
	 * Constructor: receiving a string message and an exception object. 
	 * @param pstrMessage
	 * @param poException
	 */
	public MultiTierException(String pstrMessage, Exception poException)
	{
		super(pstrMessage, poException);
	}
}

// EOF
