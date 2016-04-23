package gipsy.GEE.IVW.Warehouse;

import gipsy.GEE.GEEException;
import gipsy.GEE.IDP.demands.IDemand;

import java.rmi.RemoteException;


/**
 * All implementations of the warehouse implement this.
 * Editorializing by Serguei.
 * 
 * @author Paula Bo Lu
 * @author Serguei Mokhov
 * @author Yi Ji
 * 
 * @since
 * @version
 */
public interface IValueStore
{
	/**
	 * Older style API.
	 * @param pstrDemand
	 * @return
	 * @throws GEEException
	 */
	@Deprecated
	public Object getValue(String pstrDemand)
	throws GEEException;
	
	/**
	 * Older style API.
	 * @param pstrDemand
	 * @param poValue
	 * @return
	 * @throws GEEException
	 */
	@Deprecated
	public int setValue(String pstrDemand, Object poValue)
	throws GEEException;

	/**
	 * Get the value of the specified demand.
	 * @param poDemand the demand asking for value
	 * @return the value of the demand
	 * @throws GEEException
	 * 
	 * XXX: must not impose RemoteException (and consequently RMI) !!! in the general case
	 * 
	 * @throws RemoteException the exception is required by RMI
	 */
	public IDemand getValue(IDemand poDemand)
	throws GEEException, RemoteException;
	
	/**
	 * Store the result.
	 * @param poResult the result to be stored
	 * @throws GEEException
	 * 
	 * XXX: must not impose RemoteException (and consequently RMI) !!! in the general case
	 * 
	 * @throws RemoteException the exception is required by RMI
	 */
	public void setValue(IDemand poResult)
	throws GEEException, RemoteException;
}

// EOF
