package gipsy.GEE.multitier.DST.rmi;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import gipsy.Configuration;
import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.logger.Logger;


/**
 * This class implements the IRMITransportAgent
 * Which has method to lookup the registry, fetch demand, get demand 
 * and get result
 * 
 *  @author 
 *
 */

public class RMITransportAgent implements IRMITransportAgent, Runnable 
{
	/**
	 * creating logger object
	 */
	private Logger log = new Logger();

    /**
	* Setup the lease time.
	*/
	
	protected static final int LEASE_TIME = 10 * 60 * 1000; 
	/**
	* object of ITransportAgent
	*/
	
	private ITransportAgent oLocalTA;
	/**
	* object of IRMIDSTWrapper
	*/
	public  IRMIDSTWrapper lookUp; 
		
	/**
	* to lookup registry
	*/
	void rmiLookUp()
	{
			
	 try {
			lookUp = (IRMIDSTWrapper)Naming.lookup("lookUp");
			log.info("Lookup successful");			
		 } 
     catch (MalformedURLException e) 
	    {
			e.printStackTrace();
			log.error(e.getClass().toString() + " MalformedURL Exception");
		} 
	 catch (RemoteException e) 
	    {
			e.printStackTrace();
			log.error(e.getClass().toString() + " Remote Exception");

		} 
	 catch (NotBoundException e) 
	    {
			e.printStackTrace();
			log.error(e.getClass().toString() + " NotBound Exception");

		}
			
			
	}

		
	/**
	 *  Creates a new RMITransportAgent and start its thread.
	 *  
	 */
	public static void main(String args[]) 
	{  
	 final Logger log=new Logger();
	   try 
		{
		   RMITransportAgent oRMITA = new RMITransportAgent();
	          new Thread(oRMITA).start();
	          log.info("RMI registry lookup");
	          oRMITA.rmiLookUp();
	          
	            
	    } 
			
	   catch(Exception e) 
			{   
	           System.out.println(e.getMessage());
	           log.error(e.getClass().toString());
	        }
	    }
		
		
		
	/**
	 * This method fetches a pending demand from the demand space.
	 * 
	 * @return The pending demand fetched.
	 */
	public IDemand fetchDemand() 
	{
		try 
		{
			log.info("fetching demand");
			return this.lookUp.getPendingDemand();

		}
		catch (Exception e) {
			e.printStackTrace();
			log.error(e.getClass().toString()+ " exception occurs");
	
		}
		return null;
	}

	/**
	 * This method fetches a pending demand, if exists, from the demand space.
	 * 
	 * @return The pending demand fetched.
	 */
	public IDemand fetchDemandIfExists() 
	throws RemoteException, DMSException 
	{
		try
		{
			log.info("fetching demand if it exists");
			return this.lookUp.getPendingDemand();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			log.error(e.getClass().toString()+ " exception occurs");

		}
		return null;
	}

	/**
	 * This method fetches a pending demand from the demand space.
	 * 
	 * @return The pending demand fetched.
	 */
	public IDemand fetchDemand(String pstrDestination)
	throws RemoteException, DMSException 
	{
		Object dSign = (Object) pstrDestination;
		DemandSignature dObj = (DemandSignature)dSign;
		try 
		{
			log.info("fetching demand from the given destination");
			return this.lookUp.getHashTableValue(dObj);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error(e.getClass().toString()+ " exception occurs");

		}
		return null;
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
		try 
		{
			log.info("fetching result");
			return this.lookUp.getHashTableValue(poSignature);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			log.error(e.getClass().toString()+ " exception occurs");

		}
		return null;
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
		try 
		{
			log.info("fetching result if exists");
			return this.lookUp.getHashTableValue(poSignature);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			log.error(e.getClass().toString()+ " exception occurs");
		}
		return null;
	}

	/**
	 * This method write a demand into the demand space and 
	 * sends the demand signature back to the client.
	 * @param poDemand - The demand to write into the 
	 */
	public DemandSignature carryDemand(IDemand poDemand)
	throws RemoteException, DMSException 
	{
		try
		{
			log.info("carrying demand");
			return this.lookUp.setHashTable(poDemand);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			log.error(e.getClass().toString()+ " exception occurs");

		}
		return null;
	}

