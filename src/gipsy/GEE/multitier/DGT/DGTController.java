package gipsy.GEE.multitier.DGT;

import gipsy.Configuration;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.IMultiTierWrapper;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.NodeController;
import gipsy.GEE.multitier.TierWrapperFactory;
import gipsy.util.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Demand Generator Tier Controller class. On each GIPSY node, one of each type of controller 
 * is active, which allows to call the factory to create tiers of the respective type, 
 * and later control tiers of this type on a GIPSY node.
 * 
 * @author Bin Han
 * @author Serguei Mokhov
 * @author Joey Paquet
 * 
 * @version $Id: DGTController.java,v 1.13 2012/06/15 14:23:04 mokhov Exp $
 * @since
 */
public class DGTController
extends NodeController
{
	/**
	 * Maps DGT ID to its instance. 
	 */
	private Map<String, DGTWrapper> oActiveDGTs = new HashMap<String, DGTWrapper>();

	private long lTierInstanceCounter = 0;
	
	/**
	 * Constructor.
	 */
	public DGTController()
	{
		this.oTierFactory = new DGTFactory();
//		Collection class downcast function.
//		this.oTierList = Collections.synchronizedList(new ArrayList<DGTWrapper>());
//		this.oTierList = Collections.synchronizedList(new ArrayList<DGTWrapper>());
	}

	/* 
	 * @see gipsy.GEE.multitier.NodeController#addTier(gipsy.GEE.multitier.DMFImp)
	 */
	public void addTier(EDMFImplementation poDMFImp)
	{
		//this.oTierList.add(this.oTierFactory.createTier(poDMFImp));
		//new tier.run;
		throw new NotImplementedException("addTier()");
	}

	public void addTier()
	{
		//this.oTierList.add(this.oTierFactory.createTier(poDMFImp));
		//XXX:
		addTier(EDMFImplementation.JMS);
	}

	public IMultiTierWrapper addTier(Configuration poTierConfig)
	throws MultiTierException
	{
		try
		{
			DGTWrapper oDGT = (DGTWrapper) TierWrapperFactory.getInstance().createTier(poTierConfig);
			this.oActiveDGTs.put(oDGT.getTierID(), oDGT);
			oDGT.setTierID("" + this.lTierInstanceCounter);
			this.lTierInstanceCounter++;
			oDGT.setTAExceptionHandler(this.oTAExceptionHandler);
			return oDGT;
		}
		catch(Exception oException)
		{
			oException.printStackTrace(System.err);
			throw new MultiTierException(oException);
		}
	}
	
	/* 
	 * @see gipsy.GEE.multitier.NodeController#removeTier(gipsy.GEE.multitier.DMFImp)
	 */
	public void removeTier(EDMFImplementation poDMFImp)
	{
//		Under construction
	}

	public void removeTier()
	{
		throw new NotImplementedException("removeTier()");
	}
}

// EOF
