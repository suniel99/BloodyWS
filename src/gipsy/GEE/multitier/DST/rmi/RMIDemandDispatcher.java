package gipsy.GEE.multitier.DST.rmi;

import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;

//import org.apache.log4j.Logger;



import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcher;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcherException;
import gipsy.GEE.IDP.DemandGenerator.jms.JMSTransportAgent;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.logger.Logger;


/** RMI DemandDispatcher that extends DemandDispatcher and implements the functions of 
 * IRMIDemandDispatcher
 * 
 * @author 
 * @version RMIDemandDispatcher
 */
public class RMIDemandDispatcher extends DemandDispatcher implements IRMIDemandDispatcher
{
	private Logger log = new Logger();
	
   /**
   * Constructor for Class.forName().newInstance();
   * @throws IOException 
   * @throws ClassNotFoundException , DemandDispatcherException and IOException
   */
	public RMIDemandDispatcher() 
	throws DemandDispatcherException, ClassNotFoundException, IOException
	{
		this.oTA = (ITransportAgent) new RMITransportAgent();
		log.debug("Instantiating Transport Agent for oTA ");
	}

	/**
	* Does nothing, just for compatibility with Jini.
	* @param pstr1
	* @param pstr2
	*/
	public RMIDemandDispatcher(String pstrOne, String pstrTwo)
    throws DemandDispatcherException, ClassNotFoundException, IOException
    {
	    	this.oTA = (ITransportAgent) new RMITransportAgent();
	    	log.debug("Instantiating Transport Agent for oTA ");
	
    }
	    
	    
	/**
	* Constructor of this class that copies the object from ITransportAgent 
	* to this class
	* @param poTA
	*/
	public RMIDemandDispatcher(ITransportAgent poTA)
	{
	    	this.oTA = poTA;
	    	log.debug("Instantiating Transport Agent for oTA ");
	}
	    
	/**
	* ---------------------
	* IDemandDispatcher API
	* ---------------------
	*/
	    
	/**
	*  Cancels the Demand
	* @param poSignature 
	* @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#cancelDemand(gipsy.GEE.IDP.demands.DemandSignature)
    */
	@Override
	public void cancelDemand(DemandSignature poSignature)
	throws DemandDispatcherException
	{
	}


	/**
	* Reads the Demand
	* 
	*@return IDemand Object
	*@throws DemandDispatcherException
	*/
	
	@Override
	public IDemand readDemand()
    throws DemandDispatcherException
    {
			while(true)
			{
				try 
				{   log.info("Object of "+oTA.getClass().toString()+" reads demand");
					return (IDemand) this.oTA.getDemand(DemandSignature.DGT);
				} 
				catch (DMSException oException) 
				{  
				   log.debug("Problem in reading demand");
				   //send.sendPacket("DMS Exception occured in read Demand function of RMIDemand Dispatcher class");
				   if(this.oTAExceptionHandler != null)
					{   
					    log.info(" Handling the exception");
						try 
						{
							this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
							log.info(" handling the exception through fixTA ");
						} 
						catch (InterruptedException eInterrupted) 
						{   
							// send.sendPacket("Interrupt exception");
							eInterrupted.printStackTrace(System.err);
							log.info("Problem in handling the exception");
							log.error(eInterrupted.getClass().toString() + " Interrupted Exception");
						}
					}
					else
					{
						oException.printStackTrace(System.err);
						log.error(oException.getClass().toString());
						throw new DemandDispatcherException(oException);
					}
				}
			}
		}

	

	/**
	* Reads the Demand if exists
	* 
	*@return IDemand Object
	*@throws DemandDispatcherException
	*
	*/
	@Override
	public IDemand readDemandIfExists()
	throws DemandDispatcherException
	{
			while(true)
			{
				try 
				{
					 log.info("reads demand, if it exists");
					return (IDemand) this.oTA.getDemandIfExists();
				} 
				catch (DMSException oException) 
				{
					log.debug("Problem in reading demand");
					//send.sendPacket("DMS Exception occured in readDemandIfExists function of RMIDemand Dispatcher class");
					
					if(this.oTAExceptionHandler != null)
					{
						log.info(" Handling the exception");
						try 
						{
							this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
							log.info(" handling the exception through fixTA ");
						} 
						catch (InterruptedException eInterrupted) 
						{
							//send.sendPacket("Interrupt exception");
						
							eInterrupted.printStackTrace(System.err);
							log.info("Problem in handling the exception");
							log.error(eInterrupted.getClass().toString() + " Interrupted Exception");
						
						}
					}
					else
					{
						oException.printStackTrace(System.err);
						log.error(oException.getClass().toString());
						throw new DemandDispatcherException(oException);
					}
				}
			}
	 }

		

