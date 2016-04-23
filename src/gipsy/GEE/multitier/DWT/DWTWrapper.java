package gipsy.GEE.multitier.DWT;

import gipsy.Configuration;
import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.DemandGenerator.jini.rmi.JINITA;
import gipsy.GEE.IDP.DemandGenerator.jms.JMSTransportAgent;
import gipsy.GEE.IDP.DemandWorker.DemandWorker;
import gipsy.GEE.IDP.DemandWorker.IDemandWorker;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandState;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.multitier.EDMFImplementation;
import gipsy.GEE.multitier.GenericTierWrapper;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.DST.TAFactory;
import gipsy.interfaces.GEERSignature;
import gipsy.interfaces.GIPSYProgram;
import gipsy.interfaces.LocalDemandStore;
import gipsy.interfaces.LocalGEERPool;
import gipsy.util.GIPSYRuntimeException;
import gipsy.util.NetUtils;
import gipsy.util.Trace;

import java.util.UUID;

import marf.util.BaseThread;
import marf.util.Debug;


/**
 * Implementation class of <code>DemandWorkerTier</code> extend <code>GenericTierWrapper</code>
 * and implements <code>IMultiTierWrapper</code> interface.
 * 
 * The Demand Worker Tier is a tier that can process procedural demands. It consists of a 
 * <code>ProceduralDemandProcessor</code> that can process the value of any procedural demand 
 * corresponding to one of the elements of its <code>ProcedureClassPool</code>, which represents 
 * executable code for all the procedural demands that this Demand Worker Tier instance 
 * is able to respond to. The <code>ProcedureClassPool</code> is embedded in the 
 * <code>LocalGEERPool</code>, which serves the same purpose as for the Demand Generator Tier. 
 * 
 * The <code>ProceduralDemandProcessor</code> represents the ``worker'' part of this tier. 
 * It receives pending demands from its associated <code>TransportAgent</code>, 
 * identifies what <code>ProcedureClass</code> this demand refers to 
 * by the use of the demand signature and GEERid of the demand, which is a search 
 * key in its <code>LocalGEERPool</code>. In order for the Worker to optimize it processing 
 * time, all procedural demands are wrapped in threaded classes. 
 * 
 * Similarly to the DGT, the DWT uses a <code>LocalDemandStore</code> as an output buffer 
 * to allow the accumulation of processed demands, in case of momentaneous malfunction of 
 * the <code>TransportAgent</code> while the results of demands currently being processed 
 * are created by the <code>ProceduralDemandProcessor</code>. It also uses the notion of 
 * <code>LocalGEERPool</code>, but in this case the DWT is concerned with the 
 * <code>ProcedureClasses</code> embedded in the GEER.
 *  
 * A <code>ProcedureClass<code> is a Java class embedding a procedure that can be executed 
 * by the <code>ProceduralDemandProcessor</code>. This procedure can be a Java procedure 
 * (i.e. a method), or a procedure written in any other language that can be embedded into 
 * a Java class, e.g. using JNI to declare a "wrapper" class to embed this procedure so 
 * that it can be called by the Java Virtual Machine. The procedure classes are generated 
 * by the GIPC, and included into the GEER at compile time.
 * 
 * @author Bin Han
 * @author Serguei Mokhov
 * @author Yi Ji
 * 
 * @version $Id: DWTWrapper.java,v 1.33 2012/06/17 17:13:25 mokhov Exp $
 * @since
 * 
 * @see gipsy.GEE.multitier.IMultiTierWrapper
 * @see gipsy.GEE.multitier.GenericTierWrapper
 * @see gipsy.GEE.multitier.ProceduralDemandProcessor
 */
