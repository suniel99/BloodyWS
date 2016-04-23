/**
 * DeleteEdgeMenuItem.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/08/22 03:38:57 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: DeleteEdgeMenuItem.java,v 1.3 2011/08/22 03:38:57 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.NodeConnection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * A class to implement the deletion of an edge from within a
 * PopupStateEdgeMenuMousePlugin. An edge in our case is a connection between
 * two states. An edge may contains some informations/properties.
 * 
 * @author Sleiman Rabah based on Dr. Greg M. Bernstein's code.
 */
public class DeleteEdgeMenuItem<E> extends JMenuItem implements
        EdgeMenuListener<E>
{

    /**
     * Represents a connection between two nodes.
     */
    private E edge;
    /**
     * The Graph Visualizer which displays nodes and connections.
     */
    private VisualizationViewer visComp;

    /**
     * Class Constructor.
     * <p>
     * Creates a customized pop-up menu for when right click on an edge.
     */
    public DeleteEdgeMenuItem()
    {
        super("Delete Connection");
        this.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete this conenction?",
                        "Confirm Delete connection", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                {
                    visComp.getPickedEdgeState().pick(edge, false);
                    visComp.getGraphLayout().getGraph().removeEdge(edge);
                    NodeConnection oNodeConnection = (NodeConnection) edge;
                    if (GlobalInstance.getInstance().isItemExists(oNodeConnection))
                    {
                        GlobalInstance.getInstance().remove(oNodeConnection);
                        GlobalInstance.getInstance().removeFromHashMap(
                                oNodeConnection.getEdgeId(), oNodeConnection);
                    }
                    visComp.repaint();
                }
            }
        });
    }

    /**
     * Implements the EdgeMenuListener interface to update the menu item with
     * info on the currently chosen edge.
     * 
     * @param edge
     *            a connection between two nodes.
     * @param visComp
     *            the Graph Visualizer which display graph nodes and edges.
     */
    public void setEdgeAndView(E edge, VisualizationViewer visComp)
    {
        this.edge = edge;
        this.visComp = visComp;
        this.setText("Delete connection: " + edge.toString());
    }

}
