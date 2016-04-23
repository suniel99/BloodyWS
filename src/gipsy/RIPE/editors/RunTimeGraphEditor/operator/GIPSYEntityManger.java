package gipsy.RIPE.editors.RunTimeGraphEditor.operator;

import gipsy.GEE.multitier.GIPSYNode;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYPhysicalNode;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;
import gipsy.util.Trace;

public class GIPSYEntityManger implements Runnable
{
    
    private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";
    private static GIPSYEntityManger oSingleInstance= null;
    private GIPSYGMTController oGIPSYGMTController;
    private boolean bIsGMTStarted;

    private GIPSYEntityManger()
    {
        oGIPSYGMTController = new GIPSYGMTController();
        bIsGMTStarted = false;
    }
    
    public void allocateTier(GIPSYTier poGIPSYTier)
    {
        try
        {
            if( poGIPSYTier != null)
            {
               if(bIsGMTStarted)
               {
                   //-- Starts a GIPSY tier.
                   oGIPSYGMTController.allocateTier(poGIPSYTier);
                   poGIPSYTier.setIsAllocated(true);
               }
            }            
        }
        catch(Exception e)
        {
         System.err.println(MSG_PREFIX + " An error occured while trying to allocate tier " + poGIPSYTier.getTierName());   
        }       
    }
    
    public void deallocateTier(GIPSYTier poGIPSYTier)
    {
        try
        {
            if( poGIPSYTier != null)
            {
               if(bIsGMTStarted)
               {
                   //-- Starts a GIPSY tier.
                   oGIPSYGMTController.deallocateTier(poGIPSYTier);
                   poGIPSYTier.setIsDeAllocated(true);                   
               }
            }            
        }
        catch(Exception e)
        {
         System.err.println(MSG_PREFIX + " An error occured while trying to dallocate tier " + poGIPSYTier.getTierName());   
        }     
    }
    
    public void startNode(GIPSYPhysicalNode poGIPSYPhysicalNode)
    {
        try
        {
            if( poGIPSYPhysicalNode != null)
            {
                //-- If no GMT has been started then start one. 
               if(!bIsGMTStarted)
               {
                   bIsGMTStarted = true;
                   oGIPSYGMTController.startGMTNode();   
                   poGIPSYPhysicalNode.setIsStarted(true);
                   poGIPSYPhysicalNode.setIsRegistred(true);
                   GlobalInstance.getInstance().setIsOneGMTIsRunning(true);
               }
               else
               {
                   //-- Start a regular node.
                   oGIPSYGMTController.startRegulareNode(poGIPSYPhysicalNode);
                    System.out.println(MSG_PREFIX +" node "+ poGIPSYPhysicalNode.getNodeName() + " started successfully.");
                    poGIPSYPhysicalNode.setIsStarted(true);
               }
            }            
        }
        catch(Exception e)
        {
         System.err.println(MSG_PREFIX + " An error occured while trying to start node " + poGIPSYPhysicalNode.getNodeName());   
        }        
    }
    
    public void registerNode(GIPSYPhysicalNode poCurrentSelectedNode) throws MultiTierException
    {
        if (!poCurrentSelectedNode.isRegistred())
        {
            poCurrentSelectedNode.getGIPSYNode().registerNode();
            poCurrentSelectedNode.getGIPSYNode().start();
            poCurrentSelectedNode.setIsRegistred(true);
        }
    } 
    
    public void startAllEntities()
    {
        try
        {
            //-- Start nodes
            for (GIPSYPhysicalNode oNode : GlobalInstance.getInstance().getGIPSYNodesList())
            {
                startNode(oNode);
                registerNode(oNode);
            }
            //-- Allocate Tiers
            for (GIPSYTier oGIPSYTier : GlobalInstance.getInstance().getGIPSYTiersList())
            {
                if (!oGIPSYTier.getTierType().equals("GMT"))
                {
                    allocateTier(oGIPSYTier);
                }
            }
        }
        catch (Exception e) {
            
        }        
    }
    
    public static GIPSYEntityManger getInstance()
    {
        if(oSingleInstance == null)
        {
            oSingleInstance = new GIPSYEntityManger();
        }
        return oSingleInstance;
    }

    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        
    }

  
}
