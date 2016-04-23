package gipsy.interfaces;
import gipsy.lang.GIPSYType;
import java.io.Serializable;

/**
 * <p>CommunicationProcedure represents the means of delivery of sequential threads.</p>
 * $Id: ICommunicationProcedure.java,v 1.11 2005/10/11 08:34:11 mokhov Exp $
 * @version $Revision: 1.11 $
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @since Inception
 * @see gipsy.interfaces.SequentialThread
 */
public interface ICommunicationProcedure
extends Serializable
{
	public GIPSYType getReturnType();
	public GIPSYType getParamType(final int piParamNumber);
	public GIPSYType[] getParamTypes();
	public void setReturnType(GIPSYType poType);
	public void setParamType(final int piParamNumber, GIPSYType poType);
	public void setParamTypes(GIPSYType[] paoTypes);
	public GIPSYType getParamType(String pstrLexeme);
	public GIPSYType getParamType(String pstrLexeme, String pstrID);
	public int getParamListSize();
	/**
	 * Perform any initialization actions required.
	 * @return status object of the result of send operation.
	 * @throws CommunicationException in case of error
	 */
	public CommunicationStatus init()
	throws CommunicationException;
	/**
	 * Open a connection; whatever that means for a given protocol.
	 * @return status object of the result of send operation.
	 * @throws CommunicationException in case of error
	 */
	public CommunicationStatus open()
	throws CommunicationException;
	/**
	 * Close a connection; whatever that means for a given protocol.
	 * @return status object of the result of send operation.
	 * @throws CommunicationException in case of error
	 */
	public CommunicationStatus close()
	throws CommunicationException;
	/**
	 * Defines the means of sending data. Should be overridden by
	 * a concrete implementation, such as JINI, COM, CORBA, etc.
	 * @return status object of the result of send operation.
	 * @throws CommunicationException in case of error
	 */
	public CommunicationStatus send()
	throws CommunicationException;
	/**
	 * Defines the means of receiving data. Should be overridden by
	 * a concrete implementation, such as JINI, COM, CORBA, etc.
	 * @return status object of the result of receive operation.
	 * @throws CommunicationException in case of error
	 */
	public CommunicationStatus receive()
	throws CommunicationException;
}
