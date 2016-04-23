package gipsy.tests.GEE.simulator;

import gipsy.GEE.IDP.demands.IDemand;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Singleton pool for demand results to be dispatched.
 * 
 * @author Emil Vassev
 * @version $Id: ResultPool.java,v 1.14 2011/01/10 16:51:09 ji_yi Exp $
 * @since
 */
public class ResultPool 
{
	/**
	 * The buffer.
	 * XXX: move to a move abstract data structure, such
	 * as List or Collection.
	 */ 
	private List<IDemand> oBuffer = null;

	/** 
	 * A reference to the unique instance of the ResultPool class.
	 */
	private static ResultPool soTheInstance = null;
	   
	/**
	 * Hides the constructor - it is private.
	 */
	private ResultPool() 
	{
		this.oBuffer = new LinkedList<IDemand>();
	}
	
	/**
	 * Updates the text area field that shows the computed demands.
	 */	
	public synchronized void updateGUI()
	{
		StringBuilder oStrBuilder = new StringBuilder();
		
		// XXX: use StringBuffer or StringBuildeer
		for(int i = 0; i < this.oBuffer.size(); ++i)
		{
			oStrBuilder.append(this.oBuffer.get(i).getSignature().toString());
			oStrBuilder.append(GlobalDef.CR);
			oStrBuilder.append(GlobalDef.LF);
		}

		GlobalDef.soDGTDialog.getTAResults().setText(oStrBuilder.toString());
	}
	
	/**
     * Returns the unique instance of this class.
     */
	public static ResultPool getInstance() 
	{
		if(null == soTheInstance) 
	    {
			soTheInstance = new ResultPool();
	    }

		return soTheInstance;
	}


	/** 
	 * Sets a demand result item in the buffer.
	 */
	public synchronized void put(IDemand poIem) 
	{
		this.oBuffer.add(poIem);
		//updateGUI();
		GlobalDef.slNumComputedDemands++;
		// Update the statistics info
		//new GUIThread(GlobalDef.soDGTDialog).start();
	}

	/**
	 * Gets a demand result item from the buffer.
	 * @return a demand result
	 */
	public synchronized IDemand get() 
	{
		IDemand oItem;
		
		try
		{
			//oItem = this.oBuffer.remove();
			oItem = this.oBuffer.remove(0);
			//updateGUI();
		}
		catch(Exception ex)
		{
			oItem = null;
		}
		
		return oItem;
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
}

// EOF
