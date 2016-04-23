/**
 * GraphVisualizer.java
 * A Singleton class representing map editor.
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.4 $ $Date: 2012/06/19 23:22:15 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: GraphViewer.java,v 1.4 2012/06/19 23:22:15 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.GraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import gipsy.RIPE.editors.RunTimeGraphEditor.ApplicationStarter;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.NodeConnection;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.AppLogger;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.GraphElements.EdgeFactory;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.GraphElements.GIPSYTierFactory;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.MapMouseMenus.GraphViewType;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.mouseplugin.CheckingGraphMousePlugin;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.mouseplugin.GraphMouseSetter;

/**
 * A class which creates and setup the Graph Visualizer. Also, sets the editing
 * mouse plug-in which is used to control the mouse events in the Map Editor.
 * 
 * @author Sleiman Rabah.
 */
public class GraphViewer
{

    /**
     * A reference to the nodes connections checker.
     */
    private ConnectionChecker oConnectionCheck;

    /**
     * The Layout instance which control mapGraph elements graphLayout.
     */
    private Layout<GIPSYTier, NodeConnection> oGraphLayout;

    /**
     * The Jung's library Visualization tool, it extends some Java Swing
     * components.
     */
    private VisualizationViewer<GIPSYTier, NodeConnection> oGraphVisualizer;

    /**
     * The Graph instance which holds the nodes and the edges.
     */
    private SparseMultigraph<GIPSYTier, NodeConnection> oMapGraph;
    /**
     * A reference to the nodes checker.
     */
    private GIPSYTierChecker oGIPSYTierChecker;

    /**
     * Class Constructor.
     * <p>
     * Creates visualizer object, connection and state checker.
     */
    public GraphViewer()
    {

        oConnectionCheck = new ConnectionChecker();
        oGIPSYTierChecker = new GIPSYTierChecker();
        // --
        initViewer();
    }

    /**
     * Creates a the Map editor Panel using Jung library.
     * <p>
     * The Map Editor is encapsulated in the Graph Visualizer object.
     */
    @SuppressWarnings("unchecked")
    private void initViewer()
    {

        try
        {

            oMapGraph = new SparseMultigraph<GIPSYTier, NodeConnection>();
            // Add the graph object to the layout.
            oGraphLayout = new StaticLayout(oMapGraph);

            // Add the layout object to the Visualizer.
            oGraphVisualizer = new VisualizationViewer<GIPSYTier, NodeConnection>(
                    oGraphLayout);

            // Show States and Connections labels on the map.
            // This call the to String method of each object.
            oGraphVisualizer.getRenderContext().setVertexLabelTransformer(
                    new ToStringLabeller());
            oGraphVisualizer.getRenderContext().setEdgeLabelTransformer(
                    new ToStringLabeller());

            // Create a mapGraph mouse and add it to the visualization viewer.
            // -- Prepare and set the plug-in with the nodes and edges factory.
            GraphMouseSetter graphMouse = new GraphMouseSetter(
                    oGraphVisualizer.getRenderContext(),
                    GIPSYTierFactory.getInstance(), EdgeFactory.getInstance());

            //
            CheckingGraphMousePlugin oPlugin = new CheckingGraphMousePlugin(
                    GIPSYTierFactory.getInstance(), EdgeFactory.getInstance());

            // Remove current map graph mouse current plugin.
            GraphMousePlugin oldPlugin = graphMouse.getEditingPlugin();
            // Remove the jung default plugin.
            graphMouse.remove(oldPlugin);

            // Create the States/Edge mouse pop-up plugin.
            PopUpNodeEdgeMenuMousePlugin loGIPSYTierMenuPopUp = new PopUpNodeEdgeMenuMousePlugin();

            // Add the state popup plugin to the graph mouse controller.
            JPopupMenu edgeMenu = new MapMouseMenus.EdgeMenu();
            JPopupMenu vertexMenu = new MapMouseMenus.GIPSYTiersMenu(
                    ApplicationStarter.getMainFrame(), GraphViewType.Editor);
            loGIPSYTierMenuPopUp.setEdgePopup(edgeMenu);
            loGIPSYTierMenuPopUp.setVertexPopup(vertexMenu);
            // Removes the default existing popup.
            graphMouse.remove(graphMouse.getPopupEditingPlugin());
            // Add our new plugin to the mouse
            graphMouse.add(loGIPSYTierMenuPopUp);
            // Set the customized plugin as mouse editing one.
            // NOTE: This plugin was added in order to avoid empty states
            // creation
            // in our case.
            oPlugin.setEdgeChecker(oConnectionCheck);
            oPlugin.setVertexChecker(oGIPSYTierChecker);
            graphMouse.setEditingPlugin(oPlugin);

            // Add the customized graph mouse controller to the Visualizer.
            oGraphVisualizer.setGraphMouse(graphMouse);
            // Add a key listener to the editor to be able to switch the Editing
            // mode: Editing/Picking, etc...
            oGraphVisualizer.addKeyListener(graphMouse.getModeKeyListener());

            oGraphVisualizer.addGraphMouseListener(oPlugin);

            // Add a Mouse Mode Menu Bar to the Map Editor in order to be
            // able to switch the Map editing mode.
            JMenuBar menuBar = new JMenuBar();
            JMenu modeMenu = graphMouse.getModeMenu();
            modeMenu.setText("Change Mouse mode");
            modeMenu.setIcon(null);
            modeMenu.setPreferredSize(new Dimension(150, 20));
            menuBar.add(modeMenu);

            oGraphVisualizer.setLayout(new BorderLayout());
            oGraphVisualizer.add(menuBar, BorderLayout.NORTH);
            oGraphVisualizer.setBackground(Color.lightGray);
            oGraphVisualizer.setForeground(Color.white);
            oGraphVisualizer.getRenderer().getVertexLabelRenderer()
                    .setPosition(Renderer.VertexLabel.Position.CNTR);
            oGraphVisualizer
                    .setToolTipText("<html><center>Use the mouse wheel to zoom<p>Click and Drag the mouse to pan<p>Shift-click and Drag to Rotate</center></html>");
            // Set edges shape to line.
            oGraphVisualizer.getRenderContext().setEdgeShapeTransformer(
                    new EdgeShape.Line<GIPSYTier, NodeConnection>());

            // Start the Map Editor off in editing mode.
            graphMouse.setMode(ModalGraphMouse.Mode.EDITING);

        }
        catch (Exception e)
        {
            AppLogger.consoleSubAction("Failed to create a new map.");
            AppLogger.consoleSubAction(e.getMessage());
        }
    }

    /**
     * Gets the graph layout of the graph visualizer.
     * 
     * @return the graphLayout which holds the graph object.
     */
    public Layout<GIPSYTier, NodeConnection> getGraphLayout()
    {
        return oGraphLayout;
    }

    /**
     * Gets the graph visualizer contained in the graphViewer object.
     * 
     * @return the graphVisualizer which holds the layout and the graph object.
     */
    public VisualizationViewer<GIPSYTier, NodeConnection> getGraphVisualizer()
    {
        return oGraphVisualizer;
    }

    /**
     * Get the graph object contained in the GraphViewer.
     * 
     * @return the mapGraph object which it contains the nodes and the edges.
     */
    public SparseMultigraph<GIPSYTier, NodeConnection> getMapGraph()
    {
        return oMapGraph;
    }

    /**
     * @param mapGraph
     *            the mapGraph to set
     */
    public void setMapGraph(SparseMultigraph<GIPSYTier, NodeConnection> mapGraph)
    {
        this.oMapGraph = mapGraph;
    }

}
