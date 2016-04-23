package gipsy.GEE.IDP.DemandGenerator.jms;

import gipsy.GEE.IDP.demands.Demand;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.DemandType;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.TimeLine;
import gipsy.util.NetUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;


/**
 * Supposed to be a demand?
 * 
 * @author Amir Pourteymour
 * @author Yi Ji
 * @version $Id: DemandController.java,v 1.10 2009/08/22 21:31:04 ji_yi Exp $
 */
// Changed: Original Name DemandController
public class DemandController
//implements IDemand
extends Demand
{
	private static final int DEFAULT_NUMBER_OF_DIGITS = 10;

	private static final long serialVersionUID = 22;

	/////WorkResult
	public String strResultName;
	public String strResult;
	/////WorkDemand
	public String strCreatorAddress;
	public int iNumberOfDigits; 
	
	public DemandController()
	{
		try
		{
			this.oSignature = new DemandSignature("No Demand");
			this.strCreatorAddress = NetUtils.getLocalIPAddress("DemandController.DemandController(): ");
			System.out.println("The demand does not have any assigned name.");
		}
		catch (Exception e)
		{
			System.out.println("DemandController Error " + this.oSignature + ": " + e.getCause().toString() );
		}
	}

	public DemandController(String pstrName) 
	{
		try
		{
			if(!"null".equals(pstrName))
			{
				this.oSignature = new DemandSignature(pstrName);
				this.strCreatorAddress = NetUtils.getLocalIPAddress("DemandController.DemandController(String): ");
				this.iNumberOfDigits = DEFAULT_NUMBER_OF_DIGITS;
			}
			else //if (pstrName=="null")
			{
				System.out.println("**** Demand in not generated.****\n");
			}
		}
		catch (Exception e)
		{
			System.out.println("DemandController Error " + this.oSignature + ": " + e.getCause().toString() );
		}
	}

	/**
	public WorkResult Execute()
	 {
          return new WorkResult(sDName, GetLocalIPAddress());
     }
     * Used to be called DemandComputer()
     */
	// Changed: about to be @deprecated
	public void computeDemand(String pstrName, String pstrInCreatorAddress)
	{
		int iRequestedNumberOfDigits = this.iNumberOfDigits;   
		Task oTask = new Task();

		synchronized(this)
		{
			this.strResultName = "Result_" + pstrName.replaceFirst("Demand_","");
			//Calculating the PI number and sending the result to the variable "sResult".
			this.strResult = pstrInCreatorAddress + " , Pi calculation: " + oTask.execute(iRequestedNumberOfDigits);
		}
	}

	public String getResult()
    {
		return this.strResult;
    }

	/**
	 * XXX: is it really needed?
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void addAccess() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTimeLine(String pstrTierID)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void addTimeLine(TimeLine poTimeLine)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public IDemand execute()
	{
		int iRequestedNumberOfDigits = this.iNumberOfDigits;   
		Task oTask = new Task();

		synchronized(this)
		{
			this.strResultName = "Result_" + this.oSignature.toString().replaceFirst("Demand_","");
			//Calculating the PI number and sending the result to the variable "sResult".
			this.strResult =
				NetUtils.getLocalIPAddress("DemandController.execute(): ")
				+ " , Pi calculation: " + oTask.execute(iRequestedNumberOfDigits);
		}

		return this;
	}

	@Override
	public long getAccessNumber()
	{
		return 0;
	}


	@Override
	public double getSize() 
	{
		return 0;
	}

	@Override
	public DemandState getState() 
	{
		return null;
	}

	@Override
	public Date[] getTimeLine(String pstrTierID) 
	{
		return null;
	}

	@Override
	public void setAccessNumber(long plAccessNumber) 
	{
		// TODO Auto-generated method stub	
	}
/*
	@Override
	public void storeResult(String pstrDirName) 
	{
		// TODO Auto-generated method stub
		
	}
*/
	@Override
	public String timeLineToString() 
	{
		return null;
	}	
}

// EOF
