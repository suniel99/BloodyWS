/**
 * ContinentsCountriesPanel.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.6 $ $Date: 2012/06/19 23:22:22 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: InstancesNodesPanel.java,v 1.6 2012/06/19 23:22:22 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui;

import gipsy.RIPE.editors.RunTimeGraphEditor.core.AppConstants;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYPhysicalNode;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.dialogs.GIPSYInstanceDialog;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.dialogs.GIPSYNodeDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * A class used in the Map Editor to create a panel which it contains two list:
 * <ul>
 * <li>The list of countries.
 * <li>The list of continents.
 * </ul>
 * 
 * @author Sleiman Rabah
 * 
 */
public class InstancesNodesPanel extends JPanel implements ActionListener,
        MouseListener
{

    /**
	 * 
	 */
    private static final long serialVersionUID = -894738860598627875L;
    private final String REMOVE_COUNTRY_COMMAND = "remove_country";
    private final String ADD_COUNTRY_COMMAND = "add_country";

    // -- Continents list components.
    private JList oGIPSYInstancesList;
    private DefaultListModel oGIPSYInstancesListModel;
    private JButton btnAddInstance;
    private JButton btnRemoveInstance;
    private JPanel instanceListContainer;

    // -- Countries list components.
    private JList oGIPSYNodesList;
    private DefaultListModel oNodesListModel;
    private JButton btnAddNode;
    private JButton btnRemoveNode;
    private JPanel nodeListContainer;
    private JSplitPane splitPane;
    private ColorRenderer listCellRender;

    /**
     * Class constructor.
     */
    public InstancesNodesPanel()
    {
        super(new BorderLayout());
        this.initializeComponents();
    }

    /**
     * Initialize and create the list of countries and continents panel's
     * components.
     */
    private void initializeComponents()
    {

        try
        {

            // -------------------------------------------
            // -- Create and setup GIPSY Instances panel's components.
            // --
            oGIPSYInstancesListModel = new DefaultListModel();
            oGIPSYInstancesList = new JList(oGIPSYInstancesListModel);
            oGIPSYInstancesList.addMouseListener(this);
            oGIPSYInstancesList.setBorder(BorderFactory
                    .createTitledBorder("GIPSY Instances:"));
            oGIPSYInstancesList.setSelectionForeground(Color.black);
            // Container of the JTree and the actions buttons.
            instanceListContainer = new JPanel();
            instanceListContainer.setLayout(new BorderLayout());

            ImageIcon iconAdd = new ImageIcon(AppConstants.ICON_TREE_ADD);
            btnAddInstance = new JButton(iconAdd);
            btnAddInstance.setActionCommand(AppConstants.TREE_ADD_COMMAND);
            btnAddInstance.setToolTipText("Add GIPSY Instance");
            btnAddInstance.addActionListener(this);
            // --
            ImageIcon iconRemove = new ImageIcon(AppConstants.ICON_TREE_REMOVE);
            btnRemoveInstance = new JButton(iconRemove);
            btnRemoveInstance
                    .setActionCommand(AppConstants.TREE_REMOVE_COMMAND);
            btnRemoveInstance.setToolTipText("Remove GIPSY Instance");
            btnRemoveInstance.addActionListener(this);
            // --
            JPanel btnPanel = new JPanel(new GridLayout(0, 2));
            btnPanel.add(btnAddInstance);
            btnPanel.add(btnRemoveInstance);

            // Lay everything out.
            oGIPSYInstancesList.setPreferredSize(new Dimension(100, 150));
            // Create the scroll pane and add the list to it.
            instanceListContainer.add(new JScrollPane(oGIPSYInstancesList),
                    BorderLayout.CENTER);
            instanceListContainer.add(btnPanel, BorderLayout.SOUTH);

            // -------------------------------------------
            // -- Create and setup GIPSY nodes panel's components.
            // --
            listCellRender = new ColorRenderer();
            oNodesListModel = new DefaultListModel();
            oGIPSYNodesList = new JList(oNodesListModel);
            oGIPSYNodesList.setCellRenderer(listCellRender);
            oGIPSYNodesList.addMouseListener(this);
            oGIPSYNodesList.setBorder(BorderFactory
                    .createTitledBorder("GIPSY Nodes:"));
            oGIPSYNodesList.setSelectionForeground(Color.black);
            // Container of the JTree and the actions buttons.
            nodeListContainer = new JPanel();
            nodeListContainer.setLayout(new BorderLayout());

            btnAddNode = new JButton(iconAdd);
            btnAddNode.setActionCommand(ADD_COUNTRY_COMMAND);
            btnAddNode.setToolTipText("Add GIPSY Node");
            btnAddNode.addActionListener(this);
            // --
            btnRemoveNode = new JButton(iconRemove);
            btnRemoveNode.setActionCommand(REMOVE_COUNTRY_COMMAND);
            btnRemoveNode.setToolTipText("Remove GIPSY Node");
            btnRemoveNode.addActionListener(this);
            // --
            JPanel btnCountriesPanel = new JPanel(new GridLayout(0, 2));
            btnCountriesPanel.add(btnAddNode);
            btnCountriesPanel.add(btnRemoveNode);

            // Lay everything out.
            oGIPSYNodesList.setPreferredSize(new Dimension(200, 150));
            // Create the scroll pane and add the list to it.
            nodeListContainer.add(new JScrollPane(oGIPSYNodesList),
                    BorderLayout.CENTER);
            nodeListContainer.add(btnCountriesPanel, BorderLayout.SOUTH);

            // -- Set up the SplitPane
            splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            splitPane.setTopComponent(instanceListContainer);
            splitPane.setBottomComponent(nodeListContainer);
            // splitPane.setBottomComponent(panel);

            Dimension minimumSize = new Dimension(200, 340);
            instanceListContainer.setMinimumSize(minimumSize);
            instanceListContainer.setMinimumSize(minimumSize);
            splitPane.setDividerLocation(200);
            splitPane.setPreferredSize(new Dimension(200, 300));

        }
        catch (Exception e)
        {
            System.out.println("MapItemsTreeController initialization failed.");
            System.out.println(e.getStackTrace());
        }
    }

    /**
     * Add a new continent to the list of continents. <br>
     * pops-up a dialog box in order to get the continent name.
     */
    private void addGIPSYInstance()
    {

        try
        {
            // Append an item
            int lastPosition = oGIPSYInstancesList.getModel().getSize();
            GIPSYInstance oGIPSYInstance;

            GIPSYInstanceDialog dialog = new GIPSYInstanceDialog();
            // If the dialog has been canceled, set the state to null
            // Otherwise get the data from the dialog.
            if (!dialog.isCanceled())
            {
                oGIPSYInstance = dialog.getDialogData();
                if (oGIPSYInstance != null)
                {
                    // give a generated to the new state.
                    String strInstanceID = GlobalInstance.getInstance()
                            .generateId(oGIPSYInstance);
                    oGIPSYInstance.setInstanceID(strInstanceID);
                    GlobalInstance.getInstance().addGIPSYInstance(
                            oGIPSYInstance);
                    GlobalInstance.getInstance().addToHashMap(strInstanceID,
                            oGIPSYInstance);
                    // --
                    oGIPSYInstancesListModel.add(lastPosition, oGIPSYInstance);
                }
            }
            dialog.dispose();
        }
        catch (Exception e)
        {
            System.err
                    .println("An error has occured while adding a continent to the list");
            System.err.println(e.getStackTrace());
        }
    }

    /**
     * Remove a continent from the list of continents.
     */
    private void removeGIPSYInstance()
    {
        try
        {

            // Remove the continent from the GameMap object also.
            int index = oGIPSYInstancesList.getSelectedIndex();

            if (index > -1)
            {
                GIPSYInstance continent = (GIPSYInstance) oGIPSYInstancesListModel
                        .get(index);
                if (JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete this instance: "
                                + continent.getInstanceName() + "?",
                        "Delete GIPSY Instance confirmation",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                {

                    oGIPSYInstancesListModel.remove(oGIPSYInstancesList
                            .getSelectedIndex());
                    // -- Clean up the GameMap Object
                    if (GlobalInstance.getInstance().isItemExists(continent))
                    {
                        GlobalInstance.getInstance().remove(continent);
                        GlobalInstance.getInstance().removeFromHashMap(
                                continent.getInstanceID(), continent);
                    }
                }
            }

        }
        catch (Exception e)
        {
            System.err
                    .println("An error has occured while removing the GIPSY Instance from the list");
            System.err.println(e.getStackTrace());
        }
    }

    /**
     * Edit the properties of a given continent.
     * 
     * @param iContinent
     *            a reference to a continent needed to edit its properties.
     */
    private void editGIPSYInstance(GIPSYInstance iContinent)
    {
        try
        {

            GIPSYInstanceDialog dialog = new GIPSYInstanceDialog(iContinent);

            // If the dialog has been canceled, set the state to null
            // Otherwise get the data from the dialog.
            if (!dialog.isCanceled())
            {

                dialog.getEditedData();
            }
            dialog.dispose();

        }
        catch (Exception e)
        {
            System.err
                    .println("An error has occured while editing the GIPSY Instance");
            System.err.println(e.getStackTrace());
        }
    }

    /**
     * Remove a country from the list of countries.
     */
    private void removeGIPSYNode()
    {

        try
        {

            // Remove the continent from the GameMap object also.
            int index = oGIPSYNodesList.getSelectedIndex();

            if (index > -1)
            {
                GIPSYPhysicalNode country = (GIPSYPhysicalNode) oNodesListModel
                        .get(index);
                if (JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete this GIPSY Node: "
                                + country.getNodeName() + "?",
                        "Delete GIPSY Node confirmation",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                {

                    // -- Clean up the GameMap Object
                    if (GlobalInstance.getInstance().isItemExists(country))
                    {
                        oNodesListModel.remove(oGIPSYNodesList
                                .getSelectedIndex());
                        GlobalInstance.getInstance().remove(country);
                        GlobalInstance.getInstance().removeFromHashMap(
                                country.getNodeID(), country);
                    }
                }
            }

        }
        catch (Exception e)
        {
            System.err
                    .println("An error has occured while removing the selected GIPSY node from the list");
            System.err.println(e.getStackTrace());
        }
    }

    /**
     * Add a country to the countries's list. <br>
     * pops-up a dialog box to get the country name and its color.
     */
    private void addGIPSYNode()
    {

        try
        {
            // Append an item
            int lastPosition = oGIPSYNodesList.getModel().getSize();
            GIPSYPhysicalNode oNewGIPSYNode;

            GIPSYNodeDialog dialog = new GIPSYNodeDialog(lastPosition);
            // If the dialog has been canceled, set the state to null
            // Otherwise get the data from the dialog.
            if (!dialog.isCanceled())
            {
                oNewGIPSYNode = dialog.getEnteredData();
                if (oNewGIPSYNode != null)
                {
                    // give a generated to the new node.
                    String strNodeKey = GlobalInstance.getInstance()
                            .generateId(oNewGIPSYNode);
                    oNewGIPSYNode.setNodeKey(strNodeKey);
                    GlobalInstance.getInstance().addGIPSYPhysicalNode(
                            oNewGIPSYNode);
                    GlobalInstance.getInstance().addToHashMap(strNodeKey,
                            oNewGIPSYNode);
                    // --
                    oNodesListModel.add(lastPosition, oNewGIPSYNode);
                }
            }
            dialog.dispose();
        }
        catch (Exception e)
        {
            System.err
                    .println("An error has occured while adding a country to the list");
            System.err.println(e.getStackTrace());
        }
    }

    /**
     * Edit the properties of a given country.
     * 
     * @param iCountry
     *            a reference to country needed to edit its properties.
     */
    private void editGIPSYNode(GIPSYPhysicalNode iCountry)
    {
        try
        {

            GIPSYNodeDialog dialog = new GIPSYNodeDialog(iCountry);

            // If the dialog has been canceled, set the state to null
            // Otherwise get the data from the dialog.
            if (!dialog.isCanceled())
            {

                dialog.getEditedData();
            }
            dialog.dispose();

        }
        catch (Exception e)
        {
            System.err
                    .println("An error has occured while editing the country");
            System.err.println(e.getStackTrace());
        }
    }

    /**
     * Check what actions are performed on the expandable tree menu
     * 
     * @param e
     *            an event action
     */
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        if (AppConstants.TREE_ADD_COMMAND.equals(command))
        {
            // Add button clicked
            addGIPSYInstance();
        }
        else if (AppConstants.TREE_REMOVE_COMMAND.equals(command))
        {
            // Remove button clicked
            removeGIPSYInstance();
        }
        else if (REMOVE_COUNTRY_COMMAND.equals(command))
        {
            // Remove button clicked
            removeGIPSYNode();
        }
        else if (ADD_COUNTRY_COMMAND.equals(command))
        {
            // Remove button clicked
            addGIPSYNode();
        }

    }

    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == 2)
        {

            if (e.getSource() == oGIPSYInstancesList)
            {

                // -- Edit a Continent
                int index = oGIPSYInstancesList.locationToIndex(e.getPoint());

                if (index > -1)
                {
                    GIPSYInstance item = (GIPSYInstance) oGIPSYInstancesListModel
                            .getElementAt(index);
                    oGIPSYInstancesList.ensureIndexIsVisible(index);
                    // --
                    editGIPSYInstance(item);
                }

            }
            else if (e.getSource() == oGIPSYNodesList)
            {

                // -- Edit a Country
                int index = oGIPSYNodesList.locationToIndex(e.getPoint());

                if (index > -1)
                {
                    GIPSYPhysicalNode item = (GIPSYPhysicalNode) oNodesListModel
                            .getElementAt(index);
                    oGIPSYNodesList.ensureIndexIsVisible(index);
                    // --
                    editGIPSYNode(item);
                }
            }
        }
    }

    public void mouseEntered(MouseEvent arg0)
    {
    }

    public void mouseExited(MouseEvent arg0)
    {
    }

    public void mousePressed(MouseEvent arg0)
    {
    }

    public void mouseReleased(MouseEvent arg0)
    {
    }

    /**
     * Load the continent list with the countries found in the Map.
     */
    public void loadGIPSYInstanceList()
    {
        int liIndex = 0;
        for (GIPSYInstance loGIPSYInstance : GlobalInstance.getInstance()
                .getGIPSYInstancesList())
        {
            oGIPSYInstancesListModel.add(liIndex, loGIPSYInstance);
            liIndex++;
        }
    }

    /**
     * Load the country list with the countries found in the Map.
     */
    public void loadGIPSYNodeList()
    {

        // Load countries
        int index = 0;
        for (GIPSYPhysicalNode country : GlobalInstance.getInstance()
                .getGIPSYNodesList())
        {
            oNodesListModel.add(index, country);
            index++;
        }
    }

    /**
     * Clear the countries list by removing all its elements.
     */
    public void clearGIPSYNodesList()
    {
        oNodesListModel.removeAllElements();
    }

    /**
     * Clear the continents list by removing all its elements.
     */
    public void clearGIPSYInstanceList()
    {
        oGIPSYInstancesListModel.removeAllElements();
    }

    /**
     * Add components to the UI
     */
    public void addComponents()
    {

        // Add the split pane to this panel.
        add(splitPane);
    }

    /**
     * Remove components to the UI
     */
    public void removeCompenents()
    {
        remove(splitPane);
    }

	public int getNodesCount() {
		
		if (oNodesListModel != null) {
			return oNodesListModel.size();
		}
		return 0;
	}

	public int getInstancesCount() {
		
		if (oGIPSYInstancesListModel != null) {
			return oGIPSYInstancesListModel.size();
		}
		
		return 0;
	}
}
