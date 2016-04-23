package gipsy.storage;

import gipsy.GIPC.intensional.SimpleNode;
import gipsy.lang.GIPSYType;

import java.io.Serializable;
import java.util.Hashtable;


/**
 * <p>Dictionary Item.</p>
 * 
 * <p>Represents an element of a dictionary in the dictionary collection.
 * (Formerly known as <code>Item_in_Dict.java</code>).</p>
 * 
 * $Id: DictionaryItem.java,v 1.11 2011/01/08 00:21:43 mokhov Exp $
 * 
 * @author Aihua Wu
 * @author Serguei Mokhov
 *
 * @version $Revision: 1.11 $
 * @since 1.0.0; June 1, 2002, 2:34 PM
 */
public class DictionaryItem 
implements Serializable
{
	/*
	 * This enumeration seems to be obsolete, but keep it
	 * for now as reference:
	 * 
	 * 	public static final int TYPE_INT     = 0;
		public static final int TYPE_DOUBLE  = 1;
		public static final int TYPE_STRING  = 2;
		public static final int TYPE_BOOLEAN = 3;*/

	/**
	 * ID for serialization.
	 */
	private static final long serialVersionUID = 1069638323298660564L;

	/**
	 * Index.
	 */
	protected int iID;
	
	/**
	 * Name of the entries.
	 */
	protected String strName;
	
	/**
	 * ?Dimension or Variable.
	 */
	protected String strKind;
	
	/**
	 * GIPSYInteger, GIPSTFloat, etc.
	 * Formerly was protected int iType; //0-int, 1-float, 2-string, 3-boolean.
	 * @see gipsy.lang
	 */
	protected GIPSYType oType; 

	/**
	 * How many dimensions a variable varies.
	 * A constant has a rank of 0.
	 */
	protected String strRank;
	
	/**
	 * Reference to the node in the tree. 
	 */
	protected SimpleNode oEntry;
	
	/**
	 * Point to the father table. 
	 */
	protected DictionaryItem oPrevious;

	protected Hashtable oHashtable;
	
	/** 
	 * Creates new DictionaryItem.
	 */
	public DictionaryItem() 
	{
	    this(-1, "", "", -1, "", null, null, false);
	}
	
	public DictionaryItem
	(
		int piID, 
		String pstrName, 
		String pstrKind, 
		int piType, 
		String pstrRank, 
		SimpleNode poEntry,
	    DictionaryItem poPrevious, 
	    boolean pbChild
	) 
	{
		this.iID = piID;
		this.strName = pstrName;
		this.strKind = pstrKind;
		this.strRank = pstrRank;
		this.oEntry = poEntry;
		this.oPrevious = poPrevious;
		setType(piType);

		if(pbChild)
		{
			this.oHashtable = new Hashtable();
		}
	}
	
	public int getID()
	{
		return this.iID;
	}
	
	public void setID(int piID)
	{
		this.iID = piID;
	}
	
	public String getName()
	{
		return this.strName;
	}
	
	public void setName(String pstrName)
	{
		this.strName = pstrName;
	}
	
	public String getKind()
	{
		return this.strKind;
	}
	
	public void setKind(String pstrKind)
	{
		this.strKind = pstrKind;
	}

	public GIPSYType getType()
	{
		return this.oType;
	}
	
	public int getTypeEnumeration()
	{
		return this.oType.getTypeEnumeration();
	}

	public void setType(int piType)
	{
		this.oType = GIPSYType.getType(piType);
	}
	
	public void setType(GIPSYType poType)
	{
		this.oType = poType;
	}

	public String getRank()
	{
		return this.strRank;
	}
	
	public void setRank(String pstrRank)
	{
		this.strRank = pstrRank;
	}
	
	public SimpleNode getEntry()
	{
		return this.oEntry;
	}
	
	public void setEntry(SimpleNode poEntry)
	{
		this.oEntry = poEntry;
	}
	
	public DictionaryItem getPrevious()
	{
		return this.oPrevious;
	}
	
	public void setPrevious(DictionaryItem poPrevious)
	{
		this.oPrevious = poPrevious;
	}
	
	public Hashtable getHashtable()
	{
		return this.oHashtable;
	}
	
	public void setHashtable(Hashtable poHashtable)
	{
		this.oHashtable = poHashtable;
	}
	
	public String toString()
	{
		return
			this.iID + " __ "
			+ this.strName + " __ "
			+ this.strKind + " __ "
			+ getType() + " __ "
			+ this.strRank + " __ "
			+ this.oEntry;
	}
}

// EOF
