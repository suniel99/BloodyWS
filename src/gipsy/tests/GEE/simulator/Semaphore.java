package gipsy.tests.GEE.simulator;


/**
 * Binary Semaphore to synchronize the threads.
 * 
 * XXX: see if it's a really good idea to have a binary semaphore
 * vs. a counting one here at all.
 * 
 * XXX: generalize?
 * 
 * XXX: move to gipsy.util?
 * 
 * @author Emil Vassev
 * @version $Id: Semaphore.java,v 1.4 2009/09/07 01:07:49 mokhov Exp $
 * @since
 */
class Semaphore 
{
	/**
	 * 
	 */
	private boolean bValue;
	
	/**
	 * @param pbValue
	 */
	Semaphore(boolean pbValue) 
	{
		this.bValue = pbValue;
	}

	/**
	 * @return
	 */
	public synchronized boolean isLocked() 
	{
		return !this.bValue;
	}
	
	/**
	 * 
	 */
	public synchronized void lock() 
	{
		this.bValue = false;
	}
	
	/**
	 * 
	 */
	public synchronized void waitOn() 
	{
		while(!this.bValue) 
		{
			try { wait (); }
			catch (InterruptedException e) { };
		}
	}

	/**
	 * 
	 */
	public synchronized void release() 
	{
		this.bValue = true;
		
		// XXX: how useful is notifyAll() if only one thread
		//      will succeed through and all the others will
		//      go back to sleep?
		notifyAll();
	}
}

// EOF
