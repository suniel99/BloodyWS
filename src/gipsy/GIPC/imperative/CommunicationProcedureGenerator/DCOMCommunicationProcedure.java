package gipsy.GIPC.imperative.CommunicationProcedureGenerator;

import gipsy.lang.GIPSYType;


/**
 * <p>Stub implementation of a CP for DCOM+.</p>
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @version $Revision: 1.1 $
 */
public class DCOMCommunicationProcedure
extends CommunicationProcedure
{
	/**
	 * 
	 */
	public DCOMCommunicationProcedure()
	{
		super();
	}

	/**
	 * @param poReturnType
	 */
	public DCOMCommunicationProcedure(GIPSYType poReturnType)
	{
		super(poReturnType);
	}

	/**
	 * @param poReturnType
	 * @param paoParamTypes
	 */
	public DCOMCommunicationProcedure(GIPSYType poReturnType, GIPSYType[] paoParamTypes)
	{
		super(poReturnType, paoParamTypes);
	}

	/**
	 * @param paoParamTypes
	 */
	public DCOMCommunicationProcedure(GIPSYType[] paoParamTypes)
	{
		super(paoParamTypes);
	}
}

// EOF
