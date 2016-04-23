package gipsy.interfaces;

import java.io.Serializable;


/**
 * Encapsulates unique GEER signature.
 * 
 * @author Serguei Mokhov
 * @version $Id: GEERSignature.java,v 1.6 2013/08/25 02:54:42 mokhov Exp $
 */
public class GEERSignature
extends GIPSYSignature
{
	/**
	 * For serialization versioning. 
	 */
	private static final long serialVersionUID = -69169728417154596L;

	public GEERSignature()
	{
		super();
	}

	public GEERSignature(Serializable poSignature)
	{
		super(poSignature);
	}

	/**
	 * Compute signature based on the GEER itself and set it.
	 * @param poProgram
	 */
	public void setSignature(GIPSYProgram poProgram)
	{
		this.oSignature = new String(poProgram.getName() + ":" + poProgram.getContextValue());
	}
}

// EOF