	/**
	 * This method write a result into the demand space and 
	 * sends the demand signature id back to the client.
	 * @param poResult - The result to write into the 
	 */
	public DemandSignature carryResult(IDemand poResult)
	throws RemoteException, DMSException 
	{
		try 
		{
			log.info("carrying result");
			return this.lookUp.setHashTable(poResult);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			log.error(e.getClass().toString()+ " exception occurs");
		}
		return null;
	}
	
    /**
     * @return configuration details
     */
	@Override
	public Configuration getConfiguration() 

	{
		return this.oLocalTA.getConfiguration();
	}
	
	
	/**
	 * This is the proxy that uses the backend protocol. Since this proxy implements IRMITransportAgent, clients can use it transparently.
	 * 
	 * @version 1.1.0
	 * @see IRMITransportAgent
	 */
	
	public static class RMITransportAgentProxy extends RMITransportAgent
    implements Serializable, ITransportAgent
	{
		// A constructor, throws IOException, ClassNotFoundException
		public RMITransportAgentProxy() throws IOException,
				ClassNotFoundException
		{
		
			this(null);
			
		}
		
	private IRMITransportAgent oIRMITA;
		
	/**
	* This constructor preserves the backend object passed as an argument.
	*/
	
	public RMITransportAgentProxy(IRMITransportAgent poRMITA) throws ClassNotFoundException, IOException
	{
            this.oIRMITA = poRMITA;
    
	}
	
	/**
    * 
	*/
	private Logger log = new Logger();
	private static final long serialVersionUID = 1L;
	private static final String MSG_PREFIX = "RMITransportAgentProxy message: ";
	private static final String ERR_PREFIX = "RMITransportAgentProxy error: ";

		
	/**
	* This method prints out a message by adding a special prefix to it.
	* XXX: move to PrintUtils
	*/
	public static void printOut(String psMsg)
	{
		System.out.println(MSG_PREFIX + psMsg);
		
	}

	
	/**
	 * This method prints out an error message by adding a special prefix to it.
	 * XXX: move to PrintUtils
	 */
	public static void printOutError(String psError)
	{
		System.err.println(ERR_PREFIX + psError);
	}

	

	/**
	 * This method fetch demand 
	 * @return IDemand object
	 * @throws DMSException
	 */

	@Override
	public IDemand getDemand() 
	  throws DMSException
	{
		try 
			{
				printOut("RMITA getDemand is runnning ..."); 
				log.info("getting demand");
				return this.fetchDemand();
			}
		catch(Exception oException) 
			{ 
				printOutError(oException.getMessage());
				log.error(oException.getClass().toString()+ " exception occurs");
				throw new DMSException(oException);
			}
	}
	
	/**
	 * This method fetch demand if it exists
	 * @return IDemand object
	 * @throws DMSException
	 */
	@Override
	public IDemand getDemandIfExists()
		throws DMSException
		{
			try 
			{
				log.info("getting demand if exists");
				return this.fetchDemandIfExists();
			} 
			catch(DMSException oException)
			{  
				log.error(oException.getClass().toString()+ " DMSexception occurs");
				throw oException;
			}
			catch(Exception oException) 
			{	
				printOutError(oException.getMessage());
				log.error(oException.getClass().toString()+ " exception occurs");
				throw new DMSException(oException);
			}
		}

	/**
	 * This method get the result
	 * @return IDemand object
	 * @throws DMSException
	 */
		
	@Override
	public IDemand getResult(DemandSignature poSignature) 
		throws DMSException
		{
			try 
			{
				log.info("getting result");
				return this.fetchResult(poSignature);
			} 
			catch(DMSException oException)
			{  				
				log.error(oException.getClass().toString()+ " DMS exception occurs");
				throw oException;
			}
			catch(Exception oException) 
			{	
			
				printOutError(oException.getMessage());
				log.error(oException.getClass().toString()+ " exception occurs");
				throw new DMSException(oException);
			}
		}
	
	/**
	 * This method get the result if it exists
	 * @return IDemand object
	 * @throws DMSException
	 */

	@Override
	public IDemand getResultIfExists(DemandSignature poSignature) 
		throws DMSException
		{
			try 
			{
				log.info("getting result if exists");
				return this.fetchResultIfExists(poSignature);
			} 
			catch(DMSException oException)
			{	 
				log.error(oException.getClass().toString()+ " DMS exception occurs");
				throw oException;
			}
			catch(Exception oException) 
			{	
			
				printOutError(oException.getMessage());
				log.error(oException.getClass().toString()+ " exception occurs");
				throw new DMSException(oException);
			}
		}
		
	/**
	 * This method set the demand
	 * @return DemandSignature object
	 * @throws DMSException
	 */
	@Override
	public DemandSignature setDemand(IDemand poDemand) 
	throws DMSException
		{
			try 
			{
				log.info("carrying demand");
				return this.carryDemand(poDemand);
			} 
			catch(DMSException oException)
			{  
				log.error(oException.getClass().toString()+ " DMS exception occurs");
				throw oException;
			}
			catch(Exception oException) 
			{	
			
				printOutError(oException.getMessage());
				log.error(oException.getClass().toString()+ " exception occurs");
				throw new DMSException(oException);
			}
		}

	/**
	 * This method set the result
	 * @param poResult- an object of IDemand
	 * @return DemandSignature object
	 * @throws DMSException
	 */
	@Override
	public DemandSignature setResult(IDemand poResult) 
	throws DMSException
		{
			try 
			{
				log.info("set result");
				return this.carryResult(poResult);
			} 
			catch(DMSException oException)
			{	
				log.error(oException.getClass().toString()+ " DMS exception occurs");
				throw oException;
			}
			catch(Exception oException) 
			{	
     			printOutError(oException.getMessage());
				log.error(oException.getClass().toString()+ " exception occurs");
				throw new DMSException(oException);
			}

		}
	
	/**
	 * This method gets the demand
	 * @param pstrDestination
	 * @return IDemand object
	 * @throws DMSException
	 */
	@Override
	public IDemand getDemand(String pstrDestination) 
	throws DMSException 
		{
			try 
			{
				printOut("RMITA getDemand is runnning ..."); 
				log.info("fetching demand");
				return this.fetchDemand(pstrDestination);
			}
			catch(DMSException oException)
			{  
			
				log.error(oException.getClass().toString()+ " DMS exception occurs");
				throw oException;
			}
			catch(Exception oException) 
			{  
			
				printOutError(oException.getMessage());
				log.error(oException.getClass().toString()+ " exception occurs");
				throw new DMSException(oException);
			}
		}
	
	/**
	* This method gets the ip Address and set its up
    * @param pstrIPAddress
	* @return void
	*/
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
				log.info("getting the configuration");
				return this.getConfiguration();
			} 
			catch (Exception e) 
			{  
			
				e.printStackTrace(System.err);
				log.error(e.getClass().toString()+ " exception occurs");
				return null;
			}
		}

		
	@Override
	public void setConfiguration(Configuration poTAConfig)
	throws DMSException 
		{
		}
	
	/**
	* This method gets the RMI transport Agent
  	* @return ITransportAgent
  	* @throws NotBoundException
	*/
	
	
	public ITransportAgent getRMITA() throws Exception, NotBoundException 
	{
        try 
		{
			
        	IRMITransportAgent backend = (IRMITransportAgent)Naming.lookup("oTA");
			log.info(" lookup successful");
            return new RMITransportAgentProxy(backend);
        } 
		catch (RemoteException e) 
		{
			log.error(e.getClass().toString()+ " remote exception occurs");
            return null;
        }
    }

	}
	
	public RMITransportAgent() 
	throws IOException, ClassNotFoundException 
	{
		
		
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
                log.info("Make thread sleep");
            } 
			catch(InterruptedException ex) 
			{
			
				log.error(ex.getClass().toString()+" Interrupted exception");
            }
        }
    }

	
	
	
	
	
	
	
	
}	
	

