/**
 * ConsoleTab.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.1 $ $Date: 2011/08/22 03:38:51 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: MessageAndErrorConsole.java,v 1.1 2011/08/22 03:38:51 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui;

import gipsy.RIPE.editors.RunTimeGraphEditor.core.AppConstants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

/**
 * A Singleton class representing the Map Editor console, a view to display log
 * messages.
 * 
 * @author Sleiman Rabah
 * 
 */
public class MessageAndErrorConsole extends JPanel implements ToolBarSwitchView
{

    private static final long serialVersionUID = -5580842093621594480L;
    /**
     * The single instance of the ConsoleTab class.
     */
    private static MessageAndErrorConsole oConsoleTabSingleInstance;
    /*
     * Tabbed pane for logging messages and errors.
     */
    private JTabbedPane oMessageErrorTabs;
    private JTextArea oTxtMessagesActionLog;
    private JTextArea oTxtErrorsActionLog;
    private int iGMTTabCount = 0;
    private List<Integer> aoGMTTabIndexes;

    /**
     * 
     * 
     * @return consoleTab The single instance of the Class consoleTab.
     */
    public static MessageAndErrorConsole getInstance()
    {

        if (oConsoleTabSingleInstance == null)
            oConsoleTabSingleInstance = new MessageAndErrorConsole();

        return oConsoleTabSingleInstance;

    }

    /**
     * The clear button used to clear the Console view.
     */
    private JButton oBtnClearConsole;
    /**
     * The console toolbar which holds the control buttons.
     */
    private JToolBar oConsoleToolBar;

    /**
     * Class Constructor.
     */
    private MessageAndErrorConsole()
    {
        super();

        oBtnClearConsole = new JButton("Clear");
        oConsoleToolBar = new JToolBar();
        aoGMTTabIndexes = new ArrayList<Integer>();
        // --
        this.initializeComponents();
    }

    /**
     * Appends a message to the list of messages in the Console view.
     * 
     * @param str
     *            a string message append to other messages.
     */
    public void append(String str)
    {

        if (str != "")
        {
            this.oTxtMessagesActionLog.append(str);
        }
    }

