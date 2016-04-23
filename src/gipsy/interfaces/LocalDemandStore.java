package gipsy.interfaces;

import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;


/**
 * LocalDemandStore, will be used as Demand buffer.
 * 
 * @author Bin Han
 * @version $Id: LocalDemandStore.java,v 1.17 2012/06/17 19:14:14 mokhov Exp $
 * @since
 */
public class LocalDemandStore
{	
	/**
	 * Hashtable contain the computed demand, a pair of demand signature and the result (IDemand).
	 * Hashtable object is self-synchronized.
	 */
	private Map<DemandSignature, IDemand> oStore = new Hashtable<DemandSignature, IDemand>();
	
	/**
	 * @param poDemandSignature
	 * @return
	 */
	public synchronized boolean contains(DemandSignature poDemandSignature)
	{
		return this.oStore.containsKey(poDemandSignature);
	}
	
	/**
	 * Put a computed demand in the local store.
	 * 
	 * @param poDemandSignature
	 * @param poResult
	 */
	public synchronized void put(DemandSignature poDemandSignature, IDemand poResult)
	{
		this.oStore.put(poDemandSignature, poResult);
	}
	
	public synchronized IDemand get(DemandSignature poDemandSignature)
	{
		return this.oStore.get(poDemandSignature);
	}
	
	public synchronized void remove(DemandSignature poDemandSignature)
	{
		this.oStore.remove(poDemandSignature);
	}
	
	/**
	 * @return
	 */
	public Collection<IDemand> getDemandValues()
	{
		return this.oStore.values();
	}

	/**
	 * @return
	 */
	public Set<Map.Entry<DemandSignature, IDemand>> getEntrySet()
	{
		return this.oStore.entrySet();
	}

	/**
	 * @return
	 */
	public Set<DemandSignature> getKeySet()
	{
		return this.oStore.keySet();
	}

	/*
	 * ----------
	 * Object API
	 * ----------
	 */
	
	public int hashCode()
	{
		return this.oStore.hashCode();
	}
	
	public boolean equals(LocalDemandStore oStore)
	{
		return oStore.hashCode() == (this.hashCode());
	}

	/**
	 * For the purpose of testing.
	 * XXX: convert to toString() instead.
	 */
	public synchronized void printDemand()
	{
		System.out.println("<----LocalDemandStore.printDemand---- ");
		
		for(DemandSignature oDemandSignature: this.oStore.keySet())
		{
			IDemand oDemand = this.oStore.get(oDemandSignature);
			System.out.println(oDemand);
		}
		
		System.out.println(" ----LocalDemandStore.printDemand---->");
	}
}

// EOF
