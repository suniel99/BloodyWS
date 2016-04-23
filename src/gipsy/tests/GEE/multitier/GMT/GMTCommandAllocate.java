package gipsy.tests.GEE.multitier.GMT;

import gipsy.Configuration;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.TierIdentity;
import gipsy.GEE.multitier.GMT.GMTWrapper;
import gipsy.GEE.multitier.GMT.demands.DSTRegistration;

/**
 * The Allocate command is used to allocate tiers (DST, DWT, DGT) via the GMT.
 *
 * @author jos_m
 */
public class GMTCommandAllocate extends GMTCommand
{
    protected Configuration oTierConfig;
    protected DSTRegistration oDataDST;
    protected int iNumOfInstances;

    GMTCommandAllocate(CommandId poCmdId, GMTWrapper poGmtWrapper, int piNodeIdx, TierIdentity poTierIdentity)
    {
        super(poCmdId, poGmtWrapper, piNodeIdx, poTierIdentity);
    }

    GMTCommandAllocate(CommandId poCmdId, GMTWrapper poGmtWrapper, int piNodeIdx, TierIdentity poTierIdentity,
                    Configuration poTierConfig, DSTRegistration poDataDST, int piNumOfInstances)
    {
        super(poCmdId, poGmtWrapper, piNodeIdx, poTierIdentity);

        this.oTierConfig = poTierConfig;
        this.oDataDST = poDataDST;
        this.iNumOfInstances = piNumOfInstances;
    }

    public void execute() throws MultiTierException
    {
        StringBuffer sb = new StringBuffer("--Allocating ");

        try
        {

            sb.append(this.iNumOfInstances + " ");
            sb.append(this.oTierIdentity.toString() + "(s) in Node " + this.strNodeID);
            sb.append("\n");
            this.setResultStr(sb.toString());

            this.oGmtWrapper.allocateTier(this.strNodeID, this.oTierIdentity, this.oTierConfig, this.oDataDST, this.iNumOfInstances);

            sb.append("--Tier allocation finished!");
            this.setResultStr(sb.toString());
        }
        catch (MultiTierException e) 
        {
            throw new MultiTierException("--Tier deallocation failed! " + e.getMessage());
        }
    }

    /********************************************************************************
     * 
     * GETTERS / SETTERS
     * 
     ********************************************************************************/
    public Configuration getTierConfig()
    {
        return this.oTierConfig;
    }

    public void setTierConfig(Configuration poTierConfig)
    {
        this.oTierConfig = poTierConfig;
    }

    public DSTRegistration getDataDST()
    {
        return this.oDataDST;
    }

    public void setDataDST(DSTRegistration dataDST)
    {
        this.oDataDST = dataDST;
    }

    public int getNumOfInstances()
    {
        return this.iNumOfInstances;
    }

    public void setNumOfInstances(int numOfInstances)
    {
        this.iNumOfInstances = numOfInstances;
    }
}
