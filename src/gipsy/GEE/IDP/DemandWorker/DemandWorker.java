package gipsy.GEE.IDP.DemandWorker;

import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITA;
import gipsy.GEE.IDP.DemandGenerator.jms.JMSTransportAgent;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.TimeLine;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.TAExceptionHandler;
import gipsy.interfaces.LocalDemandStore;
import gipsy.interfaces.LocalGEERPool;
import gipsy.util.GIPSYRuntimeException;


/**
 * A light-weight worker extracting the core part of the WorkerJTA.
 * Please make sure the TA is set before launch.
 * 
 * XXX: allow Configuration object use etc. setting which TA to use, etc/ 
 * 
 * @author Yi Ji
 * @author Bin Han
 * @version $Id: DemandWorker.java,v 1.31 2012/06/17 17:09:14 mokhov Exp $
 * @since
 */
public class DemandWorker
implements IDemandWorker
{
	/**
	 * XXX
	 */
	protected ITransportAgent oTA;
	
	/**
	 * 
	 */
	protected LocalDemandStore oLocalDemandStore;
	
	/**
	 * 
	 */
	protected LocalGEERPool oLocalGEERPool;
	
	/**
	 * 
	 */
	protected volatile boolean bIsWorking = true;
	
	/**
	 * 
	 */
	private TAExceptionHandler oTAExceptionHandler = null;
	
	/**
	 * 
	 */
	private String strWorkerID = null;
	
	/**
	 * Default constructor. 
	 */
	public DemandWorker()
	{
		
	}
	
	/**
	 * @param pstrWorkerID
	 */
	public DemandWorker(String pstrWorkerID)
	{
		this.strWorkerID = "DWT" + pstrWorkerID;
	}
	
	/**
	 * @param poLocalDemandStore
	 * @param poLocalGEERPool
	 */
	public DemandWorker(LocalDemandStore poLocalDemandStore, LocalGEERPool poLocalGEERPool)
	{
		this.oLocalDemandStore = poLocalDemandStore;
		this.oLocalGEERPool = poLocalGEERPool;
	}


	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandWorker.IDemandWorker#startWorker()
	 */
	public void startWorker()
	{
		this.bIsWorking = true;
		
		IDemand oDemand; 
		IDemand oResult;

		while(this.bIsWorking)
		{
			try 
			{
				printOut("");
				oDemand = this.oTA.getDemand();
				printOut("Demand received: name: " + oDemand.getSignature());
				
				if(this.oLocalDemandStore.contains(oDemand.getSignature()))
				{
					oResult = this.oLocalDemandStore.get(oDemand.getSignature());
				}
				else
				{
					oResult = oDemand.execute();
					printOut("Demand computed");
					this.oLocalDemandStore.put(oDemand.getSignature(), oResult);
				}
				
				printOut("The computed result value: " + oResult.getResult());
				this.oTA.setResult(oResult);
				printOut("Result dispatched");
			} 
			catch(DMSException e) 
			{
				e.printStackTrace(System.err);
				break;
			} 
		}
	}
	
	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandWorker.IDemandWorker#setTransportAgent(gipsy.GEE.IDP.ITransportAgent)
	 */
	public void setTransportAgent(ITransportAgent poTA)
	{
		this.oTA = poTA;
	}
	
	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandWorker.IDemandWorker#setTransportAgent(gipsy.GEE.multitier.EDMFImplementation)
	 */
	public void setTransportAgent(EDMFImplementation poDMFImp) 
	{
		try
		{
			switch(poDMFImp)
			{
				case JINI:
				{
					this.oTA = new JINITA();
					break;
				}
				case JMS:
				{
					this.oTA = new JMSTransportAgent();
					break;
				}	
				default:
				{
					this.oTA = null;
					throw new GIPSYRuntimeException("Unknown DMF Implementation Instance Type: " + poDMFImp);
				}
				
			}
		} 
		catch(Exception e) 
		{
			e.printStackTrace(System.err);
		} 
	}
	
	/**
	 * @return
	 */
	public static ITransportAgent getTransportAgent()
	{
		ITransportAgent oJTA = null;

		try 
		{
			oJTA = new JINITA();
//			oJTA = new JMSTransportAgent();
		} 
		catch(Exception e) 
		{
			e.printStackTrace(System.err);
		}

		return oJTA;
	}
	
    /**
     * XXX: move to gipsy.util.PrintUtils
     * @param pstrMsg
     */
    public static void printOut(String pstrMsg)
    {
        System.out.println(pstrMsg);
    }

	@Override
	public void stopWorker() 
	{
		this.bIsWorking = false;
	}

	/**
	 * @param poTAExceptionHandler
	 */
	public void setTAExceptionHandler(TAExceptionHandler poTAExceptionHandler)
	{
		this.oTAExceptionHandler = poTAExceptionHandler;
	}
	
	@Override
	public void run()
	{
		this.bIsWorking = true;
		
		IDemand oDemand; 
		IDemand oResult;
		
		//printOut("");
		TimeLine oTimeLine = new TimeLine();
		
		String strBeginReceive = this.strWorkerID + " BRecv";
		String strEndReceive = this.strWorkerID + " ARecv";
		String strBeginSend = this.strWorkerID + " BSend";
		String strEndSend = this.strWorkerID + " ALastSend";
		
		while(this.bIsWorking)
		{
			try 
			{
				oTimeLine.addTimeLine(strBeginReceive);
				oDemand = (IDemand)this.oTA.getDemand(DemandSignature.DWT);
				//printOut("Demand received: name: " + oDemand.getSignature());
				oTimeLine.addTimeLine(strEndReceive);
				
				//if (we have the resource for process procedural demand)
				oResult = oDemand.execute();
				//printOut("Demand computed");
				//this.oLocalDemandStore.put(oDemand.getSignature(), oResult);
				oResult.addTimeLine(oTimeLine);
				//printOut("The computed result value: " + oResult.getResult());
				
				oTimeLine = new TimeLine();
				oResult.addTimeLine(strBeginSend);
				this.oTA.setResult(oResult);
				oTimeLine.addTimeLine(strEndSend);
				//printOut("Result dispatched");
				//System.out.println(this.strWorkerID + ": " + oDemand.getSignature() + " computed!");
			} 
			catch(DMSException oException) 
			{
				if(this.oTAExceptionHandler != null)
				{
					try 
					{
						this.oTA = this.oTAExceptionHandler.fixTA(this.oTA, oException);
					} 
					catch(InterruptedException oInterruptedException) 
					{
						oInterruptedException.printStackTrace(System.err);
					}
				}
			} 
		}
	}
}

// EOF
