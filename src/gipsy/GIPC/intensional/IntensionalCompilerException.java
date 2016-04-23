package gipsy.GIPC.intensional;

import gipsy.GIPC.GIPCException;

/**
 * @author Serguei Mokhov
 * @version $Id: IntensionalCompilerException.java,v 1.6 2013/01/07 19:16:10 mokhov Exp $
 * @since 
 */
public class IntensionalCompilerException
extends GIPCException
{
	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = -9137792902979535413L;

	/**
	 * 
	 */
	public IntensionalCompilerException()
	{
		super();
	}
	
	/**
	 * @param poException
	 */
	public IntensionalCompilerException(Exception poException)
	{
		super(poException);
	}
	
	/**
	 * @param pstrMessage
	 */
	public IntensionalCompilerException(String pstrMessage)
	{
		super(pstrMessage);
	}
}

// EOF
