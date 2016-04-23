package gipsy.GIPC.imperative.CommunicationProcedureGenerator;

import gipsy.lang.GIPSYType;


/**
 * <p>Stub implementation of a CP for RMI.</p>
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @version $Revision: 1.5 $
 */
public class RMICommunicationProcedure
extends CommunicationProcedure
{
	/**
	 * 
	 */
	public RMICommunicationProcedure()
	{
		super();
	}

	/**
	 * @param poReturnType
	 */
	public RMICommunicationProcedure(GIPSYType poReturnType)
	{
		super(poReturnType);
	}

	/**
	 * @param poReturnType
	 * @param paoParamTypes
	 */
	public RMICommunicationProcedure(GIPSYType poReturnType, GIPSYType[] paoParamTypes)
	{
		super(poReturnType, paoParamTypes);
	}

	/**
	 * @param paoParamTypes
	 */
	public RMICommunicationProcedure(GIPSYType[] paoParamTypes)
	{
		super(paoParamTypes);
	}
}

// EOF
