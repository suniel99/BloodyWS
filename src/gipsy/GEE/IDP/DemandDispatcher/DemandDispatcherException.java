package gipsy.GEE.IDP.DemandDispatcher;

import gipsy.GEE.IDP.DMSException;


/**
 * DMS Demand Dispatcher Exception.
 * 
 * @author Emil Vassev
 * @author Serguei Mokhov
 * 
 * @since 1.0.0
 * @version $Id: DemandDispatcherException.java,v 1.9 2011/01/06 00:14:43 mokhov Exp $
 */
public class DemandDispatcherException
extends DMSException
{
  	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -8217928425820501397L;
	
	/**
	 * Common error message prefix.
	 */
	public static final String ERR_MSG_PREFIX = "DemandDispatcher exception: ";

	/**
	 * @param pstrMsg
	 */
	public DemandDispatcherException(String pstrMsg)	
	{
		super(ERR_MSG_PREFIX + pstrMsg);
	}

	/**
	 * @since Serguei
	 */
	public DemandDispatcherException()
	{
		super();
	}

	/**
	 * @param poException
	 * @since Serguei
	 */
	public DemandDispatcherException(Exception poException)
	{
		super(poException);
	}

	/**
	 * @param pstrMessage
	 * @param poException
	 * @since Serguei
	 */
	public DemandDispatcherException(String pstrMessage, Exception poException)
	{
		super(pstrMessage, poException);
	}
}

// EOF
