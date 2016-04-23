/**
 * NodeTextTips.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 *  
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: NodeTextTips.java,v 1.2 2011/08/18 04:17:44 s_rabah Exp $
 */
package gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers;

import org.apache.commons.collections15.Transformer;

/**
 * 
 * This class is used to create a pop-up text tip when the mouse has been placed
 * on a node.
 * 
 * @author Sleiman Rabah
 * 
 * @version $Revision: 1.2 $ $Date: 2011/08/18 04:17:44 $
 */
public class NodeTextTips<GeoState> implements Transformer<GeoState, String>
{

    /**
     * Register which contains the graph node and their colors ans shapes.
     */
    private VerticesRegister verticesRegister;

    /**
     * Class Constructor.
     * 
     * @param iVerticesRegister
     *            a register of graph nodes and their properties.
     */
    public NodeTextTips(VerticesRegister iVerticesRegister)
    {
        this.verticesRegister = iVerticesRegister;
    }

    /**
     * 
     * This method pops-up a tool tip text note displaying the node name.
     * 
     * @param geoState
     *            a reference which represents a graph node.
     * @throws Exception
     *             if the graph visualizer has not been created.
     */
    public String transform(GeoState geoState)
    {

        String toolTipText = "";
        try
        {

            toolTipText = "GIPSY Tier Name: "
                    + verticesRegister.getSeedVertices().get(geoState)
                            .getTierName();

        }
        catch (Exception e)
        {
            System.err
                    .println("- SimulationController.java: An error has occured while constructing the node text tool tips.");
            System.err.println(e.getStackTrace());
        }

        return toolTipText;
    }
}