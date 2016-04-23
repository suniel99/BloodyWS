package gipsy.GEE.IDP;

import gipsy.Configuration;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;

import java.io.Serializable;


/**
 * A unification interface for all transport agents (TAs) 
 * in DMF and DMS. All TAs must implement this. This interface
 * is a super-interface for use by the multi-tier architecture.
 * 
 * @author Serguei Mokhov
 * @author Yi Ji
 * 
 * @since Thu Oct 23 2008
 * @version $Id: ITransportAgent.java,v 1.17 2010/12/22 02:25:57 ji_yi Exp $
 * 
 * @see gipsy.GEE.IDP.DemandGenerator.jini.rmi.IJINITransportAgent
 * @see gipsy.GEE.IDP.DemandGenerator.jms.IJMSTransportAgent
 */
public interface ITransportAgent
extends Serializable
{
	/**
	 * The value of this configuration property specifies the TA
	 * implementation class. With this configuration property 
	 * specified in its configuration a TA implementation can 
	 * inform the TAFactory who should use its configuration.
	 */
	public static final String TA_IMPL_CLASS = "gipsy.GEE.TA.implementation";
	
	/**
	 * Get a pending demand to be picked up by any tier from the store.
	 * This is usually a blocking method.
	 * @return demand reference
	 * @throws DMSException
	 */
	IDemand getDemand() 
	throws DMSException;
	
	/**
	 * Get any pending demand if it exists.
	 * This is a non-blocking method and may return null.
	 * @return demand reference if exists or null
	 * @throws DMSException
	 */
	IDemand getDemandIfExists() 
	throws DMSException;
	
	/**
	 * Get a pending demand sent to the specified destination
	 * 
	 * @param pstrDestination - The destination to which the demand is sent.
	 * Usually the value of this parameter is tier ID.
	 * @return - The demand sent to the specified destination
	 * @throws DMSException
	 */
	IDemand getDemand(String pstrDestination)
	throws DMSException;
	
	
	/**
	 * Get the result of the demand with the specified signature
	 * from the store. Once called, this method would wait until
	 * there is a result or an exception emerges.
	 * @param poSignature the demand signature
	 * @return the result of the computed demand
	 * @throws DMSException
	 */
	IDemand getResult(DemandSignature poSignature) 
	throws DMSException;
	
	/**
	 * Peek to see if there is a result for the specified demand,
	 * and return it if there is any. This method does not block
	 * the caller's thread.
	 * 
	 * @param poSignature the demand signature
	 * @return the result, could be null
	 * @throws DMSException
	 */
	IDemand getResultIfExists(DemandSignature poSignature) 
	throws DMSException;

	/**
	 * Puts the demand into the store.
	 * @param poDemand the demand requiring computation
	 * @return the signature of the demand
	 * @throws DMSException
	 */
	DemandSignature setDemand(IDemand poDemand) 
	throws DMSException;

	/**
	 * Puts the result back into the store.
	 * @param poResult the computed result
	 * @return the signature of the result
	 * @exception DMSException
	 */
	DemandSignature setResult(IDemand poResult) 
	throws DMSException;

	/**
	 * Client IP address setting.
	 * XXX: review or document of whether we still need it here.
	 * @param pstrIPAddress
	 */
	@Deprecated
	void setClientIPAddress(String pstrIPAddress);
	
	/**
	 * Returns the configuration of this TA
	 * @return - The configuration of this TA.
	 */
	Configuration getConfiguration();
	
	/**
	 * Set the configuration of the TA, resulting in TA refresh itself
	 * according to the new configuration.
	 * 
	 * @param poTAConfig - The new configuration of this TA
	 * @throws DMSException
	 */
	void setConfiguration(Configuration poTAConfig)
	throws DMSException;
}

// EOF
