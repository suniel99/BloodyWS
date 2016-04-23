package gipsy.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.GEE.IDP.DMSException;

import java.rmi.RMISecurityManager;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceMatches;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;

/**
 * This class serving as a utility to locate a Jini service by
 * performing unicast lookup, and should be accessible only
 * by the Jini transport agents.
 * 
 * @author Yi Ji
 * @version $Id: UnicastJiniServiceDiscoverer.java,v 1.3 2012/04/09 01:28:29 mokhov Exp $
 */
public class UnicastJiniServiceDiscoverer
implements IJiniServiceDiscoverer
{	    
    /**
     * Construct a Jini service locator instance.
     */
	public UnicastJiniServiceDiscoverer() 
	{
		if(System.getSecurityManager() == null)
		{
			//System.setSecurityManager(new RMISecurityManager());
		}
	}

	/**
	 * Perform unicast discovery to get the desired Jini service.
	 * 
	 * @param poServiceTemplate the service template
	 * @param pstrLookupServiceURI the URI of the Jini lookup service
	 * 
	 * @return The proxy of the Jini service.
	 * @exception DMSException
	 */
	public Object getService(ServiceTemplate poServiceTemplate, String... pastrOtherServiceInfo)
	throws DMSException
	{
		try
		{
			String strLookupServiceURI = pastrOtherServiceInfo[0];
			LookupLocator oLookupLocator = new LookupLocator(strLookupServiceURI);
			ServiceRegistrar oRegistrar = oLookupLocator.getRegistrar();
			return oRegistrar.lookup(poServiceTemplate);
		}
		catch(Exception oException)
		{
			throw new DMSException(oException);
		}
	}

	@Override
	public ServiceMatches getServices
	(
		ServiceTemplate poServiceTemplate,
		String... pastrOtherServiceInfo
	)
	throws DMSException 
	{
		try
		{
			String strLookupServiceURI = pastrOtherServiceInfo[0];
			LookupLocator oLookupLocator = new LookupLocator(strLookupServiceURI);
			ServiceRegistrar oRegistrar = oLookupLocator.getRegistrar();
			return oRegistrar.lookup(poServiceTemplate, 100);
		}
		catch(Exception oException)
		{
			throw new DMSException(oException);
		}
	}
}
