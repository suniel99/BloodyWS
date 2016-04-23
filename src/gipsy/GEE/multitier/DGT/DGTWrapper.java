package gipsy.GEE.multitier.DGT;

import gipsy.Configuration;
import gipsy.GEE.GEE;
import gipsy.GEE.GEEException;
import gipsy.GEE.IDP.DemandDispatcher.DemandDispatcherException;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.DemandGenerator.IDemandGenerator;
import gipsy.GEE.IDP.DemandGenerator.LegacyEductiveInterpreter;
import gipsy.GEE.IDP.DemandGenerator.LegacyInterpreter;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JiniDemandDispatcher;
import gipsy.GEE.IDP.DemandGenerator.jms.JMSDemandDispatcher;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.GenericTierWrapper;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.interfaces.GEERSignature;
import gipsy.interfaces.GIPSYProgram;
import gipsy.interfaces.LocalDemandStore;
import gipsy.interfaces.LocalGEERPool;
import gipsy.lang.GIPSYType;
import gipsy.tests.GEE.IDP.demands.TestDemandFactory;
import gipsy.tests.GEE.simulator.DGT;
import gipsy.util.GIPSYRuntimeException;

import java.net.InetAddress;
import java.util.UUID;

import marf.util.BaseThread;


/**
 * Implementation class of <code>DemandGeneratorTier</code> extending
 * <code>GenericTierWrapper</code> and implements <code>IMultiTierWrapper</code> interface.
 * 
 * The Demand Generator Tier generates intensional demands and procedural demands according 
 * to an initial demand and the program declarations stored in the GEER generated for this 
 * GIPSY program. Some of the demands generated locally will be dispatched through the 
 * Demand Store Tier to be further processed by other generators or workers. 
 * 
 * The Demand Generator Tier uses a <code>LocalGEERPool</code> that contains the GEER 
 * instances that this Demand Generator Tier instance can process demands for. 
 * If a Demand Generator Tier receives a demand to be processed for a GEER that is not 
 * in its <code>LocalGEERPool</code>, it may send a resource demand for it. 
 * 
 * @author Bin Han
 * @author Yi Ji
 * @author Serguei Mokhov
 * 
 * @version $Id: DGTWrapper.java,v 1.39 2012/06/16 03:10:43 mokhov Exp $
 * @since
 * 
 * @see gipsy.GEE.multitier.GenericTierWrapper
 */