    /**
     * Clear the console by resetting the Text Area object to null.
     */
    public void clearConsole()
    {
        // -- Verify if the a Map has been loaded in the Map Editor.
        if (JOptionPane.showConfirmDialog(null,
                "Are you sure you want to clear the console?",
                "Confirm Clear Console", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            this.oTxtMessagesActionLog.setText("");
            this.oTxtErrorsActionLog.setText("");

        }
    }

    /**
     * Returns the console tool bar.
     * 
     * @return the console's tab tool bar.
     */
    public JToolBar getToolBar()
    {
        return this.oConsoleToolBar;
    }

    /**
     * Initialize the components of the Console view.
     * <p>
     * The Console view has a text area where the program's message are
     * displayed.
     */
    private void initializeComponents()
    {

        // Set the title's tab.
        setName("Console");
        this.setLayout(new BorderLayout());
        this.setOpaque(true);
        // this.setSize(400, 400);

        // --
        // -- Close Map Button.
        ImageIcon clearMap = new ImageIcon(AppConstants.ICON_MAP_CLOSE);
        oBtnClearConsole = new JButton(clearMap);
        oBtnClearConsole.setToolTipText("Clear Console");
        oBtnClearConsole.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                clearConsole();
            }
        });

        oConsoleToolBar.setRollover(true);
        oConsoleToolBar.setFloatable(false);
        oConsoleToolBar.add(oBtnClearConsole);

        oMessageErrorTabs = new JTabbedPane();
        oMessageErrorTabs.setPreferredSize(new Dimension(400, 200));
        // oMessageErrorTabs.setMinimumSize(new Dimension(400, 100));
        oMessageErrorTabs.setOpaque(true);
        // --
        oMessageErrorTabs.addTab("Messages", makeMessagesLogPanel());
        oMessageErrorTabs.addTab("Errors", makeErrorsLogPanel());
        oMessageErrorTabs.add(oConsoleToolBar);
        // --
        JPanel oPanel = new JPanel(new BorderLayout());
        oPanel.add(oMessageErrorTabs, BorderLayout.CENTER);
        oPanel.add(oConsoleToolBar, BorderLayout.NORTH);

        // this.add(oConsoleToolBar);
        this.add(oPanel);
        disablePanels();

    }

    private Component makeErrorsLogPanel()
    {
        oTxtErrorsActionLog = new JTextArea();
        oTxtErrorsActionLog.setLineWrap(true);
        oTxtErrorsActionLog.setEditable(false);
        JScrollPane oScrollPane = new JScrollPane(oTxtErrorsActionLog);
        oScrollPane.setPreferredSize(new Dimension(200, 200));
        
        // Redirect System.err to the message text area.
        OutputStream oOut = new OutputStream()
        {
            public void write(int b) throws IOException
            {
                oTxtErrorsActionLog.append(String.valueOf((char) b));
            }

            public void write(byte[] b, int off, int len)
            {
                oTxtErrorsActionLog.append(new String(b, off, len));
            }
        };
        System.setErr(new PrintStream(oOut, true));
        
        return oScrollPane;
    }

    private Component makeMessagesLogPanel()
    {
        oTxtMessagesActionLog = new JTextArea();
        oTxtMessagesActionLog.setLineWrap(true);
        oTxtMessagesActionLog.setEditable(false);
        JScrollPane oScrollPane = new JScrollPane(oTxtMessagesActionLog);
        oScrollPane.setPreferredSize(new Dimension(200, 200));
        
        // Redirect System.out to the message text area.
        OutputStream oOut = new OutputStream()
        {
            public void write(int b) throws IOException
            {
                oTxtMessagesActionLog.append(String.valueOf((char) b));
            }

            public void write(byte[] b, int off, int len)
            {
                oTxtMessagesActionLog.append(new String(b, off, len));
            }
        };
        System.setOut(new PrintStream(oOut, true));
        
        return oScrollPane;
    }

    public void disablePanels()
    {
        this.oConsoleToolBar.setEnabled(false);
        this.oMessageErrorTabs.setEnabled(false);
        this.oBtnClearConsole.setEnabled(false);
        this.oTxtMessagesActionLog.setText("");
        this.oTxtErrorsActionLog.setText("");
    }

    public void enablePanels()
    {
        this.oConsoleToolBar.setEnabled(true);
        this.oMessageErrorTabs.setEnabled(true);
        this.oBtnClearConsole.setEnabled(true);
        
    } 

    public OutputStream getOutStreamAddGMTTab()
    {        
        iGMTTabCount++;
        final JTextArea  oOutTextArea  = new JTextArea();
        oOutTextArea.setLineWrap(true);
        oOutTextArea.setEditable(false);
        JScrollPane oScrollPane = new JScrollPane(oOutTextArea);
        oScrollPane.setPreferredSize(new Dimension(200, 200));
        //-- Add new tab for the new created GMT.
        String lstrTabTitle = "GMT "+iGMTTabCount;
        oMessageErrorTabs.addTab(lstrTabTitle, oScrollPane);
        //-- Store the freshly added tab index in a list to be used for removing the tab
        // in question when stoppin the GMT.
        aoGMTTabIndexes.add(oMessageErrorTabs.indexOfTab(lstrTabTitle));
                
        // Redirect System.err to the message text area.
        OutputStream oOut = new OutputStream()
        {
            public void write(int b) throws IOException
            {
                oOutTextArea.append(String.valueOf((char) b));
            }

            public void write(byte[] b, int off, int len)
            {
                oOutTextArea.append(new String(b, off, len));
            }
        };
        
        return oOut;
    }

    public void removeGMTTabs()
    {
        for (Integer iIndex : this.aoGMTTabIndexes)
        {
            this.oMessageErrorTabs.remove(iIndex);
        }
        
    }
}
