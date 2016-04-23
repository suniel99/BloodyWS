package gipsy.tests.GEE.multitier.GMT;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import gipsy.GEE.multitier.IMultiTierWrapper;
import gipsy.GEE.multitier.MultiTierException;
import gipsy.GEE.multitier.GMT.GMTWrapper;

/**
 * Reads command strings from a FIFO and write back a result string to another.
 * The idea here is to use the 'gmtc' from a shell to interact with the GMT.
 * 
 * IDEAS: -Listen to a port instead of a pipe. We would be able to send commands
 * to the GMT from any node. -Condider using JNA to create the FIFO.
 * 
 * @author jos_m
 *
 */
public class GMTd implements Runnable
{
    private final static String DEFAULT_PIPE_PATH_IN = "/tmp/gmt_cmd_in";
    private final static String DEFAULT_PIPE_PATH_IN_OUT = "/tmp/gmt_cmd_out";

    private GMTWrapper oGmtWrapper = null;
    private Thread oGMTThread = null;

    private String oPipePath = DEFAULT_PIPE_PATH_IN;
    private String oOutPipePath = DEFAULT_PIPE_PATH_IN_OUT;
    private File oPipeFile;
    private File oOutPipeFile;

    public GMTd(IMultiTierWrapper poGMT) throws IOException, MultiTierException
    {
        this.oGmtWrapper = (GMTWrapper) poGMT;

        this.oPipeFile = new File(oPipePath);
        if (!oPipeFile.exists())
        {
            throw new MultiTierException("FIFO not found:" + this.oPipePath + ". Run 'gmtc -c' to create the in/out FIFO");
        }

        this.oOutPipeFile = new File(this.oOutPipePath);
        if (!this.oOutPipeFile.exists())
        {
            throw new MultiTierException("FIFO not found:" + this.oPipePath + ". Run 'gmtc -c' to create the in/out FIFO");
        }

        this.oGmtWrapper.startTier();
        this.oGMTThread = new Thread(this.oGmtWrapper);
        this.oGMTThread.start();
    }

    @Override
    public void run()
    {
        System.out.println("starting GMTd...");

        if (oPipeFile == null)
        {
            System.out.println("[ERROR] pipeFile is null");
            return;
        }

        try (BufferedReader oPipe = new BufferedReader(new FileReader(this.oPipeFile)))
        {
            String strLine;
            ICommand oCommand;
            GMTCommandParser oParser = new GMTCommandParser(this.oGmtWrapper);

            while (true)
            {
                strLine = oPipe.readLine();
                try
                {
                    if (strLine != null)
                    {
                        oCommand = oParser.parse(strLine);
                        oCommand.execute();

                        this.writeToOutPipe(oCommand.getResultStr() + "\n");
                    }
                    else
                    {
                        Thread.sleep(1000);
                    }
                }
                catch (GMTCommandParserException e)
                {
                    System.out.println(e.getMessage());
                    this.writeToOutPipe(e.getMessage() + "\n");
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("[ERROR] got " + e.getClass().getName() + " with message: " + e.getMessage());
        }
    }

    void writeToOutPipe(String pstrMsg)
    {
        try (PrintWriter out = new PrintWriter(new BufferedOutputStream(new FileOutputStream(this.oOutPipeFile))))
        {
            out.write(pstrMsg);
            out.flush();
        }
        catch (IOException e)
        {
            System.out.println("[ERROR] Could not write to FIFO:" + this.oOutPipePath);
        }
    }
}
