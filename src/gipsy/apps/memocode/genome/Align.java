package gipsy.apps.memocode.genome;

import gipsy.util.NotImplementedException;


/**
 * This is meant to be a pure Java implementation of
 * a crude brute-force alignment. Anything new, better,
 * optimized, or native should either override this or
 * implement IAlign directly by themselves.
 *  
 * @author Serguei Mokhov
 * @version $Id: Align.java,v 1.5 2012/04/07 16:35:18 mokhov Exp $
 */
public class Align
implements IAlign
{
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
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see gipsy.apps.memocode.genome.IAlign#match(byte[], int, int, int)
	 */
	@Override
	public Match[] match
	(
		byte[] patSequences,
		int piStartSequence,
		int piSequenceLength,
		int piEndSequence
	)
	{
		// Assume pre-configured default reference base
		return match
		(
			null,
			patSequences,
			piStartSequence,
			piSequenceLength,
			piEndSequence
		);
	}

	/* (non-Javadoc)
	 * @see gipsy.apps.memocode.genome.IAlign#match(byte[], int, int)
	 */
	@Override
	public Match[] match
	(
		byte[] patSequences,
		int piStartSequence,
		int piSequenceLength
	)
	{
		return match
		(
			patSequences,
			piStartSequence,
			piSequenceLength,
			patSequences.length / getBytePaddedSequenceLength(piSequenceLength)
		);
	}

	/* (non-Javadoc)
	 * @see gipsy.apps.memocode.genome.IAlign#match(byte[], int)
	 */
	@Override
	public Match[] match
	(
		byte[] patSequences,
		int piStartSequence
	)
	{
		return match
		(
			patSequences,
			piStartSequence,
			DEFAULT_SEQUENCE_LENGTH
		);
	}

	/* (non-Javadoc)
	 * @see gipsy.apps.memocode.genome.IAlign#match(byte[])
	 */
	@Override
	public Match[] match(byte[] patSequences)
	{
		return match
		(
			patSequences,
			0
		);
	}
	
	/**
	 * Calculate byte-padded sequence length
	 * @param piSequenceLength length in base pairs
	 * @return byte count sequence length
	 */
	public final int getBytePaddedSequenceLength(final int piSequenceLength)
	{
		return
			((piSequenceLength + IAlign.SEQUENCE_ALIGN * 4 - 1)
			& ~(IAlign.SEQUENCE_ALIGN * 4 - 1)) >> 2;
	}
}

// EOF
