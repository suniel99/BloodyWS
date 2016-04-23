package gipsy.GEE.multitier.DWT;

import gipsy.GEE.multitier.MultiTierException;

/**
 * Exception class for exceptions raised by a worker tier during 
 * execution. 
 * 
 * @author BinHan
 *
 */
public class DWTException extends MultiTierException {

	/**
	 * @param pstrMessage
	 */
	public DWTException(String pstrMessage) {
		super(pstrMessage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param poException
	 */
	public DWTException(Exception poException) {
		super(poException);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public DWTException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pstrMessage
	 * @param poException
	 */
	public DWTException(String pstrMessage, Exception poException) {
		super(pstrMessage, poException);
		// TODO Auto-generated constructor stub
	}

}
