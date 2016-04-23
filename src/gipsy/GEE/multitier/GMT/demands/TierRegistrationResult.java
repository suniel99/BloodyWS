/**
 * 
 */
package gipsy.GEE.multitier.GMT.demands;

import gipsy.GEE.IDP.demands.SystemDemand;

/**
 * 
 * @author Yi Ji
 * @version $Id: TierRegistrationResult.java,v 1.1 2010/10/06 00:09:00 ji_yi Exp $
 */
public class TierRegistrationResult 
extends SystemDemand 
{
	private boolean isOK = false;

	public TierRegistrationResult(boolean pbIsOK)
	{
		this.isOK = pbIsOK;
	}
	
	public boolean isOK() 
	{
		return isOK;
	}

	public void setOK(boolean pbIsOK) 
	{
		this.isOK = pbIsOK;
	}
	
}
