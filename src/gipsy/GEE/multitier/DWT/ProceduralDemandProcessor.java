package gipsy.GEE.multitier.DWT;

import gipsy.GEE.IDP.demands.Demand;
import gipsy.GEE.multitier.GenericTierWrapper;

/**
 * Receive Procedural Demand from TA, then search the Local GEER Pool for the corresponding 
 * <code>ProcedureClass</code>. If it does not have the corresponding <code>ProcedureClass</code>, 
 * issues a resource demand to get it, apply decorator pattern and template pattern. 
 * 
 * @author BinHan
 * @version $Id: ProceduralDemandProcessor.java,v 1.3 2010/02/02 00:22:35 bin_ha Exp $
 */
public class ProceduralDemandProcessor
extends DWTWrapper
{
	private GenericTierWrapper oDWTWrapper;
	
	private Demand processProceduralDemand(Demand poDemand)
	{
		// The computing unit, reserve for the Engine;
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
