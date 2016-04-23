package gipsy.GEE.IDP.DemandGenerator.jms;


import gipsy.GEE.IDP.DemandDispatcher.IDispatcherEntry;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.IDemand;

import java.io.Serializable;


/**
 * JMS Dispatcher Entry. By the time committed by Yi it was found of no use 
 * for JMS because in JMS a demands must be wrapped in an ObjectMessage who 
 * must be instantiated by a Session, i.e. its instantiation and its internal
 * implementation is out of our control. Besides, the ObjectMessage itself
 * allows setting primitive properties for query purpose, therefore using 
 * a DispatcherEntry to wrap and delegate an ObjectMessage does not add
 * more functionality but complexity, therefore discarded. 
 *
 * @author Amir Pourteymour
 * @author Serguei Mokhov
 * @since 1.0.0
 * @version $Id: JMSDispatcherEntry.java,v 1.10 2010/12/20 16:41:39 ji_yi Exp $
 */
//public class DispatcherEntry extends AbstractEntry
@Deprecated
public class JMSDispatcherEntry
//implements Serializable
implements IDispatcherEntry, Serializable
{
	/*
	 * ------------
	 * Data Members
	 * ------------
	 */
	private IDemand oDemand;
	private DemandSignature oSignature;
	private DemandState oState;
	
	
	
	/**
	 * @param oDemand
	 * @param oState
	 */
	public JMSDispatcherEntry(IDemand oDemand, DemandState oState) 
	{
		super();
		this.oDemand = oDemand;
		this.oState = oState;
	}
	/**
	 * @param oDemand
	 * @param oSignature
	 * @param oState
	 */
	public JMSDispatcherEntry(
			DemandSignature oSignature, 
			IDemand oDemand,
			DemandState oState) 
	{
		super();
		this.oDemand = oDemand;
		this.oSignature = oSignature;
		this.oState = oState;
	}
	/**
	 * @return the oDemand
	 */
	public IDemand getoDemand() 
	{
		return oDemand;
	}
	/**
	 * @param oDemand the oDemand to set
	 */
	public void setoDemand(IDemand oDemand) 
	{
		this.oDemand = oDemand;
	}
	/**
	 * @return the oSignature
	 */
	public DemandSignature getoSignature() 
	{
		return oSignature;
	}
	/**
	 * @param oSignature the oSignature to set
	 */
	public void setoSignature(DemandSignature oSignature) 
	{
		this.oSignature = oSignature;
	}
	/**
	 * @return the oState
	 */
	public DemandState getoState() 
	{
		return oState;
	}
	/**
	 * @param oState the oState to set
	 */
	public void setoState(DemandState oState) 
	{
		this.oState = oState;
	}
	@Override
	public String getDestination() 
	{
		// 
		return null;
	}
	@Override
	public void setDestination(String pstrDestination) 
	{
		// 
	}
	
}

// EOF
