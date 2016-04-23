package gipsy.lang.context.forensic;

import gipsy.lang.GIPSYDouble;
import gipsy.lang.GIPSYIdentifier;
import gipsy.lang.GIPSYInteger;
import gipsy.lang.GIPSYType;
import gipsy.lang.context.Dimension;
import gipsy.lang.context.TagSet;

/**
 * Observation dimension.
 * @author Serguei Mokhov
 * @version $Id: Observation.java,v 1.1 2013/08/25 02:53:20 mokhov Exp $
 */
public class Observation
extends Dimension
{
	/**
	 * The P part of (P, min, max, w, t). 
	 */
	protected GIPSYType oProperty = null;
	
	/**
	 * The min part of (P, min, max, w, t). 
	 */
	protected GIPSYInteger oMin = new GIPSYInteger(1);
	
	/**
	 * The max part of (P, min, max, w, t). 
	 */
	protected GIPSYInteger oMax = new GIPSYInteger(0);
	
	/**
	 * The w part of (P, min, max, w, t). 
	 */
	protected GIPSYDouble oWeight = new GIPSYDouble(1.0);
	
	/**
	 * The t part of (P, min, max, w, t). 
	 */
	protected GIPSYInteger oTimestamp = null;
	
	/**
	 * For serialization versioning.
	 */
	private static final long serialVersionUID = -8940414749785764521L;

	/**
	 * 
	 */
	public Observation()
	{
		super();
	}

	/**
	 * @param poDimensionName
	 * @param poDimensionTags
	 */
	public Observation(GIPSYIdentifier poDimensionName, TagSet poDimensionTags)
	{
		super(poDimensionName, poDimensionTags);
	}

	/**
	 * @param poDimension
	 */
	public Observation(Dimension poDimension)
	{
		super(poDimension);
	}
}

// EOF
