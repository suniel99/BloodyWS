package gipsy.apps.memocode.genome;

import com.sun.jna.Structure;


/**
 * Represents a structure of sequence matches in JNA.
 * 
 * @author Sleiman Rabah 
 * @version $Id: JNAMatch.java,v 1.3 2012/04/07 16:35:18 mokhov Exp $
 */
public class JNAMatch
extends Structure
{
	/**
	 * Position of a match if count > 0. 
	 */
	//public int iPosition;
	public long lPosition;
	
	/**
	 * Number of matches found.
	 */
	public int iCount;
	//public long iCount;
	
	public JNAMatch()
	{
		super();
	}
	
	public Match adaptToMatch()
	{
		return new Match(this.lPosition, this.iCount);
	}

	public void adaptToMatch(Match poMatchToSet)
	{
		poMatchToSet.iCount = this.iCount;
		poMatchToSet.lPosition = this.lPosition;
	}

	/*
	public JNAMatch(int piPosition, int piCount)
	{
		this.iPosition = piPosition;
		this.iCount = piCount;
	}

	public JNAMatch(int piPosition)
	{
		this(piPosition, 1);
	}
	
	public void copyObject(JNAMatch poJNAMatchFrom) {
		this.iCount = poJNAMatchFrom.iCount;
		this.iPosition = poJNAMatchFrom.iPosition;
	}

	public void incCount()
	{
		this.iCount++;
	}
	/*
	public int getCount()
	{
		return this.iCount;
	}*/
	/*
	public int getPosition()
	{
		return this.iPosition;
	}
	*/		
}

// EOF
