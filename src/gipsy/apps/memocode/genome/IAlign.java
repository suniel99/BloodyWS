package gipsy.apps.memocode.genome;

/**
 * Generic Align API.
 * 
 * @author Serguei Mokhov
 * @version $Id: IAlign.java,v 1.5 2012/04/07 19:48:46 mokhov Exp $
 */
public interface IAlign
{
	/**
	 * Assumed alignment for sequences in the sequence file, in bytes.
	 * (Ported from the reference implementation).
	 */
	int SEQUENCE_ALIGN = 8;
	
	/**
	 * Sequence length in base pairs.
	 */
	int DEFAULT_SEQUENCE_LENGTH = 100;
	
	/**
	 * Short reference file for testing.
	 */
	public static final String SHORT_REFERENCE_FILE = "src/gipsy/apps/memocode/genome/reference/short_reference.bin";
	
	/**
	 * Short sequence file for testing.
	 */
	public static final String SHORT_SEQUENCES_FILE = "src/gipsy/apps/memocode/genome/reference/short_sequences.bin"; 

	/**
	 * Complete reference human genome.
	 */
	public static final String FULL_REFERENCE_FILE = "src/gipsy/apps/memocode/genome/reference/human_g1k_v37.bin"; 

	/**
	 * Attempt to match an array of two-bit sequences
	 * against a reference sequence.
	 *
	 * This allocates a buffer large enough to hold a single sequence
	 * and shifts the whole reference sequence through it, one pair at a time.
	 * At each point, this buffer is compared to each of the given sequences.
	 *
	 * @param patReferenceBase beginning of reference sequence
	 * @param patSequences beginning of sequences
	 * @param piStartSequence index of first sequence in array 
	 * @param piSequenceLength base pairs per sequence
	 * @param piEndSequence one more than index of last sequence
	 * @return return the matches array with the results; one per sequence
	 */
	Match[] match
	(
		byte[] patReferenceBase,
		byte[] patSequences,
		int piStartSequence,
		int piSequenceLength,
		int piEndSequence
	);
	
	/**
	 * Presume the reference base is known in advance and loaded.
	 * @param patSequences beginning of sequences
	 * @param piStartSequence index of first sequence in array 
	 * @param piSequenceLength base pairs per sequence
	 * @param piEndSequence one more than index of last sequence
	 * @return return the matches array with the results; one per sequence
	 * @see #match(byte[], byte[], int, int, int)
	 */
	Match[] match
	(
		byte[] patSequences,
		int piStartSequence,
		int piSequenceLength,
		int piEndSequence
	);

	/**
	 * Presume the reference base is known in advance and loaded and the
	 * last sequence is computed from the lengths of the sequences array
	 * and each sequence's length.
	 * @param patSequences beginning of sequences
	 * @param piStartSequence index of first sequence in array 
	 * @param piSequenceLength base pairs per sequence
	 * @return return the matches array with the results; one per sequence
	 * @see #match(byte[], int, int, int)
	 */
	Match[] match
	(
		byte[] patSequences,
		int piStartSequence,
		int piSequenceLength
	);

	/**
	 * Presume the reference base is known in advance and loaded and the
	 * last sequence is computed from the lengths of the sequences array
	 * and each sequence's length, where the latter is assumed to be a default.
	 * @param patSequences beginning of sequences
	 * @param piStartSequence index of first sequence in array 
	 * @return return the matches array with the results; one per sequence
	 * @see #DEFAULT_SEQUENCE_LENGTH
	 */
	Match[] match
	(
		byte[] patSequences,
		int piStartSequence
	);

	/**
	 * Matches assume a single sequence at the beginning of the default length.
	 * @param patSequences beginning of sequences
	 * @return return the matches array with the results; one per sequence
	 * @see #DEFAULT_SEQUENCE_LENGTH
	 */
	Match[] match
	(
		byte[] patSequences
	);
}

// EOF
