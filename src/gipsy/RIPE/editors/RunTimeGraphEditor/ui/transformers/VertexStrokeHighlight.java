/**
 * 
 */
package gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.HashMap;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;

/**
 * @author Sleiman Rabah
 * 
 */
public class VertexStrokeHighlight<V, E> implements Transformer<V, Stroke>
{

    protected Stroke heavy = new BasicStroke(12, BasicStroke.JOIN_BEVEL,
            BasicStroke.JOIN_ROUND);
    // protected Stroke heavy = new BasicStroke(25);
    protected Stroke medium = new BasicStroke(5);
    protected Stroke light = new BasicStroke(1);
    private HashMap<GIPSYTier, String> nodesToHighlight;
    //private HashMap<GIPSYTier, String> defenderToHighlight;

    public VertexStrokeHighlight(VerticesRegister verticesRegister)
    {

        this.nodesToHighlight = verticesRegister.getNodesToHighlight();
        //this.defenderToHighlight = verticesRegister.getDefenderToHighlight();
    }

    public Stroke transform(V v)
    {

        /*
         * if (pi.isPicked(v)) { return heavy; } else { for (V w :
         * graph.getNeighbors(v)) { // Vertex w = (Vertex)iter.next(); if
         * (pi.isPicked(w)) return medium; } return light; }
         */

//        if (defenderToHighlight.containsKey(v))
//        {
//            return medium;
//        }

        if (nodesToHighlight.containsKey(v))
        {
            return medium;
        }
        return light;
    }
}
