package gipsy.GIPC.imperative.CommunicationProcedureGenerator;

import gipsy.interfaces.ICommunicationProcedure;
import gipsy.interfaces.ICommunicationProceduresEnum;

import java.util.Hashtable;


/**
 * <p>Generates Communications Procedures for Generator-Worker communication.</p>
 *
 * @author Serguei Mokhov
 * @version $Id: CommunicationProcedureGenerator.java,v 1.10 2010/12/07 01:37:08 mokhov Exp $
 * @since 1.0.0
 */
public abstract class CommunicationProcedureGenerator
implements ICommunicationProceduresEnum
{
	/**
	 * Available implementations of CPs.
	 */
	protected Hashtable<Integer, Boolean> oCPImplementations = new Hashtable<Integer, Boolean>();
	
	/**
	 * 
	 */
	protected ICommunicationProcedure[] aoCPs = null;

	/**
	 * Default Constructor.
	 * Sets up implementations available.
	 */
	public CommunicationProcedureGenerator()
	{
		/*
		 * NOTE: when you implement another CP set its implementation flag
		 * to 'true' here.
		 */
		this.oCPImplementations.put(NULL_CP,      true);
		this.oCPImplementations.put(RMI_CP,       true);
		this.oCPImplementations.put(CORBA_CP,     false);
		this.oCPImplementations.put(COM_CP,       false);
		this.oCPImplementations.put(JINI_RMI_CP,  true);
		this.oCPImplementations.put(JINI_JERI_CP, false);
	}
	
	/**
	 * Implementation-specific generation of the CP annotations.
	 */
	public abstract void generate();
	
	/**
	 * @return
	 */
	public ICommunicationProcedure[] getCommunicationProcedures()
	{
		return this.aoCPs;
	}
}

// EOF
