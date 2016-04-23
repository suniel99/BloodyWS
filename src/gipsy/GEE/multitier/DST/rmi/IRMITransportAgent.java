package gipsy.GEE.multitier.DST.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import gipsy.Configuration;
import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;

/**
 * An interface for RMI Transport Agent that extends remote interface
 * @author 
 * @since 1.0.0
 * @version $Id: IRMIDemandDispatcher.java,v :
 */

public interface IRMITransportAgent extends  Remote 
{
	
	/**
	 * To get Demand stored in IDemand Object
	 *   
	 * @return IDemand object
	 * @throws RemoteException, DMSException 
	 */
	public IDemand fetchDemand()
	throws RemoteException, DMSException;
	
	/**
	 * To get Demand if it Exists in IDemand Object
	 *   
	 * @return IDemand object
	 * @throws RemoteException, DMSException 
	 */
	public IDemand fetchDemandIfExists() 
	throws RemoteException, DMSException;

	/**
	 * To get Demand by providing the name of it
	 *   
	 * @param pstrDestination
	 * @return IDemand object 
	 * @throws RemoteException, DMSException 
	 */
	public IDemand fetchDemand(String pstrDestination) 
	throws RemoteException, DMSException;

	/**
	 * To get the result
	 * 
	 * @param poSignature
	 * @return IDemand object
	 * @throws RemoteException, DMSException 
	 */
	public IDemand fetchResult(DemandSignature poSignature)
	throws RemoteException, DMSException;

	/**
	 * To get the result if it exists
	 * 
	 * @param poSignature
	 * @return IDemand object
	 * @throws RemoteException, DMSException 
	 */
	public IDemand fetchResultIfExists(DemandSignature poSignature)
	throws RemoteException, DMSException;

	/**
	 * Carries the demand
	 * 
	 * @param poDemand
	 * @return DemandSignature object
	 * @throws RemoteException, DMSException 
	 */
	public DemandSignature carryDemand(IDemand poDemand)
	throws RemoteException, DMSException;

	/**
	 * Carries the demand result
	 * 
	 * @param poResult
	 * @return DemandSignature object
	 * @throws RemoteException, DMSException 
	 */
    public DemandSignature carryResult(IDemand poResult) 
	throws RemoteException, DMSException;    
	
	
			

	/**
	 * Get the configuration 
	 * 
	 */
	public Configuration getConfiguration();

}
