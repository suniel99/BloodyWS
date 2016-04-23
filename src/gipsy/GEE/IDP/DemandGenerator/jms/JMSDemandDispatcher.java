package gipsy.GEE.IDP.DemandGenerator.jms;

import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcher;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcherException;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;



/**
 * The DemandDispatcher of JMS version, currently has no dependency
 * on JMS features, and talks to the store through generic 
 * ITransportAgent.
 * 
 * @author Yi Ji
 * @version $Id: JMSDemandDispatcher.java,v 1.27 2012/04/04 13:28:42 mokhov Exp $
 * @since
 */
public class JMSDemandDispatcher
extends DemandDispatcher
implements IDemandDispatcher
{
    /**
	 * Constructor for easy Class.forName().newInstance();
	 */
	public JMSDemandDispatcher() 
	throws DemandDispatcherException
	{
		try 
    	{
			this.oTA = new JMSTransportAgent();
		} 
    	catch (DMSException e) 
    	{
			e.printStackTrace(System.err);
			throw new DemandDispatcherException(e);
		}
	}

	/**
     * Does nothing, just for compatibility with Jini.
     * @param pstr1
     * @param pstr2
     */
    public JMSDemandDispatcher(String pstr1, String pstr2)
    throws DemandDispatcherException
    {
    	try 
    	{
			this.oTA = new JMSTransportAgent();
		} 
    	catch (DMSException e) 
    	{
			e.printStackTrace(System.err);
			throw new DemandDispatcherException(e);
		}
    }
    
    public JMSDemandDispatcher(ITransportAgent poTA)
    {
    	this.oTA = poTA;
    }
    
	/*
	 * ---------------------
	 * IDemandDispatcher API
	 * ---------------------
	 */
    
	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#cancelDemand(gipsy.GEE.IDP.demands.DemandSignature)
	 */
	@Override
	public void cancelDemand(DemandSignature poSignature)
	throws DemandDispatcherException
	{
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readDemand()
	 */
	@Override
	public IDemand readDemand()
	throws DemandDispatcherException
	{
		while(true)
		{
			try 
			{
				return (IDemand) this.oTA.getDemand(DemandSignature.DGT);
			} 
			catch (DMSException oException) 
			{
				if(this.oTAExceptionHandler != null)
				{
					try 
					{
						this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
					} 
					catch (InterruptedException eInterrupted) 
					{
						eInterrupted.printStackTrace(System.err);
					}
				}
				else
				{
					oException.printStackTrace(System.err);
					throw new DemandDispatcherException(oException);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readDemandIfExists()
	 */
	@Override
	public IDemand readDemandIfExists()
	throws DemandDispatcherException
	{
		while(true)
		{
			try 
			{
				return (IDemand) this.oTA.getDemandIfExists();
			} 
			catch (DMSException oException) 
			{
				if(this.oTAExceptionHandler != null)
				{
					try 
					{
						this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
					} 
					catch (InterruptedException eInterrupted) 
					{
						eInterrupted.printStackTrace(System.err);
					}
				}
				else
				{
					oException.printStackTrace(System.err);
					throw new DemandDispatcherException(oException);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readResult(gipsy.GEE.IDP.demands.DemandSignature)
	 */
	@Override
	public IDemand readResult(DemandSignature poSignature)
	throws DemandDispatcherException
	{
		while(true)
		{
			try 
			{
				return (IDemand) this.oTA.getResult(poSignature);
			} 
			catch (DMSException oException) 
			{
				if(this.oTAExceptionHandler != null)
				{
					try 
					{
						this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
					} 
					catch (InterruptedException eInterrupted) 
					{
						eInterrupted.printStackTrace(System.err);
					}
				}
				else
				{
					oException.printStackTrace(System.err);
					throw new DemandDispatcherException(oException);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readResultIfExists(gipsy.GEE.IDP.demands.DemandSignature)
	 */
	@Override
	public IDemand readResultIfExists(DemandSignature poSignature)
	throws DemandDispatcherException
	{
		while(true)
		{
			try 
			{
				return this.oTA.getResultIfExists(poSignature);
			} 
			catch (DMSException oException) 
			{
				if(this.oTAExceptionHandler != null)
				{
					try 
					{
						this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
					} 
					catch (InterruptedException eInterrupted) 
					{
						eInterrupted.printStackTrace(System.err);
					}
				}
				else
				{
					oException.printStackTrace(System.err);
					throw new DemandDispatcherException(oException);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#writeDemand(gipsy.GEE.IDP.demands.IDemand)
	 */
	@Override
	public DemandSignature writeDemand(IDemand poDemand)
	throws DemandDispatcherException
	{
		// Check entry condition
		if(poDemand == null)
		{
			throw new NullPointerException();
		}
		
		// Check if the demand has already been computed
		DemandSignature oSignature = poDemand.getSignature();
		IDemand oResult = null;
		
		while(oSignature != null)
		{
			try 
			{
				oResult = this.oTA.getResultIfExists(oSignature);
				break;
			} 
			catch (DMSException oException) 
			{
				if(this.oTAExceptionHandler != null)
				{
					try 
					{
						this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
					} 
					catch (InterruptedException eInterrupted) 
					{
						eInterrupted.printStackTrace(System.err);
					}
				}
				else
				{
					oException.printStackTrace(System.err);
					throw new DemandDispatcherException(oException);
				}
			}
		}
		
		// If the demand has been computed then no need to send it
		if(oResult != null)
		{
			return oSignature;
		}
		
		// Send the demand
		while(true)
		{
			try 
			{
				oSignature = this.oTA.setDemand(poDemand);
				return oSignature;
			} 
			catch (DMSException oException) 
			{
				if(this.oTAExceptionHandler != null)
				{
					try 
					{
						this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
					} 
					catch (InterruptedException eInterrupted) 
					{
						eInterrupted.printStackTrace(System.err);
					}
				}
				else
				{
					oException.printStackTrace(System.err);
					throw new DemandDispatcherException(oException);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#writeResult(gipsy.GEE.IDP.demands.DemandSignature, gipsy.GEE.IDP.demands.IDemand)
	 */
	@Override
	public DemandSignature writeResult(DemandSignature poSignature, IDemand poResult)
	throws DemandDispatcherException
	{
		while(true)
		{
			try 
			{
				return this.oTA.setResult(poResult);
			} 
			catch (DMSException oException) 
			{
				if(this.oTAExceptionHandler != null)
				{
					try 
					{
						this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
					} 
					catch (InterruptedException eInterrupted) 
					{
						eInterrupted.printStackTrace(System.err);
					}
				}
				else
				{
					oException.printStackTrace(System.err);
					throw new DemandDispatcherException(oException);
				}
			}
		}
	}

	@Override
	public IDemand getValue(IDemand poDemand) 
	throws DemandDispatcherException 
	{
		// Check entry condition
		if(poDemand == null)
		{
			throw new NullPointerException();
		}
		
		// Check if the demand has already been computed
		DemandSignature oSignature = poDemand.getSignature();
		IDemand oResult = null;
		
		while(oSignature != null)
		{
			try 
			{
				oResult = this.oTA.getResultIfExists(oSignature);
				break;
			} 
			catch (DMSException oException) 
			{
				if(this.oTAExceptionHandler != null)
				{
					try 
					{
						this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
					} 
					catch (InterruptedException eInterrupted) 
					{
						eInterrupted.printStackTrace(System.err);
					}
				}
				else
				{
					oException.printStackTrace(System.err);
					throw new DemandDispatcherException(oException);
				}
			}
		}
		
		// If the demand has been computed then no need to send it
		if(oResult != null)
		{
			return oResult;
		}
		
		// Send the demand and wait for the result
		while(true)
		{
			try 
			{
				oSignature = this.oTA.setDemand(poDemand);
				oResult = this.oTA.getResult(oSignature);
				return oResult;
			} 
			catch (DMSException oException) 
			{
				if(this.oTAExceptionHandler != null)
				{
					try 
					{
						this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
					} 
					catch (InterruptedException eInterrupted) 
					{
						eInterrupted.printStackTrace(System.err);
					}
				}
				else
				{
					oException.printStackTrace(System.err);
					throw new DemandDispatcherException(oException);
				}
			}
		}
	}
}

// EOF
