package gipsy.apps.memocode.genome;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import com.sun.jna.Library;
import com.sun.jna.Native;


/**
 * A JNA-based wrapper for the reference implementation.
 * @author Sleiman Rabah
 * @author Serguei Mokhov
 * @version $Id: AlignJNAWrapper.java,v 1.10 2012/04/07 19:48:46 mokhov Exp $
 */
public class AlignJNAWrapper
extends Align
{
	/**
	 * 
	 */
	public static final String LIBRARY_NAME = "src/gipsy/apps/memocode/genome/reference/alternate/libalign.so";

	/**
	 * The JNA mapping reference.
	 */
	private IAlignJNAWrapper oJNAWrapperLib = null;
	
	/**
	 * The JNA collection of matches.
	 */
	private JNAMatch[] aoJNAMatches = null;
	
	/**
	 * JNA-wrapper library interface for native mapping.
	 * @author Sleiman Rabah
	 */
	public interface IAlignJNAWrapper
	extends Library
	{
		public int main(int piArgc, String[] pastrArgv);

		public  JNAMatch reMain
		(
			int piNumSequences,
	 		byte[] patSequences,
			int piArgc,
			String[] pastrArgv
		);
        
        public JNAMatch getMatches
        (
    		 byte[] patReferenceBase,
    		 int iReferenceTotal,
    		 byte[] patSequences,
    		 int iStartSequence,
    		 int iSequenceLength,
    		 int iEndSequence
    	);
        
        public void freeMatches(JNAMatch[] matches);
    }

	
	public AlignJNAWrapper()
	{
		super();

		this.oJNAWrapperLib = (IAlignJNAWrapper)Native.loadLibrary
		(
			AlignJNAWrapper.LIBRARY_NAME,
			IAlignJNAWrapper.class
		);
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
		Match[] aoMatches = null;
		
		try
		{
			callGennomeAlignMatch
			(
				patReferenceBase,
				patSequences,
				piStartSequence,
				piSequenceLength,
				piEndSequence
			);
			
			if(this.aoJNAMatches != null)
			{
				aoMatches = new Match[this.aoJNAMatches.length];
				
				for(int i = 0; i < this.aoJNAMatches.length; i++)
				{
					aoMatches[i] = this.aoJNAMatches[i].adaptToMatch();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
		}
		
		return aoMatches;
	}	
	
	@Override
	public Match[] match
	(
		byte[] patSequences,
		int piStartSequence,
		int piSequenceLength,
		int piEndSequence
	)
	{
		Match[] aoMatches = null;
		
		try
		{
			callGennomeAlignReMain
			(
				patSequences,
				//FULL_REFERENCE_FILE,
				SHORT_REFERENCE_FILE,
				piSequenceLength
			);
			
			if(this.aoJNAMatches != null)
			{
				aoMatches = new Match[this.aoJNAMatches.length];
				
				for(int i = 0; i < this.aoJNAMatches.length; i++)
				{
					aoMatches[i] = this.aoJNAMatches[i].adaptToMatch();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
		}
		
		return aoMatches;
	}

	/**
	 * Wrapper's testing main().
	 * @param argv
	 */
	public static void main(String[] argv)
	{
		try
		{
			AlignJNAWrapper oAlignJNAWrapper = new AlignJNAWrapper();
			
			// Call the main function in "libalign.so".
			/*oAlignJNAWrapper.callGennomeAlignMain
			(
				"src/gipsy/apps/memocode/genome/reference/short_reference.bin",
				"src/gipsy/apps/memocode/genome/reference/short_sequences.bin",
				"100"
			);
			*/
/*			
			byte[] atReferenceBase = new byte[]
			{
				2,2,2,0,2,2,2,0, 
				2,2,2,0,2,2,2,0, 
				1,1,1,0,1,1,1,0,
				3,3,3,0,3,3,3,0,
				4,4,4,0,4,4,4,0, 
				5,5,5,0,5,5,5,0,
				4,4,4,0,4,4,4,0, 
				6,6,6,0,6,6,6,0,
				2,2,2,0,2,2,2,0, 
			};
			
			byte[] atSequences = new byte[]
			{
				2,2,2,0,2,2,2,0, // 0: 16, 1
				4,4,4,0,4,4,4,0, // 1: 80. 2
				7,7,7,0,7,7,7,0, // 2: -
			};*/
			
			byte[] atReferenceBase = new byte[]
  			{
				'2','2','2','0','2','2','2','0', 
				'2','2','2','0','2','2','2','0', 
				'1','1','1','0','1','1','1','0',
				'3','3','3','0','3','3','3','0',
				'4','4','4','0','4','4','4','0', 
				'5','5','5','0','5','5','5','0',
				'4','4','4','0','4','4','4','0', 
				'6','6','6','0','6','6','6','0',
				'2','2','2','0','2','2','2','0', 
  			};
  			
  			byte[] atSequences = new byte[]
  			{
  				'2','2','2','0','2','2','2','0', // 0: 16',' 1
  				'4','4','4','0','4','4','4','0', // 1: 80. 2
  				'7','7','7','0','7','7','7','0', // 2: -
  			};
			
  			/*
			// Call the match function in "libalign.so"
			oAlignJNAWrapper.callGennomeAlignMatch
			(
				atReferenceBase,
				atSequences,
				0, 
				32, // in base pairs, each pair = 2 bits long
				3
			);
			//*/
			
  			FileInputStream oFIS = new FileInputStream(new File(SHORT_SEQUENCES_FILE));
  			BufferedInputStream oBIS = new BufferedInputStream(oFIS);

  			// Quick test on the short sequences file which has 320 bytes (10 sequences)
  			atSequences = new byte[320];
  			
  			int iReadRetVal = oBIS.read(atSequences, 0, atSequences.length);
  			
  			System.out.println("iReadRetVal = " + iReadRetVal);
			
			oAlignJNAWrapper.callGennomeAlignReMain
			(
				atSequences,
				SHORT_REFERENCE_FILE,
				DEFAULT_SEQUENCE_LENGTH
			);
			
			oBIS.close();
			oFIS.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	/**
	 * Calls the main function defined in "align.c". 
	 * 
	 * @param pstrShortReferenceFile 
	 * @param pstrShortSequenceFile
	 * @param pstrSequenceLength
	 * @throws Exception
	 */
	private void callGennomeAlignMain
	(
		String pstrShortReferenceFile,
		String pstrShortSequenceFile,
		String pstrSequenceLength
	)
	throws Exception
	{
		if(this.oJNAWrapperLib == null)
		{
			this.oJNAWrapperLib = (IAlignJNAWrapper)Native.loadLibrary
			(
				AlignJNAWrapper.LIBRARY_NAME,
				IAlignJNAWrapper.class
			);
		}
		
		String[] astrArgv = {"align", pstrShortReferenceFile, pstrShortSequenceFile, pstrSequenceLength};        
		this.oJNAWrapperLib.main(astrArgv.length, astrArgv);
	}
	
	/**
	 * Calls the reMain function defined in "align.c". 
	 * 
	 * @param patSequences
	 * @param pstrReferenceFile
	 * @param pstrSequenceLength
	 * @throws Exception
	 */
	private void callGennomeAlignReMain
	(
		byte[] patSequences,
		String pstrReferenceFile,
		int piSequenceLength
	)
	throws Exception
	{
		if(this.oJNAWrapperLib == null)
		{
			this.oJNAWrapperLib = (IAlignJNAWrapper)Native.loadLibrary
			(
				AlignJNAWrapper.LIBRARY_NAME,
				IAlignJNAWrapper.class
			);
		}
		
		String[] astrArgv = 
		{
			"align",
			pstrReferenceFile,
			String.valueOf(piSequenceLength)
		};
		
		int iNumSequences = patSequences.length / getBytePaddedSequenceLength(piSequenceLength);
		
		JNAMatch oJNAMatch = this.oJNAWrapperLib.reMain
		(
			iNumSequences,
			patSequences,
			astrArgv.length,
			astrArgv
		);

		if(oJNAMatch != null)
		{
			System.out.println("reMain: Pointer match position: " + oJNAMatch.lPosition);
			System.out.println("reMain: Pointer match count: " + oJNAMatch.iCount);
			
			this.aoJNAMatches = (JNAMatch[])oJNAMatch.toArray(iNumSequences);
			
			System.out.println("reMain: Received matches count: " + this.aoJNAMatches.length);
			
			int s = 0;
			
			for(JNAMatch oNativeMatch: this.aoJNAMatches)
			{
				System.out.println("reMain: seuence: " + s++);
				System.out.println("reMain: Received match position: " + oNativeMatch.lPosition);
				System.out.println("reMain: Received match count: " + oNativeMatch.iCount);
			}
		 
			//m_oJNAWrapperLib.free_matches(poReceivedMatches);
		}
		else
		{
			System.err.println("reMain: Looks like oJNAMatch is null...");
		}
	}
	
	/**
	 * Calls the "match" function defined in "align.c"
	 * 
	 * @param patReferenceBase beginning of reference sequence
	 * @param patSequences     beginning of sequences
	 * @param piStartSequence  index of first sequence in array 
	 * @param piSequenceLength base pairs per sequence
	 * @param piEndSequence    one more than index of last sequence
	 * @throws Exception
	 */
	private void callGennomeAlignMatch
	(
		byte[] patReferenceBase,
		byte[] patSequences,
		int piStartSequence,
		int piSequenceLength,
		int piEndSequence
	)
	throws Exception
	{
		//System.getProperties().list(System.out);
		
		if(this.oJNAWrapperLib == null)
		{
			this.oJNAWrapperLib = (IAlignJNAWrapper)Native.loadLibrary
			(
				AlignJNAWrapper.LIBRARY_NAME,
				IAlignJNAWrapper.class
			);
		}
							
		JNAMatch oJNAMatch = this.oJNAWrapperLib.getMatches
		( 
			patReferenceBase, 
			patReferenceBase.length, 
			patSequences, 
			piStartSequence, 
			piSequenceLength, 
			piEndSequence
		);
		
		if(oJNAMatch != null)
		{
			System.out.println("Pointer match position: " + oJNAMatch.lPosition);
			System.out.println("Pointer match count: " + oJNAMatch.iCount);
			
			this.aoJNAMatches = (JNAMatch[])oJNAMatch.toArray(piEndSequence);
			
			System.out.println("Received matches count: " + this.aoJNAMatches.length);
			
			for(JNAMatch oNativeMatch: this.aoJNAMatches)
			{
				System.out.println("Received match position: " + oNativeMatch.lPosition);
				System.out.println("Received match count: " + oNativeMatch.iCount);
			}
		 
			//m_oJNAWrapperLib.free_matches(poReceivedMatches);
		}
		else
		{
			System.err.println("Looks like oJNAMatch is null...");
		}
	}
}

// EOF
