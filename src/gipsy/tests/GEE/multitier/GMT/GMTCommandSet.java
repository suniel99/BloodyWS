package gipsy.tests.GEE.multitier.GMT;

import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.GMT.GMTWrapper;

public class GMTCommandSet extends GMTCommand
{
    private int iPolicy;

    GMTCommandSet(CommandId poCmdId, GMTWrapper poGmtWrapper, int piPolicy)
    {
        super(poCmdId, poGmtWrapper);
        this.iPolicy = piPolicy;
    }

    @Override
    public void execute() throws MultiTierException
    {
        String strMsg = this.getSetPolicyString(this.iPolicy);
        this.setResultStr(strMsg);

        if (this.oGmtWrapper.isValidPolicyNumber(this.iPolicy))
        {
            this.oGmtWrapper.iRecoverPolicy = this.iPolicy;
        }
    }

    /**
     * Returns the log message for the set command.
     * 
     * @param piPolicy
     * @return
     */
    private String getSetPolicyString(int piPolicy)
    {
        String strResult = null;
        switch (piPolicy)
        {
            case GMTWrapper.LET_IT_BE:
                strResult = "--Policy set: LET_IT_BE.";
                break;
            case GMTWrapper.TRY_NEXT_UNTIL_THE_END:
                strResult = "--Policy set: TRY_NEXT_UNTIL_THE_END.";
                break;
            case GMTWrapper.TRY_NEXT_AND_WRAP_AROUND:
                strResult = "--Policy set: TRY_NEXT_AND_WRAP_AROUND.";
                break;
            case GMTWrapper.IF_CRASH_THEN_TRY_NEXT_UNTIL_THE_END:
                strResult = "--Policy set: IF_CRASH_THEN_TRY_NEXT_UNTIL_THE_END.";
                break;
            case GMTWrapper.IF_CRASH_THEN_RESTART:
                strResult = "--Policy set: IF_CRASH_THEN_RESTART.";
                break;
            default:
                strResult = "--Wrong policy number specified!";

        }

        return strResult;
    }
}
