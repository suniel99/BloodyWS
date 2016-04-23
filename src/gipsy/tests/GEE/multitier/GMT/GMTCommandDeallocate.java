package gipsy.tests.GEE.multitier.GMT;

import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.TierIdentity;
import gipsy.GEE.multitier.GMT.GMTWrapper;

/**
 * The Deallocate command tells the GMT to deallocate a Tier (ie DST, DWT or
 * DGT).
 * 
 * @author jos_m
 */
public class GMTCommandDeallocate extends GMTCommand
{

    private final String RESULT_STRING = "--Tier allocation finished!";
    private String[] astrTierIDs;

    public GMTCommandDeallocate(CommandId poCmdId, GMTWrapper poGmtWrapper, int piNodeIdx,
                    TierIdentity poTierIdentity, String[] pastrTierIDs)
    {
        super(poCmdId, poGmtWrapper, piNodeIdx, poTierIdentity);
        this.astrTierIDs = pastrTierIDs;

    }

    @Override
    public void execute() throws MultiTierException
    {
        try
        {
            if (this.astrTierIDs == null)
            {
                throw new MultiTierException("astrTierIDs is null");
            }
            this.oGmtWrapper.deallocateTier(this.strNodeID, this.oTierIdentity, this.astrTierIDs);
        }
        catch (MultiTierException e)
        {
            throw new MultiTierException("--Tier deallocation failed! " + e.getMessage());
        }
    }

    public String getResultString()
    {
        return RESULT_STRING;
    }

    /********************************************************************************
     * 
     * GETTERS / SETTERS
     * 
     ********************************************************************************/
    public String[] getTierIDs()
    {
        return this.astrTierIDs;
    }

    public void setTierIDs(String[] pastrTierIDs)
    {
        this.astrTierIDs = pastrTierIDs;
    }
}
