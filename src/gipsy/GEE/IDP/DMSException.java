package gipsy.GEE.IDP;

import gipsy.GEE.GEEException;


/**
 * GIPSY Demand Migration System Exception.
 * A root for all DMS-descendant exceptions.
 *
 * @author Emil Vassev
 * @author Serguei Mokhov
 * 
 * @since 1.0.0
 * @version $Id: DMSException.java,v 1.6 2010/12/30 23:58:03 ji_yi Exp $
 */
public class DMSException
extends GEEException
{  
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -5370280102220375296L;
	
	/**
	 * Common message prefix.
	 */
	public static final String ERR_TAG = "DMSException: ";
	
	/**
	 * Message for exception caused by low memory in DST.
	 */
	public static final String OUT_OF_MEMORY = "OUT OF MEMORY";
		
	
	public DMSException(String pstrMsg)
	{
		super(ERR_TAG + pstrMsg);
	}

	/**
	 * @since Serguei Mokhov 
	 */
	public DMSException()
	{
		super();
	}

	/**
	 * @since Serguei Mokhov 
	 */
	public DMSException(Exception poException)
	{
		super(poException);
	}

	/**
	 * @since Serguei Mokhov 
	 */
	public DMSException(String pstrMessage, Exception poException)
	{
		super(pstrMessage, poException);
	}
}

// EOF
