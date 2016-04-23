package gipsy.GEE.multitier.DST;

import gipsy.Configuration;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.multitier.MultiTierException;

import java.lang.reflect.Constructor;

import marf.util.Debug;

/**
 * The factory to create a TA instance based on configuration.
 * Currently it is made a singleton to save instantiation effort.
 * 
 * @author Yi Ji
 * @version $Id: TAFactory.java,v 1.7 2012/04/08 01:10:28 mokhov Exp $
 * @since
 */
public class TAFactory 
{
	private static TAFactory soInstance = null;
	
	private TAFactory()
	{
		
	}
	
	public synchronized static TAFactory getInstance()
	{
		if(soInstance == null)
		{
			soInstance = new TAFactory();
		}
		return soInstance;
	}
	
	/**
	 * Create a TA instance based on configuration.
	 * 
	 * @param poConfiguration - The TA configuration
	 * @return the TA instance
	 * @throws MultiTierException
	 */
	public ITransportAgent createTA(Configuration poConfiguration)
	throws MultiTierException 
	{
		try 
		{
			String strTAImplClassName = poConfiguration.getProperty(ITransportAgent.TA_IMPL_CLASS);
			
			Class<?> oTAImplClass = Class.forName(strTAImplClassName);
			Class<?>[] aoParamTypes = new Class[] { Configuration.class };
			Constructor<?> oTAImplConstructor = oTAImplClass.getConstructor(aoParamTypes);
			
			Object[] aoArgs = new Object[]{poConfiguration};
			ITransportAgent oTA = (ITransportAgent)oTAImplConstructor.newInstance(aoArgs);
			return oTA;
		}
		catch(Exception oException) 
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
		
			throw new MultiTierException(oException);
		}
	}
}
