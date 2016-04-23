package gipsy.GEE.multitier.DST;

import gipsy.Configuration;
import gipsy.GEE.IVW.Warehouse.IVWInterface;
import gipsy.GEE.multitier.GenericTierWrapper;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.util.Trace;
import marf.util.Debug;


/**
 * Implementation class of Demand Store Tier (DST) extending 
 * <code>GenericTierWrapper</code> and implementing the 
 * <code>IMultiTierWrapper</code> interface.
 * 
 * The Demand Store Tier (DST) acts as a middleware between tiers in order to migrate demands 
 * between them. In addition to the migration of the demands and values across different tiers, 
 * the Demand Store Tier provide persistent storage of demands and their resulting values, 
 * thus achieving better processing performances by not having to re-compute the value of 
 * every demand every time it is eventually re-generated after having been processed. 
 * From this latter perspective, it is equivalent to the historical notion of "warehouse" in 
 * the eduction model of computation. 
 * 
 * As a DST instance is more suitable to be a process than to be a thread (to overcome the 
 * virtual memory limit of any process, usually 2G in Windows and 3G in Linux), the DSTWrapper
 * is responsible to start a new DST process, monitor this process and destroy this process.
 * Moreover, the DSTWrapper should also be able to export the TA exposed by the new
 * DST process, and be able to communicate with other DST using the assigned TA.
 * 
 * Also, the heterogeneity of storage subsystems implies different launching sequences, 
 * therefore each storage subsystem may need its own DSTWrapper.
 * 
 * 
 * @author Bin Han
 * @author Serguei Mokhov
 * @author Yi Ji
 * 
 * @version $Id: DSTWrapper.java,v 1.20 2012/04/04 13:40:17 mokhov Exp $
 * @since
 * 
 * @see gipsy.GEE.multitier.IMultiTierWrapper
 * @see gipsy.GEE.multitier.GenericTierWrapper
 */
public class DSTWrapper
extends GenericTierWrapper
{
	/**
	 * Abstraction over the object serialization, NetCDF,
	 * JavaSpaces or JBoss for storage management.
	 * 
	 * However, middleware such as Jini and JMS originally does
	 * not comply with IVWInterface, instead, it is the TA
	 * that provides the services defined by IVWInterface, 
	 * therefore the IVWInterface storage subsystem is currently not 
	 * useful for the middleware providing storage service. 
	 */
	protected IVWInterface oStorageSubsystem = null;

	/**
	 * Each DST instance is a process to overcome the potential 
	 * memory limit that possessed by memory-based storage subsystem. 
	 * It is also compatible with storage subsystems that not very 
	 * memory dependent. For storage subsystems that are not easily 
	 * replicable, e.g. a database, the process here could be the
	 * database client implementing the service defined by its 
	 * exposed remote TA. 
	 */
	protected Process oDSTInstance = null;
	
	/**
	 * For logging
	 */
	private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";
	
	/*
	 * Configuration properties
	 */
	public static final String DST_WORKING_DIR = "gipsy.GEE.multitier.DST.workingdir";
	public static final String IS_PUBLIC = "gipsy.GEE.multitier.DST.ispublic";
	public static final String MAX_ACTIVE_CONNECTION = "gipsy.GEE.multitier.DST.maxActiveConnection";
	
	
	public DSTWrapper()
	{
		if(Debug.isDebugOn())
		{
			Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() + 
					"() was called by [" +
					Trace.getCallerClassName() + "]." + Trace.getCallerMethodName() + 
					"()");	
		}
	}
	
	/**
	 * Constructor
	 * @param poStorageSubsystem
	 */
	/*public DSTWrapper(ITransportAgent poTA, IVWInterface poStorageSubsystem)
	{
		super(poTA);
		this.oStorageSubsystem = poStorageSubsystem;
		if(Debug.isDebugOn())
		{
			Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() + 
					"(IVWInterface) was called by [" +
					Trace.getCallerClassName() + "]." + Trace.getCallerMethodName() + 
					"()");	
		}
	}*/

	/**
	 * Set StorageSubsystem
	 * @param poStorageSubsystem
	 */
	public void setStorageSubsystem(IVWInterface poStorageSubsystem)
	{
		this.oStorageSubsystem = poStorageSubsystem;
	}
	/**
	 * Get StorageSubsystem;
	 * @return
	 */
	public IVWInterface getStorageSubsystem()
	{
		return this.oStorageSubsystem;
	}
	/**
	 * Inheritance from Runnable.
	 */
	public void run()
	{
//		Under Construction.
//		this.oTA.start();
//		this.oStorageSubsystem.start();
	}
	
	/**
	 * Start the tier instance.
	 * @throws MultiTierException 
	 */
	public void startTier() 
	throws MultiTierException
	{
		// print "DST starting..."
		if(Debug.isDebugOn())
		{
			Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() + 
					"() was called by [" +
					Trace.getCallerClassName() + "]." + Trace.getCallerMethodName() + 
					"()");	
		}
	}
	
	/**
	 * Stop the tier instance.
	 */
	public void stopTier()
	throws MultiTierException
	{	
		// print "DST stopping..."
		if(Debug.isDebugOn())
		{
			Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() + 
					"() was called by [" +
					Trace.getCallerClassName() + "]." + Trace.getCallerMethodName() + 
					"()");	
		}
	} 
	
	/**
	 * Expose the TA configuration.
	 * @return - The TA configuration exposed.
	 */
	public Configuration exportTAConfig()
	{
		return null;
	}
	
}

//EOF
