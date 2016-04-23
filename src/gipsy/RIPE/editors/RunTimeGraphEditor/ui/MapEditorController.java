/**
 * MapEditorController.java 
 * 
 * @version $Revision: 1.6 $ $Date: 2012/06/21 19:50:29 $
 * 
 * Copyright(c) 2011, GIPSY Team.
 * 
 * $Id: MapEditorController.java,v 1.6 2012/06/21 19:50:29 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.AppConstants;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.FileFilterDialog;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.FileManager;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYPhysicalNode;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GraphDataManager;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.NodeConnection;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.GraphViewer;

/**
 * A class representing the map editor controller which controls the map editing
 * actions.
 * 
 * @author Sleiman Rabah
 * 
 */
public class MapEditorController extends JPanel implements Serializable
{

    private static final long serialVersionUID = 8817711300343377214L;

    /**
     * Represents an object of the FileManager class.
     */
    private FileManager oFileManager;
    /**
     * Represents an object of the graphViewer class.
     */
    private GraphViewer oGraphViewer;
    /**
     * Represents an object of the MapEditorState class.
     */
    private MapEditorState mapEditorState;
    private InstancesNodesPanel poInstancesNodesPanel;
    /**
     * The Map Editor toolbar.
     */
    protected JToolBar mapToolBar;
    /**
     * A reference used to control the zoom action of the graph visualizer.
     */
    private GraphZoomScrollPane scrollPane;

    /**
     * Class Constructor.
     * 
     * @param iMapEditorState
     *            a reference of the MapEditorState.
     * @see MapEditorState
     */
    public MapEditorController(MapEditorState iMapEditorState, InstancesNodesPanel ioInstancesNodesPanel)
    {
        super();
        mapEditorState = iMapEditorState;
        poInstancesNodesPanel = ioInstancesNodesPanel;
        
    }

    /**
     * Close the map.
     * <p>
     * <ul>
     * <li>Ask the user if he want to save the map before closing it.</li>
     * <li>Save the map the user confirm the saving action.</li>
     * </ul>
     */
    public void closeMap()
    {

        /*
         * if (mapEditorState.isMapSaved()) {
         * System.out.println("DN: Map is saved."); } if
         * (mapEditorState.isMapClosed()) {
         * System.out.println("DN: Map is closed."); }
         */
        if (oGraphViewer.getMapGraph() != null)
        {
            // If map is open and not saved and we have created at least on
            // node.
            if (!mapEditorState.isMapSaved() && !mapEditorState.isMapClosed()
                    && (oGraphViewer.getMapGraph().getVertexCount() > 0
                         || poInstancesNodesPanel.getNodesCount() > 0 
                         || poInstancesNodesPanel.getInstancesCount() > 0 )
                       )
            {

                int response = JOptionPane
                        .showConfirmDialog(this,
                                "Do you want to save this network?", "confirm",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);
                AppLogger.log("" + response);

                if (response != JOptionPane.DEFAULT_OPTION)
                {
                    if (response == JOptionPane.YES_OPTION)
                    {
                        // -- save map
                        saveMap();
                        mapEditorState.setCloseCanceled(true);
                    }
                    else if (response == JOptionPane.NO_OPTION)
                    {
                        updateMapView();
                        mapEditorState.setCloseCanceled(false);
                    }
                }
                else
                {
                    mapEditorState.setCloseCanceled(true);
                }
            }
            else
            {
                updateMapView();
            }
        }
    }

    /**
     * Creates a new map by instantiating the GraphViewer class.
     * 
     * @see GraphViewer
     */
    private void createMap()
    {
        oGraphViewer = new GraphViewer();
        scrollPane = new GraphZoomScrollPane(oGraphViewer.getGraphVisualizer());
        /*
         * Set up map editor panel settings.
         */
        this.setLayout(new GridLayout(1, 2));
        this.add(scrollPane);
        this.setVisible(true);
        this.setSize(900, 900);
        AppLogger.consoleSubAction("Network intialized successfully.");
    }

    /**
     * Validate the connections between the nodes, each node must connected with
     * at least with one or more node.
     * 
     * @return boolean indicating whether the data contained in the mapGraph
     *         object is valid or not.
     */
    private boolean isValidGraphToSave()
    {

        SparseMultigraph<GIPSYTier, NodeConnection> loGraph = (SparseMultigraph<GIPSYTier, NodeConnection>) oGraphViewer
                .getGraphVisualizer().getGraphLayout().getGraph();

        if (oGraphViewer.getGraphVisualizer().getGraphLayout().getGraph() == null
                || GlobalInstance.getInstance().getGIPSYNodesList().size() == 0
                || GlobalInstance.getInstance().getGIPSYTiersList().size() == 0)
        {

            JOptionPane
                    .showMessageDialog(
                            oGraphViewer.getGraphVisualizer(),
                            "You have to create at least one GIPSY Node and one GIPSY Tier connected before saving this graph!",
                            "Save Grpah", 0, new ImageIcon(
                                    AppConstants.ICON_ERROR));
            return false;
        }

        return true;
    }

    /**
     * Update the map view by removing the graphViewer object from the map
     * editor container.
     */
    private void updateMapView()
    {

        if (scrollPane != null)
        {
            scrollPane.remove(oGraphViewer.getGraphVisualizer());
            this.remove(scrollPane);
        }
        this.updateUI();
        mapEditorState.closeMap();
    }

