package gipsy.GIPC.intensional.SIPL.JLucid;

import java.io.InputStream;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.Preprocessing.Preprocessor;

/**
 * Split the #JAVA and #JLUCID code chunks.
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.9 $
 */
public class JLucidPreprocessor
extends Preprocessor
{
	protected LucidSource oLucidSource;
	protected JavaSource oJavaSource;

	/**
	 * @param poGIPSYCode
	 * @throws GIPCException
	 */
	public JLucidPreprocessor(InputStream poGIPSYCode)
	throws GIPCException
	{
		super(poGIPSYCode);
		addValidSegmentName("funcdecl");
		addValidSegmentName("JLUCID");
		addValidSegmentName("JAVA");
	}

	public JLucidPreprocessor()
	throws GIPCException
	{
		this(System.in);
	}

	public void preprocess()
	{
		this.oLucidSource = new LucidSource("a; where a = 2 + foo(); end");
		this.oJavaSource = new JavaSource("int foo() {return 2;}");
	}

	public LucidSource getLucidChunk()
	{
		return this.oLucidSource;
	}

	public JavaSource getJavaChunk()
	{
		return this.oJavaSource;
	}
}

// EOF
