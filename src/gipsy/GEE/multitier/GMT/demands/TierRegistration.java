/**
 * 
 */
package gipsy.GEE.multitier.GMT.demands;

import gipsy.GEE.IDP.demands.SystemDemand;

/**
 * The registration demand contain information required for all tier registrations.
 * 
 * @author Yi Ji
 * @version $Id: TierRegistration.java,v 1.1 2010/10/07 01:20:02 ji_yi Exp $
 */
public class TierRegistration 
extends SystemDemand 
{
	protected String strNodeID;
	protected String strTierID;
	
	/**
	 * @param pstrNodeID
	 * @param pstrTierID
	 */
	public TierRegistration(String pstrNodeID, String pstrTierID, 
			String pstrGMTTierID) 
	{
		super();
		this.strNodeID = pstrNodeID;
		this.strTierID = pstrTierID;
		this.oDestinationTierID = pstrGMTTierID;
	}

	public String getNodeID() 
	{
		return strNodeID;
	}

	public void setNodeID(String pstrNodeID) 
	{
		this.strNodeID = pstrNodeID;
	}

	public String getTierID() 
	{
		return strTierID;
	}

	public void setTierID(String pstrTierID) 
	{
		this.strTierID = pstrTierID;
	}
	
	
	
}
