package gipsy.GEE.IDP.DemandWorker;

import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.TAExceptionHandler;



/**
 * The interface for all the worker implementation, in progressing. 
 * 
 * @author Bin Han
 * @version $Id: IDemandWorker.java,v 1.6 2012/06/17 17:09:14 mokhov Exp $
 */
public interface IDemandWorker
extends Runnable
{
	/**
	 * Set TA which will be used by the worker.
	 * 
	 * @param poDMFImp might be Jini, JMS...
	 */
	void setTransportAgent(EDMFImplementation poDMFImp);
	
	void setTransportAgent(ITransportAgent poTA);

	void setTAExceptionHandler(TAExceptionHandler poTAExceptionHandler);

	void startWorker();
	
	void stopWorker();
}

// EOF
