package gipsy.RIPE.editors.RunTimeGraphEditor.operator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JScrollPane;

import gipsy.Configuration;
import gipsy.GEE.multitier.GIPSYNode;
import gipsy.GEE.multitier.IMultiTierWrapper;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.TierIdentity;
import gipsy.GEE.multitier.DGT.DGTWrapper;
import gipsy.GEE.multitier.DWT.DWTWrapper;
import gipsy.GEE.multitier.GMT.GMTWrapper;
import gipsy.GEE.multitier.GMT.demands.DSTRegistration;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.MessageAndErrorConsole;

public class GIPSYTiersController implements Runnable
{

    private GMTWrapper oGMT = null;
    private Thread oGMTThread = null;
    private PipedInputStream oPipedInOut;
    private PipedOutputStream oPipedOutOut;
    private PrintStream oPrintOut;
    private PrintStream oPrintErr;

    private static final String USAGE = ""
            + "Note: [] indicates the argument is optional\n"
            + "  allocate NodeIndex DST JiniDST.config [number of instances to start]\n"
            + "  allocate NodeIndex DGT sDGT.config DSTIndexAtGMT [number of instances to start]\n"
            + "  allocate NodeIndex DWT DWT.config DSTIndexAtGMT [number of instances to start]\n"
            + "  set policy 0\n" + "  load commands commandFile\n"
            + "  resume\n" + "  exit";

    public GIPSYTiersController(IMultiTierWrapper poGMT) throws IOException,
            MultiTierException
    {
        this.oGMT = (GMTWrapper) poGMT;
        //
        this.oPipedInOut = new PipedInputStream();
        this.oPipedOutOut = new PipedOutputStream(oPipedInOut);

        OutputStream oOut = MessageAndErrorConsole.getInstance()
                .getOutStreamAddGMTTab();
        this.oPrintOut = new PrintStream(oOut, true);
        this.oGMT.setOut(this.oPrintOut);
        this.oPrintErr = this.oPrintOut;
        this.oGMT.setErr(this.oPrintErr);

        // Start GMT
        this.oGMT.startTier();
        this.oGMTThread = new Thread(this.oGMT);
        this.oGMTThread.start();

    }

    public void stopGMT()
    {
        synchronized (oGMTThread)
        {
            try
            {
                oGMT.stopTier();
                oGMTThread.interrupt();
                System.out.println("GMT thread stopped: " + oGMTThread);
            }
            catch (Exception oException)
            {
                oException.printStackTrace(System.err);
            }
        }
    }

    public void allocateTier(GIPSYTier poGIPSYTier, int iDSTIndex)
    {
        try
        {
            int piHowManyToAllocate = 1;
            int iNodeIndex = Integer.parseInt(poGIPSYTier.getGIPSYNodeID());
            String strNodeID = oGMT.getInfoKeeper().getNodeRegistrations(0, Integer.MAX_VALUE).get(iNodeIndex).getNodeID();
            TierIdentity oTierIdentity = TierIdentity.valueOf(poGIPSYTier.getTierType());

            Configuration oTierConfig = GIPSYNode.loadFromFile(poGIPSYTier.getConfigFilePath());

            DSTRegistration oDataDST = null;

            String strConnectTo = "";
           
            if(oTierIdentity == TierIdentity.DGT || oTierIdentity == TierIdentity.DWT)
            {
                
                oDataDST = oGMT.getInfoKeeper()
                .getDSTRegistrations(0, Integer.MAX_VALUE)
                .get(iDSTIndex);
                
                if(oDataDST == null 
                        || oDataDST.getActiveConnectionCount() >= oDataDST.getMaxActiveConnection())
                {
                    oPrintOut.println("--The DST chozen is not available for this " 
                            + oTierIdentity.toString() + ": max " +
                            oDataDST.getMaxActiveConnection() + " connections allowed.\n");
                    return;
                }
                else
                {
                    Configuration oTAConfig = oDataDST.getTAConfig();
                    if(oTierIdentity == TierIdentity.DGT)
                    {
                        oTierConfig.setObjectProperty(DGTWrapper.TA_CONFIG, oTAConfig);
                    }
                    else
                    {
                        oTierConfig.setObjectProperty(DWTWrapper.TA_CONFIG, oTAConfig);
                    }
                    
                    strConnectTo = " connecting to DST (index " + iDSTIndex + ")";
                }
    
                piHowManyToAllocate = Integer.parseInt(poGIPSYTier.getHowManyTierPerNode());
                if(piHowManyToAllocate < 1)
                {
                    piHowManyToAllocate = 0;
                }
            }

                oPrintOut.println("--Allocating " + piHowManyToAllocate + " "
                        + oTierIdentity.toString() + "(s) in Node " + strNodeID
                        + " using " + poGIPSYTier.getConfigFilePath() + strConnectTo + " ...");
                
                oGMT.allocateTier(strNodeID, oTierIdentity, oTierConfig, oDataDST, piHowManyToAllocate);
                oPrintOut.println("--Tier allocation finished!\n");
        }
        catch (NumberFormatException oNumberFormatException)
        {
            oPrintErr.println("--Illegal command, see usage:" + USAGE);
        }
        catch (IndexOutOfBoundsException oIndexException)
        {
            oPrintErr.println("--DST index out of bounds!");
        }
        catch (Exception e)
        {
            e.printStackTrace(oPrintErr);
        }
    }

    @Override
    public void run()
    {

    }

    public void deallocateTier(GIPSYTier poGIPSYTier)
    {
        int iNodeIndex = Integer.parseInt(poGIPSYTier.getGIPSYNodeID());
        String strNodeID = oGMT.getInfoKeeper().getNodeRegistrations(0, Integer.MAX_VALUE).get(iNodeIndex).getNodeID();

        TierIdentity oTierIdentity = TierIdentity.valueOf(poGIPSYTier.getTierType());

        //-- Currently hardcoded to 0;
        int iNumOfTierIDs = 0;
        String[] astrTierIDs = new String[1];
        astrTierIDs[0] = "0";
        try
        {
            oGMT.deallocateTier(strNodeID, oTierIdentity, astrTierIDs);
            oPrintErr.println("--Tier deallocation finished!");
        }
        catch(MultiTierException oDeallocationException)
        {
            oPrintErr.println("--Tier deallocation failed!");
        }
    }
}
