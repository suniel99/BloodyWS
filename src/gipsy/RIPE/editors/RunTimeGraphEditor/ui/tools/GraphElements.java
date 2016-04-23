/**
 * GraphElements.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.4 $ $Date: 2012/06/19 23:22:15 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: GraphElements.java,v 1.4 2012/06/19 23:22:15 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools;

import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.NodeConnection;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.dialogs.TierPropertyDialog;

import java.awt.Point;
import org.apache.commons.collections15.Factory;

/**
 * Graph elements factory which creates Nodes/States and Edges/connections
 * between states.
 * <p>
 * The services of this class are called from the graph visualizer of the Map
 * Editor.
 * 
 * @see GraphViewer
 * 
 * @author Sleiman Rabah
 */
public class GraphElements
{

    /**
     * Class Constructor. Creates a new instance of GraphElements
     */
    public GraphElements()
    {
    }

    /**
     * Singleton factory for creating edges between states.
     */
    public static class EdgeFactory implements Factory<NodeConnection>
    {
        /**
         * Edge name which is the edge label, the text to show on the edge.
         */
        private static String strEdgeName;
        /**
         * The id of the state which represents the start of the connection.
         */
        private static String strFromTierID;
        /**
         * The single instance of the Edge Factory.
         */
        private static EdgeFactory oInstance = new EdgeFactory();

        /**
         * The id of the state which represents the end of the connection.
         */
        private static String strToTierID;

        /**
         * Gets the single instance of the Edge Factory which is used to create
         * connection and call the Edge Factory services.
         * 
         * @return The singleton instance of the EdgeFactory.
         */
        public static EdgeFactory getInstance()
        {
            return oInstance;
        }

        /**
         * Sets the connection name which is the edge label.
         * 
         * @param name
         *            the connection label to show on the edge.
         */
        public static void setEdgeName(String name)
        {
            strEdgeName = name;
        }

        /**
         * Sets the id of the state where the connection starts from.
         * 
         * @param fromStateId
         *            the fromStateId to set
         */
        public static void setFromStateId(String fromStateId)
        {
            EdgeFactory.strFromTierID = fromStateId;
        }

        /**
         * Sets the id of the state where the connection ends.
         * 
         * @param toStateId
         *            the toStateId to set
         */
        public static void setToStateId(String toStateId)
        {
            EdgeFactory.strToTierID = toStateId;
        }

        /**
         * Class Constructor.
         */
        private EdgeFactory()
        {
        }

        /**
         * Creates an edge object which represent a link between two states.
         * 
         * @return edge a new created edge with a name and two identifiers.
         */
        public NodeConnection create()
        {
            NodeConnection oNodeConnection = new NodeConnection();
            String edgeId = GlobalInstance.getInstance().generateId(
                    oNodeConnection);
            oNodeConnection.setEdgeId(edgeId);
            oNodeConnection.setName(strEdgeName);
            oNodeConnection.setFromTierId(strFromTierID);
            oNodeConnection.setToTierId(strToTierID);
            GlobalInstance.getInstance().addNodeConnection(oNodeConnection);
            GlobalInstance.getInstance().addToHashMap(edgeId, oNodeConnection);

            return oNodeConnection;
        }
    }

    /**
     * 
     * Single factory for creating Geo. States.
     * 
     * @author Sleiman Rabah
     * 
     */
    public static class GIPSYTierFactory implements Factory<GIPSYTier>
    {

        /**
         * The single instance of the State Factory.
         */
        private static GIPSYTierFactory instance = new GIPSYTierFactory();
        /**
         * The pop-up point of the state creation dialog box.
         */
        private static Point popUpPoint;

        /**
         * @return returns the single instance of the state factory.
         */
        public static GIPSYTierFactory getInstance()
        {
            return instance;
        }

        /**
         * Class Constructor.
         */
        private GIPSYTierFactory()
        {
        }

        /**
         * Creates a GeoState with the data coming from the state creation
         * dialog box.
         * 
         * @return state a state with the data from the input dialog.
         */
        public GIPSYTier create()
        {
            GIPSYTier oGIPSYTier = null;
            // Pops up the state creation dialog.
            TierPropertyDialog dialog = new TierPropertyDialog(popUpPoint);
            // If the dialog has been cancelled, set the state to null
            // Otherwise get the data from the dialog.
            if (!dialog.isCanceled())
            {
                oGIPSYTier = dialog.getDialogData();
                if (oGIPSYTier != null)
                {
                    // give a generated to the new state.
                    String lstrTierId = GlobalInstance.getInstance()
                            .generateId(oGIPSYTier);
                    oGIPSYTier.setTierID(lstrTierId);
                    GlobalInstance.getInstance().addGIPSYTier(oGIPSYTier);
                    GlobalInstance.getInstance().addToHashMap(lstrTierId,
                            oGIPSYTier);
                }
            }
            dialog.dispose();

            return oGIPSYTier;
        }

        /**
         * Sets the position of a node in the graph.
         * 
         * @param x
         *            the horizontal distance of point (x,y)
         * @param y
         *            the vertical distance of point (x,y)
         */
        public void setPoint(int x, int y)
        {
            popUpPoint = new Point(x, y);
        }
    }
}
