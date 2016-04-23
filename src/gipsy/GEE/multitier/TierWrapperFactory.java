package gipsy.GEE.multitier;

import java.lang.reflect.Constructor;

import gipsy.Configuration;


/**
 * This is a TierFactory implementation using reflection and configuration 
 * to mask the differences in the creation (instantiation) of different types
 * of tiers. Its existence greatly reduces the necessity of the existence of
 * abstract TierFactory, however, the TierFactory is still kept in case of
 * other factory implementation in the future.
 * 
 * Currently this class is made a singleton so that all tier controllers 
 * within the same GIPSYNode process share the same TierWrapperFactory 
 * instance.
 * 
 * @author Yi Ji
 * @version $Id: TierWrapperFactory.java,v 1.3 2012/06/16 03:10:40 mokhov Exp $
 */
public class TierWrapperFactory 
extends TierFactory 
{
	/**
	 * 
	 */
	private static TierWrapperFactory soInstance = new TierWrapperFactory();
	
	/**
	 * Create a factory instance 
	 */
	private TierWrapperFactory() 
	{
	}
	
	/**
	 * @return
	 */
	public static TierWrapperFactory getInstance()
	{
		return soInstance;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.TierFactory#createTier(gipsy.Configuration)
	 */
	@Override
	public IMultiTierWrapper createTier(Configuration poConfiguration)
	throws MultiTierException 
	{
		try 
		{
			String strWrapperImplClassName = poConfiguration.getProperty(IMultiTierWrapper.WRAPPER_IMPL_CLASS);
			Class<?> oWrapperImplClass = Class.forName(strWrapperImplClassName);
			Class<?>[] aoParamTypes = new Class[] { Configuration.class };
			Constructor<?> oWrapperImplConstructor = oWrapperImplClass.getConstructor(aoParamTypes);
			Object[] aoArgs = new Object[] { poConfiguration };
			IMultiTierWrapper oWrapper = (IMultiTierWrapper)oWrapperImplConstructor.newInstance(aoArgs);
			return oWrapper;
		} 
		catch(Exception oException) 
		{
			oException.printStackTrace(System.err);
			throw new MultiTierException(oException);
		}
	}
}

// EOF
