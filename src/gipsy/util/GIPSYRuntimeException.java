package gipsy.util;

import marf.util.MARFRuntimeException;


/**
 * An exception GIPSY's run-time systems components or otherwise.
 *
 * @author Serguei A. Mokhov
 * @version $Id: GIPSYRuntimeException.java,v 1.5 2012/06/19 16:58:06 mokhov Exp $
 * @since 1.0.0
 */
public class GIPSYRuntimeException
extends MARFRuntimeException
{
	/**
	 * For serialization versioning.
	 */
	private static final long serialVersionUID = 1480773743841084173L;

	/**
	 * Generic exception.
	 * @param pstrMessage Error message string
	 */
	public GIPSYRuntimeException(String pstrMessage)
	{
		super(pstrMessage);
	}

	/**
	 * Generates Class.Method exception message.
	 * @param poObject object to query for class name that has something unimplemented
	 * @param pstrMethod method name that is not implemented
	 */
	public GIPSYRuntimeException(final Object poObject, String pstrMethod)
	{
		this(poObject.getClass().getName() + "." + pstrMethod);
	}

	public GIPSYRuntimeException()
	{
		super();
	}

	public GIPSYRuntimeException(Exception poException)
	{
		super(poException);
	}

	public GIPSYRuntimeException(String pstrMessage, Exception poException)
	{
		super(pstrMessage, poException);
	}

}

// EOF
