package gipsy.GEE.multitier;

import gipsy.Configuration;

/**
 * Factory Pattern is applied for the creation of tiers in a GIPSY node. 
 * It makes use of the respective tier factories for the corresponding tier.  
 * 
 * @author Bin Han
 * @author Yi Ji
 * @version $Id: TierFactory.java,v 1.6 2010/12/28 16:01:58 ji_yi Exp $
 */
public abstract class TierFactory
{
	
	/**
	 * Return new subclass object of GenericTierWrapper.
	 */
	public IMultiTierWrapper createTier(EDMFImplementation poDMFImp)
	throws MultiTierException
	{
		return null;
	}
	
	/**
	 * Create a new tier wrapper instance based on configuration. 
	 * No that the configuration passed as parameter must indicate the name 
	 * of the tier wrapper implementation and critical configuration properties
	 * that are needed by the tier wrapper instance. i.e. this configuration
	 * will be used by both this factory and the tier wrapper implementation.
	 * 
	 * @param poConfiguration - The configuration telling what tier wrapper instance
	 * should use it and containing necessary properties to use by the tier wrapper
	 * instance.
	 * @return - The created tier wrapper instance.
	 * @throws MultiTierException
	 */
	public IMultiTierWrapper createTier(Configuration poConfiguration)
	throws MultiTierException
	{
		/*
		 * XXX Currently this method has a "return null" implementation to
		 * prevent unnecessary changes to subclasses that do not need this
		 * method yet. Whether or not this method should have some extracted
		 * implementation or be a complete abstract method shall depend on its
		 * functionality in all subclasses.
		 */
		return null;
	}
	
//	public IMultiTierWrapper createTier(String pstrType)
//	{
//		IMultiTierWrapper oTierWrapper = null;
//
//		if(pstrType.equals("DST"))
//		{
//			oTierWrapper = new DSTWrapper();
//		}
//		else if(pstrType.equals("DWT"))
//		{
//			oTierWrapper = new DWTWrapper();
//		}
//		else if(pstrType.equals("DGT"))
//		{
//			oTierWrapper = new DGTWrapper();
//		}
//		
//		return oTierWrapper;
//	}
}

//EOF