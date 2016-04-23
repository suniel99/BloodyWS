package gipsy.GEE.multitier.DGT;

import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcherException;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.interfaces.LocalDemandStore;

import marf.util.BaseThread;


/**
 * Thread to deposit pending demand into Demand Store.
 * XXX Make sure when putting demand into the Local Demand Store, there is no computed demand there with the same signature. 
 * 
 * @author Bin Han
 * @version $Id: DepositDemand.java,v 1.3 2010/12/09 04:30:56 mokhov Exp $
 * @since
 */
public class DepositDemand
extends BaseThread
{
	private LocalDemandStore oLocalDemandStore;
	
	private IDemandDispatcher oDemandDispatcher;
	
	public DepositDemand(LocalDemandStore poLocalDemandStore, IDemandDispatcher poDemandDispatcher)
	{
		this.oLocalDemandStore = poLocalDemandStore;
		this.oDemandDispatcher = poDemandDispatcher;
	}

	public void run()
	{
		DemandSignature oSignature;
		
		if(this.oLocalDemandStore != null)
		{
			//Traverse local demand store.
			for(DemandSignature oDemandSignature: this.oLocalDemandStore.getKeySet())
		    {
		        IDemand oDemand = this.oLocalDemandStore.get(oDemandSignature);
		        
		        //Send the pending demand to Demand Store, set its state to processing.
		        if(oDemand.getState().isPending())
		        {   	
					// Now send it to the store
					try
					{
						oSignature = this.oDemandDispatcher.writeDemand(oDemand);
						oDemand.setState(DemandState.INPROCESS);
						this.oLocalDemandStore.put(oSignature, oDemand);
						System.out.println("Demand Signature: " + oSignature + " inprocess.");
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
