package gipsy.interfaces;

import java.io.Serializable;


/**
 * TODO: document.
 *
 * @author Serguei Mokhov
 * @since Mar 9, 2010
 * @version $Id: ResourceSignature.java,v 1.2 2013/08/25 02:54:42 mokhov Exp $
 */
public class ResourceSignature
extends GIPSYSignature
{
	/**
	 * For serialization versioning. 
	 */
	private static final long serialVersionUID = 3215308075137579771L;

	/**
	 * 
	 */
	public ResourceSignature()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param poSignature
	 */
	public ResourceSignature(Serializable poSignature)
	{
		super(poSignature);
	}

}

// EOF