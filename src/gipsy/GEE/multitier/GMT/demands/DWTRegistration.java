/**
 * 
 */
package gipsy.GEE.multitier.GMT.demands;

import java.util.UUID;

import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.SystemDemand;

/**
 * The registration demand for either DGT or DWT registration
 * @author Yi Ji
 * @version $Id: DWTRegistration.java,v 1.1 2010/10/07 01:20:02 ji_yi Exp $
 */
public class DWTRegistration 
extends TierRegistration 
{
	
	
	/**
	 * @param pstrNodeID
	 * @param pstrTierID
	 */
	public DWTRegistration(String pstrNodeID, String pstrTierID, 
			String pstrGMTTierID) 
	{
		super(pstrNodeID, pstrTierID, pstrGMTTierID);
		this.oSignature = new DemandSignature(UUID.randomUUID().toString());
	}	
}
