package gipsy.RIPE.editors.RunTimeGraphEditor.operator;

import gipsy.GEE.multitier.MultiTierException;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYPhysicalNode;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.operator.GIPSYCommands.COMMANDS_TYPE;
import gipsy.util.Trace;

public class GIPSYCommandRunner 
extends Thread
{
    private static final String MSG_PREFIX = "[" + Trace.getEnclosingClassName() + "] ";
    private COMMANDS_TYPE eCommandType;
    private GIPSYPhysicalNode oCurrentSelectedNode = null; 
    private GIPSYTier oGIPSYTier = null;
    
    public GIPSYCommandRunner(COMMANDS_TYPE peCommandType, GIPSYPhysicalNode poCurrentSelectedNode)
    {
        this.eCommandType = peCommandType;
        this.oCurrentSelectedNode = poCurrentSelectedNode;
    }
    public GIPSYCommandRunner(COMMANDS_TYPE peCommandType, GIPSYTier poGIPSYTier)
    {
        this.eCommandType = peCommandType;
        this.oGIPSYTier = poGIPSYTier;
    }
    public GIPSYCommandRunner(COMMANDS_TYPE peCommandType)
    {
        this.eCommandType = peCommandType;
    }
        
    public void run()
    {
        if (this.eCommandType == COMMANDS_TYPE.START_NODE)
        {
            GIPSYEntityManger.getInstance().startNode(oCurrentSelectedNode);
        }                
        else if (this.eCommandType == COMMANDS_TYPE.ALLOCATE_TIER)
        {
            if (this.oGIPSYTier != null)
            {
                GIPSYEntityManger.getInstance().allocateTier(this.oGIPSYTier);
            }
        }
        else if (this.eCommandType == COMMANDS_TYPE.DEALLOCATE_TIER)
        {
            if (this.oGIPSYTier != null)
            {
                GIPSYEntityManger.getInstance().deallocateTier(this.oGIPSYTier);
            }
        }
        else if (this.eCommandType == COMMANDS_TYPE.REGISTER_NODE)
        {
            try
            {
                GIPSYEntityManger.getInstance().registerNode(oCurrentSelectedNode);
            }
            catch (MultiTierException e)
            {
                System.err.println("An error occured while trying to register node " + oCurrentSelectedNode.getNodeName());
            }
        }
        else if(this.eCommandType == COMMANDS_TYPE.START_ALL)
        {
            //-- Start all defined GIPSY Nodes in the grpah and allocate tiers.
            GIPSYEntityManger.getInstance().startAllEntities();
        }
    }
    
    public static void allocateGIPSYTier(GIPSYTier poGIPSYTier)
    {
        if(poGIPSYTier != null)
        {
            if (!poGIPSYTier.isAllocated())
            {
                //-- allocates a GIPSY tier based on its type.
                GIPSYCommandRunner oGIPSYCommandRunner = new GIPSYCommandRunner(
                        COMMANDS_TYPE.ALLOCATE_TIER, poGIPSYTier);
                oGIPSYCommandRunner.start();
            }
            else
            {
                System.out.println(MSG_PREFIX + " Tier "+ poGIPSYTier.getTierName() + " is already allocated ...");
            }
        }
    }
    public static void StopGIPSYTier(GIPSYTier poGIPSYTier)
    {
        if(poGIPSYTier != null)
        {
            if (!poGIPSYTier.isDeAllocated())
            {
                //-- allocates a GIPSY tier based on its type.
                GIPSYCommandRunner oGIPSYCommandRunner = new GIPSYCommandRunner(
                        COMMANDS_TYPE.DEALLOCATE_TIER, poGIPSYTier);
                oGIPSYCommandRunner.start();
            }
            else
            {
                System.out.println(MSG_PREFIX + " Tier "+ poGIPSYTier.getTierName() + " is already deallocated ...");
            }
        }        
    }    
}
