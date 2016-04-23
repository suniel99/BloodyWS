package gipsy.RIPE.editors.RunTimeGraphEditor.operator;

import gipsy.Configuration;
import gipsy.GEE.multitier.GIPSYNode;
import gipsy.GEE.multitier.IMultiTierWrapper;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.TierIdentity;
import gipsy.GEE.multitier.GMT.GMTWrapper;
import gipsy.GEE.multitier.GMT.demands.NodeRegistration;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.AppConstants;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYPhysicalNode;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;
import gipsy.tests.GEE.multitier.GMT.GMTTestConsole;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GIPSYGMTController
{

    private GMTWrapper oGMT = null;
    private Thread oGMTThread = null;
    private GIPSYTiersController oGIPSYGMTTiersController;

    
    public void startGMTNode() throws MultiTierException
    {
        // Load the configuration file
        Configuration oNodeConfig;       

        try
        {
            // -- Load the GMT configuration file.
            oNodeConfig = GIPSYNode.loadFromFile(AppConstants.CONFIG_FILE_DIR+ "GMTNode.config");
            // Create a GIPSY Node instance
            GIPSYNode oNode = new GIPSYNode(oNodeConfig);

            // Starts a DST
            Configuration oGMTConfig = GIPSYNode.loadFromFile(AppConstants.CONFIG_FILE_DIR+ "GMTJini.config");
            oGMTConfig.setObjectProperty(GMTWrapper.GMT_NODE, oNode);
            IMultiTierWrapper oTier = oNode.getGMTController().addTier(oGMTConfig);
            oGIPSYGMTTiersController = new GIPSYTiersController(oTier);            
            Thread oDSTThread = new Thread(oGIPSYGMTTiersController);
            oDSTThread.start();
            oNode.isRegistered = true;
            oNode.start();
            //-- Keep track of the started GMT.
            GlobalInstance.getInstance().addNewRunningGMT(oGIPSYGMTTiersController);
            
        }
        catch (IOException e)
        {
          System.err.println(e.getMessage() +"\n" +e.getStackTrace());
        }
    }

    public void allocateTier(GIPSYTier poGIPSYTier)
    {
        //-- Default DST index = 0;
       oGIPSYGMTTiersController.allocateTier(poGIPSYTier, 0);        
    }
    
    public void deallocateTier(GIPSYTier poGIPSYTier)
    {
        if(poGIPSYTier != null)
        {
            oGIPSYGMTTiersController.deallocateTier(poGIPSYTier);            
        }
    }
    
    public void allocateTier()
    {
  /*      oGIPSYGMTTiersController.allocateTier("1", "DST", "bin/multitierEclipse/DSTProfiles/p10.config", 1);        
        oGIPSYGMTTiersController.allocateTier("1", "DST", "bin/multitierEclipse/DSTProfiles/p11.config", 1);        
        oGIPSYGMTTiersController.allocateTier("1", "DST", "bin/multitierEclipse/DSTProfiles/p12.config", 1);        
        oGIPSYGMTTiersController.allocateTier("1", "DST", "bin/multitierEclipse/DSTProfiles/p9.config", 1);        
        oGIPSYGMTTiersController.allocateTier( "1", "DWT", "bin/multitierEclipse/DWT.config", 1);
        oGIPSYGMTTiersController.allocateTier("1", "DGT", "bin/multitierEclipse/sDGT.config", 1);*/
    }

    
    public void startRegulareNode(GIPSYPhysicalNode poGIPSYPhysicalNode) throws IOException
    {
        if (poGIPSYPhysicalNode != null)
        {
         // Load the configuration file
            Configuration oNodeConfig = GIPSYNode.loadFromFile(AppConstants.CONFIG_FILE_DIR+"RegularNode.config");            
            // Create a GIPSY Node instance
            GIPSYNode oNode = new GIPSYNode(oNodeConfig);            
            poGIPSYPhysicalNode.setGIPSYNode(oNode);
        }        
    }

   
}
