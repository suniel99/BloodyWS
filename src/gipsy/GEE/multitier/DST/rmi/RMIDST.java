package gipsy.GEE.multitier.DST.rmi;

import gipsy.GEE.GEEException;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IVW.Warehouse.ValueHouse;
import gipsy.GEE.logger.Logger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class implements the remote interface of RMI.
 * Which has method to store the values in the Hashtable.
 * We are taking unique string as key and the object of IDemand class as value. 
 * 
 *  @author p_pandy
 *
 */
public class RMIDST extends UnicastRemoteObject implements IRMIDSTWrapper {


	private Logger log = new Logger();
	//A Constructor, throws RemoteException
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RMIDST() throws RemoteException
	{
	 super();
	 
	}
	

	/**
	 * This method stores IDemand object in hash table through ValueHouse.
	 * @param poIdObj -  Object of IDemand
	 * @see setValue
	 * @see ValueHouse
	 * 
	 */
	public DemandSignature setHashTable(IDemand poIdObj)
	{
		if(poIdObj == null)
		{  
			log.info("Throwing null pointer exception");
			throw new NullPointerException();
		}
		ValueHouse valueobj;
		try 
		{
			valueobj = new ValueHouse();
			String str = poIdObj.getSignature().toString();//Extracting demand signature from IDemand object 
			valueobj.setValue(str, poIdObj);
			System.out.println("Demand object has been stored successfully to value house.");
		    log.info("Demand object has been stored successfully to value house.");
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
			log.error(e.getClass().toString() +" remote Exception");
		}	
		return poIdObj.getSignature();
	 }
	
	/**
	 * This method returns the IDemand object for the given signature which uses the get method of ValueHouse.
	 * 
	 * @param podSignature - Object of DemandSignature
	 * @see getValue
	 * @see ValueHouse
	 * 
	 */
	public IDemand getHashTableValue(DemandSignature podSignature)
	{
		if(podSignature == null)
		{
			log.info("Throwing null pointer exception");
			throw new NullPointerException();
		}
		ValueHouse valueobj;
		String str;
		IDemand dReturn = null;
		try 
		{
			valueobj = new ValueHouse();
			str = podSignature.getSignature().toString(); //Converting DemandSignature object to string
			dReturn=(IDemand) valueobj.getValue(str);
		    log.info("getting value successful for"+podSignature.getClass().toString());

			
		}
		catch (RemoteException e) 
		{
			e.printStackTrace();
			log.error(e.getClass().toString() +" remote Exception");

		}
		
		return dReturn;
	}
	
	/**
	 * This method return the object of IDemand. Searches for the pending demand in value house.
	 * If found pending, it returns the object of IDemand. 
	 */
	public IDemand getPendingDemand()
	{	
		IDemand idObj = null;
		
		try 
		{
			ValueHouse valueobj = new ValueHouse();
			
			try 
			{
				
				idObj = valueobj.getPendingValue();
			    log.info("getting pending value");

				
			} 
			catch (GEEException e)
			{
			   e.printStackTrace();
			   log.error(e.getClass().toString() +" GEE Exception");
			}
			
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
			log.error(e.getClass().toString() +" remote Exception");

		}
		
		
		return idObj;
		
	}
}