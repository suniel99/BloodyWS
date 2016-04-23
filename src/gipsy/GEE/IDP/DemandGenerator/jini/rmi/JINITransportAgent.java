package gipsy.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.Configuration;
import gipsy.GIPSY;
import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.util.NetUtils;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.MarshalledObject;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.rmi.activation.ActivationID;
import java.util.Hashtable;
import java.util.Properties;

import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceRegistration;
import net.jini.discovery.DiscoveryEvent;
import net.jini.discovery.DiscoveryListener;
import net.jini.discovery.LookupDiscovery;
import net.jini.id.Uuid;


/** 
 * This is the JINI Transport Agent implementation. 
 * It is implemented as a JINI service.
 * 
 * The initial tasks performed by the class are:
 * <ul>
 * <li>Sets a security manager.
 * <li>Runs a listener for discovering the Lookup Service.
 * <li>When LUS is discovered, registers with it publishes the Proxy.
 * <li>Connects with the Demand Dispatcher
 * </ul>
 *  
 * @author Emil Vassev 
 * @author Serguei Mokhov 
 * @author Yi Ji
 * 
 * @since 1.0.0
 * @version 2.0.0 $Id: JINITransportAgent.java,v 1.32 2010/12/22 02:25:56 ji_yi Exp $
 */
