package gipsy.GIPC.imperative.CommunicationProcedureGenerator;

import gipsy.lang.GIPSYType;


/**
 * <p>Stub implementation of a CP for CORBA.</p>
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @version $Revision: 1.1 $
 */
public class CORBACommunicationProcedure
extends CommunicationProcedure
{
	/**
	 * 
	 */
	public CORBACommunicationProcedure()
	{
		super();
	}

	/**
	 * @param poReturnType
	 */
	public CORBACommunicationProcedure(GIPSYType poReturnType)
	{
		super(poReturnType);
	}

	/**
	 * @param poReturnType
	 * @param paoParamTypes
	 */
	public CORBACommunicationProcedure(GIPSYType poReturnType, GIPSYType[] paoParamTypes)
	{
		super(poReturnType, paoParamTypes);
	}

	/**
	 * @param paoParamTypes
	 */
	public CORBACommunicationProcedure(GIPSYType[] paoParamTypes)
	{
		super(paoParamTypes);
	}
}

// EOF
