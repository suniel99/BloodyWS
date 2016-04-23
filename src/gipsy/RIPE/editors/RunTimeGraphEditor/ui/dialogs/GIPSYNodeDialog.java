/**
 * GIPSYNodeDialog.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 *   
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: GIPSYNodeDialog.java,v 1.7 2012/06/20 01:05:26 s_rabah Exp $
 */
package gipsy.RIPE.editors.RunTimeGraphEditor.ui.dialogs;

import gipsy.RIPE.editors.RunTimeGraphEditor.ApplicationStarter;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.AppConstants;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYPhysicalNode;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.ColorRenderer;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.GIPSYColors;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.MessageBoxWrapper;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A class used to create a dialog box for counties creation. <br>
 * Also this class is used to edit a given country's properties.
 * 
 * @author Sleiman Rabah
 * 
 * @version $Revision: 1.7 $ $Date: 2012/06/20 01:05:26 $
 */
public class GIPSYNodeDialog extends JDialog implements ActionListener
{

    private JButton btnCancel;
    private JButton btnSave;
    private boolean bIsCanceled;
    private boolean bIsNewNode;
    private JTextField txtNodeName;
    private JTextField txtNodeID;
    private JTextField txtIPAddress;
    private GIPSYPhysicalNode oGIPSYNode;

    /**
     * A combobox where to show the colors list.
     */
    private JComboBox oNodesColorComboBox;
    /**
     * A label to display a color chosen by the user.
     */
    private JLabel oColorLabel;

    /**
     * Class constructor.
     */
    public GIPSYNodeDialog(int piNodeCount)
    {

        super(ApplicationStarter.getMainFrame(), true);
        this.bIsCanceled = false;
        this.oGIPSYNode = null;
        bIsNewNode = true;       
        // --
        this.initComponents(piNodeCount +"");
    }

    public GIPSYNodeDialog(GIPSYPhysicalNode pGIPSYNode)
    {
        super(ApplicationStarter.getMainFrame(), true);

        this.oGIPSYNode = pGIPSYNode;
        this.bIsCanceled = false;
        bIsNewNode = false;
        // --
        this.initComponents(pGIPSYNode.getNodeID());
    }

    /**
     * Creates and initializes the UI components.
     */
    private void initComponents(String piNodeID)
    {

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(
                AppConstants.ICON_MAIN_FRMAE));

        

        oColorLabel = new JLabel("     ");
        oColorLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        oColorLabel.setOpaque(true);
        txtNodeName = new JTextField();
        txtNodeName.setSize(20, 25);
        // --
        txtIPAddress = new JTextField();
        txtIPAddress.setSize(20, 25);
        try
        {
            InetAddress oAddress = InetAddress.getLocalHost();
            txtIPAddress.setText(oAddress.getHostAddress());

        }
        catch (UnknownHostException e1)
        {
            e1.printStackTrace();
        }
        // --
        txtNodeID = new JTextField();
        txtNodeID.setSize(20, 25);
        txtNodeID.setText(piNodeID);
        txtNodeID.setEnabled(false);
        /**
         * Load node info for editing.
         */
        if (this.oGIPSYNode != null)
        {
            txtNodeName.setText(this.oGIPSYNode.getNodeName());
            txtIPAddress.setText(this.oGIPSYNode.getIPAddress());
            txtNodeID.setText(this.oGIPSYNode.getNodeID());
            setTitle("Editing GIPSY Node: " + this.oGIPSYNode.getNodeName());
            oNodesColorComboBox = new JComboBox(GIPSYColors.GIPSYNodesColorsList);
            oNodesColorComboBox.addActionListener(this);
            oNodesColorComboBox.setSelectedItem(new String(this.oGIPSYNode
                    .getNodeColor()));
            updateColorLabel(this.oGIPSYNode.getNodeColor());

        }
        else
        {

            oNodesColorComboBox = new JComboBox(GIPSYColors.GIPSYNodesColorsList);
            oNodesColorComboBox.addActionListener(this);
            setTitle("Create New GIPSY Node");
        }

        btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // get the inputed data.
                bIsCanceled = false;
                showValidateDialog();

            }
        });
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // Dispose the state creation dialog.
                bIsCanceled = true;
                setVisible(false);
            }
        });
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        this.pack();
        this.add(buildPanel());
        this.setSize(520, 300);
        this.setLocationRelativeTo(ApplicationStarter.getMainFrame());
        this.setVisible(true);

    }

    /**
     * Builds the panel which holds the dialog's components.
     * 
     * @return the built panel
     */
    public JComponent buildPanel()
    {
        JPanel panel = null;
        try
        {
            FormLayout layout = new FormLayout(
                    "right:[40dlu,pref], 3dlu, 70dlu, 7dlu, "
                            + "right:[40dlu,pref], 3dlu, 70dlu",
                    "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, "
                            + "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, "
                            + "p, 3dlu, p, 3dlu, p, 3dlu, p");

            PanelBuilder builder = new PanelBuilder(layout);
            builder.setDefaultDialogBorder();

            // Fill the table with labels and components.
            CellConstraints cc = new CellConstraints();
            builder.addSeparator("GIPSY Node Properties:", cc.xyw(1, 1, 7));
            builder.addLabel("Node Name:", cc.xy(1, 3));
            builder.add(txtNodeName, cc.xyw(3, 3, 5));
            builder.addLabel("Node ID:", cc.xy(1, 5));
            builder.add(txtNodeID, cc.xyw(3, 5, 5));
            builder.addLabel("IP Address:", cc.xy(1, 7));
            builder.add(txtIPAddress, cc.xyw(3, 7, 5));
            builder.addLabel("Color:", cc.xy(1, 9));
            builder.add(oNodesColorComboBox, cc.xyw(3, 9, 5));
            builder.addLabel("Selected Color:", cc.xy(1, 11));
            builder.add(oColorLabel, cc.xy(3, 11));
            builder.add(btnSave, cc.xyw(3, 13, 1));
            builder.add(btnCancel, cc.xy(7, 13));

            panel = builder.getPanel();
            panel.setSize(200, 150);
        }
        catch (Exception ex)
        {
            MessageBoxWrapper.showException(null,
                    "An error occured while building the GIPSY Nodes panel ",
                    ex);
        }

        return panel;
    }

    /**
     * Update the color label with the picked color in the combo box.
     * 
     * @param color
     *            a string representing the choice of the user.
     */
    private void updateColorLabel(String color)
    {
        oColorLabel.setBackground(ColorRenderer.getColorCode(color));
    }

    /**
     * 
     * @return boolean which tells if the dialog has been canceled or not.
     */
    public boolean isCanceled()
    {
        return this.bIsCanceled;
    }

    /**
     * 
     * @return boolean which determines whether if the data in the dialog box is
     *         valid or not.
     */
    private boolean isValidGIPSYNode()
    {
        boolean bIsValid = true;

        if (txtNodeName.getText().trim().isEmpty())
        {
            MessageBoxWrapper.displayErrorMsg(null,
                    "You must enter a node name!", this.getTitle());
            bIsValid = false;
            txtNodeName.requestFocus();
        }
        else if (txtNodeID.getText().trim().isEmpty())
        {
            MessageBoxWrapper.displayErrorMsg(null,
                    "You must enter a node ID!", this.getTitle());
            bIsValid = false;
            txtNodeID.requestFocus();
        }
        else if (!isValidIPAddress(txtIPAddress.getText().trim()))
        {
            MessageBoxWrapper.displayErrorMsg(null,
                    "You entered an ivalid IP address!", this.getTitle());
            bIsValid = false;
            txtIPAddress.requestFocus();
        }
        else if (((String) oNodesColorComboBox.getSelectedItem())
                .equals("Please Select"))
        {
            MessageBoxWrapper.displayErrorMsg(null, "You must select a color!",
                    this.getTitle());
            bIsValid = false;
            oNodesColorComboBox.requestFocus();
        }
        else if (GlobalInstance.getInstance().getGIPSYNodeByName(
                txtNodeName.getText().trim()) != null
                && bIsNewNode)
        {
            MessageBoxWrapper
                    .displayErrorMsg(null,
                            "The node name you entered exist already!",
                            this.getTitle());
            bIsValid = false;
            txtNodeName.requestFocus();
        }
        else if (GlobalInstance.getInstance().getGIPSYNodeById(
                txtNodeID.getText().trim()) != null
                && bIsNewNode)
        {
            MessageBoxWrapper.displayErrorMsg(null,
                    "The node ID you entered exist already!", this.getTitle());
            bIsValid = false;
            txtNodeName.requestFocus();
        }

        if (!bIsNewNode)
        {
            String lstrErrorMessage = "Please correct the following errors: ";
            // Validates node ID if it already exists
            GIPSYPhysicalNode loGIPSYNodeToUpdate = GlobalInstance
                    .getInstance()
                    .getGIPSYNodeById(this.oGIPSYNode.getNodeID());
            GIPSYPhysicalNode loExistingGIPSYNode = GlobalInstance
                    .getInstance().getGIPSYNodeById(txtNodeID.getText().trim());
            if (loExistingGIPSYNode != null
                    && loExistingGIPSYNode != loGIPSYNodeToUpdate)
            {
                lstrErrorMessage += "\n- The node ID you entered exist already!";
                bIsValid = false;
                txtNodeID.requestFocus();
            }
            // Validates node name if it already exists
            loGIPSYNodeToUpdate = GlobalInstance.getInstance()
                    .getGIPSYNodeByName(this.oGIPSYNode.getNodeName());
            loExistingGIPSYNode = GlobalInstance.getInstance()
                    .getGIPSYNodeByName(txtNodeName.getText().trim());
            if (loExistingGIPSYNode != null
                    && loExistingGIPSYNode != loGIPSYNodeToUpdate)
            {
                lstrErrorMessage += "\n- The node name you entered exist already!";
                bIsValid = false;
                txtNodeName.requestFocus();
            }
            if (!bIsValid)
            {
                MessageBoxWrapper.displayErrorMsg(null, lstrErrorMessage,
                        this.getTitle());
            }

        }

        if (bIsValid)
        {
            try
            {
                Integer.parseInt(txtNodeID.getText().trim());
            }
            catch (NumberFormatException e)
            {
                MessageBoxWrapper.displayErrorMsg(null,
                        "The Node ID must be an integer!", this.getTitle());
                return false;
            }
        }

        return bIsValid;
    }

    /**
     * Display the input dialog while the data is not valid.
     */
    private void showValidateDialog()
    {
        if (isValidGIPSYNode())
        {
            setVisible(false);
        }
    }

    /**
     * Update the color label with the one coming from the combo box.
     * 
     * @param e
     *            an ActionEvent object.
     */
    public void actionPerformed(ActionEvent e)
    {

        JComboBox cb = (JComboBox) e.getSource();
        String color = (String) cb.getSelectedItem();
        updateColorLabel(color);
    }

    /**
     * Gets the data entered in the dialog box and update the properties of the
     * country with it.
     */
    public void getEditedData()
    {

        if (isValidGIPSYNode())
        {

            this.oGIPSYNode.setNodeName((String) txtNodeName.getText()
                    .toUpperCase());

            this.oGIPSYNode.setNodeID((String) txtNodeID.getText()
                    .toUpperCase());

            this.oGIPSYNode.setIPAddress((String) txtIPAddress.getText());
            this.oGIPSYNode.setNodeColor((String) oNodesColorComboBox
                    .getSelectedItem());
        }
    }

    /**
     * Creates a GeoState object, extract and validate the data from entered in
     * the dialog box.
     * 
     * @return GeoState object which contains the data from the dialog box.
     */
    public GIPSYPhysicalNode getEnteredData()
    {

        GIPSYPhysicalNode oGIPSYNode = null;
        if (isValidGIPSYNode())
        {
            oGIPSYNode = new GIPSYPhysicalNode();
            oGIPSYNode
                    .setNodeName((String) txtNodeName.getText().toUpperCase());
            oGIPSYNode.setIPAddress((String) txtIPAddress.getText()
                    .toUpperCase());
            oGIPSYNode.setNodeID((String) txtNodeID.getText().toUpperCase());
            oGIPSYNode.setNodeColor((String) oNodesColorComboBox
                    .getSelectedItem());
        }

        return oGIPSYNode;
    }

    private boolean isValidIPAddress(String pstrIPAddress)
    {
        boolean bIsValid = true;

        if (pstrIPAddress.isEmpty())
        {
            return false;
        }

        String[] strAddressParts = pstrIPAddress.split("\\.");

        if (strAddressParts.length < 4)
        {
            return false;
        }

        try
        {
            for (String strPart : strAddressParts)
            {
                int i = Integer.parseInt(strPart);
                if (i < 0 || i > 255)
                {
                    bIsValid = false;
                }
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(
                    this,
                    "Error parsing the entered IP address! \n error: "
                            + e.getMessage(), this.getTitle(), 0,
                    new ImageIcon(AppConstants.ICON_ERROR));
            bIsValid = false;
        }
        return bIsValid;
    }
}
