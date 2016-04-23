package gipsy.tests.GEE.multitier.GMT;

import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.GMT.GMTWrapper;

/**
 * @author jos_m
 */
public class GMTCommandExit extends GMTCommand
{

    private int iSleepTimeInSeconds;

    GMTCommandExit(CommandId poCmdId, GMTWrapper poGmtWrapper)
    {
        super(poCmdId, poGmtWrapper);
        this.iSleepTimeInSeconds = 5;
    }

    @Override
    public void execute() throws MultiTierException
    {
        this.setResultStr("--exiting in " + this.iSleepTimeInSeconds + " second(s)");

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                boolean bDone = false;
                while (!bDone)
                {
                    try
                    {
                        Thread.sleep(1000 * iSleepTimeInSeconds);
                        bDone = true;
                    }
                    catch (InterruptedException e)
                    {
                        System.out.println("sleep failed");
                    }
                }

                System.exit(0); // TODO exit cleanly svp
            }
        }).start();
    }
}
