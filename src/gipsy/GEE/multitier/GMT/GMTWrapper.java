package gipsy.GEE.multitier.GMT;

import gipsy.Configuration;
import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.demands.SystemDemand;
import gipsy.GEE.multitier.GIPSYNode;
import gipsy.GEE.multitier.GenericTierWrapper;
import gipsy.GEE.multitier.IMultiTierWrapper;
import gipsy.GEE.multitier.INodeController;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.TierIdentity;
import gipsy.GEE.multitier.DST.DSTWrapper;
import gipsy.GEE.multitier.DST.TAFactory;
import gipsy.GEE.multitier.GMT.demands.DSTIssueReport;
import gipsy.GEE.multitier.GMT.demands.DSTRegistration;
import gipsy.GEE.multitier.GMT.demands.NodeRegistration;
import gipsy.GEE.multitier.GMT.demands.RegistrationResult;
import gipsy.GEE.multitier.GMT.demands.TierAllocationRequest;
import gipsy.GEE.multitier.GMT.demands.TierAllocationResult;
import gipsy.GEE.multitier.GMT.demands.TierDeallocationRequest;
import gipsy.GEE.multitier.GMT.demands.TierDeallocationResult;
import gipsy.GEE.multitier.GMT.demands.TierRegistration;
import gipsy.GEE.multitier.GMT.demands.TierRegistrationResult;
import gipsy.util.Trace;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import marf.util.Debug;


/**
 * GIPSY Management Tier wrapper. The GIPSY Management Tier is a tier that manages 
 * a GIPSY instance (i.e. a set of interconnected GIPSY tiers). It is through the 
 * use of the GMT that GIPSY tiers are connected and managed.  
 * 
 * @author Bin Han
 * @author Serguei Mokhov
 * @author Yi Ji
 * 
 * @version $Id: GMTWrapper.java,v 1.37 2012/06/13 14:14:08 mokhov Exp $
 */
