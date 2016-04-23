/**
 * StateProperties.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.3 $ $Date: 2012/06/21 19:50:29 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: GIPSYTierProperties.java,v 1.3 2012/06/21 19:50:29 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui;

import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYPhysicalNode;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A class used to create a panel with a node properties.
 * 
 * @author s_rabah
 */
public class GIPSYTierProperties extends JPanel
{

    private GIPSYTier oGIPSYTier;

    /**
     * Class constructor.
     */
    public GIPSYTierProperties()
    {
        super();
        this.setLayout(new BorderLayout());
    }

    /**
     * Class constructor.
     * 
     * @param ipGIPSYTier
     *            a reference to a graph's node.
     */
    public GIPSYTierProperties(GIPSYTier ipGIPSYTier)
    {
        super();

        this.oGIPSYTier = ipGIPSYTier;
        initComponents();
        this.setOpaque(true);

    }

    /**
     * Initialize the panel components.
     */
    private void initComponents()
    {

        // -- Panel related setting
        this.setLayout(new BorderLayout());
        this.add(makeStateProperties());
    }

    /**
     * Create an inspection panel of a given node.
     * 
     * @return panel the node's properties panel.
     */
    private JComponent makeStateProperties()
    {

        JPanel panel = null;

        try
        {
            GlobalInstance oAppSingleton = GlobalInstance.getInstance();
            JCheckBox isCity;
            JLabel lblGIPSYNode = new JLabel();
            lblGIPSYNode.setOpaque(true);
            isCity = new JCheckBox();
            isCity.setEnabled(false);
            isCity.setSelected(oGIPSYTier.isGMT());
            // --
            GIPSYInstance oGIPSYInstance = oAppSingleton
                    .getGIPSYInstanceById(oGIPSYTier.getGIPSYInstanceID());
            GIPSYPhysicalNode oGIPSYNode = oAppSingleton
                    .getGIPSYNodeById(oGIPSYTier.getGIPSYNodeID());

            FormLayout layout = new FormLayout(
                    "left:[40dlu,pref], 3dlu, 70dlu, 7dlu, "
                            + "right:[40dlu,pref], 3dlu, 70dlu",
                    "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, "
                            + "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, "
                            + "p, 3dlu, p, 3dlu, p, 3dlu, p");

            PanelBuilder builder = new PanelBuilder(layout);
            builder.setDefaultDialogBorder();

            lblGIPSYNode.setText(oGIPSYNode.getNodeName());

            // Fill the table with labels and components.
            CellConstraints cc = new CellConstraints();
            builder.addSeparator("Tier Properties ", cc.xyw(1, 1, 7));
            builder.addLabel("Tier Name:", cc.xy(1, 3));
            builder.addLabel(oGIPSYTier.getTierName(), cc.xy(3, 3));
            builder.addLabel("Tier Type:", cc.xy(5, 3));
            builder.addLabel(oGIPSYTier.getTierType(), cc.xy(7, 3));
            builder.addLabel("Is GMT:", cc.xy(1, 5));
            builder.add(isCity, cc.xy(3, 5));
    		builder.addLabel("How many:", cc.xy(5,5 ));
    		builder.addLabel(oGIPSYTier.getHowManyTierPerNode(), cc.xy(7, 5));
            builder.addLabel("Config File: " + oGIPSYTier.getConfigFilePath(),
                    cc.xyw(1, 7, 7));

            // -- Node info
            builder.addSeparator("Node Info: ", cc.xyw(1, 9, 7));
            builder.addLabel("Node Name:", cc.xy(1, 11));
            builder.addLabel(oGIPSYNode.getNodeName(), cc.xy(3, 11));
            builder.addLabel("Node ID:", cc.xy(5, 11));
            builder.addLabel(oGIPSYNode.getNodeID(), cc.xy(7, 11));
            builder.addLabel("IP Address:", cc.xy(1, 13));
            builder.addLabel(oGIPSYNode.getIPAddress(), cc.xy(3, 13));

            // -- Instance properties
            builder.addSeparator("Instance info: ", cc.xyw(1, 17, 7));
            builder.addLabel("Instance Name:", cc.xy(1, 19));
            builder.addLabel(oGIPSYInstance.getInstanceName(), cc.xy(3, 19));
            builder.addLabel("Instance ID:", cc.xy(5, 19));
            builder.addLabel(oGIPSYInstance.getInstanceName(), cc.xy(7, 19));

            panel = builder.getPanel();
            panel.setPreferredSize(new Dimension(200, 150));

        }
        catch (Exception e)
        {
            System.err
                    .println("An error occured while loading the tier properties."
                            + e.getMessage());
        }
        return panel;
    }
}
