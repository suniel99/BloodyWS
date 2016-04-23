package gipsy.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.IntensionalDemand;
import gipsy.GEE.IDP.demands.ProceduralDemand;
import gipsy.GEE.IDP.demands.ResourceDemand;
import gipsy.GEE.IDP.demands.SystemDemand;

import java.io.Serializable;

import net.jini.core.entry.Entry;
import net.jini.core.lease.Lease;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.id.Uuid;
import net.jini.id.UuidFactory;
import net.jini.lookup.entry.Name;
import net.jini.space.JavaSpace;


/**
 * The class acting as the JavaSpace access object who communicates
 * with the JavaSpace directly, and has almost the same content and 
 * duty as the original JiniDemandDispatcher, so that it now takes 
 * over the role of the original JiniDemandDispatcher inside the 
 * Jini TAs. By this substitution the "Jini features" inside the 
 * JiniDemandDispatchers are kept here and the traumatic effect on 
 * the Jini TAs is minimum. 
 * 
 * @author Emil Vassev
 * @author Serguei Mokhov
 * @author Yi Ji
 * 
 * @since 1.0.0
 * @version $Id: JavaSpaceAccessObject.java,v 1.24 2010/12/27 23:28:15 ji_yi Exp $
 */
@Deprecated //Note: its functionality has been moved to JINITA.
public class JavaSpaceAccessObject
{
	/*
	 * ------------
	 * Constants
	 * ------------
	 */

// XXX: unused constance
//	private static final String MSG_PREFIX = "DemandDispacther message: ";
//	private static final String ERR_PREFIX = "DemandDispacther error: ";

	
	/**
	 * Data members
	 */
	private static String sstrSpaceName = "";

	private JavaSpace oJavaSpace = null;

	
	


