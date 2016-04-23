package gipsy.GIPC.DFG;

import gipsy.GIPC.GIPCException;

/**
 * <p>DFGException - an exceptional situation in DFG compilers and analyzers.</p>
 * 
 * @author Serguei Mokhov
 * @version $Id: DFGException.java,v 1.2 2013/01/09 14:50:29 mokhov Exp $
 * @since 1.0.0
 */
public class DFGException 
extends GIPCException
{
	/**
	 * For serialization versionning.
	 */
	private static final long serialVersionUID = -7776900057029504975L;

	/**
	 * Generic exception
	 * @param pstrMessage Error message string
	 */
	public DFGException(String pstrMessage)
	{
		super(pstrMessage);
	}

	/**
	 * Wrapper around other than GIPSY exceptions
	 * @param poException Exception object to report
	 */
	public DFGException(Exception poException)
	{
		super(poException);
	}
}

// EOF
