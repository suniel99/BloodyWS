package gipsy.util;


/**
 * <p>Class NotImplementedException.</p>
 * <p>This class extends GIPSYRuntimeException for GIPSY unimplemented parts.</p>
 *
 * @author Serguei A. Mokhov
 * @version $Id: NotImplementedException.java,v 1.5 2009/08/22 17:25:44 mokhov Exp $
 * @since 1.0.0
 */
public class NotImplementedException
extends GIPSYRuntimeException
{
	/**
	 * Generic exception.
	 * @param pstrMessage Error message string
	 */
	public NotImplementedException(String pstrMessage)
	{
		super("Not implemented: " + pstrMessage);
	}

	/**
	 * @param poObject
	 * @param pstrMethod
	 */
	public NotImplementedException(Object poObject, String pstrMethod)
	{
		super(poObject, pstrMethod);
	}

	/**
	 * 
	 */
	public NotImplementedException()
	{
		super();
	}

	/**
	 * @param poException
	 */
	public NotImplementedException(Exception poException)
	{
		super(poException);
	}

	/**
	 * @param pstrMessage
	 * @param poException
	 */
	public NotImplementedException(String pstrMessage, Exception poException)
	{
		super(pstrMessage, poException);
	}
}

// EOF
