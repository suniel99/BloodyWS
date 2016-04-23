package gipsy.GEE.IDP.DemandGenerator.jms;

import gipsy.GEE.IDP.ITransportAgent;

import java.io.Serializable;


/**
 * DMSJMS implements this interface.
 * 
 * @author Amir Pourteymour
 * @author Serguei Mokhov
 * 
 * @version 1.0.0, $Id: IJMSTransportAgent.java,v 1.4 2009/08/21 19:49:05 ji_yi Exp $
 * @since 2007-06-18
 */
public interface IJMSTransportAgent
extends ITransportAgent
{	

	public void release();
}

// EOF
