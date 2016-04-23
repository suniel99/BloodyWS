package gipsy.RIPE;

import gipsy.util.GIPSYException;


/**
 * <p>RIPEException - an exceptional situation in RIPE in general.</p>
 *
 * @author Serguei Mokhov
 * @version $Id: RIPEException.java,v 1.2 2013/08/25 02:54:45 mokhov Exp $
 * @since 1.0.0
 */
public class RIPEException
extends GIPSYException
{
	/**
	 * For serialization versioning.
	 */
	private static final long serialVersionUID = 7020322753900541625L;

	/**
	 * Default.
	 */
	public RIPEException()
	{
		super();
	}

	/**
	 * Generic exception.
	 * @param pstrMessage Error message string
	 */
	public RIPEException(String pstrMessage)
	{
		super(pstrMessage);
	}

	/**
	 * Wrapper around other than GIPSY exceptions.
	 * @param poException Exception object to report
	 */
	public RIPEException(Exception poException)
	{
		super(poException.getMessage(), poException);
	}
}

// EOF
