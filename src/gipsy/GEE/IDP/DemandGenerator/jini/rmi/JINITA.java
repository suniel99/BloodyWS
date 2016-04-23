package gipsy.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.Configuration;
import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.IntensionalDemand;
import gipsy.GEE.IDP.demands.ProceduralDemand;
import gipsy.GEE.IDP.demands.ResourceDemand;
import gipsy.GEE.IDP.demands.SystemDemand;
import gipsy.util.NotImplementedException;
import gipsy.util.Trace;

import java.io.Serializable;
import java.util.UUID;

import marf.util.Debug;
import net.jini.core.entry.Entry;
import net.jini.core.lease.Lease;
import net.jini.core.lookup.ServiceID;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.Transaction.Created;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.lookup.entry.Name;
import net.jini.space.JavaSpace;


/**
 * A Jini Transport Agent that directly connects to the
 * JavaSpace. This class is revised based on the original
 * JiniDemandDispatcher composed by Emil Vassev and Serguei Mokhov.
 * 
 * @author Emil Vassev
 * @author Serguei Mokhov
 * @author Yi Ji
 * 
 * @version $Id: JINITA.java,v 1.51 2012/06/17 16:58:55 mokhov Exp $
 */
public class JINITA 
implements Serializable, ITransportAgent 
{
	/**
	 * Serial UID. 
	 */
	private static final long serialVersionUID = 4010001532671352860L;

	/** 
	 * The value of this configuration property specifies the URI of unicast lookup service.
	 */
	public static final String UNICAST_DISCOVERY_URI = "gipsy.GEE.TA.jini.discovery.unicast.URI";
	
	/**
	 * The value of this configuration property specifies the name of the multicast
	 * group. If not specified, LookupDiscovery.ALL_GROUPS will be used.
	 */
	public static final String MULTICAST_DISCOVERY_GROUP = "gipsy.GEE.TA.jini.discovery.multicast.group";
	
	/**
	 * The valud of this configuration property specifies if unicast is prefered for DST
	 * discovery. If not true, multicast discovery will be used instead.
	 */
	public static final String IS_UNICAST_PREFERRED = "gipsy.GEE.TA.jini.discovery.unicast.preferred";
	
	/**
	 * The value of this configuration property specifies the name of javaspace and 
	 * transaction services. Each Jini DST is expected to have its own transaction 
	 * service to prevent potential bottleneck caused by centralized transaction 
	 * services, therefore this name is used to locate both javaspace and transaction 
	 * services of the DST. If the transaction option is not enabled, only javaspace 
	 * will be located.
	 */
	public static final String DST_SERVICE_NAME = "gipsy.GEE.TA.jini.discovery.lookup.name";
	
	/**
	 * The value of this configuration property specifies the service ID of javaspace
	 * and transaction services. If not specified, only the service name will be used
	 * to locate the services.
	 */
	public static final String DST_JAVASPACE_SERVICE_ID = "gipsy.GEE.TA.jini.discovery.id.javaspace";
	
	public static final String DST_TXMANAGER_SERVICE_ID = "gipsy.GEE.TA.jini.discovery.id.transaction";
	
	
	/**
	 * The value of this configuration property specifies if the transaction is required
	 * for all the operations of this TA. The expected value for this property is true. 
	 * If not specified or not true, transaction will not be used.
	 */
	public static final String IS_TRANSACTIONAL = "gipsy.GEE.TA.jini.isTransactional";
	
	/**
	 * The value of this configuration property indicates if the Jini services is
	 * exported via JERI or JRMP. If not specified as true, JRMP is assumed.
	 */
	public static final String IS_JERI = "gipsy.GEE.TA.jini.isJERI";
	
	/**
	 * The javaspace service used as DST
	 */
	private JavaSpace oJavaSpace = null;
	
	/**
	 * The transaction manager service provided by the DST
	 */
	private TransactionManager oTransactionManager = null;
	
	/**
	 * The configuration of this TA.
	 */
	private Configuration oTAConfig = null;
	
	/**
	 * Default value of transaction timeout
	 */
	private static final long TRANSACTION_LIFETIME = 36000000;
	
	/**
	 * Default value of receive timeout
	 */
	private static final long RECEIVE_TIMEOUT = TRANSACTION_LIFETIME - 5000;
	
	/**
	 * For logging
	 */
	private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";
	
	/**
     * The message contained in Exception indicating that the JavaSpace
     * is short of memory. Such message is provider dependent and 
     * we are assuming services were exported by JERI.
     */
    private static final String OUT_OF_MEMORY_MSG1 = "OutOfMemoryError";
	
    /**
     * The message contained in Exception indicating that the JavaSpace
     * is short of memory. Such message is provider dependent and 
     * we are assuming services were exported by JRMP.
     */
    private static final String OUT_OF_MEMORY_MSG2 = "socket write error";
    
	/**
	 * Construct a TA instance using default settings, such as
	 * multicast discovery and anonymous JavaSpace services, etc.
	 * 
	 * @throws DMSException
	 */
	public JINITA() 
	throws DMSException
	{
		this(null);
	}

	/**
	 * Construct a TA instance using the specified configuration no
	 * matter if the configuration is correct or not. If the configuration 
	 * is null, deal with it. If the TA could not connect to the DST, let it be,
	 * because the same error will be reported and handled when the TA is 
	 * being used.
	 * 
	 * @param poTAConfig - The configuration containing the properties
	 * defined in this class.
	 */
	public JINITA(Configuration poTAConfig) 
	{
		if(poTAConfig == null)
		{
			poTAConfig = new Configuration();
			poTAConfig.setProperty(ITransportAgent.TA_IMPL_CLASS, JINITA.class.getCanonicalName());
			
			/* 
			 * By default in this case multicast discovery will be used, so: 
			 * 1. unicast discovery URI is not set;
			 * 2. javaspace name is not set.
			 */	
		}
		
		try
		{
			setConfiguration(poTAConfig);
		}
		catch(DMSException oException)
		{
			// Let it be. The same report will be reported and handled later.
		}
	}

	/**
	 * Get a demand from the queue.
	 */
	@Override
	public IDemand getDemand()
	throws DMSException
	{
		return this.getDemand((String)null);
	}

	@Override
	public IDemand getDemand(String pstrDestination) 
	throws DMSException 
	{
		try
		{
			// Prepare the template
			JiniDispatcherEntry oTemplate = new JiniDispatcherEntry();
			oTemplate.oDemandState = DemandState.PENDING;
			
			if(pstrDestination == null)
			{
				oTemplate.strDestination = DemandSignature.DWT;
			}
			else
			{
				oTemplate.strDestination = pstrDestination;
			}

			Transaction oTransaction = null;
			Created oCreated = null;
			if(this.oTransactionManager != null)
			{
				oCreated = TransactionFactory.create(this.oTransactionManager, 
						TRANSACTION_LIFETIME);
				oTransaction = oCreated.transaction;
			}
			
			JiniDispatcherEntry oEntry = null;
			
			// Loop until the demand is returned or exceptions thrown
			while(oEntry == null)
			{
				// Take an object matching the above template from the JavaSpace
				oEntry = (JiniDispatcherEntry)this.oJavaSpace.take
				(
					oTemplate,
					oTransaction, 
					RECEIVE_TIMEOUT
				);
				
				// If nothing received and transaction was created
				if(oEntry == null && oCreated != null)
				{
					 // Renew the transaction by reset it lifetime
					 oCreated.lease.renew(TRANSACTION_LIFETIME);
				}
			}
			
			IDemand oDemand = (IDemand)oEntry.oDemand;
			
			if(Debug.isDebugOn())
			{
				Debug.debug
				(
					MSG_PREFIX + "." + Trace.getEnclosingMethodName() + 
					"{}: demand " + oEntry.oSignature + " taken"
				);
			}
			
			/* 
			 * Renew the transaction for demand state update. This is based on
			 * the assumption that the logic below will not take longer time 
			 * than transaction lifetime.
			 */
			if(oCreated != null)
			{
				oCreated.lease.renew(TRANSACTION_LIFETIME);
			}
			
			// Now set the demand state to in process
			oEntry.oDemandState = DemandState.INPROCESS;
			
			// And stripe body demand body
			oEntry.oDemand = null;
			
			this.oJavaSpace.write(oEntry, oTransaction, Lease.FOREVER);
			
			if(oTransaction != null)
			{
				// Assume that the transaction expiration causes exception
				oTransaction.commit();
			}
			
			if(Debug.isDebugOn())
			{
				Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() +
						"(): demand " + oDemand.getSignature() + " state updated!");
			}
			
			return oDemand;
		}
		catch(Exception oException)
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			
			if(this.isDSTMemoryProblem(oException))
			{
				throw new DMSException(DMSException.OUT_OF_MEMORY);
			}
			else
			{
				throw new DMSException(oException);
			}
		}
	}
	
	@Override
	public IDemand getDemandIfExists()
	{
		throw new NotImplementedException();
	}

	@Override
	public IDemand getResult(DemandSignature poSignature) 
	throws DMSException 
	{
		return this.readValue(poSignature, false);
	}

	@Override
	public IDemand getResultIfExists(DemandSignature poSignature) 
	throws DMSException 
	{
		return this.readValue(poSignature, true);
	}
	
	/**
	 * Read the result of the demand with the specified signature.
	 * This method extracts the shared business logic of:
	 * ITransportAgent.getResult(poSignature) and
	 * ITransportAgent.getResultIfExists(poSignature)
	 * 
	 * @param poSignature - The demand signature
	 * @param pbIsPeeking - If the method peeks the Javaspace or not
	 * @return the result matching the signature
	 * @throws DMSException
	 */
	private IDemand readValue(DemandSignature poSignature, boolean pbIsPeeking)
	throws DMSException
	{
		try 
		{
			// Check the store to see if the demand has been computed
			JiniDispatcherEntry oEntry;
			
			// Wait here until we get the value
			oEntry = new JiniDispatcherEntry();
			oEntry.oSignature = poSignature;
			oEntry.oDemandState = DemandState.COMPUTED;
			
			if(pbIsPeeking)
			{
				oEntry = (JiniDispatcherEntry)oJavaSpace.read(oEntry, null, JavaSpace.NO_WAIT);
			}
			else
			{
				oEntry = (JiniDispatcherEntry)oJavaSpace.read(oEntry, null, Lease.FOREVER);
			}
			
			if(oEntry == null && !pbIsPeeking)
			{
				throw new DMSException("The DST is offline!");
			}
			
			if(oEntry != null)
			{
				return (IDemand)oEntry.oDemand;
			}
			else
			{
				return null;
			}
			
		} 
		catch(DMSException oException)
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			
			throw oException;
		}
		catch(Exception oException) 
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			
			if(this.isDSTMemoryProblem(oException))
			{
				throw new DMSException(DMSException.OUT_OF_MEMORY);
			}
			else
			{
				throw new DMSException(oException);
			}
		}
	}
	
	@Override
	public void setClientIPAddress(String pstrIPAddress) 
	{
		throw new NotImplementedException();
	}

	@Override
	public DemandSignature setDemand(IDemand poDemand) 
	throws DMSException 
	{
		return this.writeValue(poDemand, DemandState.PENDING);
	}

	@Override
	public DemandSignature setResult(IDemand poResult) 
	throws DMSException 
	{
		return this.writeValue(poResult, DemandState.COMPUTED);
	}
	
	/**
	 * Write the value into the store. This method extracts the
	 * mutual business logic of the Jini implementation of:
	 * ITransportAgent#setResult(poResult) and
	 * ITransportAgent#setDemand(poDemand);
	 * 
	 * @param poDemand - The demand/result to be put into the store
	 * @param poState - The state of the demand/result
	 * @return the signature
	 * @throws DMSException
	 */
	public DemandSignature writeValue(IDemand poDemand, DemandState poState) 
	throws DMSException
	{
		// Check the entry condition
		if(poDemand == null || poState == null || poDemand.getSignature() == null)
		{
			throw new DMSException("Illegal argument!");
		}
		
		// Write the value into the store
		try 
		{
			JiniDispatcherEntry oEntry;
			
			// Write the computed result to the store
			oEntry = new JiniDispatcherEntry(poDemand, poState);
			
			// If it is a pending demand
			if(poState.equals(DemandState.PENDING))
			{
				if(poDemand instanceof SystemDemand)
				{
					oEntry.strDestination = (String) ((SystemDemand)poDemand).getDestinationTierID();
				}
				else if(poDemand instanceof ProceduralDemand)
				{
					oEntry.strDestination = DemandSignature.DWT;
				}
				else if(poDemand instanceof IntensionalDemand)
				{
					oEntry.strDestination = DemandSignature.DGT;
				}
				else if(poDemand instanceof ResourceDemand)
				{
					oEntry.strDestination = DemandSignature.ANY_DEST;
				}
				else
				{
					/* 
					 * Treat unknown demand as a procedural demand by default
					 * for backward compatibility.
					 */
					oEntry.strDestination = DemandSignature.DWT;
				}
			}
			
			this.oJavaSpace.write(oEntry, null, Lease.FOREVER);
			
			// If it is a result
			if(poState.isComputed())
			{
				// Take the in-process demand from the store as many as possible
				JiniDispatcherEntry oTemplate = 
					new JiniDispatcherEntry(poDemand.getSignature(), DemandState.INPROCESS);
				oJavaSpace.take(oTemplate, null, 5000);
			}
		} 
		catch (Exception oException) 
		{
			//e.printStackTrace(System.err);
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}

			if(isDSTMemoryProblem(oException))
			{
				throw new DMSException(DMSException.OUT_OF_MEMORY);
			}
			else
			{
				throw new DMSException(oException);
			}
		}
		if(Debug.isDebugOn())
		{
			Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() + 
					"(): demand " + poDemand.getSignature() + " with state " +
					poState + " was sent!");
		}
		
		return poDemand.getSignature();
	}

	@Override
	public Configuration getConfiguration() 
	{
		return this.oTAConfig;
	}

	@Override
	public void setConfiguration(Configuration poTAConfig) 
	throws DMSException
	{
		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MSG_PREFIX + "." + Trace.getEnclosingMethodName() + "() was called by ["
				+ Trace.getCallerClassName() + "]." + Trace.getCallerMethodName() + "()"
			);
		}
		
		if(poTAConfig == null)
		{
			if(Debug.isDebugOn())
			{
				Debug.debug
				(
					MSG_PREFIX + "." + Trace.getEnclosingMethodName() + "(): [" + Trace.getCallerClassName() + "]."
					+ Trace.getCallerMethodName() + "() was trying to set a null configuration!"
				);
			}

			throw new DMSException("The TA configuration specified was null!");
		}
		
		this.oTAConfig = poTAConfig;
		
		String strUnicastDiscoveryURI = this.oTAConfig.getProperty(UNICAST_DISCOVERY_URI);
		String strDSTServiceName = this.oTAConfig.getProperty(DST_SERVICE_NAME);
		String strMulticastGroup = this.oTAConfig.getProperty(MULTICAST_DISCOVERY_GROUP);
		
		ServiceID oJavaspaceServiceID = null;
		ServiceID oTXManagerServiceID = null;
		
		String strJavaspaceServiceID = this.oTAConfig.getProperty(DST_JAVASPACE_SERVICE_ID);
		String strTXManagerServiceID = this.oTAConfig.getProperty(DST_TXMANAGER_SERVICE_ID);
		
		if(strJavaspaceServiceID != null)
		{
			oJavaspaceServiceID = getIDFromString(strJavaspaceServiceID);
		}

		if(strTXManagerServiceID != null)
		{
			oTXManagerServiceID = getIDFromString(strTXManagerServiceID);
		}
		
		//XXX TODO Defensively verify these values.
		
		// Boolean.parseBoolean(null) is expected to return false.
		boolean bIsTransactional = Boolean.parseBoolean(this.oTAConfig.getProperty(IS_TRANSACTIONAL));
		boolean bIsUnicastPreferred = Boolean.parseBoolean(this.oTAConfig.getProperty(IS_UNICAST_PREFERRED));
		
		try 
		{
			// Set up the service template that the Javaspace should match.
			if(Debug.isDebugOn())
			{
				Debug.debug
				(
					MSG_PREFIX + "." + Trace.getEnclosingMethodName() +  "() called by ["
					+ Trace.getCallerClassName() + "]."	+ Trace.getCallerMethodName()
					+ "(): JavaSpace ServiceID = " + oJavaspaceServiceID
				);
			}
			
			Class<?>[] aoServiceTypes = new Class[] {JavaSpace.class};
			Entry[] aoAttributes = null;
			
			if(strDSTServiceName != null)
			{
				aoAttributes = new Entry[] {new Name(strDSTServiceName)};
			}
			
			ServiceTemplate oTemplate = new ServiceTemplate(oJavaspaceServiceID, aoServiceTypes, aoAttributes);
	        
			IJiniServiceDiscoverer oServiceDiscoverer = null;
			
			String strOtherServiceInfo = null;
			
			// XXX
			bIsUnicastPreferred = true;
			
			// Check if unicast discovery is preferred
			if(bIsUnicastPreferred == true)
			{
				Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() + ": Preferring unicast.");
				oServiceDiscoverer = new UnicastJiniServiceDiscoverer();
				strOtherServiceInfo = strUnicastDiscoveryURI;
			}
			
			// Otherwise multicast discovery is used.
			else
			{
				Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() + ": Preferring multicast.");
				oServiceDiscoverer = new MulticastJiniServiceDiscoverer();
				strOtherServiceInfo = strMulticastGroup;
			}
			
			this.oJavaSpace = (JavaSpace)oServiceDiscoverer.getService(oTemplate, strOtherServiceInfo);
			
			if(this.oJavaSpace == null)
			{
				System.err.println("oTemplate=" + oTemplate);
				System.err.println("strOtherServiceInfo=" + strOtherServiceInfo);
				throw new DMSException("Unable to locate JavaSpace " + strDSTServiceName);
			}
			
			// Now get the transaction manager service if necessary
			if(bIsTransactional == true)
			{
				if(Debug.isDebugOn())
				{
					Debug.debug
					(
						MSG_PREFIX + "." + Trace.getEnclosingMethodName() + "() called by [" + Trace.getCallerClassName() + "]."
						+ Trace.getCallerMethodName() + "(): TxnManager ServiceID = " + oTXManagerServiceID
					);
				}
				
				aoServiceTypes = new Class[] {TransactionManager.class};
				oTemplate = new ServiceTemplate(oTXManagerServiceID, aoServiceTypes, aoAttributes);
				
				this.oTransactionManager = (TransactionManager)oServiceDiscoverer.getService(oTemplate, strOtherServiceInfo);
				if(this.oTransactionManager == null)
				{
					throw new DMSException("Unable to locate TransactionManager " + strDSTServiceName);
				}
			}
		}
		catch(DMSException oException)
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			
			throw oException;
		}
		catch(Exception oException)
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}

			if(this.isDSTMemoryProblem(oException))
			{
				throw new DMSException(DMSException.OUT_OF_MEMORY);
			}
			else
			{
				throw new DMSException(oException);
			}
		}
	}
	
	public static ServiceID getIDFromString(String pstrUUID)
	throws DMSException
	{
		ServiceID oServiceID = null;
		
		if(pstrUUID != null)
		{
			try
			{
				UUID oUUID = UUID.fromString(pstrUUID);
				long lMostSig = oUUID.getMostSignificantBits();
				long lLeastSig = oUUID.getLeastSignificantBits();
				oServiceID = new ServiceID(lMostSig, lLeastSig);
			}
			catch(IllegalArgumentException oWrongServiceIDFormat)
			{
				throw new DMSException("Wrong service ID specified!");
			}
		}

		return oServiceID;
	}
	
	private boolean isDSTMemoryProblem(Exception poException)
	{
		if(poException == null || poException.getMessage() == null)
		{
			poException.printStackTrace(System.err);
			return false;
		}
		
		if(poException.getMessage().contains(OUT_OF_MEMORY_MSG1)
				||poException.getMessage().contains(OUT_OF_MEMORY_MSG2))
		{
			return true;
		}
		
		return false;
	}
	
}
