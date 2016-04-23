package gipsy.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.demands.IDemand;

import java.io.Serializable;

import net.jini.id.Uuid;


/**
 * Interface IJINITransportAgent.
 * This is the interface that the JTA proxy implements.
 *
 * @author Emil Vassev
 * @author Serguei Mokhov
 * @since 1.0.0
 * @version $Id: IJINITransportAgent.java,v 1.11 2010/09/11 23:45:13 ji_yi Exp $
 */
@Deprecated
public interface IJINITransportAgent
extends ITransportAgent{
	/*
	 * These are Jini-specific interface methods
	 * (Uuid is Jini's mechanism).
	 */
	@Deprecated
	Serializable getDemand(Uuid poDemandSignature);
	@Deprecated
	Serializable getDemandIfExists(Uuid poDemandSignature); 	@Deprecated
	Serializable getResult(Uuid poResultSignature);
	@Deprecated
	Serializable getResultIfExists(Uuid poResultSignature); 

	//public Uuid setDemand(Serializable poDemand); 
	@Deprecated	public Uuid setResult(Serializable poResult, Uuid idResult); }

// EOF