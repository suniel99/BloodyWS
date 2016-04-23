/**
 * ConnectionChecker.java
 * 
 * GIPSY Project.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/08/18 04:17:47 $
 * 
 * Copyright(c) 2011, GIPSY Team.
 * 
 * $Id: ConnectionChecker.java,v 1.3 2011/08/18 04:17:47 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.NodeConnection;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.MessageBoxWrapper;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.mouseplugin.CheckingGraphMousePlugin.EdgeChecker;

/**
 * A class used to check/validate a connection between two nodes.
 * 
 * @author s_rabah
 */
public class ConnectionChecker implements
        EdgeChecker<GIPSYTier, NodeConnection>
{

    /**
     * Check if the edge is valid to create. An edge connect two nodes, in case
     * of error, a pop-up dialog will pop up displaying a customized error
     * message.
     * 
     * <ul>
     * <li>Check if the connection triggered on the node itself.</li>
     * <li>Check for parallel connections.</li>
     * </ul>
     */
    public boolean checkEdge(Graph<GIPSYTier, NodeConnection> poGraph,
            VisualizationViewer<GIPSYTier, NodeConnection> poGraphVisualizer,
            NodeConnection poEdge, GIPSYTier poFromNode, GIPSYTier poToNode,
            EdgeType peDir)
    {
        if (peDir == EdgeType.DIRECTED)
        {
            MessageBoxWrapper
                    .displayErrorMsg(poGraphVisualizer,
                            "No Directed connection allowed in this map!",
                            "Edge Check");
            return false;
        }
        else if (poFromNode == poToNode)
        {
            MessageBoxWrapper.displayErrorMsg(poGraphVisualizer,
                    "No self connection allowed in this map!",
                    "Connection Check");
            return false;
        }
        else if (poGraph.findEdge(poFromNode, poToNode) != null)
        {
            MessageBoxWrapper.displayErrorMsg(poGraphVisualizer,
                    "No parallel connections allowed in this map!",
                    "Connection Check");
            return false;
        }
        else if (poFromNode.getTierType().equalsIgnoreCase(
                poToNode.getTierType()))
        {
            MessageBoxWrapper.displayErrorMsg(poGraphVisualizer,
                    "Two nodes having the same tier type cannot be connected!",
                    "Connection Check");
            return false;
        }
        return true;
    }
}
