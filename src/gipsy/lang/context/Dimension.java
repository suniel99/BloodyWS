package gipsy.lang.context;

import gipsy.lang.GIPSYIdentifier;
import gipsy.lang.GIPSYType;


/**
 * Dimension contains the identifier and a tag set attached to it.
 * It also merges the old notion of Context Element, by introducing the current tag.
 * 
 * @author Xin Tong
 * @version $Id$
 */
public class Dimension
extends GIPSYType
{
	/**
	 * For serialization versioning. 
	 */
	private static final long serialVersionUID = -7144129044748902741L;
	
	/**
	 * Dimension identifier in the dimension simple node.
	 */
	protected GIPSYIdentifier oDimensionName;
	
	/**
	 * TagSet node in Dimension, which is like {red, blue}.
	 */
	protected TagSet oDimensionTags = null;
	
	/**
	 * The current oCurrentTag in the former context element.
	 */
	protected GIPSYType oCurrentTag = null;
	
	/**
	 * 
	 */
	public Dimension()
	{
		this.oDimensionName = null;
		this.oDimensionTags = null;
		this.oCurrentTag = null;
	}
	
	/**
	 * @param poDimensionName
	 * @param poDimensionTags
	 */
	public Dimension(GIPSYIdentifier poDimensionName, TagSet poDimensionTags)
	{
		this.oDimensionName = poDimensionName;
		this.oDimensionTags = poDimensionTags;
	}
	
	/**
	 * @param poDimension
	 */
	public Dimension(Dimension poDimension)
	{
		this.oDimensionName = poDimension.oDimensionName;
		this.oDimensionTags = poDimension.oDimensionTags;
	}
	
	/**
	 * @param poDimensionName
	 */
	public void setDimensionName(GIPSYIdentifier poDimensionName)
	{
		this.oDimensionName = poDimensionName;
	}
	
	/**
	 * oDimensionTags could be ordered or unordered. 
	 * @param poDimensionTags
	 */
	public void setDimensionTags(TagSet poDimensionTags)
	{
		this.oDimensionTags = poDimensionTags;
	}
	
	/**
	 * @return
	 */
	public GIPSYIdentifier getDimensionName()
	{
		return this.oDimensionName;
	}
	
	/**
	 * @return
	 */
	public TagSet getDimensionTags()
	{
		return this.oDimensionTags;
	}
	
	/**
	 * @param poTag
	 */
	public void setCurrentTag(GIPSYType poTag)
	{	
		this.oCurrentTag = poTag;
	}

	/**
	 * @return
	 */
	public GIPSYType getCurrentTag()
	{
	 	return this.oCurrentTag;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object poOtherObject)
	{
		if(this.getClass() != poOtherObject.getClass())
		{
			return false;
		}
		else
		{
			Dimension oDimension = (Dimension)poOtherObject;

			if(isNull() && oDimension.isNull())
			{
				return true;
			}
			
			return
			(
				this.oDimensionName.equals(oDimension.oDimensionName)
				&& this.oDimensionTags.equals(oDimension.oDimensionTags)
				&&
				(
					this.oCurrentTag != null && oDimension.oCurrentTag != null
						? this.oCurrentTag.equals(oDimension.oCurrentTag)
						: true
				)
			);

		}
	}

	/**
	 * @return true if all members are null
	 */
	public boolean isNull()
	{
		return
			this.oCurrentTag == null
			&& this.oDimensionName == null
			&& this.oDimensionTags == null;
	}
	
	/**
	 * @return tag set
	 * @see gipsy.lang.GIPSYType#getEnclosedTypeOject()
	 */
	public Object getEnclosedTypeOject()
	{
		return this.oDimensionTags;
	}

	/**
	 * @return the name of the dimension
	 */
	public String toString()
	{
		return this.oDimensionName.getValue();
	}
}

// EOF
