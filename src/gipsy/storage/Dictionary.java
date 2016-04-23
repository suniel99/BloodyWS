package gipsy.storage;

import gipsy.lang.GIPSYIdentifier;

import java.util.Collection;

import marf.util.Arrays;
import marf.util.FreeVector;


/**
 * <p>Encapsulation of dictionary operations.</p>
 * 
 * <p>By extending from FreeVector can be potentially infinite on read,
 * i.e. it is possible to try to access items in the dictionary beyond
 * the current length/capacity of the dictionary (where those items'
 * values will obviously correspond to nulls).
 * </p>
 *
 * <p>The class adheres more-or-less to Java Collections's API (as the
 * Vector class is one of the parents in the hierarchy) so all those
 * methods are accessible to clients. As such, it is also properly
 * synchronized.</p>
 *
 * @author Serguei Mokhov
 * @version $Id: Dictionary.java,v 1.12 2013/01/09 14:50:39 mokhov Exp $
 * @since 1.0.0
 */
public class Dictionary
// XXX: should be, but a can of worms
//extends FreeVector<DictionaryItem>
extends FreeVector<Object>
{
	/**
	 * For serialization versionning.
	 */
	private static final long serialVersionUID = -3121971849816611083L;

	/**
	 * Current dictionary item. 
	 */
	protected DictionaryItem oDictionaryItem;

	/**
	 * A reference to the current item (in case of linked dictionary items).
	 */
	protected DictionaryItem oCurrentDictionaryItem = null;

	/**
	 * A reference to the predecessor item (in case of linked dictionary items).
	 */
	protected DictionaryItem oPreviousDictionaryItem = null;

	/**
	 * Default constructor just constructs an empty dictionary. 
	 */
	public Dictionary()
	{
		super();
	}

	/**
	 * Reserves location of dictionary items up to the
	 * specified capacity.
	 * This is an inherited constructor.
	 * @param piInitialCapacity the capacity
	 */
	public Dictionary(int piInitialCapacity)
	{
		super(piInitialCapacity);
	}

	/**
	 * Constructs this dictionary given capacity and its increment.
	 * This is an inherited constructor.
	 * @param piInitialCapacity the capacity
	 * @param piCapacityIncrement increment
	 */
	public Dictionary(int piInitialCapacity, int piCapacityIncrement)
	{
		super(piInitialCapacity, piCapacityIncrement);
	}

	/**
	 * Constructs this dictionary out of a collection.
	 * This is an inherited constructor.
	 * @param poCollection collection for the dictionary elements
	 */
	public Dictionary(Collection<DictionaryItem> poCollection)
	{
		super(poCollection);
	}
	
	/**
	 * Sorts the entries in the dictionary.
	 * @return sorted self (note, this is NOT copy)
	 */
	public synchronized Dictionary sort()
	{
		Arrays.sort(this.elementData);
		return this;
	}
	
	/**
	 * Verifies whether a given identifier's name is in dictionary.
	 * @param pstrCurrentID string ID of the identifier
	 * @return true if the ID is found
	 */
	public synchronized boolean isInDictionary(String pstrCurrentID)
	{
		boolean bResult = false;

		for(int i = 0; i < size(); i++)
		{
			if(pstrCurrentID.equals(((DictionaryItem)elementAt(i)).getName()))
			{
				bResult = true;
				break;
			}
		}

		return bResult;
	}

	/**
	 * Verifies whether a given identifier's numeric index/ID is in dictionary.
	 * @param piID integer ID of the identifier
	 * @return true if the ID is found
	 */
	public synchronized boolean isInDictionary(int piID)
	{
		boolean bResult = false;

		for(int i = 0; i < size(); i++)
		{
			if(piID == (((DictionaryItem)elementAt(i)).getID()))
			{
				bResult = true;
				break;
			}
		}

		return bResult;
	}

	/**
	 * Verifies whether a given item is in dictionary.
	 * @param poItem the item
	 * @return true if the item is found
	 */
	public synchronized boolean isInDictionary(DictionaryItem poItem)
	{
		boolean bResult = false;

		for(int i = 0; i < size(); i++)
		{
			if(poItem.equals(elementAt(i)))
			{
				bResult = true;
				break;
			}
		}

		return bResult;
	}

	/**
	 * Verifies whether a given identifier's name is in dictionary.
	 * @param poID GIPSYIdenitifier ID type
	 * @return true if the ID is found
	 */
	public synchronized boolean isInDictionary(GIPSYIdentifier poID)
	{
		boolean bResult = false;

		for(int i = 0; i < size(); i++)
		{
			if(poID.getID().equals(((DictionaryItem)elementAt(i)).getName()))
			{
				bResult = true;
				break;
			}
		}

		return bResult;
	}
	
	/**
	 * This method should ensure that if the DictionaryItem object under
	 * pstrName is inside the dictionary and also if it is inside the
	 * right scope.
	 *
	 * @param pstrName
	 * @param poCurrentScope
	 * @return
	 * @since Xin Tong
	 */
	public synchronized DictionaryItem getItem(String pstrName, DictionaryItem poCurrentScope)
	{
		DictionaryItem oResult = null;

		for(int i = 0; i < size(); i++)
		{
			DictionaryItem oCurrentItem = (DictionaryItem)this.elementAt(i);

			if
			(
				poCurrentScope.getHashtable().containsKey(oCurrentItem.getName())
				&& oCurrentItem.getName().equals(pstrName)
			)
			{
				oResult = oCurrentItem;
			}
		}
		
		return oResult;
	}
}

// EOF
