package gipsy.GEE.multitier.GMT.demands;

import java.util.List;
import java.util.UUID;

import gipsy.Configuration;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.SystemDemand;
import gipsy.GEE.multitier.GIPSYNode;

/**
 * The system demand to register a GIPSY node.
 * This demand notifies the GIPSY Instance manager what tier types
 * are available to the node and their maximum occurrence allowed.
 * 
 * @author Yi Ji
 * @version $Id: NodeRegistration.java,v 1.2 2010/10/14 15:25:41 ji_yi Exp $
 */
public class NodeRegistration 
extends SystemDemand 
{	
	private String strNodeID;
	
	private String strHostName;
	
	// The List interface does not inherit Serializable.
	private List<Configuration> oAvailableDSTConfigs = null;//new ArrayList<Configuration>();
	private List<Configuration> oAvailableDWTConfigs = null;
	private List<Configuration> oAvailableDGTConfigs = null;
	
	public NodeRegistration(GIPSYNode poNode, String pstrGMTTierID)
	{
		this.strHostName = poNode.getHostName();
		
		if(poNode.getDSTController() != null && poNode.getDSTController().getTierConfigs().size() > 0)
		{
			this.oAvailableDSTConfigs = poNode.getDSTController().getTierConfigs();
		}
		
		// Currently the signature is a random UUID
		this.oSignature = new DemandSignature(UUID.randomUUID().toString());
		this.setDestinationTierID(pstrGMTTierID);
	}
	
	public String getNodeID()
	{
		return this.strNodeID;
	}
	
	public String getHostName()
	{
		return this.strHostName;
	}
	
	public void setNodeID(String pstrNodeID)
	{
		this.strNodeID = pstrNodeID;
	}
	
	public List<Configuration> getAvailableDSTConfigs()
	{
		return this.oAvailableDSTConfigs;
	}
	
	public List<Configuration> getAvailableDGTConfigs()
	{
		return this.oAvailableDGTConfigs;
	}
	
	public List<Configuration> getAvailableDWTConfigs()
	{
		return this.oAvailableDWTConfigs;
	}
	
}
