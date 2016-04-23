package gipsy.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcher;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcherException;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.util.NetUtils;
import gipsy.util.Trace;

import java.io.Serializable;

import marf.util.Debug;
import net.jini.core.lease.Lease;
import net.jini.id.Uuid;
import net.jini.id.UuidFactory;
import net.jini.space.JavaSpace;


/**
 * Jini Demand Dispatcher.
 *
 * @author Emil Vassev
 * @author Serguei Mokhov
 * @author Yi Ji
 * 
 * @since 1.0.0
 * @version $Id: JiniDemandDispatcher.java,v 1.30 2012/06/17 16:58:55 mokhov Exp $
 */
public class JiniDemandDispatcher
extends DemandDispatcher
//implements IJiniDemandDispatcher, IValueStore
implements IJiniDemandDispatcher
{
	/*
	 * ------------
	 * Data Members
	 * ------------
	 */
	private static final String DEFAULT_JAVASPACE = "JavaSpaces";
	private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";
	private static final String ERR_PREFIX = "DemandDispacther error: ";

	private static String sstrSpaceName = "";
	private static String sstrHostName = "";
	private static JavaSpace soJavaSpace = null;

	private static int siMaxRetryTime = 4;
	
	/**
	 * Configuration settings.
	 */
	private static final String SECURITY_FILE = "jini.policy";
	
	/*
	 * ---------------
	 * Private Methods
	 * ---------------
	 */
	

	/**
	 * Create new unique ID.   
	 */
	private synchronized static Uuid getNewUniqueID()
	{
		return UuidFactory.generate();
	}
	
	/**
	 * Constructor for easy Class.forName().newInstance();
	 * @throws DemandDispatcherException
	 */
	public JiniDemandDispatcher()
	throws DemandDispatcherException
	{
		try 
		{
			if(Debug.isDebugOn())
			{
				Debug.debug
				(
					MSG_PREFIX
					+ ".Constructor() was called, instantiating TA ..."
				);
			}

			this.oTA = new JINITA();
			
			if(Debug.isDebugOn())
			{
				Debug.debug(MSG_PREFIX + ".Constructor(): TA hash: " + this.oTA.hashCode());
			}
		} 
		catch(Exception e) 
		{
			if(Debug.isDebugOn())
			{
				Debug.debug(MSG_PREFIX + ".Conctructor(): TA instantiation failed.");
			}

			e.printStackTrace(System.err);
			throw new DemandDispatcherException(e);
		}
	}
	
	/**
	 * Write a task or result to the JavaSpace and return the unique ID 
	 * of the written object.
	 * If bResult is true, write the result and vice versa.
	 * 
	 * @param poID
	 * @param poDemandOrResult
	 * @param poDemandState
	 * @return
	 * @throws DemandDispatcherException
	 */
	private static Uuid write(Uuid poID, Serializable poDemandOrResult, DemandState poDemandState)
	throws DemandDispatcherException
	{
		try
		{
			//XXX: oLease is never read
			Lease oLease;
			Uuid oUniqueID;
			JiniDispatcherEntry oEntry;

			if(poDemandState.isComputed())
			{
				oUniqueID = poID;
			}
			else
			{
				oUniqueID = getNewUniqueID();
			}

			oEntry = new JiniDispatcherEntry(oUniqueID, poDemandOrResult, poDemandState);
			oLease = soJavaSpace.write(oEntry, null, Lease.FOREVER);   

			return oEntry.oUniqueID;
		}
		catch(Exception ex)
		{
			throw new DemandDispatcherException(ex.getMessage()); 
		}
	}

	/**
	 * Read a task or result from the JavaSpace.
	 *
	 * @param poID
	 * @param poDemandState
	 * @return
	 * @throws DemandDispatcherException
	 */
	private static JiniDispatcherEntry read(Uuid poID, DemandState poDemandState)
	throws DemandDispatcherException
	{
		try
		{
			JiniDispatcherEntry oEntry;

			oEntry = new JiniDispatcherEntry(poID, null, poDemandState);
			oEntry = (JiniDispatcherEntry) soJavaSpace.take(oEntry, null, Lease.FOREVER);   
		
			return oEntry;
		}
		catch(Exception ex)
		{
			throw new DemandDispatcherException(ex.getMessage()); 
		}
	}

	/**
	 * Read a task or result, if exists, from the JavaSpace.
	 *
	 * @param poID
	 * @param poDemandState
	 * @return
	 * @throws DemandDispatcherException
	 */
	private static JiniDispatcherEntry readIfExists(Uuid poID, DemandState poDemandState)
	throws DemandDispatcherException
	{
		try
		{
			JiniDispatcherEntry oEntry;

			oEntry = new JiniDispatcherEntry(poID, null, poDemandState);
			oEntry = (JiniDispatcherEntry)soJavaSpace.takeIfExists(oEntry, null, JavaSpace.NO_WAIT);

			return oEntry;
		}
		catch(Exception ex)
		{
			throw new DemandDispatcherException(ex.getMessage());
		}
	}


	/*
	 * --------------
	 * Public Methods
	 * --------------
	 */

	/**
	 * Write a task to the JavaSpace and return the unique ID of the written object.
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#writeDemand(java.io.Serializable)
	 */
	public synchronized Uuid writeDemand(Serializable poDemand)
	throws DemandDispatcherException
	{
		return write(null, poDemand, DemandState.PENDING);
	}

	/**
	 * Write a result to the JavaSpace and return the unique ID of the written object.
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#writeResult(net.jini.id.Uuid, java.io.Serializable)
	 */
	public synchronized Uuid writeResult(Uuid poID, Serializable poResult)
	throws DemandDispatcherException
	{
		return write(poID, poResult, DemandState.COMPUTED);
	}

	/**
	 * Read a task from the JavaSpace.
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readDemandEntry()
	 */
	public JiniDispatcherEntry readDemandEntry()
	throws DemandDispatcherException
	{
		return read(null, DemandState.PENDING);
	}

	/**
	 * Read a task, if exists, from the JavaSpace.
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readDemandEntryIfExists()
	 */
	public JiniDispatcherEntry readDemandEntryIfExists()
	throws DemandDispatcherException
	{
		return readIfExists(null, DemandState.PENDING);
	}
	
	/**
	 * Read a result from the JavaSpace.
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readResult(net.jini.id.Uuid)
	 */
	public Serializable readResult(Uuid poID)
	throws DemandDispatcherException
	{
		return read(poID, DemandState.COMPUTED).oDemand;
	}

	/**
	 * Read a result, if exists, from the JavaSpace.
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readResultIfExists(net.jini.id.Uuid)
	 */
	public Serializable readResultIfExists(Uuid poID)
	throws DemandDispatcherException
	{
		JiniDispatcherEntry oEntry = readIfExists(poID, DemandState.COMPUTED);

		if(oEntry != null)
		{
			return oEntry.oDemand;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Cancel a task already dispatched to the JavaSpace;
	 * If the task is already proceeded, then the result will be canceled.
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#cancelDemand(net.jini.id.Uuid)
	 */
	public void cancelDemand(Uuid poID)
	throws DemandDispatcherException
	{
		try
		{
			JiniDispatcherEntry oEntry;

			oEntry = new JiniDispatcherEntry(poID, null, null);
			oEntry = (JiniDispatcherEntry)soJavaSpace.take(oEntry, null, Lease.FOREVER);   
		}
		catch(Exception ex)
		{
			throw new DemandDispatcherException(ex.getMessage()); 
		}
	}


	/*
	 * ---------------
	 * Object Lifetime
	 * ---------------
	 */
	
	/**
	 * Constructor.
	 *
	 * @param pstrHostName
	 * @param pstrStoreName
	 * @throws DemandDispatcherException
	 */
	public JiniDemandDispatcher(String pstrHostName, String pstrStoreName) 
	throws DemandDispatcherException
	{
		try 
		{
			/*
			 * Check and init the input variables
			 */		  
			if((pstrHostName.equalsIgnoreCase("")) || (pstrHostName.equalsIgnoreCase("local")))
			{
				sstrHostName = NetUtils.getLocalIPAddress();
			}
			else
			{
				sstrHostName = pstrHostName;
			}

			if(pstrStoreName.equalsIgnoreCase(""))
			{
				sstrSpaceName = DEFAULT_JAVASPACE;
			}
			else
			{
				sstrSpaceName = pstrStoreName;
			}

			/*
			 * Get the JavaSpace
			 		
			soJavaSpace = getJavaSpace(sstrHostName, sstrSpaceName);
			
			if(soJavaSpace == null)
			{
				throw new Exception("Impossible to locate the JavaSpace " + sstrSpaceName);
			}
*/	
			runAfterCreation();
			this.oTA = new JINITA();
//			this.oJTA = new JMSTransportAgent("","");
		}
		catch(Exception ex)
		{
			throw new DemandDispatcherException(ex); 
		}
	}
    
	/**
	 * This method runs after the creation of the Demand Dispatcher object;
	 * put your post-initial stuff here.
	 */
	protected void runAfterCreation()
	{
		// Nothing
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
		cancelDemand((Uuid)poSignature.getSignature());
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readDemand()
	 */
	@Override
	public IDemand readDemand()
	throws DemandDispatcherException
	{
		if(Debug.isDebugOn())
		{
			Debug.debug(MSG_PREFIX + "." +  Trace.getEnclosingMethodName() + 
					"() was called by [" + Trace.getCallerClassName() + "]");
		}
		
		for(int i = 0; i<siMaxRetryTime; i++)
		{
			try 
			{
				return this.oTA.getDemand(DemandSignature.DGT);
			} 
			catch (DMSException oException) 
			{
				if(this.oTAExceptionHandler != null && i < (siMaxRetryTime-1))
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
		throw new DemandDispatcherException("DST is no longer reachable!");
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readDemandIfExists()
	 */
	@Override
	public IDemand readDemandIfExists()
	throws DemandDispatcherException
	{
		for(int i = 0; i<siMaxRetryTime; i++)
		{
			try 
			{
				return this.oTA.getDemandIfExists();
			} 
			catch (DMSException oException) 
			{
				if(this.oTAExceptionHandler != null && i < (siMaxRetryTime-1))
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
		throw new DemandDispatcherException("DST is no longer reachable!");
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readResult(gipsy.GEE.IDP.demands.DemandSignature)
	 */
	@Override
	public IDemand readResult(DemandSignature poSignature)
	throws DemandDispatcherException
	{
		for(int i = 0; i < siMaxRetryTime; i++)
		{
			try 
			{
				return this.oTA.getResult(poSignature);
			} 
			catch(DMSException oException) 
			{
				if(this.oTAExceptionHandler != null && i < (siMaxRetryTime - 1))
				{
					try 
					{
						this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
					} 
					catch(InterruptedException eInterrupted) 
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

		throw new DemandDispatcherException("DST is no longer reachable!");
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readResultIfExists(gipsy.GEE.IDP.demands.DemandSignature)
	 */
	@Override
	public IDemand readResultIfExists(DemandSignature poSignature)
	throws DemandDispatcherException
	{
		try 
		{
			if(Debug.isDebugOn())
			{
				Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() +
						"(" + poSignature + ") called by [" + Trace.getCallerClassName()
						+ "]");
			}
			
			return this.oTA.getResultIfExists(poSignature);
		} 
		catch (DMSException e) 
		{
			e.printStackTrace(System.err);
			throw new DemandDispatcherException(e);
		}
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#writeDemand(gipsy.GEE.IDP.demands.IDemand)
	 */
	@Override
	public DemandSignature writeDemand(IDemand poDemand)
	throws DemandDispatcherException
	{
		if(Debug.isDebugOn())
		{
			Debug.debug(MSG_PREFIX + "." + Trace.getEnclosingMethodName() + 
					"(sig: " + poDemand.getSignature() + ") called by [" + Trace.getCallerClassName() + "]");
		}
		
		
		// Check the entry condition
		if(poDemand == null)
		{
			throw new NullPointerException("demand is null");
		}
		
		// Check if the demand has already been computed
		DemandSignature oSignature = poDemand.getSignature();
		IDemand oResult = null;
		
		for(int i = 0; i < siMaxRetryTime; i++)
		{
			try 
			{
				oResult = this.oTA.getResultIfExists(oSignature);
				break;
			} 
			catch(DMSException oException)
			{
				if(this.oTAExceptionHandler != null && i < (siMaxRetryTime-1))
				{
					try 
					{
						this.oTA = this.oTAExceptionHandler.fixTA(this.oTA, oException);
					} 
					catch(InterruptedException eInterrupted) 
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
		
		// If no result available, then send the demand
		for(int i = 0; i < siMaxRetryTime; i++)
		{
			try 
			{
				oSignature = this.oTA.setDemand(poDemand);
				break;
			} 
			catch(DMSException oException) 
			{
				if(this.oTAExceptionHandler != null && i < (siMaxRetryTime-1))
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

			Debug.debug
			(
				MSG_PREFIX + "." + 
				Trace.getEnclosingMethodName() + 
				"(" + poDemand +
				"): demand sent"
			);
		}

		return oSignature;
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#writeResult(gipsy.GEE.IDP.demands.DemandSignature, gipsy.GEE.IDP.demands.IDemand)
	 */
	@Override
	public DemandSignature writeResult(DemandSignature poSignature, IDemand poResult)
	throws DemandDispatcherException
	{
		for(int i = 0; i < siMaxRetryTime; i++)
		{
			try 
			{
				return this.oTA.setResult(poResult);
			} 
			catch(DMSException oException) 
			{
				if(this.oTAExceptionHandler != null && i < (siMaxRetryTime-1))
				{
					try 
					{
						this.oTA = this.oTAExceptionHandler.fixTA(this.oTA, oException);
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

		throw new DemandDispatcherException("DST is no longer reachable!");
	}
	
	
	/*
	 * --------------------
	 * Integration material
	 * --------------------
	 */
	
	public JiniDemandDispatcher(ITransportAgent poJTA)
	{
		this.oTA = poJTA;
	}

	@Override
	public IDemand getValue(IDemand poDemand)
	throws DemandDispatcherException 
	{
		// Check entry condition
		if(poDemand == null)
		{
			throw new NullPointerException("Demand parameter is null.");
		}
		
		// Check if the demand has already been computed
		DemandSignature oSignature = poDemand.getSignature();
		IDemand oResult = null;
		for(int i = 0; i < siMaxRetryTime; i++)
		{
			try 
			{
				oResult = this.oTA.getResultIfExists(oSignature);
				break;
			} 
			catch(DMSException oException) 
			{
				if(this.oTAExceptionHandler != null && i < (siMaxRetryTime-1))
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
		for(int i = 0; i < siMaxRetryTime; i++)
		{
			try 
			{
				oSignature = this.oTA.setDemand(poDemand);
				oResult = this.oTA.getResult(oSignature);
				return oResult;
			} 
			catch(DMSException oException) 
			{
				if(this.oTAExceptionHandler != null && i < (siMaxRetryTime-1))
				{
					try 
					{
						this.oTA = this.oTAExceptionHandler.fixTA(oTA, oException);
					} 
					catch(InterruptedException eInterrupted) 
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

		throw new DemandDispatcherException("DST is no longer reachable!");
	}
}

// EOF
