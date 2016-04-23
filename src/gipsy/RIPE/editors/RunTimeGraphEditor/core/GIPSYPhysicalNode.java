/**
 * GIPSYPhysicalNode.java
 *  
 * 
 * @version $Revision: 1.3 $ $Date: 2011/08/22 03:36:59 $
 * 
 * 
 * $Id: GIPSYPhysicalNode.java,v 1.3 2011/08/22 03:36:59 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.core;

import gipsy.GEE.multitier.GIPSYNode;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * A class which represents a GIPSY node hosted on computer.
 * 
 * @author Sleiman Rabah
 * 
 */
public class GIPSYPhysicalNode implements Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = -8281249427148414106L;
    private String strNodeID;
    private String strNodeKey;
    private String strNodeName;
    private String strIPAddress;
    private String strNodeColor;
    private boolean bIsStarted;
    private boolean bIsRegistred;
    private boolean bIsStopped;
    private GIPSYNode oGIPSYNode;
    private ArrayList<GIPSYPhysicalNode> neighborList;

    /**
     * Constructor
     * <p>
     * Initialize a Country object
     */
    public GIPSYPhysicalNode()
    {
        this.strNodeName = "";
        setNeighborList(new ArrayList<GIPSYPhysicalNode>());
        bIsStopped = false;
        bIsRegistred = false;
        bIsStopped = false;
        oGIPSYNode = null;
    }

    /**
     * Over-written method of toString() for this class
     * 
     */
    public String toString()
    {
        return this.strNodeName;
    }

    /**
     * @param strNodeID
     *            the strNodeID to set
     */
    public void setNodeID(String strNodeID)
    {
        this.strNodeID = strNodeID;
    }

    /**
     * @return the strNodeID
     */
    public String getNodeID()
    {
        return strNodeID;
    }

    /**
     * @param strIPAddress
     *            the strIPAddress to set
     */
    public void setIPAddress(String strIPAddress)
    {
        this.strIPAddress = strIPAddress;
    }

    /**
     * @return the strIPAddress
     */
    public String getIPAddress()
    {
        return strIPAddress;
    }

    /**
     * @param neighborList
     *            the neighborList to set
     */
    public void setNeighborList(ArrayList<GIPSYPhysicalNode> neighborList)
    {
        this.neighborList = neighborList;
    }

    /**
     * @return the neighborList
     */
    public ArrayList<GIPSYPhysicalNode> getNeighborList()
    {
        return neighborList;
    }

    /**
     * @param strNodeColor
     *            the strNodeColor to set
     */
    public void setNodeColor(String strNodeColor)
    {
        this.strNodeColor = strNodeColor;
    }

    /**
     * @return the strNodeColor
     */
    public String getNodeColor()
    {
        return strNodeColor;
    }

    public String getNodeName()
    {
        return strNodeName;
    }

    public void setNodeName(String strNodeName)
    {
        this.strNodeName = strNodeName;
    }

    /**
     * @param strNodeKey
     *            the strNodeKey to set
     */
    public void setNodeKey(String strNodeKey)
    {
        this.strNodeKey = strNodeKey;
    }

    /**
     * @return the strNodeKey
     */
    public String getNodeKey()
    {
        return strNodeKey;
    }

    /**
     * @param bIsStarted the bIsStarted to set
     */
    public void setIsStarted(boolean bIsStarted)
    {
        this.bIsStarted = bIsStarted;
    }

    /**
     * @return the bIsStarted
     */
    public boolean isStarted()
    {
        return bIsStarted;
    }

    /**
     * @param bIsRegistred the bIsRegistred to set
     */
    public void setIsRegistred(boolean bIsRegistred)
    {
        this.bIsRegistred = bIsRegistred;
    }

    /**
     * @return the bIsRegistred
     */
    public boolean isRegistred()
    {
        return bIsRegistred;
    }

    /**
     * @param bIsStopped the bIsStopped to set
     */
    public void setIsStopped(boolean bIsStopped)
    {
        this.bIsStopped = bIsStopped;
    }

    /**
     * @return the bIsStopped
     */
    public boolean isStopped()
    {
        return bIsStopped;
    }

    /**
     * @param oGIPSYNode the oGIPSYNode to set
     */
    public void setGIPSYNode(GIPSYNode oGIPSYNode)
    {
        this.oGIPSYNode = oGIPSYNode;
    }

    /**
     * @return the oGIPSYNode
     */
    public GIPSYNode getGIPSYNode()
    {
        return oGIPSYNode;
    }
}
