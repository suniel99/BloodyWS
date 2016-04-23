/**
 * 
 */
package gipsy.GEE.multitier.DST.jms;

import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.jmq.jmsclient.Debug;

import gipsy.Configuration;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jms.JMSTransportAgent;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.DST.DSTWrapper;
import gipsy.util.Trace;

/**
 * The DST Wrapper who is able to start up a new JMS broker process 
 * and set physical queues. For instruction on how to start and manage
 * JMS broker and queues from command line, please consult the document
 * "Sun Java SystemMessageQueue 4.3 Administration Guide"
 * 
 * @author Yi Ji
 * @version $Id: JMSDSTWrapper.java,v 1.9 2010/12/30 23:57:59 ji_yi Exp $
 */
public class JMSDSTWrapper 
extends DSTWrapper 
{
	/**
	 * The value of this configuration propety specifies how to
	 * run imqbrokerd in the system.
	 */
	private static final String IMQBROKERD_CMD = "gipsy.GEE.multitier.DST.jms.imqbrokerd.cmd";
	
	/**
	 * The value of this configuration property specifies user
	 * customized options to be passed to imqbrokerd.
	 */
	private static final String IMQBROKERD_OPTION = "gipsy.GEE.multitier.DST.jms.imqbrokerd.option";
	
	/**
	 * The value of this configuration property specifies how to 
	 * run imqcmd in the system. 
	 */
//	private static final String IMQCMD_CMD = "gipsy.GEE.multitier.DST.jms.imqcmd.cmd";
	
	/**
	 * The value of this configuration property specifies user
	 * customized options to be passed to imqcmd.
	 */
//	private static final String IMQCMD_OPTION = "gipsy.GEE.multitier.DST.jms.imqcmd.option";
	
	
	private static final String BROKER_NAME = "gipsy.GEE.multitier.DST.jms.broker.name";
	
	private static final String BROKER_PORT = "gipsy.GEE.multitier.DST.jms.broker.port";
	
	/**
	 * The prefix used to generate broker name.
	 */
	private static final String BROKER_NAME_PREFIX = "DST";
	
	/**
	 * The name of the broker instance started by this DST wrapper
	 */
	private String strBrokerName = null;
	
	/**
	 * The port of where the PortMapper of the broker instance resides.
	 */
	private int iBrokerPort = 0;
	
	
	/**
	 * The TA configuration exposed by this DST.
	 */
	private Configuration oTAConfig = null;
	
	
	/**
	 * For logging
	 */
	private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";
	
	public JMSDSTWrapper(Configuration poConfiguration)
	{
		this.oConfiguration = poConfiguration;
	}
	
	
	@Override
	public void startTier() 
	throws MultiTierException
	{
		try
		{
			String strDSTWorkingDir = this.oConfiguration.getProperty(DSTWrapper.DST_WORKING_DIR);
			String strImqbrokerdCMD = this.oConfiguration.getProperty(IMQBROKERD_CMD);
			String strImqbrokerdOption = this.oConfiguration.getProperty(IMQBROKERD_OPTION);
//			String strImqcmdCMD = this.oConfiguration.getProperty(IMQCMD_CMD);
//			String strImqcmdOption = this.oConfiguration.getProperty(IMQCMD_OPTION);
			
			this.strBrokerName = this.oConfiguration.getProperty(BROKER_NAME);
			
			try
			{
				this.iBrokerPort = Integer.parseInt(this.oConfiguration.getProperty(BROKER_PORT));
			}
			catch(NumberFormatException oException)
			{
				this.iBrokerPort = 0;
			}
			
			String[] astrQueueNames = { 
				this.oConfiguration.getProperty(JMSTransportAgent.PENDING_DEMAND_QUEUE_NAME),
				this.oConfiguration.getProperty(JMSTransportAgent.INPROCESS_DEMAND_QUEUE_NAME),
				this.oConfiguration.getProperty(JMSTransportAgent.COMPUTED_DEMAND_QUEUE_NAME)
			};
			
			/*
			 * Now determine the name and port of the new broker instance.
			 * By default Sun's implementation assume the broker name is 
			 * imqbrokerd and the port is 7676. In our case we assume broker
			 * names begin with DST and ends with tierID, e.g. DST0 and DST1.
			 * For the port number, we use a ServerSocket to get us a free
			 * port number then destroy the server socket.
			 */
			
			if(this.strBrokerName == null) // If no broker name was assigned
			{
				this.strBrokerName = BROKER_NAME_PREFIX + this.strTierID;
			}
			
			if(this.iBrokerPort == 0)
			{
				ServerSocket oPortFinder = new ServerSocket(0);
				this.iBrokerPort = oPortFinder.getLocalPort();
				oPortFinder.close();
			}
			
			// Compose the command to start a JMS broker
			String strDSTLaunchCMD = strImqbrokerdCMD
						+ " -name " + this.strBrokerName
						+ " -port " + this.iBrokerPort
						+ " " + strImqbrokerdOption
						+ " -jrehome ?"; 
			
			String[] astrCommand = strDSTLaunchCMD.split("\\s+");
			
			// Now pass the jre path to these commands.
			String strJREHome = System.getProperty("java.home");
			
			// 1. Deal with space in the path name)
			if(strJREHome.contains(" "))
			{
				// Either quoated with "" or ''
				if(System.getProperty("os.name").startsWith("Windows"))
				{
					strJREHome = "\"" + strJREHome + "\"";
				}
				else
				{
					strJREHome = "\'" + strJREHome + "\'";
				}
			}
			
			// 2. Replace the ? with JRE home path
			astrCommand[astrCommand.length-1] = strJREHome;
			
			// Build the process who executes the demand
			ProcessBuilder oProcBuilder = new ProcessBuilder(astrCommand);
			
			// Set its working directory.
			File oDSTWorkingDIR = new File(strDSTWorkingDir);
			oProcBuilder.directory(oDSTWorkingDIR);
			
			/* 
			 * Start the process to execute the demand.
			 * Note: this process is not necessarily the process 
			 * of the real DST.
			 */
			oProcBuilder.start();
			
			// Allow several seconds for the broker to start.
			Thread.sleep(5000);
			
			// Now investigate if the broker has been started
			
			String strBrokerAddress = 
				InetAddress.getLocalHost().getHostName() + ":" + this.iBrokerPort;
			
			ConnectionFactory oFactory = new ConnectionFactory();
			oFactory.setProperty(ConnectionConfiguration.imqAddressList, strBrokerAddress);
			
			// Only try three times
			for(int i = 0; i<3; i++)
			{
				try
				{
					Connection oConnection = oFactory.createConnection();
					
					Session oSession = oConnection.createSession(false,
							Session.AUTO_ACKNOWLEDGE);
					
					String strPingMsg = "Ping";
					
					for(int j = 0; j < astrQueueNames.length; j++)
					{
						Destination oDest = new com.sun.messaging.Queue(astrQueueNames[j]);
						
						// Create a message producer.
						MessageProducer oProducer = oSession.createProducer(oDest);
						
						// Create a message consumer.
						MessageConsumer oConsumer = oSession.createConsumer(oDest, 
								strPingMsg + "='" + strPingMsg + "'");
						
						// Create a message.
						
						
						TextMessage oTextMsg = oSession.createTextMessage(strPingMsg);
						
						oTextMsg.setStringProperty(strPingMsg, strPingMsg);
						
						// Non-persistent message.
						oTextMsg.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
						
						// Send the message to the destination.
						oProducer.send(oTextMsg);
						
						oProducer.close();
						
						// Start the connection.
						oConnection.start();
						
						// Receive messages from the destination.
						Message oIncomingMsg = oConsumer.receive();
						
						// Clear all messages from the destination.
						while(oIncomingMsg != null)
						{
							oIncomingMsg = oConsumer.receiveNoWait();
						}
						
						oConnection.stop();
						
						oConsumer.close();
					}
					
					// Close the session and the connection.
					oSession.close();
					
					oConnection.close();
					
					marf.util.Debug.debug(MSG_PREFIX + "Test of the 3 queues was successful!");
					
					break;
				}
				
				catch(Exception oException)
				{
					if(i<2)
					{
						Thread.sleep(3000);
					}
					else
					{
						if(marf.util.Debug.isDebugOn())
						{
							oException.printStackTrace(System.err);
						}
						
						throw new MultiTierException("The JMS Broker " 
								+ this.strBrokerName 
								+ " could not be started at port " 
								+ this.iBrokerPort);
					}
					
				}
			}
			
			System.out.println(MSG_PREFIX + "JMS Broker " + this.strBrokerName 
								+ " started at port " + this.iBrokerPort);
			
			this.oConfiguration.setProperty(BROKER_NAME, strBrokerName);
			this.oConfiguration.setProperty(BROKER_PORT, iBrokerPort + "");
			
			// Now generate the TA configuration
			
			synchronized(this)
			{
				this.oTAConfig = new Configuration();
				this.oTAConfig.setProperty(ITransportAgent.TA_IMPL_CLASS, 
						JMSTransportAgent.class.getCanonicalName());
				
				// Transfer all TA relevant properties to the TA configuration.
                
                Set<Entry<Object, Object>> oProperties = this.oConfiguration.getConfigurationSettings().entrySet();
                
                Iterator<Entry<Object, Object>> oIterator = oProperties.iterator();
                
                while(oIterator.hasNext())
                {
                	Entry<Object, Object> oEntry = oIterator.next();
                	
                	String strProperty = (String)oEntry.getKey();
                	
                	if(strProperty.startsWith("gipsy.GEE.TA.jms"))
                	{
                		this.oTAConfig.setProperty(strProperty, (String)oEntry.getValue());
                	}
                }
				
                this.oTAConfig.setProperty(JMSTransportAgent.BROKER_ADDRESS, strBrokerAddress);
                
			}
			
		}
		catch(MultiTierException oException)
		{
			if(marf.util.Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			throw oException;
		}
		catch(Exception oException)
		{
			if(marf.util.Debug.isDebugOn())
			{
				oException.printStackTrace(System.err);
			}
			throw new MultiTierException(oException);
		}
	}


	@Override
	public synchronized Configuration exportTAConfig() 
	{
		return this.oTAConfig;
	}
		
}
