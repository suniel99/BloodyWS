package gipsy.interfaces;

import gipsy.util.GIPSYException;

/**
 * All Communication Procedures and related routines
 * should throw our own CommunicationException.
 *
 * @author Serguei Mokhov
 * @version $Id: CommunicationException.java,v 1.4 2013/08/25 02:54:42 mokhov Exp $
 */
public class CommunicationException
extends GIPSYException
{
	/**
	 * For serialization versioning. 
	 */
	private static final long serialVersionUID = -3592402917493007375L;

	public CommunicationException()
	{
		super();
	}

	public CommunicationException(Exception poException)
	{
		super(poException);
	}

	public CommunicationException(String pstrMessage, Exception poException)
	{
		super(pstrMessage, poException);
	}

	public CommunicationException(String pstrMessage)
	{
		super(pstrMessage);
	}
}

// EOF
