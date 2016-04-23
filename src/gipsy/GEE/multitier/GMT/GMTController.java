/**
 * 
 */
package gipsy.GEE.multitier.GMT;

import java.util.ArrayList;
import java.util.List;

import gipsy.Configuration;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.IMultiTierWrapper;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.NodeController;

/**
 * The tier controller encapsulates the creation of the GMT.
 * 
 * @author Yi Ji
 * @version $Id: GMTController.java,v 1.1 2010/10/06 00:08:59 ji_yi Exp $
 */
public class GMTController 
extends NodeController 
{

	private List<Configuration> oAvailableTierConfigs = new ArrayList<Configuration>();
	private List<GMTWrapper> oStartedGMTs = new ArrayList<GMTWrapper>();
	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.NodeController#addTier(gipsy.GEE.multitier.EDMFImplementation)
	 */
	@Override
	public void addTier(EDMFImplementation poDMFImp) 
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.NodeController#addTier()
	 */
	@Override
	public void addTier() 
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.NodeController#removeTier(gipsy.GEE.multitier.EDMFImplementation)
	 */
	@Override
	public void removeTier(EDMFImplementation poDMFImp) 
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.NodeController#removeTier()
	 */
	@Override
	public void removeTier() 
	{
		// TODO Auto-generated method stub

	}

	
	public IMultiTierWrapper addTier(Configuration poConfiguration)
	throws MultiTierException
	{
		GMTWrapper oGMT = new GMTWrapper(poConfiguration);
		this.oStartedGMTs.add(oGMT);
		return oGMT;
	}
	
	public void addTierConfig(Configuration poConfiguration)
	{
		this.oAvailableTierConfigs.add(poConfiguration);
	}
	
	public List<Configuration> getTierConfigs()
	{
		return this.oAvailableTierConfigs;
	}
}
