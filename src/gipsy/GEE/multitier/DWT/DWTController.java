package gipsy.GEE.multitier.DWT;

import java.util.HashMap;
import java.util.Map;

import gipsy.Configuration;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.IMultiTierWrapper;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.NodeController;
import gipsy.GEE.multitier.TierWrapperFactory;

/**
 * Demand Worker Tier Controller class. On each GIPSY node, one of each type of controller 
 * is active, which allows to call the factory to create tiers of the respective type, 
 * and later control tiers of this type on a GIPSY node.   
 * 
 * @author BinHan
 * @author paquet
 * 
 * @version $Id: DWTController.java,v 1.7 2010/12/28 16:09:03 ji_yi Exp $
 */
public class DWTController
extends NodeController
{
	
	private Map<String, DWTWrapper> oActiveTiers = new HashMap<String, DWTWrapper>();
	private long lTierInstanceCounter = 0;
	
	public DWTController()
	{
		// We create a tier-specific factory, while the list of
		// wrappers container is instantiated in the parent class,
		// and is filled in with tier=specific wrappers in the children,
		// like DWTWrapper in here.
		this.oTierFactory = new DWTFactory();
//		Collection class downcast function.
//		this.oTierList = Collections.synchronizedList(new ArrayList<DSTWrapper>());
	}

	@Override
	public void addTier(EDMFImplementation poDMFImp)
	{
		try
		{
			this.oTierList.add(this.oTierFactory.createTier(poDMFImp));
		}
		// XXX: for now the exception handling is here; see if it has to
		//      move to the GIPSYNode when it is created
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}

	@Override
	public void removeTier(EDMFImplementation poDMFImp)
	{
//		Under construction.
	}

	@Override
	public void addTier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IMultiTierWrapper addTier(Configuration poTierConfig)
	throws MultiTierException 
	{
		try
		{
			DWTWrapper oDWT = (DWTWrapper) TierWrapperFactory.getInstance().createTier(poTierConfig);
			this.oActiveTiers.put(oDWT.getTierID(), oDWT);
			oDWT.setTierID("" + this.lTierInstanceCounter);
			this.lTierInstanceCounter++;
			oDWT.setTAExceptionHandler(this.oTAExceptionHandler);
			return oDWT;
		}
		catch(Exception oException)
		{
			throw new MultiTierException(oException);
		}
	}
	
	
	
}

// EOF
