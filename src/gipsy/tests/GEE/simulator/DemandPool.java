package gipsy.tests.GEE.simulator;

import gipsy.GEE.IDP.demands.IDemand;

import java.util.LinkedList;
import java.util.NoSuchElementException;


/**
 * Singleton pool for demands to be dispatched.
 * 
 * @author Emil Vassev
 */
public class DemandPool 
{
	/**
	 * The buffer.
	 * XXX: use a more general interface, like List.
	 */ 
	private LinkedList<IDemand> oBuffer = null;

	/** 
	 * A reference to the unique instance of the DemandPool class.
	 */
	private static DemandPool soInstance = null;
	   
	/**
	 * Hides the constructor - it is private.
	 */
	private DemandPool() 
	{
		this.oBuffer = new LinkedList<IDemand>();
	}
	
   /**
    * Returns the unique instance of this class.
    */
	public static synchronized DemandPool getInstance() 
	{
		if(null == soInstance) 
	    {
			soInstance = new DemandPool();
	    }
		
	    return soInstance;
	}

	/**
	 * Updates the text area field that shows the pending demands.
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
		
		GlobalDef.soDGTDialog.getTADemands().setText(oStrBuilder.toString());
	}
	
	/** 
	 * Sets a demand result item in the buffer.
	 */
	public synchronized void put(IDemand poItem) 
	{
		this.oBuffer.add(poItem);
		// updateGUI();
		GlobalDef.slNumPendingDemands++;
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
			oItem = this.oBuffer.remove();
			GlobalDef.slNumPendingDemands--;
			//updateGUI();
		}
		catch(NoSuchElementException ex)
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
