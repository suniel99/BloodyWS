package gipsy.RIPE.editors.RunTimeGraphEditor.core;

import gipsy.RIPE.editors.RunTimeGraphEditor.ui.AppLogger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

public class GraphDataManager
{

    private final String INSTANCE_KEY = "instance_key.";
    private final String INSTANCE_NAME = "gipsy.instance.name.";
    private final String INSTANCE_ID = "gipsy.instance.ID.";
    private final String NODE_KEY = "node_key.";
    private final String NODE_NAME = "gipsy.node.name.";
    private final String NODE_IPADDRESS = "gipsy.node.ipaddress.";
    private final String NODE_COLOR = "gipsy.node.color.";
    private final String NODE_ID = "gipsy.node.ID.";
    private final String TIER_KEY = "tier_key.";
    private final String TIER_NAME = "gipsy.tier.name.";
    private final String TIER_HOWMANY = "gipsy.tier.howmany.";
    private final String TIER_TYPE = "gipsy.tier.tiertype.";
    private final String TIER_NODE_ID = "gipsy.tier.node.";
    private final String TIER_INSTANCE_ID = "gipsy.tier.instance.";
    private final String TIER_POSY = "gipsy.tier.posy.";
    private final String TIER_POSX = "gipsy.tier.posx.";
    private final String TIER_IS_GMT = "gipsy.tier.isgmt.";
    private final String TIER_CONFIG_FILE = "gipsy.tier.configfile.";
    private final String TIER_ID = "gipsy.tier.ID.";
    private final String EDGE_KEY = "connection_key.";
    private final String EDGE_NAME = "gipsy.connection.name.";
    private final String EDGE_FROM = "gipsy.connection.from.";
    private final String EDGE_TO = "gipsy.connection.to.";
    private final String EDGE_ID = "gipsy.connection.id.";

