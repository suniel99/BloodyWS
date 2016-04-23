package gipsy.RIPE;

import gipsy.GEE.GEE;
import gipsy.GIPC.GIPC;
import gipsy.tests.Regression;
import gipsy.RIPE.RuntimeSystem.Controller;
import gipsy.RIPE.RuntimeSystem.IVWInspector;
import gipsy.RIPE.editors.TextualEditor;
import gipsy.RIPE.editors.DFGEditor.DFGEditor;
import gipsy.RIPE.editors.RunTimeGraphEditor.ApplicationStarter;
import gipsy.util.NotImplementedException;
import marf.util.BaseThread;
import marf.util.Debug;
import marf.util.OptionProcessor;


/**
 * <p>The main RIPE module and facade to the rest of the RIPE.</p>
 *
 * @author Serguei Mokhov
 * @version $Id: RIPE.java,v 1.10 2012/06/19 03:19:46 mokhov Exp $
 * @since 1.0.0
 */
public class RIPE
extends BaseThread
{
	public static final int OPT_HELP_SHORT = 0;
	public static final int OPT_HELP_LONG = 1;
	
	public static final int OPT_GIPC = 2;
	public static final int OPT_GEE = 3;
	public static final int OPT_DFG = 4;
	public static final int OPT_TXT = 5;
	
	public static final int OPT_DEBUG = 6;
	
	public static final int OPT_REGRESSION = 7;

	/**
	 * Start graphical graph-based GMT GUI.
	 * @since June 18, 2012
	 */
	public static final int OPT_GGMT = 8;

	private Controller oController = null;
	private IVWInspector oIVWInspector = null;
	private DFGEditor oDFGEditor = null;
	private TextualEditor oTextualEditor = null;

	private OptionProcessor oOptionProcessor = null;

	private String[] astrArgv;
	
	/**
	 * @param pastrArgv
	 * @throws RIPEException
	 */
	public RIPE(String[] pastrArgv)
	throws RIPEException
	{
		super("RIPE $Id: RIPE.java,v 1.10 2012/06/19 03:19:46 mokhov Exp $");

		this.astrArgv = pastrArgv;
		
		this.oOptionProcessor = new OptionProcessor();

		this.oOptionProcessor.addValidOption(OPT_HELP_LONG, "--help");
		this.oOptionProcessor.addValidOption(OPT_HELP_SHORT, "-h");

		this.oOptionProcessor.addValidOption(OPT_GGMT, "--ggmt");

		this.oOptionProcessor.addValidOption(OPT_GIPC, "--gipc", true);
		this.oOptionProcessor.addValidOption(OPT_GEE, "--gee", true);
		this.oOptionProcessor.addValidOption(OPT_REGRESSION, "--regression", true);
		this.oOptionProcessor.addValidOption(OPT_DFG, "--dfg", true);
		this.oOptionProcessor.addValidOption(OPT_TXT, "--txt", true);
/*		this.oOptionProcessor.addValidOption(OPT_GIPC, "--gipc");
		this.oOptionProcessor.addValidOption(OPT_GEE, "--gee");
		this.oOptionProcessor.addValidOption(OPT_REGRESSION, "--regression");
		this.oOptionProcessor.addValidOption(OPT_DFG, "--dfg");
		this.oOptionProcessor.addValidOption(OPT_TXT, "--txt");*/
		
		this.oOptionProcessor.addValidOption(OPT_DEBUG, "--debug");

		this.oOptionProcessor.parse(pastrArgv);
		
		if(this.oOptionProcessor.isActiveOption(OPT_DEBUG))
		{
			Debug.enableDebug();
		}
	}

	@Override
	public void run()
	{
		try
		{
			BaseThread oModule = null;
			
			if
			(
				this.oOptionProcessor.isActiveOption(OPT_HELP_LONG)
				|| this.oOptionProcessor.isActiveOption(OPT_HELP_SHORT)
			)
			{
				usage();
			}

			else if(this.oOptionProcessor.isActiveOption(OPT_GGMT))
			{
				ApplicationStarter.main(this.astrArgv);
			}
			
			else if(this.oOptionProcessor.isActiveOption(OPT_GIPC))
			{
				//GIPC.main(this.astrArgv);
				this.astrArgv = this.oOptionProcessor.getOptionArgument(OPT_GIPC).split(" "); 
				oModule = new BaseThread(new GIPC(this.astrArgv), "RIPE-started GIPC");
			}
			
			else if(this.oOptionProcessor.isActiveOption(OPT_GEE))
			{
				//GEE.main(this.astrArgv);
				this.astrArgv = this.oOptionProcessor.getOptionArgument(OPT_GEE).split(" "); 
				oModule = new BaseThread(new GEE(this.astrArgv), "RIPE-started GEE");
			}
			
			else if(this.oOptionProcessor.isActiveOption(OPT_REGRESSION))
			{
				//Regression.main(this.astrArgv);
				this.astrArgv = this.oOptionProcessor.getOptionArgument(OPT_REGRESSION).split(" "); 
				oModule = new BaseThread(new Regression(this.astrArgv), "RIPE-started Regression");
			}
			
			else if(this.oOptionProcessor.isActiveOption(OPT_DFG))
			{
				throw new NotImplementedException("OPT_DFG");
			}
			
			else if(this.oOptionProcessor.isActiveOption(OPT_TXT))
			{
				throw new NotImplementedException("OPT_TXT");
			}
			
			else
			{
				Debug.debug("Unhadling options: " + this.oOptionProcessor);
				return;
			}
			
			if(oModule != null)
			{
				System.out.println("RIPE: starting up module: " + oModule);
				oModule.start();
				System.out.println("RIPE: waiting for module: " + oModule);
				oModule.join();
				System.out.println("RIPE: module terminated: " + oModule);
			}
		}
		catch(Exception e)
		{
			System.err.println(System.err);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Starts the main RIPE thread.
	 * @param argv command-line arguments
	 */
	public static final void main(String[] argv)
	{
		try
		{
			new RIPE(argv).start();
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}

	/**
	 * Displays usage information to STDOUT.
	 */
	public static final void usage()
	{
		System.out.println
		(
			"Usage:\n\n" +
			"\tjava RIPE <options>\n\n" +

			"Where options are one of the following (maybe combined with --debug):\n\n" +

			"\t--help or -h\n" +
			"\t\tdisplays application usage information\n\n" +

			"\t--ggmt\n" +
			"\t\ttells RIPE to invoke graph-based GMT GUI\n\n" +

			"\t--gipc='<GIPC OPTIONS>'\n" +
			"\t\ttells RIPE to invoke GIPC with a set of GIPC options\n\n" +

			"\t--gee='<GEE OPTIONS>'\n" +
			"\t\ttells RIPE to invoke GEE with a set of GEE options\n\n" +

			"\t--regression='<GEE OPTIONS>'\n" +
			"\t\ttells RIPE to invoke Regression testing with a set of its options\n\n" +

			"\t--dfg='<DFG EDITOR OPTIONS>'\n" +
			"\t\ttells RIPE to start the DFG editor with its options\n\n" +

			"\t--txt='<TEXTUAL EDITOR OPTIONS>'\n" +
			"\t\ttells RIPE to start the textual editor with its options\n\n" +

			"\t--debug\n" +
			"\t\tto run in a debug/verbose\n" +

			"\n"
		);
	}
}

// EOF
