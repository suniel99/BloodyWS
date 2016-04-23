package gipsy.GIPC;

import gipsy.GEE.GEE;
import gipsy.GIPC.DFG.DFGGenerator.DFGCodeGenerator;
import gipsy.GIPC.Preprocessing.CodeSegment;
import gipsy.GIPC.Preprocessing.Preprocessor;
import gipsy.GIPC.imperative.EImperativeLanguages;
import gipsy.GIPC.imperative.IImperativeCompiler;
import gipsy.GIPC.intensional.EIntensionalLanguages;
import gipsy.GIPC.intensional.IIntensionalCompiler;
import gipsy.GIPC.intensional.IntensionalCompiler;
import gipsy.GIPC.intensional.IntensionalCompilerException;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.GIPC.intensional.GIPL.GIPLCompiler;
import gipsy.GIPC.intensional.GenericTranslator.Translator;
import gipsy.GIPC.intensional.SIPL.ForensicLucid.ForensicLucidCompiler;
import gipsy.GIPC.intensional.SIPL.ForensicLucid.ForensicLucidSemanticAnalyzer;
import gipsy.GIPC.intensional.SIPL.IndexicalLucid.IndexicalLucidCompiler;
import gipsy.GIPC.intensional.SIPL.JLucid.JLucidCompiler;
import gipsy.GIPC.intensional.SIPL.Lucx.LucxCompiler;
import gipsy.GIPC.intensional.SIPL.ObjectiveLucid.ObjectiveLucidCompiler;
import gipsy.interfaces.AbstractSyntaxTree;
import gipsy.interfaces.GIPSYProgram;
import gipsy.storage.Dictionary;
import gipsy.tests.GIPC.intensional.SIPL.Lucx.SemanticTest.LucxSemanticAnalyzer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;

import marf.util.BaseThread;
import marf.util.Debug;
import marf.util.ExpandedThreadGroup;
import marf.util.OptionProcessor;


/**
 * <p>General Intensional Program Compiler.</p>
 *
 * <p>Keeps track and maintains the state of the entire compilation process,
 * such as collecting results from the Preprocessor as well as different
 * compilers and feeding those to the backend.</p>
 *
 * <p>The GIPC also serves as a facade to all the other modules.</p>
 *
 * <p>Feeds the symbol table (collected from the preprocessor), compiled
 * object code from imperative parts (or sometimes possibly just the
 * wrapped source, collected from imperative compilers) and IPL
 * AST (collected from IPL compiler(s)) to the backend.</p>
 *
 * @author Serguei Mokhov
 * @version $Id: GIPC.java,v 1.70 2013/08/25 02:59:29 mokhov Exp $
 * @since 1.0.0
 */