    /**
     * Load the mapGraph object with the data contained in the gameMapObj
     * object.
     * 
     * @param gameMapObj
     *            the single GameMap object loaded with the data coming from the
     *            map file.
     */
    public void loadGraph()
    {

        try
        {
            GlobalInstance loGlobalInstance = GlobalInstance.getInstance();
            // Get the Geo. states from the Game Map object
            // and load them into the graph visualizer.
            AppLogger.consoleSubAction("Loading States ....");
            // --
            for (GIPSYTier geoState : loGlobalInstance.getGIPSYTiersList())
            {
                // --
                oGraphViewer.getMapGraph().addVertex(geoState);
                Point2D vertexLocation = new Point((int) geoState.getPosX(),
                        (int) geoState.getPosY());
                oGraphViewer.getGraphLayout().setLocation(geoState,
                        vertexLocation);
                GlobalInstance.getInstance().addToHashMap(geoState.getTierID(),
                        geoState);
            }

            AppLogger.consoleSubAction("States loaded successfully");
            AppLogger.consoleSubAction("Loading connections ...");
            // --
            for (NodeConnection edge : loGlobalInstance
                    .getNodesConnectionList())
            {

                GIPSYTier stateFrom = loGlobalInstance.getGIPSYTierByID(edge
                        .getFromTierId());
                GIPSYTier stateTo = loGlobalInstance.getGIPSYTierByID(edge
                        .getToTierId());
                oGraphViewer.getMapGraph().addEdge(edge, stateFrom, stateTo);
                GlobalInstance.getInstance().addToHashMap(edge.getEdgeId(),
                        edge);
            }

            // --
            addToHashMap();

            // -- Loop over the states and load them to the graph viewer.
            for (GIPSYTier state : oGraphViewer.getMapGraph().getVertices())
            {
                Collection<GIPSYTier> neighbors = oGraphViewer.getMapGraph()
                        .getNeighbors(state);
                // state.setNeighbors(neighbors);
            }

            AppLogger.consoleSubAction("States loaded successfully");

            // Refresh the map editor view.
            oGraphViewer.getGraphVisualizer().repaint();
            this.repaint();
            mapEditorState.openMap();

        }
        catch (Exception e)
        {
            AppLogger
                    .consoleSubAction("A probelm has occurred while loading the map.");
            System.out.println("A probelm has occurred while loading the map.");
            System.out.println(e.getMessage());
            e.getStackTrace();
        }
    }

    private void addToHashMap()
    {
        for (GIPSYPhysicalNode oGIPSYNode : GlobalInstance.getInstance()
                .getGIPSYNodesList())
        {
            GlobalInstance.getInstance().addToHashMap(oGIPSYNode.getNodeID(),
                    oGIPSYNode);
        }
        for (GIPSYInstance continent : GlobalInstance.getInstance()
                .getGIPSYInstancesList())
        {
            GlobalInstance.getInstance().addToHashMap(
                    continent.getInstanceID(), continent);
        }

    }

    /**
     * Load a map into the map editor.
     */
    public void loadGraphData()
    {

        AppLogger.consoleAction("loading Graph:");
        // --
        oFileManager = new FileManager(MapEditorController.this, "Load Graph",
                AppConstants.SAVED_GRAPHS_DIR, new FileFilterDialog(
                        AppConstants.SAVED_GRAPHS_FILE_EXT,
                        AppConstants.CONFIG_FILE_DESCR));
        oFileManager.setCurrentAction("Openning");
        String pstrFileName = oFileManager.getFileName();
        // Check if the the cancel button has been pressed.
        // Which means the loading operation has been canceled.
        if (!oFileManager.isCanceled() && pstrFileName != "")
        {
            mapEditorState.openMap();
            mapEditorState.setOpenCanceled(false);
            createMap();
            GraphDataManager oGraphDataManager = new GraphDataManager();
            oGraphDataManager.loadDataFromFile(pstrFileName);
            loadGraph();
        }
        else
        {
            AppLogger.consoleSubAction("You canceled.");
            mapEditorState.setOpenCanceled(true);
            mapEditorState.setMapClosed(true);
        }
       
    }

    /**
     * Map management methods.
     */
    public void newMap()
    {

        try
        {
            AppLogger.consoleAction("New network:");
            createMap();
            mapEditorState.openMap();
        }
        catch (Exception e)
        {
            System.out
                    .println("A problem has occured while creating a new map.");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Save the map into a file.
     * <p>
     * The user is asked to enter the name of the map and also the location
     * in/where to save the file by showing a File Chooser input dialog box.
     * 
     * @return boolean which indicates if the saving was completed successfully
     *         or not.
     * @see FileManager
     */
    public boolean saveMap()
    {
        boolean isValid = false;
        AppLogger.consoleAction("save Graph:");
        if (isValidGraphToSave())
        {
            oFileManager = new FileManager(MapEditorController.this,
                    "Save Graph", AppConstants.SAVED_GRAPHS_DIR,
                    new FileFilterDialog(AppConstants.SAVED_GRAPHS_FILE_EXT,
                            AppConstants.CONFIG_FILE_DESCR));
            oFileManager.setCurrentAction("Saving");
            String lstrFileName = oFileManager.getFileName();
            if (!oFileManager.isCanceled())
            {
                updateMapView();

                try
                {
                    if (oGraphViewer.getMapGraph() != null
                            && oGraphViewer.getMapGraph().getVertexCount() > 0)
                    {
                        // --
                        GraphDataManager oGraphDataManager = new GraphDataManager();
                        oGraphDataManager.saveDataToFile(lstrFileName);
                        isValid = true;
                    }
                }
                catch (Exception e)
                {
                    System.out
                            .println("A problem has occured while saving the graph: failed to save the map.");
                    System.out.println(e.getMessage());
                }
                finally
                {
                    updateMapView();
                }
            }
            else
            {
                mapEditorState.setSaveCanceled(true);
            }
        }
        return isValid;
    }

    /**
     * Gets the graphViewer object.
     * 
     * @return the graphViewer
     */
    public GraphViewer getGraphViewer()
    {
        return oGraphViewer;
    }

}
