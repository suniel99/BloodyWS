package gipsy.GEE.multitier.DST.ws;

import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;

import java.rmi.Remote;

/**
 * An interface for RMI DST Wrapper that extends the remote interface
 * @author 
 * @since 1.0.0
 * @version $Id: IRMIDSTWrapper.java,v :
 */


public interface IWSDSTWrapper extends Remote 
{   
	/**
	 * Write demand object to store in hash table.
	 *   
	 * @param poIDemand
	 * @return the DemandSignature object
	 * @throws Exception
	 */
	public DemandSignature setHashTable(IDemand poIDObj) 
			throws Exception;
	
	/**
	 * Write a result into a store.
	 *   
	 * @param poDSignature
	 * @return IDemand object which contains the HashTable value  
	 * @throws Exception
	 */
	public IDemand getHashTableValue(DemandSignature poDSignature)
			throws Exception;
	
	/**
	 * To get the pending Demand
	 * @return IDemand object that contains the pending demands
	 * @throws Exception
	 */
	public IDemand getPendingDemand()
			throws Exception;
}
