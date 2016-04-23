package gipsy.GEE.multitier.DGT;

import marf.util.BaseThread;

import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcherException;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.interfaces.LocalDemandStore;

/**
 * Thread to withdraw computed result from Demand Store, put back in the Local Demand Store.
 * 
 * @author Bin Han
 * @version $Id: WithdrawResult.java,v 1.3 2010/12/09 04:30:56 mokhov Exp $
 * @since 
 */
public class WithdrawResult
extends BaseThread
{
	private LocalDemandStore oLocalDemandStore;

	private IDemandDispatcher oDemandDispatcher;

	public WithdrawResult(LocalDemandStore poLocalDemandStore, IDemandDispatcher poDemandDispatcher)
	{
		this.oLocalDemandStore = poLocalDemandStore;
		this.oDemandDispatcher = poDemandDispatcher;
	}
	
	public void run()
	{
		IDemand oWorkResult;
		
//		while (true){}
		if (this.oLocalDemandStore != null)
		{
			//Traverse local demand store.
			for(DemandSignature oDemandSignature: this.oLocalDemandStore.getKeySet())
		    {
		        IDemand oDemand = this.oLocalDemandStore.get(oDemandSignature);
		        
		        //Request the InProcess demand from Demand Store, set its state to computed, and put back to the Local Demand Store.
		        if (oDemand.getState().isInProcess())
		        {   	
					// Get the result from Demand Store, 
					try
					{
						oWorkResult = this.oDemandDispatcher.readResultIfExists(oDemandSignature);
						
						if(oWorkResult != null)
						{
							oDemand.storeResult(oWorkResult);
							oDemand.setState(DemandState.INPROCESS);
							this.oLocalDemandStore.put(oDemandSignature, oDemand);
							System.out.println("Demand Signature: "+oDemandSignature+" inprocess.");
						}
					}
					catch(DemandDispatcherException e)
					{
						e.printStackTrace(System.err);
					}
		        }
		    }
		}

		// gives a chance to others to run on green threads Java
		yield();
	}
}
