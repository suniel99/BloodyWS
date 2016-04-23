/**
 * SimPanel.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.6 $ $Date: 2012/06/15 02:55:39 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: MainPanel.java,v 1.6 2012/06/15 02:55:39 mokhov Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.Serializable;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * This class represents the main panel of the UI.
 * <p>
 * it contains the Tabbed Panel and also a menu bar.
 * 
 * @author Sleiman Rabah
 * @version $Id: MainPanel.java,v 1.6 2012/06/15 02:55:39 mokhov Exp $
 */
public class MainPanel
extends JPanel
implements Serializable
{
    /**
     * Serialization versionning. 
     */
    private static final long serialVersionUID = 807315188316271554L;
    
    /**
     * Holds the reference to the current tool bar to show.
     */
    private JToolBar oCurrentToolbar;
    
    /**
     * The final Panel which contains the panel to show with its toolbar.
     */
    private JPanel oFinalMainPanel;
    
    /**
     * A tabbed panel used to switch between the Map Editor and other UI's
     * views.
     */
    private JTabbedPane oTabbedPane;

    /**
     * /** Class Constructor.
     */
    public MainPanel()
    {
        super();
        initializeComponents();
    }

    /**
     * Add a component object to to the JTabbedPane container
     * 
     * @param poComponent
     *            Java Swing component
     */
    private void addTab(ToolBarSwitchView poComponent)
    {
        oTabbedPane.add((Component) poComponent);
    }

    /**
     * Initialize swing components of the main panel.
     */
    private void initializeComponents()
    {
        setLayout(new BorderLayout());
        
        // Instantiate the SimPanel tabs
        oTabbedPane = new JTabbedPane();
        oFinalMainPanel = new JPanel(new BorderLayout());

        /**
         * Tab Panel settings
         */
        this.addTab(GIPSYGMTOperator.getInstance());
        this.addTab(MapEditor.getMapEditor_INSTANCE());
        //this.addTab(ConsoleTab.getInstance());

        // An action listener which controls the tool bar menu.
        ChangeListener changeTabMenu = new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                ToolBarSwitchView toolBar = (ToolBarSwitchView) oTabbedPane
                        .getSelectedComponent();
                if (oCurrentToolbar != null)
                {
                    oFinalMainPanel.remove(oCurrentToolbar);
                }
                oCurrentToolbar = toolBar.getToolBar();
                oCurrentToolbar.setOrientation(javax.swing.JToolBar.HORIZONTAL);
                oFinalMainPanel.add(oCurrentToolbar, java.awt.BorderLayout.NORTH);
                // Repaint the panel.
                repaint();
            }
        };
        JSplitPane oSlitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        // --
        oSlitPane.setLeftComponent(oFinalMainPanel);
        // --
        oSlitPane.setRightComponent(MessageAndErrorConsole.getInstance());

        // Set the Divider width.
        oSlitPane.setDividerLocation(650);
        // Provide a preferred size for the split pane.
//        splitPane.setPreferredSize(new Dimension(250, 200));

        this.add(oSlitPane);

        oTabbedPane.addChangeListener(changeTabMenu);        
        oFinalMainPanel.add(oTabbedPane, BorderLayout.CENTER);
        this.add(oSlitPane);

        changeTabMenu.stateChanged(null);
    }

}
