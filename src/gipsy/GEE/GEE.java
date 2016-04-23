package gipsy.GEE;

import gipsy.GEE.IVW.Warehouse.IVWControl;
import gipsy.GEE.IVW.Warehouse.IVWInterface;
import gipsy.GEE.evaluation.IEvaluationEngine;
import gipsy.GEE.multitier.IMultiTierWrapper;
import gipsy.GEE.multitier.DGT.DGTWrapper;
import gipsy.GEE.multitier.DST.DSTWrapper;
import gipsy.GEE.multitier.DWT.DWTWrapper;
import gipsy.GEE.multitier.GMT.GMTWrapper;
import gipsy.interfaces.GIPSYProgram;
import gipsy.lang.GIPSYContext;
import gipsy.lang.GIPSYInteger;
import gipsy.lang.GIPSYType;
import gipsy.lang.context.Dimension;
import gipsy.storage.Dictionary;
import gipsy.tests.GEE.IDP.demands.TestDemandFactory;
import gipsy.tests.GEE.multitier.GIPSYNodeTestDriver;
import gipsy.util.GIPSYException;
import gipsy.util.GIPSYRuntimeException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import marf.Storage.StorageManager;
import marf.util.BaseThread;
import marf.util.Debug;
import marf.util.ExpandedThreadGroup;
import marf.util.FreeVector;
import marf.util.OptionProcessor;


/**
 * <p>General Eduction Engine Main Class.</p>
 *
 * <p>Main Module. Drives the execution of a GIPSY program.
 * Can be run as stand-alone application on the already-precompiled
 * GIPSY program or be triggered from GIPC.</p>
 *
 * @author Serguei Mokhov
 * @version $Id: GEE.java,v 1.39 2013/08/25 03:07:04 mokhov Exp $
 * @since 1.0.0
 */
