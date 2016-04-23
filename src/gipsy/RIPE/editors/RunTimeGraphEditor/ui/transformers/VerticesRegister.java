/**
 * VerticesRegister.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 *  
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: VerticesRegister.java,v 1.3 2012/06/20 21:36:25 s_rabah Exp $
 */
package gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers;

import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;

/**
 * A class representing a register which contains special node drawing
 * information. This information found in color, shape, and continent shape data
 * structures.
 * 
 * @author Sleiman Rabah
 * 
 * @version $Revision: 1.3 $ $Date: 2012/06/20 21:36:25 $
 */
public class VerticesRegister implements Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 9179887363180002532L;
    /**
     * List of nodes to change their color where the key is a GeoState object.
     */
    private HashMap<GIPSYTier, GIPSYTier> seedVertices;
    /**
     * List of node => color where the key is a GeoState object.
     */
    private HashMap<GIPSYTier, Color> nodesColor;
    /**
     * List of node => shape where the key is a GeoState object.
     */
    private static HashMap<GIPSYTier, String> nodesShape;
    private HashMap<GIPSYTier, String> nodesToHighlight;
    //private HashMap<GIPSYTier, String> defenderToHighlight;
    /**
     * list of continent => shape where the key is a string representing a
     * continent.
     */
    private HashMap<String, String> continentShape;

    /**
     * Class constructor. Create the register data member.
     */
    public VerticesRegister()
    {

        seedVertices = new HashMap<GIPSYTier, GIPSYTier>();
        nodesColor = new HashMap<GIPSYTier, Color>();
        nodesShape = new HashMap<GIPSYTier, String>();
        nodesToHighlight = new HashMap<GIPSYTier, String>();
        //setDefenderToHighlight(new HashMap<GIPSYTier, String>());
        continentShape = new HashMap<String, String>();
    }

    /**
     * Gets the node => geoState list.
     * 
     * @return the seedVertices a node => geoState list.
     */
    public HashMap<GIPSYTier, GIPSYTier> getSeedVertices()
    {
        return seedVertices;
    }

    /**
     * Sets the node => color list.
     * 
     * @param seedVertices
     *            the seedVertices to set
     */
    public void setSeedVertices(HashMap<GIPSYTier, GIPSYTier> seedVertices)
    {
        this.seedVertices = seedVertices;
    }

    /**
     * Gets the node => color list.
     * 
     * @return the nodesColor a node => color list.
     */
    public HashMap<GIPSYTier, Color> getNodesColor()
    {
        return nodesColor;
    }

    /**
     * Sets the list of node => Color.
     * 
     * @param nodesColor
     *            the nodesColor to set
     */
    public void setNodesColor(HashMap<GIPSYTier, Color> nodesColor)
    {
        this.nodesColor = nodesColor;
    }

    /**
     * Gets the list of node => shape.
     * 
     * @return the nodesShape a list of node => shape.
     */
    public static HashMap<GIPSYTier, String> getNodesShape()
    {
        return nodesShape;
    }

    /**
     * Sets the list of nodes => shape. z
     * 
     * @param nodesShape
     *            the nodesShape to set.
     */
    public static void setNodesShape(HashMap<GIPSYTier, String> nodesShape)
    {
        VerticesRegister.nodesShape = nodesShape;
    }

    /**
     * Gets the continent shape list.
     * 
     * @return continentShape the list of the continent shape.
     */
    public HashMap<String, String> getInstanceShape()
    {
        return continentShape;
    }

    /**
     * Sets the continent shape list.
     * 
     * @param continentShape
     *            the continentShape to set.
     */
    public void setContinentShape(HashMap<String, String> continentShape)
    {
        this.continentShape = continentShape;
    }

    public HashMap<GIPSYTier, String> getNodesToHighlight()
    {
        return this.nodesToHighlight;
    }

    /**
     * @param defenderToHighlight
     *            the defenderToHighlight to set
     */
    /*public void setDefenderToHighlight(
            HashMap<GIPSYTier, String> defenderToHighlight)
    {
        this.defenderToHighlight = defenderToHighlight;
    }*/

    /**
     * @return the defenderToHighlight
     */
    /*public HashMap<GIPSYTier, String> getDefenderToHighlight()
    {
        return defenderToHighlight;
    }*/

}
