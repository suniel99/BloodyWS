package gipsy.tests;

import gipsy.GEE.GEE;
import gipsy.GIPC.GIPC;
import gipsy.GIPC.GIPCException;
import gipsy.GIPC.Preprocessing.CodeSegment;
import gipsy.storage.Dictionary;
import gipsy.util.GIPSYException;

import java.util.Enumeration;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestFailure;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import marf.util.BaseThread;
import marf.util.Debug;
import marf.util.Logger;
import marf.util.OptionProcessor;


/**
 * <p>The GIPSY Regression Test Suite.</p>
 *
 * <p>Runs tests through various modules with the test inputs
 * and compares the outputs with expected ones.</p>
 *
 * $Id: Regression.java,v 1.19 2013/08/05 13:27:25 mokhov Exp $
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @version $Revision: 1.19 $
 * @since 1.0.0
 */
public class Regression
extends BaseThread
implements Test
{
	public static final int OPT_DEBUG = 0;

	public static final int OPT_GIPL_PROGRAMS = 1;
	public static final int OPT_INDEXICAL_PROGRAMS = 2;
	public static final int OPT_GIPSY_PROGRAMS = 3;
	public static final int OPT_ALL_PROGRAMS = 4;

	public static final int OPT_GEE = 5;

	public static final int OPT_HELP_SHORT = 6;
	public static final int OPT_HELP_LONG = 7;

	public static final int OPT_REGRESSION = 8;
	public static final int OPT_JUNIT = 9;

	public static final int OPT_PARALLEL = 10;
	public static final int OPT_SEQUENTIAL = 11;
	public static final int OPT_DIRECTORY = 12;

	protected static String sstrGIPLTestsDir = "src/gipsy/tests/lucid";
	protected static String sstrIndexicalTestsDir = "src/gipsy/tests/lucid";
	protected static String sstrGIPSYTestsDir = "src/gipsy/tests/gipsy";


	protected static String[] sastrGIPLFiles =
	{
		"t1.ipl",
		"t2.ipl"
	};

	protected static String[] sastrIndexicalFiles =
	{
		"fun_1.ipl"
	};

	protected static String[] sastrGIPSYFiles =
	{
		"gipl.ipl",
		"indexical.ipl",
		"language-mix.ipl"
	};

	protected static OptionProcessor soOptionProcessor = new OptionProcessor();

	//protected static TestSuite soTestSuite = new TestSuite(Regression.class);
	protected TestSuite oTestSuite = new TestSuite();
	
	public Regression()
	{
	}
	
	public Regression(final String[] pastArgv)
	{
    	Debug.enableDebug(true);

    	soOptionProcessor.addValidOption(OPT_DEBUG, "--debug");
    	soOptionProcessor.addValidOption(OPT_GIPL_PROGRAMS, "--gipl");
    	soOptionProcessor.addValidOption(OPT_INDEXICAL_PROGRAMS, "--indexical");
    	soOptionProcessor.addValidOption(OPT_GIPSY_PROGRAMS, "--gipsy");
    	soOptionProcessor.addValidOption(OPT_ALL_PROGRAMS, "--all");
    	soOptionProcessor.addValidOption(OPT_GEE, "--gee");
    	soOptionProcessor.addValidOption(OPT_HELP_SHORT, "-h");
    	soOptionProcessor.addValidOption(OPT_HELP_LONG, "--help");

    	soOptionProcessor.addValidOption(OPT_REGRESSION, "--regression");
    	soOptionProcessor.addValidOption(OPT_JUNIT, "--junit");
    	
    	soOptionProcessor.addValidOption(OPT_PARALLEL, "--parallel");
    	soOptionProcessor.addValidOption(OPT_SEQUENTIAL, "--sequential");

    	// Requires option argument
    	soOptionProcessor.addValidOption(OPT_SEQUENTIAL, "--directory", true);

    	int iValidOptions = soOptionProcessor.parse(pastArgv);

    	if(soOptionProcessor.getInvalidOptions().size() > 0)
    	{
			System.err.println("Unrecognized options: " + soOptionProcessor.getInvalidOptions());
			usage();
			//System.exit(1);
			return;
    	}

		if(soOptionProcessor.isActiveOption(OPT_HELP_SHORT) || soOptionProcessor.isActiveOption(OPT_HELP_LONG))
		{
			usage();
			//System.exit(0);
			return;
		}

		// Assume defaults
		if(iValidOptions == 0)
		{
			soOptionProcessor.addActiveOption(OPT_ALL_PROGRAMS, "--all");
			//soOptionProcessor.addActiveOption(OPT_GIPL_PROGRAMS, "--gipl");
			soOptionProcessor.addActiveOption(OPT_GEE, "--gee");
			soOptionProcessor.addActiveOption(OPT_PARALLEL, "--parallel");
			soOptionProcessor.addActiveOption(OPT_REGRESSION, "--regression");
		}

		if(soOptionProcessor.isActiveOption(OPT_DEBUG))
		{
	    	Debug.enableDebug(true);
		}
	}
	
	/**
	 * Body of the regression testing app.
	 * Implements Runnable.
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
	    try
	    {
	    	if(soOptionProcessor.isActiveOption(OPT_JUNIT))
	    	{
	    		oTestSuite.addTest(new DictionaryTest());
	    		oTestSuite.addTest(new DictionaryTest());

	    		System.out.println
				(
					"JUnit Test Suite: " + oTestSuite + "\n" +
					"Tests: " + oTestSuite.countTestCases() + "\n" +
					""
				);
	    		
	    		TestResult oTestResult = new TestResult();
				oTestSuite.run(oTestResult);
	    		System.out.println
				(
					"JUnit Test Result:\n" +
					"  runs:     " + oTestResult.runCount() + "\n" +
					"  errors:   " + oTestResult.errorCount() + "\n" +
					"  failures: " + oTestResult.failureCount() + "\n" +
					""
				);
	    		
	    		if(oTestResult.errorCount() > 0)
	    		{
	    			Enumeration<TestFailure> oErrors = oTestResult.errors();
	    			
	    			while(oErrors.hasMoreElements())
	    			{
	    				System.err.println("Error: " + oErrors.nextElement());
	    			}
	    		}
	    		
	    		if(oTestResult.failureCount() > 0)
	    		{
	    			Enumeration<TestFailure> oFailures = oTestResult.failures();
	    			
	    			while(oFailures.hasMoreElements())
	    			{
	    				System.err.println("Failure: " + oFailures.nextElement());
	    			}
	    		}
	    	}
	    	else if(soOptionProcessor.isActiveOption(OPT_REGRESSION))
	    	{
				for(int iOption = OPT_GIPL_PROGRAMS; iOption <= OPT_ALL_PROGRAMS; iOption++)
				{
					if(soOptionProcessor.isActiveOption(iOption))
					{
						switch(iOption)
						{
							case OPT_GIPL_PROGRAMS:
							{
								testGIPC("--gipl", sstrGIPLTestsDir, sastrGIPLFiles);
	
								if(soOptionProcessor.isActiveOption(OPT_GEE))
								{
									testGEE("--gipl", sstrGIPLTestsDir, sastrGIPLFiles);
								}
	
								break;
							}
	
							case OPT_INDEXICAL_PROGRAMS:
							{
								// Run same tests as GIPL but with Indexical Lucid compiler
								// and it should produce the same results
								testGIPC("--indexical", sstrGIPLTestsDir, sastrGIPLFiles);
	
								// Run on pure/plain Indexical Lucid examples
								testGIPC("--indexical", sstrIndexicalTestsDir, sastrIndexicalFiles);
	
								if(soOptionProcessor.isActiveOption(OPT_GEE))
								{
									testGEE("--indexical", sstrGIPLTestsDir, sastrGIPLFiles);
									testGEE("--indexical", sstrIndexicalTestsDir, sastrIndexicalFiles);
								}
	
								break;
							}
	
							case OPT_GIPSY_PROGRAMS:
							{
								testGIPC("", sstrGIPSYTestsDir, sastrGIPSYFiles);
	
								if(soOptionProcessor.isActiveOption(OPT_GEE))
								{
									testGEE("", sstrGIPSYTestsDir, sastrGIPSYFiles);
								}
	
								break;
							}
	
							// Run all of the above
							case OPT_ALL_PROGRAMS:
							{
								testGIPC("--gipl", sstrGIPLTestsDir, sastrGIPLFiles);
								testGIPC("--indexical", sstrGIPLTestsDir, sastrGIPLFiles);
								testGIPC("--indexical", sstrIndexicalTestsDir, sastrIndexicalFiles);
								testGIPC("", sstrGIPSYTestsDir, sastrGIPSYFiles);
	
								if(soOptionProcessor.isActiveOption(OPT_GEE))
								{
									testGEE("--gipl", sstrGIPLTestsDir, sastrGIPLFiles);
									testGEE("--indexical", sstrGIPLTestsDir, sastrGIPLFiles);
									testGEE("--indexical", sstrIndexicalTestsDir, sastrIndexicalFiles);
									testGEE("", sstrGIPSYTestsDir, sastrGIPSYFiles);
								}
	
								break;
							}
	
							// Should never happen
							default:
							{
								throw new GIPSYException("Regression: Internal error; invalid option: " + iOption);
							}
						}
					}
				}
			}
	    }
	    catch(Exception e)
		{
			System.err.println(e);
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
			//System.exit(1);
		}
	}
	
	/**
	 * Main Regression Testing Routine.
	 * @param argv command-line arguments
	 */
	public static final void main(String[] argv)
	throws InterruptedException
	{
		System.out.println("The Regression Testing application $Revision: 1.19 $ has begun.");
		Regression oRegression = new Regression(argv);
		oRegression.start();
		oRegression.join();
		System.out.println("The Regression Testing application $Revision: 1.19 $ has completed.");
	}

	public static void testGIPC(String pstrMajorOption, String pstrDirectory, String[] pastrFiles)
	throws GIPSYException
	{
		try
		{
			//Logger oLogger = new Logger("regression." + pstrMajorOption + pstrDirectory.replaceAll("/", ".") + ".gipc.log", Logger.LOG_STDOUT_TO_FILE, false);

			for(int i = 0; i < pastrFiles.length; i++)
			{
				String strFilename = pstrDirectory + "/" + pastrFiles[i];

				Logger oLogger = new Logger(strFilename + ".gipc.log", Logger.LOG_STDOUT_TO_FILE, false);

				GIPC.main
				(
					new String[]
					{
						pstrMajorOption,
//						soOptionProcessor.getOption(OPT_GEE, true),
						soOptionProcessor.getOption(OPT_DEBUG, true),
						strFilename
					}
				);
			}
		}
		catch(GIPSYException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new GIPSYException(e.getMessage(), e);
		}
	}

	public static void testGEE(String pstrMajorOption, String pstrDirectory, String[] pastrFiles)
	throws GIPSYException
	{
		try
		{
			//Logger oLogger = new Logger("regression." + pstrMajorOption + pstrDirectory.replaceAll("/", ".") + ".log", Logger.LOG_STDOUT_TO_FILE, false);

			for(int i = 0; i < pastrFiles.length; i++)
			{
				String strFilename = (pstrDirectory + "/" + pastrFiles[i]).replaceAll("\\.ipl$", "\\.gipsy");

				Logger oLogger = new Logger(strFilename + ".gee.log", Logger.LOG_STDOUT_TO_FILE, false);

				GEE.main
				(
					new String[]
					{
						soOptionProcessor.getOption(OPT_DEBUG, true),
						strFilename
					}
				);
			}
		}
		catch(GIPSYException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new GIPSYException(e.getMessage(), e);
		}
	}
	
	public class DictionaryTest extends TestCase
	{
		private Dictionary oExpectedDictiornary;
		private CodeSegment oCodeSegment;

		protected void setUp()
		{
			this.oExpectedDictiornary = new Dictionary();
			this.oExpectedDictiornary.add("foo");
			this.oExpectedDictiornary.add("bar");
			this.oExpectedDictiornary.add("baz");

			this.oCodeSegment = new CodeSegment
			(
				"GIPL",
				"foo + bar - baz where foo = 1; bar = 2; baz = 3; end"
			);
		}
		
		public void testGIPLDictionary()
		throws GIPCException
		{
			GIPC oGIPC = new GIPC(this.oCodeSegment.getSourceCodeStream(), new String[] {"--gipl", "--debug"});
			oGIPC.compile();
			assertEquals(oGIPC.getDictionary().sort(), this.oExpectedDictiornary.sort());
		}

		public void testPreprocessorDictionary()
		throws GIPCException
		{
			GIPC oGIPC = new GIPC(this.oCodeSegment.getSourceCodeStream(), new String[] {"--preprocess-only", "--debug"});
			oGIPC.compile();
			assertEquals(oGIPC.getDictionary().sort(), this.oExpectedDictiornary.sort());
		}
	}
	
	/**
	 * Tests dictionary embryo, stubs, and code segments data structures.
	 */
	public class PreprocessorTest extends TestCase
	{
		
	}

	/**
	 * Displays usage information of this application.
	 */
	public static final void usage()
	{
		System.out.println
		(
			"\nGIPSY Regression Tesing Suite, $Revision: 1.19 $\n\n" +
			"Usage:\n\n" +
			"    --help | -h   display this help and exit\n" +
			"    --directory=NAME  read test files from the specified directory instead of defaults\n" +
			"    --gipl            test pure GIPL programs only\n" +
			"    --indexical       test pure GIPL and Indexical programs with Indexical Lucid compiler\n" +
			"    --gipsy           test general-style GIPSY programs with code segments\n" +
			"    --all             do all of the above tests in one run (default)\n" +
			"    --gee             if specified, run GEE after compilation (default)\n" +
			"    --regression      run in the output regression mode\n" +
			"    --junit           run in the JUnit mode\n" +
			"    --junitgui        run in the JUnit's GUI mode\n" +
			"    --debug           enable debug mode and pass it down the line\n" +
			"\n"
		);
	}
	
	public int countTestCases()
	{
		return oTestSuite.countTestCases();
	}
	
	public void run(TestResult poTestResult)
	{
		oTestSuite.run(poTestResult);
	}

//	 build a tree manually in a form of Simple Node, imported by syntax and semantic checking
	/*  void buildDict()
	  {
	    // a = if (#.d = 0 ) then 1 else ( (if(x<=y) then x else y) @.d (#.d-1)
	    SimpleNode node1, node2, node3, node4, node5, node6, node7, node8, node9, node10, node11, node12, node13, node14, node15;

	    node1 = new SimpleNode(JJTIF);
	    node2 = new SimpleNode(JJTLE);
	    node3 = new SimpleNode(JJTCONST);
	    node3.setImage("1");
	    node4 = new SimpleNode(JJTAT);

	    node5 = new SimpleNode(JJTHASH);
	    node6 = new SimpleNode(JJTCONST);
	    node6.setImage("0");
	    node7 = new SimpleNode(JJTADD);
	    node8 = new SimpleNode(JJTDIM);
	    node8.setImage("0"); // dimension d
	    node9 = new SimpleNode(JJTMIN);

	    node10 = new SimpleNode(JJTDIM);
	    node10.setImage("0"); // dimension d
	    node11 = new SimpleNode(JJTID);
	    node11.setImage("a"); // identifier a

	    node12 = new SimpleNode(JJTCONST);
	    node12.setImage("1");
	    node13 = new SimpleNode(JJTHASH);
	    node14 = new SimpleNode(JJTDIM);
	    node14.setImage("0"); // dimension d

	    node15 = new SimpleNode(JJTCONST);
	    node15.setImage("1");

	    node2.jjtSetParent(node1);
	    node3.jjtSetParent(node1);
	    node4.jjtSetParent(node1);
	    node1.jjtAddChild(node2, 0);
	    node1.jjtAddChild(node3, 1);
	    node1.jjtAddChild(node4, 2);

	    node5.jjtSetParent(node2);
	    node6.jjtSetParent(node2);
	    node2.jjtAddChild(node5, 0);
	    node2.jjtAddChild(node6, 1);

	    node10.jjtSetParent(node5);
	    node5.jjtAddChild(node10, 0);

	    node7.jjtSetParent(node4);
	    node8.jjtSetParent(node4);
	    node9.jjtSetParent(node4);
	    node4.jjtAddChild(node7, 0);
	    node4.jjtAddChild(node8, 1);
	    node4.jjtAddChild(node9, 2);

	    node11.jjtSetParent(node7);
	    node12.jjtSetParent(node7);
	    node7.jjtAddChild(node11, 0);
	    node7.jjtAddChild(node12, 1);

	    node13.jjtSetParent(node9);
	    node15.jjtSetParent(node9);
	    node9.jjtAddChild(node13, 0);
	    node9.jjtAddChild(node15, 1);

	    node14.jjtSetParent(node13);
	    node13.jjtAddChild(node14, 0);

	    dict.add( 0, node1); // set dictionary "a" - node1
	  }
	*/
}

// EOF
