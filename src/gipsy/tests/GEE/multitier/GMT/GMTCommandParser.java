package gipsy.tests.GEE.multitier.GMT;

import gipsy.Configuration;
import gipsy.GEE.multitier.GIPSYNode;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.TierIdentity;
import gipsy.GEE.multitier.DGT.DGTWrapper;
import gipsy.GEE.multitier.DWT.DWTWrapper;
import gipsy.GEE.multitier.GMT.GMTWrapper;
import gipsy.GEE.multitier.GMT.demands.DSTRegistration;
import gipsy.tests.GEE.multitier.GMT.GMTCommand.CommandId;

/**
 * The parsing logic originally in GMTTestConsole has been extracted here.
 *
 * @author jos_m
 */
public class GMTCommandParser
{

    private final static int CMD_NODE_IDX = 1;
    private final static int CMD_TIER_IDENTITY_IDX = 2;
    private final static int CMD_TIER_CONFIG_IDX = 3;
    private final static int CMD_ALLOC_DST_NUM_OF_INSTANCES_IDX = 4;

    private final static int CMD_LENGTH_ALLOC_DST_WITH_NUM_OF_INSTANCES = 5;

    private static final String USAGE = "" + "Note: [] indicates the argument is optional\n"
                    + "  allocate NodeIndex DST JiniDST.config [number of instances to start]\n"
                    + "  allocate NodeIndex DGT sDGT.config DSTIndexAtGMT [number of instances to start]\n"
                    + "  allocate NodeIndex DWT DWT.config DSTIndexAtGMT [number of instances to start]\n"
                    + "  set policy 0\n" + "  load commands commandFile\n" + "  resume\n" + "  exit";

    private GMTWrapper oGmtWrapper;

    GMTCommandParser(GMTWrapper poGmtWrapper)
    {
        this.oGmtWrapper = poGmtWrapper;
    }

