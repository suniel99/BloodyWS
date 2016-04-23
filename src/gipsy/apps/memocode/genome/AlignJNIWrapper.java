package gipsy.apps.memocode.genome;

/**
 * A JNI-based wrapper for the reference implementation.
 * @author
 * @version $Id: AlignJNIWrapper.java,v 1.3 2012/03/29 16:53:37 mokhov Exp $
 */
public class AlignJNIWrapper
extends Align
{
	public AlignJNIWrapper()
	{
		super();
	}

	@Override
	public Match[] match
	(
		byte[] patReferenceBase,
		byte[] patSequences,
		int piStartSequence,
		int piSequenceLength,
		int piEndSequence
	)
	{
		return super.match
		(
			patReferenceBase,
			patSequences,
			piStartSequence,
			piSequenceLength,
			piEndSequence
		);
	}
}

// EOF
