package gipsy.apps.memocode.genome;

import java.io.Serializable;


/**
 * Result of matching a single sequence; IAlign.match()'s
 * implementation fills in an array.
 * 
 * Match data structure somewhat mimicking the match struct in the
 * reference implementation.
 * 
 * @author Serguei Mokhov
 * @version $Id: Match.java,v 1.4 2012/04/07 16:35:18 mokhov Exp $
 * @see IAlign#match(byte[], byte[], int, int, int)
 */
public class Match
implements Serializable
{
	/**
	 * For serialization versionning and transmission. 
	 */
	private static final long serialVersionUID = 6376576556615023661L;

	/**
	 * Position of a match if count > 0. 
	 */
	//protected int iPosition;
	protected long lPosition;
	
	/**
	 * Number of matches found.
	 */
	protected int iCount;
	
	//public Match(int piPosition, int piCount)
	public Match(long plPosition, int piCount)
	{
		//this.iPosition = piPosition;
		this.lPosition = plPosition;
		this.iCount = piCount;
	}

	//public Match(int piPosition)
	public Match(long plPosition)
	{
		//this(piPosition, 1);
		this(plPosition, 1);
	}

	public int incCount()
	{
		return ++this.iCount;
	}
	
	public int getCount()
	{
		return this.iCount;
	}
	
	//public int getPosition()
	public long getPosition()
	{
		//return this.iPosition;
		return this.lPosition;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Match [iCount=");
		builder.append(iCount);
		builder.append(", iPosition=");
		//builder.append(iPosition);
		builder.append(lPosition);
		builder.append("]");
		return builder.toString();
	}
}

// EOF
