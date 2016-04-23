package gipsy.GEE.IDP.demands;

import gipsy.GEE.GEEException;

/**
 * TODO: document.
 *
 * $Id: IDemandList.java,v 1.10 2009/08/12 01:57:58 mokhov Exp $
 * $Revision: 1.10 $
 * $Date: 2009/08/12 01:57:58 $
 *
 * @author Paula Bo Lu, refactored by Serguei Mokhov
 */
public interface IDemandList
{
	/**
	 * Adds demand to the collection.
	 * @param pstrDemand demand string to add
	 * @return integer - ?? TODO: find out
	 * @exception GEEException
	 */
	public int addDemand(String pstrDemand) throws GEEException;

	/**
	 * Removes demand from the collection.
	 * @param pstrDemand demand string to remove
	 * @exception GEEException
	 */
	public void removeDemand(String pstrDemand) throws GEEException;

	/**
	 * Provides status of the demand collection -- empty or not.
	 * @return true if the collection is empty; false otherwise.
	 * @exception GEEException
	 */
	public boolean isEmpty() throws GEEException;

	/**
	 * Gives amount of demands that are still active in the collection.
	 * @return number of active demands; an int
	 * @exception GEEException
	 */
	public int amountDemands() throws GEEException;

	/**
	 * Fetches next demand from the collection to be passed along.
	 * @return next demand string from the collection
	 * @exception GEEException
	 */
	public String getDemand() throws GEEException;
}

// EOF
