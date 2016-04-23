package gipsy.apps.memocode.genome;

import gipsy.Configuration;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcherException;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.DWT.DWTWrapper;
import gipsy.interfaces.LocalDemandStore;
import gipsy.interfaces.LocalGEERPool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import marf.util.BaseThread;


/**
 * PS-DWT for the Genome Alignment Problem.
 * @author Serguei Mokhov
 * @version $Id: AlignDWT.java,v 1.6 2012/04/08 01:10:37 mokhov Exp $
 */
public class AlignDWT
extends DWTWrapper
{
	/**
	 * Stopping condition. 
	 */
	protected boolean bIsWorking = false;

	/**
	 * Currently used aligner.
	 */
	protected IAlign oSquenceAligner = new AlignJNAWrapper();

	public AlignDWT()
	{
		super();
	}

	public AlignDWT(Configuration poTierConfig)
	{
		super(poTierConfig);
	}

	public AlignDWT
	(
		LocalGEERPool poLocalGEERPool,
		LocalDemandStore poLocalDemandStore,
		EDMFImplementation poDMFImp,
		Configuration poConfiguration
	)
	{
		super(poLocalGEERPool, poLocalDemandStore, poDMFImp, poConfiguration);
	}

	public AlignDWT(LocalGEERPool poGEERPool)
	{
		super(poGEERPool);
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.DWT.DWTWrapper#run()
	 */
	@Override
	public void run()
	{
		this.bIsWorking = true;
		
		IDemand oDemand = null; 
		IDemand oResult = null;
		
		while(this.bIsWorking)
		{
			try 
			{
				// XXX: presumption that each demand has a single read sequence, so the returned
				//      match will always be one for now
				{
					oDemand = (IDemand)this.oDemandDispatcher.readDemand();
	
					byte[] atSequence = (byte[])oDemand.getSignature().getSignature();
					
					Match[] aoMatchesFound = this.oSquenceAligner.match(atSequence);
	
					if(aoMatchesFound == null || aoMatchesFound.length == 0)
					{
						System.out.println("Found zero matches for sequence " + oDemand.getSignature());
					}
					else
					{
						System.out.println("Found matches: " + aoMatchesFound.length);
						
						for(Match oMatch: aoMatchesFound)
						{
							System.out.println("Match: " + oMatch);
						}
						
						oResult = oDemand;
						oResult.setState(DemandState.COMPUTED);
						oResult.storeResult(aoMatchesFound[0]);
						
						this.oDemandDispatcher.writeResult(oDemand.getSignature(), oResult);
					}
				}
			} 
			catch(DemandDispatcherException oException) 
			{
				oException.printStackTrace(System.err);
			} 
		}
	}
	
	
	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.DGT.DGTWrapper#startTier()
	 */
	@Override
	public void startTier()
	throws MultiTierException
	{
		super.startTier();
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.DGT.DGTWrapper#stopTier()
	 */
	@Override
	public void stopTier()
	throws MultiTierException
	{
		this.bIsWorking = false;
		super.stopTier();
	}

	/**
	 * For testing and main bootstrapping.
	 * @param argv
	 * @throws Exception
	 */
	public static void main(String[] argv)
	throws Exception
	{
		AlignDWT oWorkerTier = new AlignDWT(); 
		
		// Temporary test at the local worker tier
		{
			FileInputStream oFIS = new FileInputStream(new File(IAlign.SHORT_SEQUENCES_FILE));
			BufferedInputStream oBIS = new BufferedInputStream(oFIS);
	
			// Quick test on the short sequences file which has 320 bytes (10 sequences)
			byte[] atSequences = new byte[320];
			
			int iReadRetVal = oBIS.read(atSequences, 0, atSequences.length);
			
			System.out.println("iReadRetVal = " + iReadRetVal);
			
			oWorkerTier.oSquenceAligner.match(atSequences);
	
			oBIS.close();
			oFIS.close();
		}

		BaseThread oWorkerThread = new BaseThread(oWorkerTier);
		oWorkerThread.start();
		oWorkerThread.join();
	}
}

// EOF
