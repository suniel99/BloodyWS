package gipsy.interfaces;

import java.io.Serializable;


/**
 * Encapsulates unique generic type of signature.
 * Specific signatures, such as GEER, Resource, Demand
 * override this one.
 * 
 * XXX: review GIPSYSingature idea to become a GIPSYType
 * 
 * @author Serguei Mokhov
 * @version $Id: GIPSYSignature.java,v 1.2 2013/08/25 02:54:42 mokhov Exp $
 * @since Tue Mar 9 2010
 */
public class GIPSYSignature
implements Serializable
{
	/**
	 * For serialization versioning. 
	 */
	private static final long serialVersionUID = -7818880619900463523L;
	
	/**
	 * The actual signature container.
	 */
	protected Serializable oSignature = null;

	public GIPSYSignature()
	{
	}

	public GIPSYSignature(Serializable poSignature)
	{
		this.oSignature = poSignature;
	}
	
	/**
	 * @return
	 */
	public Serializable getSignature()
	{
		return this.oSignature;
	}

	/**
	 * @param poSignature
	 */
	public void setSignature(Serializable poSignature)
	{
		this.oSignature = poSignature;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return
			this.oSignature == null ?
			"null" :
			this.oSignature.toString();
	}
}

// EOF
