/**
 * AppConstants.java
 * 
 * GIPSY project.
 * 
 * @version $Revision: 1.6 $ $Date: 2012/06/19 23:22:18 $
 * 
 * Copyright(c) 2010, GIPSY Team.
 * 
 * $Id: AppConstants.java,v 1.6 2012/06/19 23:22:18 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.core;

/**
 * A class which holds the application constants.
 * <p>
 * <b>Section 1. Global Project Information</b>
 * 
 * @author Sleiman Rabah
 * @version $Id: AppConstants.java,v 1.6 2012/06/19 23:22:18 s_rabah Exp $
 */
public class AppConstants
{

    public static final String PROJ_VERSION = "Version: Build 2";
    public static final String PLEASE_SELECT = "Please Select";

    /**
     * 3.5 Constants declaration for Strategic Gathering Stage
     * <p>
     * <ul>
     * <li>TIER_STATE_RUNNING
     * <li>TIER_STATE_STOPPED
     * <li>NODE_STATE_REGISTRED
     * </ul>
     */
    public static final int TIER_STATE_RUNNING = 0;
    public static final int TIER_STATE_STOPPED = 1;
    public static final int NODE_STATE_REGISTRED = 2;

    /**
     * The minimum number of instances required for before tier creation.
     */
    public static final int MIN_ALLOWD_INSTANCES = 1;
    public static final int MIN_ALLOWD_NODES = 1;
    public static final String[] TIER_TYPES =
    { "Please Select", "DWT", "GMT", "DGT", "DST" };

    public static final String SAVED_GRAPHS_FILE_EXT = ".config";
    public static final String CONFIG_FILE_DESCR = "Config Files (*.config)";
    public static final String GAME_FILE_EXT = ".saves";
    public static final String CONFIG_FILE_EXT = ".config";
    public static final String DST_CONFIG_FILE_DIR = "bin/multitier/DSTProfiles/";
    public static final String CONFIG_FILE_DIR = "bin/multitierEclipse/";

    /**
     * 2.1 Constants of declaration for Frame info
     * <p>
     * <ul>
     * <li>FRAME_TITLE
     * <li>ICON_ENCS_LOGO
     * <li>ICON_ERROR
     * <li>ICON_LOAD_MAP
     * </ul>
     */
    public static final String FRAME_TITLE = "GIPSY - Graph-Operated GMT";
    public static final String ICON_ENCS_LOGO = "res/ENCS-logo07.gif";
    public static final String ICON_ERROR = "res/dialog-error.png";
    public static final String ICON_WARNING = "res/dialog-warning.png";
    public static final String ICON_LOAD_MAP = "res/document-open.png";

    /**
     * 2.2 Constants of declaration for Icons path
     * <p>
     * <ul>
     * <li>ICON_MAIN_FRMAE
     * <li>ICON_MAP_CLOSE
     * <li>ICON_MAP_EDITOR
     * <li>ICON_MAP_HELP
     * <li>ICON_MAP_PRINT
     * <li>ICON_MAP_TRASH
     * <li>ICON_MAP_ZOOM_IN
     * <li>ICON_MAP_ZOOM_OUT
     * </ul>
     */
    public static final String ICON_MAIN_FRMAE = "res/system-software-update.png";
    public static final String ICON_MAP_CLOSE = "res/process-stop.png";
    public static final String ICON_MAP_EDITOR = "res/internet-web-browser.png";
    public static final String ICON_MAP_HELP = "res/help-browser.png";
    public static final String ICON_MAP_PRINT = "res/printer.png";
    public static final String ICON_MAP_TRASH = "res/user-trash.png";
    public static final String ICON_MAP_ZOOM_IN = "res/Zoom-In-icon.png";
    public static final String ICON_MAP_ZOOM_OUT = "res/Zoom-Out-icon.png";

    /**
     * 2.3 Constants of declaration for Map control
     * <p>
     * <ul>
     * <li>ICON_NEW_MAP
     * <li>ICON_SAVE_MAP
     * <li>ICON_NEW_MAP
     * <li>ICON_SAVE_MAP
     * </ul>
     */
    public static final String ICON_TREE_REMOVE = "res/list-remove.png";
    public static final String ICON_TREE_ADD = "res/list-add.png";
    public static final String ICON_NEW_MAP = "res/document-new.png";
    public static final String ICON_SAVE_MAP = "res/save.png";

    /**
     * 2.4 Constants declaration for the map's files directory name
     * <p>
     * <ul>
     * <li>MAP_DIR
     * <li>MAP_EDITOR_INSTRUCTIONS
     * <li>MAP_FILE_DESCR
     * <li>MAP_FILE_EXT
     * </ul>
     */
    public static final String GAME_DIR = "games/";
    public static final String SAVED_GRAPHS_DIR = "savedgraphs/";

    public static final String MAP_EDITOR_INSTRUCTIONS = "<html>"
            + "<body>"
            + "<h3>All Modes:</h3>"
            + "<ul>"
            + "	<li>Right-click an empty area for <b>Create Vertex</b> popup"
            + "	<li>Right-click on a Vertex for <b>Delete Vertex</b> popup"
            + "	<li>Right-click on a Vertex for <b>Add Edge</b> menus <br>(if there are selected Vertices)"
            + "	<li>Right-click on an Edge for <b>Delete Edge</b> popup"
            + "	<li>Mousewheel scales with a crossover value of 1.0.<p>"
            + "     - scales the graph layout when the combined scale is greater than 1<p>"
            + "     - scales the graph view when the combined scale is less than 1"
            + "</ul>"
            + "<h3>Editing Mode:</h3>"
            + "<ul>"
            + "	<li>Double-click an empty area to create a <b>new State </b>"
            + "	<li>Left-click on a State/Node and drag to another Vertex to create an Undirected <b>Edge</b>"
            + "</ul>"
            + "<h3>Picking Mode:</h3>"
            + "<ul>"
            + "	<li>Mouse1 on a Vertex selects the vertex"
            + "	<li>Mouse1 elsewhere unselects all Vertices"
            + "	<li>Mouse1+Shift on a Vertex adds/removes Vertex selection"
            + "	<li>Mouse1+drag on a Vertex moves all selected Vertices"
            + "	<li>Mouse1+drag elsewhere selects Vertices in a region"
            + "	<li>Mouse1+Shift+drag adds selection of Vertices in a new region"
            + "	<li>Mouse1+CTRL on a Vertex selects the vertex and centers the display on it"
            + "	<li>Mouse1 double-click on a vertex or edge allows you to edit the label"
            + "</ul>"
            + "<h3>Transforming Mode:</h3>"
            + "<ul>"
            + "	<li>Mouse1+drag pans the graph"
            + "	<li>Mouse1+Shift+drag rotates the graph"
            + "	<li>Mouse1+CTRL(or Command)+drag shears the graph"
            + "	<li>Mouse1 double-click on a vertex or edge allows you to edit the label"
            + "</ul>" + "</body>" + "</html>";

    /**
     * 2.5 Constants declaration for the simulation's files directory name
     * <p>
     * <ul>
     * <li>SIM_SAVE_DIR
     * <li>TREE_ADD_COMMAND
     * <li>TREE_CLEAR_COMMAND
     * <li>TREE_REMOVE_COMMAND
     * </ul>
     */
    public static final String SIM_SAVE_DIR = "simulations/";
    public static final String TREE_ADD_COMMAND = "add";
    public static final String TREE_CLEAR_COMMAND = "clear";
    public static final String TREE_REMOVE_COMMAND = "remove";
 }
