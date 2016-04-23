package gipsy.GEE.IDP.demands;
import java.io.Serializable;


/**
 * GIPSY Demand State Enumeration Type. 
 * This is the enumeration type used by the DispatcherEntry to store 
 * the current state of the demand.
 * 
 * How to use the enumerator:
 * 
 * 1. Declare variables to hold states days as normal: DemandState oState;
 * 2. Assign a specific state to the variable: oState = DemandState.PENDING;           
 *
 * @author Emil Vassev
 * @since 1.0.0
 * @see gipsy.GEE.IDP.IDispatcherEntry
 */
public class DemandState
implements Serializable 
{
	/*
	 * Constants
	 */

	private static final long serialVersionUID = 1000L;
	
	/*
	 * ------------
	 * Data Members
	 * ------------
	 */

	private static final String STATE_PENDING = "pending";  
	private static final String STATE_INPROCESS = "inprocess"; 
	private static final String STATE_COMPUTED = "computed"; 

	private String strState = "";

	public static final DemandState PENDING = new DemandState(STATE_PENDING);  
	public static final DemandState INPROCESS = new DemandState(STATE_INPROCESS); 
	public static final DemandState COMPUTED = new DemandState(STATE_COMPUTED); 

	private DemandState(String pstrNewState) 
	{ 
		this.strState = pstrNewState.toLowerCase();
	} 

	public boolean isPending()
	{
		if(this.strState.equals(STATE_PENDING))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isInProcess()
	{
		if(this.strState.equals(STATE_INPROCESS))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isComputed()
	{
		if(this.strState.equals(STATE_COMPUTED))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * For the purpose of testing.
	 */
	public String toString()
	{
		if(this.strState.equals(STATE_PENDING))
		{
			return "STATE_PENDING";
		}
		else if(this.strState.equals(STATE_INPROCESS))
		{
			return "STATE_INPROCESS";
		}
		else
			return "STATE_COMPUTED";
	}
}

// EOF
