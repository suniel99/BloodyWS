package gipsy.tests.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.demands.IDemand;

/**
 * A demand worker thread.
 * 
 * @author Yi Ji
 * @version $Id: DemandWorker.java,v 1.7 2010/08/12 18:05:16 ji_yi Exp $
 */
public class DemandWorker extends Thread {
	IDemand oDemand = null;
	ITransportAgent oJTA = null;
	IDemand oResult = null;
	
	public static volatile int siErrorCount; // Incremented whenever a thread dies.
	public int iID;	// The user-assigned ID of this thread.
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() 
	{
		try 
		{
			System.out.println("Worker: " + iID + " Waiting for demand");
			oDemand = (IDemand) this.oJTA.getDemand();
			System.out.println("Worker: " + iID + " Demand received " + oDemand.getSignature());
			oResult = oDemand.execute();
			System.out.println("Worker: " + iID + " Result generated " + oResult.getSignature());
			this.oJTA.setResult(oResult);
			System.out.println("Worker: " + iID + " Result put back " + oResult.getSignature());
		} 
		catch (Exception e) 
		{
			siErrorCount++;
			System.err.print("Worker " + iID + " report: ");
			e.printStackTrace(System.err);
			System.err.println();
		}
	}

	/**
	 * @param poJTA
	 */
	public DemandWorker(ITransportAgent poJTA) 
	{
		super();
		this.oJTA = poJTA;
	}

	

	/**
	 * @return the oJTA
	 */
	public ITransportAgent getoJTA() 
	{
		return oJTA;
	}

	/**
	 * @param oJTA the oJTA to set
	 */
	public void setoJTA(ITransportAgent oJTA) 
	{
		this.oJTA = oJTA;
	}

	/**
	 * @return the oResult
	 */
	public IDemand getoResult() 
	{
		return oResult;
	}

	/**
	 * @param oResult the oResult to set
	 */
	public void setoResult(IDemand oResult)
	{
		this.oResult = oResult;
	}

	/**
	 * @return the oDemand
	 */
	public IDemand getoDemand() 
	{
		return oDemand;
	}

	/**
	 * @param oDemand the oDemand to set
	 */
	public void setoDemand(IDemand oDemand) 
	{
		this.oDemand = oDemand;
	}
	
	
}
