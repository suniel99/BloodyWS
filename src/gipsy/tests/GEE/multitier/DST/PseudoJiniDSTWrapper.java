package gipsy.tests.GEE.multitier.DST;

import gipsy.Configuration;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITA;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.DST.DSTWrapper;
import gipsy.util.Trace;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import marf.util.Debug;



/**
 * A DST Wrapper who starts Jini services that are exported by JRMP 
 * (explained as classic Java RMI but no idea why called JRMP). JRMP
 * does not allow user to determine service port, therefore when a
 * service is restarted, the old proxy and service ID becomes invalid, 
 * therefore this DSW Wrapper must report GMT the new service ID and
 * the DWT or DGT who holds the old remote reference must retrieve the 
 * updated service ID from GMT to connect to the restarted service. 
 * Although more complex in dealing with service restart, JRMP offers
 * faster speed in the JavaSpace operations than JERI.
 * 
 * @author Yi Ji
 * @version $Id: PseudoJiniDSTWrapper.java,v 1.1 2011/01/10 16:51:11 ji_yi Exp $
 */
public class PseudoJiniDSTWrapper 
extends DSTWrapper 
{	
	// Constant configuration properties
	/**
	 * The TA configuration exposed by this DST.
	 */
	private Configuration oTAConfig = null;
	
	/**
	 * For logging
	 */
	private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";
	
	/**
	 * Create an instance based on configuration.
	 * 
	 * @param poDSTConfig - The DST configuration.
	 */
	public PseudoJiniDSTWrapper(Configuration poDSTConfig)
	{
		this.oConfiguration = poDSTConfig;
		if(Debug.isDebugOn())
		{
			System.out.println(MSG_PREFIX + " PseudoJiniDSTWrapper called!");
		}
	}
	
	
	public void startTier() 
	throws MultiTierException 
	{
		
		try
		{
			String strLookupServiceURI = "jini://" + InetAddress.getLocalHost().getHostName();
			boolean bIsPersistenceRequired = false;
        	
            // Generate the TA configuration for the new DST instance.
            synchronized(this)
            {
            	this.oTAConfig = new Configuration();
                this.oTAConfig.setProperty(ITransportAgent.TA_IMPL_CLASS, JINITA.class.getCanonicalName());
                
                // Transfer all TA relevant properties to the TA configuration.
                
                Set<Entry<Object, Object>> oProperties = this.oConfiguration.getConfigurationSettings().entrySet();
                
                Iterator<Entry<Object, Object>> oIter = oProperties.iterator();
                
                while(oIter.hasNext())
                {
                	Entry<Object, Object> oEntry = oIter.next();
                	
                	String strProperty = (String)oEntry.getKey();
                	
                	if(strProperty.startsWith("gipsy.GEE.TA.jini"))
                	{
                		this.oTAConfig.setProperty(strProperty, (String)oEntry.getValue());
                	}
                }
                
                // Overwrite or add updated properties.
                this.oTAConfig.setProperty(JINITA.UNICAST_DISCOVERY_URI, strLookupServiceURI);
                if(bIsPersistenceRequired)
                {
                	this.oTAConfig.setProperty(JINITA.IS_JERI, Boolean.TRUE.toString());
                }
            }
            
           
		}
		catch(Exception oException)
		{
			oException.printStackTrace(System.err);
			throw new MultiTierException(oException);
		}
	}
	
	public void stopTier()
	throws MultiTierException
	{
		
	}

	public synchronized Configuration exportTAConfig() 
	{
		return this.oTAConfig;
	}
}