public class GEE
//extends BaseThread
extends StorageManager
implements Runnable
{
	/**
	 * Serial version UID.
	 * We may preserve state of GEE by dumping it to disk and reloading later.
	 */
	private static final long serialVersionUID = 2178955671888147327L;

	/**
	 * Reading GIPSY program data from STDIN.
	 */
	public static final int OPT_STDIN = 0;

	/*
	 * -------------
	 * Service types
	 * -------------
	 */
	
	/**
	 * All services are ON.
	 */
	public static final int OPT_ALL = 1;
	
	/*
	 * Regular services
	 */

	public static final int OPT_THREADED = 2;
	public static final int OPT_RMI = 3;
	public static final int OPT_JINI = 4;
	public static final int OPT_DCOM = 5;
	public static final int OPT_CORBA = 6;

	/*
	 * Multi-tier options
	 */
	
	public static final int OPT_DGT = 7;
	public static final int OPT_DST = 8;
	public static final int OPT_DWT = 9;
	public static final int OPT_GMT = 10;
	
	/**
	 * Argument: configuration file.
	 */
	public static final int OPT_NODE = 15;
	
	public static final int MIN_OPT_SERVICES = OPT_ALL; 

	/**
	 * Need to update this one when new service types are
	 * added, regular or tier.
	 */
	public static final int MAX_OPT_SERVICES = OPT_NODE; 

	/**
	 * Enable extra debugging output if specified. 
	 */
	public static final int OPT_DEBUG = 11;
	
	/**
	 * Option to reference the compiled program's filename. 
	 */
	public static final int OPT_FILENAME = 12;
	
	/**
	 * Option to request command-line help.
	 */
	public static final int OPT_HELP = 13;

	/**
	 * A pass-through option for scripting purposes.
	 * Has no any real effect on anything in GEE.
	 */
	public static final int OPT_GEE = 14;


	/*
	 * ------------------
	 * Instance variables
	 * ------------------
	 */
	
	/*
	 * The main payload to work with.
	 */
	
	/**
	 * Local reference to a compiled GIPSY program (GEER).
	 */
	private GIPSYProgram oGIPSYProgram = null;

	/**
	 * Option processor is for command-line processing.
	 */
	private OptionProcessor oOptionProcessor = new OptionProcessor();

	/**
	 * Reference to the GIPSY code executor (an interpreter).
	 */
	//private Executor oExecutor = null;
	private IEvaluationEngine oExecutor = null;

	/**
	 * A group of Executor threads for a forest of ASTs.
	 */
	private ExpandedThreadGroup oExecutors = null;

	/**
	 * A collection of tier services.
	 */
	private ArrayList<IMultiTierWrapper> oTierWrappers = null;

	
	/*
	 * ------------------
	 * Methods
	 * ------------------
	 */

	/**
	 * Accepts a compiled GIPSY program from STDIN.
	 */
	public GEE()
	{
		//super("GIPSY GEE $Revision: 1.39 $, TID = " + BaseThread.getNextTID());
		this.bDumpOnNotFound = false;

		Debug.enableDebug();
		
		Debug.debug("Constructing " + this);

		this.oOptionProcessor.addValidOption(OPT_STDIN, "--stdin");
		this.oOptionProcessor.addValidOption(OPT_ALL, "--all");
		this.oOptionProcessor.addValidOption(OPT_THREADED, "--threaded");
		this.oOptionProcessor.addValidOption(OPT_RMI, "--rmi");
		this.oOptionProcessor.addValidOption(OPT_JINI, "--jini");
		this.oOptionProcessor.addValidOption(OPT_DCOM, "--dcom");
		this.oOptionProcessor.addValidOption(OPT_CORBA, "--corba");

		this.oOptionProcessor.addValidOption(OPT_DEBUG, "--debug");
		this.oOptionProcessor.addValidOption(OPT_HELP, "--help");
		this.oOptionProcessor.addValidOption(OPT_GEE, "--gee");

		this.oOptionProcessor.addValidOption(OPT_DGT, "--dgt");
		this.oOptionProcessor.addValidOption(OPT_DST, "--dst");
		this.oOptionProcessor.addValidOption(OPT_DWT, "--dwt");
		this.oOptionProcessor.addValidOption(OPT_GMT, "--gmt");
		this.oOptionProcessor.addValidOption(OPT_NODE, "--node");

		Debug.debug("Constructed " + this);
	}

	/**
	 * Accepts command-line arguments.
	 * @param argv
	 */
	public GEE(String[] argv)
	throws GEEException
	{
		this();

		int iValidOptions = this.oOptionProcessor.parse(argv);

		// Enable debugging if requested nearly right from the start
		// when all options are parsed.
		if(this.oOptionProcessor.isActiveOption(OPT_DEBUG))
		{
			Debug.enableDebug(true);
		}

		// Presence of "--help" will cause the rest of the options
		// (except "--debug") to be ignored. We just quit this instance
		// of GEE. System.exit(0) may not be appropriate as it will
		// switch off the entire JVM, which we may not want to do
		// in the complex environment.
		if(this.oOptionProcessor.isActiveOption(OPT_HELP))
		{
			usage();
			//System.exit(0);
			return;
		}

		// With too many invalid options (2+) cannot proceed.
		if(this.oOptionProcessor.getInvalidOptions().size() > 1)
		{
			usage();

			throw new GEEException
			(
				"Unrecognized options found: " +
				this.oOptionProcessor.getInvalidOptions()
			);
		}

		// Exactly one "invalid" option is interpreted as a filename
		// and we turn it into the valid one. Absence of the filename
		// may mean we are reading the serialized GIPSYProgram instance
		// from STDIN -- this is interpreted as needed based on the other
		// options supplied
		if(this.oOptionProcessor.getInvalidOptions().size() < 1)
		{
			System.err.println("Missing a filename argument, will assume STDIN as a GIPSYProgram input if/when needed.");
			this.strFilename = null;
			Debug.debug("GEE: loading GIPSYProgram from file STDIN");
		}
		else
		{
			iValidOptions++;

			// Filename argument has to be treated specially
			this.oOptionProcessor.addActiveOption(OPT_FILENAME, this.oOptionProcessor.getInvalidOptions().elementAt(0).toString());
			this.oOptionProcessor.getInvalidOptions().clear();

			this.oObjectToSerialize = this.oGIPSYProgram;
			this.strFilename = this.oOptionProcessor.getOption(OPT_FILENAME);

			Debug.debug("GEE: loading GIPSYProgram from file: " + this.strFilename);
		}

		Debug.debug("GEE: final active options: " + this.oOptionProcessor.getActiveOptions());

		if
		(
			this.oOptionProcessor.isActiveOption(OPT_DST)
			|| this.oOptionProcessor.isActiveOption(OPT_DWT)
			|| this.oOptionProcessor.isActiveOption(OPT_GMT)
			|| this.oOptionProcessor.isActiveOption(OPT_NODE)
			// XXX: tmp hack for now for testing a simulated GEER
			|| this.oOptionProcessor.isActiveOption(OPT_DGT)
		)
		{
			// Demand Store, Worker, and General Manager Tier
			// services not require a loading of the GIPSYProgram
			// GEER, so we ignore its deserialization here.
		}
		else
		{
			try
			{
				restore();
			}
			catch(Exception e)
			{
				e.printStackTrace(System.err);
				throw new GEEException(e);
			}
		}
	}

	
	/**
	 * Accepts a compiled GIPSY program.
	 * @param poGIPSYProgram a GEER instance
	 */
	public GEE(GIPSYProgram poGIPSYProgram)
	{
		this();
		Debug.debug("GEE Constructed with GIPSYProgram.");
		this.oGIPSYProgram = poGIPSYProgram;
	}

	/**
	 * Body of the GEE for demand-driven execution.
	 * TODO: finish implementation / option processing
	 */
	public void run()
	{
		try
		{
			System.out.println(this + " starting up...");
			System.out.println("Config: " + this.oOptionProcessor.getActiveOptions());
			
			// XXX: refactor
			GIPSYType oFinalValue = null;
	
			// TODO: Start all services/servers here.
			// TODO: This includes threaded, RMI, Jini and others.
			startServices();
	
			// Interpretation Stage and Warehousing
			// IFF this is not a tier start up
			if
			(
				this.oGIPSYProgram != null
				&&
				(
					this.oOptionProcessor.isActiveOption(OPT_DGT) == false
					&&
					this.oOptionProcessor.isActiveOption(OPT_DST) == false
					&&
					this.oOptionProcessor.isActiveOption(OPT_DWT) == false
					&&
					this.oOptionProcessor.isActiveOption(OPT_GMT) == false
				)
			)
			{
				Dictionary oDictionary = this.oGIPSYProgram.getDictionary();
				
				// Start up the storage subsystem (NetCDF, JavaSpaces, JBoss)
				IVWInterface oValueHouse = new IVWControl();
				String strValueCacheFilename = this.oGIPSYProgram.getName();
				oValueHouse.initIVW(oDictionary, strValueCacheFilename);
				oValueHouse.setupIVW(strValueCacheFilename);
	
				// Begin evaluation of the program (should be demand-driven)
				this.oExecutor = new Executor(oValueHouse);
				String strContext = this.oGIPSYProgram.getContext();
				GIPSYContext oContext = this.oGIPSYProgram.getContextValue();
				
				if(oContext == null)
				{
					oFinalValue = new GIPSYInteger(eval(strContext));
				}
				else
				{
					oFinalValue = eval(oContext);
				}
	
				oValueHouse.stopIVW();
				oValueHouse.viewSet();
				oValueHouse.getDataFile(strValueCacheFilename + ".nc");
			}
	
			stopServices();
			
			System.out.println(this + " terminated with a final value " + oFinalValue);
		}
		catch(Exception e)
		{
			System.err.println("GEE.run(): " + e);
			e.printStackTrace(System.err);
		}
	}

	@Deprecated
	public int eval(String pstrContext)
	{
		if(this.oExecutor == null)
		{
			this.oExecutor = new Executor();
		}

		System.out.println("GEER instance: [[[" + this.oGIPSYProgram +"]]]");
		return ((Executor)this.oExecutor).execute(this.oGIPSYProgram.getDictionary(), pstrContext);
	}
	
	public GIPSYType eval(GIPSYContext poContext)
	{
		if(this.oExecutor == null)
		{
			this.oExecutor = new Executor();
		}

		// XXX: kludge! the else return is the one that should be kept.
		if(true)
		{
			FreeVector<GIPSYType> oDimensions = poContext.getDemensions();
			FreeVector<GIPSYType> oTags = poContext.getTags();
			
			int iTagIndex = 0;
			
			String strContext = "";
			
			for(GIPSYType oDimension: oDimensions)
			{
				if(iTagIndex > 0)
				{
					strContext += ",";
				}
				
				strContext += ((Dimension)oDimension).getDimensionName().getValue() + "=" + oTags.get(iTagIndex).getEnclosedTypeOject();
				
				iTagIndex++;
			}
			
			System.out.println("Older style context: " + strContext);
			this.oGIPSYProgram.setContext(strContext);
			this.oGIPSYProgram.setContext(poContext);
			
			return new GIPSYInteger(eval(strContext));
		}
		else
		{
			return this.oExecutor.execute(this.oGIPSYProgram.getDictionary(), poContext);
		}
	}

	/* (non-Javadoc)
	 * @see marf.Storage.StorageManager#backSynchronizeObject()
	 */
	@Override
	public void backSynchronizeObject()
	{
		this.oGIPSYProgram = (GIPSYProgram)this.oObjectToSerialize;
	}

	
	/**
	 * Bootstraps the requested services -- can be direct invocation of the local or
	 * distributed services, or starting up tiers instead.
	 */
	public void startServices()
	throws GEEException
	{
		Debug.debug("GEE: startServices() is somewhat implemented...");

		// XXX: Ideally we should create an instance of this only if
		// any tier options were requested,
		this.oTierWrappers = new ArrayList<IMultiTierWrapper>();

		for(int iServiceOption = MIN_OPT_SERVICES; iServiceOption <= MAX_OPT_SERVICES; iServiceOption++)
		{
			if(this.oOptionProcessor.isActiveOption(iServiceOption))
			{
				switch(iServiceOption)
				{
					case OPT_ALL:
					{
						try
						{
							System.err.println("GEE: OPT_ALL only Jini is available");
							startJiniService();
							return;
						}
						catch(Exception e)
						{
							e.printStackTrace(System.err);
							throw new GEEException(e);
						}
					}

					case OPT_THREADED:
					{
						System.err.println("GEE: OPT_THREADED is not implemented yet.");
						break;
					}

					case OPT_RMI:
					{
						System.err.println("GEE: OPT_RMI is not implemented yet.");
						break;
					}

					case OPT_JINI:
					{
						try
						{
							System.err.println("GEE: OPT_JINI: attempting to start...");
							startJiniService();
							break;
						}
						catch(Exception e)
						{
							e.printStackTrace(System.err);
							throw new GEEException(e);
						}
					}

					case OPT_DCOM:
					{
						System.err.println("GEE: OPT_DCOM is not implemented yet.");
						break;
					}

					case OPT_CORBA:
					{
						System.err.println("GEE: OPT_CORBA is not implemented yet.");
						break;
					}

					/*
					 * Accumulate requested multi-tier services for start up.
					 */
					case OPT_DGT:
					{
						System.out.println("GEE: Instantiating DGTWrapper for start.");
						
						try
						{
							// XXX: mini-simulation testing
							TestDemandFactory.getInstance();
							this.oGIPSYProgram = TestDemandFactory.getIntensionalGEER();
							
							DGTWrapper oDGT = new DGTWrapper();

							if(this.oGIPSYProgram != null)
							{
								oDGT.putGEER(this.oGIPSYProgram.getSignature(), this.oGIPSYProgram);
							}
							
							this.oTierWrappers.add(oDGT);
						}
						catch(GIPSYException e)
						{
							e.printStackTrace(System.err);
						}
						
						break;
					}

					case OPT_DST:
					{
						System.out.println("GEE: Instantiating DSTWrapper for start.");
						this.oTierWrappers.add(new DSTWrapper());
						break;
					}
					
					case OPT_DWT:
					{
						System.out.println("GEE: Instantiating DWTWrapper for start.");
						this.oTierWrappers.add(new DWTWrapper());
						break;
					}
					
					case OPT_GMT:
					{
						System.out.println("GEE: Instantiating GMTWrapper for start.");
						this.oTierWrappers.add(new GMTWrapper());
						break;
					}
					
					case OPT_NODE:
					{
						String[] astrArgs = {this.oOptionProcessor.getOptionArgument(OPT_NODE)};
						GIPSYNodeTestDriver.main(astrArgs);
						break;
					}
					
					default:
					{
						Debug.debug("Unsupported service option or non-service option ignored: " + iServiceOption);
						break;
					}
				} // switch
			} // if active option
		} // for all service option types

		/*
		 * Start all the recognized tiers from the options.
		 */
		for(IMultiTierWrapper oTier: this.oTierWrappers)
		{
			//oTier.serConfiguration(xxx);
			oTier.startTier();
		}
	}

	/**
	 * Stop started services.	 
	 */
	public void stopServices()
	throws GEEException
	{
		/*
		 * Stop all the potentially started services.
		 */
		for(IMultiTierWrapper oTier: this.oTierWrappers)
		{
			oTier.stopTier();
		}
	}
	
	/**
	 * Essentially a crude Java re-implementation of the
	 * batch and shell scripts that do the same externally.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	private void startJiniService()
	throws IOException, InterruptedException
	{
		new BaseThread()
		{
			public void run()
			{
				try
				{
	//				String JINI_JVM_STARTUP_COMMAND = "java -Xss64k";
					Debug.debug("user.dir: " + System.getProperty("user.dir"));
	/*
					String strJiniCommand =
						"java "
						+ "-Xss64k "
						+ "-Djava.security.policy=bin/jini/start.policy "
						+ "-jar bin/jini/lib/start.jar "
						+ "bin/jini/startprimary.config ";
		*/				
	
					/*String strJiniCommand =
						System.getProperty("user.dir") + "\\bin\\jini\\start.bat";
						*/
					
					///*
					String strJiniCommand =
						"cmd /c start java "
						+ "-Xss64k "
						+ "-Djava.security.policy=start.policy "
						+ "-jar lib/start.jar "
						+ "startprimary.config ";
					//*/


					Debug.debug("JiniService: spawning external service");
					//Process oExternalService = Runtime.getRuntime().exec(strJiniCommand);
	
					Process oExternalService = Runtime.getRuntime().exec
					(
						strJiniCommand,
						null,
						new File(System.getProperty("user.dir") + "/bin/jini")
					);
					
					Debug.debug("user.dir: " + System.getProperty("user.dir") + " sleeping...");
					Thread.sleep(20000);
	
					Debug.debug("JiniService: waiting for external service to terminate");
					int iRetVal = oExternalService.waitFor();
	
					Debug.debug("JiniService: external service terminated");
				
				
					int iChar;
					String strStdError = "";
					String strStdOut = "(o)";
	
					Debug.debug("JiniService: reading in STDERR and STDOUT streams of the external service");
	
					while((iChar = oExternalService.getErrorStream().read()) != -1)
					{
						strStdError += (char)iChar;
					}
	
					while((iChar = oExternalService.getInputStream().read()) != -1)
					{
						strStdOut += (char)iChar;
					}
					
					System.out.println("STDOUT >>>: " + strStdOut + "<<<STDOUT");
					
					if(iRetVal != 0)
					{
						throw new GIPSYRuntimeException
						(
							"External service " + oExternalService + " failed. Details:\n" +
							strStdError + "\nCommand was: " + strJiniCommand
						);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace(System.err);
				}
			}
		}.start();
		
	}
	
	/**
	 * GEE may be run as a command-line application; hence, the main.
	 * @param argv command-line arguments vector
	 */
	public static final void main(String[] argv)
	{
		try
		{
			if(System.getSecurityManager() == null)
			{
				//System.setSecurityManager(new RMISecurityManager());
				System.setSecurityManager(new SecurityManager());
			}
			
			BaseThread oGEEThread = new BaseThread(new GEE(argv), "GEE, $Id: GEE.java,v 1.39 2013/08/25 03:07:04 mokhov Exp $");
			oGEEThread.start();
			oGEEThread.join();
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}

	/**
	 * Prints out command-line usage information.
	 * 
	 */
	public static final void usage()
	{
		System.out.println("usage: java GEE [OPTS]");
		System.out.println("		--stdin");
		System.out.println("				Reading GIPSY program data from STDIN.\n");
		System.out.println("		--all");
		System.out.println("				All services are ON.\n");
		System.out.println("		--threaded");
		System.out.println("				Specifies that the tiers should be run as " 
											 + "threads instead of processes. *** NOT YET IMPLEMENTED ***\n");
		System.out.println("		--rmi, --jini, --dcom, --corba");
		System.out.println("				Middleware selection. RMI, DCOM and CORBA are not yet implemented.\n");
		System.out.println("		--debug");
		System.out.println("				Activate debug mode.\n");
		System.out.println("		--help");
		System.out.println("				Display usage message.\n");
		System.out.println("		--gee");
		System.out.println("				NOOP.\n");
		System.out.println("		--dgt");
		System.out.println("				Instantiating DGTWrapper for start.\n");
		System.out.println("		--dst");
		System.out.println("				Instantiating DSTWrapper for start.\n");
		System.out.println("		--dwt");
		System.out.println("				Instantiating DWTWrapper for start.\n");
		System.out.println("		--gmt");
		System.out.println("				Instantiating GMTWrapper for start.\n");
		System.out.println("		--node=NODE.config");
		System.out.println("				Specifies the node configuration file\n");
		System.out.println("		--filename=FILE");
		System.out.println("				Option to reference the compiled program's filename.\n");	  
		System.exit(0);
	}
}

// EOF
