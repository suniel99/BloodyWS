package gipsy.tests.GEE.simulator.demands;

import gipsy.GEE.IDP.demands.Demand;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.ProceduralDemand;
import gipsy.GEE.IDP.demands.TimeLine;
import gipsy.util.NetUtils;


/**
 * Class WorkDemandPi.
 *
 * @author Emil Vassev
 * @author Serguei Mokhov
 * @since 1.0.0
 * @version 2.0.0, $Id: WorkDemandPi.java,v 1.16 2011/01/26 05:11:37 ji_yi Exp $
 */
public class WorkDemandPi
extends ProceduralDemand
{
	/*
	 * ---------
	 * Constants
	 * ---------
	 */
	
	private static final long serialVersionUID = 1L;
	private static volatile long slSerialID = 0;
	private static final Object soSerialIDLock = new Object();
	
	/*
	 * ------------
	 * Data Members
	 * ------------
	 */

	/**
	 * XXX.
	 */
	public String strCreatorAddress;
	
//	***************************************************************
//	**** object lifetime
		
	/**
	 * Constructor by default.
	 * XXX.
	 */
	public WorkDemandPi() 
	{
		synchronized(soSerialIDLock)
		{
			this.oSignature = new DemandSignature("Pi_" + slSerialID);
			slSerialID++;
			//this(oSignature);
		}
		
	}

	/**
	 * XXX.
	 * @param poSignature XXX
	 */
	public WorkDemandPi(DemandSignature poSignature)
	{
		super(poSignature);
		
		//addTimeLine("DGT");
		
		//this.strCreatorAddress = NetUtils.getLocalIPAddress();
	}

//	***************************************************************
//	**** local methods

//	***************************************************************
//	**** IDemand methods


	@Override
    public double getSize()
    {
		return 1;
    }

	@Override
	public IDemand execute()
	{
		Demand oResult = new WorkResultPi(getSignature(), NetUtils.getLocalIPAddress());
		//oResult.setType(getType());
		//oResult.setState(DemandState.COMPUTED);
		   
		//oResult.setAccessNumber(getAccessNumber());
		//oResult.addAccess();
		   
		//oResult.addTimeLine(this.oTimeLine);
		//oResult.addTimeLine("DWT");

		//this.oWorkResult = oResult.getResult();
		
		return oResult;
	}
}

// EOF
