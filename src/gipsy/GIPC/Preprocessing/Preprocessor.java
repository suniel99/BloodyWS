package gipsy.GIPC.Preprocessing;

import gipsy.GIPC.GIPCException;
import gipsy.storage.Dictionary;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;

import marf.util.Debug;


/**
 * <p>General GIPSY Program Preprocessor.</p>
 *
 * <p>Takes as an input a GIPSY program and splits into #LANG
 * chunks and feeds to the appropriate LANG parsers. Additionally,
 * it maintains the minimal semantic linkage between the modules via
 * a symbol table, that is passed further on to the SemanticAnalyzer.</p>
 *
 * @author Serguei Mokhov
 * @version $Id: Preprocessor.java,v 1.16 2013/01/09 14:50:36 mokhov Exp $
 * @since 1.0.0
 */
public class Preprocessor
implements PreprocessorParserConstants, PreprocessorParserTreeConstants
{
	/**
	 * Source input stream.
	 */
	protected InputStream oSource = null;

	/**
	 * Root of the AST created by the PreprocessorParser.
	 */
	protected SimpleNode oPreprocessorASTRoot = null;

	/**
	 * List of references to the source code segments of
	 * a GIPSY program.
	 */
	protected Vector<CodeSegment> oCodeSegments = new Vector<CodeSegment>();

	/**
	 * List of valid segment names.
	 */
	protected Vector<String> oValidSegmentNames = new Vector<String>();

	/**
	 * List of invalid segment names.
	 */
	protected Vector<String> oInvalidSegmentNames = new Vector<String>();

	/**
	 * Embryo of the symbol dictionary.
	 * Contains symbols from the #funcdecl and #typedecl
	 * segments.
	 */
	protected Dictionary oDictionary = new Dictionary();

	/**
	 * Hashtable that maps imperative symbols to their
	 * stub nodes to be replaced/updated by the real imperative
	 * nodes at the linking stage.
	 */
	protected Hashtable oImperativeStubs = new Hashtable();

	/**
	 * Preprocess incoming GIPSY program.
	 *
	 * @param poGIPSYCode Source code stream of a GIPSY program
	 */
	public Preprocessor(InputStream poGIPSYCode)
	throws GIPCException
	{
		this.oSource = poGIPSYCode;
	}

	/**
	 * The body of the preprocessing.
	 *
	 * @throws GIPCException if there was a parsing or otherwise error.
	 */
	public void preprocess()
	throws GIPCException
	{
		try
		{
			PreprocessorParser oPreprocessorParser = new PreprocessorParser(oSource);
			oPreprocessorParser.parse();
			this.oPreprocessorASTRoot = oPreprocessorParser.getPreprocessorASTRoot();

			// Valid takes precedence over invalid if specified
			if(this.oInvalidSegmentNames.size() > 0 && this.oValidSegmentNames.size() > 0)
			{
				this.oInvalidSegmentNames.clear();
			}

			splitCodeSegments(this.oPreprocessorASTRoot);
			//produceImperativeStubs(this.oPreprocessorASTRoot);
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			throw new GIPCException
			(
				"Source InputStream has not been initialized.\n" +
				"HINT: Make sure that the parameter to the constructor or setSourceStream() is not null.",
				e
			);
		}
		catch(Exception e)
		{
			throw new GIPCException(e);
		}
	}

	/**
	 * Initializes the source code stream.
	 *
	 * @param poSource incoming GIPSY source code.
	 */
	public void setSourceStream(InputStream poSource)
	{
		this.oSource = poSource;
	}

	/**
	 * Returns the root node of the preprocessed AST.
	 *
	 * @return SimpleNode the root
	 */
	public SimpleNode getPreprocessorASTRoot()
	{
		return this.oPreprocessorASTRoot;
	}

	/**
	 * @return
	 */
	public Dictionary getDictionary()
	{
		return this.oDictionary;
	}

	/**
	 * Splits intensional and imperative code
	 * segments into separate code pieces to be
	 * later fed to appropriate compilers.
	 *
	 * @throws GIPCException
	 */
	private void splitCodeSegments(SimpleNode poRoot)
	throws GIPCException
	{
		//this.oPreprocessorASTRoot.dump("preprocessed");

		SimpleNode oCurrentNode = poRoot;

		int i = oCurrentNode.jjtGetNumChildren();

		for(int c = 0; c < i; c++)
		{
			SimpleNode oChild = (SimpleNode)oCurrentNode.jjtGetChild(c);

			switch(oChild.id)
			{
				case JJTCODESEGMENT:
				{
					Debug.debug("JJTCODESEGMENT!!!!!");

					/*
					 * Extract language; nuke the '#'.
					 *
					 * The #LANG should be first non-blank characters, and when we
					 * split, the element [0] of the string array should be what we
					 * are looking for; discard the rest. Then, starting from index
					 * [1] of this token, we take the rest, thereby discarding leading '#'.
					 *
					 * This kind of post-parsing is necessary due to the wholesale
					 * all-consuming nature of the <LANGDATA> token than is only
					 * separated by <LANGID> subtokens.
					 */
					String strLanguage = oChild.getLexeme().split("\\s")[0].substring(1);

					/*
					 * Go through the filters if any specified.
					 * Filtering applies only if either type of
					 * segments is specified. None means accept all.
					 */

					if(this.oInvalidSegmentNames.size() != 0 || this.oValidSegmentNames.size() != 0)
					{
						String strError =
							"Language name " + strLanguage +
							"is not recognized as valid";

						if(this.oValidSegmentNames.size() > 0)
						{
							if(this.oValidSegmentNames.contains(strLanguage) == false)
							{
								throw new GIPCException(strError);
							}
						}

						if(this.oInvalidSegmentNames.size() > 0)
						{
							if(this.oInvalidSegmentNames.contains(strLanguage) == true)
							{
								throw new GIPCException(strError);
							}
						}
					}


					/*
					 * Extract code, nuke the tag, we extracted above.
					 * Also, trim it -- no point of keeping any possible leftover blanks.
					 */
					String strCode = oChild.getLexeme().replaceFirst("#" + strLanguage, "").trim();

					// Finally, we have the desired code segment.
					this.oCodeSegments.add(new CodeSegment(strLanguage, strCode));
					break;
				}
/*
				case JJTSTART:
				case JJTGIPSY:
				case JJTDECLARATIONS:
				case JJTTYPELIST:
				case JJTCODESEGMENTS:
				case JJTURL:
				case JJTTYPE:
*/
				case JJTPROTOTYPES:
				{
					Debug.debug("PROTOTYPES: " + oChild);
					break;
				}

				case JJTTYPES:
				{
					Debug.debug("TYPES: " + oChild);
					break;
				}

				case JJTFUNCDECLS:
				case JJTTYPEDECLS:
				{
					Debug.debug("DECL: " + oChild);
					//produceImperativeStubs(oChild);
					break;
				}

				default:
				{
				  	Debug.debug("Preprocessor.splitCodeSegments(): unhandled " + jjtNodeName[oChild.id]);
				}
			}

			splitCodeSegments(oChild);
		}
	}

	/**
	 * Produces AST stubs for the identifiers
	 * located in the imperative code segments
	 * but called from intensional parts,
	 * so that the semantic analyzer does not
	 * choke on undefined symbols.
	 *
	 * @throws GIPCException
	 */
	private void produceImperativeStubs(SimpleNode poDeclRoot)
	throws GIPCException
	{

	}

	public Hashtable getImperativeStubs()
	{
		return this.oImperativeStubs;
	}

	/**
	 * Retrieves references to all code segments.
	 * @return Vector of references to produced code segments.
	 */
	public Vector<CodeSegment> getCodeSegments()
	{
		return this.oCodeSegments;
	}

	public void addValidSegmentName(String pstrName)
	{
		if(pstrName == null || pstrName.equals(""))
		{
			throw new IllegalArgumentException("Segment name cannot be null or empty string");
		}

		this.oValidSegmentNames.add(pstrName);
	}

	public void addInvalidSegmentName(String pstrName)
	{
		if(pstrName == null || pstrName.equals(""))
		{
			throw new IllegalArgumentException("Segment name cannot be null or empty string");
		}

		this.oInvalidSegmentNames.add(pstrName);
	}
}

// EOF
