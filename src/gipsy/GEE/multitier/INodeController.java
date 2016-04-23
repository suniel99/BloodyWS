package gipsy.GEE.multitier;

import gipsy.Configuration;

import java.util.List;


/**
 * @author Serguei Mokhov
 * @author Bin Han
 * @since December 8, 2010
 */
public interface INodeController
{

	/**
	 * Add a new tier for the node, create a new thread, 
	 * start it, and add it to the list of tiers. Receives an 
	 * EDMFImplementation object as parameter, in cases where 
	 * the Configuration object is not available.  
	 * 
	 * XXX: unfinished!
	 * 
	 * @param poDMFImp 
	 */
	void addTier(EDMFImplementation poDMFImp);

	/**
	 * Add a new tier for the node, create a new thread, 
	 * start it, and add it to the list of tiers. Reads 
	 * the Configuration object to figure out how the tier should 
	 * be configured. 
	 * 
	 * XXX: unfinished!
	 */
	void addTier();

	/**
	 * Removes a tier on the node, stop the thread and remove it 
	 * from the list of tiers. Receives an 
	 * EDMFImplementation object as parameter, in cases where 
	 * the Configuration object is not available.  
	 * 
	 * XXX: unfinished!
	 * 
	 * @param poDMFImp 
	 */
	void removeTier(EDMFImplementation poDMFImp);

	/**
	 * Removes a tier on the node, stop the thread and remove it 
	 * from the list of tiers. Reads the Configuration object to 
	 * figure out how the tier is configured.  
	 * 
	 * XXX: unfinished!
	 */
	void removeTier();

	/**
	 * XXX.
	 * @param poTierConfig
	 * @return
	 * @throws MultiTierException
	 */
	IMultiTierWrapper addTier(Configuration poTierConfig)
			throws MultiTierException;

	/**
	 * @param poTierConfig
	 */
	void addTierConfig(Configuration poTierConfig);

	/**
	 * @return
	 */
	List<Configuration> getTierConfigs();
}

// EOF
