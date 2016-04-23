/**
 * 
 */
package gipsy.GEE.multitier.GMT.demands;

import gipsy.Configuration;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.SystemDemand;

/**
 * 
 * @author Yi Ji
 * @version $Id: DSTRegistration.java,v 1.5 2010/12/23 04:12:13 ji_yi Exp $
 */
public class DSTRegistration 
extends TierRegistration 
{
	// To be completed by the Node
	private Configuration oTAConfig;
	private Configuration oUpdatedTierConfig;
	
	// To be completed by the GMT
	private ITransportAgent oTA = null;
	private int iMaxActiveConnection = 0;
	private int iActiveConnectionCount = 0;
	private long lAllocationTime;
	
	/**
	 * @param pstrNodeID
	 * @param pstrTierID
	 * @param poTAConfig
	 */
	public DSTRegistration(String pstrNodeID, 
			String pstrTierID,
			Configuration poUpdatedTierConfig,
			Configuration poTAConfig, 
			String pstrGMTTierID) 
	{
		super(pstrNodeID, pstrTierID, pstrGMTTierID);
		// Use NodeID + TierID as the signature
		this.oSignature = new DemandSignature(pstrNodeID + ":" + pstrTierID);
		this.oTAConfig = poTAConfig;
		this.oUpdatedTierConfig = poUpdatedTierConfig;
	}
	
	public Configuration getUpdatedTierConfig()
	{
		return this.oUpdatedTierConfig;
	}
	
	public void setUpdatedTierConfig(Configuration poUpdatedTierConfig)
	{
		this.oUpdatedTierConfig = poUpdatedTierConfig;
	}
	
	public Configuration getTAConfig() 
	{
		return this.oTAConfig;
	}
	public void setTAConfig(Configuration oTAConfig) 
	{
		this.oTAConfig = oTAConfig;
	}
	public ITransportAgent getTA() 
	{
		return this.oTA;
	}
	public void setTA(ITransportAgent poTA) 
	{
		this.oTA = poTA;
	}
	public int getMaxActiveConnection() 
	{
		return this.iMaxActiveConnection;
	}
	public void setMaxActiveConnection(int piMaxActiveConnection) 
	{
		this.iMaxActiveConnection = piMaxActiveConnection;
	}
	public int getActiveConnectionCount() 
	{
		return this.iActiveConnectionCount;
	}
	public void setActiveConnectionCount(int piActiveConnectionCount) 
	{
		this.iActiveConnectionCount = piActiveConnectionCount;
	}
	
	public void setAllocationTime(long plAllocationTime)
	{
		this.lAllocationTime = plAllocationTime;
	}
	
	public long getAllocationTime()
	{
		return this.lAllocationTime;
	}
	
}