public class GIPC
extends IntensionalCompiler
{
	/**
	 * For serialization versionning. 
	 */
	private static final long serialVersionUID = 5412492426375587515L;
	
	/*
	 * Typed INF+ and INF- representations.
	 * XXX: move elsewhere.
	 */
	
	public static final int INFPLUS_INT = Integer.MAX_VALUE;
	public static final int INFMINUS_INT = Integer.MIN_VALUE;

	public static final long INFPLUS_LONG = Long.MAX_VALUE;
	public static final long INFMINUS_LONG = Long.MIN_VALUE;

	public static final double INFPLUS_DOUBLE = Double.MAX_VALUE;
	public static final double INFMINUS_DOUBLE = Double.MIN_VALUE;

	/*
	 * XXX: these are too limiting and need to be eliminated
	 * Came all the way back from Chun Lei Ren, Aihua Wu, and Paula Lu,
	 * and Xin Tong and Serguei and got in during the refactoring
	 * and retrofitting process.
	 * Need to get rid of this dependencies as the concrete compilers
	 * should not rely on these.
	 */
	public static final int GIPL_PARSER = 0;
	public static final int INDEXICAL_LUCID_PARSER = 1;
	public static final int LUCX_PARSER = 2;
	public static final int FORENSIC_LUCID_PARSER = 3;

	
	/**
	 * Read the source code to compile from STDIN. 
	 */
	public static final int OPT_STDIN = 1;

	/*
	 * ------------------------
	 * Dialect-specific options
	 * ------------------------
	 */
	
	public static final int OPT_GIPL = 2;
	public static final int OPT_GIPL_SHORT = 3;
	
	public static final int OPT_INDEXICAL = 4;
	public static final int OPT_INDEXICAL_SHORT = 5;

	public static final int OPT_JLUCID = 6;
	public static final int OPT_OBJECTIVE_LUCID = 7;

	/**
	 * @since Xin Tong
	 */
	public static final int OPT_LUCX = 8; 

	/**
	 * Forensic Lucid compiler invocation.
	 * @since November 5, 2008, Serguei Mokhov
	 */
	public static final int OPT_FORENSIC_LUCID = 9; 

	/**
	 * @since November 5, 2008, Serguei Mokhov
	 */
	public static final int OPT_MARFL = 10; 

	/**
	 * @since November 5, 2008, Serguei Mokhov
	 */
	public static final int OPT_JOOIP = 11; 

	
	public static final int OPT_TRANSLATE = 12;
	public static final int OPT_TRANSLATE_SHORT = 13;
	public static final int OPT_DISABLE_TRANSLATE = 14;

	public static final int OPT_PREPROCESS_ONLY = 15;
	public static final int OPT_PARSE_ONLY = 16;
	public static final int OPT_DFG = 17;

	
	/**
	 * If set, tell GIPC to invoke GEE right after compilation.
	 */
	public static final int OPT_GEE = 18;

	
	/*
	 * -----------------
	 * General behaviour
	 * -----------------
	 */

	public static final int OPT_WARNINGS_AS_ERRORS = 19;
	public static final int OPT_DEBUG = 20;
	public static final int OPT_HELP_SHORT = 21;
	public static final int OPT_HELP_LONG = 22;

	
	public static final int OPT_FILENAME = 23;
	public static final int OPT_NO_FILENAME = 24;


	/**
	 * Ignore, this is a pass-through option. 
	 */
	public static final int OPT_GIPC = 25;


	/*
	 * -----------------
	 * Member variables
	 * -----------------
	 */
	
	/**
	 * Aggregation of the Preprocessor object.
	 */
	private Preprocessor oPreprocessor = null;

	/**
	 * Aggregation of the global dictionary.
	 */
	private Dictionary oDictionary = null;

	/**
	 * Option Processor.
	 */
	private OptionProcessor oOptionProcessor = new OptionProcessor();

	/**
	 * GEERGenerator's instance.
	 */
	private GEERGenerator oGEERGenerator = null;

	/**
	 * SemanticAnalyzer's instance.
	 */
	private ISemanticAnalyzer oSemanticAnalyzer = null;

	/**
	 * A collection of intensional compilers.
	 */
	private IIntensionalCompiler[] aoIntensionalCompilers = null;

    /**
	 * A collection of imperative compilers.
	 */
	private IImperativeCompiler[] aoImperativeCompilers = null;

	/**
	 * Local collection of ASTs harvested from
	 * multiple compile resources.
	 */
	private Vector<AbstractSyntaxTree> oASTs = new Vector<AbstractSyntaxTree>();

    /**
	 * References to the GIPSY IC demand code generator.
	 */
	private IdentifierContextCodeGenerator oICCodeGenerator = null;

    /**
	 * References to the SIPL to GIPL translator.
	 */
	private Translator oTranslator = null;

	/**
	 * Reference to compiled GIPSY program, the GEER.
	 */
	private GIPSYProgram oGIPSYProgram = null;

    /**
	 * References to the engine, that may be optionally invoked
	 * after compilation.
	 */
	private GEE oGEE = null;

	/**
	 * A separate AST reserved for the Preprocessor
	 * to avoid conflicts with the main AST of a GIPSY
	 * program.
	 */
	private AbstractSyntaxTree oPreprocessortAST = null;

	/**
	 * Current type of primary parser to use
	 * for intensional dialects.
	 * TODO: A kludge that came from the Lucid class.
	 */
	public static int siPrimaryParserType;

	/**
	 * Default constructor. Sets default input stream to STDIN.
	 * @throws GIPCException if System.in is null
	 */
	public GIPC()
	throws GIPCException
	{
		super();
		setupDefaultConfig();
		this.oObjectToSerialize = this.oGIPSYProgram;
	}

	/**
	 * A convenience constructor that allows getting the source
	 * code via some InputStream and plus have the options.
	 * Extremely useful in the <code>Regression</code> testing
	 * application.
	 * 
	 * @param poSourceCodeStream source code input stream
	 * @param argv command-line arguments
	 * @throws GIPCException
	 * @see gipsy#tests#Regression
	 */
	public GIPC(InputStream poSourceCodeStream, String[] argv)
	throws GIPCException
	{
		this.oOptionProcessor.addActiveOption(OPT_NO_FILENAME, "--nofilename");
		setupConfig(argv);
		this.oSourceCodeStream = poSourceCodeStream;
	}

	/**
	 * Option-processing constructor.
	 * @param argv command-line arguments
	 * @throws GIPCException
	 */
	public GIPC(String[] argv)
	throws GIPCException
	{
		setupConfig(argv);
	}

	/**
	 * @param argv
	 * @throws GIPCException
	 */
	protected void setupConfig(String[] argv)
	throws GIPCException
	{
		try
		{
			this.oOptionProcessor.addValidOption(OPT_STDIN, "--stdin");

			this.oOptionProcessor.addValidOption(OPT_GIPL, "--gipl");
			this.oOptionProcessor.addValidOption(OPT_GIPL_SHORT, "-G");
			
			this.oOptionProcessor.addValidOption(OPT_INDEXICAL, "--indexical");
			this.oOptionProcessor.addValidOption(OPT_INDEXICAL_SHORT, "-S");

			this.oOptionProcessor.addValidOption(OPT_JLUCID, "--jlucid");
			this.oOptionProcessor.addValidOption(OPT_OBJECTIVE_LUCID, "--objective");
			this.oOptionProcessor.addValidOption(OPT_LUCX, "--lucx");
			this.oOptionProcessor.addValidOption(OPT_FORENSIC_LUCID, "--flucid");
			this.oOptionProcessor.addValidOption(OPT_MARFL, "--marfl");
			this.oOptionProcessor.addValidOption(OPT_JOOIP, "--jooip");
			
			this.oOptionProcessor.addValidOption(OPT_TRANSLATE, "--translate");
			this.oOptionProcessor.addValidOption(OPT_TRANSLATE_SHORT, "-T");
			this.oOptionProcessor.addValidOption(OPT_DISABLE_TRANSLATE, "--disable-translate");
			this.oOptionProcessor.addValidOption(OPT_WARNINGS_AS_ERRORS, "--warnings-as-errors");
			this.oOptionProcessor.addValidOption(OPT_GEE, "--gee");
			this.oOptionProcessor.addValidOption(OPT_DEBUG, "--debug");
			this.oOptionProcessor.addValidOption(OPT_DFG, "--dfg");
			this.oOptionProcessor.addValidOption(OPT_HELP_SHORT, "-h");
			this.oOptionProcessor.addValidOption(OPT_HELP_LONG, "--help");
			this.oOptionProcessor.addValidOption(OPT_GIPC, "--gipc");
			this.oOptionProcessor.addValidOption(OPT_PREPROCESS_ONLY, "--preprocess-only");
			this.oOptionProcessor.addValidOption(OPT_PARSE_ONLY, "--parse-only");

			int iValidOptions = this.oOptionProcessor.parse(argv);

			if(this.oOptionProcessor.isActiveOption(OPT_DEBUG))
			{
				Debug.enableDebug(true);
			}

			Debug.debug("GIPC: active options: " + this.oOptionProcessor.getActiveOptions());

			if
			(
				this.oOptionProcessor.isActiveOption(OPT_HELP_SHORT) ||
				this.oOptionProcessor.isActiveOption(OPT_HELP_LONG)
			)
			{
				usage();
				return;
				//System.exit(0);
			}

			// One "invalid" option is the filename, so we ignore it.
			// The rest should be reported.
			int iAllowedErrors = this.oOptionProcessor.isActiveOption(OPT_NO_FILENAME) ? 0 : 1;
			
			if(this.oOptionProcessor.getInvalidOptions().size() > iAllowedErrors)
			{
				throw new GIPCException
				(
					"Unrecognized options: " +
					this.oOptionProcessor.getInvalidOptions()
				);
			}

			if(this.oOptionProcessor.isActiveOption(OPT_NO_FILENAME) == false)
			{
				if(this.oOptionProcessor.getInvalidOptions().size() < 1)
				{
					throw new GIPCException
					(
						"Missing a filename argument."
					);
				}
				else
				{
					// Filename argument has to be treated specially
					this.oOptionProcessor.addActiveOption(OPT_FILENAME, this.oOptionProcessor.getInvalidOptions().elementAt(0).toString());
					this.oOptionProcessor.getInvalidOptions().clear();
					iValidOptions++;

					this.strFilename = this.oOptionProcessor.getOption(OPT_FILENAME);
					setSourceCodeStream(new FileInputStream(this.strFilename));
				}

				if(iValidOptions == 0)
				{
					usage();
					return;
					//System.exit(1);
				}
			}
			else if(iValidOptions == 0)
			{
				setupDefaultConfig();
			}

			Debug.debug("final active options: " + this.oOptionProcessor.getActiveOptions());
		}
		catch(GIPCException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new GIPCException(e);
		}
	}
	
	/**
	 * Reads the source code from the file specified.
	 * @param pstrFilename GIPSY source code filename
	 * @throws GIPCException if there was an en error opening the file.
	 */
	public GIPC(String pstrFilename)
	throws GIPCException
	{
		super(pstrFilename);
		setupDefaultConfig();
		this.oOptionProcessor.addActiveOption(OPT_FILENAME, pstrFilename);
		this.oObjectToSerialize = this.oGIPSYProgram;
		this.strFilename = pstrFilename;
	}

	/**
	 * @param poInputStream
	 * @throws GIPCException
	 */
	public GIPC(InputStream poInputStream)
	throws GIPCException
	{
		super(poInputStream);
		setupDefaultConfig();
	}

	/**
	 * Sets up default configuration og GIPC.
	 * By default we assume STDIN and SIPL-to-GIPL translation.
	 */
	protected void setupDefaultConfig()
	{
		this.oOptionProcessor.addActiveOption(OPT_STDIN, "--stdin");
		this.oOptionProcessor.addActiveOption(OPT_TRANSLATE, "--translate");
		this.oOptionProcessor.addActiveOption(OPT_TRANSLATE_SHORT, "-T");
	}

	/**
	 * Main processing routine.
	 *
	 * @throws GIPCException should there be an IOException or another GIPCException while processing
	 */
	public GIPSYProgram process()
	throws GIPCException
	{
		// For debugging
		String strPhase = "process() begun";

		try
		{
			Debug.debug("Main processing begun.");

			Vector<CodeSegment> oChunks = new Vector<CodeSegment>();

			// GIPL-only by-pass
			if(this.oOptionProcessor.isActiveOption(OPT_GIPL))
			{
				Debug.debug("GIPL-only processing");
				strPhase = "GIPL";
				siPrimaryParserType = GIPL_PARSER;
				GIPLCompiler oGIPLCompiler = new GIPLCompiler(this.oSourceCodeStream);
				this.oAST = oGIPLCompiler.compile();

				/*
				 * If asked, generate the DFG
				 */
				if(this.oOptionProcessor.isActiveOption(OPT_DFG))
				{
					DFGCodeGenerator oDFGCodeGenerator = new DFGCodeGenerator();
					oDFGCodeGenerator.generateDFG((SimpleNode)this.oAST.getRoot(), GIPL_PARSER, null);
				}
			}

			// Indexical Lucid-only by-pass
			else if(this.oOptionProcessor.isActiveOption(OPT_INDEXICAL))
			{
				strPhase = "Indexical Lucid";

				siPrimaryParserType = INDEXICAL_LUCID_PARSER;
				IndexicalLucidCompiler oIndexicalLucidCompiler = new IndexicalLucidCompiler(this.oSourceCodeStream);
				this.oAST = oIndexicalLucidCompiler.compile();

				/*
				 * If asked, generate the DFG
				 */
				if(this.oOptionProcessor.isActiveOption(OPT_DFG))
				{
					DFGCodeGenerator oDFGCodeGenerator = new DFGCodeGenerator();
					oDFGCodeGenerator.generateDFG((SimpleNode)this.oAST.getRoot(), INDEXICAL_LUCID_PARSER, null);
				}
			}

			// JLucid-only by-pass
			else if(this.oOptionProcessor.isActiveOption(OPT_JLUCID))
			{
				strPhase = "JLucid";

				JLucidCompiler oJLucidCompiler = new JLucidCompiler(this.oSourceCodeStream);
				this.oAST = oJLucidCompiler.compile();
			}

			// Objective Lucid-only by-pass
			else if(this.oOptionProcessor.isActiveOption(OPT_OBJECTIVE_LUCID))
			{
				strPhase = "Objective Lucid";

				ObjectiveLucidCompiler oObjectiveLucidCompiler = new ObjectiveLucidCompiler(this.oSourceCodeStream);
				this.oAST = oObjectiveLucidCompiler.compile();
			}

			// Lucx-only by-pass
			else if(this.oOptionProcessor.isActiveOption(OPT_LUCX))
			{
				strPhase = "Lucx";
				siPrimaryParserType = LUCX_PARSER;
				LucxCompiler oCompiler = new LucxCompiler(this.oSourceCodeStream);
				this.oAST = oCompiler.compile();
			}

			// Forensic Lucid-only by-pass
			else if(this.oOptionProcessor.isActiveOption(OPT_FORENSIC_LUCID))
			{
				strPhase = "Forensic Lucid";
				siPrimaryParserType = FORENSIC_LUCID_PARSER;
				ForensicLucidCompiler oCompiler = new ForensicLucidCompiler(this.oSourceCodeStream);
				this.oAST = oCompiler.compile();
			}

			// General processing of GIPSY programs
			else
			{
				strPhase = "General GIPSY Program Preprocessing";

				// Construct the preprocessor AST from the GIPSY source.
				this.oPreprocessor = new Preprocessor(this.oSourceCodeStream);
				this.oPreprocessor.preprocess();
				this.oDictionary = this.oPreprocessor.getDictionary(); 

				strPhase = "General GIPSY Program AST generation";
				this.oPreprocessortAST = new AbstractSyntaxTree(this.oPreprocessor.getPreprocessorASTRoot());
				this.oPreprocessortAST.dump("[GIPC ok]->");

				// Process chunks and invoke appropriate compilers.
				strPhase = "General GIPSY Program Chunk Processing";
				oChunks = this.oPreprocessor.getCodeSegments();

				if(oChunks.size() == 0)
				{
					throw new GIPCException
					(
						"GIPC: the program contained no code.\n" +
						"HINT: verify if you have commented it all out."
					);
				}

				/*
				 * Each compiler is a thread and they are all part
				 * of the same group.
				 */
				ExpandedThreadGroup oCompilerThreads = new ExpandedThreadGroup("GIPSY GIPC Compilers");

				strPhase = "Creating compiler threads.";

				Vector<Exception> oCompilerFailures = new Vector<Exception>();

				/*
				 * Compile every chunk with appropriate compiler.
				 * Each compiler is looked up and chosen at GIPC run-time
				 * dynamically based on the types of code segments got
				 * from the preprocessor.
				 */
				for(int i = 0; i < oChunks.size(); i++)
				{
					CodeSegment oCodeSegment = (CodeSegment)oChunks.elementAt(i);

					Debug.debug("GIPC: CodeSegment: " + oCodeSegment);

					String strCompiler = lookupCompiler(oCodeSegment.getLanguageName());

					if(strCompiler == null)
					{
						/*
						 * This is a bit of a delayed failure reporting
						 * to allow the existing compilers to voice their
						 * complaints as well.
						 */
						oCompilerFailures.add
						(
							new GIPCException
							(
								"GIPC: compiler not found for language: [" +
								oCodeSegment.getLanguageName() + "]"
							)
						);
						
						continue;
					}

					// Instantiate a compiler
					ICompiler oChunkCompiler =
						(ICompiler)Class.forName(strCompiler).newInstance();

					// Pass the chunk to each compiler.
					oChunkCompiler.setSourceCodeStream(oCodeSegment.getSourceCodeStream());

					// Add the newly created compiler thread to the compiler thread group
					oCompilerThreads.addThread(new BaseThread(oCompilerThreads, oChunkCompiler));
				}

				// Start all compilers and wait for them to finish.
				strPhase = "Starting compiler threads.";
				oCompilerThreads.start();
				oCompilerThreads.join();

				// Collect compiled ASTs .... and failures
				strPhase = "Collecting ASTs.";
				Thread[] aoCompilerThreads = oCompilerThreads.enumerate(false);

				for(int i = 0; i < aoCompilerThreads.length; i++)
				{
					strPhase = "Getting a compiler.";
					ICompiler oCompiler = ((ICompiler)((BaseThread)aoCompilerThreads[i]).getTarget());

					strPhase = "Checking for compiler failures.";

					if(oCompiler.getLastException() == null)
					{
						// Kludgery here is that assumption that only
						// one main IPL tree is there
						if(oCompiler instanceof IIntensionalCompiler)
						{
							this.oAST = oCompiler.getAbstractSyntaxTree();
						}
						else
						{
							this.oASTs.add(oCompiler.getAbstractSyntaxTree());
						}
					}
					else
					{
						// Collect all failures
						oCompilerFailures.add(oCompiler.getLastException());
					}
				}

				if(oCompilerFailures.size() > 0)
				{
					throw new GIPCException
					(
						"Failed number of compilers: " +
						oCompilerFailures.size() +
						". Details:\n" + oCompilerFailures
					);
				}

				// KLUDGE KLUDGE KLUDGE KLUDGE KLUDGE KLUDGE
				if(this.oASTs.size() == 0 && this.oAST == null)
				{
					throw new GIPCException("GIPC: no main AST found");
				}
				else
				{
	//				this.oAST = (AbstractSyntaxTree)this.oASTs.elementAt(0);
				}
				// KLUDGE KLUDGE KLUDGE KLUDGE KLUDGE KLUDGE
			}

			/*
			 * Linking stage.
			 *
			 * Before we can start semantic analysis, we need to
			 * group all ASTs in one in the link stage by replacing
			 * imperative stubs with the actual contents.
			 */
			if(this.oOptionProcessor.isActiveOption(OPT_PARSE_ONLY) == false)
			{
				strPhase = "Linking Pass 1";
				this.oGEERGenerator = new GEERGenerator(this.oAST);
				//this.oGEERGenerator.setDictionary(this.oDictionary);
				//this.oGEERGenerator.setASTs(this.oASTs);
				strPhase = "Linking 2, AST=" + this.oAST;
				this.oGIPSYProgram = this.oGEERGenerator.link();
	
				// Perform Semantic Analysis
				// TODO: threading for multiple intensional expressions
				strPhase = "Semantic Analysis";
				
				// Pick appropriate semantic analyzer to use
				if(this.oOptionProcessor.isActiveOption(OPT_LUCX))
				{
					this.oSemanticAnalyzer = new LucxSemanticAnalyzer();
				}
				
				else if(this.oOptionProcessor.isActiveOption(OPT_FORENSIC_LUCID))
				{
					this.oSemanticAnalyzer = new ForensicLucidSemanticAnalyzer();
				}
				
				// Default
				else
				{
					this.oSemanticAnalyzer = new SemanticAnalyzer();
				}
				
				strPhase
					= "Semantic Analyzer " + this.oSemanticAnalyzer.getClass().getName()
					+ ": instantiated.\nSetting up dictionary.";
				
				this.oSemanticAnalyzer.setupDictionary
				(
					(gipsy.GIPC.intensional.SimpleNode)this.oAST.getRoot()
				);
				
				strPhase = "Semantic Analyzer: done";
	
				System.err.println(this.oSemanticAnalyzer.getErrorCount() + " semantic errors.");
				System.err.println(this.oSemanticAnalyzer.getWarningCount() + " semantic warnings.");
	
				// XXX
				strPhase = "Linking Pass 2";
				this.oGIPSYProgram.setName(this.strFilename);
				this.oGIPSYProgram.setDictionary(this.oSemanticAnalyzer.getDictionary());
	
				// IC code generation
				//this.oICCodeGenerator = new IdentifierContextCodeGenerator();
				//this.oICCodeGenerator.generate(this.oSemanticAnalyzer.getDictionary());
	
				// Serialization and running should only be done if there are no errors.
				if(this.oSemanticAnalyzer.getErrorCount() == 0)
				{
					strPhase = "Serialization";
	
					this.oObjectToSerialize = this.oGIPSYProgram;
					this.strFilename = this.strFilename.replaceAll(".ipl", ".gipsy");
					serializeGIPSYProgram();
	
					System.out.println("GIPC: compilation process has completed successfully.");
	
					if(this.oOptionProcessor.isActiveOption(OPT_GEE))
					{
						strPhase = "Instantiating GEE...";
						System.out.println("GIPC: staring the GEE...");
	
						/*
						 * The --debug option will be implied as GIPC
						 * has already set it, so no need to pass that
						 */
						this.oGEE = new GEE(this.oGIPSYProgram);
	
						strPhase = "Starting GEE...";
						//this.oGEE.start();
						//new BaseThread(this.oGEE).start();
						this.oGEE.run();
					}
				}
				
				return this.oGIPSYProgram;
			} // parse only
			
			// No program was compiled
			return null;
		}

		// If we were already of type GIPCException, just re-throw.
		catch(GIPCException e)
		{
			throw e;
		}

		// Else wrap, and re-throw.
		catch(Exception e)
		{
			Debug.debug("GIPC foobared: " + e);
			e.printStackTrace(System.err);
			throw new GIPCException("Phase: " + strPhase + ", " + e.getMessage() + e, e);
		}
	}

	/**
	 * Implementation of ICompiler.
	 * @throws GIPCException if there was an initialization error
	 */
	public void init()
	throws GIPCException
	{
		this.oDictionary = new Dictionary();
	}

	/**
	 * Implementation of ICompiler.
	 * @throws GIPCException if there was a parse error
	 */
	public AbstractSyntaxTree parse()
	throws GIPCException
	{
		return this.oAST;
	}

	/**
	 * For GIPC compiler, we simply call translate
	 * of all intensional compilers, and return our
	 * own AST.
	 *
	 * TODO: AST merge.
	 *
	 * @return original AST
	 */
	public AbstractSyntaxTree translate()
	throws IntensionalCompilerException
	{
		for(int i = 0; i < this.aoIntensionalCompilers.length; i++)
		{
			AbstractSyntaxTree oCurrentIntensionalAST =
				this.aoIntensionalCompilers[i].translate();
		}

		return this.oAST;
	}

	/**
	 * Dumps GIPSYProgram's instance to disk.
	 * @throws GIPCException in the case of an I/O error
	 */
	public void serializeGIPSYProgram()
	throws GIPCException
	{
		try
		{
			dump();
		}
		catch(Exception e)
		{
			throw new GIPCException(e.getMessage(), e);
		}
	}

	/**
	 * Looks up compiler based on language name.
	 * @param pstrLanguageName language name to search compiler for.
	 * @return fully qualified compiler class name string if found; null otherwise
	 */
	public String lookupCompiler(String pstrLanguageName)
	{
		int i;

		/*
		 * Attempt intensional languages first, then
		 * imperative.
		 */
		for(i = 0; i < EIntensionalLanguages.INTENSIONAL_LANGUAGES.length; i++)
		{
			if(pstrLanguageName.equals(EIntensionalLanguages.INTENSIONAL_LANGUAGES[i]))
			{
				return EIntensionalLanguages.INTENSIONAL_COMPILERS[i];
			}
		}

		for(i = 0; i < EImperativeLanguages.IMPERATIVE_LANGUAGES.length; i++)
		{
			if(pstrLanguageName.equals(EImperativeLanguages.IMPERATIVE_LANGUAGES[i]))
			{
				return EImperativeLanguages.IMPERATIVE_COMPILERS[i];
			}
		}

		// No matching compiler found.
		return null;
	}

	public Dictionary getDictionary()
	{
		return this.oDictionary;
	}
	
	/**
	 * Implementation of <code>ICompiler</code>.
	 * Overridden from IntensionalCompiler to include <code>process()</code>
	 * in the loop and have translation be called from it.
	 * @param poExtraArgs unused
	 * @see #process()
	 * @see ICompiler
	 */
	public AbstractSyntaxTree compile(Object poExtraArgs)
	throws GIPCException
	{
		init();
		process();

		return this.oAST;
	}

	/**
	 * A stand-alone main compiler thread.
	 * @param argv command-line arguments.
	 */
	public static final void main(String[] argv)
	{
		try
		{
			BaseThread oGIPCThread = new BaseThread(new GIPC(argv), "GIPC $Revision: 1.70 $");
			oGIPCThread.start();
			oGIPCThread.join();
			System.out.println("Compilation process completed.");
		}
		catch(NullPointerException e)
		{
			usage();
			System.exit(1);
		}
		catch(Exception e)
		{
			System.err.println(e);
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}

	public GIPSYProgram getGIPSYProgram()
	{
		return this.oGIPSYProgram;
	}

	public GIPSYProgram getGEER()
	{
		return getGIPSYProgram();
	}
	
	/**
	 * Displays usage information of a command-line compiler.
	 */
	private static final void usage()
	{
		System.out.println
		(
			"Usage: java GIPC <filename> <dimension> [OPTIONS]\n\n" +

			"Options:\n\n" +

			"  --help      | -h      show this help and exit\n" +
			"  --gipl      | -G      force GIPL-only compilation\n" +
			"  --indexical | -S      force Indexical Lucid-only compilation\n" +
			"  --jlucid              force JLucid-only compilation\n" +
			"  --objective           force Objective Lucid-only compilation\n" +
			"  --lucx                force Lucx-only compilation\n" +
			"  --flucid              force Forensic Lucid-only compilation\n" +
			"  --gee                 run GEE after compilation\n" +
			"  --dfg                 enable DFG generation\n" +
			"  --translate | -T      enable translation (default, always enabled)\n" +
			"  --disable-translate   disable translation\n" +
			"  --stdin               read a source code program from STDIN\n" +
			"  --debug               enable debugging\n"
		);
	}
}

// EOF
