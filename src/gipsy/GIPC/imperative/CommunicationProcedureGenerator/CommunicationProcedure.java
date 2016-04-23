package gipsy.GIPC.imperative.CommunicationProcedureGenerator;

import gipsy.interfaces.CommunicationException;
import gipsy.interfaces.CommunicationStatus;
import gipsy.interfaces.ICommunicationProcedure;
import gipsy.lang.GIPSYType;
import gipsy.lang.GIPSYVoid;
import gipsy.util.NotImplementedException;


/**
 * <p>Generic implementation of a CP .</p>
 *
 * $Id: CommunicationProcedure.java,v 1.1 2005/09/11 00:07:56 mokhov Exp $
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @version $Revision: 1.1 $
 */
public abstract class CommunicationProcedure
implements ICommunicationProcedure
{
	protected GIPSYType oReturnType = null;
	protected GIPSYType[] aoParamTypes = null;
	
	public CommunicationProcedure()
	{
		this(new GIPSYVoid());
	}
	
	public CommunicationProcedure(GIPSYType poReturnType)
	{
		this(poReturnType, new GIPSYType[] {});
	}

	public CommunicationProcedure(GIPSYType[] paoParamTypes)
	{
		this(new GIPSYVoid(), paoParamTypes);
	}

	public CommunicationProcedure(GIPSYType poReturnType, GIPSYType[] paoParamTypes)
	{
		setReturnType(poReturnType);
		setParamTypes(paoParamTypes);
	}

	public GIPSYType getReturnType()
	{
		return this.oReturnType;
	}
	
	public GIPSYType getParamType(final int piParamNumber)
	{
		return this.aoParamTypes[piParamNumber];
	}

	public GIPSYType[] getParamTypes()
	{
		return this.aoParamTypes;
	}

	public void setReturnType(GIPSYType poType)
	{
		this.oReturnType = poType;
	}
	
	public void setParamType(final int piParamNumber, GIPSYType poType)
	{
		this.aoParamTypes[piParamNumber] = poType;
	}

	public void setParamTypes(GIPSYType[] paoTypes)
	{
		this.aoParamTypes = paoTypes;
	}

	public GIPSYType getParamType(String pstrLexeme)
	{
		for(int i = 0; i < this.aoParamTypes.length; i++)
		{
			if(this.aoParamTypes[i].getLexeme().equals(pstrLexeme))
			{
				return this.aoParamTypes[i];
			}
		}
		
		return null;
	}

	public GIPSYType getParamType(String pstrLexeme, String pstrID)
	{
		for(int i = 0; i < this.aoParamTypes.length; i++)
		{
			if
			(
				this.aoParamTypes[i].getLexeme().equals(pstrLexeme)
				&& this.aoParamTypes[i].getID().equals(pstrID)
			)
			{
				return this.aoParamTypes[i];
			}
		}
		
		return null;
	}

	public int getParamListSize()
	{
		return this.aoParamTypes == null ? 0 : this.aoParamTypes.length; 
	}

	/**
	 * Perform any initialization actions required.
	 * @return status object of the result of send operation.
	 * @throws CommunicationException in case of error
	 */
	public CommunicationStatus init()
	throws CommunicationException
	{
		throw new NotImplementedException(this, "generateCommuncationProcedures()");
	}

	/**
	 * Open a connection; whatever that means for a given protocol.
	 * @return status object of the result of send operation.
	 * @throws CommunicationException in case of error
	 */
	public CommunicationStatus open()
	throws CommunicationException
	{
		throw new NotImplementedException(this, "generateCommuncationProcedures()");
	}

	/**
	 * Close a connection; whatever that means for a given protocol.
	 * @return status object of the result of send operation.
	 * @throws CommunicationException in case of error
	 */
	public CommunicationStatus close()
	throws CommunicationException
	{
		throw new NotImplementedException(this, "close()");
	}

	/**
	 * Defines the means of sending data. Should be overridden by
	 * a concrete implementation, such as JINI, COM, CORBA, etc.
	 * @return status object of the result of send operation.
	 * @throws CommunicationException in case of error
	 */
	public CommunicationStatus send()
	throws CommunicationException
	{
		throw new NotImplementedException(this, "send()");
	}

	/**
	 * Defines the means of receiving data. Should be overridden by
	 * a concrete implementation, such as JINI, COM, CORBA, etc.
	 * @return status object of the result of receive operation.
	 * @throws CommunicationException in case of error
	 */
	public CommunicationStatus receive()
	throws CommunicationException
	{
		throw new NotImplementedException(this, "receive()");
	}
}

// EOF
