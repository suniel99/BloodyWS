package gipsy.GEE.multitier;

import gipsy.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Node Controller for each GIPSY Node. That is what the GIPSY Manager 
 * Tier is interacting with in order to create a GIPSY network. 
 * A singleton pattern is used to create a new instance.
 *
 * @author Bin Han
 * @author Serguei Mokhov
 * @version $Id: NodeController.java,v 1.9 2010/12/24 21:42:44 ji_yi Exp $
 * @since
 */
public abstract class NodeController
implements INodeController
{
	/**
	 * A tier factory that can be called when new tiers need to be started.
	 */
	protected TierFactory oTierFactory;

	/**
	 * List of all tiers residing on the node.
	 */
	//protected List oTierList;
	protected List<IMultiTierWrapper> oTierList = Collections.synchronizedList(new ArrayList<IMultiTierWrapper>());

	/**
	 * List of all tier configurations that can be used to allocate a new tier.
	 */
	protected List<Configuration> oAvailableTierConfigs = new ArrayList<Configuration>();

	/**
	 * The TA fixer, i.e. whenever a TA exception is met, this handler is called
	 * to fix TA. There should only be one such fixer instance within a GIPSY node.
	 */
	protected TAExceptionHandler oTAExceptionHandler = null;
	
	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.INodeController#addTier(gipsy.Configuration)
	 */
	@Override
	public IMultiTierWrapper addTier(Configuration poTierConfig)
	throws MultiTierException
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.INodeController#addTierConfig(gipsy.Configuration)
	 */
	@Override
	public void addTierConfig(Configuration poTierConfig)
	{
		this.oAvailableTierConfigs.add(poTierConfig);
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.INodeController#getTierConfigs()
	 */
	@Override
	public List<Configuration> getTierConfigs()
	{
		return this.oAvailableTierConfigs;
	}
	
	public void setTAExceptionHandler(TAExceptionHandler poTAExceptionHandler)
	{
		this.oTAExceptionHandler = poTAExceptionHandler;
	}
	
}

// EOF
