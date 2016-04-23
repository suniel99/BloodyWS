/**
 * VertexColor.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 *  
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: VertexColor.java,v 1.2 2011/08/18 04:17:44 s_rabah Exp $
 */
package gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers;

import java.awt.Color;
import java.awt.Paint;
import java.io.Serializable;
import java.util.HashMap;
import org.apache.commons.collections15.Transformer;
import edu.uci.ics.jung.visualization.picking.PickedInfo;

/**
 * A class used to draw a colored graph node in the simulation view.
 * 
 * @author Sleiman Rabah
 * 
 * @version $Revision: 1.2 $ $Date: 2011/08/18 04:17:44 $
 */
public class VertexColor<GeoState> implements Transformer<GeoState, Paint>,
        Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 7729257417738252885L;
    /**
     * The list of nodes to change their colors.
     */
    private HashMap<GeoState, GeoState> seedVertices;
    /**
     * The list of association: node => color.
     */
    private HashMap<GeoState, Color> nodesColor;
    /**
     * Register which contains the graph node and their colors ans shapes.
     */
    private VerticesRegister verticesRegister;

    /**
     * Class constructor.
     * 
     * @param verticesRegister
     *            contains color and vertices data structure.
     */
    public VertexColor(VerticesRegister verticesRegister)
    {

        this.nodesColor = (HashMap<GeoState, Color>) verticesRegister
                .getNodesColor();
        this.seedVertices = (HashMap<GeoState, GeoState>) verticesRegister
                .getSeedVertices();
    }

    /**
     * Repaint a node using the color specified in the seedVertices HasMap.
     * 
     * @return a color object specifying the color of the node.
     */
    public Paint transform(GeoState v)
    {
        float alpha = 0.9f;

        if (seedVertices.containsKey(v))
        {
            return nodesColor.get(v);
        }
        else
            return new Color(1f, 0, 0, alpha);
    }
}