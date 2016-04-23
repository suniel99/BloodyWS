package gipsy.lang.context.forensic;

import gipsy.lang.GIPSYIdentifier;
import gipsy.lang.context.Dimension;
import gipsy.lang.context.TagSet;

/**
 * Evidential statement dimension.
 * @author Serguei Mokhov
 * @version $Id: EvidentialStatement.java,v 1.1 2013/08/25 02:53:20 mokhov Exp $
 */
public class EvidentialStatement
extends Dimension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4038848065965134947L;

	/**
	 * 
	 */
	public EvidentialStatement()
	{
		super();
	}

	/**
	 * @param poDimensionName
	 * @param poDimensionTags
	 */
	public EvidentialStatement(GIPSYIdentifier poDimensionName, TagSet poDimensionTags)
	{
		super(poDimensionName, poDimensionTags);
	}

	/**
	 * @param poDimension
	 */
	public EvidentialStatement(Dimension poDimension)
	{
		super(poDimension);
	}
}

// EOF
