package gipsy.interfaces;

/**
 * Enumeration of implemented or planned communication procedures.
 *
 * $Header: /cvsroot/gipsy/gipsy/src/gipsy/interfaces/ICommunicationProceduresEnum.java,v 1.2 2004/06/22 13:56:12 mokhov Exp $
 * @author Serguei Mokhov
 */
public interface ICommunicationProceduresEnum
{
	/**
	 * NullCommuncationProcedure, plain TLP
	 */
	public int NULL_CP      = 0;

	/**
	 * Plain RMI
	 */
	public int RMI_CP       = 1;

	/**
	 * CORBA
	 */
	public int CORBA_CP     = 2;

	/**
	 * DCOM, COM, COM+
	 */
	public int COM_CP       = 3;

	/**
	 * Jini over RMI
	 */
	public int JINI_RMI_CP  = 4;

	/**
	 * Jini over JERI
	 */
	public int JINI_JERI_CP = 5;
}

// EOF
