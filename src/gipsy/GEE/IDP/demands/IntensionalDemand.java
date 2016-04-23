package gipsy.GEE.IDP.demands;

import gipsy.lang.GIPSYContext;
import gipsy.lang.GIPSYIdentifier;


/**
 * A demand for the evaluation of a Lucid identifier, given a certain context.
 * 
 * Intensional demands are created and further processed by
 * the Intensional Demand Processor.
 *  
 * @author Bin Han
 * @author Serguei Mokhov
 * @version $Id: IntensionalDemand.java,v 1.12 2010/12/06 13:38:39 mokhov Exp $
 * @since
 */
public class IntensionalDemand
extends Demand
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3278299569506091625L;

	//	GEERid -- signature

	/**
	 * programID 
	 */
	protected GIPSYIdentifier oProgramID = null;

	/**
	 * Default type of the demand is intensional.
	 * @see DemandType#INTENSIONAL 
	 */
	public IntensionalDemand()
	{
		super();
		this.oType = DemandType.INTENSIONAL;
	}

	/**
	 * Constructor with a specified context.
	 * @param poContext the context
	 */
	public IntensionalDemand(GIPSYContext poContext)
	{
		this();
		this.oContextId = poContext;
	}
	
	/*
	 * Getters/setters
	 */

	/**
	 * Allows querying for TODO.
	 * @return returns the value oProgramID field.
	 */
	public GIPSYIdentifier getProgramID()
	{
		return this.oProgramID;
	}

	/**
	 * Allows setting TODO.
	 * @param poProgramID the new value of oProgramID to set.
	 */
	public void setProgramID(GIPSYIdentifier poProgramID)
	{
		this.oProgramID = poProgramID;
	}
	
	

	@Override
	public IDemand execute()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getSize()
	{
		// TODO: can technically be a rank for intensional demands
		// TODO Auto-generated method stub
		return 0;
	}
}

// EOF
