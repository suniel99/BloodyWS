package gipsy.tests.GEE.IDP.DemandGenerator.jini.rmi;

import marf.util.BaseThread;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcher;
import gipsy.GEE.IDP.demands.IDemand;


/**
 * TODO: document.
 *
 * @author Yi Ji
 * @author Serguei Mokhov
 * @since August 2009
 * @version $Id: DemandDispatcherClientTest.java,v 1.7 2009/08/25 02:45:12 ji_yi Exp $
 */
public class DemandDispatcherClientTest
extends BaseThread
{
	private IDemand oDemand = null;
	private IDemand oResult = null;
	private DemandDispatcher oDispatcher = null;
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		try
		{
			System.out.println("Sender: Sending demand " + this.oDemand.getSignature());
			oResult = this.oDispatcher.getValue(oDemand);
			System.out.println("Sender: Result received " + this.oResult.getSignature());
		}
		catch (Exception e)
		{
			e.printStackTrace(System.err);
		}
	}

	/**
	 * @param poDemand
	 * @param poDispatcher
	 */
	public DemandDispatcherClientTest(IDemand poDemand, DemandDispatcher poDispatcher)
	{
		super();
		this.oDemand = poDemand;
		this.oDispatcher = poDispatcher;
	}

	/**
	 * @return the oDemand
	 */
	public IDemand getoDemand()
	{
		return this.oDemand;
	}

	/**
	 * @param poDemand the oDemand to set
	 */
	public void setoDemand(IDemand poDemand)
	{
		this.oDemand = poDemand;
	}

	/**
	 * @return the oResult
	 */
	public IDemand getoResult()
	{
		return this.oResult;
	}

	/**
	 * @param poResult the oResult to set
	 */
	public void setoResult(IDemand poResult)
	{
		this.oResult = poResult;
	}

	/**
	 * @return the oDispatcher
	 */
	public DemandDispatcher getoDispatcher()
	{
		return this.oDispatcher;
	}

	/**
	 * @param poDispatcher the oDispatcher to set
	 */
	public void setoDispatcher(DemandDispatcher poDispatcher)
	{
		this.oDispatcher = poDispatcher;
	}
}

// EOF
