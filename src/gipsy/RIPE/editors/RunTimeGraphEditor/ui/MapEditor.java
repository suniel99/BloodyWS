/**
 * SimMapEditor.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.7 $ $Date: 2012/06/19 23:22:22 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: MapEditor.java,v 1.7 2012/06/19 23:22:22 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.*;

import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.AppConstants;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;

/**
 * A Singleton class representing the Map Editor of the game.
 * 
 * @author Sleiman Rabah
 * 
 */
public class MapEditor extends JPanel implements ToolBarSwitchView,
        Serializable
{

    private static final long serialVersionUID = 1933695400310995138L;

    /**
     * The load map button text.
     */
    public static final String BUTTON_LOAD_MAP = "Load Map";
    /**
     * The new map button text.
     */
    public static final String BUTTON_NEW_MAP = "New Map";
    /**
     * The save map button text.
     */
    public static final String BUTTON_SAVE_MAP = "Save Map";

    /**
     * The single instance of MapEditor class.
     */
    private static MapEditor simMapEditor_INSTANCE;

    private JButton btnClearMap;
    private JButton btnLoadMap;
    private JButton btnNewMap;
    private JButton btnSaveMap;
    private JButton help;
    // --
    private MapEditorController mapEditorController;
    private MapEditorState mapEditorState;
    private JButton minus;
    private JButton plus;
    // --
    private JSplitPane splitPane;
    // --
    private JToolBar tlbMapToolBar;

    private InstancesNodesPanel continentsCountriesPanel;

    /**
     * Class Constructor.
     */
    private MapEditor()
    {
        super();

        continentsCountriesPanel = new InstancesNodesPanel();
        mapEditorState = new MapEditorState();
        mapEditorController = new MapEditorController(mapEditorState, continentsCountriesPanel);

        // --
        this.initializeComponents();
        this.disableMapButtons();
    }

    /**
     * Initializes the Map Editor's components.
     * <p>
     * The Map Editor is divided in two main views: Graph visualizer and
     * countries and continents editor.
     */
    private void initializeComponents()
    {

        try
        {
            // Set the title's tab.
            setName("Graph Editor");
            
            // Create and initialize tool bar buttons of the map editor.
            this.createMapEditorToolBar();

            // Set up left panel
            continentsCountriesPanel.setOpaque(true); // content panes must be
                                                      // opaque

            this.setLayout(new BorderLayout());
            this.setOpaque(false);

            // Create a split pane with the two scroll panes in it.
            splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            // --
            splitPane.setLeftComponent(continentsCountriesPanel);
            // --
            splitPane.setRightComponent(new JScrollPane(mapEditorController));

            // Set the Divider width.
            splitPane.setDividerLocation(150);
            // Provide a preferred size for the split pane.
            splitPane.setPreferredSize(new Dimension(250, 200));

            this.add(splitPane);
        }
        catch (Exception e)
        {
            System.out.println("Map editor initialization failed.");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create the tool bar of the map editor tab.
     */
    private void createMapEditorToolBar()
    {

        tlbMapToolBar = new JToolBar();
        tlbMapToolBar.setRollover(true);
        tlbMapToolBar.setFloatable(false);

        /**
         * Setup Buttons
         */
        ImageIcon newIcon = new ImageIcon(AppConstants.ICON_NEW_MAP);
        btnNewMap = new JButton(newIcon);
        btnNewMap.setToolTipText(BUTTON_NEW_MAP);
        // btnNewMap.setActionCommand(BUTTON_NEW_MAP);
        btnNewMap.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                doNewMap();
            }
        });

        // --
        ImageIcon load = new ImageIcon(AppConstants.ICON_LOAD_MAP);
        btnLoadMap = new JButton(load);
        btnLoadMap.setToolTipText(BUTTON_LOAD_MAP);
        // btnLoadMap.setActionCommand(BUTTON_LOAD_MAP);
        btnLoadMap.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                doLoadMap();
            }
        });

        // --
        ImageIcon save = new ImageIcon(AppConstants.ICON_SAVE_MAP);
        btnSaveMap = new JButton(save);
        btnSaveMap.setToolTipText(BUTTON_SAVE_MAP);
        // btnSaveMap.setActionCommand(BUTTON_SAVE_MAP);
        btnSaveMap.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                doSaveMap();
            }
        });

        /**
         * Map editor tool bar. Used to create some control buttons like Zoom
         * in/Zoom out.
         */

        /* Buttons */
        final ScalingControl scaler = new CrossoverScalingControl();

        // -- Zoom In button.
        ImageIcon icon = new ImageIcon(AppConstants.ICON_MAP_ZOOM_IN);
        plus = new JButton(icon);
        plus.setToolTipText("Zoom In");
        plus.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                scaler.scale(mapEditorController.getGraphViewer()
                        .getGraphVisualizer(), 1.1f, mapEditorController
                        .getGraphViewer().getGraphVisualizer().getCenter());
            }
        });
        // -- Zoom Out button.
        ImageIcon icon1 = new ImageIcon(AppConstants.ICON_MAP_ZOOM_OUT);
        minus = new JButton(icon1);
        minus.setToolTipText("Zoom Out");
        minus.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                scaler.scale(mapEditorController.getGraphViewer()
                        .getGraphVisualizer(), 1 / 1.1f, mapEditorController
                        .getGraphViewer().getGraphVisualizer().getCenter());
            }
        });
        // -- Help button.
        ImageIcon helpIcon = new ImageIcon(AppConstants.ICON_MAP_HELP);
        help = new JButton(helpIcon);
        help.setToolTipText("Help");
        help.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(MapEditor.this,
                        AppConstants.MAP_EDITOR_INSTRUCTIONS);
            }
        });

        // -- Close Map Button.
        ImageIcon clearMap = new ImageIcon(AppConstants.ICON_MAP_CLOSE);
        btnClearMap = new JButton(clearMap);
        btnClearMap.setToolTipText("Close Map");
        btnClearMap.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                doCloseMap();
            }
        });

        tlbMapToolBar.add(btnNewMap);
        tlbMapToolBar.addSeparator();
        tlbMapToolBar.add(btnLoadMap);
        tlbMapToolBar.addSeparator();
        tlbMapToolBar.add(btnSaveMap);
        tlbMapToolBar.addSeparator();
        tlbMapToolBar.add(btnClearMap);
        tlbMapToolBar.addSeparator();
        tlbMapToolBar.add(minus);
        tlbMapToolBar.addSeparator();
        tlbMapToolBar.add(plus);
        tlbMapToolBar.addSeparator();
        tlbMapToolBar.add(help);
    }

    /**
     * Disable the Map Editor control buttons.
     */
    public void disableMapButtons()
    {

        this.minus.setEnabled(false);
        this.plus.setEnabled(false);
        this.btnClearMap.setEnabled(false);
        this.btnSaveMap.setEnabled(false);
    }

    /**
     * Close the Map Editor by resetting disabling its components.
     */
    private void doCloseMap()
    {

        mapEditorController.closeMap();

        if (!mapEditorState.isCloseCanceled() && mapEditorState.isMapClosed())
        {
            restContinentsCountriesPanel();
            this.repaint();
            disableMapButtons();
            resetGameMap();
            MessageAndErrorConsole.getInstance().disablePanels();
        }
    }

    /**
     * Load a map file into the Map Editor.
     */
    private void doLoadMap()
    {
        if (!GlobalInstance.getInstance().isLoaded())
        {
            // Load a map.
            if (mapEditorState.isMapClosed())
            {
                resetGameMap();
                mapEditorController.loadGraphData();
                this.repaint();
                if (!mapEditorState.isOpenCanceled())
                {
                    // mapEditorController.loadMap();
                    continentsCountriesPanel.addComponents();
                    continentsCountriesPanel.loadGIPSYInstanceList();
                    continentsCountriesPanel.loadGIPSYNodeList();
                    // -
                    enableMapButtons();
                    MessageAndErrorConsole.getInstance().enablePanels();
                    this.repaint();
                }
            }
            else
            {
                JOptionPane
                        .showMessageDialog(null,
                                "You must close the new map window before loading a map");
            }
        }
        else
        {
            MessageBoxWrapper.displayErrorMsg(this, "Another graph is being edited/opened in other view. Please close it and retry again."
                    , "Load Saved Graph");
        }
    }

    /**
     * Creates a new map.
     */
    private void doNewMap()
    {
        // Allow only one open new map frame.
        if (mapEditorState.isMapClosed())
        {
            resetGameMap();
            mapEditorController.newMap();
            continentsCountriesPanel.addComponents();
            this.repaint();
            mapEditorState.setMapClosed(false);            

            // --
            enableMapButtons();
            MessageAndErrorConsole.getInstance().enablePanels();
        }
        else
        {
            JOptionPane
                    .showMessageDialog(null,
                            "You must close the new map window before creating a new map");
        }
    }

    /**
     * Save a map to an Xml file and rest the Map Editor components.
     */
    private void doSaveMap()
    {

        if (mapEditorController.saveMap())
        {
            if (!mapEditorState.isSaveCanceled())
            {
                restContinentsCountriesPanel();
                GlobalInstance.getInstance().setCleanMap(true);
                GlobalInstance.getInstance().reset();

                this.repaint();
                // Disable Map's buttons.
                disableMapButtons();
            }
        }
    }

    private void restContinentsCountriesPanel()
    {
        continentsCountriesPanel.clearGIPSYInstanceList();
        continentsCountriesPanel.clearGIPSYNodesList();
        continentsCountriesPanel.removeCompenents();
    }

    private void resetGameMap()
    {
        GlobalInstance.getInstance().setCleanMap(true);
        GlobalInstance.getInstance().reset();
    }

    /**
     * Enable the Map Editor control buttons.
     */
    public void enableMapButtons()
    {

        this.minus.setEnabled(true);
        this.plus.setEnabled(true);
        this.btnClearMap.setEnabled(true);
        this.btnSaveMap.setEnabled(true);
    }

    /**
     * Gets the Map Editor toolbar which contains the control buttons.
     * 
     * @return tlbMapToolBar the Map Editor toolbar.
     */
    public JToolBar getToolBar()
    {
        return this.tlbMapToolBar;
    }

    /**
     * Gets the singleton instance.
     * 
     * @return simMapEditor_INSTANCE the singleton instance of the MapEditor
     *         class.
     */
    public static MapEditor getMapEditor_INSTANCE()
    {

        if (simMapEditor_INSTANCE == null)
            simMapEditor_INSTANCE = new MapEditor();

        return simMapEditor_INSTANCE;
    }

}