    /**
     * Parses the command string passed as argument and return the corresponding
     * ICommand Object is successful.
     */
    ICommand parse(String pstrInput) throws GMTCommandParserException
    {
        ICommand oResult = null;
        String[] astrCMD;
        
        if (pstrInput == null) {
            throw new GMTCommandParserException("pstrInput is null");
        }

        astrCMD = pstrInput.trim().split("\\s+");

        if (astrCMD.length == 0)
        {
            throw new GMTCommandParserException("--Illegal command, see usage: " + USAGE);
        }


        if (astrCMD[0].equalsIgnoreCase("allocate"))
        {
            try
            {
                int nodeIdx = Integer.parseInt(astrCMD[CMD_NODE_IDX]);
                TierIdentity oTierIdentity = TierIdentity.valueOf(astrCMD[CMD_TIER_IDENTITY_IDX]);
                Configuration oTierConfig = GIPSYNode.loadFromFile(astrCMD[CMD_TIER_CONFIG_IDX]);
                DSTRegistration oDataDST = null;
                int iNumOfInstances = 1;
                String strConnectTo = "";

                if (oTierIdentity == TierIdentity.DST)
                {
                    if (astrCMD.length == CMD_LENGTH_ALLOC_DST_WITH_NUM_OF_INSTANCES)
                    {
                        iNumOfInstances = Integer.parseInt(astrCMD[CMD_ALLOC_DST_NUM_OF_INSTANCES_IDX]);
                    }
                }
                else if (oTierIdentity == TierIdentity.DGT || oTierIdentity == TierIdentity.DWT)
                {
                    if (astrCMD.length < 5)
                    {
                        throw new GMTCommandParserException("--Illegal command, see usage:");
                    }

                    if (astrCMD.length == 6)
                    {
                        iNumOfInstances = Integer.parseInt(astrCMD[5]);
                    }

                    int iDSTIndex = Integer.parseInt(astrCMD[4]);

                    oDataDST = oGmtWrapper.getInfoKeeper().getDSTRegistrations(0, Integer.MAX_VALUE).get(iDSTIndex);
                    if (oDataDST == null || oDataDST.getActiveConnectionCount() >= oDataDST.getMaxActiveConnection())
                    {
                        throw new GMTCommandParserException("--The DST chozen is not available for this "
                                        + oTierIdentity.toString() + ": max " + oDataDST.getMaxActiveConnection()
                                        + " connections allowed.\n");
                    }

                    Configuration oTAConfig = oDataDST.getTAConfig();

                    if (oTierIdentity == TierIdentity.DGT)
                    {
                        oTierConfig.setObjectProperty(DGTWrapper.TA_CONFIG, oTAConfig);
                    }
                    else
                    {
                        oTierConfig.setObjectProperty(DWTWrapper.TA_CONFIG, oTAConfig);
                    }

                    strConnectTo = " connecting to DST (index " + iDSTIndex + ")";
                }

                System.out.println("--Allocating " + iNumOfInstances + " " + oTierIdentity.toString() + "(s) in Node "
                                + nodeIdx + " using " + astrCMD[CMD_TIER_CONFIG_IDX] + strConnectTo + " ...\n");

                Object[] args = { nodeIdx, oTierIdentity, oTierConfig, oDataDST, iNumOfInstances };
                oResult = GMTCommandSimpleFactory.createGMTCommand(CommandId.GMT_ALLOCATE, oGmtWrapper, args);
            }
            catch (NumberFormatException oNumberFormatException)
            {
                throw new GMTCommandParserException("--Illegal command, see usage");
            }
            catch (IndexOutOfBoundsException oIndexException)
            {
                throw new GMTCommandParserException("--DST index out of bounds!");
            }
            catch (Exception e)
            {
                throw new GMTCommandParserException(e.getMessage());
            }
        }
        /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
         * 
         * COMMAND : deallocate
         * 
         *-------------------------------------------------------------------------------*/
        else if (astrCMD[0].equalsIgnoreCase("deallocate"))
        {
            int nodeIdx = Integer.parseInt(astrCMD[1]);
            TierIdentity oTierIdentity = TierIdentity.valueOf(astrCMD[2]);
            int iNumOfTierIDs = astrCMD.length - 3;
            String[] astrTierIDs = new String[iNumOfTierIDs];

            for (int i = 0; i < astrTierIDs.length; i++)
            {
                astrTierIDs[i] = astrCMD[i + 3];
            }

            Object[] args = { nodeIdx, oTierIdentity, astrTierIDs };
            oResult = GMTCommandSimpleFactory.createGMTCommand(CommandId.GMT_DEALLOCATE, oGmtWrapper, args);
        }
        /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
         * 
         * COMMAND : exit
         * 
         *-------------------------------------------------------------------------------*/
        else if (astrCMD[0].trim().equalsIgnoreCase("exit"))
        {
            oResult = GMTCommandSimpleFactory.createGMTCommand(CommandId.GMT_EXIT, oGmtWrapper);
        }
        /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
         * 
         * COMMAND : set
         * 
         *-------------------------------------------------------------------------------*/
        else if (astrCMD[0].trim().equalsIgnoreCase("set"))
        {
            if (astrCMD.length < 3)
            {
                throw new GMTCommandParserException("--Illegal command, see usage:" + USAGE);
            }

            int iPolicy = 0;
            try
            {
                iPolicy = Integer.parseInt(astrCMD[2]);

                Object[] args = { iPolicy };
                oResult = GMTCommandSimpleFactory.createGMTCommand(CommandId.GMT_SET, oGmtWrapper, args);

            }
            catch (NumberFormatException oNumberFormatException)
            {
                throw new GMTCommandParserException("--Illegal command, see usage:" + USAGE);
            }

        }
        else
        {
            throw new GMTCommandParserException("--Illegal command, see usage:" + USAGE);
        }

        return oResult;
    }
}
