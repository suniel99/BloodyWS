package gipsy.apps.memocode.genome;

import gipsy.Configuration;
import gipsy.GEE.GEEException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcher;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcherException;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.ProceduralDemand;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.DGT.DGTWrapper;
import gipsy.GEE.multitier.DST.TAFactory;
import gipsy.interfaces.LocalDemandStore;
import gipsy.interfaces.LocalGEERPool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;

import marf.util.BaseThread;
import marf.util.Debug;


/**
 * PS-DGT for Genome Sequence Align Problem.
 * @author Serguei Mokhov
 * @version $Id: AlignDGT.java,v 1.5 2012/04/08 17:05:32 mokhov Exp $
 */
public class AlignDGT
extends DGTWrapper
{
	/**
	 * Stopping condition. 
	 */
	protected boolean bIsWorking = false;

	protected byte[] atReadSequencesToTry = null;
	
	public AlignDGT()
	throws GEEException
	{
		super();
	}

	public AlignDGT(Configuration poConfiguration)
	{
		super(poConfiguration);
	}

	public AlignDGT
	(
		LocalGEERPool poLocalGEERPool,
		LocalDemandStore poLocalDemandStore,
		EDMFImplementation poDMFImp,
		Configuration poConfiguration
	)
	throws GEEException
	{
		super(poLocalGEERPool, poLocalDemandStore, poDMFImp, poConfiguration);
	}

	public AlignDGT
	(
		LocalGEERPool poLocalGEERPool,
		LocalDemandStore poLocalDemandStore,
		IDemandDispatcher poDemandDispatcher,
		Configuration poConfiguration
	)
	throws GEEException
	{
		super(poLocalGEERPool, poLocalDemandStore, poDemandDispatcher, poConfiguration);
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.DGT.DGTWrapper#run()
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
				DemandSignature oSignature = new DemandSignature(this.atReadSequencesToTry);
				oDemand = new ProceduralDemand();
				oDemand.setSignature(oSignature);
				this.oDemandDispatcher.writeDemand(oDemand);
				
				oResult = this.oDemandDispatcher.readResult(oSignature);
				System.out.println("The computed result value: " + oResult.getResult());
				
				
				this.bIsWorking = false;
				//printOut("Demand received: name: " + oDemand.getSignature());
				
				//if (we have the resource for process procedural demand)
				//oResult = oDemand.execute();
				//printOut("Demand computed");
				//this.oLocalDemandStore.put(oDemand.getSignature(), oResult);
				
				//this.oDemandDispatcher.writeResult(oDemand.getSignature(), oResult);
				//printOut("Result dispatched");
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
		try
		{
			// Create a TA instance using the configuration
			Configuration oTAConfig = (Configuration)this.oConfiguration.getObjectProperty(DGTWrapper.TA_CONFIG);
			ITransportAgent oTA = TAFactory.getInstance().createTA(oTAConfig);
			
			// Create a DemandDispatcher instance using the configuration
			String strImplClassName = this.oConfiguration.getProperty(DGTWrapper.DEMAND_DISPATCHER_IMPL);
			Class<?> oImplClass = Class.forName(strImplClassName);
			Class<?>[] aoParamTypes = new Class[] {ITransportAgent.class};
			Constructor<?> oImplConstructor = oImplClass.getConstructor(aoParamTypes);
			Object[] aoArgs = new Object[]{oTA};
			
			this.oDemandDispatcher = (DemandDispatcher)oImplConstructor.newInstance(aoArgs);
			this.oDemandDispatcher.setTAExceptionHandler(this.oTAExceptionHandler);
			
			new BaseThread(this).start();
		}
		catch(MultiTierException oException)
		{
			throw oException;
		}
		catch(Exception oException)
		{
			throw new MultiTierException(oException);
		}
	}

	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.DGT.DGTWrapper#stopTier()
	 */
	@Override
	public void stopTier()
	throws MultiTierException
	{
		this.bIsWorking = false;
	}

	/**
	 * @param argv
	 * @throws Exception
	 */
	public static void main(String[] argv)
	throws Exception
	{
		Debug.enableDebug();
		
		String strSequenceFilename = "";
		
		// In pairs
		int iSequenceLength = 0;
		
		try
		{
			strSequenceFilename = argv[0];
		}
		catch(Exception e)
		{
			System.err.println("Please supply read sequences filename.");
			System.exit(1);
		}
		
		try
		{
			iSequenceLength = Integer.parseInt(argv[1]);
		}
		
		// No argument was supplied for this one, so we assume the default.
		catch(ArrayIndexOutOfBoundsException e)
		{
			iSequenceLength = IAlign.DEFAULT_SEQUENCE_LENGTH;
		}

		catch(Exception e)
		{
			System.err.println(e);
			e.printStackTrace(System.err);
			System.exit(1);
		}

		if(iSequenceLength <= 0)
		{
			System.err.println("Squence length (" + iSequenceLength + ") should be positive.");
			System.exit(1);
		}
		
		/* Pad the sequences out to a multiple of SEQUENCE_ALIGN bytes */
		int iPaddedSequenveLengthBytes
			= ((iSequenceLength + IAlign.SEQUENCE_ALIGN * 4 - 1)
			& ~(IAlign.SEQUENCE_ALIGN * 4 - 1)) >> 2;
		
		FileInputStream oFIS = new FileInputStream(new File(strSequenceFilename));
		BufferedInputStream oBIS = new BufferedInputStream(oFIS);

		// Quick test on the short sequences file which has 320 bytes (10 sequences)
		//byte[] atSequences = new byte[320];
		byte[] atSequences = new byte[32];
		
		int iReadRetVal = oBIS.read(atSequences, 0, atSequences.length);
		
		System.out.println("iReadRetVal = " + iReadRetVal);
		
		//oWorkerTier.oSquenceAligner.match(atSequences);
		AlignDGT oDGT = new AlignDGT();
		oDGT.atReadSequencesToTry = atSequences;
		oDGT.startTier();
		oDGT.wait();

		oBIS.close();
		oFIS.close();
	}
}

// EOF
