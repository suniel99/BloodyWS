/**
 * Edge.java
 *  
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/08/18 04:17:33 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: NodeConnection.java,v 1.3 2011/08/18 04:17:33 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.core;

import java.io.Serializable;

import edu.uci.ics.jung.visualization.picking.PickedInfo;

/**
 * 
 * A class used to create an edge between two nodes. Each edge is identified by
 * a unique ID.
 * 
 * @author Sleiman Rabah
 * 
 */
public class NodeConnection implements Serializable, PickedInfo
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 4089722769728614723L;
    /**
     * The ID of a connection
     */
    private String edgeId;
    /**
     * The name of a connection
     */
    private String name;
    /**
     * The ID of a state which is at the "from" end of a connection
     */
    private String fromStateId;
    /**
     * The ID of a state which is at the "to" end of a connection
     */
    private String toStateId;

    /**
     * Constructor
     * <p>
     * Initialize the edge(StateConnection) object
     */
    public NodeConnection()
    {
        this.name = "";
        this.toStateId = "";
        this.fromStateId = "";
        this.edgeId = "";
    }

    /**
     * Getter method to obtain the id of the current edge
     * 
     * @return the id of edge
     */
    public String getEdgeId()
    {
        return edgeId;
    }

    /**
     * Getter method to obtain the id of start node
     * 
     * @return the id of start node
     */
    public String getFromTierId()
    {
        return fromStateId;
    }

    /**
     * Getter method to obtain the name of edge
     * 
     * @return the the name of edge
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Getter method to obtain the id of start node
     * 
     * @return the id of start node
     */
    public String getToTierId()
    {
        return this.toStateId;
    }

    /**
     * Setter method to specify an id for this edge
     * 
     * @param edgeId
     *            the edgeId to set
     */
    public void setEdgeId(String edgeId)
    {
        this.edgeId = edgeId;
    }

    /**
     * Setter method to specify an id for the start node
     * 
     * @param fromStateId
     *            - the id of start node
     */
    public void setFromTierId(String fromStateId)
    {
        this.fromStateId = fromStateId;
    }

    /**
     * Setter method to specify a name for the edge
     * 
     * @param name
     *            - the name of edge
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Setter method to specify an id for the end node
     * 
     * @param toStateId
     *            - the id of end node
     */
    public void setToTierId(String toStateId)
    {
        this.toStateId = toStateId;
    }

    /**
     * Over-written method of toString() for this class
     * 
     * @return the name of the current edge as a String
     */
    public String toString()
    {
        return name;
    }

    @Override
    public boolean isPicked(Object arg0)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
