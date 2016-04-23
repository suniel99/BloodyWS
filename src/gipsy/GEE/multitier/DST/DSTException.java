/**
 * 
 */
package gipsy.GEE.multitier.DST;

import gipsy.GEE.multitier.MultiTierException;

/**
 * Exception class for exceptions raised by a store tier during 
 * execution. 
 * 
 * @author BinHan
 * @version $Id: DSTException.java,v 1.3 2010/09/05 15:48:27 ji_yi Exp $
 */
public class DSTException 
extends MultiTierException 
{

	/**
	 * @param pstrMessage
	 */
	public DSTException(String pstrMessage) 
	{
		super(pstrMessage);
	}

	/**
	 * @param poException
	 */
	public DSTException(Exception poException) 
	{
		super(poException);
	}

	/**
	 * 
	 */
	public DSTException() 
	{
	}

	/**
	 * @param pstrMessage
	 * @param poException
	 */
	public DSTException(String pstrMessage, Exception poException) 
	{
		super(pstrMessage, poException);
	}

}
