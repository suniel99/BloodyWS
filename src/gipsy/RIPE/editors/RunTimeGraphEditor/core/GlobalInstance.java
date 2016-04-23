/**
 * GameMap.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.5 $ $Date: 2011/08/24 04:21:47 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 *  $Id: GlobalInstance.java,v 1.5 2011/08/24 04:21:47 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.core;

import gipsy.RIPE.editors.RunTimeGraphEditor.operator.GIPSYTiersController;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.MessageBoxWrapper;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.dialogs.GIPSYNodeDialog;

import java.io.Serializable;
import java.util.*;

/**
 * A class representing a game map which contains states, continents and country
 * info and the edges between states. The singleton pattern is applied.
 * 
 * @author Sleiman Rabah
 * 
 */
public class GlobalInstance implements Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 9150596053331365502L;

    /**
     * The game map object
     */
    private static GlobalInstance oSingleInstance;

    /**
     * The list of continent objects
     */
    private ArrayList<GIPSYInstance> oGIPSYInstanceList;
    /**
     * The hash map of GIPSY instances.
     */
    private HashMap<String, GIPSYInstance> oGipsyInstancesHash;
    /**
     * The list of physical GIPSY nodes.
     */
    private ArrayList<GIPSYPhysicalNode> oPhysicalNodesList;
    /**
     * The hash map of GIPSY nodes
     */
    private HashMap<String, GIPSYPhysicalNode> oGIPSYNodesHash;
    /**
     * The list of connection objects
     */
    private ArrayList<NodeConnection> oNodeConnectionsList;
    /**
     * The hash map of connection objects
     */
    private HashMap<String, NodeConnection> oNodeConnectionsHash;
    /**
     * The list of GIPSY tiers.
     */
    private ArrayList<GIPSYTier> oGIPSYTiersList;
    /**
     * The hash map of GIPSY tiers.
     */
    private HashMap<String, GIPSYTier> oGIPSYTiersHash;

    /**
     * The state of whether the game map is cleaned
     */
    private boolean cleanMap;

    /**
     * The suffix for generating names of continents
     */
    private int nNewInstanceSuffix;

    /**
     * The suffix for generating names of countries
     */
    private int nNewNodeSuffix;
    /**
     * The suffix for generating names of connections
     */
    private int nNewNodeConnectionSuffix;
    /**
     * The suffix for generating names of geostates
     */
    private int nNewTierSuffix;
    private List<GIPSYTiersController> aoStartedGMT;
    private boolean bIsOneGMTIsRunning;
    

    /**
     * Constructor
     * <p>
     * Initialize the GameMap, default values are mandatory, null value are not
     * acceptable. Because of applying the singleton pattern, the constructor is
     * at private access level.
     */
    private GlobalInstance()
    {
        oGIPSYInstanceList = new ArrayList<GIPSYInstance>();
        oGIPSYTiersList = new ArrayList<GIPSYTier>();
        oPhysicalNodesList = new ArrayList<GIPSYPhysicalNode>();
        oNodeConnectionsList = new ArrayList<NodeConnection>();
        oGipsyInstancesHash = new HashMap<String, GIPSYInstance>();
        oGIPSYNodesHash = new HashMap<String, GIPSYPhysicalNode>();
        oNodeConnectionsHash = new HashMap<String, NodeConnection>();
        oGIPSYTiersHash = new HashMap<String, GIPSYTier>();
        aoStartedGMT = new ArrayList<GIPSYTiersController>();
        cleanMap = false;
        setIsOneGMTIsRunning(false);
        this.nNewInstanceSuffix = 0;
        this.nNewNodeSuffix = 0;
        this.nNewNodeConnectionSuffix = 0;
        this.nNewTierSuffix = 0;
    }

    /**
     * Get the instance of game map. If it already exists, no additional
     * instance of it will be created; otherwise, a new instance will be created
     * here by its private constructor
     * 
     * @return the only one object of game map
     */
    public static GlobalInstance getInstance()
    {

        if (oSingleInstance == null)
            oSingleInstance = new GlobalInstance();

        return oSingleInstance;
    }

    /**
     * Add a continent object to the continent list
     * 
     * @param oGIPSYInstance
     *            - the continent object that is to be added to the list of
     *            continent
     * @return true or false indicating whether the operation has been done
     *         successfully or not.
     */
    public boolean addGIPSYInstance(GIPSYInstance oGIPSYInstance)
    {
        if ((this.oGIPSYInstanceList).add(oGIPSYInstance))
            return true;
        else
            return false;
    }

    /**
     * Add a physical node object to the physical nodes list
     * 
     * @param oGIPSYPhysicalNode
     *            - the country object that is to be added to the list of
     *            continent
     * @return true or false indicating whether the operation has been done
     *         successfully or not.
     */
    public boolean addGIPSYPhysicalNode(GIPSYPhysicalNode oGIPSYPhysicalNode)
    {
        if ((this.oPhysicalNodesList).add(oGIPSYPhysicalNode))
            return true;
        else
            return false;
    }

    /**
     * Add a connection between two tiers to the connections list.
     * 
     * @param oNodeConnection
     *            - the edge object that is to be added to the list of edge
     * @return true or false indicating whether the operation has been done
     *         successfully or not.
     */
    public boolean addNodeConnection(NodeConnection oNodeConnection)
    {
        if ((this.oNodeConnectionsList).add(oNodeConnection))
            return true;
        else
            return false;
    }

    /**
     * Add an geostate to the geostate list
     * 
     * @param oNewGIPSYTier
     *            - an object of state
     * @return true or false indicating whether the operation has been done
     *         successfully or not.
     */
    public boolean addGIPSYTier(GIPSYTier oNewGIPSYTier)
    {
        if ((this.oGIPSYTiersList).add(oNewGIPSYTier))
            return true;
        else
            return false;
    }

    /**
     * Add Continent,Country,GeoState,Edge objects to a unified hashmap data
     * container
     * 
     * @param id
     *            - the id of the object that is pushed into the hash map
     * @param obj
     *            - the object whose id is specified
     * @return true or false indicating whether the operation has been done
     *         successfully or not.
     */
    public boolean addToHashMap(String id, Object obj)
    {
        if (obj instanceof GIPSYPhysicalNode)
        {
            oGIPSYNodesHash.put(id, (GIPSYPhysicalNode) obj);
            return true;
        }
        if (obj instanceof GIPSYInstance)
        {
            oGipsyInstancesHash.put(id, (GIPSYInstance) obj);
            return true;
        }
        if (obj instanceof GIPSYTier)
        {
            oGIPSYTiersHash.put(id, (GIPSYTier) obj);
            return true;
        }
        if (obj instanceof NodeConnection)
        {
            oNodeConnectionsHash.put(id, (NodeConnection) obj);
            return true;
        }
        return false;
    }

    /**
     * Check out the status of existing for map elements in the corresponding
     * list
     * 
     * @param pItemToAdd
     *            - the object of map element such as continent, country, state,
     *            edge
     * @return true or false indicating whether the object exists or not.
     */
    public boolean isItemExists(Object pItemToAdd)
    {
        if ((pItemToAdd instanceof GIPSYPhysicalNode)
                && (this.oPhysicalNodesList.contains(pItemToAdd)))
            return true;
        if ((pItemToAdd instanceof GIPSYInstance)
                && (this.oGIPSYInstanceList.contains(pItemToAdd)))
            return true;
        if ((pItemToAdd instanceof GIPSYTier)
                && (this.oGIPSYTiersList.contains(pItemToAdd)))
            return true;
        if ((pItemToAdd instanceof NodeConnection)
                && (this.oNodeConnectionsList.contains(pItemToAdd)))
            return true;

        return false;
    }

    /**
     * Generate id for all types of map element. Prefix added to the id number
     * make the object id identical
     * 
     * @param obj
     *            - the object of map element such as continent, country, state,
     *            edge
     * @return id as a string
     */
    public String generateId(Object obj)
    {
        /*
         * String id = ""; if (obj instanceof GIPSYPhysicalNode) {
         * 
         * id = UUID.randomUUID().toString(); } if (obj instanceof
         * GIPSYInstance) { id = "gipsyinstance_" + (++nNewInstanceSuffix); }
         * 
         * if (obj instanceof GIPSYTier) { id = "tier_" + (++nNewTierSuffix); }
         * if (obj instanceof NodeConnection) { id = "edge_" +
         * (++nNewNodeConnectionSuffix); }
         */
        return UUID.randomUUID().toString();
    }

    /**
     * Fetch a continent object by its id
     * 
     * @param id
     *            - the target object whose id is hereby specified
     * @return the object of continent
     */
    public GIPSYInstance getGIPSYInstanceById(String iStrInstanceID)
    {

        for (GIPSYInstance oGIPSYInstance : oGIPSYInstanceList)
        {
            if (oGIPSYInstance.getInstanceID().equals(iStrInstanceID))
            {
                return oGIPSYInstance;
            }
        }

        return null;
    }

    /**
     * Fetch a continent object by its id
     * 
     * @param id
     *            - the target object whose id is hereby specified
     * @return the object of continent
     */
    public GIPSYInstance getGIPSYInstanceByName(String pstrInstanceID)
    {

        for (GIPSYInstance oGIPSYInstance : oGIPSYInstanceList)
        {
            if (oGIPSYInstance.getInstanceName().equals(pstrInstanceID))
            {
                return oGIPSYInstance;
            }
        }

        return null;
    }

    public GIPSYPhysicalNode getGIPSYNodeById(String pstrNodeID)
    {
        for (GIPSYPhysicalNode oGIPSYNode : oPhysicalNodesList)
        {
            if (oGIPSYNode.getNodeID().equals(pstrNodeID))
            {
                return oGIPSYNode;
            }
        }
        return null;
    }

    public GIPSYPhysicalNode getGIPSYNodeByName(String pstrNodeName)
    {
        for (GIPSYPhysicalNode oGIPSYNode : oPhysicalNodesList)
        {
            if (oGIPSYNode.getNodeName().equalsIgnoreCase(pstrNodeName))
            {
                return oGIPSYNode;
            }
        }
        return null;
    }

    /**
     * Fetch the object of geostate by the specified id
     * 
     * @param iStrTierID
     *            state id
     * @return the object of geostate by the specified id
     */
    public GIPSYTier getGIPSYTierByID(String iStrTierID)
    {

        for (GIPSYTier oGIPSYTier : oGIPSYTiersList)
        {
            if (oGIPSYTier.getTierID().equals(iStrTierID))
            {
                return oGIPSYTier;
            }
        }

        return null;
    }

    /**
     * Fetch the sublist of geostate by a specified country id
     * 
     * @param id
     *            - country id which is for selecting
     * @return the sublist of geostate
     */
    public ArrayList<GIPSYTier> getGIPSYTiersByInstanceID(String iStrInstanceID)
    {

        ArrayList<GIPSYTier> oTiersList = new ArrayList<GIPSYTier>();

        for (GIPSYTier gipsyTier : oGIPSYTiersList)
        {
            if (gipsyTier.getGIPSYInstanceID().equals(iStrInstanceID))
            {
                oTiersList.add(gipsyTier);
            }
        }

        return oTiersList;
    }

    /**
     * Fetch the sublist of geostate by a specified country id
     * 
     * @param id
     *            - country id which is for selecting
     * @return the sublist of geostate
     */
    public ArrayList<GIPSYTier> getGIPSYTiersByNodeID(String iStrNodeID)
    {

        ArrayList<GIPSYTier> geoStatesByCountryId = new ArrayList<GIPSYTier>();

        for (GIPSYTier gipsyTier : oGIPSYTiersList)
        {
            if (gipsyTier.getGIPSYNodeID().equals(iStrNodeID))
            {
                geoStatesByCountryId.add(gipsyTier);
            }
        }
        return geoStatesByCountryId;
    }

    /**
     * Fetch the status of map
     * 
     * @return <p>
     *         true - it is a clean map
     *         <p>
     *         false - it is not a clean map
     */
    public boolean isCleanMap()
    {
        return cleanMap;
    }

    /**
     * Remove the element from the corresponding list
     * 
     * @param item
     *            - the item to be removed
     */
    public void remove(Object item)
    {
        if (item instanceof GIPSYPhysicalNode)
            this.oPhysicalNodesList.remove(item);
        else if (item instanceof GIPSYInstance)
            this.oGIPSYInstanceList.remove(item);
        else if (item instanceof GIPSYTier)
            this.oGIPSYTiersList.remove(item);
        else if (item instanceof NodeConnection)
            this.oNodeConnectionsList.remove(item);
    }

    /**
     * Remove the element from the hash map by its id
     * 
     * @param id
     *            - the id of the element that stored in the hash map
     * @param obj
     *            - the object to be removed by its id
     * @return true or false indicating whether the object exists or not.
     */
    public boolean removeFromHashMap(String id, Object obj)
    {
        if (obj instanceof GIPSYPhysicalNode)
        {
            oGIPSYNodesHash.remove(id);
            return true;
        }
        if (obj instanceof GIPSYInstance)
        {
            oGipsyInstancesHash.remove(id);
            return true;
        }
        if (obj instanceof GIPSYTier)
        {
            oGIPSYTiersHash.remove(id);
            return true;
        }
        if (obj instanceof NodeConnection)
        {
            oNodeConnectionsHash.remove(id);
            return true;
        }
        return false;
    }

    /**
     * Clear the properties of game object
     * 
     */
    public void reset()
    {
        if (cleanMap)
        {
            this.nNewInstanceSuffix = 0;
            this.nNewNodeSuffix = 0;
            this.nNewNodeConnectionSuffix = 0;
            this.nNewTierSuffix = 0;
            this.oGIPSYInstanceList.clear();
            this.oPhysicalNodesList.clear();
            this.oGIPSYTiersList.clear();
            this.oNodeConnectionsList.clear();
            this.oNodeConnectionsHash.clear();
            this.oGIPSYTiersHash.clear();
            this.oGIPSYNodesHash.clear();
        }
    }

    /**
     * Set the status of the game map
     * 
     * @param cleanMap
     */
    public void setCleanMap(boolean cleanMap)
    {
        this.cleanMap = cleanMap;
    }

    /**
     * Initialize counter for ids
     * 
     */
    public void initializePrefixCounter()
    {
        nNewInstanceSuffix = this.oGIPSYInstanceList.size();
        nNewNodeSuffix = this.oPhysicalNodesList.size();
        nNewNodeConnectionSuffix = this.oNodeConnectionsList.size();
        nNewTierSuffix = this.oGIPSYTiersList.size();
    }

    public GIPSYTier getGIPSYTierByName(String pstrTierName)
    {
        for (GIPSYTier oTier : this.oGIPSYTiersList)
        {
            if (oTier.getTierName().equalsIgnoreCase(pstrTierName))
            {
                return oTier;
            }
        }

        return null;
    }


    public ArrayList<GIPSYPhysicalNode> getPhysicalNodesList()
    {
        return oPhysicalNodesList;
    }

    public ArrayList<GIPSYTier> getGIPSYTiersList()
    {
        return oGIPSYTiersList;
    }

    /**
     * Fetch the list of continent
     * 
     * @return the list of continent
     */
    public ArrayList<GIPSYInstance> getGIPSYInstancesList()
    {
        return oGIPSYInstanceList;
    }

    /**
     * Fetch the list of country
     * 
     * @return the array list of country
     */
    public ArrayList<GIPSYPhysicalNode> getGIPSYNodesList()
    {
        return oPhysicalNodesList;
    }

    /**
     * Fetch the list of edge
     * 
     * @return the array list of edge
     */
    public ArrayList<NodeConnection> getNodesConnectionList()
    {
        return oNodeConnectionsList;
    }

    public void setInstanceToNull(Object pObject)
    {
        oSingleInstance = null;
    }

    public boolean isLoaded()
    {
        return this.oPhysicalNodesList.size() > 0 || this.oGIPSYTiersList.size() > 0;
    }

    public void closeRunningGMTs()
    {
        for (GIPSYTiersController oRunningGMT: this.aoStartedGMT)
        {            
            System.out.println("Closing GMT...");
            oRunningGMT.stopGMT();
        }
    }

    public void addNewRunningGMT(GIPSYTiersController poGIPSYGMTTiersController)
    {        
        if(poGIPSYGMTTiersController != null)
        {
            this.aoStartedGMT.add(poGIPSYGMTTiersController);
        }        
    }

    /**
     * @param bIsOneGMTIsRunning the bIsOneGMTIsRunning to set
     */
    public void setIsOneGMTIsRunning(boolean bIsOneGMTIsRunning)
    {
        this.bIsOneGMTIsRunning = bIsOneGMTIsRunning;
    }

    /**
     * @return the bIsOneGMTIsRunning
     */
    public boolean isOneGMTIsRunning()
    {
        return bIsOneGMTIsRunning;
    }
}
