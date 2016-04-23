package gipsy.GEE.multitier.DST;

import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jms.*;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.IMultiTierWrapper;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.TierFactory;
import gipsy.util.GIPSYRuntimeException;


/**
 * The Demand Store Tier Factory class. Following the Factory pattern, allows for the 
 * creation of Demand Store Tier Instances on a GIPSY node, as called by the DSTController.  
 * 
 * @author Bin Han
 * @author Joey Paquet
 *
 * @version $Id: DSTFactory.java,v 1.10 2010/12/28 16:09:01 ji_yi Exp $
 */
public class DSTFactory
extends TierFactory
{
	private DSTWrapper oDSTWrapper = null;
	
	@Override
	public IMultiTierWrapper createTier(EDMFImplementation poDMFImp)
	throws MultiTierException
	{
		try
		{
			if (this.oDSTWrapper == null)
			{
				this.oDSTWrapper = new DSTWrapper();
			}
			
			switch(poDMFImp)
			{
				case JINI:
				{
					this.oDSTWrapper.setTransportAgent(new JMSTransportAgent());
					break;
				}
				case JMS:
				{
					JINITransportAgent oTAWrapper = null;
					oTAWrapper = new JINITransportAgent();
					this.oDSTWrapper.setTransportAgent(oTAWrapper.createProxy());
					break;
				}
				case RMI:
				{
	//				Under construction.
					break;
				}
				case MULTI_THREADED:
				{
	//				Under construction.
					break;
				}
				
				default:
				{
					// XXX: review null assignment
					this.oDSTWrapper = null;
					throw new GIPSYRuntimeException("Unknown DMF Implementation Instance Type: " + poDMFImp);
				}
			}
			
			return this.oDSTWrapper;
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			throw new DSTException(e);
		}
	}
}

// EOF
