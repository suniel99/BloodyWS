package gipsy.tests.GEE.simulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;


/**
 * This class represents the Profile dialog, which is used to manage the profiles.
 * 
 * @author Emil Vassev
 * @version $Id: ProfileDialog.java,v 1.6 2009/09/08 23:29:36 ji_yi Exp $
 * @since June 10, 2007
 */
public class ProfileDialog
extends JDialog
implements ActionListener 
{
	//**** constants
	private static final long serialVersionUID = 1L;
	private static final String DLG_TITLE_PREFIX = "[Profile Manager]";
	
	//*************************************************************
	//**** GUI swing components
	
	//**** menu
	private JMenu oFileMenu = null;
	private JMenuItem oNewMenuItem = null;
	private JMenuItem oOpenMenuItem = null;
	private JMenuItem oSaveMenuItem = null;
	private JMenuItem oSaveAsMenuItem = null;
	private JMenuItem oExitMenuItem = null;
	private JMenuBar oDlgMenuBar = null;
	
	//**** panels
	private JPanel oBasePanel = null;
	private JPanel oCtrlPanel = null;
	private ProfileToolbar oToolBar = null;
	
	//**** lists
	private JList oDemandClasses = null;
	private JList oDemandObjects = null;
	private DefaultListModel oMdlDemandObjects = null;
	
	//**** current profile's name
	private String strProfileName;

	
	/**
	 * This method handles different events, mainly triggered by menu items.
	 * This method implements the ActionListener interface's actionPerformed() method.
	 */
	public void actionPerformed(ActionEvent poEvent) 
	{
		if(poEvent.getSource() == this.oExitMenuItem)
		{
			setVisible(false);
		}
		else if(poEvent.getSource() == this.oOpenMenuItem)
		{
			openFile();
		}
		else if(poEvent.getSource() == this.oSaveMenuItem)
		{
			saveFile(true);
		}	
		else if(poEvent.getSource() == this.oSaveAsMenuItem)
		{
			saveFile(false);
		}	
		else if(poEvent.getSource() == this.oNewMenuItem)
		{
	        if(oMdlDemandObjects == null)
	        {
	        	getDemandObjectsList();
	        }
	        
	        this.oMdlDemandObjects.removeAllElements();
	        
	        this.strProfileName = "";
			setDialogTitle();
		}
	}
	
	/**
	 * Sets the dialog title - comprises the current profile name.
	 */
	private void setDialogTitle()
	{
    	if(this.strProfileName == "")
    	{
    		setTitle(DLG_TITLE_PREFIX + " New Profile");
    	}
    	else
    	{
    		if(this.strProfileName.endsWith(GlobalDef.PROFILE_EXTENSION))
    		{
    			this.strProfileName = this.strProfileName.substring(0, this.strProfileName.length() - 4);
    		}

    		setTitle(DLG_TITLE_PREFIX + " " + strProfileName);
    	}
	}
	
	/**
	 * Starts a file dialog to open a profile file.
	 */
	private void openFile()
	{
	    JFileChooser oChooser = new JFileChooser(GlobalDef.sstrProfileDir);
	    oChooser.setFileFilter(new ProfileFilter());
		oChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		oChooser.setDialogTitle("Open a DGT Profile File");
		
		int iRetVal = oChooser.showOpenDialog(this);
	    
		if(iRetVal == JFileChooser.APPROVE_OPTION) 
	    {
			this.strProfileName = oChooser.getSelectedFile().getName();
	    	
			GlobalDef.sstrProfileDir = oChooser.getSelectedFile().getPath();
	    	GlobalDef.sstrProfileDir = GlobalDef.sstrProfileDir.substring
	    	(
	    		0,
	    		GlobalDef.sstrProfileDir.lastIndexOf(this.strProfileName) - 1
	    	);
	    	
	    	if(loadProfile(this.strProfileName))
	    	{
	    		setDialogTitle();
	    	}
	    }
	}
	
	/**
	 * Starts a file dialog to save a profile file.
	 */
	private void saveFile(boolean pbSaveMenuItem)
	{
		if(!(pbSaveMenuItem && !this.strProfileName.equals("")))
		{
		    JFileChooser oChooser = new JFileChooser(GlobalDef.sstrProfileDir);
			
		    oChooser.setFileFilter(new ProfileFilter());
			oChooser.setDialogType(JFileChooser.SAVE_DIALOG);
			oChooser.setDialogTitle("Save a DGT Profile File");
			
			int iRetVal = oChooser.showSaveDialog(this);
			
			if(iRetVal == JFileChooser.APPROVE_OPTION)
			{
				this.strProfileName = oChooser.getSelectedFile().getName();
				GlobalDef.sstrProfileDir = oChooser.getSelectedFile().getPath();
				GlobalDef.sstrProfileDir = GlobalDef.sstrProfileDir.substring
				(
					0,
					GlobalDef.sstrProfileDir.lastIndexOf(this.strProfileName) - 1
				);
			}
		}
		
		if(!this.strProfileName.equals(""))
		{
			if(saveProfile(this.strProfileName))
			{
				setDialogTitle();
			}
		}
	}

	/**
	 * Loads the profile file into the demand object list box.
	 * 
	 * @param profileFile
	 */
	private boolean loadProfile(String pstrProfileName)
	{
        if(this.oMdlDemandObjects == null)
        {
        	getDemandObjectsList();
        }
        
    	if(!pstrProfileName.endsWith(GlobalDef.PROFILE_EXTENSION))
    	{
    		pstrProfileName += GlobalDef.PROFILE_EXTENSION;
    	}
    	
    	FileInputStream oFileIn;   
		BufferedReader oReaderFile;
		String strCurrLine;
		
		try
		{
			this.oMdlDemandObjects.removeAllElements();
			
			oFileIn =  new FileInputStream(GlobalDef.sstrProfileDir + "/" + pstrProfileName);
			oReaderFile = new BufferedReader(new InputStreamReader(oFileIn, "ASCII"));
			
			strCurrLine = oReaderFile.readLine();
			while(strCurrLine != null)
			{
				this.oMdlDemandObjects.addElement(strCurrLine.trim());

				strCurrLine = oReaderFile.readLine();
			}
			
			oReaderFile.close(); 
			oFileIn.close();
		}
		catch(IOException ex)
		{
			GlobalDef.handleNonCriticalException(ex);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Loads the profile file into the demand object list box.
	 * 
	 * @param profileFile
	 */
	private boolean saveProfile(String pstrProfileName)
	{
        if(this.oMdlDemandObjects == null)
        {
        	getDemandObjectsList();
        }
        
    	if(!pstrProfileName.endsWith(GlobalDef.PROFILE_EXTENSION))
    	{
    		pstrProfileName += GlobalDef.PROFILE_EXTENSION;
    	}
    	
    	// IO declarations - file output    
    	FileOutputStream oFileOut; 
    	Writer oWriter;
    	
		try
		{
			oFileOut = new FileOutputStream(GlobalDef.sstrProfileDir + "/" + pstrProfileName, false);
			oWriter = new BufferedWriter(new OutputStreamWriter(oFileOut, "ASCII"));
			
			for(int i = 0; i < this.oMdlDemandObjects.size(); ++i)
			{
				oWriter.write(this.oMdlDemandObjects.getElementAt(i).toString());
				oWriter.write(GlobalDef.CR);
				oWriter.write(GlobalDef.LF);
				oWriter.flush();
			}

			oWriter.flush();
			oWriter.close(); 
			oFileOut.close();
		}
		catch(IOException ex)
		{
			GlobalDef.handleNonCriticalException(ex);
			return false;
		}
		
		return true;
	}
	
	/**
	 * This constructor creates and initializes the dialog.
	 */
	public ProfileDialog()
	{
		this(null, false);
	}
	
	/**
	 * This constructor creates and initializes the dialog.
	 */
	public ProfileDialog(JDialog poOwner, boolean pbModal)
	{
		super(poOwner, pbModal);
		this.strProfileName = "";
		initialize();
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
				this.oNewMenuItem = new JMenuItem();
				this.oNewMenuItem.setText("New");
				this.oNewMenuItem.addActionListener(this);

				this.oOpenMenuItem = new JMenuItem();
				this.oOpenMenuItem.setText("Open");
				this.oOpenMenuItem.addActionListener(this);
				
				this.oSaveMenuItem = new JMenuItem();
				this.oSaveMenuItem.setText("Save");
				this.oSaveMenuItem.addActionListener(this);
				
				this.oSaveAsMenuItem = new JMenuItem();
				this.oSaveAsMenuItem.setText("Save As...");
				this.oSaveAsMenuItem.addActionListener(this);

				this.oExitMenuItem = new JMenuItem();
				this.oExitMenuItem.setText("Exit");
				this.oExitMenuItem.addActionListener(this);

				this.oFileMenu = new JMenu();
				this.oFileMenu.setText("File");
				this.oFileMenu.add(this.oNewMenuItem);
				this.oFileMenu.add(this.oOpenMenuItem);
				this.oFileMenu.add(this.oSaveMenuItem);
				this.oFileMenu.add(this.oSaveAsMenuItem);
				this.oFileMenu.add(this.oExitMenuItem);
				
				this.oDlgMenuBar = new JMenuBar();
				this.oDlgMenuBar.add(this.oFileMenu);
			}
			catch(Exception ex)
			{
				GlobalDef.handleCriticalException(ex);
			}
		}
		  
	    return this.oDlgMenuBar;
	}

	/**
	 * This method creates the statistics view list box.
	 * 
	 * @return newly created list box
	 */
	private JList getDemandObjectsList()
	{
		if(this.oDemandObjects == null)
		{
			try
			{
				this.oMdlDemandObjects = new DefaultListModel();
				this.oDemandObjects = new JList(this.oMdlDemandObjects);
				this.oDemandObjects.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				
				this.oDemandObjects.addMouseListener
				(
					new MouseAdapter() 
					{
						public void mouseClicked(MouseEvent e) 
						{
							if(e.getClickCount() == 2) 
							{
								try
								{
									int iIndex = oDemandObjects.locationToIndex(e.getPoint());
									oMdlDemandObjects.removeElementAt(iIndex);
								}
								catch(Exception ex)
								{
									java.awt.Toolkit.getDefaultToolkit().beep();				            		 
								}
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
		
		return this.oDemandObjects;
	}

	/**
	 * This method creates the statistics view list box.
	 * 
	 * @return newly created list box
	 */
	private JList getDemandClassesList()
	{
		if(this.oDemandClasses == null)
		{
			try
			{
				String[] astrCBItems = new String[GlobalDef.soDemandClasses.size()];
				
				for (int i=0; i < GlobalDef.soDemandClasses.size(); ++i)
				{
					astrCBItems[i] = GlobalDef.soDemandClasses.get(i);
				}

				this.oDemandClasses = new JList(astrCBItems);
				this.oDemandClasses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				
				this.oDemandClasses.addMouseListener
				(
					new MouseAdapter() 
					{
						public void mouseClicked(MouseEvent e) 
						{
							if(e.getClickCount() == 2) 
							{
								try
								{
									int iIndex = oDemandClasses.locationToIndex(e.getPoint());
									
									if(oMdlDemandObjects != null)
									{
										int max = Integer.parseInt(getToolBar().getToolbarTextArea().getText());
										for(int i = 0; i < max; ++i)
										{
											oMdlDemandObjects.addElement(GlobalDef.soDemandClasses.get(iIndex));
										}
									}
					        	}
					        	catch(Exception ex)
					        	{
					        		 java.awt.Toolkit.getDefaultToolkit().beep();				            		 
					        	}
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

	private ProfileToolbar getToolBar()
	{
		if(this.oToolBar == null)
		{
			try
		    {
				this.oToolBar = new ProfileToolbar();
			}
		    catch(Exception ex)
		    {
		    	GlobalDef.handleCriticalException(ex);
		    }
		}
		
		return oToolBar;	
	}
	
	/**
	 * This method creates the control panel.
	 * 
	 * @return newly created panel
	 */
	private JPanel getCtrlPanel()
	{
		if(this.oCtrlPanel == null)
		{
			try
		    {
				this.oCtrlPanel = new JPanel();
				this.oCtrlPanel.setLayout(new GridBagLayout());
		    	  
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
		    	this.oCtrlPanel.add(oDemandClasses, oGridBagConstraintsTxt);

		        oGridBagConstraintsTxt.gridx = 1;
		        oGridBagConstraintsTxt.weightx = 1.0;
		        JLabel oPendingDemands = new JLabel("Demand Objects");
		    	oPendingDemands.setFont(new Font(oPendingDemands.getFont().getFontName(),Font.PLAIN, 14));
		    	this.oCtrlPanel.add(oPendingDemands, oGridBagConstraintsTxt);
		        
		        oGridBagConstraintsTxt.gridy = 1;
		        oGridBagConstraintsTxt.gridx = 0;
		    	oGridBagConstraintsTxt.weighty = 1;
		        oGridBagConstraintsTxt.weightx = 1.0;
		        oGridBagConstraintsTxt.fill = java.awt.GridBagConstraints.BOTH;		
		        oGridBagConstraintsTxt.insets = new java.awt.Insets(0,10,0,10);
		        
		        JScrollPane oScrPaneDemandClasses = new JScrollPane
		        (
		        	getDemandClassesList(),
		        	JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		        	JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
		        );
		        
		        this.oCtrlPanel.add(oScrPaneDemandClasses, oGridBagConstraintsTxt);
		    	
		        oGridBagConstraintsTxt.gridx = 1;
		        oGridBagConstraintsTxt.weightx = 1.0;
		        JScrollPane oScrPaneDemandObjects = new JScrollPane
		        (
		        	getDemandObjectsList(),
		        	JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		        	JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
		        );
		        
		        this.oCtrlPanel.add(oScrPaneDemandObjects, oGridBagConstraintsTxt);
		    }
		    catch(Exception ex)
		    {
		    	GlobalDef.handleCriticalException(ex);
		    }
		}
		
		return oCtrlPanel;
	}

	/**
	 * This method creates the GUI. It calls the methods for creating the two 
	 * panels and adds those panels to the main control panel.
	 */
	private void createGUI()
	{
		GridBagConstraints oGridBagConstraintsPnl = new GridBagConstraints();
	    oGridBagConstraintsPnl.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    oGridBagConstraintsPnl.gridx = 0;
	    oGridBagConstraintsPnl.gridy = 0;
		oGridBagConstraintsPnl.weightx = 1;
		oGridBagConstraintsPnl.weighty = 0.1;
	    oGridBagConstraintsPnl.insets = new java.awt.Insets(1,1,1,1);
	    oGridBagConstraintsPnl.anchor = java.awt.GridBagConstraints.NORTH;
	      
	    this.oBasePanel = new JPanel();
	    this.oBasePanel.setLayout(new GridBagLayout());
	    
	    this.oBasePanel.add(getToolBar(), oGridBagConstraintsPnl);
	      
	    oGridBagConstraintsPnl.fill = java.awt.GridBagConstraints.BOTH;
	    oGridBagConstraintsPnl.gridy = 1;
		oGridBagConstraintsPnl.weighty = 1.0;
		this.oBasePanel.add(getCtrlPanel(), oGridBagConstraintsPnl);

	    this.setContentPane(this.oBasePanel);
	}
	  
	/**
	 * This method initializes the view. It is called by the constructor.
	 */
	public void initialize()
	{
		setSize(450, 400);
		setDialogTitle();
		setJMenuBar(getDialogMenuBar());
		createGUI();
	}


	/**
	 * This class represents the Profile toolbar, which is used to move and delete 
	 * the objects in the jlstDemandObjects list box.
	 * 
	 * @author Emil Vassev
	 * @since June 10, 2007
	 * @version $Id: ProfileDialog.java,v 1.6 2009/09/08 23:29:36 ji_yi Exp $
	 */
	private class ProfileToolbar
	extends JToolBar
	implements ActionListener 
	{
		//**** constants
		private static final String UP = "UP";
		private static final String DOWN = "DOWN";
		private static final String DELETE = "DELETE";
		private static final String CREATE = "CREATE";

		private static final long serialVersionUID = 1L;
		
		private JTextArea oToolbarTextArea = null;

		public void actionPerformed(ActionEvent poEvent) 
		{
			String strCommand = poEvent.getActionCommand();

			if(UP.equals(strCommand)) 
			{
				//**** UP button clicked
				try
				{
					int iIndex = oDemandObjects.getSelectedIndex();

					if(iIndex > 0)
					{
						Object sDemand = oMdlDemandObjects.getElementAt(iIndex);
						oMdlDemandObjects.removeElementAt(iIndex);
						oMdlDemandObjects.insertElementAt(sDemand, iIndex - 1);
						oDemandObjects.setSelectedIndex(iIndex-1);
					}
					else
					{
						java.awt.Toolkit.getDefaultToolkit().beep();
					}
				}
				catch(Exception ex)
				{
					java.awt.Toolkit.getDefaultToolkit().beep();				            		 
				}
			}
			else if(DOWN.equals(strCommand)) 
			{
				//**** DOWN button clicked
				try
				{
					int iIndex = oDemandObjects.getSelectedIndex();
					
					Object strDemand = oMdlDemandObjects.getElementAt(iIndex);
					
					if(iIndex < (oMdlDemandObjects.size() - 1))
					{
						oMdlDemandObjects.removeElementAt(iIndex);
						oMdlDemandObjects.insertElementAt(strDemand, iIndex + 1);
						oDemandObjects.setSelectedIndex(iIndex+1);
					}
					else
					{
						java.awt.Toolkit.getDefaultToolkit().beep();	
					}
				}
				catch(Exception ex)
				{
					java.awt.Toolkit.getDefaultToolkit().beep();				            		 
				}
			} 
			else if(DELETE.equals(strCommand)) 
			{
				//**** DELETE button clicked
				try
				{
					int iIndex = oDemandObjects.getSelectedIndex();
					
					oMdlDemandObjects.removeElementAt(iIndex);
					
					if(oMdlDemandObjects.size() > 0)
					{
						oDemandObjects.setSelectedIndex(iIndex);
					}
				}
				catch(Exception ex)
				{
					java.awt.Toolkit.getDefaultToolkit().beep();				            		 
				}
	        } 
			else if (CREATE.equals(strCommand)) 
			{
				//**** CREATE button clicked
           	 	try
           	 	{
           	 		int iIndex = oDemandClasses.getSelectedIndex();
           	 		
           	 		if(oMdlDemandObjects != null)
           	 		{
           	 			int max = Integer.parseInt(getToolBar().getToolbarTextArea().getText());
           	 			
	            		for(int i = 0; i < max; ++i)
	            		{
	            			oMdlDemandObjects.addElement(GlobalDef.soDemandClasses.get(iIndex));
	            		}
           	 		}
           	 	}
           	 	catch(Exception ex)
           	 	{
           	 		java.awt.Toolkit.getDefaultToolkit().beep();				            		 
           	 	}
			} 
		}

		/**
		 * 
		 */
		public ProfileToolbar() 
		{
			super("DGT Profile toolbar");
			setFloatable(false);
			createComponents();
		}
		 
		/**
		 * This method creates the toolbarTextArea text area.
		 * 
		 * @return newly created text area
		 */
		public JTextArea getToolbarTextArea()
		{
			if(this.oToolbarTextArea == null)
			{
				try
				{
					this.oToolbarTextArea = new JTextArea();
					this.oToolbarTextArea.setWrapStyleWord(false);
					this.oToolbarTextArea.setDocument(new LimitedPlainDocument(4));
					this.oToolbarTextArea.setBackground(Color.GRAY);
					this.oToolbarTextArea.setFont(new Font(oToolbarTextArea.getFont().getFontName(),Font.PLAIN, 18));
					this.oToolbarTextArea.setText("1");
				}
				catch(Exception ex)
				{
					GlobalDef.handleCriticalException(ex);
				}
			}
			
			return this.oToolbarTextArea;
		}
		
		/**
		 * XXX
		 */
		protected void createComponents() 
		{
			JButton oButton = null;

			//**** first button
			oButton = makeNavigationButton(UP, "Move selected demands upward.", "Up");
			add(oButton, BorderLayout.EAST);

			//**** second button
			oButton = makeNavigationButton(DOWN, "Move selected demands downward.", "Down");
			add(oButton, BorderLayout.EAST);

			//**** third button
			oButton = makeNavigationButton(DELETE, "Delete selected demands.", "Del");
			add(oButton, BorderLayout.EAST);
			 
			//**** second button
			oButton = makeNavigationButton(CREATE, "Create demands from the selected demand class.", "Create");
			add(oButton, BorderLayout.EAST);

			//**** label and text area
		    JLabel infoLabel = new JLabel("                Number of demands: ");
		    infoLabel.setFont(new Font(infoLabel.getFont().getFontName(),Font.PLAIN, 14));
		    add(infoLabel, BorderLayout.EAST);		
		     
		    add(getToolbarTextArea(), BorderLayout.EAST);
		} 
		
		/**
		 * XXX
		 * @param pstrActionCommand
		 * @param pstrToolTipText
		 * @param pstrAltText
		 * @return
		 */
		protected JButton makeNavigationButton(String pstrActionCommand, String pstrToolTipText, String pstrAltText) 
		{
			//**** creates and initializes the button.
			JButton oButton = new JButton();
			oButton.setActionCommand(pstrActionCommand);
			oButton.setToolTipText(pstrToolTipText);
			oButton.addActionListener(this);
			oButton.setText(pstrAltText);

			return oButton;
		}
	}


	/**
	 * This class provides a plain document to control the length and the input of the tool bar's text area. 
	 * 
	 * @author Emil Vassev
	 * @version $Id: ProfileDialog.java,v 1.6 2009/09/08 23:29:36 ji_yi Exp $
	 * @since
	 */
	public class LimitedPlainDocument
	extends javax.swing.text.PlainDocument 
	{
		//**** constants
		
		/**
		 * XXX: recompute.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * XXX
		 */
		private int iMaxLen = -1;
		
		/** 
		 * Creates a new instance of LimitedPlainDocument. 
		 */
		public LimitedPlainDocument() 
		{
			super();
		}
		
		/**
		 * XXX
		 * @param piMaxLen
		 */
		public LimitedPlainDocument(int piMaxLen) 
		{
			this.iMaxLen = piMaxLen;
		}
		
		/**
		 * Limits the length to maxLen and the input to digits only.
		 * @see javax.swing.text.PlainDocument#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
		 */
		public void insertString(int piParam, String pstrStringToInsert, javax.swing.text.AttributeSet poAttributeSet) 
		throws javax.swing.text.BadLocationException 
		{
			if
			(
				pstrStringToInsert != null
				&& this.iMaxLen > 0
				&& this.getLength() + pstrStringToInsert.length() > this.iMaxLen
			) 
			{
				java.awt.Toolkit.getDefaultToolkit().beep();
				return;
			}

			try
			{
				Integer oNumber = Integer.parseInt(pstrStringToInsert);
				
				if(oNumber.intValue() < 0)
				{
					return;
				}
			}
			catch(NumberFormatException ex)
			{
				java.awt.Toolkit.getDefaultToolkit().beep();
				return;
			}
	 
			super.insertString(piParam, pstrStringToInsert, poAttributeSet);
		}
	}
}

// EOF
