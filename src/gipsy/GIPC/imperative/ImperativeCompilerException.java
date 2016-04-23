package gipsy.GIPC.imperative;

import gipsy.GIPC.GIPCException;

/**
 * <p>ImperativeCompilerException - an exceptional situation in GICF.</p>
 * 
 * @author Serguei Mokhov
 * @version $Id: ImperativeCompilerException.java,v 1.4 2013/01/09 14:52:22 mokhov Exp $
 * @since 1.0.0
 */
public class ImperativeCompilerException
extends GIPCException
{
	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = -6774584151619039019L;

	/**
	 * Generic exception
	 * @param pstrMessage Error message string
	 */
	public ImperativeCompilerException(String pstrMessage)
	{
		super(pstrMessage);
	}

	/**
	 * Wrapper around other than GIPSY exceptions
	 * @param poException Exception object to report
	 */
	public ImperativeCompilerException(Exception poException)
	{
		super(poException);
	}
}

// EOF
