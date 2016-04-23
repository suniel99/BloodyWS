/**
 * SimGraphMousePlugin.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 *  
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: SimGraphMousePlugin.java,v 1.3 2011/08/22 03:39:01 s_rabah Exp $
 */
package gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.Serializable;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.NodeConnection;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.GIPSYGMTOperator;

/**
 * This class creates a Jung library mouse plug-in used to handle events when
 * clicking on a node.
 * 
 * @author Sleiman Rabah
 * 
 * @version $Revision: 1.3 $ $Date: 2011/08/22 03:39:01 $
 */
public class SimGraphMousePlugin implements GraphMouseListener<GIPSYTier>,
        Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = -7091179221192042503L;

    /**
     * Default class constructor.
     */
    public SimGraphMousePlugin()
    {
    }

    /**
     * This method handle the on click event when the mouse is clicked on any
     * node <br>
     * of the simultation graph.
     * 
     * @param geoState
     *            a GeoState reference representing a node on the simulation
     *            graph.
     * @param e
     *            Mouse event object.
     */
    public void graphClicked(GIPSYTier geoState, MouseEvent e)
    {

        try
        {

            final VisualizationViewer<GIPSYTier, NodeConnection> vv = (VisualizationViewer<GIPSYTier, NodeConnection>) e
                    .getSource();
            Point2D p = e.getPoint();// graphVisualizer.getRenderContext().getBasicTransformer().inverseViewTransform(e.getPoint());

            GraphElementAccessor<GIPSYTier, NodeConnection> pickSupport = vv
                    .getPickSupport();

            if (pickSupport != null)
            {
                final GIPSYTier v = pickSupport.getVertex(vv.getGraphLayout(),
                        p.getX(), p.getY());
                if (v != null)
                {
                    GIPSYGMTOperator.getInstance().updateStateProperties(v);
                }
            }
        }
        catch (Exception ex)
        {
            System.err
                    .println("GraphPanel.java: A probelm has occurred while executing mouse clicked event on the graph view.");
            ex.getStackTrace();
        }

    }

    public void graphPressed(GIPSYTier arg0, MouseEvent arg1)
    {

    }

    public void graphReleased(GIPSYTier arg0, MouseEvent arg1)
    {

    }
}