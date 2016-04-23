package gipsy.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcherException;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;

import java.io.Serializable;

import net.jini.id.Uuid;


/**
 * Jini-based Interface IDemandDispatcher.
 * 
 * XXX: generalize.
 * 
 * @author Emil Vassev
 * @author Serguei Mokhov
 * @since 1.0.0
 * @version $Id: IJiniDemandDispatcher.java,v 1.1 2009/08/13 08:06:27 mokhov Exp $
 */
public interface IJiniDemandDispatcher
extends IDemandDispatcher
{
	/**
	 * Write a task to the JavaSpace and return the unique ID of the written object.
	 *   
	 * @param poObject
	 * @return
	 * @throws DemandDispatcherException
	 */
	public Uuid writeDemand(Serializable poObject)
	throws DemandDispatcherException;

	/**
	 * Write a result to the JavaSpace and return the unique ID of the written object.
	 *
	 * @param poID
	 * @param poObject
	 * @return
	 * @throws DemandDispatcherException
	 */
	public Uuid writeResult(Uuid poID, Serializable poObject)
	throws DemandDispatcherException;

	/**
	 * Read a task from the JavaSpace.
	 *
	 * @return
	 * @throws DemandDispatcherException
	 */
	public JiniDispatcherEntry readDemandEntry()
	throws DemandDispatcherException;

	/**
	 * Read a task, if exists, from the JavaSpace.
	 *
	 * @return
	 * @throws DemandDispatcherException
	 */
	public JiniDispatcherEntry readDemandEntryIfExists()
	throws DemandDispatcherException;

	/**
	 * Read a result from the JavaSpace.
	 *
	 * @param poID
	 * @return
	 * @throws DemandDispatcherException
	 */
	public Serializable readResult(Uuid poID)
	throws DemandDispatcherException;

	/**
	 * Read a result, if exists, from the JavaSpace.
	 *
	 * @param poID
	 * @return
	 * @throws DemandDispatcherException
	 */
	public Serializable readResultIfExists(Uuid poID)
	throws DemandDispatcherException;

	/**
	 * Cancel a task already dispatched to the JavaSpace.
	 * If the task is already proceeded, then the result will be canceled.
	 *  
	 * @param poID
	 * @throws DemandDispatcherException
	 */
	public void cancelDemand(Uuid poID)
	throws DemandDispatcherException;
}

// EOF
