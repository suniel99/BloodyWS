package gipsy.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.GEE.IDP.DMSException;

import java.rmi.RemoteException;

import marf.util.Debug;
import net.jini.core.lookup.ServiceMatches;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.DiscoveryEvent;
import net.jini.discovery.DiscoveryListener;
import net.jini.discovery.LookupDiscovery;


/**
 * A class serving as a utility to locate a Jini service by performing
 * multicast lookup.
 * 
 * @author Yi Ji
 * @version $Id: MulticastJiniServiceDiscoverer.java,v 1.5 2012/05/30 23:11:42 mokhov Exp $
 */
public class MulticastJiniServiceDiscoverer 
implements IJiniServiceDiscoverer
{
    /**
     * XXX.
     */
    private ServiceTemplate oServiceTemplate;
    
    /**
     * Default timeout value is 10 seconds.
     */
    private long lTimeout = 10000;
    
    
    /**
     * Construct a multicast service discoverer.
     */
	public MulticastJiniServiceDiscoverer() 
	{
        
    }

	/**
	 * XXX.
	 * @param plTimeout
	 */
	public void setTimeout(long plTimeout)
	{
		this.lTimeout = plTimeout;
	}
	
	/**
	 * @return XXX
	 */
	public long getTimeout()
	{
		return this.lTimeout;
	}
	
	
	/**
	 * Perform multicast lookup to get the desired service.
	 * 
	 * @param poServiceTemplate the template of the desired service.
	 * @param pastrOtherServiceInfo the names of the service groups. A null value 
	 *        indicates LookupDiscovery.ALL_GROUPS in Jini 2.1
	 * 
	 * @return The proxy of the Jini service.
	 * @throws DMSException 
	 * @throws InterruptedException 
	 */
	public Object getService(ServiceTemplate poServiceTemplate, String... pastrOtherServiceInfo) 
	throws DMSException 
	{
		Object oServiceProxy = null;
		
		this.oServiceTemplate = poServiceTemplate;
		
		LookupDiscovery oDiscovery = null;
		
		String[] astrGroups = pastrOtherServiceInfo;
		
		boolean bIsNullNameExisting = false;
		
		// Check if all multicast group names are not null
		for(int i = 0; i < astrGroups.length; i++)
		{
			Debug.debug("Group name (?) astrGroups[" + i + "]=" + astrGroups[i]);
	 
	    	if
	    	(
	    		astrGroups[i] == null 
	    		|| astrGroups[i].length() == 0 
	    		|| astrGroups[i].trim().equalsIgnoreCase("null")
			)
			{
				bIsNullNameExisting = true;
				break;
			}
		}
	
		try
		{
			if(bIsNullNameExisting == true)
	    	{
	    		oDiscovery = new LookupDiscovery(LookupDiscovery.ALL_GROUPS);
	    	}
	    	else
	    	{
	    		oDiscovery = new LookupDiscovery(astrGroups);
	    	}
	    	
			ServiceDiscoveryListener oDiscoveryListener = new ServiceDiscoveryListener(this.oServiceTemplate);
			
			synchronized(oDiscoveryListener)
			{
				oDiscovery.addDiscoveryListener(oDiscoveryListener);
				Debug.debug("Added discovery listerner " + oDiscoveryListener + "... waiting for a timeout...");
				oDiscoveryListener.wait(this.lTimeout * 2);
				Debug.debug("Done timeout waiting for " + (this.lTimeout * 2) + " amount of time.");
				
				oServiceProxy = oDiscoveryListener.oProxy;
				Debug.debug("Got proxy: " + oServiceProxy);
				
				oDiscovery.removeDiscoveryListener(oDiscoveryListener);
				oDiscovery.terminate();
			}
			
			oDiscoveryListener = null;
			oDiscovery = null;
			
			return oServiceProxy;
		}
		catch(Exception oException)
		{
			Debug.debug("Somehow failed: " + oException);
			oException.printStackTrace(System.err);
			throw new DMSException(oException);
		}
	}
    
    /**
     * The private discovery listener to lookup the service when the service
     * registrar is returned by multicast discovery.
     * 
     * @author Yi Ji
     */
    private static class ServiceDiscoveryListener
    implements DiscoveryListener
    {
    	private Object oProxy = null;
    	private ServiceTemplate oServiceTemplate = null;
    	private ServiceMatches oServiceMatches = null;
    	
    	private ServiceDiscoveryListener(ServiceTemplate poServiceTemplate)
    	{
    		this.oServiceTemplate = poServiceTemplate;
    	}
    	
    	
		public void discarded(DiscoveryEvent poDiscoveryEvent) 
		{
			
		}
		
		public void discovered(DiscoveryEvent poDiscoveryEvent) 
		{
			if(this.oProxy == null)
			{
				ServiceRegistrar[] oaServiceRegistrar = poDiscoveryEvent.getRegistrars();
				for (int i = 0; i < oaServiceRegistrar.length; i++) 
				{
				    ServiceRegistrar oServiceRegistrar = oaServiceRegistrar[i];
				    try 
				    {
				    	synchronized(this) 
			            {
					    	this.oProxy = oServiceRegistrar.lookup(this.oServiceTemplate);
					    	this.oServiceMatches = oServiceRegistrar.lookup(this.oServiceTemplate, 100);
					    	if (this.oProxy != null) 
					        {
					            
					            	this.notify();
					           
					            break;
					        } 
			            }
					} 
				    catch (RemoteException oException) 
				    {
						oException.printStackTrace(System.err);
					}
				}
			}
		}
    }

	@Override
	public ServiceMatches getServices(ServiceTemplate poServiceTemplate,
			String... pastrOtherServiceInfo) 
	throws DMSException 
	{
		ServiceMatches oServiceMatches = null;
    	
    	this.oServiceTemplate = poServiceTemplate;
    	
    	LookupDiscovery oDiscovery = null;
    	
    	String[] astrGroups = pastrOtherServiceInfo;
    	
    	boolean bIsNullNameExisting = false;
    	
    	// Check if all multicast group names are not null
    	for(int i = 0; i<astrGroups.length; i++)
    	{
    		if(astrGroups[i] == null 
    				|| astrGroups[i].length() == 0 
    				|| astrGroups[i].trim().equalsIgnoreCase("null"))
    		{
    			bIsNullNameExisting = true;
    			break;
    		}
    	}
    	
    	try
    	{
    		if(bIsNullNameExisting)
        	{
        		oDiscovery = new LookupDiscovery(LookupDiscovery.ALL_GROUPS);
        	}
        	else
        	{
        		oDiscovery = new LookupDiscovery(astrGroups);
        	}
        	
    		ServiceDiscoveryListener oDiscoveryListener = new ServiceDiscoveryListener(this.oServiceTemplate);
    		
    		synchronized(oDiscoveryListener)
    		{
    			oDiscovery.addDiscoveryListener(oDiscoveryListener);
    			
    			oDiscoveryListener.wait(lTimeout);
    			
    			oServiceMatches = oDiscoveryListener.oServiceMatches;
    			
    			oDiscovery.removeDiscoveryListener(oDiscoveryListener);
    			oDiscovery.terminate();
    			
    		}
    		
    		oDiscoveryListener = null;
    		oDiscovery = null;
    		
    		return oServiceMatches;
    	}
    	catch(Exception oException)
    	{
    		throw new DMSException(oException);
    	}
	}
}
