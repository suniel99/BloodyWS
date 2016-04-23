/**
 * 
 */
package gipsy.GEE.multitier.GMT.demands;

import java.util.UUID;

import gipsy.Configuration;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.SystemDemand;
import gipsy.GEE.multitier.TierIdentity;

/**
 * This demand is used to ask a node to allocate specified number of 
 * tier instances using the specified configuration.
 * 
 * @author Yi Ji
 * @version $Id: TierDeallocationRequest.java,v 1.1 2011/02/07 18:26:50 ji_yi Exp $
 */
public class TierDeallocationRequest 
extends SystemDemand 
{
	private String[] astrTierIDs;
	private TierIdentity oTierIdentity;
	
	public TierDeallocationRequest(String pstrNodeID, 
			TierIdentity poTierIdentity,
			String[] pstrTierIDs)
	{
		this.oSignature = new DemandSignature(UUID.randomUUID().toString());
		this.oDestinationTierID = pstrNodeID;
		this.astrTierIDs = pstrTierIDs;
		this.oTierIdentity = poTierIdentity;
	}
	
	public String[] getTierIDs()
	{
		return this.astrTierIDs;
	}
	
	public TierIdentity getTierIdentity()
	{
		return this.oTierIdentity;
	}
}
