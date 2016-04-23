package gipsy.tests.GEE.multitier.GMT;

import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.TierIdentity;
import gipsy.GEE.multitier.GMT.GMTWrapper;

/**
 * Super class of all GMT commands.
 * 
 * @author jos_m
 *
 */
public abstract class GMTCommand implements ICommand
{

    public final static int MAX_COMMAND_LENGTH = 1024;
    public final String DEFAULT_RESULT_STRING = this.getClass().getName() + " completed (default message)";

    protected GMTWrapper oGmtWrapper;
    protected String strResult;
    protected CommandId oCmdId;
    protected String strNodeID;
    protected TierIdentity oTierIdentity;

    enum CommandId
    {
        GMT_ALLOCATE, GMT_DEALLOCATE, GMT_EXIT, GMT_SET, GMT_RESUME, GMT_LOAD
    }

    GMTCommand(CommandId poCmdId, GMTWrapper poGmtWrapper)
    {
        init(poCmdId, poGmtWrapper, null, null);
    }

    GMTCommand(CommandId poCmdId, GMTWrapper poGmtWrapper, int piNodeIdx, TierIdentity poTierIdentity)
    {
        String strNodeId = poGmtWrapper.getInfoKeeper().getNodeRegistrations(0, Integer.MAX_VALUE)
                        .get(piNodeIdx).getNodeID();
        init(poCmdId, poGmtWrapper, strNodeId, poTierIdentity);
    }

    GMTCommand(CommandId poCmdId, GMTWrapper poGmtWrapper, String pstrNodeId, TierIdentity poTierIdentity)
    {
        init(poCmdId, poGmtWrapper, pstrNodeId, poTierIdentity);
    }

    void init(CommandId poCmdId, GMTWrapper poGmtWrapper, String pstrNodeId, TierIdentity poTierIdentity)
    {
        this.oCmdId = poCmdId;
        this.oGmtWrapper = poGmtWrapper;
        this.strNodeID = pstrNodeId;
        this.oTierIdentity = poTierIdentity;
        this.strResult = DEFAULT_RESULT_STRING;
    }

    /**
     * Executes the ICommand
     */
    abstract public void execute() throws MultiTierException;

    /********************************************************************************
     * 
     * GETTERS / SETTERS
     * 
     ********************************************************************************/
    public String getStrNodeID()
    {
        return strNodeID;
    }

    public void setStrNodeID(String strNodeID)
    {
        this.strNodeID = strNodeID;
    }

    public TierIdentity getTierIdentity()
    {
        return this.oTierIdentity;
    }

    public void setTierIdentity(TierIdentity oTierIdentity)
    {
        this.oTierIdentity = oTierIdentity;
    }

    @Override
    public String getResultStr()
    {
        return this.strResult;
    }

    @Override
    public void setResultStr(String strResult)
    {
        this.strResult = strResult;
    }
}
