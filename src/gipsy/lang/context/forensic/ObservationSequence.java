package gipsy.lang.context.forensic;

import gipsy.lang.GIPSYIdentifier;
import gipsy.lang.context.Dimension;
import gipsy.lang.context.TagSet;

/**
 * Observation sequence dimension.
 * @author Serguei Mokhov
 * @version $Id: ObservationSequence.java,v 1.1 2013/08/25 02:53:20 mokhov Exp $
 */
public class ObservationSequence
extends Dimension
{
	/**
	 * For serialization versioning.
	 */
	private static final long serialVersionUID = -6154707125598736370L;

	/**
	 * 
	 */
	public ObservationSequence()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param poDimensionName
	 * @param poDimensionTags
	 */
	public ObservationSequence(GIPSYIdentifier poDimensionName, TagSet poDimensionTags)
	{
		super(poDimensionName, poDimensionTags);
	}

	/**
	 * @param poDimension
	 */
	public ObservationSequence(Dimension poDimension)
	{
		super(poDimension);
	}
}

// EOF
