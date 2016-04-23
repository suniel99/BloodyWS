package gipsy.GEE.evaluation;

import gipsy.GEE.GEEException;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.interfaces.GIPSYProgram;
import gipsy.lang.GIPSYContext;
import gipsy.lang.GIPSYType;
import gipsy.storage.Dictionary;


/**
 * @author Serguei Mokhov
 * @version $Id: IEvaluationEngine.java,v 1.1 2010/07/22 18:21:29 mokhov Exp $
 * @since July 22, 2010
 */
public interface IEvaluationEngine
{
	/**
	 * Main evaluation method.
	 * @param poGEER
	 * @return
	 * @throws GEEException
	 */
	GIPSYType eval(GIPSYProgram poGEER)
	throws GEEException;

	/**
	 * New version of <code>eval()</code> that corresponds to the new definitions
	 * of GIPSYContext.
	 * 
	 * @param poRoot root of the AST
	 * @param paoContext
	 * @param piIndent
	 * @return
	 * @throws GEEException
	 */
	GIPSYType eval(SimpleNode poRoot, GIPSYContext[] paoContext, int piIndent)
	throws GEEException;

	/**
	 * Main execution method.
	 * @param poDictionary
	 * @param poDimensionTags
	 */
	GIPSYType execute(Dictionary poDictionary, GIPSYContext poDimensionTags);
}

// EOF
