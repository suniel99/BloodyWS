package gipsy.GEE.multitier;

import gipsy.Configuration;


/**
 * Interface for <code>MultiTierWrapper</code>.
 * 
 * @author Bin Han
 * @author Serguei Mokhov
 * @author Yi Ji
 * 
 * @version $Id: IMultiTierWrapper.java,v 1.11 2012/04/04 13:40:22 mokhov Exp $
 * @since 
 */
public interface IMultiTierWrapper
extends Runnable
{
	/**
	 * The value of this configuration property specifies the ID of 
	 * the tier wrapper.
	 */
	public static final String WRAPPER_TIER_ID = "gipsy.GEE.multitier.wrapper.tierID";
	
	/** 
	 * The value of this configuration property specifies the complete
	 * class name of the IMultiTierWrapper implementation.
	 */
	public static final String WRAPPER_IMPL_CLASS = "gipsy.GEE.multitier.wrapper.impl";
	
	/**
	 * Start the tier instance.
	 * @throws MultiTierException
	 */
	void startTier()
	throws MultiTierException;

	/**
	 * Stop the tier instance.
	 * @throws MultiTierException
	 */
	void stopTier()
	throws MultiTierException;

	/**
	 * Set the runtime configuration settings for the tier. 
	 * @param poConfiguration
	 */
	void setConfiguration(Configuration poConfiguration);

	/**
	 * @return the configuration settings object of the tier
	 */
	Configuration getConfiguration(); 
	
	/**
	 * @return the tier ID
	 */
	String getTierID();
}

// EOF
