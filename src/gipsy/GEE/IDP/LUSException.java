package gipsy.GEE.IDP;


/**
 * DMS LUS Exception. 
 *
 * @author Emil Vassev
 * @author Serguei Mokhov
 *
 * @since 1.0.0
 * @version $Id: LUSException.java,v 1.7 2013/01/07 19:16:05 mokhov Exp $
 */
public class LUSException
extends DMSException
{
  	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = 2578205267115604080L;
	
	public static final String ERR_TAG = "Impossible to locate a LUS on host ";

	public LUSException(String pstrHost)	
	{
		super(ERR_TAG + pstrHost);
	}

	/**
	 * @since Serguei Mokhov
	 */
	public LUSException()
	{
		super();
	}

	/**
	 * @since Serguei Mokhov
	 */
	public LUSException(Exception poException)
	{
		super(poException);
	}

	/**
	 * @since Serguei Mokhov
	 */
	public LUSException(String pstrMessage, Exception poException)
	{
		super(pstrMessage, poException);
	}
}

// EOF
