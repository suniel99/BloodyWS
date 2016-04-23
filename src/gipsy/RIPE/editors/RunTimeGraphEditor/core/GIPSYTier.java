/**
 * GIPSYTier.java
 * 
 * 
 * @version $Revision: 1.4 $ $Date: 2011/08/22 03:36:59 $
 * 
 * 
 * $Id: GIPSYTier.java,v 1.4 2011/08/22 03:36:59 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.core;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class representing a geographic state.
 * 
 * @author Sleiman Rabah
 */
public class GIPSYTier implements Serializable, Cloneable
{

    private static final long serialVersionUID = 958342996029065424L;

    private String strTierID;
    private String strTierName;
    private String strTierType;
    private String strHowManyTierPerNode;
    private String strGIPSYNodeID;
    private String strGIPSYInstanceID;
    private String strConfigFilePath;
    private boolean bIsGMT;
    private boolean bIsAllocated;
    private boolean bIsDeAllocated;
    private int iPosX;
    private int iPosY;

    private ArrayList<GIPSYTier> neighbors;

    public GIPSYTier()
    {
        this.setTierID("");
        strConfigFilePath = "";
        strTierName = "";
        strTierType = "";
        strGIPSYNodeID = "";
        strGIPSYInstanceID = "";
        strConfigFilePath = "";
        strHowManyTierPerNode = "";
        this.bIsGMT = false;
        this.iPosX = 0;
        this.iPosY = 0;
        this.neighbors = new ArrayList<GIPSYTier>();
        setGIPSYNodeID("");
        bIsAllocated = false;
        bIsDeAllocated = false;
    }

    public int getPosX()
    {
        return iPosX;
    }

    public int getPosY()
    {
        return iPosY;
    }

    public synchronized ArrayList<GIPSYTier> getNeighbors()
    {
        return this.neighbors;
    }

    public void setPosX(int posX)
    {
        this.iPosX = posX;
    }

    public void setPosY(int posY)
    {
        this.iPosY = posY;
    }

    /**
     * @param strNodeName
     *            the strNodeName to set
     */
    public void setTierName(String strNodeName)
    {
        this.strTierName = strNodeName;
    }

    /**
     * @return the strNodeName
     */
    public String getTierName()
    {
        return strTierName;
    }

    /**
     * @param bIdGMT
     *            the bIdGMT to set
     */
    public void setIsGMT(boolean bIdGMT)
    {
        this.bIsGMT = bIdGMT;
    }

    /**
     * @return the bIdGMT
     */
    public boolean isGMT()
    {
        return bIsGMT;
    }

    public String toString()
    {
        return this.strTierName;

    }

    /**
     * @param strTierID
     *            the strTierID to set
     */
    public void setTierID(String strTierID)
    {
        this.strTierID = strTierID;
    }

    /**
     * @return the strTierID
     */
    public String getTierID()
    {
        return strTierID;
    }

    /**
     * @param strTierType
     *            the strTierType to set
     */
    public void setTierType(String strTierType)
    {
        this.strTierType = strTierType;
    }

    /**
     * @param strHowManyTierPerNode
     *            the strHowManyTierPerNode to set
     */
    public void setHowManyTierPerNode(String strHowManyTierPerNode)
    {
        this.strHowManyTierPerNode = strHowManyTierPerNode;
    }

    /**
     * @return the strHowManyTierPerNode
     */
    public String getHowManyTierPerNode()
    {
        return strHowManyTierPerNode;
    }

    /**
     * @return the strTierType
     */
    public String getTierType()
    {
        return strTierType;
    }

    /**
     * @param strGIPSYNodeID
     *            the strGIPSYNodeID to set
     */
    public void setGIPSYNodeID(String strGIPSYNodeID)
    {
        this.strGIPSYNodeID = strGIPSYNodeID;
    }

    /**
     * @return the strGIPSYNodeID
     */
    public String getGIPSYNodeID()
    {
        return strGIPSYNodeID;
    }

    /**
     * @param strGIPSYInstanceID
     *            the strGIPSYInstanceID to set
     */
    public void setGIPSYInstanceID(String strGIPSYInstanceID)
    {
        this.strGIPSYInstanceID = strGIPSYInstanceID;
    }

    /**
     * @return the strGIPSYInstanceID
     */
    public String getGIPSYInstanceID()
    {
        return strGIPSYInstanceID;
    }

    /**
     * @param strConfigFilePath
     *            the strConfigFilePath to set
     */
    public void setConfigFilePath(String strConfigFilePath)
    {
        this.strConfigFilePath = strConfigFilePath;
    }

    /**
     * @return the strConfigFilePath
     */
    public String getConfigFilePath()
    {
        return strConfigFilePath;
    }

    /**
     * @param bIsAllocated the bIsAllocated to set
     */
    public void setIsAllocated(boolean bIsAllocated)
    {
        this.bIsAllocated = bIsAllocated;
    }

    /**
     * @return the bIsAllocated
     */
    public boolean isAllocated()
    {
        return bIsAllocated;
    }

    /**
     * @param bIsDeAllocated the bIsDeAllocated to set
     */
    public void setIsDeAllocated(boolean bIsDeAllocated)
    {
        this.bIsDeAllocated = bIsDeAllocated;
    }

    /**
     * @return the bIsDeAllocated
     */
    public boolean isDeAllocated()
    {
        return bIsDeAllocated;
    }
}
