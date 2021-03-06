package gipsy.GEE.IDP.DemandGenerator.threaded;

import gipsy.interfaces.IIdentifierContext;
import gipsy.util.NotImplementedException;
import gipsy.GEE.GEEException;

/**
 * This is the "identifier-context class originally designed for the GEE prototype developed by Paula Lu. 
 * It has to be replaced with a "Demand" class that will be generated by the GEE, and eventually migrated
 * using the Demand Migration Framework (DMF).
 * 
 * Changes to be made: 
 * 
 * 1- The new class will have mainly two members: identifier (the identifier in the Lucid program to which this demand corresponds to), and context (the context associated with this demand).
 * 2- The values stored (i.e. the result of the demand) is now an integer. It has to accomodate for any type of value (i.e. Object). 
 * 3- We have to accommodate for "Aggregated Demands", i.e. demands associated with context sets such as a Box (see Kaiyu Wan's thesis). 
 * 4- Propagate the design changes into the GEE and DMF design. 
 * 
 * @author Paula Lu
 */
public class IdentifierContext
implements IIdentifierContext, Runnable
{
	protected int hcode;
	
	/**	
	 * thread name is given here to make the debugging easier
	 */
	protected int name;
	
	protected int[] cont;
	protected boolean ready;
	protected int value;

	public IdentifierContext(int h, int n, int[] k)
	{
		//    super( tname );
		hcode = h;
		name = n;
		cont = k;
		ready = false;
		value = -1;
	}

	/**
	 * the thread ic is executed due to its definition
	 * each identifier in a lucid program has its own definition, which should be convert into a separate class
	 */ 
	public void run()
	{
		throw new NotImplementedException(this.getClass().getName() + ".run()");
	}

	public final boolean isReady()
	{
		return ready;
	}

	public final int getValue()
	{
		return value;
	}

	public final void setReady()
	{
		ready = true;
	}

	public final void setValue(int v)
	{
		value = v;
	}

	public final int getName()
	{
		return name;
	}

	public final int getHcode()
	{
		return hcode;
	}

	public final int[] getCont()
	{
		return cont;
	}

	public Object cal()	throws GEEException
	{
		throw new GEEException(this.getClass().getName() + ".cal()");
	}
}

// EOF
