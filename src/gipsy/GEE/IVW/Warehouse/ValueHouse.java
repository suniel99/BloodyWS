package gipsy.GEE.IVW.Warehouse;

import gipsy.GEE.GEEException;
import gipsy.GEE.IDP.demands.Demand;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.IDemand;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Map;


/**
 * TODO: document.
 * 
 * @author 
 * @since
 * @version $Id: ValueHouse.java,v 1.10 2009/08/12 04:00:21 mokhov Exp $
 */
public class ValueHouse
extends UnicastRemoteObject
implements IValueStore, Remote
{
	/**
	 * 
	 */
	private static Hashtable<String, Object> oValues = new Hashtable<String, Object>();

	/**
	 * @throws RemoteException
	 */
	public ValueHouse()
	throws RemoteException
	{
		super();
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IVW.Warehouse.IValueStore#getValue(java.lang.String)
	 */
	public Object getValue(String pstrDimension)
	{
		Object oValue = null;

		try
		{
			oValue= this.oValues.get(pstrDimension);
			System.out.println("ValueHouse: value of " + pstrDimension + " is " + oValue);
		}
		catch(Exception e)
		{
			System.err.println("Error: vH get value" + e.getMessage());
			e.printStackTrace(System.err);
		}

		return oValue;
	}

	/**
	 * @return value: 1 does / 0 faked
	 * @see gipsy.GEE.IVW.Warehouse.IValueStore#setValue(java.lang.String, java.lang.Object)
	 */
	public synchronized int setValue(String pstrDimension, Object poValue)
	{
		int iRetVal = 0;

		

		if(!this.oValues.containsKey(pstrDimension))
		{
			try
			{
				this.oValues.put(pstrDimension, poValue);
				System.out.println("ValueHouse: setValue of " + pstrDimension + " " + poValue);
				iRetVal = 1;     // really added
			}
			catch(Exception e)
			{
				System.err.println("The inserted key or the value is null. Key: " + pstrDimension + "   Value:" + poValue);
				e.printStackTrace(System.err);
			}
		}

		return iRetVal;
	}

	/* (non-Javadoc)
	 * XXX: employ.
	 * @see gipsy.GEE.IVW.Warehouse.IValueStore#getValue(gipsy.GEE.IDP.demands.IDemand)
	 */
	@Override
	public IDemand getValue(IDemand poDemand)
	throws GEEException, RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * XXX: employ.
	 * @see gipsy.GEE.IVW.Warehouse.IValueStore#setValue(gipsy.GEE.IDP.demands.IDemand)
	 */
	@Override
	public void setValue(IDemand poResult)
	throws GEEException, RemoteException
	{
		// TODO Auto-generated method stub
	}
	
	/**
	 * This method checks for pending demand, if is there any pending demand,
	 *  it's gives back the object of IDemand. 
	 * 
	 * @return oDemand
	 * @throws GEEException
	 * @throws RemoteException
	 */
	public IDemand getPendingValue()
	throws GEEException, RemoteException
	{
		Object Obj = null; 
		Demand oDemand = null;
		
		 for(Map.Entry entry: oValues.entrySet())
		 {
			 
			 String  key = (String)entry.getKey();
			 Obj =oValues.get(key);
			 oDemand = (Demand)Obj;
			 DemandState dState = oDemand.getState();
			 
	            if(DemandState.PENDING == dState)
	            {
	               
	              return (IDemand)oDemand; //breaking because its one to one map
	            }
	        }



		
		
		
		return (IDemand)oDemand;
	}
}

// EOF