public class GMTWrapper
extends GenericTierWrapper
{
	// Configuration properties
	public static final String GMT_NODE = "gipsy.GEE.multitier.GMT.Node";
	protected static final String GMT_TIERID = "gipsy.GEE.multitier.GMT.tierID";
	public static final String REGISTRATION_DST_CONFIG = "gipsy.GEE.multitier.registrationDST.config";
	private static final String IS_ONE_NODE_PER_HOST = "gipsy.GEE.multitier.GMT.isOneNodePerHost";
	
	public GMTInfoKeeper oInfoKeeper = new GMTInfoKeeper();
	
	/**
	 * Specify what the GMT should do using the predefined procedures.
	 */
	public volatile int iRecoverPolicy = 0;
	
	/**
	 * Do not handle the failure and return the reported TA configuration 
	 * to the reporter.
	 */
	public static final int LET_IT_BE = 0;
	
	/**
	 * Use the next (index + 1 in the DST list kept by GMT) available 
	 * DST until the end of the list (kept by GMT) is reached. When
	 * the end of the list is reached, pause and wait for user control.
	 */
	public static final int TRY_NEXT_UNTIL_THE_END = 1;
	
	/**
	 * Use the next (index + 1 in the DST list kept by GMT) available.
	 * If the end of the list is reached, wrap around from the beginning
	 * again.
	 */
	public static final int TRY_NEXT_AND_WRAP_AROUND = 2;
	
	public static final int IF_CRASH_THEN_TRY_NEXT_UNTIL_THE_END = 3;
	
	public static final int IF_CRASH_THEN_RESTART = 4;
	
	//private ITransportAgent oRegDSTTA = null;
	//private Configuration oRegDSTConfig = null;
	private DSTRegistration oRegDSTReg = null;
	private GIPSYNode oHostNode = null;
	
	private PrintStream oOut = null;
	private PrintStream oErr = null;
	
	/**
	 * This flag is used to indicate if the tier has been
	 * started and has not been stopped, so that the thread
	 * can exit its loop. Besides, it is labeled as volatile
	 * to make the loop always known the most recent value.
	 */
	private volatile boolean bIsWorking = true;
	
	private long lNodeCounter = 0;
	
	/**
	 * For logging.
	 */
	private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";
	
	/**
	 * An empty constructor. As an implicitly inherited constructor in
	 * all the constructors of its subclasses, it better does nothing
	 * interfering the instantiation of its subclasses.
	 */
	public GMTWrapper()
	{
	}

	public GMTWrapper(Configuration poGMTConfig)
	{
		this.oConfiguration = poGMTConfig;
		this.oOut = System.out;
		this.oErr = System.err;
		this.strTierID = this.oConfiguration.getProperty(GMT_TIERID);
		this.oHostNode = (GIPSYNode)this.oConfiguration.getObjectProperty(GMT_NODE);
	}
	
	/**
	 * Once started, this thread monitors registration
	 * store to process any system demands sent to the
	 * GMT. Note that if multiple threads of the same
	 * Runnable object are started, the following code
	 * may go wrong especially the error handling part.
	 */
	public void run()
	{	
		ITransportAgent oRegDSTTA = this.oRegDSTReg.getTA();
		
		
		boolean bIsOneNodePerHost = Boolean.parseBoolean(this.oConfiguration.getProperty(IS_ONE_NODE_PER_HOST));
		
		while(bIsWorking)
		{
			
			SystemDemand oDemand = null;
			
			while(oDemand == null)
			{
				try 
				{
					oDemand = (SystemDemand)oRegDSTTA.getDemand(this.strTierID);
				} 
				catch (DMSException oException) 
				{
					this.handleRegDSTCrash();
				}
			}
			
			if(oDemand instanceof NodeRegistration)
			{
				NodeRegistration oRegistration = (NodeRegistration)oDemand;
				
				// Check if this node has already been registered
				String strNodeID = null;
				
				if(bIsOneNodePerHost)
				{
					strNodeID = this.oInfoKeeper.getNodeIDfromRegistration(oRegistration);
				}
				
				DSTRegistration oAssignedSysDST = null;
				
				// It is a new node
				if(strNodeID == null)
				{
					// Tell the node what systemDST to use
					oAssignedSysDST = this.getAnAvailableDST();
					
					if(oAssignedSysDST != null)
					{
						// Now assign a node ID
						strNodeID = this.lNodeCounter + "";
						this.lNodeCounter++;
					}
				}
				else
				{
					/* 
					 * Clear its DGT and DWT registration. No need to clear
					 * DST registration as if it is no longer there and is
					 * discovered by someone using it, it will be restarted.
					 */
					
					this.oInfoKeeper.removeDGTDWTRegistration(strNodeID);
					
					oAssignedSysDST = (DSTRegistration) this.oInfoKeeper.getNodeSysDST(strNodeID);
				}
				
				oRegistration.setNodeID(strNodeID);
				
				Configuration oSysTAConfig = null;
				
				if(oAssignedSysDST != null)
				{
					oSysTAConfig = oAssignedSysDST.getTAConfig();
					/*
					 * No need to check if the DST is really available
					 * by now, because the node will report the DST
					 * crash later if the DST is not working.
					 */
				}
				
				RegistrationResult oRegResult = new RegistrationResult(strNodeID, oSysTAConfig);
				oRegResult.setSignature(oRegistration.getSignature());
				
				while(true)
				{
					try 
					{
						oRegDSTTA.setResult(oRegResult);
						break;
					} 
					catch (DMSException oException) 
					{
						this.handleRegDSTCrash();
					}					
				}
				
				if(oSysTAConfig != null)
				{
					/*
					 * Save new registration, or replace old registration with the new one
					 */
					this.oInfoKeeper.saveNodeRegistration(oRegistration, oAssignedSysDST);
					this.oOut.println("--Node " + oRegistration.getNodeID() + " registered!");
				}
				else
				{
					this.oOut.println("--Node " + oRegistration.getNodeID() + " failed to register!");
					/*
					 * If there was no DST available, now let the user manually 
					 * allocate a new DST;
					 */
				}
			}
			else if(oDemand instanceof DSTIssueReport)
			{
				DSTIssueReport oReport = (DSTIssueReport) oDemand;
				Configuration oCrashedTAConfig = oReport.getProblematicTAConfig();
				DMSException oTAException = oReport.getTAException();
				
				// First find the DST registration
				List<DSTRegistration> oRegisteredDSTs = this.oInfoKeeper.getDSTRegistrations();
				
				this.oOut.println("--A DST issue report was received.");
				
				DSTRegistration oTheDSTReg = null;
				
				int iDSTIndex = 0;
				int iDSTListLength = 0;
				
				synchronized(oRegisteredDSTs)
				{
					iDSTListLength = oRegisteredDSTs.size();
					
					for(DSTRegistration oDSTReg:oRegisteredDSTs)
					{
						// Find the DST
						if(oDSTReg.getTAConfig().equals(oCrashedTAConfig))
						{
							oTheDSTReg = oDSTReg;
							break;
						}
						iDSTIndex++;
					}
				}
				
				// If the DST was found
				if(oTheDSTReg != null)
				{
					// If the DST was short of memory
					if(oTAException != null 
							&& oTAException.getMessage() != null
							&& oTAException.getMessage().contains(DMSException.OUT_OF_MEMORY))
					{
						switch(iRecoverPolicy)
						{
						case LET_IT_BE:
							this.oOut.println("--Assigning DST (index " + iDSTIndex + ") to the issue reporter ...");
							break;
						case TRY_NEXT_UNTIL_THE_END:
							while(iRecoverPolicy == TRY_NEXT_UNTIL_THE_END)
							{
								// Refresh the DST list.
								oRegisteredDSTs = this.oInfoKeeper.getDSTRegistrations();
								
								if(iDSTIndex < oRegisteredDSTs.size() - 1)
								{
									oTheDSTReg = oRegisteredDSTs.get(iDSTIndex + 1);
									this.oOut.println("--Assigning DST (index " + (iDSTIndex + 1) + ") to the issue reporter ...");
									break;
								}
								else
								{
									/*
									 * Wait and let the user to add a new DST.
									 */
									
									this.oErr.println("--GMT paused: no new DST available, " +
											"please allocate a new DST!");
									
									
									synchronized(this)
									{
										try 
										{
											this.wait();
											/* 
											 * Until waken up by the user.
											 */
											this.oOut.println("--GMT resumed.");
										} 
										catch (InterruptedException oInterrupted) 
										{
											oInterrupted.printStackTrace(System.err);
										}
									}
								}
							}
							if(iRecoverPolicy != TRY_NEXT_AND_WRAP_AROUND)
							{
								break;
							}
						case TRY_NEXT_AND_WRAP_AROUND:
							// Refresh the DST list.
							oRegisteredDSTs = this.oInfoKeeper.getDSTRegistrations();
							
							if(iDSTIndex < oRegisteredDSTs.size() - 1)
							{
								oTheDSTReg = oRegisteredDSTs.get(iDSTIndex + 1);
								this.oOut.println("--Assigning DST (index " + (iDSTIndex + 1) + ") to the issue reporter ...");
							}
							else
							{
								oTheDSTReg = oRegisteredDSTs.get(0);
								this.oOut.println("--Assigning DST (index 0) to the issue reporter ...");
							}
							break;
						}
					}
					else
					{
						if(this.iRecoverPolicy == IF_CRASH_THEN_TRY_NEXT_UNTIL_THE_END)
						{
							while(this.iRecoverPolicy == IF_CRASH_THEN_TRY_NEXT_UNTIL_THE_END)
							{
								// Refresh the DST list.
								oRegisteredDSTs = this.oInfoKeeper.getDSTRegistrations();
								
								if(iDSTIndex < oRegisteredDSTs.size() - 1)
								{
									oTheDSTReg = oRegisteredDSTs.get(iDSTIndex + 1);
									this.oOut.println("--Assigning DST (index " + (iDSTIndex + 1) + ") to the issue reporter ...");
									break;
								}
								else
								{
									/*
									 * Wait and let the user to add a new DST.
									 */
									
									this.oErr.println("--GMT paused: no new DST available, " +
											"please allocate a new DST!");
									
									
									synchronized(this)
									{
										try 
										{
											this.wait();
											/* 
											 * Until waken up by the user.
											 */
											this.oOut.println("--GMT resumed.");
										} 
										catch (InterruptedException oInterrupted) 
										{
											oInterrupted.printStackTrace(System.err);
										}
									}
								}
							}
						}
						
						if(this.iRecoverPolicy != IF_CRASH_THEN_TRY_NEXT_UNTIL_THE_END)
						{
							// If it is a real crash, deal with it
							oTheDSTReg = this.handleNonRegDSTCrash(oTheDSTReg);
						}
					}
				}
				else
				{
					/* 
					 * If the DST was not found, assign it any DST 
					 * available.
					 */
					
					oTheDSTReg = this.getAnAvailableDST();
				}
				
				// If the DST could not be fixed or found
				if(oTheDSTReg == null)
				{
					/* 
					 * Let the user add a new DST and try the new 
					 * DST.
					 */
					this.oErr.println("--GMT paused: the reported DST could not be fixed, " +
					"please allocate a new DST!");
			
					synchronized(this)
					{
						try 
						{
							this.wait();
							/* 
							 * Until waken up by the user.
							 */
							this.oOut.println("--GMT resumed.");
						} 
						catch (InterruptedException oInterrupted) 
						{
							oInterrupted.printStackTrace(System.err);
						}
					}
					
					// Refresh the DST list.
					oRegisteredDSTs = this.oInfoKeeper.getDSTRegistrations();
					if(iDSTListLength > oRegisteredDSTs.size())
					{
						iDSTListLength = oRegisteredDSTs.size();
					}
					else if(iDSTListLength < oRegisteredDSTs.size())
					{
						iDSTListLength++;
					}
					
					// Get a newly allocated DST.
					oTheDSTReg = oRegisteredDSTs.get(iDSTListLength-1);
					
				}
				
				oReport.setDestinationTierID(oReport.getSourceTierID());
				oReport.setCorrectedTAConfig(oTheDSTReg.getTAConfig());
				
				while(true)
				{
					try 
					{
						oRegDSTTA.setResult(oReport);
						this.oOut.println("--The DST issue fix was sent!");
						break;
					} 
					catch (DMSException oException) 
					{
						this.handleRegDSTCrash();
					}
				}
			}
		}
	}
	
	/**
	 * Start the tier instance.
	 */
	public void startTier()
	throws MultiTierException
	{
		try 
		{
			INodeController oDSTController = this.oHostNode.getDSTController();
			DSTWrapper oRegDST = null;
			Configuration oRegDSTConfig = null;

			Debug.debug("GMTWrapper.startTier 1");
			
			// If no previous DST started
			if(this.oRegDSTReg == null)
			{
				Debug.debug("GMTWrapper.startTier 1.1: " + this.oConfiguration);
				Debug.debug("GMTWrapper.startTier 1.1: " + this.oConfiguration.getProperty(REGISTRATION_DST_CONFIG));
				oRegDSTConfig = GIPSYNode.loadFromFile(this.oConfiguration.getProperty(REGISTRATION_DST_CONFIG));
				Debug.debug("GMTWrapper.startTier 1.2");
			}
			else
			{
				// Remove the registration info of this DST.
				throw new MultiTierException("GMT has already been started!");
			}

			Debug.debug("GMTWrapper.startTier 2");
			
			// Start the DST using the configuration
			oRegDST = (DSTWrapper)oDSTController.addTier(oRegDSTConfig);
			oRegDST.startTier();

			Debug.debug("GMTWrapper.startTier 3");
			
			/*
			 * Update the configuration based on the assumption that the 
			 * configuration was updated by the DST to ensure that using
			 * this configuration the same DST would be started.
			 */
			
			oRegDSTConfig = oRegDST.getConfiguration();
			
			Configuration oTAConfig = oRegDST.exportTAConfig();
			
			Debug.debug("GMTWrapper.startTier 4");
			
			// Export the registrar DST config to file RegDSTTA.config
			
			FileOutputStream oFileOutputStream = new FileOutputStream("RegDSTTA.config");
			oTAConfig.store(oFileOutputStream, "Registrar DST TA Configuration");
			oFileOutputStream.flush();
			oFileOutputStream.close();
			
			Debug.debug("GMTWrapper.startTier 5");
			
			// Get the TA
			ITransportAgent oTA = TAFactory.getInstance().createTA(oTAConfig);
			
			// Register this node;
			NodeRegistration oNodeRegistration = new NodeRegistration(this.oHostNode, null);
			String strNodeID = this.lNodeCounter + "";
			this.lNodeCounter++;

			Debug.debug("GMTWrapper.startTier 6");

			oNodeRegistration.setNodeID(strNodeID);
			this.oHostNode.setNodeID(strNodeID);
			
			this.oInfoKeeper.saveNodeRegistration(oNodeRegistration, null);
			
			// Register this DST;
			this.oRegDSTReg = new DSTRegistration
			(
				this.oHostNode.getNodeID(), 
				oRegDST.getTierID(),
				oRegDSTConfig,
				oTAConfig,
				this.strTierID
			);
			
			// Set the maximum connection allowance
			int iMaxConnectionAllowance
				= Integer.parseInt(oRegDSTConfig.getProperty(DSTWrapper.MAX_ACTIVE_CONNECTION));
			
			this.oRegDSTReg.setMaxActiveConnection(iMaxConnectionAllowance);
			
			// Keep a TA to this DST
			this.oRegDSTReg.setTA(oTA);
			//oDSTRegistration.setMaxActiveConnection();
			
			this.oRegDSTReg.setAllocationTime(System.currentTimeMillis());
			
			this.oInfoKeeper.saveTierRegistration(this.oRegDSTReg, null);
			
			
			// Assign the DST as the node's system DST, and update the node registration
			oTA = TAFactory.getInstance().createTA(oTAConfig);
			this.oHostNode.setRegistrationDSTTA(oTA);
			this.oHostNode.setSystemDSTTA(oTA);
			this.oInfoKeeper.saveNodeRegistration(oNodeRegistration, this.oRegDSTReg);
			
			this.oOut.println("--Node " + this.oHostNode.getNodeID() + " registered!");
		}
		catch(MultiTierException oException)
		{
			throw oException;
		}
		catch(Exception oException) 
		{
			System.err.println(oException.getClass().getName() + ": " + oException.getMessage());
			oException.printStackTrace(this.oErr);
			throw new MultiTierException(oException);
		}
	}
	
	public void allocateTier(String pstrNodeID, 
			TierIdentity poTierIdentity, 
			Configuration poTierConfig, 
			DSTRegistration poDataDSTReg,
			int piNumOfInstances)
	throws MultiTierException
	{
		DSTRegistration oSysTAReg =  this.oInfoKeeper.getNodeSysDST(pstrNodeID);
		ITransportAgent oSysTA = oSysTAReg.getTA();
		
		if(oSysTA == null)
		{
			oSysTA = TAFactory.getInstance().createTA(oSysTAReg.getTAConfig());
		}
		
		TierAllocationRequest oRequest = new TierAllocationRequest(pstrNodeID, poTierIdentity, 
				poTierConfig, piNumOfInstances);
		
		String strTierID = poTierConfig.getProperty(IMultiTierWrapper.WRAPPER_TIER_ID);
		
		boolean bIsReallocation = false;
		
		if(strTierID != null)
		{
			bIsReallocation = true;
			
			if(Debug.isDebugOn())
			{
				Debug.debug(MSG_PREFIX + "This is a reallocation!");
			}
			
			if(piNumOfInstances != 1)
			{
				throw new MultiTierException("Reallocateion of multiple tiers is not allowed!");
			}
		}
		
		for(int i = 0; i<4; i++)
		{
			try 
			{
				oSysTA.setDemand(oRequest);
				break;
			} 
			catch (DMSException oException) 
			{
				if(i<3)
				{
					this.handleRegDSTCrash();
				}
				else
				{
					if(Debug.isDebugOn())
					{
						oException.printStackTrace(System.err);
					}
					throw new MultiTierException(oException);
				}
			}					
		}
		
		if(Debug.isDebugOn())
		{
			Debug.debug(MSG_PREFIX + "The TierAllocationRequest " + 
					oRequest.getSignature().toString() + " was sent!");
		}
		
		TierAllocationResult oResult = null;
		
		for(int i = 0; i< 4 && oResult == null; i++)
		{
			try 
			{
				oResult = (TierAllocationResult)oSysTA.getResult(oRequest.getSignature());
			} 
			catch (DMSException oException) 
			{
				if(i<3)
				{
					this.handleRegDSTCrash();
				}
				else
				{
					if(Debug.isDebugOn())
					{
						oException.printStackTrace(System.err);
					}
					throw new MultiTierException(oException);
				}
			}					
		}
		
		if(Debug.isDebugOn())
		{
			Debug.debug(MSG_PREFIX + "The TierAllocationResult " + 
					oResult.getSignature().toString() + " was received!");
		}
		
		// XXX What if the Node does not reply at all? We need TA timeouts.
		
		TierRegistration[] aoRegistrations = oResult.getRegistrations();
		
		if(aoRegistrations == null || aoRegistrations.length == 0)
		{
			if(oResult.getException() != null)
			{
				throw oResult.getException();
			}
			else
			{
				throw new MultiTierException("The allocation failed for unknown reason!");
			}
		}
		
		for(int i = 0; i<aoRegistrations.length; i++)
		{
			TierRegistration oRegistration = aoRegistrations[i];
			
			if(poTierIdentity == TierIdentity.DST)
			{
				DSTRegistration oDSTReg = (DSTRegistration)oRegistration;
				
				if(bIsReallocation)
				{
					oDSTReg.setMaxActiveConnection(poDataDSTReg.getMaxActiveConnection());
					oDSTReg.setActiveConnectionCount(poDataDSTReg.getActiveConnectionCount());
				}
				oDSTReg.setAllocationTime(System.currentTimeMillis());
				
			}
			
			this.oInfoKeeper.saveTierRegistration(oRegistration, poDataDSTReg);
		}
	}
	
	public void deallocateTier(String pstrNodeID, TierIdentity poTierIdentity,
			String[] pastrTierIDs)
	throws MultiTierException
	{
		DSTRegistration oSysTAReg =  this.oInfoKeeper.getNodeSysDST(pstrNodeID);
		ITransportAgent oSysTA = oSysTAReg.getTA();
		
		if(oSysTA == null)
		{
			oSysTA = TAFactory.getInstance().createTA(oSysTAReg.getTAConfig());
		}
		
		TierDeallocationRequest oRequest = new TierDeallocationRequest(pstrNodeID, 
				poTierIdentity, pastrTierIDs);
		
		for(int i = 0; i<4; i++)
		{
			try 
			{
				oSysTA.setDemand(oRequest);
				break;
			} 
			catch (DMSException oException) 
			{
				if(i<3)
				{
					this.handleRegDSTCrash();
				}
				else
				{
					if(Debug.isDebugOn())
					{
						oException.printStackTrace(System.err);
					}
					throw new MultiTierException(oException);
				}
			}					
		}
		
		if(Debug.isDebugOn())
		{
			Debug.debug(MSG_PREFIX + "The TierAllocationRequest " + 
					oRequest.getSignature().toString() + " was sent!");
		}
		
		TierDeallocationResult oResult = null;
		
		for(int i = 0; i< 4 && oResult == null; i++)
		{
			try 
			{
				oResult = (TierDeallocationResult)oSysTA.getResult(oRequest.getSignature());
			} 
			catch (DMSException oException) 
			{
				if(i<3)
				{
					this.handleRegDSTCrash();
				}
				else
				{
					if(Debug.isDebugOn())
					{
						oException.printStackTrace(System.err);
					}
					throw new MultiTierException(oException);
				}
			}					
		}
		
		if(Debug.isDebugOn())
		{
			Debug.debug(MSG_PREFIX + "The TierDeallocationResult " + 
					oResult.getSignature().toString() + " was received!");
		}
	}
	
	private void handleRegDSTCrash()
	{
		long lReportedTime = System.currentTimeMillis();
		
		this.oOut.println("--A RegDST crash was reported.");
		
		synchronized(this)
		{
			try 
			{
				long lLastAllocationTime = this.oRegDSTReg.getAllocationTime();
				
				Configuration oRegDSTTAConfig = this.oRegDSTReg.getTAConfig();
				
				if((lReportedTime - lLastAllocationTime) > 3000)
				{
					this.oOut.println("--Restarting registration DST ...");
					Configuration oRegDSTConfig = this.oRegDSTReg.getUpdatedTierConfig();
					INodeController oDSTController = this.oHostNode.getDSTController();
					DSTWrapper oRegDST =  (DSTWrapper) oDSTController.addTier(oRegDSTConfig);
					oRegDST.startTier();
					oRegDSTConfig = oRegDST.getConfiguration();
					this.oRegDSTReg.setUpdatedTierConfig(oRegDSTConfig);
					oRegDSTTAConfig = oRegDST.exportTAConfig();
					this.oRegDSTReg.setAllocationTime(System.currentTimeMillis());
					this.oOut.println("--Registration DST was restarted!");
				}
				else
				{
					this.oOut.println("--The Registration DST was just restarted, trying to reconnect ...");
				}
				
				// Reset the TA.
				this.oRegDSTReg.getTA().setConfiguration(oRegDSTTAConfig);
				this.oOut.println("--Registration DST was reconnected!");
			} 
			catch (Exception oAllocationExp) 
			{
				//oAllocationExp.printStackTrace(this.oErr);
				this.oErr.println("--Registration DST could not be connected!");
			}
		}
	}
	
	public GMTInfoKeeper getInfoKeeper()
	{
		return this.oInfoKeeper;
	}
	
	/**
	 * Stop the tier instance.
	 */
	public void stopTier()
	throws MultiTierException
	{
		this.bIsWorking = false;
	} 
	
	public void setOut(PrintStream poOut)
	{
		this.oOut = poOut;
	}
	
	public void setErr(PrintStream poErr)
	{
		this.oErr = poErr;
	}
	
	private DSTRegistration getAnAvailableDST()
	{
		// Tell the node what systemDST to use
		List<DSTRegistration> oRegisteredDSTs = this.oInfoKeeper.getDSTRegistrations();
		
		DSTRegistration oTheDSTReg = null;
		
		synchronized(oRegisteredDSTs)
		{
			for(DSTRegistration oDSTReg:oRegisteredDSTs)
			{
				if(oDSTReg.getAccessNumber() < oDSTReg.getMaxActiveConnection())
				{
					oTheDSTReg = oDSTReg;
					break;
				}
			}
		}
		return oTheDSTReg;
	}
	
	private DSTRegistration handleNonRegDSTCrash(DSTRegistration poCrashedDSTReg)
	{
		// Check the freshness of its TA configuration
		long lAllocationTime =  poCrashedDSTReg.getAllocationTime();
		
		// If the allocation is too old
		if((System.currentTimeMillis() - lAllocationTime) > 30000)
		{
			this.oOut.println("--Reallocating crashed DST [Node " +  poCrashedDSTReg.getNodeID() 
					+ ", Tier " +  poCrashedDSTReg.getTierID() + "] ...");
			// Reallocate the DST
			try 
			{
				this.allocateTier( poCrashedDSTReg.getNodeID(), 
						TierIdentity.DST, 
						poCrashedDSTReg.getUpdatedTierConfig(), 
						poCrashedDSTReg, 1);
				this.oOut.println("--Reallocated DST [Node " +  poCrashedDSTReg.getNodeID() 
					+ ", Tier " +  poCrashedDSTReg.getTierID() + "]");
			} 
			catch (MultiTierException oAllocationException) 
			{
				return null;
			}
		}
		else
		{
			this.oOut.println("--DST [Node " +  poCrashedDSTReg.getNodeID() + 
					", Tier " +  poCrashedDSTReg.getTierID() + 
					"] was just restarted within 5 seconds, " +
					"using existing TA config as the fix ...");
			/* 
			 * Wait for sometime in case that the same error is
			 * repeated too often.
			 */
//			try 
//			{
//				Thread.sleep(3000);
//			}
//			catch (InterruptedException oInterruptedException) 
//			{
//				oInterruptedException.printStackTrace(this.oErr);
//			}
		}
		
		return  this.oInfoKeeper.getDSTRegistration(poCrashedDSTReg.getNodeID(), poCrashedDSTReg.getTierID());
	}
	
	public boolean isValidPolicyNumber(int iPolicy) {
	    return iPolicy > 0 && iPolicy <= 4;
	}
}
//EOF
