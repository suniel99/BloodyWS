package gipsy.GIPC.imperative.CommunicationProcedureGenerator;

import gipsy.lang.GIPSYType;


/**
 * <p>Stub implementation of a CP for Jini.</p>
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @version $Revision: 1.1 $
 */
public class JiniCommunicationProcedure
extends CommunicationProcedure
{
	/**
	 * 
	 */
	public JiniCommunicationProcedure()
	{
		super();
	}

	/**
	 * @param poReturnType
	 */
	public JiniCommunicationProcedure(GIPSYType poReturnType)
	{
		super(poReturnType);
	}

	/**
	 * @param poReturnType
	 * @param paoParamTypes
	 */
	public JiniCommunicationProcedure(GIPSYType poReturnType, GIPSYType[] paoParamTypes)
	{
		super(poReturnType, paoParamTypes);
	}

	/**
	 * @param paoParamTypes
	 */
	public JiniCommunicationProcedure(GIPSYType[] paoParamTypes)
	{
		super(paoParamTypes);
	}
}

// EOF
