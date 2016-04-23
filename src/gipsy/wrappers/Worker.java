package gipsy.wrappers;

//import gipsy.interfaces.SequentialThread;
import gipsy.interfaces.ICommunicationProcedure;
import gipsy.util.*;

import marf.util.BaseThread;

/**
 * Worker Class Definition
 * 
 * $Revision: 1.11 $ by $Author: mokhov $ on $Date: 2004/11/06 00:50:09 $
 *
 * @version $Revision: 1.11 $
 * @author Serguei Mokhov
 */
public class Worker extends BaseThread
{
	/**
	 * Aggregation of sequential threads.
	 */
	private Thread[] aoSequentialThreads = null;

	/**
	 * Set of available communication procedures for different protocols.
	 */
	private ICommunicationProcedure[] aoCommuncationProcedures = null;

	/**
	 * Default settings.
	 */
	public Worker()
	{
		super();
	}

	/**
	 * Generate a demand.
	 */
	public void demand()
	{
	}

	/**
	 * Receive a result on a demand.
	 */
	public void receive()
	{
	}

	/**
	 * Perform computation.
	 */
	public void work() throws GIPSYException
	{
		try
		{
			for(int i = 0; i < this.aoSequentialThreads.length; i++)
				this.aoSequentialThreads[i].start();
		}
		catch(NullPointerException e)
		{
			throw new GIPSYException
			(
				"Worker TID=" + getTID() +
				" did not have any sequential threads to work on."
			);
		}
	}

	/**
	 * Stops worker thread.
	 */
	public void stopWorker()
	{
	}

	/**
	 * From Runnable interface, for TLP
	 */
	public void run()
	{
		try
		{
			work();
		}
		catch(GIPSYException e)
		{
			System.err.println(e);
		}
	}
}

// EOF