	/**
	* Reads the Demand Result
	* 
	* @param poSignature 
	* @return IDemand object
	* @throws DemandDispatcherException
	*/
	@Override
	public IDemand readResult(DemandSignature poSignature)
	throws DemandDispatcherException
		{
			while(true)
			{
				try 
				{
					log.info("reads result");
					return this.oTA.getResult(poSignature);
				} 
				catch (DMSException oException) 
				{ 
					log.debug("Problem in reading result");
					// send.sendPacket("DMS Exception occured in readResult function of RMIDemand Dispatcher class");
				    if(this.oTAExceptionHandler != null)
					{
						try 
						{
							this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
							log.info(" handling the exception through fixTA ");
						} 
						catch (InterruptedException eInterrupted) 
						{
						   
							//send.sendPacket("Interrupt exception");
							eInterrupted.printStackTrace(System.err);
							log.info("Problem in handling the exception");
							log.error(eInterrupted.getClass().toString() + " Interrupted Exception");
						
						}
					}
					else
					{
						oException.printStackTrace(System.err);
						log.error(oException.getClass().toString());
						throw new DemandDispatcherException(oException);
					}
				}
			}
		}


	/**
	* Reads the Demand Results if it exists
	* 
	* @param poSignature 
	* @return IDemand object
	* @throws DemandDispatcherException
	*/
	@Override
	public IDemand readResultIfExists(DemandSignature poSignature)
	throws DemandDispatcherException
	{
		 while(true)
			{
				try 
				{
					log.info("reads result if it exists");
					return this.oTA.getResultIfExists(poSignature);
				} 
				catch (DMSException oException) 
				{  
					log.debug("Problem in reading result");
					// send.sendPacket("DMS Exception occured in readResultExists Demand function of RMIDemand Dispatcher class");
					if(this.oTAExceptionHandler != null)
					{
						try 
						{
							this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
							log.info(" handling the exception through fixTA ");

						} 
						catch (InterruptedException eInterrupted) 
						{	
							//send.sendPacket("Interrupt exception");
							eInterrupted.printStackTrace(System.err);
							log.info("Problem in handling the exception");
							log.error(eInterrupted.getClass().toString() + " Interrupted Exception");
						
						}
					}
					else
					{
						oException.printStackTrace(System.err);
						log.error(oException.getClass().toString());
						throw new DemandDispatcherException(oException);
					}
				}
			}
		}


	/**
	* write the Demand
	* 
	* @param poDemand 
	* @return IDemand object
	* @throws DemandDispatcherException
	*/
		
	@Override
	public DemandSignature writeDemand(IDemand poDemand)
	throws DemandDispatcherException
	{
			// Check entry condition
			if(poDemand == null)
			{   log.info("throwing null pointer exception");
				throw new NullPointerException();
			}
			
			// Check if the demand has already been computed
			DemandSignature oSignature = poDemand.getSignature();
			IDemand oResult = null;
			
			while(oSignature != null)
			{
				

				try 
				{   
					oResult = this.oTA.getResultIfExists(oSignature);
					log.info("reads result if it exists");
					break;
				} 
				catch (DMSException oException) 
				{  
					log.debug("Problem in reading result");
					// send.sendPacket("DMS Exception occured in writeDemand function of RMIDemand Dispatcher class");
					if(this.oTAExceptionHandler != null)
					{
						try 
						{
							this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
							log.info(" handling the exception through fixTA ");

						} 
						catch (InterruptedException eInterrupted) 
						{ //   send.sendPacket("Interrupt exception");
							eInterrupted.printStackTrace(System.err);
							log.info("Problem in handling the exception");
							log.error(eInterrupted.getClass().toString() + " Interrupted Exception");
						
						}
					}
					else
					{
						oException.printStackTrace(System.err);
						log.error(oException.getClass().toString());
						throw new DemandDispatcherException(oException);
					}
				}
			}
			
			// If the demand has been computed then no need to send it
			if(oResult != null)
			{
				return oSignature;
			}
			
			// Send the demand
			while(true)
			{
				try 
				{
					log.info("writing demand");
					oSignature = this.oTA.setDemand(poDemand);
					return oSignature;
				} 
				catch (DMSException oException) 
				{
					log.debug("Problem in writing demand");
					//  send.sendPacket("DMS Exception occured in writeDemand function of RMIDemand Dispatcher class");
					if(this.oTAExceptionHandler != null)
					{
						try 
						{
							this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
							log.info(" handling the exception through fixTA ");

						} 
						catch (InterruptedException eInterrupted) 
						{ //  send.sendPacket("Interrupt exception");		
							eInterrupted.printStackTrace(System.err);
							log.info("Problem in handling the exception");
							log.error(eInterrupted.getClass().toString() + " Interrupted Exception");
						
						}
					}
					else
					{
						oException.printStackTrace(System.err);
						log.error(oException.getClass().toString());
						throw new DemandDispatcherException(oException);
					}
				}
			}
	}


