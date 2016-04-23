package gipsy.GIPC.imperative.CommunicationProcedureGenerator;

import gipsy.interfaces.CommunicationException;
import gipsy.interfaces.CommunicationStatus;
import gipsy.lang.GIPSYType;


/**
 * <p>Stub implementation of a CP for local TLP.</p>
 *
 * $Id: NullCommunicationProcedure.java,v 1.9 2005/09/11 00:07:56 mokhov Exp $
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @version $Revision: 1.9 $
 */
public class NullCommunicationProcedure
extends CommunicationProcedure
{
	/**
	 * 
	 */
	public NullCommunicationProcedure()
	{
		super();
	}

	/**
	 * @param poReturnType
	 */
	public NullCommunicationProcedure(GIPSYType poReturnType)
	{
		super(poReturnType);
	}

	/**
	 * @param poReturnType
	 * @param paoParamTypes
	 */
	public NullCommunicationProcedure(GIPSYType poReturnType, GIPSYType[] paoParamTypes)
	{
		super(poReturnType, paoParamTypes);
	}

	/**
	 * @param paoParamTypes
	 */
	public NullCommunicationProcedure(GIPSYType[] paoParamTypes)
	{
		super(paoParamTypes);
	}

	/**
	 * Perform any initialization actions required.
	 * @return status object of the result of send operation.
	 * @throws CommunicationException in case of error
	 */
	public CommunicationStatus init()
	throws CommunicationException
	{
		return null;
	}

	/**
	 * Open a connection; whatever that means for a given protocol.
	 * @return status object of the result of send operation.
	 * @throws CommunicationException in case of error
	 */
	public CommunicationStatus open()
	throws CommunicationException
	{
		return null;
	}

	/**
	 * Close a connection; whatever that means for a given protocol.
	 * @return status object of the result of send operation.
	 * @throws CommunicationException in case of error
	 */
	public CommunicationStatus close()
	throws CommunicationException
	{
		return null;
	}

	/**
	 * Defines the means of sending data. Should be overridden by
	 * a concrete implementation, such as Jini, COM, CORBA, etc.
	 * @return status object of the result of send operation.
	 * @throws CommunicationException in case of error
	 */
	public CommunicationStatus send()
	throws CommunicationException
	{
		return null;
	}

	/**
	 * Defines the means of receiving data. Should be overridden by
	 * a concrete implementation, such as Jini, COM, CORBA, etc.
	 * @return status object of the result of receive operation.
	 * @throws CommunicationException in case of error
	 */
	public CommunicationStatus receive()
	throws CommunicationException
	{
		return null;
	}
}

// EOF
