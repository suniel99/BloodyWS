package gipsy.GEE.multitier.DWT;

import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jms.*;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.IMultiTierWrapper;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.TierFactory;
import gipsy.util.GIPSYRuntimeException;


/**
 * The Demand Worker Tier Factory class. Following the Factory pattern, allows for the 
 * creation of Demand Worker Tier Instances on a GIPSY node, as called by the DWTController.  
 * 
 * @author Bin Han
 * @author Yi Ji
 *
 * @version $Id: DWTFactory.java,v 1.7 2012/04/04 13:40:20 mokhov Exp $
 */
public class DWTFactory
extends TierFactory
{
	private DWTWrapper oDWTWrapper = null;

	@Override
	public IMultiTierWrapper createTier(EDMFImplementation poDMFImp)
	throws MultiTierException
	{
		try
		{
			if(this.oDWTWrapper == null)
			{
				this.oDWTWrapper = new DWTWrapper(); 
			}
	
			switch(poDMFImp)
			{
				case JMS:
				{
					this.oDWTWrapper.setTransportAgent(new JMSTransportAgent());
					break;
				}
				
				case JINI:
				{
					JINITransportAgent oTAWrapper = null;
					oTAWrapper = new JINITransportAgent();
					this.oDWTWrapper.setTransportAgent(oTAWrapper.createProxy());
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
					this.oDWTWrapper = null;
					throw new GIPSYRuntimeException("Unknown DMF Implementation Instance Type: " + poDMFImp);
				}
			}
			
			return this.oDWTWrapper;
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			throw new DWTException(e);
		}
	}
}

// EOF