public class JINITransportAgent
implements Runnable
{
	//**** Data members
	
	/**
	 * Setup the lease time.
	 */
    protected static final int LEASE_TIME = 10 * 60 * 1000; 
	
	/**
	 * Setup the lookup group's name.
	 */
	protected static final String [] DISCOVERY_GROUP_NAMES = {"gipsy"};
	
    protected Hashtable registrations = new Hashtable();
    protected ServiceItem item;
    protected LookupDiscovery disco;
	public static  Thread serviceThread;
	
	/**
	 * Configuration settings
	 */
	protected static final String SECURITY_FILE = "jini.policy";
	protected static String sstrsCodebase = "";
	protected static String sstrIPLocalAddress = "";

	
	/**
	 * This interface defines the remote communications protocol between the
	 * client-side JTABackend stub and the service-side JTABackend object.
	 */
    interface JTABackendProtocol 
    extends Remote
	{    	
    	/*
    	 * These methods are essentially the same as what are defined in
    	 * ITransportAgent, the only difference is the RemoteException.
    	 */
		public IDemand fetchDemand() 
		throws RemoteException, DMSException;

		public IDemand fetchDemandIfExists() 
		throws RemoteException, DMSException;

		public IDemand fetchDemand(String pstrDestination) 
		throws RemoteException, DMSException;

		public IDemand fetchResult(DemandSignature poSignature)
		throws RemoteException, DMSException;

		public IDemand fetchResultIfExists(DemandSignature poSignature)
		throws RemoteException, DMSException;

		public DemandSignature carryDemand(IDemand poDemand)
		throws RemoteException, DMSException;

		public DemandSignature carryResult(IDemand poResult) 
		throws RemoteException, DMSException;    
		
		public Configuration getConfiguration()
		throws RemoteException;
	}

	/**
	 * This class implements the backend interface. It is activatable. 
	 * This is the class who is used by RMI to assure service-side execution. 
	 * The compilation process (see comiple_jta.bat) generates stubs 
	 * from this class, which are transported to the client. 
	 * Internally these stubs communicates with the service JTABackend object. 
	 * 
	 * Basically this class is an RMI wrapper of the ITransportAgent implementation
	 * to allow the use of Java RMI features such as activation, etc.
	 * 
	 * @author Emil Vassev
	 * @version 1.0.0
	 * @see JTABackendProtocol
	 * 
	 */
	public static class JTABackend 
	extends Activatable 
	implements JTABackendProtocol, Runnable 
	{
		/**
		 * This TA connects directly to the Javaspace, and preferably
		 * runs in the same host of Javaspace, hence the name "local".
		 * However, it connects to the Javaspace via sockets and is 
		 * initialized via configuration, therefore to make it really
		 * "local", the configuration has to be properly set by e.g.
		 * setting the Jini Lookup service URI pointing to "localhost".
		 */
		private ITransportAgent oLocalTA;
		
		/**
		 * This method is inherited from the Runnable interface.
		 * The purpose is to keep the service alive for some time.
		 */
		public void run() 
		{
			while (true) 
			{
				try 
				{
					Thread.sleep(1000);
				} 
				catch (InterruptedException ex) 
				{
				}
			}
		}

		/**
		 * <p>
		 * The constructor calls the constructor of the super class Activatable.
		 * <p>
		 * The constructor spawns a new thread.
		 * <p>
		 * <code>
		 *  super(id, 0); <br>
		 *	new Thread(this).start();
		 * </code>
		 */
		public JTABackend(ActivationID oID, MarshalledObject<Configuration> oMarshalledConfig) 
		throws RemoteException 
		{
			super(oID, 0);
			try 
			{
				this.oLocalTA = new JINITA(oMarshalledConfig.get());
			} 
			catch (Exception oException) 
			{
				oException.printStackTrace(System.err);
				RemoteException oRemoteExp = new RemoteException(oException.getMessage());
				oRemoteExp.setStackTrace(oException.getStackTrace());
				throw oRemoteExp;
			}
			new Thread(this).start();
		}

		/**
		 * This method fetches a pending demand from the demand space.
		 * 
		 * @return The pending demand fetched.
		 */
		public IDemand fetchDemand() 
		throws RemoteException, DMSException 
		{
			return this.oLocalTA.getDemand();
		}

		/**
		 * This method fetches a pending demand, if exists, from the demand space.
		 * 
		 * @return The pending demand fetched.
		 */
		public IDemand fetchDemandIfExists() 
		throws RemoteException, DMSException 
		{
			return this.oLocalTA.getDemandIfExists();
		}

		/**
		 * This method fetches a pending demand from the demand space.
		 * 
		 * @return The pending demand fetched.
		 */
		public IDemand fetchDemand(String pstrDestination)
		throws RemoteException, DMSException 
		{
			return this.oLocalTA.getDemand(pstrDestination);
		}

		/**
		 * This method fetches a result from the demand space. 
		 * In addition, the method sends the demand back to the client.
		 * 
		 * @param poSignature The demand signature of the result to be fetched from the demand space.
		 * @return The result corresponding to the signature.
		 */
		public IDemand fetchResult(DemandSignature poSignature)
		throws RemoteException, DMSException 
		{
			return this.oLocalTA.getResult(poSignature);
		}

		/**
		 * This method fetches a result, if exists, from the demand space. 
		 * In addition, the method sends the demand back to the client.
		 * 
		 * @param poSignature The signature of the result to be fetched from the demand space.
		 * @return The result corresponding to the signature.
		 */
		public IDemand fetchResultIfExists(DemandSignature poSignature)
		throws RemoteException, DMSException 	
		{
			return this.oLocalTA.getResultIfExists(poSignature);
		}

		/**
		 * This method write a demand into the demand space and 
		 * sends the demand signature back to the client.
		 * @param poDemand - The demand to write into the Javaspace
		 */
		public DemandSignature carryDemand(IDemand poDemand)
		throws RemoteException, DMSException 
		{
			return this.oLocalTA.setDemand(poDemand);
		}

		/**
		 * This method write a result into the demand space and 
		 * sends the demand signature id back to the client.
		 * @param poResult - The result to write into the Javaspace
		 */
		public DemandSignature carryResult(IDemand poResult)
		throws RemoteException, DMSException 
		{
			return this.oLocalTA.setResult(poResult);
		}

		@Override
		public Configuration getConfiguration() 
		throws RemoteException 
		{
			return this.oLocalTA.getConfiguration();
		}
	}

	/**
	 * This is the proxy that uses the backend protocol. Since this proxy implements IJINITransportAgent, clients can use it transparently.
	 * 
	 * @author Emil Vassev
	 * @version 1.1.0
	 * @see IJINITransportAgent
	 */
	// XXX: review
	public static class JINITransportAgentProxy 
    implements Serializable, ITransportAgent
	{
		private static final String MSG_PREFIX = "JINITransportAgentProxy message: ";
		private static final String ERR_PREFIX = "JINITransportAgentProxy error: ";

		private JTABackendProtocol oTABackend;

		/**
		 * This method prints out a message by adding a special prefix to it.
		 * XXX: move to PrintUtils
		 */
		private static void printOut(String sMsg)
		{
			System.out.println(MSG_PREFIX + sMsg);
		}

		/**
		 * This method prints out an error message by adding a special prefix to it.
		 * XXX: move to PrintUtils
		 */
		private static void printOutError(String sError)
		{
			System.err.println(ERR_PREFIX + sError);
		}

		/**
		 * This constructor (by default) does not do any special initializations.
		 * It is for further use. 
		 */
        public JINITransportAgentProxy() 
		{
        }

		/**
		 * This constructor preserves the backend object passed as an argument.
		 */
		public JINITransportAgentProxy(JTABackendProtocol poBackend) 
		{
            this.oTABackend = poBackend;
        }


		@Override
		public IDemand getDemand() 
		throws DMSException
		{
			try 
			{
				printOut("JTA getDemand is runnning ..."); 
				return this.oTABackend.fetchDemand();
			}
			catch(DMSException oException)
			{
				throw oException;
			}
			catch(Exception oException) 
			{
				printOutError(oException.getMessage());
				throw new DMSException(oException);
			}
		}

		@Override
		public IDemand getDemandIfExists()
		throws DMSException
		{
			try 
			{
				return this.oTABackend.fetchDemandIfExists();
			} 
			catch(DMSException oException)
			{
				throw oException;
			}
			catch(Exception oException) 
			{
				printOutError(oException.getMessage());
				throw new DMSException(oException);
			}
		}
		
		@Override
		public IDemand getResult(DemandSignature poSignature) 
		throws DMSException
		{
			try 
			{
				return this.oTABackend.fetchResult(poSignature);
			} 
			catch(DMSException oException)
			{
				throw oException;
			}
			catch(Exception oException) 
			{
				printOutError(oException.getMessage());
				throw new DMSException(oException);
			}
		}

		@Override
		public IDemand getResultIfExists(DemandSignature poSignature) 
		throws DMSException
		{
			try 
			{
				return this.oTABackend.fetchResultIfExists(poSignature);
			} 
			catch(DMSException oException)
			{
				throw oException;
			}
			catch(Exception oException) 
			{
				printOutError(oException.getMessage());
				throw new DMSException(oException);
			}
		}
		
		@Override
		public DemandSignature setDemand(IDemand poDemand) 
		throws DMSException
		{
			try 
			{
				return this.oTABackend.carryDemand(poDemand);
			} 
			catch(DMSException oException)
			{
				throw oException;
			}
			catch(Exception oException) 
			{
				printOutError(oException.getMessage());
				throw new DMSException(oException);
			}
		}


		@Override
		public DemandSignature setResult(IDemand poResult) 
		throws DMSException
		{
			try 
			{
				return this.oTABackend.carryResult(poResult);
			} 
			catch(DMSException oException)
			{
				throw oException;
			}
			catch(Exception oException) 
			{
				printOutError(oException.getMessage());
				throw new DMSException(oException);
			}
		}

		@Override
		public IDemand getDemand(String pstrDestination) 
		throws DMSException 
		{
			try 
			{
				printOut("JTA getDemand is runnning ..."); 
				return this.oTABackend.fetchDemand(pstrDestination);
			}
			catch(DMSException oException)
			{
				throw oException;
			}
			catch(Exception oException) 
			{
				printOutError(oException.getMessage());
				throw new DMSException(oException);
			}
		}

		@Override
		@Deprecated
		public void setClientIPAddress(String pstrIPAddress) 
		{
			
		}

		@Override
		public Configuration getConfiguration() 
		{
			try 
			{
				return this.oTABackend.getConfiguration();
			} 
			catch (RemoteException oException) 
			{
				oException.printStackTrace(System.err);
				return null;
			}
		}

		@Override
		public void setConfiguration(Configuration poTAConfig)
		throws DMSException 
		{
			//XXX TODO
		}
    }

	/**
	 * Inner class to listen for discovery events. 
	 * 
	 * @author Emil Vassev
	 * @version 1.1.0
	 * 
	 */
    class Listener implements DiscoveryListener 
	{
		/**
		 * This method is called when found a new lookup service. This method register the GIPSY JINI Service with the discovered lookup service.
		 */
		public void discovered(DiscoveryEvent ev) 
		{
            printOut("A GIPSY Lookup service has been discovered!");
            ServiceRegistrar[] newregs = ev.getRegistrars();
            for (int i=0 ; i<newregs.length ; i++) 
			{
                if (!registrations.containsKey(newregs[i])) 
				{
//**** This method register the GIPSY JINI Service with the discovered lookup service.
                    registerWithLookup(newregs[i]);
                }
            }
        }
        
		/**
		 * This method is called ONLY when we explicitly discard lookup service, not 
		 * "automatically" when a lookup service goes down. Once discovered, there is NO ongoing communication with a lookup service.
		 */
        public void discarded(DiscoveryEvent ev) 
		{
 	        System.out.println("Listener.discarded()");
            ServiceRegistrar[] deadregs = ev.getRegistrars();
            for (int i=0 ; i<deadregs.length ; i++) 
			{
                registrations.remove(deadregs[i]);
            }
        }
    }


	/*
	 * ---------------
	 * Object Lifetime
	 * ---------------
	 */

	/**
	 * Constructs a JINI transport agent.
	 * <br>
	 * The initial tasks performed by the constructor are:
     * <ul>
	 * <li>Sets a security manager.
	 * <li>Connects with the Demand Dispatcher.
	 * <li>Searches for the GIPSY LUS. 
	 * <li>Runs a discovery protocol. 
	 * </ul>
	 */
	public JINITransportAgent() 
	throws IOException, ClassNotFoundException 
	{
//	  try 
//	  {
		sstrIPLocalAddress = NetUtils.getLocalIPAddress();
		printOut("JTA IP: " + sstrIPLocalAddress); 
		sstrsCodebase = "http://" + sstrIPLocalAddress + ":8085/"; 
		printOut("JTA CODEBASE: " + sstrsCodebase); 
			   
	  
        
//**** Set a security manager
		if (System.getSecurityManager() == null) 
		{
			// Get the current class file directory.
			System.setProperty(
					Configuration.JAVA_SECURITY_POLICY_KEY, 
					GIPSY.getConfugration().getProperty(Configuration.CONFIGURATION_ROOT_PATH_KEY) + SECURITY_FILE);
			System.setSecurityManager(new RMISecurityManager());
		}
		
		item = new ServiceItem(null, createProxy(), null);
        
//**** connect with the Demand Dispatcher		   
		connectDemandDispatcher();

//**** Search for the GIPSY LUS 
 	    printOut("Multicast Discovery on GIPSY Lookup Serice(s)...");
//	    disco = new LookupDiscovery(DISCOVERY_GROUP_NAMES);
 	    disco = new LookupDiscovery(LookupDiscovery.ALL_GROUPS);
//**** Run discovery protocol 
 	    printOut("Runs Discovery Listener ....");
	    disco.addDiscoveryListener(new Listener());
/*	   
	  }
	  catch (Exception e)
	  {
		 printOutError(e.getMessage());
	  }
*/
	}

	/**
	 * This method connects the JINITA with the Demand Dispatcher.
	 */
	static public void connectDemandDispatcher()
	{
		try 
		{ 
			//soJAO = new JavaSpaceAccessObject("","");
		}
		catch (Exception e) 
		{
			printOutError(e.getMessage());

		}
	}

	/**
	 * This method prints out a message with a special prefix added to it.
	 */
	static public void printOut(String sMsg)
	{
		System.out.println("JINITransportAgent message: " + sMsg);
	}

	/**
	 * This method prints out an error message with a special prefix added to it.
	 */
	static public void printOutError(String sError)
    {
        System.err.println("JINITransportAgent error: " + sError);
    }

	/**
	 * This method creates a JINITA proxy.
	 * <br>
	 * The initial tasks performed by the method are:
     * <ul>
	 * <li>Creates a descriptor for a new activation group to run the backend object in.
	 * <li>Registers the group and gets the ID. 
	 * <li>Creates the group.  
	 * <li>Creates an activation descriptor for the JINITA proxy. 
	 * <li>Creates the 'backend' object that will implement the protocol. 
	 * </ul>
	 */
	public ITransportAgent createProxy() 
	{
        try 
		{
			
			printOut("");
//**** Create a descriptor for a new activation group to run our backend object in.
	        Properties props = new Properties();
			props.put(Configuration.JAVA_SECURITY_POLICY_KEY, SECURITY_FILE);
            ActivationGroupDesc group = new ActivationGroupDesc(props, null);
			printOut("1.Has been creating a descriptor for a new activation group");
	
//**** Register the group and get the ID. 
            ActivationGroupID gid = ActivationGroup.getSystem().registerGroup(group);
            printOut("2.Has been registering the group and get the ID");
           
//**** Create the group 
            ActivationGroup.createGroup(gid, group, 0);
            printOut("3.Has been creating the group");
			
//**** Create an activation descriptor for our object
            String location = sstrsCodebase;
            MarshalledObject data = null;
            ActivationDesc desc = new ActivationDesc(this.getClass().getPackage().getName() +
                       ".JINITransportAgent$JTABackend", location, data);
            printOut("4.Has been creating an activation descriptor");
            
//**** Create the 'backend' object that will implement the protocol. 
            JTABackendProtocol backend = (JTABackendProtocol) Activatable.register(desc);
            printOut("5.Has been creating the backend object");
			printOut("");
			
            return new JINITransportAgentProxy(backend);
        } 
		catch (RemoteException e) 
		{
            printOutError("Error creating backend object: " + e.getMessage());
            return null;
        } 
		catch (ActivationException e) 
		{
            printOutError("Problem with activation: " + e.getMessage());
            e.printStackTrace(System.err);
            return null;
        }
    }

	/**
	 * This method register the JINITransportAgent - a JINI Service, with the discovered Lookup
	 * service. This work involves remote calls, and may take a while to complete.
	 * Thus, since it's called from discovered() (see class Listener), it will 
	 * prevent us from responding in a timely fashion to new discovery events.
	 * An improvement would be to spin off a separate short-lived thread.
	 */
    protected synchronized void registerWithLookup(ServiceRegistrar registrar) 
	{
        ServiceRegistration registration = null;

        try 
		{
            registration = registrar.register(item, LEASE_TIME);
        }
        catch (RemoteException e) 
		{
            printOutError("GIPSY Service cannot register with the Lookup: " + e.getMessage());
            return;
        }
        
		/*
		 * Every time the service gets a new ID. To improve the code, we should 
		 * save this ID so that it can be used after restarts of the service.
		 */
        if (item.serviceID == null) 
		{
            item.serviceID = registration.getServiceID();
            printOut("Set GIPSY Service ID to " + item.serviceID);
        }
        
        registrations.put(registrar, registration);
		
		startAfterRegistration();
    }
    
	/**
	 * Method startAfterRegistration()
	 * This method runs after the registration with the Lookup.
	 */
	public void startAfterRegistration()
	{
		printOut("JINITransportAgent has been started successfully.");
		printOut("****************************************************");
	}
	
	/**
	 * The purpose of the thread (the run() method) is to keep the
	 * service alive for some time.
	 */
    public void run() 
	{
        while(true) 
		{
            try 
			{
                Thread.sleep(1000);
            } 
			catch(InterruptedException ex) 
			{
            }
        }
    }

	/**
	 *  Creates a new JINITransportAgent and start its thread.
	 */
	public static void main(String args[]) 
	{
        try 
		{
			printOut("JTA is running ..."); 
            JINITransportAgent oJTA = new JINITransportAgent();
            new Thread(oJTA).start();
        } 
		catch(Exception e) 
		{
            printOutError("Couldn't create JTA service: " + e.getMessage());
        }
    }
}

// EOF
