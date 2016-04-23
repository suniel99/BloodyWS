package gipsy.GEE.multitier.GMT.demands;

import gipsy.Configuration;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.SystemDemand;

/**
 * This system demand confirms node/tier registration
 * by telling the node/tier what store to use as the
 * system DST.
 * 
 * @author Yi Ji
 * @version $Id: RegistrationResult.java,v 1.3 2010/10/14 15:25:41 ji_yi Exp $
 */
public class RegistrationResult 
extends SystemDemand 
{
	private String strAssignedNodeID;
	
	private Configuration oSysDSTTAConfig;
	
	public RegistrationResult(String pstrAssignedNodeID, Configuration poSysDSTTAConfig) 
	{
		this.strAssignedNodeID = pstrAssignedNodeID;
		this.oSysDSTTAConfig = poSysDSTTAConfig;
	}

	public Configuration getSystemDSTTAConfig() 
	{
		return oSysDSTTAConfig;
	}

	public void setSystemDSTTAConfig(Configuration poSystemDSTTAConfig) 
	{
		this.oSysDSTTAConfig = poSystemDSTTAConfig;
	}
	
	public String getAssignedNodeID()
	{
		return this.strAssignedNodeID;
	}
}
