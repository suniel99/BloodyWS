package gipsy.GEE.multitier;

import gipsy.Configuration;
import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.ITransportAgent;
import gipsy.GEE.IDP.demands.SystemDemand;
import gipsy.GEE.multitier.DGT.DGTController;
import gipsy.GEE.multitier.DST.DSTController;
import gipsy.GEE.multitier.DST.DSTWrapper;
import gipsy.GEE.multitier.DST.TAFactory;
import gipsy.GEE.multitier.DWT.DWTController;
import gipsy.GEE.multitier.GMT.GMTController;
import gipsy.GEE.multitier.GMT.demands.DGTRegistration;
import gipsy.GEE.multitier.GMT.demands.DSTRegistration;
import gipsy.GEE.multitier.GMT.demands.NodeRegistration;
import gipsy.GEE.multitier.GMT.demands.RegistrationResult;
import gipsy.GEE.multitier.GMT.demands.TierAllocationRequest;
import gipsy.GEE.multitier.GMT.demands.TierAllocationResult;
import gipsy.GEE.multitier.GMT.demands.TierDeallocationRequest;
import gipsy.GEE.multitier.GMT.demands.TierDeallocationResult;
import gipsy.GEE.multitier.GMT.demands.TierRegistration;
import gipsy.util.Trace;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;


/**
 * The GIPSY Node application. Each GIPSY Node is preassigned a
 * registration DST TA (configuration), therefore after creation,
 * a GIPSY Node is able to be registered into the registration DST.
 * 
 * @author Bin Han
 * @author Yi Ji
 * @version $Id: GIPSYNode.java,v 1.21 2012/06/15 14:22:43 mokhov Exp $
 */
