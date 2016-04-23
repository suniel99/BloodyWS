package gipsy.GEE.IDP.DemandGenerator.ws;

import gipsy.GEE.CONFIG;
import gipsy.GEE.IDP.demands.IDemandList;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

/**
 * TODO: Replace RemoteException with GEEException
 *
 * $Header: /cvsroot/gipsy/gipsy/src/gipsy/GEE/IDP/DemandGenerator/ws/DemandList.java,v 1.10 2009/08/12 01:57:52 mokhov Exp $
 *
 * @author Paula
 */
public class DemandList
extends UnicastRemoteObject
implements IDemandList, Remote, CONFIG
{
	/**
	 *
	 */
	private static Vector demands = new Vector();

	/**
	 *
	 */
	private static int counter = 0;

	/**
	 *
	 */
	public DemandList() throws RemoteException
	{
		super();
	}

	/**
	 * demand list only contains the string form of a demand
	 * @param d demand
	 * @return int - 1 or 0, meaning added or not
	 * TODO: why not boolean then? Are other states possible? Clarify.
	 */
	public synchronized int addDemand(String pstrDemand)
	{
		if(demands.contains(pstrDemand))
		{
			System.out.println("DemandList: demand exist " + pstrDemand);
			//  increasePriority ( d );  // or it could be add the demanding objects on the list
			return 0;
		}
		else
		{
			demands.add(pstrDemand);
			System.out.println("DemandList: add demand " + pstrDemand + " " + demands.indexOf(pstrDemand));
			return 1;
		} // else
	}

	public synchronized void removeDemand(String pstrDemand)
	{
		demands.remove(pstrDemand);
	}

	public boolean isEmpty()
	{
		return demands.isEmpty();
	}

	public int amountDemands()
	{
		return demands.size();
	}

	public String getDemand()
	{
		String strDemand = null;

		int iSize = demands.size();

		if(iSize > 0)
		{
			strDemand = (String)demands.get(counter % iSize);
			System.out.println("DemandList: get demand from DemadList " + strDemand);
			counter++;
		}

		return strDemand;
	}
}

// EOF
