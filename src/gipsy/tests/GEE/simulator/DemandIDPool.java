package gipsy.tests.GEE.simulator;

import gipsy.GEE.IDP.demands.DemandSignature;

import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;


/**
 * Singleton pool for demand IDs.
 * 
 * @author Emil Vassev
 * @version $Id: DemandIDPool.java,v 1.7 2011/01/12 22:03:17 ji_yi Exp $
 * @since
 */
public class DemandIDPool 
{
	/**
	 * The buffer.
	 * XXX: use a more generic interface, such as List
	 */ 
	LinkedList<DemandSignature> oBuffer = null;

	/** 
	 * A reference to the unique instance of the DemandIDPool class.
	 */
	private static DemandIDPool soInstance = null;
	   
	/**
	 * Hides the constructor - it is private.
	 */
	private DemandIDPool() 
	{
		this.oBuffer = new LinkedList<DemandSignature>();
	}
	
   /**
    * Returns the unique instance of this class.
    */
	public static DemandIDPool instance() 
	{
		if(null == soInstance) 
	    {
			soInstance = new DemandIDPool();
	    }
		
	    return soInstance;
	}

	/** 
	 * Sets a demand result item in the buffer.
	 */
	public synchronized void put(DemandSignature poSignature) 
	{
		this.oBuffer.add(poSignature);
	}

	/**
	 * Gets a demand result item from the buffer.
	 * @return a demand result
	 */
	public synchronized DemandSignature get() 
	{
		DemandSignature poSignature;
		
		try
		{
			poSignature = this.oBuffer.remove();
		}
		catch (NoSuchElementException ex)
		{
			poSignature = null;
		}
		
		return poSignature;
   }

   /**
    * Checks if the buffer is empty.
    * @return true/false if the buffer is empty/not empty
    */
   public synchronized boolean isEmpty()
   {
	   return this.oBuffer.size() == 0;
   }
   
   public void clear()
   {
		this.oBuffer.clear();
   }
   
   public void reverse()
   {
	   Collections.reverse(this.oBuffer);
   }
}

// EOF
