package gipsy.GEE.multitier.DGT;

import gipsy.GEE.GEEException;
import gipsy.GEE.IDP.demands.Demand;
import gipsy.GEE.multitier.GenericTierWrapper;

/**
 * Traverse the AST in the GEER, process Intensional Demand, apply decorator pattern and template pattern.
 * @author Bin Han
 * @version $Id: IntensionalDemandProcessor.java,v 1.3 2010/12/09 04:36:58 mokhov Exp $
 */
public class IntensionalDemandProcessor
extends DGTWrapper
{
	private GenericTierWrapper oDGTWrapper;

	public IntensionalDemandProcessor()
	throws GEEException
	{
		super();
	}

	private Demand processIntensionalDemand(Demand poDemand)
	{
		// Reserve for the Engine;
		return null;
	}
	
	public void run()
	{
		//template pattern
	}

	@Override
	public void startTier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopTier() {
		// TODO Auto-generated method stub
		
	}
}