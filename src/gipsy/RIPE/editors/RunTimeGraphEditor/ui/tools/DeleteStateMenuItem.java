/**
 * DeleteStateMenuItem.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/08/22 03:38:57 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: DeleteStateMenuItem.java,v 1.3 2011/08/22 03:38:57 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * A class to implement the deletion of a vertex from within a
 * PopupStateEdgeMenuMousePlugin.
 * 
 * @author Sleiman Rabah based on Dr. Greg M. Bernstein's code.
 */
public class DeleteStateMenuItem<V> extends JMenuItem implements
        GIPSYTierMenuListener<V>
{

    private V oGraphNode;
    private VisualizationViewer visComp;

    /**
     * Class Constructor.
     * <p>
     * Creates a confirmation dialog before deleting a graph node.
     * 
     * @see JOptionPane
     */
    public DeleteStateMenuItem()
    {
        super("Delete State");

        this.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete this tier?",
                        "Confirm Delete Tier", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                {
                    visComp.getPickedVertexState().pick(oGraphNode, false);
                    visComp.getGraphLayout().getGraph()
                            .removeVertex(oGraphNode);
                    GIPSYTier oGIPSYTier = (GIPSYTier) oGraphNode;
                    if (GlobalInstance.getInstance().isItemExists(oGIPSYTier))
                    {
                        GlobalInstance.getInstance().remove(oGIPSYTier);
                        GlobalInstance.getInstance().removeFromHashMap(
                                oGIPSYTier.getTierID(), oGIPSYTier);
                    }
                    visComp.repaint();
                }
            }
        });
    }

    /**
     * Sets the Graph Visualizer and the state where the mouse has been clicked
     * on.
     * 
     * @param state
     *            a Geo. State to delete from the graph.
     * @param visComp
     *            The graph visualizer which holds the graph and its elements.
     */
    public void setGIPSYTierAndView(V state, VisualizationViewer visComp)
    {
        this.oGraphNode = state;
        this.visComp = visComp;
        this.setText("Delete Tier: " + ((GIPSYTier) state).getTierName());
    }

}
