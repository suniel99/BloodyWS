package gipsy.GEE.multitier.DST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import marf.util.Debug;

import gipsy.Configuration;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.IMultiTierWrapper;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.NodeController;
import gipsy.GEE.multitier.TierWrapperFactory;
import gipsy.util.Trace;

/**
 * Demand Store Tier Controller class. On each GIPSY node, one of each type of controller 
 * is active, which allows to call the factory to create tiers of the respective type, 
 * and later control tiers of this type on a GIPSY node. 
 * 
 * @author paquet
 * @author Yi Ji 
 * @version $Id: DSTController.java,v 1.13 2010/12/29 21:01:59 ji_yi Exp $
 * 
 */
public class DSTController
extends NodeController
{
	private Map<String, DSTWrapper> oActiveDSTs = new HashMap<String, DSTWrapper>();
	private long lTierInstanceCounter = 0;
	
	/**
	 * For logging
	 */
	private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";
	
	public DSTController()
	{
		//this.oTierFactory = new DSTFactory();
//		Collection class downcast function.
//		this.oTierList = Collections.synchronizedList(new ArrayList<DSTWrapper>());
	}
	/* 
	 * @see gipsy.GEE.multitier.NodeController#addTier(gipsy.GEE.multitier.DMFImp)
	 */
	@Override
	public void addTier(EDMFImplementation poDMFImp) 
	{
		//this.oTierList.add(this.oTierFactory.createTier(poDMFImp));
	}

	/* 
	 * @see gipsy.GEE.multitier.NodeController#removeTier(gipsy.GEE.multitier.DMFImp)
	 */
	@Override
	public void removeTier(EDMFImplementation poDMFImp) 
	{
//		Under construction.
	}
	@Override
	public void addTier() 
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeTier() 
	{
		// TODO Auto-generated method stub
		
	}

	public IMultiTierWrapper addTier(Configuration poTierConfig)
	throws MultiTierException
	{
		String strTierID = poTierConfig.getProperty(IMultiTierWrapper.WRAPPER_TIER_ID);
		
		if(Debug.isDebugOn())
		{
			Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() + 
					"():  DST TierID found in configuration: " + strTierID);
		}
		
		DSTWrapper oDST = this.oActiveDSTs.get(strTierID);
		
		if(oDST == null)
		{
			if(Debug.isDebugOn())
			{
				Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() + 
						"(): Starting new DST with future ID " + this.lTierInstanceCounter);
			}
			
			oDST = (DSTWrapper) TierWrapperFactory.getInstance().createTier(poTierConfig);
			
			if(strTierID == null)
			{
				oDST.setTierID(this.lTierInstanceCounter + "");
				this.lTierInstanceCounter++;
			}
			else
			{
				oDST.setTierID(strTierID);
			}
			
			this.oActiveDSTs.put(oDST.getTierID(), oDST);
		}
		else
		{
			if(Debug.isDebugOn())
			{
				Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() + 
						"(): Starting old DST with ID " + strTierID);
			}
		}
		
		return oDST;
	}
	
	public List<Configuration> getTierConfigs()
	{
		return this.oAvailableTierConfigs;
	}
}
