package gipsy.GEE.IDP.DemandGenerator.ws;

import java.rmi.server.*;
import java.rmi.*;

import gipsy.GEE.GEEException;

import gipsy.GEE.IVW.Warehouse.*;

/**
 * TODO: document
 * @author Paula
 */
public class IdentifierContextServer
extends UnicastRemoteObject
implements Remote
{
	public IdentifierContextServer() throws RemoteException
	{
		super();
	}

	public static void main(String args[])
	throws GEEException
	{
		try
		{
			new IdentifierContextServer().start(args);
		}
		catch(RemoteException e)
		{
			throw new GEEException(e);
		}
	}

	public void start(String args[])
	throws GEEException
	{
		System.out.println("IdentifierContextServer starting up...");

		System.out.println("Setting security manager...");

		if(System.getSecurityManager() == null)
		{
			System.setSecurityManager(new RMISecurityManager());
		}

		try
		{
			System.out.println("Binding Value Warehouse and Demand List...");

			// IC aic = new IC0 ();
			ValueHouse oValueHouse = new ValueHouse();
			DemandList oDemandList = new DemandList();

			//     	  Naming.rebind ("//132.205.45.81:1101/VH", vHouse);
			//	  Naming.rebind ("//132.205.45.81:1101/DL", dList);
			Naming.rebind("ValueHouse", oValueHouse);
			Naming.rebind("DemandList", oDemandList);

			System.out.println("IdentifierContextServer bound in registry.");
		}
		catch(Exception e)
		{
			throw new GEEException(e);
		}
	}
}

// EOF
