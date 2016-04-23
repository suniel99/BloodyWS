/**
 * VertexColor.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 *  
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: ActionsLog.java,v 1.3 2011/08/22 03:38:51 s_rabah Exp $
 */
package gipsy.RIPE.editors.RunTimeGraphEditor.ui;


import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * 
 * A class representing the simulation actions log window.
 * 
 * @author Sleiman Rabah
 * 
 * @version $Revision: 1.3 $ $Date: 2011/08/22 03:38:51 $
 */
public class ActionsLog implements Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 8537999740364267232L;
    /**
     * Text area of the countries actions log.
     */
    private JTextArea txtNodesActionLog;
    /**
     * Text area of the states actions log.
     */
    private JTextArea txtTierActionLog;
    private JTextArea txtGMTActionLog;
    /**
     * The states's inspection window.
     */
    private GIPSYTierProperties oTiersProperties;
    /**
     * The states's inspection panel.
     */
    private JPanel propertiesPanel;
    /**
     * Container for the actions log component.
     */
    private JTabbedPane actionsLog;

    private JPanel actionsLogPanel;
    

    /**
     * Class constructor.
     */
    public ActionsLog()
    {

        initializeComponents();
    }

    /**
     * Create and initialize the actions log panel.
     */
    private void initializeComponents()
    {

        actionsLogPanel = new JPanel();
        actionsLogPanel.setLayout(new BorderLayout());
        actionsLogPanel.setOpaque(true);
        actionsLogPanel.setBorder(BorderFactory
                .createTitledBorder("Actions Log:"));
        actionsLogPanel.setBackground(Color.white);

        actionsLog = new JTabbedPane();
        actionsLog.setPreferredSize(new Dimension(400, 200));
        actionsLog.setMinimumSize(new Dimension(400, 100));
        actionsLog.setOpaque(true);                
        // -- Action log for countries
        actionsLog.addTab("Nodes ", makeNodesLog());
        actionsLog.addTab("Tiers", makeTiersLog());
        actionsLog.addTab("Tier Properties", makeTierProperties());        
        actionsLog.addTab("GMT Console ",makeGMTConsole());

        actionsLog.setFont(new Font("Helvetica", Font.BOLD, 12));
        actionsLogPanel.add(actionsLog);
    }

    private Component makeGMTConsole()
    {
        this.txtGMTActionLog = new JTextArea();
        JScrollPane oScrollPane = new JScrollPane(txtGMTActionLog);
        oScrollPane.setPreferredSize(new Dimension(400, 200));

        return oScrollPane;
    }

    /**
     * Create the states's actions log.
     * 
     * @return scrollPaneState a scroll pane containing the states actions log.
     */
    private Component makeTiersLog()
    {

        txtTierActionLog = new JTextArea();
        JScrollPane scrollPaneState = new JScrollPane(txtTierActionLog);
        scrollPaneState.setPreferredSize(new Dimension(400, 200));

        return scrollPaneState;
    }

    /**
     * Create the countries actions log panel.
     * 
     * @return scrollPane a scroll pane containing the countries actions log.
     */
    private Component makeNodesLog()
    {

        txtNodesActionLog = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txtNodesActionLog);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        return scrollPane;
    }

    /**
     * Update the states's inspection window with the one received as parameter.
     * 
     * @param oGIPSYTier
     *            representing a node of the graph.
     */
    public void showStateProperties(GIPSYTier oGIPSYTier)
    {

        actionsLog.setSelectedIndex(2);
        propertiesPanel.removeAll();
        oTiersProperties = new GIPSYTierProperties(oGIPSYTier);
        propertiesPanel.add(oTiersProperties);

    }

    /**
     * Create the state properties panel and add it to the actions log
     * container.
     * 
     * @return propertiesPanel the state properties panel.
     */
    private Component makeTierProperties()
    {

        propertiesPanel = new JPanel();
        propertiesPanel.setBackground(Color.white);
        propertiesPanel.setOpaque(true);
        propertiesPanel.setLayout(new BorderLayout());
        propertiesPanel.add(new JTextArea(
                "Select a Tier to see its properties."));

        return propertiesPanel;
    }

    /**
     * Append a message to the states's log.
     * 
     * @param msg
     *            message to append to the log.
     */
    public void appendStatesMsg(String msg)
    {
        this.txtTierActionLog.append(msg);
        this.txtTierActionLog.setCaretPosition(this.txtTierActionLog.getText()
                .length());
    }

    /**
     * Append a message to the countries's log.
     * 
     * @param msg
     *            message to append to the log.
     */
    public void appendCountryMsg(String msg)
    {
        this.txtNodesActionLog.append(msg);
        // -- Scroll text after appending
        this.txtNodesActionLog.setCaretPosition(this.txtNodesActionLog
                .getText().length());
    }

    

    public String getCountriesLog()
    {
        return this.txtNodesActionLog.getText();
    }

    public String getTiersLog()
    {
        return this.txtTierActionLog.getText();
    }

    public String getGameLog()
    {
        return this.txtGMTActionLog.getText();
    }

    public void setNodesLog(String msg)
    {
        this.txtNodesActionLog.setText(msg);
    }

    public void setTiersLog(String msg)
    {
        this.txtTierActionLog.setText(msg);
    }

    public void setGameLog(String msg)
    {
        this.txtGMTActionLog.setText(msg);
    }

    /**
     * 
     * @param msg
     */
    private void setCountrycolor(Color color)
    {
        this.txtNodesActionLog.setForeground(color);

    }

    /**
     * Clear the states's log.
     */
    public void clearTiersLog()
    {
        this.txtTierActionLog.setText("");
    }

    /**
     * Clear the countries's log.
     */
    public void clearNodesLog()
    {
        this.txtNodesActionLog.setText("");
    }

    /**
     * Gets the actions log panel.
     * 
     * @return the actionsLog the actions log panel.
     */
    public Component getActionsLog()
    {
        return actionsLogPanel;
    }

    public void updateTiersLog(String msg)
    {
        appendStatesMsg(msg);
    }

    public void updateNodesLog(String msg)
    {
        // setCountrycolor(color);
        appendCountryMsg(msg);
    }

    public void updateGMTConsoleLog(String msg)
    {
        this.txtGMTActionLog.append("GMT> " + msg);
        // -- Scroll text after appending
        this.txtGMTActionLog.setCaretPosition(this.txtGMTActionLog.getText().length());        
    }
}
