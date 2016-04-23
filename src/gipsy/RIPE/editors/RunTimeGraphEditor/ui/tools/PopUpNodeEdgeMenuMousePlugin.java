/**
 * PopupStateEdgeMenuMousePlugin.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/08/22 03:38:57 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: PopUpNodeEdgeMenuMousePlugin.java,v 1.3 2011/08/22 03:38:57 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.JPopupMenu;

/**
 * A GraphMousePlugin that brings up distinct popup menus when an edge or vertex
 * is appropriately clicked in a graph. If these menus contain components that
 * implement either the EdgeMenuListener or StateMenuListener then the
 * corresponding interface methods will be called prior to the display of the
 * menus (so that they can display context sensitive information for the edge or
 * vertex).
 * 
 * @author Sleiman Rabah based on Dr. Greg M. Bernstein's code.
 */
public class PopUpNodeEdgeMenuMousePlugin<V, E> extends
        AbstractPopupGraphMousePlugin
{
    private JPopupMenu oEdgePopup, oVertexPopup;

    /**
     * Class Constructor. Creates a new instance of
     * PopupStateEdgeMenuMousePlugin
     */
    public PopUpNodeEdgeMenuMousePlugin()
    {
        this(MouseEvent.BUTTON3_MASK);
    }

    /**
     * Creates a new instance of PopupStateEdgeMenuMousePlugin
     * 
     * @param modifiers
     *            mouse event modifiers see the jung visualization Event class.
     */
    public PopUpNodeEdgeMenuMousePlugin(int modifiers)
    {
        super(modifiers);
    }

    /**
     * Getter for the edge pop-up.
     * 
     * @return edgePopup which is a customized pop-up menu.
     */
    public JPopupMenu getEdgePopup()
    {
        return oEdgePopup;
    }

    /**
     * Getter for the vertex pop-up.
     * 
     * @return a popup menu for vertex
     */
    public JPopupMenu getVertexPopup()
    {
        return oVertexPopup;
    }

    /**
     * Implementation of the AbstractPopupGraphMousePlugin method. This is where
     * the work gets done. You shouldn't have to modify unless you really want
     * to...
     * 
     * @param e
     *            the mouse event object.
     */
    @SuppressWarnings("unchecked")
    protected void handlePopup(MouseEvent e)
    {
        final VisualizationViewer<V, E> graphVisualizer = (VisualizationViewer<V, E>) e
                .getSource();
        Point2D p = e.getPoint();

        GraphElementAccessor<V, E> pickSupport = graphVisualizer
                .getPickSupport();
        if (pickSupport != null)
        {
            final V v = pickSupport.getVertex(graphVisualizer.getGraphLayout(),
                    p.getX(), p.getY());
            if (v != null)
            {
                updateVertexMenu(v, graphVisualizer, p);
                oVertexPopup.show(graphVisualizer, e.getX(), e.getY());
            }
            else
            {
                final E edge = pickSupport.getEdge(
                        graphVisualizer.getGraphLayout(), p.getX(), p.getY());
                if (edge != null)
                {
                    updateEdgeMenu(edge, graphVisualizer, p);
                    oEdgePopup.show(graphVisualizer, e.getX(), e.getY());

                }
            }
        }
    }

    /**
     * Setter for the Edge popup.
     * 
     * @param edgePopup
     *            the customized edge pop-up menu.
     */
    public void setEdgePopup(JPopupMenu edgePopup)
    {
        this.oEdgePopup = edgePopup;
    }

    /**
     * Setter for the vertex popup.
     * 
     * @param vertexPopup
     *            a customized node pop-up menu.
     */
    public void setVertexPopup(JPopupMenu vertexPopup)
    {
        this.oVertexPopup = vertexPopup;
    }

    /**
     * Update the edge menu when the graph visualizer is repainted.
     * 
     * @param edge
     *            an object representing a connection between two nodes.
     * @param graphVisualizer
     *            a reference to the graph visualizer.
     * @param point
     *            an object which tells where to pop-up the menu.
     */
    private void updateEdgeMenu(E edge, VisualizationViewer vv, Point2D point)
    {
        if (oEdgePopup == null)
            return;
        Component[] menuComps = oEdgePopup.getComponents();
        for (Component comp : menuComps)
        {
            if (comp instanceof EdgeMenuListener)
            {
                ((EdgeMenuListener) comp).setEdgeAndView(edge, vv);
            }
            if (comp instanceof MenuPointListener)
            {
                ((MenuPointListener) comp).setPoint(point);
            }
        }
    }

    /**
     * Update the vertex menu when the graph visualizer is repainted.
     * 
     * @param vertex
     *            an object representing a node.
     * @param graphVisualizer
     *            a reference to the graph visualizer.
     * @param point
     *            an object which tells where to pop-up the menu.
     */
    private void updateVertexMenu(V vertex, VisualizationViewer vv,
            Point2D point)
    {
        if (oVertexPopup == null)
            return;
        Component[] menuComps = oVertexPopup.getComponents();
        for (Component comp : menuComps)
        {
            if (comp instanceof GIPSYTierMenuListener)
            {
                ((GIPSYTierMenuListener) comp).setGIPSYTierAndView(vertex, vv);
            }
            if (comp instanceof MenuPointListener)
            {
                ((MenuPointListener) comp).setPoint(point);
            }
        }
    }
}
