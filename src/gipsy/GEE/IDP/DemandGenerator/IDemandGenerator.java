package gipsy.GEE.IDP.DemandGenerator;

import gipsy.GEE.GEEException;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IVW.Warehouse.IVWControl;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.interfaces.GEERSignature;
import gipsy.interfaces.GIPSYProgram;
import gipsy.lang.GIPSYContext;
import gipsy.lang.GIPSYType;
import gipsy.storage.Dictionary;


/**
 * The interface all demand generators must adhere to.
 * Used by the DGT as the upper-half of the demand processing.
 * 
 * @author Serguei Mokhov
 * @version $Id: IDemandGenerator.java,v 1.8 2010/12/10 04:00:27 mokhov Exp $
 * @since
 * @see DGTWrapper
 * @see IDemandDispatcher
 */
public interface IDemandGenerator
{
	/**
	 * Associates this demand generator with a specified dispatcher in the DGT.
	 * @param poDispatcher the dispatcher to associate with
	 */
	void setDemandDispatcher(IDemandDispatcher poDispatcher);
	
	/**
	 * Sets the GEER to work on.
	 * @param poGEER
	 */
	void setGEER(GIPSYProgram poGEER);

	/**
	 * @param piID
	 * @param paiContext
	 * @return
	 */
	String generateDemand(int piID, int[] paiContext);

	/**
	 * Returns a value of any GIPSY type; blocking.
	 * @param poDemand
	 * @return
	 */
	GIPSYType generateDemandAndWait(IDemand poDemand);

	/**
	 * We don't care for the return result immediately, e.g. a signal or a
	 * a command we don't care to know the result of OR this
	 * is asynchronous call, so returning immediately, but
	 * can't do nothing yet; will have to query for it later.
	 * @param poDemand
	 */
	void generateDemand(IDemand poDemand);

	// Blocking calls to poll/query for the computed demands
	// given either just the demand signature and/or GEERSignature.
	// If the result is already available, it is non-blocking,
	// else it is blocking waiting for the demand to be computed
	IDemand getComputedDemand(DemandSignature poSiganture);
	IDemand getComputedDemand(DemandSignature poDemandSignature, GEERSignature poGEERSignature);

	/**
	 * Any.
	 * @return
	 * @see #getComputedDemand(DemandSignature)
	 */
	IDemand getDemand();

	/**
	 * Final value notified to the store in case.
	 * DGs compute the whole program and the whole's
	 * programs result is known under the same context.
	 * @param poResult
	 */
	void putResult(IDemand poResult);

	// In non-blocking operations, query periodically, and move on
	// XXX: review
	Boolean isComputed(DemandSignature poSignature);
	Boolean isComputed(DemandSignature poDemandSignature, GEERSignature poGEERSignature);

	// Become an observer of the current/specified storage subsystem
	// XXX: review
	Boolean registerWithDemandStore();
	Boolean registerWithDemandStore(IVWControl poStorageSubsystem);
	
	// Actual traversal, evaluation and execution
	/**
	 * @param poDictionary
	 * @param poDimensionTags
	 * @return
	 */
	public GIPSYType execute(Dictionary poDictionary, GIPSYContext poDimensionTags);

	/**
	 * Assume GEER is set.
	 * @return
	 */
	public GIPSYType execute();
	
	/**
	 * @param poRoot
	 * @param paoContext
	 * @param piIndent
	 * @return
	 * @throws GEEException
	 */
	public GIPSYType eval(SimpleNode poRoot, GIPSYContext[] paoContext, int piIndent) 
	throws GEEException;

	/**
	 * Assumes GEER is set.
	 * @return
	 * @throws GEEException
	 */
	public GIPSYType eval() 
	throws GEEException;
}

// EOF
