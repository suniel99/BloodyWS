package gipsy.tests.GEE.IDP.DemandStore;

import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITA;
import gipsy.tests.GEE.IDP.DemandGenerator.jini.rmi.DemandWorker;

/**
 * Test how many concurrent connections are supported by the store.
 * Note that the size network backlog queue may be overflowed by
 * concurrent connection flood, therefore the speed of thread-launch is
 * adjusted to minimize the backlog impact.
 * 
 * @author Yi Ji
 * @version $Id: ConnectionCapacityTest.java,v 1.2 2010/08/12 18:05:24 ji_yi Exp $
 */
public class ConnectionCapacityTest 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		int iMaxNum = 6000;
		int iSlowdownThreshold = 200;
		
		try 
		{
			ITransportAgent oTA = new JINITA();
			
			DemandWorker[] aoWorkers = new DemandWorker[iMaxNum];
			
			for(int i = 0; i < iMaxNum; i++) 
			{
				aoWorkers[i] = new DemandWorker(oTA);
				aoWorkers[i].iID = i;
			}
			
			for(int i = 0; i < iMaxNum; i++)
			{
				aoWorkers[i].start();
				System.out.println("worker " + i + " started");
				
				if(i>iSlowdownThreshold) 
				{
					if(i%50 == 0)
						Thread.sleep(1000);
				}
			}
			
			for(int i = 0; i<5; i++) 
			{
				Thread.sleep(20*iMaxNum);
				System.out.println("\n Error count = " + DemandWorker.siErrorCount
						+ " Error rate = " + (double)DemandWorker.siErrorCount/(double)iMaxNum);
			}
			
			for(Thread oWorker : aoWorkers) 
			{
				oWorker.join();
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace(System.err);
		}
		
	}

}
