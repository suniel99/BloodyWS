package gipsy.interfaces;

import java.io.Serializable;
import java.lang.reflect.Method;


/**
 * <p>Sequential Thread represents a piece work to be done.
 * Has to extend Serializable for RMI, CORBA, COM+, Jini to work.
 * Runnable needed to run it in a separate thread.</p>
 *
 * @author Serguei Mokhov
 * @version $Id: ISequentialThread.java,v 1.17 2009/09/30 10:47:19 mokhov Exp $
 * @since Inception
 */
public interface ISequentialThread
extends Runnable, Serializable
{
	/**
	 * Work-piece to be done.
	 * @return WorkResult container
	 */
//	public WorkResult work();
//	public IDemand work();
//	public Object work();
	public Serializable work();
	
//	public WorkResult getWorkResult();
//	public IDemand getWorkResult();
//	public Object getWorkResult();
	public Serializable getWorkResult();

	public void setMethod(Method poSTMethod);
}

// EOF
