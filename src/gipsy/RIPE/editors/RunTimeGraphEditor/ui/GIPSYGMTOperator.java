/**
 * SimActivityView.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.5 $ $Date: 2012/06/21 19:50:29 $
 * 
 *          Copyright(c) 2010, SOEN6441 Team 8.
 * 
 *          $Id: GIPSYGMTOperator.java,v 1.5 2012/06/21 19:50:29 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.Random;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.AppConstants;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.FileFilterDialog;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.FileManager;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYPhysicalNode;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GraphDataManager;
import gipsy.RIPE.editors.RunTimeGraphEditor.operator.GIPSYCommandRunner;
import gipsy.RIPE.editors.RunTimeGraphEditor.operator.GIPSYCommands;
import gipsy.RIPE.editors.RunTimeGraphEditor.operator.GIPSYCommands.COMMANDS_TYPE;
import gipsy.RIPE.editors.RunTimeGraphEditor.operator.GIPSYEntityManger;
import gipsy.RIPE.editors.RunTimeGraphEditor.operator.GIPSYGMTController;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.dialogs.GIPSYNodeDialog;
import gipsy.util.Trace;

/**
 * A class representing the simulation activity main view. <br>
 * It holds its own menu which controls the simulation activities.
 * 
 * @author Sleiman Rabah
 * 
 */
