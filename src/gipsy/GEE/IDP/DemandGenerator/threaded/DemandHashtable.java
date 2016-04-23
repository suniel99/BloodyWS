package gipsy.GEE.IDP.DemandGenerator.threaded;

import java.rmi.RemoteException;

import gipsy.GEE.CONFIG;
import gipsy.GEE.GEEException;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.IDemandList;
import gipsy.GEE.IVW.Warehouse.IValueStore;
import marf.util.BaseThread;

/**
 * TODO: Maybe extend from java.util.Hashtable somehow.
 * TODO: Complete implementation of IDemandList.
 *
 * @author Paula, refactoring by Serguei
 */
public class DemandHashtable
implements IDemandList, IValueStore
{
	/**
	 * TODO: fix public
	 */
	public IdentifierContext[] entries;

	public DemandHashtable()
	{
		entries = new IdentifierContext[CONFIG.DEMANDS_MAX];
	}

	/**
	 * add new demand to the hashtable, this won't lock the resource for a long time
	 * identifier
	 */
	private synchronized int createIC(int h, int id, int[] i_cont)
	{
		// TODO: ???? how come the old context will be changed in this createIC function that i have to clone a array with the same value to be used in this function
		// if the program has a lot of dimensions, this process will be long ~_~

		int cont[] = new int[CONFIG.DIMENSION_MAX];
		
		for(int i = 0; i < CONFIG.DIMENSION_MAX; i++)
			cont[i] = i_cont[i];

		System.err.println
		(
			BaseThread.currentThread().getName()
				+ ", ht_syn_getlock on entries "
				+ h
				+ ", "
				+ id
				+ ", "
				+ cont[0]
		);

		if(entries[h] == null)
		{
			System.err.println
			(
				BaseThread.currentThread().getName()
					+ " create entry for "
					+ h
					+ ","
					+ id
					+ ","
					+ cont[0]
			);

			//TODO: ???? how to create a new object whose name is a variable? -- Class.forName()

			if(id == 0)
				entries[h] = new ic0(h, 0, cont);
			else
				if(id == 1)
					entries[h] = new ic1(h, 1, cont);
				else
					if(id == 2)
						entries[h] = new ic2(h, 2, cont);
					else
						if(id == 3)
							entries[h] = new ic3(h, 3, cont);
						else
							if(id == 4)
								entries[h] = new ic4(h, 4, cont);

			return 1;
		}
		else
		{
			System.err.println(Thread.currentThread().getName() + " entry exists for " + h);
			return -1;
		}
	}

	// TODO: implement
	public synchronized Object getValue(String pstrDemand)
	throws GEEException
	{
		return null;
	}
	
	// TODO: implement
	public int setValue(String pstrDemand, Object poValue) 
	throws GEEException
	{
		return 0;		
	}
	
	/**
	 * if the value is not ready, the demanding thread will be suspended untill being notified
	 * however, there seems have only one thread list that all threads will be awakened when a value is ready.
	 *
	 * after it's notified, it will update its own value, such as set the ready flag to ture and notify the other threads that are waiting for itself
	 * it also returns the value to the caller
	 */
	public synchronized int getValue(int h)
	{
		// TODO: locking on sigle ic object makes the execution much slower??? 3-4 times more
		//      synchronized ( entries[h] ) {
		while(!entries[h].isReady())
		{
			try
			{
				// TODO: fix code dup
				System.err.println
				(
					BaseThread.currentThread().getName()
						+ ", waiting for value of "
						+ h
						+ ", "
						+ entries[h].getName()
						+ ", "
						+ entries[h].getCont()[0]
				);

				// TODO: ??? waiting time is not carefully examined yet.
				wait(1000);
				//wait for a maximum wait time to avoid deadlock  entries[h].wait(1000);
			}
			catch(InterruptedException e)
			{
				System.err.println(Thread.currentThread().getName() + e);
			}
			catch(IllegalMonitorStateException e2)
			{
				System.err.println(Thread.currentThread().getName() + e2);
			}
		}

		System.err.println
		(
			BaseThread.currentThread().getName()
				+ ", getValue of "
				+ h
				+ ", "
				+ entries[h].getName()
				+ ", "
				+ entries[h].getCont()[0]
				+ ", value: "
				+ entries[h].getValue()
		);

		entries[h].setReady();
		notifyAll(); // entries[h].notifyAll()

		return entries[h].getValue();
	}

	/**
	 * this is used when leaf nodes (const) is reached. often, this would be the last thread invoked
	 * notify the other threads on the waiting list. this should be synchronized too
	 */
	public synchronized void setValue(int h, int value)
	{
		entries[h].setReady();
		entries[h].setValue(value);

		System.err.println
		(
			BaseThread.currentThread().getName()
				+ ", set value of "
				+ h
				+ ", "
				+ entries[h].getName()
				+ ", "
				+ entries[h].getCont()[0]
		);

		notifyAll();
	}

	/**
	 * demand a ic, if it's not in the hashtable, create the object, then start a thread for it
	 * identifier + context
	 */
	public int demand(int id, int[] k)
	{
		int h = generateCode(id, k);

		System.err.println
		(
			BaseThread.currentThread().getName()
				+ ", ht_demand: demanding "
				+ h
				+ ","
				+ id
				+ ","
				+ k[0]
		);

		if(createIC(h, id, k) == 1 && entries[h] != null)
		{
			System.err.println
			(
				BaseThread.currentThread().getName()
					+ ", start "
					+ h
					+ ","
					+ id
					+ ","
					+ k[0]
			);

			new BaseThread(entries[h]).start();
			// here we may control the number of thread
			// TODO: TLP
		}

		return h;
	}

	/**
	 * generate a unique number for each ic
	 * will be used in the ic when setting value
	 */
	public int generateCode(int id, int[] k)
	{
		return id * 10 + k[0];
	}
	
	/*
	 * -----------------------------
	 * Implementation of IDemandList
	 * -----------------------------
	 */

	/**
	 * TODO: implement properly
	 */
	public int addDemand(String pstrDemand) throws GEEException
	{
		return 0;
	}

	/**
	 * TODO: implement properly
	 */
	public void removeDemand(String pstrDemand) throws GEEException
	{
		
	}

	/**
	 *
	 */
	public boolean isEmpty() throws GEEException
	{
		return (amountDemands() == 0);
	}

	/**
	 *
	 */
	public int amountDemands() throws GEEException
	{
		return this.entries.length;
	}

	/**
	 * TODO: implement properly
	 */
	public String getDemand() throws GEEException
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IVW.Warehouse.IValueStore#getValue(gipsy.GEE.IDP.demands.IDemand)
	 */
	@Override
	public IDemand getValue(IDemand poDemand)
	throws GEEException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IVW.Warehouse.IValueStore#setValue(gipsy.GEE.IDP.demands.IDemand)
	 */
	@Override
	public void setValue(IDemand poResult)
	throws GEEException
	{
		// TODO Auto-generated method stub
		
	}
}

// EOF
