package gipsy.GEE.IDP.DemandGenerator.jms;

import gipsy.Configuration;
import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.IntensionalDemand;
import gipsy.GEE.IDP.demands.ProceduralDemand;
import gipsy.GEE.IDP.demands.ResourceDemand;
import gipsy.GEE.IDP.demands.SystemDemand;
import gipsy.util.NotImplementedException;
import gipsy.util.Trace;

import java.io.Serializable;
import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import marf.util.Debug;

import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.ConnectionConfiguration;

/**
 * The JMS TA. Originally named DMSJMS, then JMSTA, now JMSTransportAgent.
 * It has been refactored to implement more generic methods defined
 * in ITransportAgent. 
 * 
 * @author Emil Vassev
 * @author Amir Pourteymour
 * @author Serguei Mokhov
 * @author Yi Ji
 * @version $Id: JMSTransportAgent.java,v 1.48 2011/01/11 23:23:30 ji_yi Exp $
 */
public class JMSTransportAgent 
implements Serializable, IJMSTransportAgent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5925533961064772409L;
//	private String strClientIPAddr = ""; 

	//*****************************************************
	/** 
	 * Configuration constants
	 */
    public static final String PENDING_DEMAND_QUEUE_NAME = "gipsy.GEE.TA.jms.queue.pending";
    public static final String INPROCESS_DEMAND_QUEUE_NAME = "gipsy.GEE.TA.jms.queue.inprocess";
    public static final String COMPUTED_DEMAND_QUEUE_NAME = "gipsy.GEE.TA.jms.queue.computed";
    public static final String BROKER_ADDRESS = "gipsy.GEE.TA.jms.broker.address";
    public static final String IS_TRANSACTIONAL= "gipsy.GEE.TA.jms.isTransactional";
    public static final String IS_PERSISTENT= "gipsy.GEE.TA.jms.isPersistent";
    
    /**
     * Default values
     */
    private static final String DEFAULT_BROKER_ADDRESS = "localhost:7676";
    private static final String DEFAULT_PENDING_DEMAND_QUEUE_NAME = "pending";
    private static final String DEFAULT_INPROCESS_DEMAND_QUEUE_NAME = "inprocess";
    private static final String DEFAULT_COMPUTED_DEMAND_QUEUE_NAME = "computed";
    
    /**
     * Currently each send and receive method opens and closes its own
     * connections (TCP connections), just as what Jini does.
     */
    private ConnectionFactory oConnectionFactory = null;
    private Destination oPendingDemandQ = null;
    private Destination oInprocessDemandQ = null;
    private Destination oComputedDemandQ = null;
	
    private boolean bIsTransactional = false;
    private boolean bIsPersistent = false;
    
    private static final String DEMAND_SIGNATURE = "DemandSignature";
    private static final String DEMAND_DESTINATION = "DemandDestination";
	
    /**
     * Original DMSJMS feature. The flag of asynchronous or synchronous
     * way. Currently of no use.
     */
    boolean bAsynchronous = false;
	
    private Configuration oTAConfig = null;
    
    /**
     * For logging
     */
    private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";
    
    /**
     * The message contained in JMSException telling that the broker
     * is low in memory. Such message is provider dependent and 
     * we are assuming Sun's MQ 4.3 is used.
     */
    private static final String LOW_MEMORY_MSG = "Low memory";
    
    /**
     * The message contained in JMSException telling that the broker
     * is out of memory. Such message is provider dependent and 
     * we are assuming Sun's MQ 4.3 is used.
     */
    private static final String OUT_OF_MEMORY_MSG = "Write packet failed";
    private static final String BROKER_HEAP_ERROR_MSG = "Java heap space";
    private static final String DESTINATION_IS_FULL_MSG = "Destination is full";
    
    private static final String BROKER_UNREACHABLE = "Error occurred on connection creation";
    /**
     * Construct an instance providing synchronous operations.
     * @throws DMSException
     */
	public JMSTransportAgent() 
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
	 * @param poTAConfig the configuration specified.
	 */
	public JMSTransportAgent(Configuration poTAConfig)
	{	
		if(poTAConfig == null)
		{
			poTAConfig = new Configuration();
			poTAConfig.setProperty(ITransportAgent.TA_IMPL_CLASS, this.getClass().getCanonicalName());
			poTAConfig.setProperty(BROKER_ADDRESS, DEFAULT_BROKER_ADDRESS);
			poTAConfig.setProperty(PENDING_DEMAND_QUEUE_NAME, DEFAULT_PENDING_DEMAND_QUEUE_NAME);
			poTAConfig.setProperty(INPROCESS_DEMAND_QUEUE_NAME, DEFAULT_INPROCESS_DEMAND_QUEUE_NAME);
			poTAConfig.setProperty(COMPUTED_DEMAND_QUEUE_NAME, DEFAULT_COMPUTED_DEMAND_QUEUE_NAME);
		}
		
		try
		{
			this.setConfiguration(poTAConfig);
		}
		catch (DMSException oException)
		{
			// Do not complain here, complain only when the TA is being used.
		}
	}
	
	
	/**
	 * Get a demand from the queue
	 */
	@Override
	public IDemand getDemand() 
	throws DMSException 
	{
		return getDemand(DemandSignature.DWT);
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
		return getValue(poSignature, true);
	}

	@Override
	public IDemand getResultIfExists(DemandSignature poSignature)
	throws DMSException 
	{
		try
		{
			return getValue(poSignature, false);
		}
		catch(DMSException oException)
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			throw oException;
		}
	}

	@Override
	public void setClientIPAddress(String pstrIPAddress)
	{
//		this.strClientIPAddr = pstrIPAddress;
	}

	@Override
	public DemandSignature setDemand(IDemand poDemand)
	throws DMSException
	{
		if(poDemand == null)
		{
			throw new NullPointerException();
		}
		
		// Create a message queue session. All work is 
		// done within the session.
		try
		{
			Connection oConnection = this.oConnectionFactory.createConnection();
			
			ObjectMessage oEntry;
			
			Session oSession = 
				oConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			oEntry = 
				oSession.createObjectMessage(poDemand);
			oEntry.setStringProperty(DEMAND_SIGNATURE, poDemand.getSignature().toString());
			
			if(poDemand instanceof SystemDemand)
			{
				oEntry.setStringProperty(DEMAND_DESTINATION,
						(String) ((SystemDemand)poDemand).getDestinationTierID());
			}
			else if(poDemand instanceof ProceduralDemand)
			{
				oEntry.setStringProperty(DEMAND_DESTINATION, DemandSignature.DWT);
			}
			else if(poDemand instanceof IntensionalDemand)
			{
				oEntry.setStringProperty(DEMAND_DESTINATION, DemandSignature.DGT);
			}
			else if(poDemand instanceof ResourceDemand)
			{
				oEntry.setStringProperty(DEMAND_DESTINATION, DemandSignature.ANY_DEST);
			}
			else
			{
				/* 
				 * Treat unknown demand as a procedural demand by default
				 * for backward compatibility.
				 */
				oEntry.setStringProperty(DEMAND_DESTINATION, DemandSignature.DWT);
			}
			
			
			MessageProducer oMessageProducer = 
				oSession.createProducer(this.oPendingDemandQ);
			
			// send the message
			if(this.bIsPersistent)
			{
				oMessageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
			}
			else
			{
				oMessageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			}
			
			oMessageProducer.send(oEntry);
			
			oMessageProducer.close();
			oSession.close();
			oConnection.close();
			
			if(Debug.isDebugOn())
			{
				Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() + 
						"(): the PENDING demand with signature " + poDemand.getSignature() + 
						" was sent!");
			}
			
			return poDemand.getSignature();
		} 
		catch (JMSException oException)
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			/*
			 * Inspect if the exception is due to short of memory.
			 * It is JMS service provider dependent and we are assuming
			 * the Sun's MQ is being used.
			 */
			if(this.isDSTMemoryProblem(oException))
			{
				throw new DMSException(DMSException.OUT_OF_MEMORY);
			}
			else
			{
				throw new DMSException(oException);
			}
		}
		catch (Exception oException)
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			
			throw new DMSException(oException);
		}
	}

	@Override
	public DemandSignature setResult(IDemand poResult) 
	throws DMSException 
	{
		// Create a message queue session. All work is 
		// done within the session.
		try 
		{
			Connection oConnection = this.oConnectionFactory.createConnection();
			
			// Create a CLIENT_ACKNOWLEDGE session.
			// It has to be this mode to "read" messages,
			// with the pitfall that no transaction is allowed.
			Session oSession = 
				oConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer oMessageProducer = 
				oSession.createProducer(this.oComputedDemandQ);
			// Create the message
			ObjectMessage oEntry = 
				oSession.createObjectMessage(poResult);
			// Set the message property "DemandSignature" for query
			// The state does not need to be set for computed demands
			oEntry.setStringProperty(DEMAND_SIGNATURE, poResult.getSignature().toString());
			// Set the delivery mode to persistent so that
			// the results are stored in the queue, as long
			// as nobody acknowledges the message receiving.
			if(this.bIsPersistent)
			{
				oMessageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
			}
			else
			{
				oMessageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			}
			oMessageProducer.send(oEntry);
			oMessageProducer.close();
			
			// Remove the corresponding "in progress" demand if any
			MessageConsumer oMsgConsumer = 
				oSession.createConsumer(this.oInprocessDemandQ, 
						DEMAND_SIGNATURE + " = '" + poResult.getSignature() + "'");
			
			oConnection.start();
			oMsgConsumer.receive(5000);
			oConnection.stop();
			
			oMsgConsumer.close();
			oSession.close();
			oConnection.close();
			
			if(Debug.isDebugOn())
			{
				Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() + 
						"(): the COMPUTED demand with signature " + poResult.getSignature() + 
						" was sent!");
			}
			
			return poResult.getSignature();
		} 
		catch (JMSException oException)
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			/*
			 * Inspect if the exception is due to short of memory.
			 * It is JMS service provider dependent and we are assuming
			 * the Sun's MQ is being used.
			 */
			if(this.isDSTMemoryProblem(oException))
			{
				throw new DMSException(DMSException.OUT_OF_MEMORY);
			}
			else
			{
				throw new DMSException(oException);
			}
		}
		catch (Exception oException)
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			
			throw new DMSException(oException);
		}
	}

	/*
	 * To be added
	 */

	private IDemand getValue(DemandSignature poSignature, boolean pbIsWaiting) 
	throws DMSException 
	{
		if(poSignature == null)
		{
			throw new NullPointerException();
		}
		
		try
		{
			Connection oConnection = this.oConnectionFactory.createConnection();
			
			IDemand oResult = null;
			
			// Original DMSJMS feature: the asychronous way
			if (bAsynchronous)
			{
				Debug.debug("********************");
				Debug.debug(bAsynchronous);
				Debug.debug("Consumer is started.");
				Session oSession = 
					oConnection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
				MessageConsumer consumer = oSession.createConsumer(this.oComputedDemandQ, "demandType = 'result'");
				Debug.debug("Listener is started.");
				JMSAsynchListener oListener = new JMSAsynchListener();
				consumer.setMessageListener(oListener);
				Debug.debug("Consumer is set.");
				oConnection.start();
				Debug.debug("Connection is started.");
				
				Debug.debug("one step before the result");
				while (oResult == null)
				{
					oResult = (IDemand) oListener.getMessage();
				}
				oListener.setMessage(null);
				return oResult;
			}
			else
			{
				// Check if there is already a result in the store
				ObjectMessage oEntry = null;
				Session oSession = 
					oConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				
				String strSelector = DEMAND_SIGNATURE + " = '" + poSignature + "'";
				QueueBrowser oBrowser = oSession.createBrowser(
						(Queue) this.oComputedDemandQ, strSelector);
				
				while(true)
				{
					Enumeration<?> oMessages = oBrowser.getEnumeration();
					
					if(oMessages.hasMoreElements())
					{
						oEntry = (ObjectMessage) oMessages.nextElement();
						oResult = (IDemand)oEntry.getObject();
						break;
					}
					else
					{
						if(!pbIsWaiting)
						{
							break;
						}
					}
				}
				
				oSession.close();
				
				if(oEntry == null && pbIsWaiting)
				{
					throw new DMSException("The JMS connection is offline!");
				}
			}
			oConnection.close();
			return oResult;
		}
		catch (JMSException oException)
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			/*
			 * Inspect if the exception is due to short of memory.
			 * It is JMS service provider dependent and we are assuming
			 * the Sun's MQ is being used.
			 */
			if(this.isDSTMemoryProblem(oException))
			{
				throw new DMSException(DMSException.OUT_OF_MEMORY);
			}
			else
			{
				throw new DMSException(oException);
			}
		}
		catch (DMSException oException)
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			throw oException;
		}
		catch (Exception oException)
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			
			throw new DMSException(oException);
		}
	}	

	@Override
	public IDemand getDemand(String pstrDestination) 
	throws DMSException 
	{
		try
		{
			Connection oConnection = this.oConnectionFactory.createConnection();
			Session oSession = null;
			
			if(this.bIsTransactional)
			{
				oSession = oConnection.createSession(true, Session.SESSION_TRANSACTED);
			}
			else
			{
				oSession = oConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			}
			
			IDemand oDemand = null;
			
			// Original DMSJMS feature: the asychronous way
			if (bAsynchronous)
			{
				Debug.debug("********************");
				Debug.debug(bAsynchronous);
				Debug.debug("Consumer is started.");
				
				MessageConsumer consumer = oSession.createConsumer(this.oPendingDemandQ, 
						DEMAND_DESTINATION + " = '" + pstrDestination + "'" );
				Debug.debug("Listener is started.");
				JMSAsynchListener oListener = new JMSAsynchListener();
				consumer.setMessageListener(oListener);
				Debug.debug("Consumer is set.");
				oConnection.start();
				Debug.debug("Connection is started.");
				Debug.debug("one step before the result");
				while (oDemand == null)
				{
					oDemand = (IDemand) oListener.getMessage();
				}
				oListener.setMessage(null);
			}
			else
			{
				
				// Create a AUTO_ACKNOWLEDGE session to take a
				// DispatcherEntry out of the queue

				MessageConsumer oMessageConsumer = 
					oSession.createConsumer(this.oPendingDemandQ, 
							DEMAND_DESTINATION + " = '" + pstrDestination + "'" );
				
				oConnection.start();
				ObjectMessage oEntry = (ObjectMessage)oMessageConsumer.receive();
				oConnection.stop();
				
				if(oEntry == null) // Possibly because the connection was down.
				{
					throw new DMSException("The JMS connection is broken!");
				}
				
				// Get the demand
				oDemand = (IDemand)oEntry.getObject();
				oMessageConsumer.close();
				
				// Now write the demand back with the new state "in progress"
				MessageProducer oMessageProducer = 
					oSession.createProducer(this.oInprocessDemandQ);
				
				// Stripe the demand body
				oEntry.clearBody();
				
				// Use its original delivery mode.
				//oMessageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				oMessageProducer.send(oEntry);
				
				oMessageProducer.close();
			}
			
			if(oSession.getTransacted())
			{
				oSession.commit();
			}
			
			oSession.close();
			oConnection.close();
			return oDemand;
		}
		catch (JMSException oException)
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			/*
			 * Inspect if the exception is due to short of memory.
			 * It is JMS service provider dependent and we are assuming
			 * the Sun's MQ is being used.
			 */
			if(this.isDSTMemoryProblem(oException))
			{
				throw new DMSException(DMSException.OUT_OF_MEMORY);
			}
			else
			{
				throw new DMSException(oException);
			}
		}
		catch (DMSException oException)
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			throw oException;
		}
		catch (Exception oException)
		{
			if(Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			
			throw new DMSException(oException);
		}
	}

	@Override
	public Configuration getConfiguration() 
	{
		return this.oTAConfig;
	}


	@Override
	public void release() 
	{
		//XXX Only useful when a shared connection was used.
	}


	@Override
	public void setConfiguration(Configuration poTAConfig) 
	throws DMSException 
	{
		if(poTAConfig == null)
		{
			if(Debug.isDebugOn())
			{
				Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() + 
						"(): [" + Trace.getCallerClassName() + "]."
						+ Trace.getCallerMethodName() + 
						"() was trying to set a null configuration!");
			}
			
			throw new DMSException("The TA configuration specified by is " +
					sun.reflect.Reflection.getCallerClass(2).getName()
					+ "null!");
		}
		
		if(this.oTAConfig == null || !this.oTAConfig.equals(poTAConfig))
		{
			this.oTAConfig = poTAConfig;
			String strBrokerAddress = this.oTAConfig.getProperty(BROKER_ADDRESS);
			String strPendingDemandQName = this.oTAConfig.getProperty(PENDING_DEMAND_QUEUE_NAME);
			String strInprocessDemandQName = this.oTAConfig.getProperty(INPROCESS_DEMAND_QUEUE_NAME);
			String strComputedDemandQName = this.oTAConfig.getProperty(COMPUTED_DEMAND_QUEUE_NAME);
			this.bIsTransactional = Boolean.parseBoolean(this.oTAConfig.getProperty(IS_TRANSACTIONAL));
			this.bIsPersistent = Boolean.parseBoolean(this.oTAConfig.getProperty(IS_PERSISTENT));
				
			this.oConnectionFactory = new ConnectionFactory();
			
			try 
			{
				this.oConnectionFactory.setProperty(ConnectionConfiguration.imqAddressList, 
						strBrokerAddress);
				this.oPendingDemandQ = new com.sun.messaging.Queue(strPendingDemandQName);
				this.oInprocessDemandQ = new com.sun.messaging.Queue(strInprocessDemandQName);
				this.oComputedDemandQ = new com.sun.messaging.Queue(strComputedDemandQName);
				
				/*
				 * Verify the configuration.
				 */
				Connection oConnection = this.oConnectionFactory.createConnection();
				oConnection.close();
			} 
			catch (JMSException oException)
			{
				if(Debug.isDebugOn())
				{
					oException.printStackTrace(System.err);
				}
				/*
				 * Inspect if the exception is due to short of memory.
				 * It is JMS service provider dependent and we are assuming
				 * the Sun's MQ is being used.
				 */
				if(this.isDSTMemoryProblem(oException))
				{
					throw new DMSException(DMSException.OUT_OF_MEMORY);
				}
				else
				{
					throw new DMSException(oException);
				}
			}
			catch (Exception oException)
			{
				if(Debug.isDebugOn())
				{
					oException.printStackTrace(System.err);
				}
				
				throw new DMSException(oException);
			}
		}
		
	}
	
	private boolean isDSTMemoryProblem(JMSException poException)
	{		
		if(!poException.getMessage().contains(BROKER_UNREACHABLE))
		{
			//poException.printStackTrace(System.err);
			return false;
		}
		else
		{
			return true;
		}
	}
}
