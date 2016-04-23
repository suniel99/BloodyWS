/**
 * 
 */
package gipsy.tests.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.demands.IDemand;

/**
 * A demand sender thread.
 * 
 * @author ji_yi
 * @version $Id: DemandSender.java,v 1.7 2010/08/12 18:05:16 ji_yi Exp $
 */
public class DemandSender extends Thread {

	private IDemand oDemand = null;
	private IDemand oResult = null;
	private ITransportAgent oJTA = null;
	
	public int iID; // User-defined ID
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() 
	{
		try 
		{
			System.out.println("Sender: " + iID + " Sending demand " + oDemand.getSignature());
			this.oJTA.setDemand(oDemand);
			System.out.println("Sender: " + iID + " demand is sent" + oDemand.getSignature());
			oResult = this.oJTA.getResult(oDemand.getSignature());
			System.out.println("Sender: " + iID + " Result received " + oResult.getSignature());
		} 
		catch (DMSException e) 
		{
			System.err.println("Sender " + iID + " report:");
			e.printStackTrace(System.err);
			System.err.println();
		}
	}
	
	/**
	 * @param poDemand
	 * @param poJTA
	 */
	public DemandSender(IDemand poDemand, ITransportAgent poJTA) 
	{
		super();
		this.oDemand = poDemand;
		this.oJTA = poJTA;
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
	
	
	
}
