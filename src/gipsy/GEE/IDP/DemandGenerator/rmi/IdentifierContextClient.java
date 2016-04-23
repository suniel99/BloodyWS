package gipsy.GEE.IDP.DemandGenerator.rmi;

import gipsy.GEE.CONFIG;
import gipsy.GEE.GEEException;
import gipsy.GEE.IDP.DemandGenerator.DemandGenerator;
import gipsy.GEE.IDP.demands.IDemandList;
import gipsy.GEE.IVW.Warehouse.IValueStore;
import gipsy.interfaces.IIdentifierContext;
import gipsy.util.NotImplementedException;

import java.lang.reflect.Constructor;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.util.Date;

/**
 * IC objects stay on the client machines, while the demand list
 * and value house are located on the server.
 *
 * @author Paula Bo Lu
 */
public class IdentifierContextClient
implements IIdentifierContext, Remote, CONFIG
{
	// demanding id = 0 A = if (#.d == 0 ) then 0 else A+1@.d (#.d -1 )
	private Object value;
	private int icx = 0;  // demanded id
	private int[] contxt = new int[DIMENSION_MAX];

	private static IValueStore vHouse;  // IValueHouse is a interface, value house
	private static IDemandList dList; // demand list, interface, or you may get a cast execption shared by all IC

	public IdentifierContextClient()
	{
	}

	public void atWork(int iCont)
	{
		if(System.getSecurityManager() == null)
		{
			System.setSecurityManager(new RMISecurityManager());
		}

		try
		{
			System.err.println("Start a client, looking for vh and dl");

//      vHouse = (IValueHouse) Naming.lookup( "//132.205.45.81:1101/IValueHouse");   // remote
			vHouse = (IValueStore)Naming.lookup("IValueHouse");   // remote
			System.err.println("received IValueHouse");
	
			dList = (IDemandList)Naming.lookup("IDemandList");  // remote
//      dList = (IDemandList) Naming.lookup ( "//132.205.45.81:1101/IDemandList" );  // remote
			System.err.println("received IDemandList");

			contxt[0] = iCont;

      //System.err.println( "received IValueHouse and IDemandList");

			Date d = new Date();  // for timing

			String sn = DemandGenerator.generateLegacyDemand(icx, contxt);

			System.err.println(sn);

			// ERROR ARGUMENT TYPE MISMATCH, if any of the parameters is user-defined
			addDemand(sn); // add a new demand in the IDemandList for the ultimate value we need

			String ds = dList.getDemand();  // pick up a demand from the IDemandList until all of them have been solved

			System.err.println(ds);

			IdentifierContext demandObj = new IdentifierContext();
			demandObj.init(contxt); // initialize IValueHouse and IDemandList for IC object

			while(ds != null)
			{
				icx = parseDemand(ds, contxt);
				demandObj = getIC(icx, contxt);
				demandObj.cal();
				ds = dList.getDemand();
			}

			System.err.println("The result of calculation is " + getValue(sn));

			Date d2 = new Date();

			System.err.println("Timing: " + Long.toString(d2.getTime() - d.getTime()));
		}
		catch(Exception e)
		{
			System.err.println("error" + e.getMessage());
		}
	} // icclient

	/**
	 * use reflection to create ic objects
	 */
	public IdentifierContext getIC(int id, int[] cont)
	{
		String icStr = "IC" + Integer.toString(id);
		Class<?> cls = null;

		try
		{
			cls = Class.forName(icStr);
		}
		catch(Exception e)
		{
			System.err.println(" Error: creating demandIC - " + icStr + e.getMessage());
		}

		Class<?> partypes[] = null;
		Constructor<?> ct = null;

		try
		{
			ct = cls.getConstructor(partypes);
		}
		catch(Exception e)
		{
			System.err.println(" Error: creating Constructor - " + icStr + e.getMessage());
		}

		Object arglist[] = null;
		Object retobj = null;

		try
		{
			retobj = ct.newInstance(arglist);
		}
		catch(Exception e)
		{
			System.err.println(" Error: creating Instance - " + icStr + e.getMessage());
		}

		IdentifierContext demandIC = (IdentifierContext)retobj;
		demandIC.init(cont);

		return demandIC;
	}

/*  public IdentifierContext getIC ( int id, int[] cont ) {
    IdentifierContext demandIC = null;
    switch ( id ) {
      case 0:
        try {
          demandIC = new IC0( ) ;
        }
        catch ( Exception e ) {
          System.err.println( " Error: creating demandIC - " + e.getMessage() );
        }
        break;
      default:
        System.err.println( "ICImp: " + id );
    }
    demandIC.init( cont );
    return demandIC;
  }
*/

	public static int parseDemand(String sn, int[] context)
	{
		int idx = sn.indexOf(":");
		int idx2;

		String str = sn.substring(0, idx );
		String str2 = sn.substring ( idx + 1 );  //
		String str3;

		int contxt[] = new int[DIMENSION_MAX];
		int k = 0;

		while(str2.length () > 0)
		{
			idx2 = str2.indexOf(":");
			str3 = str2.substring(0, idx2);
			contxt[k++] = (new Integer(str3)).intValue();
			str2 = str2.substring(idx2 + 1);
		}

		for(k = 0; k < DIMENSION_MAX; k++)
		context[k] = contxt[k];

		return (new Integer(str)).intValue();
	}

	//public Object getValue(String icx) throws RemoteException
	public Object getValue(String icx) throws GEEException
	{
		Object obj = vHouse.getValue(icx);
		System.err.println("???: " + icx);
		return obj;
	}

	public void addDemand(String icx) //throws RemoteException
	throws GEEException
	{
		int did = dList.addDemand(icx);

		if(did == 1)
			System.err.println("+++: " + icx);
	}

	/**
	 * set value in the warehouse, and remote the relative demand
	 */
	public void setValue(String icx, Object value) //throws RemoteException
	throws GEEException
	{
		int did = vHouse.setValue(icx, value);
		dList.removeDemand(icx);

		if(did == 1)
			System.err.println("!!!: " + icx + " " + value.toString());
	}

	/*
	 * ---------------------
	 * Implementation of the IIdentifierContext
	 * ---------------------
	 */

	public Object cal()
	{
		throw new NotImplementedException("IIdentifierContextClient.cal()");
	}

	public boolean isReady()
	{
		throw new NotImplementedException("IIdentifierContextClient.isReady()");
	}

	public int getValue()
	{
		throw new NotImplementedException("IIdentifierContextClient.getValue()");
	}

	public void setReady()
	{
		throw new NotImplementedException("IIdentifierContextClient.setReady()");
	}

	public void setValue(int piValue)
	{
		throw new NotImplementedException("IIdentifierContextClient.setValue(int)");
	}

	public int getName()
	{
		throw new NotImplementedException("IIdentifierContextClient.getName()");
	}

	public int getHcode()
	{
		throw new NotImplementedException("IIdentifierContextClient.getHcode()");
	}

	public int[] getCont()
	{
		throw new NotImplementedException("IIdentifierContextClient.getCont()");
	}

	public static void main(String args[])
	{
		IdentifierContextClient iccl = new IdentifierContextClient();
		Integer iObj = new Integer(args[0]);
		iccl.atWork(iObj.intValue());
	}
}

// EOF