public class GIPSYGMTOperator extends JPanel implements ToolBarSwitchView,
        ActionListener, MouseListener, Serializable, ItemListener
{

    private static final long serialVersionUID = -4698624349613823294L;

    public static final String BUTTON_STOP_CONSOLE = "Stop Instances";
    public static final String BUTTON_LOAD_SIM = "Load Graph";
    public static final String BUTTON_SAVE_SIM = "Save Graph";
    public static final String BUTTON_START_SIM = "Start Instances";
    public static final String BUTTON_CLEAR_SIM = "Clear View";
    public static final String START_NODE_CMD = "Start Node";
    public static final String REGISTER_NODE_CMD = "Register Node";
    public static final String STOP_NODE_CMD = "STOP Node";
    private static final String MSG_PREFIX = "["
            + Trace.getEnclosingClassName() + "] ";

    private static GIPSYGMTOperator simActivityView_INSTANCE;

    private Image map;
    // -- Buttons
    private JButton btnStopSim;
    private JButton btnLoadSim;
    private JButton btnSaveSim;
    private JButton btnStartSim;
    private JButton btnClearSim;
    private JButton minus;
    private JButton plus;

    // -- Panels
    private JPanel leftPanel;
    private JPanel oGIPSYNodesPanel;
    // -- States Panels
    private JPanel statesPanel;
    // -- Simulation Panels
    private JPanel oSimPanel;
    private JPanel imgPanel;
    private JList oGIPSYNodeList;
    private DefaultListModel oGIPSYNodeListModel;
    private JList oGIPSYTiersList;
    private DefaultListModel oStatesListModel;
    private JToolBar tlbActivityToolBar;
    private JPanel oMainPanel;
    // --
    private GraphPanel bOperatorView;
    private ColorRenderer listCellRender;
    private FileManager fileManager;
    private ActionsLog oActionsLog;
    // --
    private boolean bIsRunning;
    private float zoomPoint;
    private boolean bIsCountinue;
    private FileManager oFileManager;
    private GIPSYGMTController oGIPSYEntityController;
    private JPopupMenu oNodeListPopUpMenu;
    private JMenuItem oMenuItemStartNode;
    private JMenuItem oMenuItemRegisterNode;
    private JMenuItem oMenuItemStopNode;
    private GIPSYPhysicalNode oCurrentSelectedNode;

    /**
     * Class constructor.
     */
    private GIPSYGMTOperator()
    {
        super();
        this.initializeComponents();
        bIsRunning = false;
        zoomPoint = 0.8f;
        bIsCountinue = false;
        oGIPSYEntityController = new GIPSYGMTController();
    }

    /**
     * Initialize the simulation component and create the simulation buttons.
     */
    private void initializeComponents()
    {
        setName("GMT Operator");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        getWarImage();

        // -- Toolbar and Buttons setup
        tlbActivityToolBar = new JToolBar();
        tlbActivityToolBar.setRollover(true);
        tlbActivityToolBar.setFloatable(false);
        // --
        ImageIcon start = new ImageIcon("res/go-next.png");
        btnStartSim = new JButton(start);
        btnStartSim.setToolTipText(BUTTON_START_SIM);
        btnStartSim.setActionCommand(BUTTON_START_SIM);
        btnStartSim.addActionListener(this);
        // -- Clear Console Button
        ImageIcon clearConsole = new ImageIcon("res/process-stop.png");
        btnStopSim = new JButton(clearConsole);
        btnStopSim.setActionCommand(BUTTON_STOP_CONSOLE);
        btnStopSim.addActionListener(this);
        btnStopSim.setToolTipText(BUTTON_STOP_CONSOLE);
        // -- Load Simulation button
        ImageIcon load = new ImageIcon("res/document-open.png");
        btnLoadSim = new JButton(load);
        btnLoadSim.setActionCommand(BUTTON_LOAD_SIM);
        btnLoadSim.addActionListener(this);
        btnLoadSim.setToolTipText(BUTTON_LOAD_SIM);
        // -- Save Simulation button
        ImageIcon save = new ImageIcon("res/save.png");
        btnSaveSim = new JButton(save);
        btnSaveSim.setActionCommand(BUTTON_SAVE_SIM);
        btnSaveSim.addActionListener(this);
        btnSaveSim.setToolTipText(BUTTON_SAVE_SIM);
        // --
        btnClearSim = new JButton();
        ImageIcon stop = new ImageIcon("res/system-log-out.png");
        btnClearSim = new JButton(stop);
        btnClearSim.setActionCommand(BUTTON_CLEAR_SIM);
        btnClearSim.addActionListener(this);
        btnClearSim.setToolTipText(BUTTON_CLEAR_SIM);

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
                updateZoomOut();
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
                updateZoomIn();
            }
        });

        tlbActivityToolBar.add(btnStartSim);
        tlbActivityToolBar.addSeparator();
        tlbActivityToolBar.add(btnLoadSim);
        tlbActivityToolBar.addSeparator();
        tlbActivityToolBar.add(btnSaveSim);
        tlbActivityToolBar.addSeparator();
        tlbActivityToolBar.add(btnStopSim);
        tlbActivityToolBar.add(btnClearSim);
        tlbActivityToolBar.add(minus);
        tlbActivityToolBar.add(plus);

        // --
        disableButtons();
    }

    private void startInstance()
    {
        try
        {
            // -- Start the GMT first.
            System.out.println("Starting the GMT...");
            oGIPSYEntityController.startGMTNode();
            System.out.println("GMT started successfully...");
            oGIPSYEntityController.allocateTier();

        }
        catch (Exception e)
        {
            System.err
                    .println("An error occured while trying to start instances.\n Error"
                            + e.getMessage());
        }
    }

    /**
     * Zoom in the graph view by decreasing the zoom value.
     */
    public void updateZoomIn()
    {
        this.zoomPoint -= 0.1f;
    }

    /**
     * Zoom out the graph view by increasing the zoom value.
     */
    public void updateZoomOut()
    {
        this.zoomPoint = (1 / 1.f);
    }

    /**
     * Creates the countries panel which contains the countries name and the
     * spinner panel.
     * 
     * @return countriesPanel
     */
    private Component makeGIPSYNodesPanel()
    {

        // -- Countries's components setup: countries list and properties.
        // --
        oGIPSYNodesPanel = new JPanel();
        oGIPSYNodesPanel.setLayout(new BorderLayout());
        oGIPSYNodesPanel.setBorder(BorderFactory.createEmptyBorder());
        // --
        listCellRender = new ColorRenderer();
        oGIPSYNodeListModel = new DefaultListModel();
        oGIPSYNodeList = new JList(oGIPSYNodeListModel);
        oGIPSYNodeList.setCellRenderer(listCellRender);
        // --
        oGIPSYNodeList.setBorder(BorderFactory
                .createTitledBorder("GIPSY Nodes List:"));
        // -- Add a pop up menu to the Nodes list.

        oNodeListPopUpMenu = new JPopupMenu();
        oMenuItemStartNode = new JMenuItem(START_NODE_CMD);
        oMenuItemStartNode.addActionListener(this);
        oNodeListPopUpMenu.add(oMenuItemStartNode);
        // --
        oMenuItemRegisterNode = new JMenuItem(REGISTER_NODE_CMD);
        oMenuItemRegisterNode.addActionListener(this);
        oNodeListPopUpMenu.add(oMenuItemRegisterNode);
        // --
        oMenuItemStopNode = new JMenuItem(STOP_NODE_CMD);
        oMenuItemStopNode.addActionListener(this);
        oNodeListPopUpMenu.add(oMenuItemStopNode);
        // Add listener to components that can bring up popup menus.
        MouseListener popupListener = new PopupListener();
        oGIPSYNodeList.addMouseListener(popupListener);

        oGIPSYNodesPanel.add(new JScrollPane(oGIPSYNodeList));
        oGIPSYNodeList.setSelectionForeground(Color.black);
        return oGIPSYNodesPanel;
    }

    /**
     * Creates and returns the simulation actions log.
     * 
     * @return component representing the actions log.
     */
    private Component makeActionsLogPanel()
    {
        oActionsLog = new ActionsLog();
        Component component = oActionsLog.getActionsLog();

        return component;
    }

    /**
     * Creates the states panel containing the actions log and the nodes names.
     * 
     * @return statesPanel the state panel where the nodes names are displayed.
     */
    private Component makeGIPSYTiersPanel()
    {

        // -- States's components setup: states list and properties.
        // --
        statesPanel = new JPanel();
        statesPanel.setLayout(new BorderLayout());
        statesPanel.setPreferredSize(new Dimension(100, 50));

        oStatesListModel = new DefaultListModel();
        oGIPSYTiersList = new JList(oStatesListModel);
        oGIPSYTiersList.addMouseListener(this);
        oGIPSYTiersList.setBorder(BorderFactory
                .createTitledBorder("GIPSY Tiers List:"));

        JScrollPane sp = new JScrollPane(oGIPSYTiersList);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JSplitPane splitPaneLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        // -- countries Panel
        splitPaneLeft.setTopComponent(sp);
        // -- States Panel
        splitPaneLeft.setBottomComponent(makeActionsLogPanel());
        splitPaneLeft.setDividerLocation(100);
        // splitPaneLeft.setPreferredSize(new Dimension(200, 150));
        statesPanel.add(splitPaneLeft);

        return statesPanel;
    }

    /**
     * Create and initialize the simulation's panel.
     */
    private void createPanels()
    {

        this.remove(imgPanel);

        // -- Panels setup -----
        bOperatorView = GraphPanel.getInstance();
        oMainPanel = new JPanel();
        oSimPanel = new JPanel();
        oSimPanel.setLayout(new BorderLayout());

        // ---------------------
        // -- Setup the left Panel
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setOpaque(true);
        JSplitPane splitPaneLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        // -- GIPSY nodes Panel
        splitPaneLeft.setTopComponent(makeGIPSYNodesPanel());
        // -- GIPSY Tiers Panel
        splitPaneLeft.setBottomComponent(makeGIPSYTiersPanel());
        splitPaneLeft.setDividerLocation(200);
        leftPanel.add(splitPaneLeft);

        // -- Create the graph view.
        makeGraphView();

        oMainPanel.setLayout(new BorderLayout());
        JSplitPane splitPaneMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneMain.setLeftComponent(leftPanel);
        splitPaneMain.setRightComponent(oSimPanel);
        splitPaneMain.setDividerLocation(400);

        oMainPanel.add(splitPaneMain);

        this.add(oMainPanel);
        this.repaint();
    }

    /**
     * This method is used to verify is the map is loaded in the map editor.
     * 
     * @return isValid determines whether the map has been loaded or not.
     */
    private boolean isMapLoaded()
    {

        boolean isValid = false;

        GlobalInstance oSingleInstance = GlobalInstance.getInstance();
        if (oSingleInstance.getGIPSYInstancesList().size() > 0
                && oSingleInstance.getGIPSYNodesList().size() > 0
                && oSingleInstance.getGIPSYTiersList().size() > 0)
        {
            isValid = true;
        }

        return isValid;
    }

    private void enablePanels()
    {
        try
        {
            createPanels();
            loadListPanels();
            enableButtons();
            bIsRunning = true;
        }
        catch (Exception e)
        {
            System.err.println(e.getStackTrace());
        }
    }

    /**
     * Load the countries and the nodes name panel with the loaded map data.
     */
    private void loadListPanels()
    {

        for (GIPSYPhysicalNode oGIPSYNode : GlobalInstance.getInstance()
                .getGIPSYNodesList())
        {
            int lastPosition = oGIPSYNodeList.getModel().getSize();
            oGIPSYNodeListModel.add(lastPosition, oGIPSYNode);
        }
        // -- Load States
        for (GIPSYTier oGIPSYTier : GlobalInstance.getInstance()
                .getGIPSYTiersList())
        {
            int lastPosition = oGIPSYTiersList.getModel().getSize();
            oStatesListModel.add(lastPosition, oGIPSYTier.getTierName());
        }
    }

    /**
     * Create the graph panel and add it to the simulation panel.
     */
    private void makeGraphView()
    {
        try
        {

            oSimPanel.add(bOperatorView.getGraphVisualizer(this.zoomPoint));
        }
        catch (Exception e)
        {
            System.err
                    .println("SimActivityView.java: A problem has occrured while loading the graph in the simulation view");
        }
    }

    /**
     * Stop the simulation while it is running.
     */
    private void pauseSimulationView()
    {

        this.bIsCountinue = true;
        this.btnStartSim.setEnabled(true);
        // this.simController.setGameOver(true);
    }

    /**
     * Reset the simulation view by cleaning up its components.
     */
    private void clearSimulationView()
    {
        // -- Verify if the a Map has been loaded in the Map Editor.
        if (JOptionPane.showConfirmDialog(null,
                "Are you sure you want to exit?",
                "Confirm Close Simualtion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            bIsRunning = false;
            // -- Clear the countries list
            oGIPSYNodeListModel.removeAllElements();
            // -- Clear the states list
            oStatesListModel.removeAllElements();
            // -- Clear the graph view.
            oSimPanel.removeAll();
            disableButtons();
            this.remove(oMainPanel);
            this.removeAll();
            getWarImage();
            refreshSimView();
            // -- Reset the global instance object.
            GlobalInstance.getInstance().setCleanMap(true);
            GlobalInstance.getInstance().reset();
            GlobalInstance.getInstance().setInstanceToNull(null);
            MessageAndErrorConsole.getInstance().disablePanels();
        }

    }

    /**
     * Create an image panel displayed in the simulation tab.
     */
    private void getWarImage()
    {
    	
        Random ran = new Random();
        map = grabImage("res/gipsystar.gif");

        imgPanel = new JPanel()
        {
            {
                setSize(200, 200);
            }

            @Override
            public void paint(Graphics g)
            {
                g.drawImage(map, 250, 100, 200, 200, Color.white, null);
            }
        };

        this.add(imgPanel);
    }

    /**
     * Create a splash image.
     * 
     * @param path
     *            the path to the splash image.
     * @return img an image object with the splash image.
     */
    public Image grabImage(String path)
    {
        Image img = new ImageIcon(path).getImage();
        return img;
    }

    /**
     * disable the simulation buttons.
     */
    private void disableButtons()
    {
        btnSaveSim.setEnabled(false);
        btnStopSim.setEnabled(false);
        btnClearSim.setEnabled(false);
        btnStartSim.setEnabled(false);
        btnLoadSim.setEnabled(true);
        minus.setEnabled(false);
        plus.setEnabled(false);
    }

    /**
     * Enable the simulation buttons.
     */
    private void enableButtons()
    {

        btnSaveSim.setEnabled(true);
        btnStopSim.setEnabled(true);
        btnClearSim.setEnabled(true);
        btnStartSim.setEnabled(true);
        btnLoadSim.setEnabled(false);
        minus.setEnabled(true);
        plus.setEnabled(true);
    }

    /**
     * Load the UI's panel upon the a saved game load.
     */
    private void loadView()
    {

    }

    /**
     * Continue the simulation if we've loaded a saved one.
     */
    public void continueSimulation()
    {
    }

    /**
     * Pops up a File Dialog and Load a saved simulation based on the file
     * selected in the Dialog Box.
     * 
     */
    public void loadSavedGraph()
    {
        try
        {
            if (!GlobalInstance.getInstance().isLoaded())
            {
                AppLogger.consoleAction("loading Graph:");
                // --
                oFileManager = new FileManager(GIPSYGMTOperator.this,
                        "Load Graph", AppConstants.SAVED_GRAPHS_DIR,
                        new FileFilterDialog(
                                AppConstants.SAVED_GRAPHS_FILE_EXT,
                                AppConstants.CONFIG_FILE_DESCR));
                oFileManager.setCurrentAction("Openning");
                String pstrFileName = oFileManager.getFileName();
                // Check if the the cancel button has been pressed.
                // Which means the loading operation has been cancelled.
                if (!oFileManager.isCanceled() && pstrFileName != "")
                {
                    GraphDataManager oGraphDataManager = new GraphDataManager();
                    oGraphDataManager.loadDataFromFile(pstrFileName);
                    enablePanels();
                    MessageAndErrorConsole.getInstance().enablePanels();
                }
            }
            else
            {
                MessageBoxWrapper
                        .displayErrorMsg(
                                this,
                                "Another graph is being edited/opened in other view. Please close it and retry again.",
                                "Load Saved Graph");
            }
        }
        catch (Exception e)
        {

            AppLogger
                    .printMsg("An error occured while trying to load the selected file.");
        }
    }

    /**
     * Save a running simulation to a binary file.
     */
    public void saveSimulation()
    {

    }

    /**
     * Handles the buttons's events.
     * 
     * @param e
     *            an action event object to handle the buttons's events.
     */
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        if (command.equals(GIPSYGMTOperator.BUTTON_STOP_CONSOLE))
        {           
           
            GlobalInstance.getInstance().closeRunningGMTs();
            MessageAndErrorConsole.getInstance().removeGMTTabs();
            pauseSimulationView();
            // --
            oNodeListPopUpMenu.setEnabled(true);
            oMenuItemStartNode.setEnabled(true);
            oMenuItemRegisterNode.setEnabled(true);
            oMenuItemStopNode.setEnabled(true);

        }
        else if (command.equals(GIPSYGMTOperator.BUTTON_SAVE_SIM))
        {
            saveSimulation();
        }
        else if (command.equals(GIPSYGMTOperator.BUTTON_LOAD_SIM))
        {
            loadSavedGraph();
        }
        else if (command.equals(GIPSYGMTOperator.BUTTON_START_SIM))
        {
            if (!bIsCountinue)
            {
                startAllInstances();
            }
            else
            {
                continueSimulation();
            }           
        }
        else if (command.equals(GIPSYGMTOperator.BUTTON_CLEAR_SIM))
        {
            clearSimulationView();
        }
        else if (command.equals(START_NODE_CMD))
        {
            if (oCurrentSelectedNode != null)
            {
                System.out.println(MSG_PREFIX + " Starting node: "
                        + oCurrentSelectedNode.getNodeName());
                GIPSYCommandRunner oGIPSYCommandRunner = new GIPSYCommandRunner(
                        COMMANDS_TYPE.START_NODE, oCurrentSelectedNode);
                oGIPSYCommandRunner.start();
            }
        }
        else if (command.equals(STOP_NODE_CMD))
        {

        }
        else if (command.equals(REGISTER_NODE_CMD))
        {
            if (oCurrentSelectedNode != null)
            {
                System.out.println(MSG_PREFIX + " Registering node: "
                        + oCurrentSelectedNode.getNodeName());
                GIPSYCommandRunner oGIPSYCommandRunner = new GIPSYCommandRunner(
                        COMMANDS_TYPE.REGISTER_NODE, oCurrentSelectedNode);
                oGIPSYCommandRunner.start();
            }
        }
    }

    private void startAllInstances()
    {
        try
        {
            if (JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to start all nodes and allocate all tiers?",
                    "Start All Instaces", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                System.out.println(MSG_PREFIX
                        + " Starting all nodes and allocating tiers ...");
                this.btnStartSim.setEnabled(false);
                GIPSYCommandRunner oGIPSYCommandRunner = new GIPSYCommandRunner(
                        COMMANDS_TYPE.START_ALL);
                oGIPSYCommandRunner.start();
            }

        }
        catch (Exception e)
        {
            System.err
                    .println("- GIPSYTiersOperator.java: An error has occured while starting all instances");
        }
    }

    public void clearAttackNodes()
    {
        bOperatorView.clearNodesToHighlight();
    }

    /**
     * Refresh the simulation view after each turn.
     */
    public void refreshSimView()
    {
        oSimPanel.removeAll();
        makeGraphView();
        this.repaint();
    }

    public void refreshGraph()
    {

        if (bOperatorView != null)
        {
            bOperatorView.refreshGraphView();
        }
    }

    /**
     * Return the single instance of this class. <br>
     * If the instance is null it will be created.
     * 
     * @return simActivityView_INSTANCE the single instance of this class.
     */
    public static GIPSYGMTOperator getInstance()
    {

        if (simActivityView_INSTANCE == null)
        {
            simActivityView_INSTANCE = new GIPSYGMTOperator();
        }

        return simActivityView_INSTANCE;
    }

    /**
     * Update the node inspection panel with the received object.
     * 
     * @param obj
     *            node name or a node object.
     */
    public void updateStateProperties(Object obj)
    {
        try
        {

            if (obj instanceof String)
            {
                String stateName = (String) obj;
                GIPSYTier oGIPSYTier = GlobalInstance.getInstance()
                        .getGIPSYTierByName(stateName);
                if (oGIPSYTier != null)
                {
                    oActionsLog.showStateProperties(oGIPSYTier);
                }
            }
            else if (obj instanceof GIPSYTier)
            {
                GIPSYTier oGIPSYTier = (GIPSYTier) obj;
                if (oGIPSYTier != null)
                {
                    oActionsLog.showStateProperties(oGIPSYTier);
                }
            }
            this.repaint();

        }
        catch (Exception e)
        {
            System.err
                    .println("- GIPSYTiersOperator.java: An error has occured while updating the selected tier properties.");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Gets the the simulation's tool bar.
     * 
     * @return tlbActivityToolBar containing the simulation's buttons.
     */
    public JToolBar getToolBar()
    {
        return tlbActivityToolBar;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

        if (e.getSource() == oGIPSYTiersList)
        {

            // -- Edit a Continent
            int index = oGIPSYTiersList.locationToIndex(e.getPoint());
            if (index > -1)
            {
                String item = (String) oStatesListModel.getElementAt(index);
                oGIPSYTiersList.ensureIndexIsVisible(index);
                // --
                updateStateProperties(item);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0)
    {

    }

    @Override
    public void mouseExited(MouseEvent arg0)
    {
    }

    @Override
    public void mousePressed(MouseEvent arg0)
    {

    }

    @Override
    public void mouseReleased(MouseEvent arg0)
    {
    }

    class PopupListener extends MouseAdapter
    {
        public void mousePressed(MouseEvent e)
        {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e)
        {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e)
        {
            if (e.isPopupTrigger())
            {

                if (oNodeListPopUpMenu != null)
                {
                    oNodeListPopUpMenu.show(e.getComponent(), e.getX(),
                            e.getY());
                    int iIndex = oGIPSYNodeList.locationToIndex(e.getPoint());
                    if (iIndex >= 0 && iIndex < oGIPSYNodeListModel.getSize())
                    {
                        oCurrentSelectedNode = (GIPSYPhysicalNode) oGIPSYNodeListModel
                                .getElementAt(iIndex);
                        oMenuItemRegisterNode.setEnabled(!oCurrentSelectedNode
                                .isRegistred());
                        oMenuItemStartNode.setEnabled(!oCurrentSelectedNode
                                .isStarted());
                        oMenuItemStopNode.setEnabled(!oCurrentSelectedNode
                                .isStopped());
                    }
                }
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {

    }

}
