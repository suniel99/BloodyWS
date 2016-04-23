package gipsy.interfaces;

import java.util.HashMap;
import java.util.Map;


/**
 * LocalGEERPool in DGT and DWT, containing computed GEERs, provide lookup service, act as a cache.
 * 
 * @author Bin Han
 * @version $Id: LocalGEERPool.java,v 1.5 2010/12/09 04:31:00 mokhov Exp $
 * @since
 */
public class LocalGEERPool
{
	/**
	 * All the retrieved GEER are maintained in the oStore Hashtable.
	 */
	private Map<GEERSignature, GIPSYProgram> oStore = new HashMap<GEERSignature, GIPSYProgram>();
	
	public LocalGEERPool()
	{
		this.oStore = new HashMap<GEERSignature, GIPSYProgram>();
	}
	
	public LocalGEERPool(GEERSignature poGEERSignature, GIPSYProgram poGIPSYProgram)
	{
		this.oStore.put(poGEERSignature, poGIPSYProgram);
	}
	
	public LocalGEERPool(LocalGEERPool poLocalGEERPool)
	{
		this.oStore = poLocalGEERPool.getAll();
	}
	/**
	 * Put a new GEER in the LocalGEERPool
	 * @param poGEER
	 */
	public void put(GEERSignature poGEERSignature, GIPSYProgram poGEER)
	{
		this.oStore.put(poGEERSignature, poGEER);
	}
	
	/**
	 * Get GEER from the pool.
	 * 
	 * @param poGEERSignature
	 * @return
	 */
	public GIPSYProgram get(GEERSignature poGEERSignature)
	{
		return this.oStore.get(poGEERSignature);
	}
	
	public Map<GEERSignature, GIPSYProgram> getAll()
	{
		return this.oStore;
	}
	/**
	 * Removes the first occurrence of the specified element from this GEERPool.
	 * @param poGEER
	 */
	public void remove(GEERSignature poGEERSignature)
	{
		this.oStore.remove(poGEERSignature);
	}
	
	/**
	 * lookup one specific GEER in the LocalGEERPool
	 * @param poGEER
	 * @return true if poGEER is in the LocalGEERPool
	 */
	public boolean contains(GEERSignature poGEERSignature)
	{
		return this.oStore.containsKey(poGEERSignature);
	}
	
	/**
	 * @return true is the list contains no elements
	 */
	public boolean isEmpty()
	{
		return this.oStore.isEmpty();
	}
/*	
	public int hashCode()
	{
		return this.oStore.hashCode();
	}
*/	
	public boolean equals(LocalGEERPool poLocalGEERPool)
	{		
		return this.oStore.equals(poLocalGEERPool.oStore);
	}
}

//EOF