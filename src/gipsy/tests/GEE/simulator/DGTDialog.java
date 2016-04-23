package gipsy.tests.GEE.simulator;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;


/**
 * This class represents the DGT Simulator GUI.
 * 
 * @author Emil Vassev
 * @version $Id: DGTDialog.java,v 1.15 2011/01/26 05:11:36 ji_yi Exp $
 * @since June 10, 2007
 */
public class DGTDialog
extends JDialog
implements ActionListener 
{
	//**** constants
	private static final long serialVersionUID = 1L;
	
	//*************************************************************
	//**** GUI swing components

	//**** menu
	private JMenu oFileMenu = null;
	private JMenu oHelpMenu = null;
	private JMenu oThreadMenu = null;
	private JMenu oProfileMenu = null;
	private JMenuItem oExitMenuItem = null;
	private JMenuItem oRefreshMenuItem = null;
	private JMenuItem oProcessResult = null;
	private JMenuItem oRunStopReceiving = null;
	private JMenuItem oAboutMenuItem = null;
	private JMenuItem oHelpMenuItem = null;
	private JMenuItem oRunStopMenuItem = null;
	private JMenuItem oManageProfileMenuItem = null;
	private JMenuItem oLoadProfileMenuItem = null;
	private JMenuBar oDlgMenuBar = null;
	
	//**** panels
	private JPanel oTopPanel = null;
	private JPanel oBottomPanel = null;
	private JPanel oCtrlPanel = null;
	
	//**** text areas
	private JTextArea oDemandsArea = null;
	private JTextArea oResultsArea = null;
	private JTextArea oStatisticsArea = null;
	
	//**** lists
	private JList oDemandClasses = null;
	private DefaultListModel oMdlDemandClasses = null;

	/**
	 * This method handles different events, mainly triggered by menu items.
	 * This method implements the ActionListener interface's actionPerformed() method.
	 */
	public void actionPerformed(ActionEvent poEvent) 
	{
		if(poEvent.getSource() == this.oExitMenuItem)
		{
			if(GlobalDef.soSynchronizer.isLocked())
			{
				GlobalDef.soSynchronizer.release();
			}
			
			if(GlobalDef.soReceiverControl != null 
					&& GlobalDef.soReceiverControl.isLocked())
			{
				GlobalDef.soReceiverControl.release();
			}
			
			GlobalDef.sbEnd = true;
			setVisible(false);
		}
		else if(poEvent.getSource() == this.oRefreshMenuItem)
		{
			GlobalDef.refreshClasses();
			
			if(this.oMdlDemandClasses == null)
			{
				getDemandClassList();
			}
			
			this.oMdlDemandClasses.removeAllElements();
			
			for(int i = 0; i < GlobalDef.soDemandClasses.size(); ++i)
			{
				this.oMdlDemandClasses.addElement(GlobalDef.soDemandClasses.get(i));
			}
		}
		else if(poEvent.getSource() == this.oManageProfileMenuItem)
		{
			boolean bDidStop = false;
			
			if(!GlobalDef.soSynchronizer.isLocked())
			{
				bDidStop = true;
				
				this.oRunStopMenuItem.setText("Run");
				GlobalDef.soSynchronizer.lock();
			}
			
			ProfileDialog oDialog = new ProfileDialog(this, true);
			oDialog.setVisible(true);
			
			// wakes up all the threads if have been stopped
			if((bDidStop) && (GlobalDef.soSynchronizer.isLocked()))
			{
				this.oRunStopMenuItem.setText("Stop");
				GlobalDef.soSynchronizer.release();
			}			
		}
		else if(poEvent.getSource() == this.oLoadProfileMenuItem)
		{
			loadProfile();
		}
		else if(poEvent.getSource() == this.oAboutMenuItem)
		{
			showAboutDialog();
		}
		else if(poEvent.getSource() == this.oRunStopMenuItem)
		{
			
			if(GlobalDef.soSynchronizer.isLocked())
			{
				this.oRunStopMenuItem.setText("Stop");
				
				if(GlobalDef.siMode != GlobalDef.ASYN_CONTROL_MODE)
				{
					this.oThreadMenu.remove(this.oProcessResult);
				}
				
				// wakes up all the threads
				GlobalDef.soSynchronizer.release();
			}
			else
			{
				this.oRunStopMenuItem.setText("Run");
				
				if(GlobalDef.siMode != GlobalDef.ASYN_CONTROL_MODE)
				{
					this.oThreadMenu.add(this.oProcessResult);
				}
				
				GlobalDef.soSynchronizer.lock();
			}
		}
		else if(poEvent.getSource() == this.oRunStopReceiving)
		{
			if(GlobalDef.soReceiverControl.isLocked())
			{
				this.oRunStopReceiving.setText("Stop Receiving");
				this.oThreadMenu.remove(this.oProcessResult);
				
				// wakes up all the threads
				GlobalDef.soReceiverControl.release();
			}
			else
			{
				this.oRunStopReceiving.setText("Start Receiving");
				this.oThreadMenu.add(this.oProcessResult);
				GlobalDef.soReceiverControl.lock();
			}
			
		}
		else if(poEvent.getSource() == this.oProcessResult)
		{
			ResultAnalyst oAnalyst = new ResultAnalyst("RTT");
			new Thread(oAnalyst).start();
		}
	}
	
	/**
	 * This constructor creates and initializes the dialog.
	 */
	public DGTDialog()
	{
		super();

		initialize();
	}
	
	
	
	public DGTDialog(Window poWindow, ModalityType poType) 
	{
		super(poWindow, poType);
		initialize();
	}

	/**
	 * Overrides the JDialog paint method to show the statistics.
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 */
	public void paint(java.awt.Graphics poGraphics) 
	{
		super.paint(poGraphics);
	} 
	
	/**
	 * Loads a profile. 
	 */
	private void loadProfile()
	{
		if(!GlobalDef.soSynchronizer.isLocked())
		{
			this.oRunStopMenuItem.setText("Run");
			GlobalDef.soSynchronizer.lock();
		}	
		
		String strProfilePath = "";
		String strProfileName = "";
		
		JFileChooser oChooser = new JFileChooser(GlobalDef.sstrProfileDir);
	    
		oChooser.setFileFilter(new ProfileFilter());
		oChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		oChooser.setDialogTitle("Open a DGT Profile File");
		
		int oRetVal = oChooser.showOpenDialog(this);
		
	    if(oRetVal == JFileChooser.APPROVE_OPTION) 
	    {
	    	strProfilePath = oChooser.getSelectedFile().getPath();
	    	strProfileName = oChooser.getSelectedFile().getName();
	    	
    		if(strProfileName.endsWith(GlobalDef.PROFILE_EXTENSION))
    		{
    			strProfileName = strProfileName.substring(0, strProfileName.length() - 4);
    		}
	    }
	    else
	    {
	    	return;
	    }
	    
	    DemandFactory oDemandFactory = new DemandFactory();
    	
	    FileInputStream oFileIn;   
		BufferedReader oReaderFile;
		String strCurrLine;
		
		try
		{
			oFileIn =  new FileInputStream(strProfilePath);
			oReaderFile = new BufferedReader(new InputStreamReader(oFileIn, "ASCII"));
			
			strCurrLine = oReaderFile.readLine();
			
			while(strCurrLine != null)
			{
				if(GlobalDef.soDemandClasses.contains(strCurrLine.trim()))
				{
					oDemandFactory.createDemand(strCurrLine.trim());
				}

				strCurrLine = oReaderFile.readLine();
			}
			
			oReaderFile.close(); 
			oFileIn.close();
			
			GlobalDef.soRunProfiles.add(strProfileName);
		}
		catch(IOException ex)
		{
			GlobalDef.handleNonCriticalException(ex);
		}
	}

	/**
	 * This method creates and shows the "About" dialog.
	 */
	private void showAboutDialog()
	{
		String strAboutMsg
			= "GIPSY DGT Simulator " + GlobalDef.CR + GlobalDef.LF
			+ "Version 1.0.1, $Id: DGTDialog.java,v 1.15 2011/01/26 05:11:36 ji_yi Exp $ " + GlobalDef.CR + GlobalDef.LF
			+ "Originally Developed by Emil Vassev, June 1 - July 2, 2007" + GlobalDef.CR + GlobalDef.LF
			+ "GIPSY Research and Development Team" + GlobalDef.CR + GlobalDef.LF
			+ "Department of Computer Science and Software Engineering" + GlobalDef.CR + GlobalDef.LF
			+ "Concordia University, Montreal, QC, Canada";
				   				   
		JOptionPane.showMessageDialog
		(
			null,
			strAboutMsg, 
			"About",
			JOptionPane.INFORMATION_MESSAGE
		);
	}

	/**
	 * This method creates, shows and closes the dialog for entering the number of demands.
	 * @param pstrClassName XXX
	 */
	public void enterNumberOfDemands(String pstrClassName)
	{
		Object[] aoPossibleValues =
		{
			"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
			"20", "30", "50", "100", "200", "1000", "7000", "10000", "50000"
		};

		Object oSelectedValue = JOptionPane.showInputDialog
		(
			this, 
			"Enter the number of demands you want to create:", 
			pstrClassName,
			JOptionPane.INFORMATION_MESSAGE,
			null,
			aoPossibleValues,
			aoPossibleValues[0]
		);

		if(oSelectedValue != null)
		{
		  	int iNum = Integer.parseInt(oSelectedValue.toString());

		  	DemandFactory oDemandFactory = new DemandFactory();
		  	for(int i = 0; i < iNum; ++i)
		  	{
		  		DemandClassPool.getInstance().put(pstrClassName);
		  		oDemandFactory.createDemand(pstrClassName);
		  	}
		  	//DemandPool.getInstance().updateGUI();
		  	// Now update the statistics information
		  	GlobalDef.soStatisticsUpdator.updateStatisticsInfo();
		}
	}
	  
	/**
	 * This method creates the dialog menu bar.
	 * 
	 * @return reference to the dialog menu bar
	 */
	private JMenuBar getDialogMenuBar()
	{
		if(this.oDlgMenuBar == null)
		{
			try
			{
				this.oExitMenuItem = new JMenuItem();
				this.oExitMenuItem.setText("Exit");
				this.oExitMenuItem.addActionListener(this);
				
				this.oRefreshMenuItem = new JMenuItem();
				this.oRefreshMenuItem.setText("Refresh Classes");
				this.oRefreshMenuItem.addActionListener(this);

				this.oAboutMenuItem = new JMenuItem();
				this.oAboutMenuItem.setText("About");
				this.oAboutMenuItem.addActionListener(this);

				this.oHelpMenuItem = new JMenuItem();
				this.oHelpMenuItem.setText("Help");
				this.oHelpMenuItem.addActionListener(this);
				
				this.oRunStopMenuItem = new JMenuItem();
				this.oRunStopMenuItem.setText("Run");
				this.oRunStopMenuItem.addActionListener(this);
				
				this.oManageProfileMenuItem = new JMenuItem();
				this.oManageProfileMenuItem.setText("Manage");
				this.oManageProfileMenuItem.addActionListener(this);

				this.oLoadProfileMenuItem = new JMenuItem();
				this.oLoadProfileMenuItem.setText("Load");
				this.oLoadProfileMenuItem.addActionListener(this);
				
				this.oProcessResult = new JMenuItem();
				this.oProcessResult.setText("Process Result");
				this.oProcessResult.addActionListener(this);
				
				this.oFileMenu = new JMenu();
				this.oFileMenu.setText("File");
				this.oFileMenu.add(this.oRefreshMenuItem);
				this.oFileMenu.add(this.oExitMenuItem);
				
				this.oThreadMenu = new JMenu();
				this.oThreadMenu.setText("Threads");
				this.oThreadMenu.add(this.oRunStopMenuItem);
				
				if(GlobalDef.siMode == GlobalDef.ASYN_CONTROL_MODE)
				{
					this.oRunStopReceiving = new JMenuItem();
					this.oRunStopReceiving.setText("Start Receiving");
					this.oRunStopReceiving.addActionListener(this);
					this.oThreadMenu.add(this.oRunStopReceiving);
				}
				
				this.oProfileMenu = new JMenu();
				this.oProfileMenu.setText("Profile");
				this.oProfileMenu.add(this.oManageProfileMenuItem);
				this.oProfileMenu.add(this.oLoadProfileMenuItem);

				this.oHelpMenu = new JMenu();
				this.oHelpMenu.setText("Help");
				this.oHelpMenu.add(this.oHelpMenuItem);
				this.oHelpMenu.add(this.oAboutMenuItem);
				
				this.oDlgMenuBar = new JMenuBar();
				this.oDlgMenuBar.add(this.oFileMenu);
				this.oDlgMenuBar.add(this.oThreadMenu);
				this.oDlgMenuBar.add(this.oProfileMenu);
				this.oDlgMenuBar.add(this.oHelpMenu);
			}
			catch(Exception ex)
			{
				GlobalDef.handleCriticalException(ex);
			}
		}

	    return this.oDlgMenuBar;
	}
	
	/**
	 * This method creates the jtaDemands text area.
	 * 
	 * @return newly created text area
	 */
	public JTextArea getTADemands()
	{
		if(this.oDemandsArea == null)
		{
			try
			{
				this.oDemandsArea = new JTextArea();
				this.oDemandsArea.setWrapStyleWord(false);
			}
			catch(Exception ex)
			{
				GlobalDef.handleCriticalException(ex);
			}
		}
		
		return this.oDemandsArea;
	}
	
	/**
	 * This method creates the jtaResults text area.
	 * 
	 * @return newly created text area
	 */
	public JTextArea getTAResults()
	{
		if(this.oResultsArea == null)
		{
			try
			{
				this.oResultsArea = new JTextArea();
				this.oResultsArea.setWrapStyleWord(false);
			}
			catch(Exception ex)
			{
				GlobalDef.handleCriticalException(ex);
			}
		}
		
		return oResultsArea;
	}
	
	/**
	 * This method creates the jtaStatistics text area.
	 * 
	 * @return newly created text area
	 */
	public JTextArea getTAStatistics()
	{
		if(this.oStatisticsArea == null)
		{
			try
			{
				this.oStatisticsArea = new JTextArea();
				this.oStatisticsArea.setWrapStyleWord(false);
			}
			catch (Exception ex)
			{
				GlobalDef.handleCriticalException(ex);
			}
		}
		
		return this.oStatisticsArea;
	}

	/**
	 * This method creates the statistics view list box.
	 * 
	 * @return newly created list box
	 */
	private JList getDemandClassList()
	{
		if(this.oDemandClasses == null)
		{
			try
			{
				this.oMdlDemandClasses = new DefaultListModel();
				
				for(int i = 0; i < GlobalDef.soDemandClasses.size(); ++i)
				{
					this.oMdlDemandClasses.addElement(GlobalDef.soDemandClasses.get(i));
				}
				
				this.oDemandClasses = new JList(this.oMdlDemandClasses);
				this.oDemandClasses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				this.oDemandClasses.addMouseListener
				(
					new MouseAdapter() 
					{
					    public void mouseClicked(MouseEvent e) 
					    {
					    	if (e.getClickCount() == 2) 
					        {
					    		int iIndex = oDemandClasses.locationToIndex(e.getPoint());
					            enterNumberOfDemands(GlobalDef.soDemandClasses.get(iIndex));
					        }
					    }
					}
				);
			}
			catch(Exception ex)
			{
				GlobalDef.handleCriticalException(ex);
			}
		}
		
		return this.oDemandClasses;
	}

	/**
	 * This method creates the top panel.
	 * 
	 * @return newly created panel
	 */
	private JPanel getTopPanel()
	{
		if(this.oTopPanel == null)
		{
			try
		    {
				this.oTopPanel = new JPanel();
				this.oTopPanel.setLayout(new GridBagLayout());
		    	  
		        GridBagConstraints oGridBagConstraintsTxt = new GridBagConstraints();
		        oGridBagConstraintsTxt.fill = java.awt.GridBagConstraints.BOTH;		        
		        oGridBagConstraintsTxt.gridx = 0;
		        oGridBagConstraintsTxt.gridy = 0;
		        oGridBagConstraintsTxt.weightx = 1.0;
		        oGridBagConstraintsTxt.weighty = 0.1;
		        oGridBagConstraintsTxt.insets = new java.awt.Insets(10,10,0,10);
		        oGridBagConstraintsTxt.anchor = java.awt.GridBagConstraints.NORTH;
		     
		    	JLabel oDemandClasses = new JLabel("Demand Classes");
		    	oDemandClasses.setFont(new Font(oDemandClasses.getFont().getFontName(),Font.PLAIN, 14));
		    	this.oTopPanel.add(oDemandClasses, oGridBagConstraintsTxt);

		        oGridBagConstraintsTxt.gridx = 1;
		    	JLabel oPendingDemands = new JLabel("Pending Demands");
		    	oPendingDemands.setFont(new Font(oPendingDemands.getFont().getFontName(),Font.PLAIN, 14));
		    	this.oTopPanel.add(oPendingDemands, oGridBagConstraintsTxt);
		        
		        oGridBagConstraintsTxt.gridx = 2;
		    	JLabel oComputedDemands = new JLabel("Computed Demands");
		    	oComputedDemands.setFont(new Font(oComputedDemands.getFont().getFontName(),Font.PLAIN, 14));
		    	this.oTopPanel.add(oComputedDemands, oGridBagConstraintsTxt);
		        
		        oGridBagConstraintsTxt.gridy = 1;
		        oGridBagConstraintsTxt.gridx = 0;
		    	oGridBagConstraintsTxt.weighty = 1;
		        oGridBagConstraintsTxt.fill = java.awt.GridBagConstraints.BOTH;		
		        oGridBagConstraintsTxt.insets = new java.awt.Insets(0,10,0,10);
		        JScrollPane oScrPaneDemandClasses = new JScrollPane(getDemandClassList(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		        this.oTopPanel.add(oScrPaneDemandClasses, oGridBagConstraintsTxt);
		    	
		        oGridBagConstraintsTxt.gridx = 1;
		        this.oDemandsArea = getTADemands();
		        JScrollPane oScrPaneDemands = new JScrollPane(this.oDemandsArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		        this.oTopPanel.add(oScrPaneDemands, oGridBagConstraintsTxt);
		    	
		    	oGridBagConstraintsTxt.gridx = 2;
		    	this.oResultsArea = getTAResults();
		        JScrollPane oScrPaneResults = new JScrollPane(this.oResultsArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		        this.oTopPanel.add(oScrPaneResults, oGridBagConstraintsTxt);
		    }
		    catch(Exception ex)
		    {
		    	GlobalDef.handleCriticalException(ex);
		    }
		}
		
		return this.oTopPanel;
	}
	
	/**
	 * This method creates the bottom panel.
	 * 
	 * @return newly created panel
	 */
	private JPanel getBottomPanel()
	{
		if(this.oBottomPanel == null)
		{
			try
		    {
				this.oBottomPanel = new JPanel();
				this.oBottomPanel.setLayout(new GridBagLayout());
				
		        GridBagConstraints oGridBagConstraints = new GridBagConstraints();
		        oGridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		        oGridBagConstraints.gridx = 0;
		        oGridBagConstraints.gridy = 0;
		        oGridBagConstraints.weightx = 2.0;
		        oGridBagConstraints.weighty = 0.1;
		        oGridBagConstraints.insets = new java.awt.Insets(10,10,0,10);
		        oGridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		     
		    	JLabel oGInfoLabel = new JLabel("Statistics Info");
		    	oGInfoLabel.setFont(new Font(oGInfoLabel.getFont().getFontName(),Font.PLAIN, 14));
		    	this.oBottomPanel.add(oGInfoLabel, oGridBagConstraints);
		    	
		        oGridBagConstraints.gridy = 1;
		        oGridBagConstraints.weighty = 1.0;
		        oGridBagConstraints.insets = new java.awt.Insets(0,10,10,10);
		        oGridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		        
		        this.oStatisticsArea = getTAStatistics();
		        JScrollPane oScrStatistics = new JScrollPane(oStatisticsArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		        this.oBottomPanel.add(oScrStatistics, oGridBagConstraints);
		        
		    }
		    catch(Exception ex)
		    {
		    	GlobalDef.handleCriticalException(ex);
		    }
		}

		return this.oBottomPanel;
	}
	
	/**
	 * This method creates the GUI. It calls the methods for creating the two 
	 * panels and adds those panels to the main control panel.
	 */
	private void createGUI()
	{
		GridBagConstraints oGridBagConstraintsPnl = new GridBagConstraints();
	    oGridBagConstraintsPnl.fill = java.awt.GridBagConstraints.BOTH;
	    oGridBagConstraintsPnl.gridx = 0;
	    oGridBagConstraintsPnl.gridy = 0;
	    oGridBagConstraintsPnl.weighty = 2.0;
	    oGridBagConstraintsPnl.insets = new java.awt.Insets(1,1,1,1);
	    oGridBagConstraintsPnl.anchor = java.awt.GridBagConstraints.NORTH;
	      
	    this.oCtrlPanel = new JPanel();
	    this.oCtrlPanel.setLayout(new GridBagLayout());

	    this.oCtrlPanel.add(getTopPanel(), oGridBagConstraintsPnl);
	      
	    oGridBagConstraintsPnl.weightx = 1.0;
		oGridBagConstraintsPnl.gridy = 1;
		this.oCtrlPanel.add(getBottomPanel(), oGridBagConstraintsPnl);

	    this.setContentPane(this.oCtrlPanel);
	}
	  
	/**
	 * This method initializes the view. It is called by the constructor.
	 */
	public void initialize()
	{
		// XXX: hardcoding
		setSize(600, 500);
		setTitle("GIPSY DGT Simulator, ver. 2.0.0, $Id: DGTDialog.java,v 1.15 2011/01/26 05:11:36 ji_yi Exp $");
		setJMenuBar(getDialogMenuBar());
		createGUI();
	}
}

// EOF