    public void loadDataFromFile(String pstrFileName)
    {

        GlobalInstance loGlobalInstance = GlobalInstance.getInstance();

        InputStream loInPropFile = null;
        Properties loProperty = new Properties();

        try
        {
            loInPropFile = new FileInputStream(pstrFileName);
            loProperty.load(loInPropFile);

            // -- Load the instances list.
            Enumeration loEnumProps = loProperty.propertyNames();
            String lstrKey = "";
            while (loEnumProps.hasMoreElements())
            {
                lstrKey = (String) loEnumProps.nextElement();
                if (lstrKey.contains(INSTANCE_KEY))
                {
                    loadGIPSYInstance(loProperty, lstrKey);
                }
                else if (lstrKey.contains(NODE_KEY))
                {
                    loadGIPSYNodes(loProperty, lstrKey);
                }
                else if (lstrKey.contains(TIER_KEY))
                {
                    loadGIPSYTiers(loProperty, lstrKey);
                }
                else if (lstrKey.contains(EDGE_KEY))
                {
                    loadNodeConnection(loProperty, lstrKey);
                }
            }
        }
        catch (IOException ioe)
        {
            System.out.println("I/O Exception.");
            ioe.printStackTrace();
            System.exit(0);
        }
        finally
        {
            try
            {
                loInPropFile.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    private void loadNodeConnection(Properties poProperty, String pstrKey)
    {
        String lstrTierKey = poProperty.getProperty(pstrKey);
        // --
        NodeConnection loNodeConncetion = new NodeConnection();
        // -- Get instance data
        loNodeConncetion.setEdgeId(poProperty
                .getProperty(EDGE_ID + lstrTierKey).trim());
        loNodeConncetion.setFromTierId(poProperty.getProperty(
                EDGE_FROM + lstrTierKey).trim());
        loNodeConncetion.setToTierId(poProperty.getProperty(
                EDGE_TO + lstrTierKey).trim());
        loNodeConncetion.setName(poProperty
                .getProperty(EDGE_NAME + lstrTierKey).trim());
        // --
        GlobalInstance.getInstance().addNodeConnection(loNodeConncetion);
        GlobalInstance.getInstance().addToHashMap(loNodeConncetion.getEdgeId(),
                loNodeConncetion);
    }

    private void loadGIPSYTiers(Properties poProperty, String pstrKey)
    {

        String lstrTierKey = poProperty.getProperty(pstrKey);
        // --
        GIPSYTier loGIPSYTier = new GIPSYTier();
        // -- Get instance data
        loGIPSYTier.setTierName(poProperty.getProperty(TIER_NAME + lstrTierKey)
                .trim());
        loGIPSYTier.setTierID(poProperty.getProperty(TIER_ID + lstrTierKey)
                .trim());
        loGIPSYTier.setHowManyTierPerNode(poProperty.getProperty(
                TIER_HOWMANY + lstrTierKey).trim());
        loGIPSYTier.setConfigFilePath(poProperty.getProperty(
                TIER_CONFIG_FILE + lstrTierKey).trim());
        loGIPSYTier.setGIPSYNodeID(poProperty.getProperty(
                TIER_NODE_ID + lstrTierKey).trim());
        loGIPSYTier.setGIPSYInstanceID(poProperty.getProperty(
                TIER_INSTANCE_ID + lstrTierKey).trim());
        loGIPSYTier.setPosY(Integer.parseInt(poProperty.getProperty(
                TIER_POSY + lstrTierKey).trim()));
        loGIPSYTier.setPosX(Integer.parseInt(poProperty.getProperty(
                TIER_POSX + lstrTierKey).trim()));
        loGIPSYTier.setTierType(poProperty.getProperty(TIER_TYPE + lstrTierKey)
                .trim());
        loGIPSYTier.setIsGMT(Boolean.parseBoolean(poProperty.getProperty(
                TIER_IS_GMT + lstrTierKey).trim()));
        // --
        GlobalInstance.getInstance().addGIPSYTier(loGIPSYTier);
        GlobalInstance.getInstance().addToHashMap(
                loGIPSYTier.getGIPSYInstanceID(), loGIPSYTier);

    }

    private void loadGIPSYNodes(Properties poProperty, String pstrKey)
    {
        String lstrNodeKey = poProperty.getProperty(pstrKey);
        // --
        GIPSYPhysicalNode loGIPSYNode = new GIPSYPhysicalNode();
        // -- Get instance data
        loGIPSYNode.setNodeKey(poProperty.getProperty(pstrKey));
        loGIPSYNode.setNodeID(poProperty.getProperty(NODE_ID
                + loGIPSYNode.getNodeKey()));
        loGIPSYNode
                .setNodeName(poProperty.getProperty(NODE_NAME + lstrNodeKey));
        loGIPSYNode.setNodeColor(poProperty.getProperty(NODE_COLOR
                + lstrNodeKey));
        loGIPSYNode.setIPAddress(poProperty.getProperty(NODE_IPADDRESS
                + lstrNodeKey));
        GlobalInstance.getInstance().addGIPSYPhysicalNode(loGIPSYNode);
        GlobalInstance.getInstance().addToHashMap(loGIPSYNode.getNodeKey(),
                loGIPSYNode);
    }

    private void loadGIPSYInstance(Properties poProperty, String pstrKey)
    {

        String lstrInstanceID = poProperty.getProperty(pstrKey);
        // --
        GIPSYInstance loGIPSYInstance = new GIPSYInstance();
        // -- Get instance data
        loGIPSYInstance.setInstanceID(poProperty.getProperty(pstrKey));
        loGIPSYInstance.setInstanceName(poProperty.getProperty(INSTANCE_NAME
                + loGIPSYInstance.getInstanceID()));
        GlobalInstance.getInstance().addGIPSYInstance(loGIPSYInstance);
        GlobalInstance.getInstance().addToHashMap(
                loGIPSYInstance.getInstanceID(), loGIPSYInstance);
    }

    public void saveDataToFile(String pstrFileName)
    {
        Properties loProperty = new Properties();
        GlobalInstance oGolbalInstance = GlobalInstance.getInstance();

        OutputStream loOutPropFile = null;

        try
        {
            loOutPropFile = new FileOutputStream(pstrFileName);

            // -- Loop over the instances list and dump their data in the
            // property.
            for (GIPSYInstance oGIPSYInstance : oGolbalInstance
                    .getGIPSYInstancesList())
            {
                // -- Dump instances IDs
                loProperty.setProperty(
                        INSTANCE_KEY + oGIPSYInstance.getInstanceID(),
                        oGIPSYInstance.getInstanceID());

                // -- Dump instances data
                loProperty.setProperty(
                        INSTANCE_NAME + oGIPSYInstance.getInstanceID(),
                        oGIPSYInstance.getInstanceName());
                loProperty.setProperty(
                        INSTANCE_ID + oGIPSYInstance.getInstanceID(),
                        oGIPSYInstance.getInstanceID());
            }

            // -- Loop over the tiers list and dump their data in the property.
            for (GIPSYTier oGIPSYTier : oGolbalInstance.getGIPSYTiersList())
            {
                // -- Dump tiers IDS
                loProperty.setProperty(TIER_KEY + oGIPSYTier.getTierID(),
                        oGIPSYTier.getTierID());

                // -- Dump tiers data
                loProperty.setProperty(TIER_NAME + oGIPSYTier.getTierID(),
                        oGIPSYTier.getTierName());
                loProperty.setProperty(TIER_HOWMANY + oGIPSYTier.getTierID(),
                        oGIPSYTier.getHowManyTierPerNode());
                loProperty.setProperty(TIER_TYPE + oGIPSYTier.getTierID(),
                        oGIPSYTier.getTierType());
                loProperty.setProperty(TIER_NODE_ID + oGIPSYTier.getTierID(),
                        oGIPSYTier.getGIPSYNodeID());
                loProperty.setProperty(
                        TIER_INSTANCE_ID + oGIPSYTier.getTierID(),
                        oGIPSYTier.getGIPSYInstanceID());
                loProperty.setProperty(TIER_POSY + oGIPSYTier.getTierID(),
                        oGIPSYTier.getPosY() + " ");
                loProperty.setProperty(TIER_POSX + oGIPSYTier.getTierID(),
                        oGIPSYTier.getPosX() + " ");
                loProperty.setProperty(TIER_IS_GMT + oGIPSYTier.getTierID(),
                        oGIPSYTier.isGMT() + " ");
                loProperty.setProperty(
                        TIER_CONFIG_FILE + oGIPSYTier.getTierID(),
                        oGIPSYTier.getConfigFilePath());
                loProperty.setProperty(TIER_ID + oGIPSYTier.getTierID(),
                        oGIPSYTier.getTierID());
            }

            // -- Loop over the nodes list and dump their data in the property.
            for (GIPSYPhysicalNode oGIPSYNode : oGolbalInstance
                    .getGIPSYNodesList())
            {
                // -- Dump nodes IDs
                loProperty.setProperty(NODE_KEY + oGIPSYNode.getNodeID(),
                        oGIPSYNode.getNodeID());

                // -- Dump instances data
                loProperty.setProperty(NODE_NAME + oGIPSYNode.getNodeID(),
                        oGIPSYNode.getNodeName());
                loProperty.setProperty(NODE_IPADDRESS + oGIPSYNode.getNodeID(),
                        oGIPSYNode.getIPAddress());
                loProperty.setProperty(NODE_COLOR + oGIPSYNode.getNodeID(),
                        oGIPSYNode.getNodeColor());
                loProperty.setProperty(NODE_ID + oGIPSYNode.getNodeID(),
                        oGIPSYNode.getNodeID());
            }
            // -- Loop over the connection list and dump their data in the
            // property.
            for (NodeConnection oNodeConnection : oGolbalInstance
                    .getNodesConnectionList())
            {
                // -- Dump nodes IDs
                loProperty.setProperty(EDGE_KEY + oNodeConnection.getEdgeId(),
                        oNodeConnection.getEdgeId());

                // -- Dump instances data
                loProperty.setProperty(EDGE_NAME + oNodeConnection.getEdgeId(),
                        oNodeConnection.getName());
                loProperty.setProperty(EDGE_FROM + oNodeConnection.getEdgeId(),
                        oNodeConnection.getFromTierId());
                loProperty.setProperty(EDGE_TO + oNodeConnection.getEdgeId(),
                        oNodeConnection.getToTierId());
                loProperty.setProperty(EDGE_ID + oNodeConnection.getEdgeId(),
                        oNodeConnection.getEdgeId());
            }

            loProperty.store(loOutPropFile, "Graph data");

        }
        catch (IOException ioe)
        {
            System.out.println("I/O Exception.");
            ioe.printStackTrace();
            AppLogger
                    .printMsg("An error occured whil trying to save the Graph: \n");
            AppLogger.printMsg(ioe.getMessage());
        }
        finally
        {
            try
            {
                loOutPropFile.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        AppLogger.consoleSubAction("File name " + pstrFileName);
        AppLogger.consoleSubAction("Graph saved successfully.");
    }

}