public class GIPSYNode
extends Thread
{	
	private INodeController oDGTController;
	private INodeController oDSTController;
	private INodeController oDWTController;
	private INodeController oGMTController;
	
	/* 
	 * ************************ 
	 * Configuration properties
	 * ************************
	 */
	
	// Properties of tiers
	private static final String GMT_CONFIGFILES = "gipsy.GEE.multitier.Node.GMTConfigs"; 
	private static final String DST_CONFIGFILES = "gipsy.GEE.multitier.Node.DSTConfigs";
	private static final String DWT_CONFIGFILES = "gipsy.GEE.multitier.Node.DWTConfigs"; 
	private static final String DGT_CONFIGFILES = "gipsy.GEE.multitier.Node.DGTConfigs";
	
	// Properties to connect to GMT
	private static final String REGISTRATION_DST_TA_CONFIGFILE = "gipsy.GEE.multitier.Node.RegDSTTAConfig";
	private static final String GMT_TIERID = "gipsy.GEE.multitier.GMT.tierID";
	
	// Properties
	
	// Node ID
	private String strNodeID = null;
	
	// Node's host name
	private String strHostName = null;
	
	// GMT Tier ID
	private String strGMTTierID = null;
	
	// Registration DST and system DST TAs
	private ITransportAgent oRegDSTTA;
	private Configuration oRegDSTTAConfig;
	
	private ITransportAgent oSysDSTTA;
	private Configuration oSysDSTTAConfig;
	
	// If its registration is still valid, no need to register again
	public boolean isRegistered = false;
	
	private TAExceptionHandler oTAExceptionHandler = null;
	
	/**
	 * For logging
	 */
	private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";
	
	/**
	 * Create a GIPSY Node instance based on configuration,
	 * load all configuration files and assign them to their
	 * corresponding controllers.
	 * 
	 * @param poNodeConfig - The GIPSY Node configuration
	 */
	public GIPSYNode(Configuration poNodeConfig)
	{
		try
		{
			// Set tier ID
			this.strHostName= InetAddress.getLocalHost().getCanonicalHostName();
			this.strGMTTierID = poNodeConfig.getProperty(GMT_TIERID);
			
			// Get the Registration DST TA
			String strTAConfigFile = poNodeConfig.getProperty(REGISTRATION_DST_TA_CONFIGFILE);
			
			if(strTAConfigFile != null)
			{
				this.oRegDSTTAConfig = loadFromFile(strTAConfigFile);
				
				this.oRegDSTTA = TAFactory.getInstance().createTA(this.oRegDSTTAConfig);
			}
			else
			{
				// Have to be assigned later before node registration
				this.oRegDSTTA = null; 
			}
			
			/*
			 *  Load all configuration files and assign them to their
			 *  corresponding controllers.
			 */
			
			int iNumOfControllers = 4;
			String[] astrTierConfigFiles = new String[iNumOfControllers];
			INodeController[] aoControllers = new INodeController[iNumOfControllers];
			
			astrTierConfigFiles[0] = poNodeConfig.getProperty(GMT_CONFIGFILES);
			astrTierConfigFiles[1] = poNodeConfig.getProperty(DST_CONFIGFILES);
			astrTierConfigFiles[2] = poNodeConfig.getProperty(DGT_CONFIGFILES);
			astrTierConfigFiles[3] = poNodeConfig.getProperty(DWT_CONFIGFILES);
			
			// Must be in the same order as above
			aoControllers[0] = new GMTController();
			aoControllers[1] = new DSTController();
			aoControllers[2] = new DGTController();
			aoControllers[3] = new DWTController();
			
			for(int i = 0; i<iNumOfControllers; i++)
			{
				String strTierConfigFiles = astrTierConfigFiles[i];
				if(strTierConfigFiles == null || strTierConfigFiles.trim().isEmpty())
				{
					aoControllers[i] = null;
				}
				else
				{
					String[] astrConfigFiles = strTierConfigFiles.trim().split(";");
					
					for(String strConfigFile:astrConfigFiles)
					{
						if(strConfigFile.length() == 0)
						{
							continue;
						}
						
						Configuration oTierConfig = loadFromFile(strConfigFile);
						aoControllers[i].addTierConfig(oTierConfig);
					}
					
					// Check if there is no configuration file loaded at all.
					if(aoControllers[i].getTierConfigs().size() == 0)
					{
						aoControllers[i] = null;
					}
				}
			}
			
			// Must be in the same order as above
			this.oGMTController = aoControllers[0];
			this.oDSTController = aoControllers[1];
			this.oDGTController = aoControllers[2];
			this.oDWTController = aoControllers[3];
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
		}
	}
	
	/**
	 * Register the GIPSY Node into the registration DST.
	 * 
	 * @throws MultiTierException
	 */
	public void registerNode()
	throws MultiTierException
	{
		if(this.oRegDSTTA == null)
		{
			throw new MultiTierException("The Registration DST is not set yet!");
		}
		
		try 
		{
			// Generate the demand
			NodeRegistration oRegistration = 
				new NodeRegistration(this, this.strGMTTierID);
			
			System.out.println(MSG_PREFIX + "Sending NodeRegistrationDemand with " +
					" destination: " + 
					oRegistration.getDestinationTierID() +
					" and signature: " + oRegistration.getSignature());
			
			//oRegistration.setAoAvailableDSTConfigs(aoAvailableDSTs);
			// Send the demand to the manager
			this.oRegDSTTA.setDemand(oRegistration);
			
			System.out.println(MSG_PREFIX + "NodeRegistrationDemand sent, waiting for the result ...");
			RegistrationResult oRegistrationResult =
				(RegistrationResult) this.oRegDSTTA.getResult(oRegistration.getSignature());
			
			// Get the assigned Node ID
			this.strNodeID = oRegistrationResult.getAssignedNodeID();
			
			this.oSysDSTTAConfig = oRegistrationResult.getSystemDSTTAConfig();
			
			if(this.oSysDSTTAConfig == null || this.strNodeID == null)
			{
				System.err.println(MSG_PREFIX + "Node registration failed: no system DST assigned!");
				throw  new MultiTierException("Registration failed: no system DST assigned!");
			}
			else
			{
				// Connect to the assigned SysDST TA
				this.oSysDSTTA = TAFactory.getInstance().createTA(this.oSysDSTTAConfig);
				this.isRegistered = true;
				System.out.println(MSG_PREFIX + "Node registration completed!");
			}
			
			// Until now the GIPSY Node can start
		} 
		catch (DMSException e) 
		{
			System.err.println(MSG_PREFIX + "Node registration failed!");
			e.printStackTrace(System.err);
			throw new MultiTierException(e);
		}
	}
	
	
	
	
	public void addTier(TierIdentity peTierIdentity)
	{
		switch (peTierIdentity)
		{	
			case DGT:
			{
				this.oDGTController.addTier();
				break;
			}
			case DST:
			{
				this.oDSTController.addTier();
				break;
			}
			case DWT:
			{
				this.oDWTController.addTier();
				break;
			}
		}
		
	}
	
	public void removeTier(TierIdentity peTierIdentity)
	{
		switch (peTierIdentity)
		{	
			case DGT:
			{
				this.oDGTController.removeTier();
				break;
			}
			case DST:
			{
				this.oDSTController.removeTier();
				break;
			}
			case DWT:
			{
				this.oDWTController.removeTier();
				break;
			}
		}
	}
	
	

	@Override
	public void run() 
	{
		// Instantiate the TAException Handler and started.
		this.oTAExceptionHandler = 
			new TAExceptionHandler(this.strNodeID, this.strGMTTierID, this.oRegDSTTA);
		((NodeController) this.oDWTController).setTAExceptionHandler(this.oTAExceptionHandler);
		((NodeController) this.oDGTController).setTAExceptionHandler(this.oTAExceptionHandler);
		
		this.oTAExceptionHandler.start();
		
		System.out.println(MSG_PREFIX + "Node started!");
		
		while(true)
		{
			SystemDemand oDemand = null;;
			
			// Wait for demands sent to this Node
			while(oDemand == null)
			{
				try 
				{
					System.out.println(MSG_PREFIX + "Waiting for a new system demand ...");
					oDemand = (SystemDemand) this.oSysDSTTA.getDemand(this.strNodeID);
				} 
				catch (DMSException oException) 
				{
					try 
					{
						this.oSysDSTTA = this.oTAExceptionHandler.fixTA(this.oSysDSTTA, oException);
					} 
					catch (InterruptedException oInterrptedException) 
					{
						oInterrptedException.printStackTrace(System.err);
					}
				}
			}
			
			if(oDemand instanceof TierAllocationRequest)
			{
				TierAllocationRequest oRequest = (TierAllocationRequest)oDemand;
				TierIdentity oTierIdentity = oRequest.getTierIdentity();
				TierAllocationResult oResult = null;
				Configuration oTierConfig = oRequest.getTierConfig();
				int iNumOfInstances = oRequest.getNumberOfInstances();
				
				boolean bIsReallocation = false;
				String strTierID = oTierConfig.getProperty(IMultiTierWrapper.WRAPPER_TIER_ID);
				
				if(strTierID != null)
				{
					bIsReallocation = true;
				}
				
				try
				{
					if(iNumOfInstances <= 0)
					{
						System.out.println(MSG_PREFIX + "Received an illegal TierAllocationRequest!");
						throw new MultiTierException("Wrong number of instances specified " + iNumOfInstances + "!");
					}
					
					TierRegistration[] aoTierRegs = new TierRegistration[iNumOfInstances];
					IMultiTierWrapper oTier;
					
					for(int i = 0; i<aoTierRegs.length; i++)
					{
						oTier = null;
						
						/*
						 * A shallow clone of the underlying hash table is used. This
						 * is effective for immutable objects such as the String properties 
						 * and values, as well as sharable object values such as the TA 
						 * configuration for DGT and DWT.
						 */
						oTierConfig = (Configuration) oRequest.getTierConfig().clone();
						
						switch(oTierIdentity)
						{
						case DST:
							if(i == 0)
							{
								System.out.println(MSG_PREFIX + "Received a DSTAllocationRequest");
							}
							
							oTier = this.oDSTController.addTier(oTierConfig);
							oTier.startTier();
							
							DSTRegistration oDSTReg = new DSTRegistration(this.strNodeID, oTier.getTierID(), 
									oTier.getConfiguration(), ((DSTWrapper)oTier).exportTAConfig(), 
									this.strGMTTierID);
							
							if(!bIsReallocation)
							{
								int iMaxActiveConnection = Integer.parseInt(oTierConfig.getProperty(DSTWrapper.MAX_ACTIVE_CONNECTION));
								oDSTReg.setMaxActiveConnection(iMaxActiveConnection);
							}
							
							aoTierRegs[i] = oDSTReg;
							
							break;
						case DGT:
							if(i == 0)
							{
								System.out.println(MSG_PREFIX + "Received a DGTAllocationRequest");
							}
							
							oTier = this.oDGTController.addTier(oTierConfig);
							oTier.startTier();
							
							aoTierRegs[i] = new DGTRegistration(this.strNodeID,
									oTier.getTierID(), this.strGMTTierID);
							break;
							
						case DWT:
							if(i == 0)
							{
								System.out.println(MSG_PREFIX + "Received a DWTAllocationRequest");
							}
							
							oTier = this.oDWTController.addTier(oTierConfig);
							oTier.startTier();
							
							aoTierRegs[i] = new DGTRegistration(this.strNodeID,
									oTier.getTierID(), this.strGMTTierID);
							break;
						}
						System.out.println(MSG_PREFIX + oTierIdentity + oTier.getTierID() 
								+ " was allocated and started!");
					}
					
					
					oResult = new TierAllocationResult(aoTierRegs, null);
					oResult.setSignature(oRequest.getSignature());
					
					while(true)
					{
						try 
						{
							System.out.println(MSG_PREFIX + 
									"Sending TierAllocationResult enclosing tier registrations ...");
							this.oSysDSTTA.setResult(oResult);
							break;
						} 
						catch (DMSException oException) 
						{
							try 
							{
								this.oSysDSTTA = this.oTAExceptionHandler.fixTA(this.oSysDSTTA, oException);
								System.out.println(MSG_PREFIX + "TierAllocationResult sent!");
							} 
							catch (InterruptedException oInterrptedException) 
							{
								oInterrptedException.printStackTrace(System.err);
							}
						}
					}
				}
				catch(MultiTierException oException)
				{
					oResult = new TierAllocationResult(null, oException);
					oResult.setSignature(oRequest.getSignature());
					
					while(true)
					{
						try 
						{
							System.out.println(MSG_PREFIX + "Sending allocation result enclosing exception ...");
							this.oSysDSTTA.setResult(oResult);
							System.out.println(MSG_PREFIX + "TierAllocationResult sent!");
							break;
						} 
						catch (DMSException oDMSException) 
						{
							try 
							{
								this.oSysDSTTA = this.oTAExceptionHandler.fixTA(this.oSysDSTTA, oDMSException);
							} 
							catch (InterruptedException oInterrptedException) 
							{
								oInterrptedException.printStackTrace(System.err);
							}
						}
					}
				}
			}
			else if(oDemand instanceof TierDeallocationRequest)
			{
				TierDeallocationRequest oRequest = (TierDeallocationRequest)oDemand;
				
				String[] astrTierIDs = oRequest.getTierIDs();
				TierIdentity oTierIdentity= oRequest.getTierIdentity();
				
				System.out.println(MSG_PREFIX + "Received a DSTDeallocationRequest");
				
				for(int i = 0; i<astrTierIDs.length; i++)
				{
					System.out.println(MSG_PREFIX  + oTierIdentity + astrTierIDs[i] + " deallocated!");
				}
				
				TierDeallocationResult oResult = new TierDeallocationResult(null);
				oResult.setSignature(oRequest.getSignature());
				while(true)
				{
					try 
					{
						System.out.println(MSG_PREFIX + "Sending deallocation result ...");
						this.oSysDSTTA.setResult(oResult);
						System.out.println(MSG_PREFIX + "TierDeallocationResult sent!");
						break;
					} 
					catch (DMSException oDMSException) 
					{
						try 
						{
							this.oSysDSTTA = this.oTAExceptionHandler.fixTA(this.oSysDSTTA, oDMSException);
						} 
						catch (InterruptedException oInterrptedException) 
						{
							oInterrptedException.printStackTrace(System.err);
						}
					}
				}
				
			}
		}
	}

	public static Configuration loadFromFile(String pstrFileName) 
	throws IOException
	{
		FileInputStream oFileInStream = new FileInputStream(pstrFileName);
		Configuration oConfig = new Configuration();
		oConfig.load(oFileInStream);
		oFileInStream.close();
		return oConfig;
	}

	public INodeController getDSTController() 
	{
		return oDSTController;
	}

	public String getNodeID() 
	{
		return strNodeID;
	}

	public void setNodeID(String pstrNodeID) 
	{
		this.strNodeID = pstrNodeID;
	}

	public ITransportAgent getRegistrationDSTTA() 
	{
		return oRegDSTTA;
	}

	public void setRegistrationDSTTA(ITransportAgent poRegistrationDSTTA) 
	{
		this.oRegDSTTA = poRegistrationDSTTA;
		this.oRegDSTTAConfig = this.oRegDSTTA.getConfiguration();
	}

	public ITransportAgent getSystemDSTTA() 
	{
		return oSysDSTTA;
	}

	public void setSystemDSTTA(ITransportAgent poSystemDSTTA) 
	{
		this.oSysDSTTA = poSystemDSTTA;
		this.oSysDSTTAConfig = this.oSysDSTTA.getConfiguration();
	}
	
	public INodeController getGMTController()
	{
		return this.oGMTController;
	}
	
	public String getHostName()
	{
		return this.strHostName;
	}
}

