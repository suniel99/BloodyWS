/**
 * SimFrame.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.5 $ $Date: 2012/06/15 02:55:39 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: MainFrame.java,v 1.5 2012/06/15 02:55:39 mokhov Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui;

import gipsy.RIPE.editors.RunTimeGraphEditor.core.AppConstants;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

/**
 * A class representing the GUI main Frame. It contains a toolbar and a Tabbed
 * pane.
 * 
 * @author Sleiman Rabah
 */
public class MainFrame
extends JFrame
implements Serializable
{
    /**
     * For serialization versioning.
     */
    private static final long serialVersionUID = 5366326425262993205L;
    
    /**
     * JFrame object which represents the application's main container.
     */
    private JFrame appMainFrame;

    /**
     * Constructor.
     */
    public MainFrame()
    {

        super();
        initializeComponents();
        pack();
    }

    /**
     * Initialize the main frame components by creating the main Panel and
     * setting the main frame object's properties.
     */
    private void initializeComponents()
    {
        // Instantiate the GUI main Panel.
        MainPanel oMainUIPanel = new MainPanel();

        appMainFrame = new JFrame(AppConstants.FRAME_TITLE);
        appMainFrame.setContentPane(oMainUIPanel);
        appMainFrame.setSize(new Dimension(750, 560));
        appMainFrame.setExtendedState(appMainFrame.getExtendedState()
                | JFrame.MAXIMIZED_HORIZ);

        // -- Set the world icon.
        //appMainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(AppConstants.ICON_MAIN_FRMAE));

        // -- Set the menu bar.
        JMenuBar oMenuBar = new JMenuBar();
        JMenu oFileMenu = new JMenu("File");
        oFileMenu.add(new JSeparator());
        JMenuItem oMenuClose = new JMenuItem("Close");
        oMenuClose.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        oFileMenu.add(oMenuClose);
        oMenuBar.add(oFileMenu);
        JMenu oEditMenu = new JMenu("Edit");
        oEditMenu.add(new JSeparator());

        appMainFrame.setJMenuBar(oMenuBar);
        
        appMainFrame.addWindowListener
        (
            new WindowAdapter() 
            {
                public void windowClosing(WindowEvent e)
                {
                    GlobalInstance.getInstance().closeRunningGMTs();
                }
            }
        );
        

        /**
         * Calculate the default location depending on the screen size
         */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = appMainFrame.getSize();
        frameSize.height = ((frameSize.height > screenSize.height) ? screenSize.height
                : frameSize.height);
        frameSize.width = ((frameSize.width > screenSize.width) ? screenSize.width
                : frameSize.width);
        appMainFrame.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);

        appMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appMainFrame.setVisible(true);
    }

}
