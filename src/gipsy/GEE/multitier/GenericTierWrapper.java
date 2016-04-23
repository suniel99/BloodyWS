package gipsy.GEE.multitier;

import gipsy.Configuration;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.util.Trace;
import marf.util.BaseThread;
import marf.util.Debug;


/**
 * An abstract class implementing general portions of the IMultiTierWrapper interface.
 * 
 * @author Bin Han
 * @author Serguei Mokhov
 * @author Yi Ji
 * 
 * @version $Id: GenericTierWrapper.java,v 1.19 2012/04/08 01:10:21 mokhov Exp $
 * @since
 * 
 * @see IMultiTierWrapper
 */
public abstract class GenericTierWrapper
implements IMultiTierWrapper
{
	/**
	 * The demand dispatcher used by the tier.
	 * "Lower-half" of the tier's driver-like nature.
	 */
	protected IDemandDispatcher oDemandDispatcher = null;
	
	/**
	 * A nullable TA exception handler.
	 */
	protected TAExceptionHandler oTAExceptionHandler = null;
	
	/**
	 * The configuration settings of the tier.
	 */
	protected Configuration oConfiguration = null;
	
	/**
	 * A main thread of the tier to start.
	 */
	protected BaseThread oTierThread = null;

	/**
	 * The an ID within the node XXX .
	 */
	protected String strTierID = null;

	/**
	 * For logging, etc.
	 */
	private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";

	/**
	 * Default constructor with a null dispatcher and blank configuration.
	 */
	public GenericTierWrapper()
	{
		this.oConfiguration = new Configuration();
		this.oDemandDispatcher = null;
	}

	/**
	 * @param poDemandDispatcher
	 * @param poConfiguration
	 */
	public GenericTierWrapper(IDemandDispatcher poDemandDispatcher, Configuration poConfiguration)
	{
		this.oDemandDispatcher = poDemandDispatcher;
		this.oConfiguration = poConfiguration;
	}

	/**
	 * Set the tier's configuration settings.
	 * @param poConfiguration
	 */
	public void setConfiguration(Configuration poConfiguration)
	{
		this.oConfiguration = poConfiguration;

		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MSG_PREFIX + "." + Trace.getEnclosingMethodName() + "(Configuration) was called by [" +
				Trace.getCallerClassName() + "]." + Trace.getCallerMethodName() + "()"
			);	
		}
	}

	/**
	 * Return the tier's configuration settings.
	 * @return oConfiguration
	 */
	public Configuration getConfiguration()
	{
		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MSG_PREFIX + "." + Trace.getEnclosingMethodName() + "() was called by [" +
				Trace.getCallerClassName() + "]." + Trace.getCallerMethodName() + "()"
			);	
		}

		return this.oConfiguration;
	}

	/**
	 * Set the Transport Agent of the Tier's dispatcher.
	 * @param poTA the TA to set
	 * @throws NullPointerException if the dispatcher object is null
	 */
	public void setTransportAgent(ITransportAgent poTA)
	{
		this.oDemandDispatcher.setTA(poTA);
	}

	public void setTAExceptionHandler(TAExceptionHandler poTAExceptionHandler)
	{
		this.oTAExceptionHandler = poTAExceptionHandler;
	}

	public void setDemandDispatcher(IDemandDispatcher poDemandDispatcher)
	{
		this.oDemandDispatcher = poDemandDispatcher;
	}
	
	public IDemandDispatcher getDemandDispatcher()
	{
		return this.oDemandDispatcher;
	}
	
	public String getTierID()
	{
		return this.strTierID;
	}
	
	public void setTierID(String pstrTierID)
	{
		this.oConfiguration.setProperty(WRAPPER_TIER_ID, pstrTierID);
		this.strTierID = pstrTierID;
	}

	/*
	 * Object API
	 */

	/**
	 * Two tiers are equal if their configurations and dispatchers are equal.
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @see #oConfiguration
	 * @see #oDemandDispatcher
	 */
	public boolean equals(Object poObject)
	{
		if(poObject instanceof GenericTierWrapper)
		{
			GenericTierWrapper oGTW = (GenericTierWrapper)poObject;
			return this.oConfiguration.equals(oGTW.getConfiguration())
				&& this.oDemandDispatcher.equals(oGTW.getDemandDispatcher());
		}

		return false;
	}
}

// EOF
