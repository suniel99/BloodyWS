package gipsy.GEE.IDP.demands;

import gipsy.lang.GIPSYContext;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;

import marf.util.FreeVector;


/**
 * Generic demand class.
 * @author Serguei Mokhov
 * @version $Id: Demand.java,v 1.17 2012/06/17 16:54:39 mokhov Exp $
 * @since
 */
public abstract class Demand
extends FreeVector<Object>
implements IDemand
{
	/*
	 * ------------
	 * Data Members
	 * ------------
	 */

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -8784215623748612330L;

	/**
	 * The context of this demand.
	 */
	protected GIPSYContext oContextId = new GIPSYContext();

	/**
	 * The signature of the demand, supposedly unique per demand
	 * type and structure.
	 * @see DemandSignature
	 */
	protected DemandSignature oSignature = null;
	
	/**
	 * Types are flexible, and currently include intensional, procedural,
	 * system, and resource demand types.
	 * @see DemandType
	 */
	protected DemandType oType = null;

	/**
	 * State refers to primarily being pending or computed, etc.
	 * @see DemandState 
	 */
	protected DemandState oState = null;
	
	/**
	 * Vector time used for distributed processing of the demand.
	 * @see TimeLine 
	 */
	protected TimeLine oTimeLine = null;

	/**
	 * How many times this demand was referenced.
	 * For garbage collection and the like.
	 */
	protected long lAccessCounter = 0;


	// WorkTask

	/**
	 * Name. From WorkTask and WorkResult.
	 * Has to be public for JavaSpaces.
	 * From WorkTask.
	 */
	public String strName = null;
	
	/**
	 * The result of the work.
	 * From WorkTask.
	 */
//	protected IDemand oWorkResult = null;
//	protected Object oResult = null;
//	protected Object oWorkResult = null;
	protected Serializable oWorkResult = null;


	/**
	 * ST Java method.
	 * From WorkTask.
	 */
	protected Method oSTMethod = null;


	/*
	 * ---------------
	 * Object Lifetime
	 * ---------------
	 */
	
	/**
	 * Assumes the default signature 
	 * and a procedural demand type, and as pending.
	 * 
	 * @see DemandSignature#DemandSignature()
	 * @see DemandType#PROCEDURAL
	 * @see DemandState#PENDING
	 * @see TimeLine#TimeLine()
	 */
	public Demand()
	{
		super();
		
		this.oSignature = new DemandSignature();
		this.oType = DemandType.PROCEDURAL;
		this.oState = DemandState.PENDING;
		this.oTimeLine = new TimeLine();

		this.lAccessCounter = 0;
	}

	/**
	 * Came from WorkTask.
	 * @param pstrName the name to set
	 */
	public Demand(String pstrName)
	{
		this();
		this.strName = pstrName;
	}

	/**
	 * @param poSignature the known signature
	 */
	public Demand(DemandSignature poSignature)
	{
		this();
		this.oSignature = poSignature;
	}
	
	/**
	 * Inverse of the demand -- the result.
	 * @param poResult the computed payload
	 */
//	public Demand(Object poResult)
	public Demand(Serializable poResult)
	{
		this();
		this.oWorkResult = poResult;
		this.strName = poResult.toString();
		this.oState = DemandState.COMPUTED;
		
		add(this.oWorkResult);
	}

	/*
	 * -----------
	 * IDemand API
	 * -----------
	 */
	
	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.demands.IDemand#getSignature()
	 */
	@Override
	public DemandSignature getSignature()
	{
		return this.oSignature;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.demands.IDemand#setSignature(gipsy.GEE.IDP.demands.DemandSignature)
	 */
	@Override
	public void setSignature(DemandSignature poSignatureID)
	{
		this.oSignature = poSignatureID;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#SetType(gipsy.GEE.IDP.DemandType)
	 */
	@Override
	public void setType(DemandType poType)
	{
		this.oType = poType;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#GetType()
	 */
	@Override
	public DemandType getType()
	{
		return this.oType;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#SetState(gipsy.GEE.IDP.DemandState)
	 */
	@Override
	public void setState(DemandState poState)
	{
		this.oState = poState;
	}

	/*
	 * @see gipsy.GEE.IDP.IDemand#GetState()
	 */
	@Override
	public DemandState getState()
	{
		return this.oState;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#getTimeLine(java.lang.String)
	 */
	@Override
	public Date[] getTimeLine(String pstrTierID)
	{
		return this.oTimeLine.getTimeLine(pstrTierID);
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#timeLineToString()
	 */
	@Override
	public String timeLineToString()
	{
		return this.oTimeLine.getTimeLine();
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#getTimeLine()
	 */
	@Override
	public TimeLine getTimeLine()
	{
		return this.oTimeLine;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#addTimeLine(java.lang.String)
	 */
	@Override
	public void addTimeLine(String pstrTierID)
	{
		this.oTimeLine.addTimeLine(pstrTierID);
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#addTimeLine(gipsy.GEE.IDP.TimeLine)
	 */
	@Override
	public void addTimeLine(TimeLine poTimeLine)
	{
		//XXX: simulator code had: poTimeLine.addTimeLine(poTimeLine);
		this.oTimeLine.addTimeLine(poTimeLine);
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#getAccessNumber()
	 */
	@Override
	public long getAccessNumber()
	{
		return this.lAccessCounter;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#setAccessNumber(long)
	 */
	@Override
	public void setAccessNumber(long plAccessNumber)
	{
		this.lAccessCounter = plAccessNumber;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.IDemand#addAccess()
	 */
	@Override
	public void addAccess()
	{
		this.lAccessCounter++;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.demands.IDemand#getSize()
	 */
	@Override
	public double getSize()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.demands.IDemand#getResult()
	 */
	@Override
	public Serializable getResult()
	{
		return this.oWorkResult;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.demands.IDemand#storeResult(java.lang.Object)
	 */
	@Override
	public DemandSignature storeResult(Serializable poResult)
	{
		// XXX
		this.oWorkResult = poResult;
		DemandSignature oSignature = this.oSignature;
		
		if(poResult instanceof IDemand)
		{
			oSignature = ((IDemand)this.oWorkResult).getSignature(); 
		}
		
		return oSignature;
	}

	
	/*
	 * ---------------------
	 * ISequentialThread API
	 * ---------------------
	 */
	
	/**
	 * Implementation of ISequentialThread.
	 * @since Serguei.
	 */
//	public WorkResult work()
//	public IDemand work()
	//public Object work()
	public Serializable work()
	{
		//return this.oWorkResult = new WorkResult(this.strName);
		return this.oWorkResult = this.strName;
	}

	/**
	 * Implementation of ISequentialThread/Runnable.
	 * @since Serguei.
	 */
	public void run()
	{
		work();
	}
	
	/* (non-Javadoc)
	 * @see gipsy.interfaces.ISequentialThread#getWorkResult()
	 */
//	public IDemand getWorkResult()
//	public Object getWorkResult()
	public Serializable getWorkResult()
	{
		return this.oWorkResult;
	}
	
	/* (non-Javadoc)
	 * @see gipsy.interfaces.ISequentialThread#setMethod(java.lang.reflect.Method)
	 */
	public void setMethod(Method poMethod)
	{
		this.oSTMethod = poMethod;
	}

	/**
	 * Allows querying for the demands context.
	 * @return returns the value context field.
	 */
	public GIPSYContext getContext()
	{
		return this.oContextId;
	}

	/**
	 * Allows setting the context.
	 * @param poContext the new value of context to set.
	 */
	public void setContext(GIPSYContext poContext)
	{
		this.oContextId = poContext;
	}
	
	/*
	 * ----------
	 * Object API
	 * ----------
	 */

	@Override
	public synchronized boolean equals(Object poDemand)
	{
		if(poDemand != null && poDemand instanceof IDemand)
		{
			IDemand oOtherDemand = (IDemand)poDemand;
			
			if
			(
				oOtherDemand.getSignature().equals(this.oSignature) &&
				oOtherDemand.getContext().equals(this.oContextId) &&
				oOtherDemand.getType().equals(this.oType) &&
				oOtherDemand.getState().equals(this.oState)
			)
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public synchronized String toString()
	{
		String strDemandData
			= ""
			+ "oContextId: " + oContextId + ","
			+ "oSignature: " + oSignature + ","
			+ "oType: " + oType + ","
			+ "oState: " + oState + ","
			+ "oTimeLine: " + oTimeLine + ","
			+ "strName: " + strName + ","
			+ "oWorkResult: " + oWorkResult + ","
			+ "oSTMethod: " + oSTMethod + ","
			;
		
		return strDemandData + "\n" + super.toString();
	}
}

// EOF
