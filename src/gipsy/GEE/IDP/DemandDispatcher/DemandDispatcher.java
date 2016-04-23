package gipsy.GEE.IDP.DemandDispatcher;

import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.multitier.TAExceptionHandler;
import gipsy.interfaces.LocalDemandStore;

//import gipsy.GEE.IVW.Warehouse.IValueStore;


/**
 * Demand Dispatcher.
 *
 * @author Serguei Mokhov
 * @author Yi Ji
 * 
 * @since 1.0.0
 * @version $Id: DemandDispatcher.java,v 1.21 2012/04/04 13:28:36 mokhov Exp $
 */
public abstract class DemandDispatcher
implements IDemandDispatcher
{
	/*
	 * ------------
	 * Data Members
	 * ------------
	 */
	//public static final String MSG_PREFIX = "DemandDispacther message: ";
	//public static final String ERR_PREFIX = "DemandDispacther error: ";

	//private IValueStore oStore = null;
	protected LocalDemandStore oBuffer = new LocalDemandStore();

	/**
	 * Nullable TA Exception handler.
	 */
	protected TAExceptionHandler oTAExceptionHandler = null;
	
	/**
	 * The Transport Agent used by the dispatcher.
	 */
	protected ITransportAgent oTA = null;
	
	/*
	 * --------------
	 * Public Methods
	 * --------------
	 */

	/*
	 * ---------------
	 * Object Lifetime
	 * ---------------
	 */
	
	public DemandDispatcher()
	{
		
	}

	/**
	 * This method runs after the creation of the Demand Dispatcher object;
	 * put your post-initialization stuff here.
	 */
	protected void runAfterCreation()
	{
		// Nothing
	}
	
	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#setTAExceptionHandler(gipsy.GEE.multitier.TAExceptionHandler)
	 */
	public void setTAExceptionHandler(TAExceptionHandler poTAExceptionHandler)
	{
		this.oTAExceptionHandler = poTAExceptionHandler;
	}
	
	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#getTAExceptionHandler()
	 */
	public TAExceptionHandler getTAExceptionHandler()
	{
		return this.oTAExceptionHandler;
	}
	
	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#setTA(gipsy.GEE.IDP.ITransportAgent)
	 */
	public void setTA(ITransportAgent poTA)
	{
		this.oTA = poTA;
	}
	
	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#getTA()
	 */
	public ITransportAgent getTA()
	{
		return this.oTA;
	}
}

// EOF
