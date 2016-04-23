package gipsy.GEE.IDP.demands;

import gipsy.interfaces.ISequentialThread;
import gipsy.lang.GIPSYContext;

import java.io.Serializable;
import java.util.Date;


/**
 * This is the base interface for the GIPSY demand. 
 * 
 * @author Emil Vassev
 * @author Serguei Mokhov
 * @since 1.0.0
 * @version 2.0.0, $Id: IDemand.java,v 1.18 2010/09/09 18:21:06 mokhov Exp $
 */
public interface IDemand
extends ISequentialThread, Cloneable
{
	/*
	 * Context
	 */

	/**
	 * Allows querying for the demands context.
	 * @return returns the value context field.
	 */
	public GIPSYContext getContext();

	/**
	 * Allows setting the context.
	 * @param poContext the new value of context to set.
	 */
	public void setContext(GIPSYContext poContext);

	/*
	 * Identification
	 */

	/**
	 * Allows setting the internal signature of this demand.
	 * @param poSignatureID the desired custom signature
	 */
	void setSignature(DemandSignature poSignatureID);

	/**
	 * Allows getting the signature of this demand.
	 * @return the signature of this demand
	 */
	DemandSignature getSignature();
	
	/*
	 * Type
	 */

	/**
	 * Allows setting the type of this demand.
	 * 
	 * @param poType the type to set
	 * 
	 * @see DemandType#INTENSIONAL
	 * @see DemandType#PROCEDURAL
	 * @see DemandType#RESOURCE
	 * @see DemandType#SYSTEM
	 */
	void setType(DemandType poType);

	/**
	 * Allows getting the type of this demand.
	 *
	 * @return the current demand type
	 *
	 * @see DemandType#INTENSIONAL
	 * @see DemandType#PROCEDURAL
	 * @see DemandType#RESOURCE
	 * @see DemandType#SYSTEM
	 */
	DemandType getType();
	
	/*
	 * State
	 */

	/**
	 * Allows setting the state of this demand.
	 *
	 * @param poState the desired state for the demand to have
	 * 
	 * @see DemandState#PENDING
	 * @see DemandState#COMPUTED
	 * @see DemandState#INPROCESS
	 */
	void setState(DemandState poState);

	/**
	 * Allows getting the state of this demand.
	 *
	 * @return the current state the demand is in
	 *
	 * @see DemandState#PENDING
	 * @see DemandState#COMPUTED
	 * @see DemandState#INPROCESS
	 */
	DemandState getState();
	
	/*
	 * Timelines
	 */

	/**
	 * Allows getting a vector time for a named tier responsible for this demand.
	 * XXX: should it become TierSignature
	 * @param pstrTierID the ID of the processing tier
	 * @return array of Dates (timestamps) corresponding to the tier
	 * @see Date
	 */
	Date[] getTimeLine(String pstrTierID);
	
	/**
	 * Allows getting a timeline as a string.
	 * @return the String timeline
	 */
	String timeLineToString();
	
	/**
	 * Allows getting a timeline.
	 * @return the timeline
	 */
	TimeLine getTimeLine();

	/**
	 * Accumulates timeline points per tier ID for this demand.
	 * @param pstrTierID the tier's ID
	 */
	void addTimeLine(String pstrTierID);

	/**
	 * Add another whole timeline to the timeline of this demand.
	 * @param poTimeLine the timeline to add
	 */
	void addTimeLine(TimeLine poTimeLine);
	
	/*
	 * Reference counting
	 */
	
	/**
	 * Reports how many times this demand was accessed.
	 * @return reference count
	 */
	long getAccessNumber();
	
	/**
	 * Allows setting the access count to a particular value
	 * @param plAccessNumber the value to set
	 */
	void setAccessNumber(long plAccessNumber);
	
	/**
	 * Increments access count by one. 
	 */
	void addAccess();
	
	/**
	 * The size of the demand.
	 * XXX: what's that??
	 * @return the size
	 */
	double getSize();

	/*
	 * Evaluation
	 */
	
	/**
	 * Evaluate a sequential thread.
	 * @return the result encapsulated into a demand object
	 */
	IDemand execute();

	// XXX; should the return value be of type IDemand?
	// XXX: it appears that Object is appropriate as theoretically
	// the result can be of any Java data type, built-in or custom,
	// including Integer, String, Method, GIPSYType, etc. This is unless
	// we provide for an API to wrap any result type payload to
	// be encapsulated into an IDemand-implementing object.
	//Object getResult();
	Serializable getResult();
	//IDemand getResult();

	// XXX: should pstrDirName be of type DemandSignature?
	// XXX: the original pstrDirName appears to be for saving stuff into a directory
	//void storeResult(String pstrDirName);
	//DemandSignature storeResult(Object poResult);
	DemandSignature storeResult(Serializable poResult);
}

// EOF
