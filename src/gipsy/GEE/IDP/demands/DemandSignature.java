package gipsy.GEE.IDP.demands;

import gipsy.interfaces.GIPSYSignature;
import gipsy.util.NotImplementedException;

import java.io.Serializable;


/**
 * Encapsulates unique demand signature.
 * Signatures must be serializable as they
 * are likely to be transmitted and stored.
 * 
 * @author Serguei Mokhov
 * @author Yi Ji
 * @version $Id: DemandSignature.java,v 1.14 2012/06/17 16:54:39 mokhov Exp $
 */
public class DemandSignature
extends GIPSYSignature
{
	/**
	 * This designation indicates that the demand is to be 
	 * picked up by any DWT.
	 */
	public static final String DWT = "DWT";
	
	/**
	 * This designation indicates that the demand is to be
	 * picked up by any DGT.
	 */
	public static final String DGT = "DGT";
	
	/**
	 * This designation indicates that the demand (such as a 
	 * resource demand) is to be picked up by any tier.
	 */
	public static final String ANY_DEST = "ANY_DEST";
	
	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = -1171544395669634457L;

	/**
	 * Assumes a default signature of this class' instance hash code.
	 */
	public DemandSignature()
	{
		super();
		this.oSignature = hashCode();
	}

	/**
	 * Anything serializable is a signature to us.
	 * @param poSignature
	 */
	public DemandSignature(Serializable poSignature)
	{
		super(poSignature);
	}
	

	/**
	 * Compute signature based on the demand itself and set it.
	 * @param poDemand
	 */
	public void setSignature(IDemand poDemand)
	{
		this.oSignature = poDemand.hashCode();
		throw new NotImplementedException("setSignature(IDemand)");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		if(this.oSignature == null)
		{
			return super.toString();
		}
		else
		{
			return this.oSignature.toString();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object poDemandSignature) 
	{
		if(!(poDemandSignature instanceof DemandSignature))
		{
			return false;
		}
		
		DemandSignature oSig = (DemandSignature)poDemandSignature;
		
		if(oSig.oSignature == null || this.oSignature == null)
		{
			return false;
		}
		
		if(oSig.oSignature.equals(this.oSignature))
		{
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 *//*
	@Override
	public int hashCode() 
	{
		return this.oSignature.hashCode();
	}
	*/
	
}

// EOF
