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
 * @version $Id: TierAllocationRequest.java,v 1.3 2010/12/28 19:06:06 ji_yi Exp $
 */
public class TierAllocationRequest 
extends SystemDemand 
{
	/**
	 * The identity of the tier. This information is not necessary to the
	 * GIPSYNode but is necessary to the GMT.
	 */
	private TierIdentity oTierIdentity = null;
	
	/**
	 * The configuration of the tier to be started.
	 */
	private Configuration oTierConfig = null;
	
	/**
	 * Indicates how many copies of tier instances to start.
	 */
	private int iNumOfInstances = 1;
	
	
	public TierAllocationRequest(String pstrNodeID, 
			TierIdentity poTierIdentity, 
			Configuration poTierConfig, 
			int piNumOfInstances)
	{
		this.oSignature = new DemandSignature(UUID.randomUUID().toString());
		this.oDestinationTierID = pstrNodeID;
		this.oTierIdentity = poTierIdentity;
		this.oTierConfig = poTierConfig;
		this.iNumOfInstances = piNumOfInstances;
	}
	
	public TierIdentity getTierIdentity() 
	{
		return this.oTierIdentity;
	}
	public void setTierIdentity(TierIdentity poTierIdentity) 
	{
		this.oTierIdentity = poTierIdentity;
	}
	public Configuration getTierConfig() 
	{
		return this.oTierConfig;
	}
	public void setTierConfig(Configuration poTierConfig) 
	{
		this.oTierConfig = poTierConfig;
	}
	
	public int getNumberOfInstances()
	{
		return this.iNumOfInstances;
	}
}
