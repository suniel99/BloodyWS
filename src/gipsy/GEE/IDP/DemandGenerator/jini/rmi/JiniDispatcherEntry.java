package gipsy.GEE.IDP.DemandGenerator.jini.rmi;

import gipsy.GEE.IDP.DemandDispatcher.IDispatcherEntry;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.IDemand;

import java.io.Serializable;

import net.jini.entry.AbstractEntry;
import net.jini.id.Uuid;


/**
 * Jini Dispatcher Entry.
 * 
 * This is the format used by the JiniDimandDispatcher to store 
 * the tasks in JavaSpaces.
 * 
 * XXX: It also seems JavaSpaces requires the data members to
 * be explicitly public in this class.
 * 
 * @author Emil Vassev
 * @author Serguei Mokhov
 * @author Ji Yi
 * 
 * @since 1.0.0
 * @version $Id: JiniDispatcherEntry.java,v 1.13 2010/09/11 23:45:13 ji_yi Exp $
 */
public class JiniDispatcherEntry
extends AbstractEntry
implements IDispatcherEntry
{
	/*
	 * ------------
	 * Data Members
	 * ------------
	 */

	@Deprecated // Replaced by DemandSignature
	public Uuid oUniqueID = null;
	
	// Properties for template matching
	public DemandSignature oSignature = null;
	public DemandState oDemandState = null;
	public String strDestination = null;
	
	/**
	 * The demand wrapped in thie entry
	 */
	public Serializable oDemand = null; 
	/*
	 * ---------------
	 * Object Lifetime
	 * ---------------
	 */

	/**
	 * Constructor.
	 */
	public JiniDispatcherEntry() 
	{
		this.oSignature = null;
		this.oDemandState = null;
		this.strDestination = null;
		this.oDemand = null;
	}
    
	/**
	 * @param poID
	 */
	public JiniDispatcherEntry(Uuid poID) 
	{
		this(poID, null);
	}
   
	/**
	 * @param poID
	 * @param poDemand
	 */
	public JiniDispatcherEntry(Uuid poID, Serializable poDemand) 
	{
		// The demand's state is pending at par.
		this(poID, poDemand, DemandState.PENDING);
	}
   
	public JiniDispatcherEntry(Uuid poID, Serializable poDemand, DemandState poDemandState) 
	{
		this.oUniqueID = poID;
		this.oDemand = poDemand;
		this.oDemandState = poDemandState;
	}
   
   /**
    * To be added after re-engineering.
    * XXX: UUID *is* the signature!
    * @param pstrSignature
    * @param poState
    */
   public JiniDispatcherEntry(DemandSignature pstrSignature, DemandState poState)
   {
	   this.oSignature = pstrSignature;
	   this.oDemand = null;
	   this.oUniqueID = null;
	   this.oDemandState = poState;
   }
   
   /**
    * To be added after re-engineering.
    * XXX: UUID *is* the signature!
    * @param poDemand
    * @param poState
    */
   public JiniDispatcherEntry(IDemand poDemand, DemandState poState)
   {
	   this.oUniqueID = null;
	   this.oDemand = poDemand;
	   this.oDemandState = poState;

	   if(poDemand != null)
	   {
		   this.oSignature = poDemand.getSignature();
	   }
	   else
	   {
		   this.oSignature = null;
	   }
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
