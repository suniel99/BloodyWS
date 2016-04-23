package gipsy.GEE.IDP.demands;

import gipsy.interfaces.GEERSignature;
import gipsy.interfaces.ResourceSignature;


/**
 * A demand for a processing resource.
 * 
 * @author Bin Han
 * @author Serguei Mokhov
 * @version $Id: ResourceDemand.java,v 1.11 2013/01/07 19:16:07 mokhov Exp $
 */
public class ResourceDemand
extends Demand
{
	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = 2649895867888615410L;

	//	resourceTypeId
	
	public enum EResourceType
	{
		/**
		 * Essentially, the code. 
		 */
		RESOURCE_GEER,
		
		/**
		 * Any type of data, e.g. image, text, audio,
		 * whatever. 
		 */
		RESOURCE_DATA
	}
	
	/**
	 * By default we demand for GEERs.
	 */
	//protected EResourceType eResourceTypeId = EResourceType.RESOURCE_GEER; 
	protected EResourceType eResourceTypeId = null; 
	
	//	resourceId
	//protected GEERSignature oResourceID = new GEERSignature();
	protected ResourceSignature oResourceID = null;

	/**
	 * A demand of type RESOURCE.
	 * @see DemandType#RESOURCE
	 */
	public ResourceDemand()
	{
		super();
		this.oType = DemandType.RESOURCE;
		
		this.eResourceTypeId = EResourceType.RESOURCE_GEER;
		this.oResourceID = new ResourceSignature(new GEERSignature());
	}
	
	@Override
	public synchronized IDemand execute()
	{
		// TODO review
		return this;
	}

	@Override
	public synchronized double getSize()
	{
		// TODO review
		return 1;
	}

	/**
	 * Allows querying for TODO.
	 * @return returns the value eResourceTypeId field.
	 */
	public synchronized EResourceType getResourceTypeId()
	{
		return this.eResourceTypeId;
	}
	
	public boolean isResourceGEER()
	{
		return this.getResourceTypeId().equals(EResourceType.RESOURCE_GEER);
	}

	public boolean isResourceData()
	{
		return this.getResourceTypeId().equals(EResourceType.RESOURCE_DATA);
	}
	/**
	 * Allows setting TODO.
	 * @param peResourceTypeId the new value of eResourceTypeId to set.
	 */
	public synchronized void setResourceTypeId(EResourceType peResourceTypeId)
	{
		this.eResourceTypeId = peResourceTypeId;
	}

	/**
	 * Allows querying for TODO.
	 * @return returns the value oResourceID field.
	 */
//	public GEERSignature getResourceID()
	public synchronized ResourceSignature getResourceID()
	{
		return this.oResourceID;
	}

	/**
	 * Allows setting TODO.
	 * @param poResourceID the new value of oResourceID to set.
	 */
	public synchronized void setResourceID(ResourceSignature poResourceID)
	{
		this.oResourceID = poResourceID;
	}

	public synchronized void setResourceID(GEERSignature poResourceID)
	{
		this.oResourceID = new ResourceSignature(poResourceID);
	}

	/* (non-Javadoc)
	 * @see java.util.Vector#toString()
	 */
	@Override
	public synchronized String toString()
	{
		// TODO Auto-generated method stub
		return super.toString();
	}
}

// EOF