public class DGTWrapper
extends GenericTierWrapper
{	
	/*
	 * ----------------------------
	 * Configuration property names
	 * ----------------------------
	 */

	/**
	 * 
	 */
	public static final String DEMAND_DISPATCHER_IMPL = "gipsy.GEE.multitier.DGT.DemandDispatcher.impl";
	
	/**
	 * Property of a class name for the demand generation.
	 * @see LegacyEductiveInterpreter 
	 * @see LegacyInterpreter
	 * @see TestDemandFactory
	 */
	public static final String DEMAND_GENERATOR_IMPL = "gipsy.GEE.multitier.DGT.DemandGenerator.impl";
	
	/**
	 * This is a configuration known by both Manager and the DGTWrapper 
	 * because the TA can be changed later if the DST is changed.
	 */
	public static final String TA_CONFIG = "gipsy.GEE.multitier.DGT.DemandDispatcher.TAConfig";
	
	/**
	 * Local GEER pool.
	 */
	protected LocalGEERPool oGEERPool;
	
	/**
	 * Local Demand Store.
	 */
	protected LocalDemandStore oLocalDemandStore; 
	
	/**
	 * "Upper-half". 
	 */
	public IDemandGenerator oDemandGenerator = null;

	/**
	 * @param poConfiguration
	 */
	public DGTWrapper(Configuration poConfiguration)
	{
		super.setConfiguration(poConfiguration);
	}
	
	/**
	 * @param poLocalGEERPool
	 * @param poLocalDemandStore
	 * @param poDemandDispatcher
	 * @param poConfiguration
	 */
	public DGTWrapper
	(
		LocalGEERPool poLocalGEERPool,
		LocalDemandStore poLocalDemandStore,
		IDemandDispatcher poDemandDispatcher,
		Configuration poConfiguration
	)
	throws GEEException
	{
		super.setConfiguration(poConfiguration);
		this.oGEERPool = poLocalGEERPool;
		this.oLocalDemandStore = poLocalDemandStore;
		this.oDemandDispatcher = poDemandDispatcher;
		
		configureDemandGenerator();
	}
	
	/**
	 * Newest constructor.
	 * 
	 * @param poLocalGEERPool
	 * @param poLocalDemandStore
	 * @param poDMFImplementation
	 * @param poConfiguration
	 */
	public DGTWrapper
	(
		LocalGEERPool poLocalGEERPool,
		LocalDemandStore poLocalDemandStore,
		EDMFImplementation poDMFImplementation,
		Configuration poConfiguration
	)
	throws GEEException
	{
		super.setConfiguration(poConfiguration);
		this.oGEERPool = poLocalGEERPool;
		this.oLocalDemandStore = poLocalDemandStore;
		
		configureDemandDispatcher(poDMFImplementation);
		configureDemandGenerator();
	}
	
	/**
	 * Temp constructor.
	 */
	public DGTWrapper()
	throws GEEException
	{
		System.out.println("gipsy.GEE.multitier.DGTWrapper()");
		this.oGEERPool = new LocalGEERPool();
		this.oLocalDemandStore = new LocalDemandStore();
		
		configureDemandDispatcher(EDMFImplementation.JINI);
		configureDemandGenerator();
		System.out.println("gipsy.GEE.multitier.DGTWrapper() DONE");
	}
	
	/**
	 * Inheritance from Runnable.
	 * @see gipsy.GEE.IDP.DemandGenerator.jms.DemandGenerator.main(String[] args)
	 */
	public void run()
	{	
//		DepositDemand oDeposit = new DepositDemand();
//		oDeposit.start();
//		
//		WithdrawResult oWithdraw = new WithdrawResult();
//		oWithdraw.start();
//		
//		try
//		{
//			oDeposit.join();
//			oWithdraw.join();
//		}
//		catch(Exception e)
//		{
//			throw new GIPSYRuntimeException(e);
//		}
	}
	
	/**
	 * Start the tier instance.
	 */
	@Override
	public void startTier()
	throws MultiTierException
	{	
		System.out.println("gipsy.GEE.multitier.DGTWrapper.startTier()");

		try
		{
			if(true)
			{
				System.out.println("gipsy.GEE.multitier.DGTWrapper.startTier(): oGEERPool: " + this.oGEERPool);
				
				// Generator/dispatcher link startup
				for(GEERSignature oGEERSignature: this.oGEERPool.getAll().keySet())
				{
					System.out.println("DGTWrapper Generator: " + this.oDemandGenerator);
					System.out.println("DGTWrapper processing signature: " + oGEERSignature);
					this.oDemandGenerator.setGEER(this.oGEERPool.get(oGEERSignature));
					//GIPSYType oValue = this.oDemandGenerator.eval();
					GIPSYType oValue = this.oDemandGenerator.execute();
					System.out.println("DGTWrapper Generator/Dispatcher combo result: " + oValue);

					// Compute the 2nd time; should come from a DST.
					oValue = this.oDemandGenerator.execute();
					System.out.println("DGTWrapper Generator/Dispatcher combo result 2: " + oValue);
				}
			}
			else
			{
				// Simulator startup
				DGT.main(null);
			}
		}
		/*catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}*/
		finally
		{
			
		}
	}
	
	/**
	 * Stop the tier instance.
	 */
	@Override
	public void stopTier()
	throws MultiTierException
	{	
		System.out.println("gipsy.GEE.multitier.DGTWrapper.stopTier()");
	} 
	
	//Encapsulate methods for LocalDemandStore;
	public boolean containsDemand(DemandSignature poDemandSignature)
	{
		return this.oLocalDemandStore.contains(poDemandSignature);
	}
	
	public IDemand getDemand(DemandSignature poDemandSignature)
	{
		return this.oLocalDemandStore.get(poDemandSignature);
	}
	
	public void putDemand(DemandSignature poDemandSignature, IDemand poDemand)
	{
		this.oLocalDemandStore.put(poDemandSignature, poDemand);
	}
	
	//Encapsulate methods for LocalGEERPool;
	public boolean containsGEER(GEERSignature poGEERSignature)
	{
		return this.oGEERPool.contains(poGEERSignature);
	}

	/**
	 * Send resource demand for new GEER not in Local GEE Pool.
	 * not done yet
	 */
	public GIPSYProgram getGEER(GEERSignature poGEERSignature)
	{
		System.out.println("gipsy.GEE.multitier.DGTWrapper.requestGEER()");
		return this.oGEERPool.get(poGEERSignature);
	}
	
	public void putGEER(GEERSignature poGEERSignature, GIPSYProgram poGIPSYProgram)
	{
		this.oGEERPool.put(poGEERSignature, poGIPSYProgram);
	}
	
	private void configureDemandDispatcher(EDMFImplementation poDMFImp)
	throws GEEException
	{
		switch(poDMFImp)
		{
			case JINI:
			{
				this.oDemandDispatcher = new JiniDemandDispatcher();
				break;
			}	
			case JMS:
			{
				this.oDemandDispatcher = new JMSDemandDispatcher();
				break;
			}
			default:
			{
				this.oDemandDispatcher = null;
				throw new GIPSYRuntimeException("Unknown DMF Implementation Instance Type: " + poDMFImp);
			}
		}
	}

	/**
	 * Configure the Generator half of the DGT.
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private void configureDemandGenerator()
	throws GEEException
	{
		try
		{
			String strGeneratorClassName = this.oConfiguration.getProperty(DEMAND_GENERATOR_IMPL);
			
			System.out.println("Generator class for coniguration: " + strGeneratorClassName);
			
			// Set the default if no value available
			if(strGeneratorClassName == null)
			{
				strGeneratorClassName = LegacyEductiveInterpreter.class.getName();
				this.oConfiguration.setProperty(DEMAND_GENERATOR_IMPL, strGeneratorClassName);

				System.out.println("Assuming generator class: " + strGeneratorClassName);
			}
			
			// Locate the generator class and make its instance.
			Class<?> oDispatcherClass = Class.forName(strGeneratorClassName);
			this.oDemandGenerator = (IDemandGenerator)oDispatcherClass.newInstance();

			System.out.println("Generator instance: " + this.oDemandGenerator);
			
			// Link up with the dispatcher
			this.oDemandGenerator.setDemandDispatcher(this.oDemandDispatcher);
			System.out.println("Dispatcher instance: " + this.oDemandDispatcher);
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			throw new GEEException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see gipsy.GEE.multitier.GenericTierWrapper#equals(java.lang.Object)
	 */
	public boolean equals(Object poDGT)
	{
		if(poDGT instanceof DGTWrapper)
		{
			DGTWrapper oDGT = (DGTWrapper)poDGT;

			return this.oGEERPool.equals(oDGT.oGEERPool)
				&& this.oLocalDemandStore.equals(oDGT.oLocalDemandStore)
				&& super.equals((GenericTierWrapper)poDGT);
		}
		else
		{
			return false;
		}
	}
	
	public static void main(String[] args)
	throws Exception
	{
//		DGTWrapper oDGTWrapper = new DGTWrapper();
//		oDGTWrapper.startTier();

		/*Test case for intensional demand
		TestDemandFactory oDemandFactory = TestDemandFactory.getInstance();	
		LocalDemandStore oLocalDemandStore_1 = new LocalDemandStore();
		LocalGEERPool oLocalGEERPool_1 = new LocalGEERPool();
		
		IntensionalDemand oI_Demand_1 = oDemandFactory.createIntensionalDemand();
		DemandSignature oDemandSignature_1 = new DemandSignature(UUID.randomUUID()+"@"+InetAddress.getLocalHost().getHostAddress());
		oI_Demand_1.setSignature(oDemandSignature_1);
		IntensionalDemand oI_Demand_2 = oDemandFactory.createIntensionalDemand();
		DemandSignature oDemandSignature_2 = new DemandSignature(UUID.randomUUID()+"@"+InetAddress.getLocalHost().getHostAddress());
		oI_Demand_2.setSignature(oDemandSignature_2);
		
		oLocalDemandStore_1.put(oI_Demand_1.getSignature(), oI_Demand_1);
		oLocalDemandStore_1.put(oI_Demand_2.getSignature(), oI_Demand_2);
		oLocalDemandStore_1.printDemand();
		
		DGTWrapper oDGTWrapper_1 = new DGTWrapper(oLocalGEERPool_1, oLocalDemandStore_1, EDMFImplementation.JINI, new Configuration());
		Thread oWrapper = new Thread(oDGTWrapper_1);
		
		oWrapper.start();		
		oWrapper.join();
		*/
		
		TestDemandFactory oDemandFactory = TestDemandFactory.getInstance();
		
		IDemand oP_Demand_1 = oDemandFactory.createProceduralDemand();
		DemandSignature oDemandSignature_1 = new DemandSignature(UUID.randomUUID()+"@"+InetAddress.getLocalHost().getHostAddress());
		oP_Demand_1.setSignature(oDemandSignature_1);
		
		IDemand oP_Demand_2 = oDemandFactory.createProceduralDemand();
		DemandSignature oDemandSignature_2 = new DemandSignature(UUID.randomUUID()+"@"+InetAddress.getLocalHost().getHostAddress());
		oP_Demand_2.setSignature(oDemandSignature_2);
		
		LocalDemandStore oLocalDemandStore_1 = new LocalDemandStore();
		oLocalDemandStore_1.put(oDemandSignature_1, oP_Demand_1);
		oLocalDemandStore_1.put(oDemandSignature_2, oP_Demand_2);
		oLocalDemandStore_1.printDemand();
		
		LocalGEERPool oLocalGEERPool_1 = new LocalGEERPool();
		DGTWrapper oDGTWrapper_1 = new DGTWrapper(oLocalGEERPool_1, oLocalDemandStore_1, EDMFImplementation.JINI, new Configuration());
		BaseThread oWrapper = new BaseThread(oDGTWrapper_1);
		
		oWrapper.start();		
		oWrapper.join();
	}

	/**
	 * @author Bin Han
	 */
	private class DepositDemand
	extends BaseThread
	{
		public void run()
		{
			System.out.println("Inner DepositDemand running.");
			
			while(true)
			{
				if(DGTWrapper.this.oLocalDemandStore != null)
				{
					// Traverse local demand store.
					for(DemandSignature oDemandSignature: DGTWrapper.this.oLocalDemandStore.getKeySet())
				    {
				        IDemand oDemand = DGTWrapper.this.oLocalDemandStore.get(oDemandSignature);
				        
				        // Compute the intensional demand in DGT
				        // and allocate it back to the local demand store.
				        if(oDemand.getType().isIntensional() && oDemand.getState().isPending())
				        {
				        	GIPSYProgram oGEER = (GIPSYProgram)oDemand.getResult();
							GIPSYType oResult = new GEE(oGEER).eval(oGEER.getContextValue());
//Testing for DWTWrapper
//IDemand oResult = oDemand.execute(); 
							oDemand.storeResult(oResult);
							oDemand.setState(DemandState.COMPUTED);
							
							System.out.println(oDemandSignature + " is computed. The result is: " + oDemand.getResult());
							
							DGTWrapper.this.oLocalDemandStore.put(oDemandSignature, oDemand);

							System.out.println(oDemandSignature + " is computed and put back in the local demand store.");
							
							//print demand, testing purpose
							DGTWrapper.this.oLocalDemandStore.printDemand();
							
							try
							{
								DGTWrapper.this.oDemandDispatcher.writeDemand(oDemand);
							}
							catch (DemandDispatcherException e)
							{
								e.printStackTrace(System.err);
							}
							
							System.out.println(oDemandSignature+" is computed and put in DST.");
				        }
				        
				        // Send the pending demand to Demand Store, set its state to processing.
				        else if(oDemand.getState().isPending())
				        {   	
							// Now send it to the store
							try
							{
								DemandSignature oSignature = DGTWrapper.this.oDemandDispatcher.writeDemand(oDemand);
								System.out.println(oSignature + " is put in DST.");
								
								oDemand.setState(DemandState.INPROCESS);
								
								DGTWrapper.this.oLocalDemandStore.put(oSignature, oDemand);
								System.out.println(oSignature+" is inprocess and put back in the local demand store.");
								//print demand, testing purpose
								DGTWrapper.this.oLocalDemandStore.printDemand();
							}
							catch(DemandDispatcherException e)
							{
								e.printStackTrace(System.err);
							}
				        }
				    }
				}
				
				// gives a chance to others to run on green threads Java
				yield();
			}
		}
	}
	
	/**
	 * @author Bin Han
	 */
	private class WithdrawResult
	extends BaseThread
	{
		public void run()
		{
			System.out.println("Inner WithdrawResult running.");

			while(true)
			{
				if(DGTWrapper.this.oLocalDemandStore != null)
				{
					// Traverse local demand store.
					for(DemandSignature oDemandSignature: DGTWrapper.this.oLocalDemandStore.getKeySet())
				    {
				        IDemand oDemand = DGTWrapper.this.oLocalDemandStore.get(oDemandSignature);

				        // Request the InProcess demand from Demand Store, set its state to computed,
				        // and put back to the Local Demand Store.
				        if(oDemand.getState().isInProcess())
				        {   	
							// Get the result from Demand Store, 
							try
							{
								IDemand oWorkResult = DGTWrapper.this.oDemandDispatcher.readResultIfExists(oDemandSignature);

								if(oWorkResult != null)
								{
									System.out.println(oDemandSignature + " is retrived from DST.");

									oDemand.storeResult(oWorkResult);
									oDemand.setState(DemandState.COMPUTED);
									
									DGTWrapper.this.oLocalDemandStore.put(oDemandSignature, oDemand);
									
									System.out.println(oDemandSignature+" is computed and put in the local demand store.");
									//print the demand, only for testing.
									DGTWrapper.this.oLocalDemandStore.printDemand();
								}
							}
							catch(DemandDispatcherException e)
							{
								e.printStackTrace(System.err);
							}
				        }
				    }
				}
				
				yield();
			} // while()
		} // run()
	} // WithdrawResult
}

// EOF
