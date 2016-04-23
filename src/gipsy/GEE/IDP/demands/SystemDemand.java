package gipsy.GEE.IDP.demands;

import gipsy.util.NotImplementedException;

import java.io.Serializable;


/**
 * A demand related to the system's operation.
 * 
 * @author Bin Han
 * @author Serguei Mokhov
 * @version $Id: SystemDemand.java,v 1.10 2013/01/07 19:16:08 mokhov Exp $
 * @since
 */
public class SystemDemand
extends Demand
{
	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = 300208458779913937L;

	/**
	 * destinationTierId -- tier naming scheme.
	 */
	protected Serializable oDestinationTierID = null;
	
	/**
	 * 0 - neutral; < 0 lower (nicer); > 0 higher. 
	 */
	protected Integer iPriority = 0;
	
	//	systemDemandTypeId

	public enum ESystemDemandType
	{
		/**
		 * 
		 */
		SYSTEM_GET_CONFIGURATION,
		
		/**
		 *
		 */
		SYSTEM_SET_CONFIGURATION,
		
		/**
		 * Uptime, running, load,. etc. 
		 */
		SYSTEM_GET_STATUS,
		
		SYSTEM_START_TIER,
		
		SYSTEM_STOP_TIER
	}

	/**
	 *
	 */
	protected ESystemDemandType eSystemDemandTypeId; 
	
	//	object params[]
	protected Serializable[] aoParameters = null;
	
	/**
	 * Default system demand type.
	 * @see DemandType#SYSTEM
	 */
	public SystemDemand()
	{
		super();
		this.oType = DemandType.SYSTEM;
		
		this.eSystemDemandTypeId = ESystemDemandType.SYSTEM_GET_STATUS;
	}
	
	@Override
	public IDemand execute()
	{
		throw new NotImplementedException();
	}

	@Override
	public double getSize()
	{
		throw new NotImplementedException();
	}

	/**
	 * Allows querying for TODO.
	 * @return returns the value oDestinationTierID field.
	 */
	public Serializable getDestinationTierID()
	{
		return this.oDestinationTierID;
	}

	/**
	 * Allows setting TODO.
	 * @param poDestinationTierID the new value of oDestinationTierID to set.
	 */
	public void setDestinationTierID(Serializable poDestinationTierID)
	{
		this.oDestinationTierID = poDestinationTierID;
	}

	/**
	 * Allows querying for TODO.
	 * @return returns the value eSystemDemandTypeId field.
	 */
	public ESystemDemandType getSystemDemandTypeId()
	{
		return this.eSystemDemandTypeId;
	}

	/**
	 * Allows setting TODO.
	 * @param peSystemDemandTypeId the new value of eSystemDemandTypeId to set.
	 */
	public void setSystemDemandTypeId(ESystemDemandType peSystemDemandTypeId)
	{
		this.eSystemDemandTypeId = peSystemDemandTypeId;
	}

	/**
	 * Allows querying for TODO.
	 * @return returns the value aoParameters field.
	 */
	public Serializable[] getParameters()
	{
		return this.aoParameters;
	}

	/**
	 * Allows setting TODO.
	 * @param paoParameters the new value of aoParameters to set.
	 */
	public void setParameters(Serializable[] paoParameters)
	{
		this.aoParameters = paoParameters;
	}

	/**
	 * Allows querying for TODO.
	 * @return returns the value iPriority field.
	 */
	public Integer getPriority()
	{
		return this.iPriority;
	}

	/**
	 * Allows setting TODO.
	 * @param piPriority the new value of iPriority to set.
	 */
	public void setPriority(Integer piPriority)
	{
		this.iPriority = piPriority;
	}
}

// EOF
