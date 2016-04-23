/**
 * GeoStateChecker.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.4 $ $Date: 2011/08/18 04:17:47 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: GIPSYTierChecker.java,v 1.4 2011/08/18 04:17:47 s_rabah Exp $
 */
package gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.AppConstants;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.NodeConnection;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.AppLogger;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.MessageBoxWrapper;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.mouseplugin.CheckingGraphMousePlugin.VertexChecker;

/**
 * 
 * A class which is used to apply some constraints before creating nodes.
 * 
 * @author Sleiman Rabah
 * 
 */
public class GIPSYTierChecker implements
        VertexChecker<GIPSYTier, NodeConnection>
{

    /**
     * Checks the Country count if less than 2 in order to create a node.
     * 
     * @return boolean which determines if we can create a node or not depending
     *         on the continents count.
     */
    public boolean checkCountryCount(
            VisualizationViewer<GIPSYTier, NodeConnection> poGraphVisualizer)
    {

        if ((GlobalInstance.getInstance().getGIPSYNodesList().size() < AppConstants.MIN_ALLOWD_NODES)
                || (GlobalInstance.getInstance().getGIPSYInstancesList().size() < AppConstants.MIN_ALLOWD_INSTANCES))
        {
            MessageBoxWrapper
                    .displayErrorMsg(
                            poGraphVisualizer,
                            "You must create at least \n -"
                                    + AppConstants.MIN_ALLOWD_INSTANCES
                                    + " GIPSY instance \n -"
                                    + AppConstants.MIN_ALLOWD_NODES
                                    + " GIPSY Node \n before creating an new GIPSY Tier!",
                            "Create New Tier");
            return false;
        }
        return true;
    }

    /**
     * Validate the received state name if it already exists in the graph.
     * 
     * @param poGraph
     *            The graph object which holds the nodes and edges.
     * @param poGraphVisualizer
     *            The graph visualizer.
     * @param pstrTierName
     *            The state name to validate if it exists.
     * @return boolean which determines the received state whether is valid to
     *         create or not.
     */
    public boolean isValidStateName(Graph<GIPSYTier, NodeConnection> poGraph,
            VisualizationViewer<GIPSYTier, NodeConnection> poGraphVisualizer,
            String pstrTierName)
    {
        try
        {
            for (GIPSYTier loGIPSYTier : poGraph.getVertices())
            {
                if (loGIPSYTier.getTierName()
                        .equals(pstrTierName.toUpperCase()))
                {
                    MessageBoxWrapper.displayErrorMsg(poGraphVisualizer,
                            "This tier name exist, chose another one!",
                            "Create New Tier");
                    return false;
                }
            }
        }
        catch (Exception ex)
        {
            MessageBoxWrapper.showException(null,
                    "An error occured while validating the tier name.", ex);
        }
        return true;
    }
}
