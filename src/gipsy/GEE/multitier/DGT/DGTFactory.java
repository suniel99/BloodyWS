package gipsy.GEE.multitier.DGT;

import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jms.*;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.IMultiTierWrapper;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.TierFactory;
import gipsy.util.GIPSYRuntimeException;


/**
 * The Demand Generator Tier Factory class. Following the Factory pattern, allows for the 
 * creation of Demand Generator Tier instances on a GIPSY node, as called by the DGTController.  
 * 
 * @author Bin Han
 * @author Serguei Mokhov
 * @author Joey Paquet
 * @version $Id: DGTFactory.java,v 1.9 2010/12/09 04:30:56 mokhov Exp $
 * @since
 */
public class DGTFactory
extends TierFactory
{
	/**
	 * XXX 
	 */
	private DGTWrapper oDGTWrapper = null;

	@Override
	public IMultiTierWrapper createTier(EDMFImplementation poDMFImp)
	throws MultiTierException
	{
		try
		{
			// XXX: this effectively allows only one real instance
			if(this.oDGTWrapper == null)
			{
				this.oDGTWrapper = new DGTWrapper();
			}
			
			switch(poDMFImp)
			{
				case JMS:
				{
					this.oDGTWrapper.setTransportAgent(new JMSTransportAgent());
					break;
				}

				case JINI:
				{
					JINITransportAgent oTAWrapper = null;
					oTAWrapper = new JINITransportAgent();
					this.oDGTWrapper.setTransportAgent(oTAWrapper.createProxy());
					break;
				}
				
				case RMI:
				{
//					Under construction.
					break;
				}
				
				case MULTI_THREADED:
				{
//					Under construction.
					break;
				}
				
				default:
				{
					// XXX: review null assignment
					this.oDGTWrapper = null;
					throw new GIPSYRuntimeException("Unknown DMF Implementation Instance Type: " + poDMFImp);
				}
			}

			return this.oDGTWrapper;
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			throw new DGTException(e);
		}
	}
}

// EOF
