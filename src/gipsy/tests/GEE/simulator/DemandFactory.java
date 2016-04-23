package gipsy.tests.GEE.simulator;

import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.tests.GEE.simulator.demands.LightUniqueDemand;


/**
 * This is the factory for demands.
 * 
 * @author Emil Vassev
 * @version $Id: DemandFactory.java,v 1.12 2010/12/30 01:16:19 ji_yi Exp $
 * @since
 */
public class DemandFactory 
{
	/**
	 * XXX
	 */
	private DemandPool oDemandPool;
	
	/**
	 * XXX
	 */
	public DemandFactory()
	{
		if((GlobalDef.soDemandClasses.size() == 0))
		{
			try
			{
				throw new Exception("There are no demand/result classes in the pool.");
			}
			catch(Exception ex)
			{
				GlobalDef.handleCriticalException(ex);	
			}
		}
		
		this.oDemandPool = DemandPool.getInstance();
	}
	
	/**
	 * Makes an instance of the class passed as a parameter.
	 * Creates a demand associated with the parameter class and places it
	 * into the pool.
	 * 
	 * @param pstrClassName
	 */
	public void createDemand(String pstrClassName) 
	{
		try
		{
			if(GlobalDef.soDemandClasses.contains(pstrClassName))
			{
				ClassLoader oLoader = ClassLoader.getSystemClassLoader();

				Object oDemand = oLoader.loadClass
				(
					getClass().getPackage().getName()
					+ ".demands."
					+ pstrClassName
				).newInstance();
				
				// Currently all signature will be set
				// from outside except the LightUniqueDemand
				// Will be cleaned up later
				
				IDemand oTheDemand = (IDemand) oDemand;
				
				if(oTheDemand.getSignature() == null)
				{
					oTheDemand.setSignature(new DemandSignature(pstrClassName + oDemand.hashCode()));
				}
				
				this.oDemandPool.put((IDemand) oDemand);
			}
		}
		catch(Exception ex)
		{
			GlobalDef.handleCriticalException(ex);	
		}
	}
}

// EOF
