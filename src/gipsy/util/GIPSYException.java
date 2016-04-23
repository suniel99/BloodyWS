package gipsy.util;

import marf.util.MARFException;


/**
 * Root exception for GIPSY specifics. Subsystems usually
 * override for their own needs.
 * 
 * @author Serguei Mokhov
 * @version $Id: GIPSYException.java,v 1.10 2012/06/19 16:58:06 mokhov Exp $
 * @since 1.0.0
 */
public class GIPSYException
extends MARFException
{
	/**
	 * For serialization versioning.
	 */
	private static final long serialVersionUID = -4058644523342402450L;

	public GIPSYException()
	{
		super();
	}

	public GIPSYException(Exception poException)
	{
		super(poException);
	}

	public GIPSYException(String pstrMessage, Exception poException)
	{
		super(pstrMessage, poException);
	}

	public GIPSYException(String pstrMessage)
	{
		super(pstrMessage);
	}
}

// EOF
