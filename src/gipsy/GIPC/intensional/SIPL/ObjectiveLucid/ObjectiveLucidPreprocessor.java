package gipsy.GIPC.intensional.SIPL.ObjectiveLucid;

import java.io.InputStream;

import gipsy.GIPC.GIPCException;
import gipsy.GIPC.intensional.SIPL.JLucid.JLucidPreprocessor;

/**
 * Objective Lucid Preprocessor Class.
 * @author Serguei Mokhov
 * @since 1.0.0
 * @see gipsy.GIPC.intensional.SIPL.JLucid.JLucidPreprocessor
 */
public class ObjectiveLucidPreprocessor
extends JLucidPreprocessor
{
	public ObjectiveLucidPreprocessor()
	throws GIPCException
	{
		this(System.in);
	}

	public ObjectiveLucidPreprocessor(InputStream poSourceStream)
	throws GIPCException
	{
		super(poSourceStream);
		addValidSegmentName("typedecl");
	}
}

// EOF

