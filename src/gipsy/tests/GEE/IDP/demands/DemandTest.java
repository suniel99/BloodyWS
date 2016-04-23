package gipsy.tests.GEE.IDP.demands;

import gipsy.GEE.IDP.demands.Demand;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.DemandType;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.TimeLine;
import gipsy.lang.GIPSYContext;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * The demand class used for testing.
 * @author Yi Ji
 * @author Serguei Mokhov
 * @version $Id: DemandTest.java,v 1.8 2009/09/30 10:47:20 mokhov Exp $
 * @since
 */
public class DemandTest
//implements IDemand
extends Demand
{
	/**
	 * The context used for generate demand signature.
	 */
	private GIPSYContext oContext = null;
	private String strContext = null;
	
	/**
	 * Construct the demand using the context
	 * @param poContext the context of the demand
	 */
	public DemandTest(GIPSYContext poContext)
	{
		super();
		this.oContext = poContext;
	}

	public DemandTest(Object poContext)
	{
		this.strContext = poContext.toString();
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#addAccess()
	 */
	@Override
	public void addAccess() 
	{
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#addTimeLine(java.lang.String)
	 */
	@Override
	public void addTimeLine(String pstrTierID) 
	{
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#addTimeLine(gipsy.GEE.IDP.TimeLine)
	 */
	@Override
	public void addTimeLine(TimeLine poTimeLine) 
	{
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#execute()
	 */
	@Override
	public IDemand execute() 
	{
		return this;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#getAccessNumber()
	 */
	@Override
	public long getAccessNumber() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#getResult()
	 */
	@Override
	public Serializable getResult() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DemandSignature getSignature()
	{
		String strSignature = "";
		strSignature = strSignature + this.getClass().getSimpleName();
		strSignature = strSignature + this.strContext;
		return new DemandSignature(strSignature);
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#getSize()
	 */
	@Override
	public double getSize() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#getState()
	 */
	@Override
	public DemandState getState() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#getTimeLine(java.lang.String)
	 */
	@Override
	public Date[] getTimeLine(String pstrTierID) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#getTimeLine()
	 */
	@Override
	public TimeLine getTimeLine() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#getType()
	 */
	@Override
	public DemandType getType() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#setAccessNumber(long)
	 */
	@Override
	public void setAccessNumber(long plAccessNumber) 
	{
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#setSignature(java.lang.String)
	 */
	@Override
	public void setSignature(DemandSignature poSignatureID) 
	{
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#setState(gipsy.GEE.IDP.DemandState)
	 */
	@Override
	public void setState(DemandState poState) 
	{
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#setType(gipsy.GEE.IDP.DemandType)
	 */
	@Override
	public void setType(DemandType poType) 
	{
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#timeLineToString()
	 */
	@Override
	public String timeLineToString() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone()
	{
		return super.clone();
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.demands.IDemand#storeResult(java.lang.Object)
	 */
	@Override
	public DemandSignature storeResult(Serializable poResult)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see gipsy.interfaces.ISequentialThread#getWorkResult()
	 */
	@Override
	public Serializable getWorkResult()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see gipsy.interfaces.ISequentialThread#setMethod(java.lang.reflect.Method)
	 */
	@Override
	public void setMethod(Method poSTMethod)
	{
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see gipsy.interfaces.ISequentialThread#work()
	 */
	@Override
	public Serializable work()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
	}
}

// EOF
