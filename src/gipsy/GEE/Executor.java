package gipsy.GEE;

import gipsy.GEE.IDP.DemandGenerator.IDemandGenerator;
import gipsy.GEE.IDP.DemandGenerator.Interpreter;
import gipsy.GEE.IDP.DemandGenerator.LegacyInterpreter;
import gipsy.GEE.IVW.Warehouse.IVWInterface;
import gipsy.GEE.evaluation.IEvaluationEngine;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.interfaces.GIPSYProgram;
import gipsy.lang.GIPSYContext;
import gipsy.lang.GIPSYType;
import gipsy.storage.Dictionary;
import marf.util.BaseThread;


/**
 * <p>GEE Executor.</p>
 * 
 * XXX: in refactoring transition.
 * 
 * @author Serguei Mokhov
 * @version $Id: Executor.java,v 1.37 2010/07/22 18:21:28 mokhov Exp $
 * @since November 18, 2002
 */
public class Executor
extends BaseThread
implements IEvaluationEngine
{
	private IDemandGenerator oInterpreter = null;
	private IDemandGenerator oLegacyInterpreter = null;
	
	/**
	 * Assumes all the values at their defaults.
	 */
	public Executor()
	{
		this.oInterpreter = new Interpreter();
		this.oLegacyInterpreter = new LegacyInterpreter();
	}
	
    /**
     * Allows setting a different than the default data warehouse.
     * @param poValueHouse the new data warehouse to set.
     * @see #oValueHouse
     */
    public Executor(IVWInterface poValueHouse)
    {
		this.oInterpreter = new Interpreter(poValueHouse);
		this.oLegacyInterpreter = new LegacyInterpreter(poValueHouse);
    }  

	/**
	 * Older implementation of the main evaluation routine
	 * based on the local interpreter, integer dimensions,
	 * and integer types.
	 * 
	 * @param poExpressionRoot Lucid expression's root node in the AST
	 * @param paiContext current integer context of evaluation
	 * @param piIndent indentation for the printing purposes as we descend into the tree
	 * @return the value the expression evaluates to, an integer
	 * @throws GEEException
	 */
	@Deprecated
	public int eval(SimpleNode poExpressionRoot, int[] paiContext, int piIndent) 
	throws GEEException
	{
		return ((LegacyInterpreter)this.oLegacyInterpreter).eval(poExpressionRoot, paiContext, piIndent);
	}

	/**
	 * Main execution method.
	 * @param poDictionary
	 * @param pstrDimensionValues
	 * @deprecated in favor of GIPSYContext version
	 */
	@Deprecated
	public int execute(Dictionary poDictionary, String pstrDimensionValues)
	{
		return ((LegacyInterpreter)this.oLegacyInterpreter).execute(poDictionary, pstrDimensionValues);
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IEvaluationEngine#eval(gipsy.GIPC.intensional.SimpleNode, gipsy.lang.GIPSYContext[], int)
	 */
	public GIPSYType eval(SimpleNode poRoot, GIPSYContext[] paoContext, int piIndent) 
	throws GEEException
	{
		return this.oInterpreter.eval(poRoot, paoContext, piIndent);
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.IEvaluationEngine#execute(gipsy.storage.Dictionary, gipsy.lang.GIPSYContext)
	 */
	public GIPSYType execute(Dictionary poDictionary, GIPSYContext poDimensionTags)
	{
		return this.oLegacyInterpreter.execute(poDictionary, poDimensionTags);
	}

	@Override
	public GIPSYType eval(GIPSYProgram poGEER)
	throws GEEException
	{
		execute(poGEER.getDictionary(), poGEER.getContextValue());
		return null;
	}
}

// EOF
