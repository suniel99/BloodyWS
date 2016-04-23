/**
 * MapMouseMenus.java
 * 
 * GIPSY Project.
 * 
 * @version $Revision: 1.6 $ $Date: 2012/06/20 21:36:17 $
 * 
 * Copyright(c) 2011, GIPSY  Team .
 * 
 * $Id: MapMouseMenus.java,v 1.6 2012/06/20 21:36:17 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import gipsy.RIPE.editors.RunTimeGraphEditor.ApplicationStarter;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.NodeConnection;
import gipsy.RIPE.editors.RunTimeGraphEditor.operator.GIPSYCommandRunner;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.GraphPanel;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.MessageBoxWrapper;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.dialogs.TierPropertyDialog;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers.VerticesRegister;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.plaf.basic.BasicScrollPaneUI.VSBChangeListener;

/**
 * A collection of classes used to assemble popup mouse menus for the custom
 * edges and vertices developed in this example.
 * 
 * @author Sleiman Rabah based on Dr. Greg M. Bernstein's code.
 */
public class MapMouseMenus
{

    public enum GraphViewType
    {
        Editor, Operator
    }

    /**
     * A class used to show a popup menu when the mouse is clicked on a given
     * edge.
     * 
     * @author Sleiman Rabah
     */
    public static class EdgeMenu extends JPopupMenu
    {

        /**
         * Class Constructor.
         */
        public EdgeMenu()
        {
            super("Connection Menu");
            this.add(new DeleteEdgeMenuItem<NodeConnection>());
        }
    }

    /**
     * A class representing a mouse pop-up menu when the mouse is clicked on a
     * given node.
     * 
     * @author Sleiman Rabah
     */
    public static class GIPSYTiersMenu extends JPopupMenu
    {

        JMenuItem oStartNodeItem = new JMenuItem("Start Tier");

        /**
         * Class Constructor.
         * 
         * @param frame
         *            parent of the pop-up menu.
         */
        public GIPSYTiersMenu(JFrame frame, GraphViewType eContextMenuType)
        {
            super("Tier Menu");

            if (eContextMenuType == GraphViewType.Editor)
            {
                this.add(new DeleteStateMenuItem<GIPSYTier>());
                this.addSeparator();
                this.add(new CreateGIPSYTierPropertyItem(frame));
            }
            else if (eContextMenuType == GraphViewType.Operator)
            {
                this.add(new AllocateGIPSYTier<GIPSYTier>());
                this.add(new DeAllocateGIPSYTier<GIPSYTier>());
            }
        }
    }

    public static class CreateGIPSYTierPropertyItem extends JMenuItem implements
            GIPSYTierMenuListener<GIPSYTier>, MenuPointListener
    {

        private static final long serialVersionUID = 1L;
        /**
         * A point (x,y) which is the location where to show the menu.
         */
        private Point2D point;
        /**
         * A reference to a Geo. State which is a node the graph of the Map
         * Editor.
         */
        private GIPSYTier oGIPSYTier;
        /**
         * A reference to the graph visualizer of the Map Editor.
         */
        private VisualizationViewer visComp;

        /**
         * Class Constructor which creates the pop-up menu and sets its
         * appropriate <br>
         * action listener.
         * 
         * @param frame
         *            parent of the pop-up menu.
         */
        public CreateGIPSYTierPropertyItem(final JFrame frame)
        {
            super("Edit Tier Properties...");
            this.addActionListener(new ActionListener() 				
            {
                public void actionPerformed(ActionEvent e)
                {
                    TierPropertyDialog dialog = new TierPropertyDialog(frame,
                            oGIPSYTier);
                    dialog.setLocation((int) point.getX() + frame.getX(),
                            (int) point.getY() + frame.getY());
                    dialog.setLocationRelativeTo(ApplicationStarter
                            .getMainFrame());
                    visComp.repaint();
                }
            });
        }

        /**
         * @param point
         *            the point which is the location where to show the pop-up
         *            menu.
         */
        public void setPoint(Point2D point)
        {
            this.point = point;
        }

        /**
         * Sets the reference of the state and the graph visualizer.
         * 
         * @param poGIPSYTiuer
         *            a reference to a GIPSY tier object.
         * @param poVisComp
         *            a reference to the graph visualizer of the Map Editor.
         */
        public void setGIPSYTierAndView(GIPSYTier poGIPSYTiuer,
                VisualizationViewer poVisComp)
        {
            this.oGIPSYTier = poGIPSYTiuer;
            this.visComp = poVisComp;
        }

    }