	/**
	 * Create new unique ID.   
	 */
	private synchronized static Uuid getNewUniqueID()
	{
		return UuidFactory.generate();
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
	 * @throws DMSException
	 */
	private Uuid write(Uuid poID, Serializable poDemandOrResult, DemandState poDemandState)
	throws DMSException
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

//			oEntry = new JiniDispatcherEntry(oUniqueID, poDemandOrResult, poDemandState);
			oEntry = new JiniDispatcherEntry((IDemand)poDemandOrResult, poDemandState);
			oLease = oJavaSpace.write(oEntry, null, Lease.FOREVER);   

			return oEntry.oUniqueID;
		}
		catch(Exception ex)
		{
			throw new DMSException(ex.getMessage()); 
		}
	}

	/**
	 * Read a task or result from the JavaSpace.
	 *
	 * @param poID
	 * @param poDemandState
	 * @return
	 * @throws DMSException
	 */
	private JiniDispatcherEntry read(Uuid poID, DemandState poDemandState)
	throws DMSException
	{
		try
		{
			JiniDispatcherEntry oEntry;

			oEntry = new JiniDispatcherEntry(poID, null, poDemandState);
			oEntry = (JiniDispatcherEntry) oJavaSpace.take(oEntry, null, Lease.FOREVER);   
		
			return oEntry;
		}
		catch(Exception ex)
		{
			throw new DMSException(ex.getMessage()); 
		}
	}

	/**
	 * Read a task or result, if exists, from the JavaSpace.
	 *
	 * @param poID
	 * @param poDemandState
	 * @return
	 * @throws DMSException
	 */
	private JiniDispatcherEntry readIfExists(Uuid poID, DemandState poDemandState)
	throws DMSException
	{
		try
		{
			JiniDispatcherEntry oEntry;

			oEntry = new JiniDispatcherEntry(poID, null, poDemandState);
			oEntry = (JiniDispatcherEntry)oJavaSpace.takeIfExists(oEntry, null, JavaSpace.NO_WAIT);

			return oEntry;
		}
		catch(Exception ex)
		{
			throw new DMSException(ex.getMessage());
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
	throws DMSException
	{
		return write(null, poDemand, DemandState.PENDING);
	}

	/**
	 * Write a result to the JavaSpace and return the unique ID of the written object.
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#writeResult(net.jini.id.Uuid, java.io.Serializable)
	 */
	public synchronized Uuid writeResult(Uuid poID, Serializable poResult)
	throws DMSException
	{
		return write(poID, poResult, DemandState.COMPUTED);
	}

	/**
	 * Read a task from the JavaSpace.
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readDemandEntry()
	 */
	public JiniDispatcherEntry readDemandEntry()
	throws DMSException
	{
		return read(null, DemandState.PENDING);
	}

	/**
	 * Read a task, if exists, from the JavaSpace.
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readDemandEntryIfExists()
	 */
	public JiniDispatcherEntry readDemandEntryIfExists()
	throws DMSException
	{
		return readIfExists(null, DemandState.PENDING);
	}
	
	/**
	 * Read a result from the JavaSpace.
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readResult(net.jini.id.Uuid)
	 */
	public Serializable readResult(Uuid poID)
	throws DMSException
	{
		return read(poID, DemandState.COMPUTED).oDemand;
	}

	/**
	 * Read a result, if exists, from the JavaSpace.
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readResultIfExists(net.jini.id.Uuid)
	 */
	public Serializable readResultIfExists(Uuid poID)
	throws DMSException
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
	throws DMSException
	{
		try
		{
			JiniDispatcherEntry oEntry;

			oEntry = new JiniDispatcherEntry(poID, null, null);
			oEntry = (JiniDispatcherEntry)oJavaSpace.take(oEntry, null, Lease.FOREVER);   
		}
		catch(Exception ex)
		{
			throw new DMSException(ex.getMessage()); 
		}
	}


	/*
	 * ---------------
	 * Object Lifetime
	 * ---------------
	 */
	
	/**
	 * Construct a instance based on jini lookup service uri and javaspace name.
	 *
	 * @param pstrLookupServiceUR - Where the jini lookup service (reggie) is running
	 * @param pstrJavaspaceName - The attribute used to locate a specific javaspace instance
	 * @throws DMSException
	 */
	public JavaSpaceAccessObject(String pstrLookupServiceURI, String pstrJavaspaceName) 
	throws DMSException
	{
		try 
		{
			Class<?>[] aoServiceTypes = new Class[] {JavaSpace.class};
			Entry[] aoAttributes = new Entry[] {new Name(pstrJavaspaceName)};
			ServiceTemplate oTemplate = new ServiceTemplate(null, aoServiceTypes, aoAttributes);
	        
			// Using multicast discovery
			//JiniServiceDiscoverer oJiniServiceDiscoverer = new JiniServiceDiscoverer(oTemplate);
			//oJavaSpace = (JavaSpace) oJiniServiceDiscoverer.getService(LookupDiscovery.ALL_GROUPS);
			
			// Using unicast discovery
			UnicastJiniServiceDiscoverer oServiceLocator = new UnicastJiniServiceDiscoverer();
			
			this.oJavaSpace = (JavaSpace)oServiceLocator.getService(oTemplate, pstrLookupServiceURI);
			
			if(this.oJavaSpace == null)
			{
				throw new Exception("Unable to locate JavaSpace " + sstrSpaceName);
			}

			runAfterCreation();
		}
		catch(Exception ex)
		{
			throw new DMSException(ex.getMessage()); 
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

	/**
	 * Cancel a demand
	 * @param poSignature the demand signature
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#cancelDemand(gipsy.GEE.IDP.demands.DemandSignature)
	 */
//	@Override
	public void cancelDemand(DemandSignature poSignature)
	throws DMSException
	{
		cancelDemand((Uuid)poSignature.getSignature());
	}

	/**
	 * Read a pending demand and block.
	 * @return a pending demand.
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readDemand()
	 */
//	@Override
	public IDemand readDemand()
	throws DMSException
	{
		return (IDemand)readDemandEntry().oDemand;
	}

	/**
	 * Read a pending demand if there is any. Non-blocking.
	 * @return a pending demand
	 * @see gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher#readDemandIfExists()
	 */
//	@Override
	public IDemand readDemandIfExists()
	throws DMSException
	{
		return (IDemand)readDemandEntryIfExists().oDemand;
	}

	/**
	 * Read the result of the demand with the specified signature.
	 * This method extracts the mutual process of:
	 * ITransportAgent.getResult(poSignature) and
	 * ITransportAgent.getResultIfExists(poSignature)
	 * 
	 * @param poSignature the demand signature
	 * @param isWaiting if the method requires blocking: true for
	 * blocking, false for peeking
	 * @return the result matching the signature
	 * @throws DMSException
	 */
	public IDemand readResult(DemandSignature poSignature, boolean isWaiting)
	throws DMSException
	{
		try 
		{
			// Check the store to see if the demand has been computed
			JiniDispatcherEntry oEntry;
			
			// Wait here untill we get the value
			oEntry = new JiniDispatcherEntry(poSignature, DemandState.COMPUTED);
			if(isWaiting)
			{
				oEntry = (JiniDispatcherEntry)oJavaSpace.read(oEntry, null, Lease.FOREVER);
			}
			else
			{
				oEntry = (JiniDispatcherEntry)oJavaSpace.read(oEntry, null, JavaSpace.NO_WAIT);
			}
			
			if(oEntry == null)
			{
				throw new DMSException("The DST is offline!");
			}
			
			return (IDemand)oEntry.oDemand;
		} 
		catch(DMSException oException)
		{
			throw oException;
		}
		catch (Exception oException) 
		{
			throw new DMSException(oException);
		}
	}

	/**
	 * Put the value into the store. This method extracts the
	 * mutual process of the Jini implementation of:
	 * ITransportAgent#setResult(poResult) and
	 * ITransportAgent#setDemand(poDemand);
	 * 
	 * @param poValue the value to be put into the store
	 * @param poState the state of the demand/result
	 * @return the signature
	 * @throws DMSException
	 */
	public DemandSignature setValue(IDemand poValue, DemandState poState) 
	throws DMSException
	{
		// Check the entry condition
		if(poValue == null || poState == null || poValue.getSignature() == null)
		{
			throw new DMSException("Illegal argument detected");
		}
		
		// Write the value into the store
		try 
		{
			JiniDispatcherEntry oEntry;
			
			// Write the computed result to the store
			oEntry = new JiniDispatcherEntry(poValue, poState);
			
			// If it is a pending demand
			if(poState.equals(DemandState.PENDING))
			{
				if(poValue instanceof SystemDemand)
				{
					oEntry.strDestination = (String) ((SystemDemand)poValue).getDestinationTierID();
				}
				else if(poValue instanceof ProceduralDemand)
				{
					oEntry.strDestination = DemandSignature.DWT;
				}
				else if(poValue instanceof IntensionalDemand)
				{
					oEntry.strDestination = DemandSignature.DGT;
				}
				else if(poValue instanceof ResourceDemand)
				{
					oEntry.strDestination = DemandSignature.ANY_DEST;
				}
				else
				{
					/* 
					 * Treat unknown demand as a procedural demand by default
					 * for backward compatibility.
					 */
					oEntry.strDestination = DemandSignature.DWT;
				}
			}
			
			oJavaSpace.write(oEntry, null, Lease.FOREVER);
			
			// If it is a 
			if(poState.isComputed())
			{
				// Take the in-process demand from the store if any
				oEntry = new JiniDispatcherEntry(poValue.getSignature(), DemandState.INPROCESS);
				oJavaSpace.takeIfExists(oEntry, null, JavaSpace.NO_WAIT);
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace(System.err);
			throw new DMSException(e);
		}
		System.out.println("A value is set");
		return poValue.getSignature();
	}
	
	/**
	 * Take a pending demand sent to the given destination 
	 * from the Javaspace
	 * 
	 * @param pstrDestination - The destination of the demand
	 * @return - The demand sent to the destination
	 * @throws DMSException
	 */
	public IDemand takeDemand(String pstrDestination)
	throws DMSException
	{
		try
		{
			// Prepare the template
			JiniDispatcherEntry oEntry = new JiniDispatcherEntry();
			oEntry.oDemandState = DemandState.PENDING;
			
			if(pstrDestination == null)
			{
				oEntry.strDestination = DemandSignature.DWT;
			}
			else
			{
				oEntry.strDestination = pstrDestination;
			}
			
			// Take an object matching the above template from the Javaspace
			oEntry = (JiniDispatcherEntry) this.oJavaSpace.take(oEntry, null, Lease.FOREVER);
			
			if(oEntry == null)
			{
				throw new DMSException("The DST is offline!");
			}
			
			return (IDemand)oEntry.oDemand;
			
		}
		catch(DMSException oException)
		{
			throw oException;
		}
		catch(Exception oException)
		{
			throw new DMSException(oException);
		}
	}
	
}

// EOF
