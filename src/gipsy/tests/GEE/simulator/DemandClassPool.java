package gipsy.tests.GEE.simulator;

import java.util.LinkedList;
import java.util.NoSuchElementException;


/**
 * Singleton pool for class names of demands to be created.
 * 
 * @author Emil Vassev
 * @version $Id: DemandClassPool.java,v 1.4 2009/09/08 07:51:02 mokhov Exp $
 * @since
 */
public class DemandClassPool 
{
	/**
	 * The buffer.
	 * XXX: generalize to something List.
	 */ 
	private LinkedList<String> oBuffer = null;

	/** 
	 * A reference to the unique instance of the DemandClassPool class.
	 */
	private static DemandClassPool soInstance = null;
	   
	/**
	 * Hides the constructor - it is private.
	 */
	private DemandClassPool() 
	{
		this.oBuffer = new LinkedList<String>();
	}
	
   /**
    * Returns the unique instance of this class.
    */
	public static synchronized DemandClassPool getInstance() 
	{
		if(null == soInstance) 
	    {
			soInstance = new DemandClassPool();
	    }

	    return soInstance;
	}

	/** 
	 * Sets a demand class name item in the buffer.
	 */
	public synchronized void put(String pstrItem) 
	{
		this.oBuffer.add(pstrItem);
	}

	/**
	 * Gets a class name item from the buffer.
	 * @return a class name
	 */
	public synchronized String get() 
	{
		String strItem;
		
		try
		{
			strItem = this.oBuffer.remove();
		}
		catch(NoSuchElementException ex)
		{
			strItem = "";
		}
		
		return strItem;
	}
	
	/**
	 * Checks if the buffer is empty.
	 * @return true/false if the buffer is empty/not empty
	 */
	public synchronized boolean isEmpty()
	{
		return oBuffer.size() == 0;
	}	
}

// EOF
