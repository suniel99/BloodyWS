package gipsy;

import gipsy.RIPE.RIPE;
import marf.util.Arrays;
import marf.util.Debug;
import marf.util.OptionProcessor;
import marf.util.BaseThread;

/**
 * The "GIPSY" Server; also meant to be a general entry point for
 * all GIPSY components.
 *
 * Meant to be a stand-alone GIPSY "server" accepting GIPSY programs
 * for processing, e.g. from a web front-end.</p>
 * 
 * @author Serguei Mokhov
 * @version $Id: GIPSY.java,v 1.18 2011/07/10 02:29:57 mokhov Exp $
 * @since Inception
 */
public class GIPSY
extends BaseThread
{
	/**
	 * Option processing utility.
	 */
	private OptionProcessor oOptionProcessor;

	/**
	 * Global GIPSY-instance-wide configuration. 
	 */
	private static Configuration soConfiguration = new Configuration(); 
	
	/**
	 * Compile-only option enumeration.
	 */	
	private static final int OPT_COMPILE_ONLY = 0;
	
	/**
	 * Debug option enumeration.
	 */	
	private static final int OPT_DEBUG = 1;

	/**
	 * Compile-only flag to be passed to GIPC.
	 */	
	private boolean bCompileOnly = false;
	
	/**
	 * Debug flag to be passed to all modules.
	 */	
	private boolean bDebug = false;
	
	/**
	 * Constructs a GIPSY instance and configures it bases in
	 * command-line options supplied.
	 * @param argv command-line arguments
	 */
	public GIPSY(String[] argv)
	{
		super("GIPSY Server $Revision: 1.18 $, TID = " + getNextTID());
		
		if(argv.length > 0 && (argv[0].equals("--help") || argv[0].equals("-h")))
		{
			usage();
			System.exit(0);
		}
		
		this.oOptionProcessor = new OptionProcessor();
		
		this.oOptionProcessor.addValidOption(OPT_COMPILE_ONLY, "--compile-only");
		this.oOptionProcessor.addValidOption(OPT_DEBUG, "--debug");
		
		this.oOptionProcessor.parse(argv);
		
		if(this.oOptionProcessor.getInvalidOptions().size() > 1)
		{
			System.err.println
			(
				"Extra options: \n" +
				this.oOptionProcessor.getInvalidOptions() +
				" -- will be passed to RIPE verbatim."
			);
		}
		
		this.bCompileOnly = this.oOptionProcessor.isActiveOption(OPT_COMPILE_ONLY);
		this.bDebug = this.oOptionProcessor.isActiveOption(OPT_DEBUG);
		Debug.enableDebug(this.bDebug);
	}

	/**
	 * @return global Configuration object instance
	 * @since September 3, 2009
	 * @see Configuration
	 */
	public static Configuration getConfugration()
	{
		return soConfiguration;
	}
	
	/**
	 * Main thread of GIPSY.
	 * @param argv command-line arguments
	 */
	public static final void main(String[] argv)
	{
		new GIPSY(argv).start();
	}
	
	/**
	 * GIPSY thread's body.
	 * Dispatches the options and control to the main RIPE module.
	 * @see RIPE
	 */
	public void run()
	{
		System.out.println("GIPSY thread started.");
		System.out.println("GIPSY: modes: debug = " + this.bDebug + ", compile-only = " + this.bCompileOnly);
		
		// Unless explicitly requested, we compile and run by default
		if(this.bCompileOnly == true)
		{
			this.oOptionProcessor.getInvalidOptions().add("--gipc");
		}
		else
		{
			this.oOptionProcessor.getInvalidOptions().add("--gipc");
			this.oOptionProcessor.getInvalidOptions().add("--gee");
		}
		
		Debug.debug(GIPSY.class, "Invalid options: " + this.oOptionProcessor.getInvalidOptions());
		Debug.debug(GIPSY.class, "Argument vector: " + Arrays.arrayToCSV(this.oOptionProcessor.getArgumentVector()));
		
		RIPE.main(this.oOptionProcessor.getArgumentVector());
		System.out.println("GIPSY thread finished.");
	}
	
	/**
	 * Displays usage information.
	 */
	private static final void usage()
	{
		System.out.println
		(
			"\nUsage:\n" +
			"    java GIPSY --help | -h\n" +
			"    java GIPSY [OPTIONS]\n\n" +
			"Options:\n" +
			"    --compile-only  tells GIPC only to compile, no GEE invocation\n" +
			"    --debug         tells all the modules to run in debug mode\n" +
			"\n"
		);
	}
}

// EOF
