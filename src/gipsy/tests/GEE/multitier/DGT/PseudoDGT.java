package gipsy.tests.GEE.multitier.DGT;

import java.lang.reflect.Constructor;

import gipsy.Configuration;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcher;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcherException;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.TimeLine;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.DGT.DGTWrapper;
import gipsy.GEE.multitier.DST.TAFactory;


/**
 * A DGT Wrapper implementation that simply mimics DemandWorker
 * without actually parsing any intensional demands. This class
 * is created to test the performance impact on DWT when the number
 * of PseudoDGT connected to the DST increases.
 * 
 * @author Yi Ji
 * @version $Id: PseudoDGT.java,v 1.3 2012/03/31 00:08:39 mokhov Exp $
 */
public class PseudoDGT 
extends DGTWrapper
{
	/**
	 * Stopping condition. 
	 */
	private boolean bIsWorking = false;
	
	public PseudoDGT(Configuration poConfiguration) 
	{
		super(poConfiguration);
	}

	@Override
	public void run() 
	{
		this.bIsWorking = true;
		
		IDemand oDemand; 
		IDemand oResult;
		
		//printOut("");
		TimeLine oTimeLine = new TimeLine();
		
		String strBeginReceive = "DGT" + this.strTierID + " BRev";
		String strEndReceive = "DGT" + this.strTierID + " ERev";
		String strBeginSend = "DGT" + this.strTierID + " BSnd";
		String strEndSend = "DGT" + this.strTierID + " ESndLast";
		
		while(this.bIsWorking)
		{
			try 
			{
				oTimeLine.addTimeLine(strBeginReceive);
				oDemand = (IDemand) this.oDemandDispatcher.readDemand();
				//printOut("Demand received: name: " + oDemand.getSignature());
				oDemand.addTimeLine(strEndReceive);
				
				oDemand.addTimeLine(oTimeLine);
				
				//if (we have the resource for process procedural demand)
				oResult = oDemand.execute();
				//printOut("Demand computed");
				//this.oLocalDemandStore.put(oDemand.getSignature(), oResult);
				
				//printOut("The computed result value: " + oResult.getResult());
				oTimeLine = new TimeLine();
				oDemand.addTimeLine(strBeginSend);
				this.oDemandDispatcher.writeResult(oDemand.getSignature(), oResult);
				oTimeLine.addTimeLine(strEndSend);
				//printOut("Result dispatched");
			} 
			catch(DemandDispatcherException oException) 
			{
				oException.printStackTrace(System.err);
			} 
		}
	}

	@Override
	public void startTier() 
	throws MultiTierException 
	{
		try
		{
			// Create a TA instance using the configuration
			Configuration oTAConfig = (Configuration) this.oConfiguration.getObjectProperty(DGTWrapper.TA_CONFIG);
			ITransportAgent oTA = TAFactory.getInstance().createTA(oTAConfig);
			
			// Create a DemandDispatcher instance using the configuration
			String strImplClassName = this.oConfiguration.getProperty(DGTWrapper.DEMAND_DISPATCHER_IMPL);
			Class<?> oImplClass = Class.forName(strImplClassName);
			Class<?>[] aoParamTypes = new Class[] {ITransportAgent.class};
			Constructor<?> oImplConstructor = oImplClass.getConstructor(aoParamTypes);
			Object[] aoArgs = new Object[]{oTA};
			
			this.oDemandDispatcher = (DemandDispatcher)oImplConstructor.newInstance(aoArgs);
			this.oDemandDispatcher.setTAExceptionHandler(this.oTAExceptionHandler);
			
			new Thread(this).start();
		}
		catch(MultiTierException oException)
		{
			throw oException;
		}
		catch(Exception oException)
		{
			throw new MultiTierException(oException);
		}
	}

	@Override
	public void stopTier() 
	throws MultiTierException
	{
		this.bIsWorking = false;
	}
}

// EOF