public class DWTWrapper
extends GenericTierWrapper
{
	/**
	 * Key for the TA configuration.
	 */
	public static final String TA_CONFIG = "gipsy.GEE.multitier.DWT.TAConfig";

	/**
	 * Local GEER pool copy.
	 */
	protected LocalGEERPool oGEERPool;
	
	/**
	 * Local Demand Store.
	 * Only the computed demands are put in the store, once the demand is send to the DST will be deleted.
	 */
	protected LocalDemandStore oLocalDemandStore;
	
	/**
	 * Work doer. Via this API can be implemented by externals as work delegates.
	 */
	protected IDemandWorker oWorker;
	
	/**
	 * XXX.
	 */
	protected ITransportAgent oTransportAgent;
	
	/**
	 * For logging.
	 */
	private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";
	
	/**
	 * GEER pool constructor.
	 * @param poGEERPool
	 */
	public DWTWrapper(LocalGEERPool poGEERPool)
	{
		this.oGEERPool = poGEERPool;
		
		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MSG_PREFIX + "." + Trace.getEnclosingMethodName() + 
				"(LocalGEERPool) was called by [" +
				Trace.getCallerClassName() + "]." + Trace.getCallerMethodName() + "()"
			);
		}
	}
	
	/**
	 * Temporary constructor.
	 */
	public DWTWrapper()
	{
		this.oWorker = new DemandWorker();

		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MSG_PREFIX + "." + Trace.getEnclosingMethodName() + 
				"() was called by [" +
				Trace.getCallerClassName() + "]." + Trace.getCallerMethodName() + "()"
			);	
		}
	}
	
	@Deprecated
	public DWTWrapper(LocalGEERPool poLocalGEERPool, LocalDemandStore poLocalDemandStore, IDemandDispatcher poJiniDemandDispatcher, Configuration poConfiguration)
	{
		super(poJiniDemandDispatcher, poConfiguration);
		this.oGEERPool = poLocalGEERPool;
		this.oLocalDemandStore = poLocalDemandStore;
		this.oWorker = new DemandWorker(poLocalDemandStore, poLocalGEERPool);
	}
	
	public DWTWrapper(Configuration poTierConfig)
	{
		this.strTierID = UUID.randomUUID().toString();
		//this.oWorker = new DemandWorker();
		this.oConfiguration = poTierConfig;
	}
	
	/**
	 * Newest constructor.
	 * 
	 * @param poLocalGEERPool
	 * @param poLocalDemandStore
	 * @param poJiniDemandDispatcher
	 * @param poConfiguration
	 */
	public DWTWrapper
	(
		LocalGEERPool poLocalGEERPool,
		LocalDemandStore poLocalDemandStore,
		EDMFImplementation poDMFImplementation,
		Configuration poConfiguration
	)
	{
		this.oGEERPool = poLocalGEERPool;
		this.oLocalDemandStore = poLocalDemandStore;
		
		//XXX DGT constructor needs to be refactored the same way.
		try
		{
			switch(poDMFImplementation)
			{
				case JINI:
				{
					this.oTransportAgent = new JINITA();
					break;
				}
				
				case JMS:
				{
					this.oTransportAgent = new JMSTransportAgent();
					break;
				}
				
				default:
				{
					this.oTransportAgent = null;
					throw new GIPSYRuntimeException
					(
						"Unknown DMF Implementation Instance Type: "
						+ poDMFImplementation
					);
				}
			}
		} 
		catch(Exception e) 
		{
			e.printStackTrace(System.err);
		}
	}
	
	@Deprecated
	public void setTransportAgent(EDMFImplementation poDMFImp)
	{
//		this.oWorker.setTransportAgent(poDMFImp);
		try
		{
			switch(poDMFImp)
			{
				case JINI:
				{
					this.oTransportAgent = new JINITA();
					break;
				}
				
				case JMS:
				{
					this.oTransportAgent = new JMSTransportAgent();
					break;
				}	

				default:
				{
					this.oTransportAgent = null;
					throw new GIPSYRuntimeException("Unknown DMF Implementation Instance Type: " + poDMFImp);
				}
			}
		} 
		catch(Exception e) 
		{
			e.printStackTrace(System.err);
		}
	}
	
	/**
	 * The same idea as <code>DGTWrapper</code>, there are two inner classes,
	 * WithdrawDemand and DepositResult (different from WithdrawResult and DepositDemand).
	 * 
	 * WithdrawDemand takes pending demand from DST, computes it and put it in the LocalDemandStore.
	 */
	private class WithdrawDemand
	extends BaseThread
	{
		public void run()
		{
			System.out.println("DWTWrapper.WithdrawDemand.run()");
			DWTWrapper.this.oTransportAgent.setClientIPAddress(NetUtils.getLocalIPAddress());
			
			IDemand oDemand;
			IDemand oResult;

			while(true)
			{
				try
				{
					// Get a pending demand from the DST.
					oDemand = DWTWrapper.this.oTransportAgent.getDemand();
					System.out.println
					(
						"Demand received from DST: " + oDemand.getSignature()
						+ " demand state is: " + oDemand.getState()
					);
					
					if(oDemand.getState().isPending() && oDemand.getType().isProcedural())
					{
						// Execute demand.
						oResult = oDemand.execute();
						System.out.println(oDemand.getSignature() + " is computed, the result is: " + oResult + ".");
						
						// Store result, set demand state to computed, put in the LocalDemandStore.
						oDemand.storeResult(oResult);
						oDemand.setState(DemandState.COMPUTED);
						DWTWrapper.this.oLocalDemandStore.put(oDemand.getSignature(), oDemand);
						System.out.println(oDemand.getSignature() + " is put in the local demand store");
						
						// Print demand for testing purposes
						DWTWrapper.this.oLocalDemandStore.printDemand();	
					}
				}
				catch(DMSException e)
				{
					e.printStackTrace(System.err);
				}
			}
		}
	}
	
	/**
	 * DepositResult traverse the LocalDemandStore and put the computed demand
	 * back to the DST, and then remove it.
	 */
	private class DepositResult
	extends BaseThread
	{
		public void run()
		{
			System.out.println("DWTWrapper.DepositResult.run()");
			
			while(true)
			{
				if(DWTWrapper.this.oLocalDemandStore != null)
				{
					for(DemandSignature oDemandSignature: DWTWrapper.this.oLocalDemandStore.getKeySet())
					{
						IDemand oDemand = DWTWrapper.this.oLocalDemandStore.get(oDemandSignature);
						
						if(oDemand.getState().isComputed())
						{
							try
							{
								// Send computed demand to DST.
								DWTWrapper.this.oTransportAgent.setResult(oDemand);
								System.out.println(oDemandSignature + " is send to the DST.");
								
								// Remove sent out demand from local demand store;
								DWTWrapper.this.oLocalDemandStore.remove(oDemandSignature);
								System.out.println(oDemandSignature + " is removed from DWT's local demand store.");
							}
							catch(DMSException e)
							{
								e.printStackTrace(System.err);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Run the demand worker service.
	 * @see gipsy.GEE.IDP.DemandGenerator.jms
	 * @see gipsy.GEE.IDP.DemandGenerator.jms.main(String[])
	 */
	public void run()
	{	
		WithdrawDemand oWithdraw = new WithdrawDemand();
		oWithdraw.start();
		
		DepositResult oDeposit = new DepositResult();
		oDeposit.start();
		
		try
		{
			oDeposit.join();
			oWithdraw.join();
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			throw new GIPSYRuntimeException(e);
		}
	}
	
	/**
	 * Start the tier instance.
	 */
	public void startTier()
	throws MultiTierException
	{
		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MSG_PREFIX + "." + Trace.getEnclosingMethodName() + "() was called by [" +
				Trace.getCallerClassName() + "]." + Trace.getCallerMethodName() + "()"
			);	
		}
		
		if(this.oWorker == null)
		{
			this.oWorker = new DemandWorker(this.strTierID);
		}
		
		try
		{
			// Create a TA instance using the configuration
			Configuration oTAConfig = (Configuration)this.oConfiguration.getObjectProperty(TA_CONFIG);
			ITransportAgent oTA = TAFactory.getInstance().createTA(oTAConfig);
			this.oTransportAgent = oTA;

			this.oWorker.setTransportAgent(oTA);
			this.oWorker.setTAExceptionHandler(this.oTAExceptionHandler);
			
			this.oTierThread = new BaseThread(this.oWorker);
			this.oTierThread.start();
		}
		catch(Exception oException)
		{
			oException.printStackTrace(System.err);
			throw new MultiTierException(oException);
		}
	}
	
	/**
	 * Stop the tier instance.
	 */
	public void stopTier()
	throws MultiTierException
	{
		if(Debug.isDebugOn())
		{
			Debug.debug
			(
				MSG_PREFIX + "." + Trace.getEnclosingMethodName() + "() was called by [" +
				Trace.getCallerClassName() + "]." + Trace.getCallerMethodName() + "()"
			);	
		}
		
		this.oWorker.stopWorker();
		this.oTierThread.interrupt();

		this.oWorker = null;
		this.oTierThread = null;
	}
	
	//Encapsulate methods for LocalDemandStoer;
	public boolean containsDemand(DemandSignature poDemandSignature)
	{
		return this.oLocalDemandStore.contains(poDemandSignature);
	}
	
	public IDemand getDemand(DemandSignature poDemandSignature)
	{
		return this.oLocalDemandStore.get(poDemandSignature);
	}
	
	public void putDemand(DemandSignature poDemandSignature,IDemand poDemand)
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
	
	public boolean equals(Object poDWT)
	{
		if(poDWT instanceof DWTWrapper)
		{
			DWTWrapper oDWT = (DWTWrapper)poDWT;
			
			return this.oGEERPool.equals(oDWT.oGEERPool)
				&& this.oLocalDemandStore.equals(oDWT.oLocalDemandStore)
				&& super.equals((GenericTierWrapper)poDWT);
		}
		else
		{
			return false;
		}
	}

	/**
	 * Used for demo.
	 * @param argv
	 * @throws Exception
	 */
	public static void main(String[] argv)
	throws Exception
	{
		LocalDemandStore oLocalDemandStore = new LocalDemandStore();
		LocalGEERPool oLocalGEERPool = new LocalGEERPool();
		DWTWrapper oDWTWrapper1 = new DWTWrapper
		(
			oLocalGEERPool,
			oLocalDemandStore,
			EDMFImplementation.JINI,
			new Configuration()
		);
		
		BaseThread oWrapper = new BaseThread(oDWTWrapper1);
		oWrapper.start();		
		oWrapper.join();
	}
}

// EOF