	/**
	* write the results
	*  
	* @param poSignature,poResult
	* @return DemandSignature object
	* @throws DemandDispatcherException
	*/
	@Override
	public DemandSignature writeResult(DemandSignature poSignature, IDemand poResult)
	throws DemandDispatcherException
	{
			while(true)
			{
				try 
				{
					log.info("writing result");
					return this.oTA.setResult(poResult);
				} 
				catch (DMSException oException) 
				{   //send.sendPacket("DMS Exception occured in writeResult function of RMIDemand Dispatcher class");
				
					log.debug("Problem in writing demand");
					if(this.oTAExceptionHandler != null)
					{
						try 
						{
							this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
							log.info(" handling the exception through fixTA ");

						} 
						catch (InterruptedException eInterrupted) 
						{ //   send.sendPacket("Interrupt exception");		
							eInterrupted.printStackTrace(System.err);
							log.info("Problem in handling the exception");
							log.error(eInterrupted.getClass().toString() + " Interrupted Exception");
						
						}
					}
					else
					{
						oException.printStackTrace(System.err);
						log.error(oException.getClass().toString());
						throw new DemandDispatcherException(oException);
					}
				}
			}
	}


	/**
	* To get the value
	*  
	* @param  poDemand
	* @return IDemand object
	* @throws DemandDispatcherException
	*/
	@Override
	public IDemand getValue(IDemand poDemand) 
	throws DemandDispatcherException 
	{
			// Check entry condition
			if(poDemand == null)
			{	log.info("throwing null pointer exception");
				throw new NullPointerException();
			}
			
			// Check if the demand has already been computed
			DemandSignature oSignature = poDemand.getSignature();
			IDemand oResult = null;
			
			while(oSignature != null)
			{
				try 
				{
					log.info("reads result if it exists");
					oResult = this.oTA.getResultIfExists(oSignature);
					break;
				} 
				catch (DMSException oException) 
				{
					log.debug("Problem in reading result");
					// send.sendPacket("DMS Exception occured in getvalue function of RMIDemand Dispatcher class");
					if(this.oTAExceptionHandler != null)
					{
						try 
						{
							this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
							log.info(" handling the exception through fixTA ");
						} 
						catch (InterruptedException eInterrupted) 
						{   //  send.sendPacket("Interrupt exception");
						
							eInterrupted.printStackTrace(System.err);
							log.info("Problem in handling the exception");
							log.error(eInterrupted.getClass().toString() + " Interrupted Exception");
						
						}
					}
					else
					{
						oException.printStackTrace(System.err);
						log.error(oException.getClass().toString());
						throw new DemandDispatcherException(oException);
					}
				}
			 }
		 	
			 // If the demand has been computed then no need to send it
		   	 if(oResult != null)
			 {
				return oResult;
			 }
			
			// Send the demand and wait for the result
			while(true)
			{
				try 
				{
					oSignature = this.oTA.setDemand(poDemand);
					oResult = this.oTA.getResult(oSignature);
					log.info("demand sent and getting the result");

					return oResult;
				} 
				catch (DMSException oException) 
				{
					log.debug("Problem in getting value");
					//send.sendPacket("DMS Exception occured in getvalue function of RMIDemand Dispatcher class");
					if(this.oTAExceptionHandler != null)
					{   
						try 
						{
							this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
							log.info(" handling the exception through fixTA ");

						} 
						catch (InterruptedException eInterrupted) 
						{   
							// send.sendPacket("Interrupt exception");
							eInterrupted.printStackTrace(System.err);
							log.info("Problem in handling the exception");
							log.error(eInterrupted.getClass().toString() + " Interrupted Exception");
						
							
						}
					}
					else
					{
						oException.printStackTrace(System.err);
						log.error(oException.getClass().toString());
						throw new DemandDispatcherException(oException);
					}
				}
			}
	  }
}

