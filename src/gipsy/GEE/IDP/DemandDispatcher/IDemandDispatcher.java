package gipsy.GEE.IDP.DemandDispatcher;

import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.multitier.TAExceptionHandler;


/**
 * API of all demand dispatchers.
 * 
 * @author Serguei Mokhov
 * @since 1.0.0
 * @version $Id: IDemandDispatcher.java,v 1.14 2012/04/04 13:28:36 mokhov Exp $
 */
public interface IDemandDispatcher
{
	/**
	 * Write demand into a store.
	 *   
	 * @param poDemand
	 * @return the written signature (could be Uuid, String, whatever)
	 * @throws DemandDispatcherException
	 */
	DemandSignature writeDemand(IDemand poDemand)
	throws DemandDispatcherException;

	/**
	 * Write a result into a store.
	 *
	 * @param poID
	 * @param poResult
	 * @return
	 * @throws DemandDispatcherException
	 */
	DemandSignature writeResult(DemandSignature poSignature, IDemand poResult)
	throws DemandDispatcherException;

	/**
	 * Read a task from a store.
	 *
	 * @return
	 * @throws DemandDispatcherException
	 */
	IDemand readDemand()
	throws DemandDispatcherException;

	/**
	 * Read a task, if exists, from a store.
	 *
	 * @return
	 * @throws DemandDispatcherException
	 */
	IDemand readDemandIfExists()
	throws DemandDispatcherException;

	/**
	 * Read a result from the store. This method blocks the client's
	 * thread but is used in a different way from {@link #getValue(IDemand)}.
	 *
	 * @param poID
	 * @return
	 * @throws DemandDispatcherException
	 * @see #getValue(IDemand)
	 */
	IDemand readResult(DemandSignature poSignature)
	throws DemandDispatcherException;

	/**
	 * Read a result, if exists, from the store.
	 *
	 * @param poID
	 * @return
	 * @throws DemandDispatcherException
	 */
	IDemand readResultIfExists(DemandSignature poSignature)
	throws DemandDispatcherException;

	/**
	 * Cancel a demand already dispatched to the store.
	 * If the demand is already proceeded, then the result will be canceled.
	 *  
	 * @param poSignature
	 * @throws DemandDispatcherException
	 */
	void cancelDemand(DemandSignature poSignature)
	throws DemandDispatcherException;
	
	/**
	 * Get the value of the specified demand and block. This method 
	 * is different from {@link #readResult(DemandSignature)}
	 * because it is a combination of {@link #writeDemand(IDemand)}
	 * + {@link #readResult(DemandSignature)}. Its scenario is that
	 * the client passes a demand to the method, and waits for the 
	 * result to return.
	 * 
	 * @param poDemand the demand requiring a value
	 * @return the result of the demand
	 * @throws DemandDispatcherException
	 * @see #readResult(DemandSignature)
	 */
	IDemand getValue(IDemand poDemand)
	throws DemandDispatcherException;
	
	/**
	 * To set the transport agent (TA) of this demand dispatcher.
	 * @param poTA the new TA
	 */
	void setTA(ITransportAgent poTA);
	
	/**
	 * To get the transport agent (TA) of this demand dispatcher.
	 * @return the TA instance
	 */
	ITransportAgent getTA();

	/**
	 * Register a TA exception handler with this demand dispatcher for
	 * logging and error reporting.
	 * @param poTAExceptionHandler the handler to use
	 */
	void setTAExceptionHandler(TAExceptionHandler poTAExceptionHandler);
	
	/**
	 * Retrieve the exception handler instance for TAs.
	 * @return the exception handler instance
	 */
	TAExceptionHandler getTAExceptionHandler();

}

// EOF
