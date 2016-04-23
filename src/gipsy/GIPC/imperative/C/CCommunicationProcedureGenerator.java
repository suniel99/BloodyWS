package gipsy.GIPC.imperative.C;

import gipsy.GIPC.imperative.CommunicationProcedureGenerator.CommunicationProcedureGenerator;
import marf.util.Debug;


/**
 * <p>Generates a set of defined communication precedures for
 * the Java language. Does the mapping between C and GIPSY
 * types.
 * </p>
 *
 * $Id: CCommunicationProcedureGenerator.java,v 1.2 2005/09/11 00:15:59 mokhov Exp $
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @version $Revision: 1.2 $
 */
public class CCommunicationProcedureGenerator
extends CommunicationProcedureGenerator
{
	public void generate()
	{
		Debug.debug("Fake C CPs generated.");
	}
}

// EOF
