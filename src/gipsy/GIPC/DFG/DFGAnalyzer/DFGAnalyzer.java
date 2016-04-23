package gipsy.GIPC.DFG.DFGAnalyzer;

import java.io.InputStream;

import marf.util.BaseThread;
import gipsy.GIPC.GIPCException;
import gipsy.GIPC.ICompiler;
import gipsy.interfaces.AbstractSyntaxTree;

/**
 * This class is the main class.
 * Rewritten by Serguei to implement ICompiler.
 * 
 * @author <a href="mailto:dingyimin@zworg.com">Yimin Ding</a>
 * @author Serguei Mokhov
 * @version $Id: DFGAnalyzer.java,v 1.10 2013/01/09 14:50:25 mokhov Exp $
 * @since 1.0.0
 */
public class DFGAnalyzer
implements ICompiler
{
	/**
	 * Input source file name.
	 */
	private String strFilename;

	/**
	 * Facet to the DFG parser.
	 */
	private ParserFacet oParserFacet;

	/**
	 * Local references to the produced AST.
	 */
	private AbstractSyntaxTree oAST;

	/**
	 * Last exception happened inside this class' instance.
	 */
	private GIPCException oLastException;
	
	/**
	 * Constructor.
	 * @param pstFilename file name of a source file to process
	 */
	public DFGAnalyzer(String pstFilename)
	{
		this.strFilename = pstFilename;
	}
	
	/**
	 * Implementation of ICompiler. 
	 * @throws GIPCException if there was an initialization error 
	 */
	public void init()
	throws GIPCException
	{
		this.oParserFacet = new ParserFacet();
	}
	
	/**
	 * Implementation of ICompiler. 
	 * @throws GIPCException if there was a parse error 
	 */
	public AbstractSyntaxTree parse()
	throws GIPCException
	{
		// call parser
		this.oParserFacet.parsing(this.strFilename);
		this.oParserFacet.showtree();
		return new AbstractSyntaxTree(this.oParserFacet.getNode());
	}

	/**
	 * Implementation of ICompiler.
	 * @param poExtraArgs unused
	 */
	public AbstractSyntaxTree compile(Object poExtraArgs)
	throws GIPCException
	{
		init();
		this.oAST = parse();
		
		// TODO: Kludgery to be fixed; DFG SimpleNode vs. the general 
		new LucidCodeGenerator().genLC((SimpleNode)this.oAST.getRoot(), null);

		return this.oAST;
	}

	public AbstractSyntaxTree compile()
	throws GIPCException
	{
		return compile(null);
	}

	/**
	 * Implementation of ICompiler.
	 */
	public AbstractSyntaxTree getAbstractSyntaxTree()
	{
		return this.oAST;
	}

	/**
	 * Body of the compiler thread.
	 * Implements Runnable.
	 */
	public void run()
	{
		try
		{
			compile(null);
		}
		catch(GIPCException e)
		{
			System.err.println(e);
			e.printStackTrace(System.err);
			this.oLastException = e;
		}
	}
	
	/**
	 * Retrieves last exception occurred.
	 * @return GIPCException the last exception, null if there was none.
	 */
	//public GIPCException getLastException()
	public Exception getLastException()
	{
		return this.oLastException;
	}

	/**
	 * Does nothing. Provided as a part of the ICompiler interface.
	 * @param poInputStream unused
	 */
	public void setSourceCodeStream(InputStream poInputStream)
	{
		// do nothing
	}

	/**
	 * The main program.
	 * <p>Load a file to parse.</p><br />
	 *
	 * @param argv Filename which will be parsed by this Analyzer
	 */
	public static void main(String[] argv)
	{
		try
		{
			if(argv.length < 1)
			{
				System.out.println("Usage: java DFGAnalyzer filename");
				System.exit(0);
			}
		
			new BaseThread(new DFGAnalyzer(argv[0])).start();
		}
		catch(Exception e)
		{
			System.err.println(e);
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}
}

// EOF
