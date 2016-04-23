/**
 * 
 */
package gipsy.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.GEE.IDP.DMSException;

import net.jini.core.lookup.ServiceMatches;
import net.jini.core.lookup.ServiceTemplate;

/**
 * Interface for both multicast and unicast Jini service discovery.
 * 
 * @author Yi Ji
 * @version $Id: IJiniServiceDiscoverer.java,v 1.2 2010/12/24 17:07:02 ji_yi Exp $
 */
public interface IJiniServiceDiscoverer 
{
	public Object getService(ServiceTemplate poServiceTemplate, String... pastrOtherServiceInfo)
	throws DMSException;
	
	public ServiceMatches getServices(ServiceTemplate poServiceTemplate, String... pastrOtherServiceInfo)
	throws DMSException;
	
}
