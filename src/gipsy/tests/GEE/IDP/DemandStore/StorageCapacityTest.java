package gipsy.tests.GEE.IDP.DemandStore;

import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITA;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.tests.GEE.IDP.DemandGenerator.jini.rmi.DemandSender;
import gipsy.tests.GEE.IDP.demands.LargeByteArrayDemand;

/**
 * Test the storage capacity of the store by continuously adding
 * demands to the store.
 * 
 * @author Yi Ji
 * @version $Id: StorageCapacityTest.java,v 1.2 2010/08/12 18:05:24 ji_yi Exp $
 */
public class StorageCapacityTest 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try 
		{
			ITransportAgent oTA = new JINITA();
			
			int iMaxNum = 250*40;
			
			// Each byte occupies 4B memory locally, but only takes 1B as promised in the remote store
			IDemand oDemand = new LargeByteArrayDemand(1024);
			
			DemandSender[] aoSenders = new DemandSender[iMaxNum];
			
			for(int i = 0; i< iMaxNum; i++)
			{
				oDemand.setSignature(new DemandSignature(i));
				aoSenders[i] = new DemandSender(oDemand, oTA);
				aoSenders[i].iID = i;
			}
			
			for(int i = 0; i< iMaxNum; i++)
			{
				aoSenders[i].start();
				if(i%50 == 0)
				{
					Thread.sleep(1000);
				}
			}
			
			for(Thread oSender : aoSenders)
			{
				oSender.join();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace(System.err);
		}
	}

}