    /**
     * A class to implement the deletion of a vertex from within a
     * PopupStateEdgeMenuMousePlugin.
     * 
     * @author Sleiman Rabah based on Dr. Greg M. Bernstein's code.
     */
    public static class AllocateGIPSYTier<V> extends JMenuItem implements
            GIPSYTierMenuListener<V>
    {

        private V oGraphNode;
        private VisualizationViewer visComp;
        private GIPSYTier oGIPSYTier = null;

        /**
         * Class Constructor.
         * <p>
         * Creates a confirmation dialog before deleting a graph node.
         * 
         * @see JOptionPane
         */
        public AllocateGIPSYTier()
        {
            super(" Start Tier ");

            this.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {

                    visComp.getPickedVertexState().pick(oGraphNode, false);
                    GIPSYTier oGIPSYTier = (GIPSYTier) oGraphNode;
                    if (GlobalInstance.getInstance().isItemExists(oGIPSYTier))
                    {
                        if (GlobalInstance.getInstance().isOneGMTIsRunning())
                        {
                            // -- Check if already allocated then set enabled to
                            // false;
                            GIPSYCommandRunner.allocateGIPSYTier(oGIPSYTier);                            
                            // Highlight the tier in question if it has been successfully started.
                            //System.out.println("Highlight here");
                            GraphPanel.getInstance().highlightNode(oGIPSYTier);
                        }
                        else
                        {
                            MessageBoxWrapper.displayErrorMsg(
                                    visComp, 
                                    "You must at least start one GMT before doing this action!", 
                                    "Allocate GIPSY Tier");
                        }

                    }

                }
            });
            if (oGIPSYTier != null)
            {
                this.setEnabled(!oGIPSYTier.isAllocated());
            }
        }

        /**
         * Sets the Graph Visualizer and the state where the mouse has been
         * clicked on.
         * 
         * @param state
         *            a Geo. State to delete from the graph.
         * @param visComp
         *            The graph visualizer which holds the graph and its
         *            elements.
         */
        public void setGIPSYTierAndView(V state, VisualizationViewer visComp)
        {
            this.oGraphNode = state;
            this.visComp = visComp;
        }
    }

    public static class DeAllocateGIPSYTier<V> extends JMenuItem implements
            GIPSYTierMenuListener<V>
    {

        private V oGraphNode;
        private VisualizationViewer visComp;
        private GIPSYTier oGIPSYTier = null;

        /**VerticesRegister
         * Class Constructor.
         * <p>
         * Creates a confirmation dialog before deleting a graph node.
         * 
         * @see JOptionPane
         */
        public DeAllocateGIPSYTier()
        {
            super(" Stop Tier ");

            this.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {

                    visComp.getPickedVertexState().pick(oGraphNode, false);
                    GIPSYTier oGIPSYTier = (GIPSYTier) oGraphNode;
                    if (GlobalInstance.getInstance().isItemExists(oGIPSYTier))
                    {
                        // -- Check if already allocated then set enabled to
                        // false;
                        if (GlobalInstance.getInstance().isOneGMTIsRunning())
                        {
                            GIPSYCommandRunner.StopGIPSYTier(oGIPSYTier);
                            // Unhighlight deallocated tier.
                            GraphPanel.getInstance().clearHighlightedNode(oGIPSYTier);
                        }
                        else
                        {
                            MessageBoxWrapper.displayErrorMsg(
                                    visComp, 
                                    "You must at least start one GMT before doing this action!", 
                                    "Deallocate GIPSY Tier");
                        }
                    }

                }
            });
            if (oGIPSYTier != null)
            {
                this.setEnabled(!oGIPSYTier.isDeAllocated());
            }
        }

        /**
         * Sets the Graph Visualizer and the state where the mouse has been
         * clicked on.
         * 
         * @param state
         *            a Geo. State to delete from the graph.
         * @param visComp
         *            The graph visualizer which holds the graph and its
         *            elements.
         */
        public void setGIPSYTierAndView(V state, VisualizationViewer visComp)
        {
            this.oGraphNode = state;
            this.visComp = visComp;
        }
    }
}
