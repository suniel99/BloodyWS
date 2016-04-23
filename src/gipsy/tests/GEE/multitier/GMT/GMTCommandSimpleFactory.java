package gipsy.tests.GEE.multitier.GMT;

import gipsy.Configuration;
import gipsy.GEE.multitier.TierIdentity;
import gipsy.GEE.multitier.GMT.GMTWrapper;
import gipsy.GEE.multitier.GMT.demands.DSTRegistration;
import gipsy.tests.GEE.multitier.GMT.GMTCommand.CommandId;

/**
 * To create the ICommands.
 * 
 * @author jos_m
 *
 */
public class GMTCommandSimpleFactory
{

    private static final int ARG_POLICY_IDX = 0;
    private static final int ARG_NODE_IDX = 0;
    private static final int ARG_TIER_IDENTITY = 1;
    private static final int ARG_TIER_CONFIG = 2;
    private static final int ARG_ARRAY_OF_TIER_IDS = 2;
    private static final int ARG_TIER_DATA = 3;
    private static final int ARG_NB_INSTANCES = 4;

    static ICommand createGMTCommand(CommandId poCmdId, GMTWrapper poGmtWrapper)
    {
        Object[] aoArgs = {};
        return createGMTCommand(poCmdId, poGmtWrapper, aoArgs);
    }

    static ICommand createGMTCommand(CommandId poCmdId, GMTWrapper poGmtWrapper, Object[] paoArgs)
    {

        ICommand oResult = null;

        switch (poCmdId)
        {
            case GMT_ALLOCATE:
                oResult = new GMTCommandAllocate(CommandId.GMT_ALLOCATE, poGmtWrapper, (Integer) paoArgs[ARG_NODE_IDX],
                                (TierIdentity) paoArgs[ARG_TIER_IDENTITY], (Configuration) paoArgs[ARG_TIER_CONFIG],
                                (DSTRegistration) paoArgs[ARG_TIER_DATA], (Integer) paoArgs[ARG_NB_INSTANCES]);
                break;
            case GMT_DEALLOCATE:
                oResult = new GMTCommandDeallocate(CommandId.GMT_DEALLOCATE, poGmtWrapper, (Integer) paoArgs[ARG_NODE_IDX],
                                (TierIdentity) paoArgs[ARG_TIER_IDENTITY], (String[]) paoArgs[ARG_ARRAY_OF_TIER_IDS]);
                break;
            case GMT_EXIT:
                oResult = new GMTCommandExit(CommandId.GMT_EXIT, poGmtWrapper);
                break;
            case GMT_SET:
                oResult = new GMTCommandSet(CommandId.GMT_SET, poGmtWrapper, (Integer) paoArgs[ARG_POLICY_IDX]);
                break;
            case GMT_RESUME: // TODO
                break;
        }

        return oResult;
    }
}
