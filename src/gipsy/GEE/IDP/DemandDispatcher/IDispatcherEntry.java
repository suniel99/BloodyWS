package gipsy.GEE.IDP.DemandDispatcher;

import java.io.Serializable;


/**
 * Generic Dispatcher Entry.
 * 
 * @author Serguei Mokhov
 * 
 * @since 1.0.0
 * @version $Id: IDispatcherEntry.java,v 1.2 2010/09/09 18:21:05 mokhov Exp $
 */
public interface IDispatcherEntry
extends Serializable
{
	String getDestination();
	
	void setDestination(String pstrDestination);
}

// EOF
