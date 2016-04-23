package gipsy.RIPE.editors.RunTimeGraphEditor.ui.dialogs;

import gipsy.RIPE.editors.RunTimeGraphEditor.ApplicationStarter;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.AppConstants;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.FileFilterDialog;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.FileManager;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYPhysicalNode;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.MessageBoxWrapper;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


/**
 * This class is used to create a new XXX. When user clicks on the map editor
 * panel, a input dialog pops up with all the information needed to create a new
 * XXX.
 * 
 * Copyright(c) 2011, GIPSY team.
 * 
 * @author Sleiman Rabah
 * @version $Id: TierPropertyDialog.java,v 1.8 2012/06/20 01:05:26 s_rabah Exp $ 
 */
public class TierPropertyDialog
extends JDialog
{
	/**
	 * For serialization versioning. 
	 */
	private static final long serialVersionUID = -9107226214766646855L;
	
	/**
	 * XXX.
	 */
	private JComboBox oGIPSYNodesComboBox;
	
	/**
	 * XXX.
	 */
	private JComboBox oGIPSYInstancesComboBox;
	
	/**
	 * XXX.
	 */
	private JComboBox oGIPSYTierTypesComboBox;
	
	/**
	 * XXX.
	 */
	private JButton oButtonCancel;
	
	/**
	 * XXX.
	 */
	private JButton oButtonSave;
	
	/**
	 * XXX.
	 */
	private JButton oButtonBrowse;
	
	/**
	 * XXX.
	 */
	private boolean bIsCanceled;
	
	/**
	 * XXX.
	 */
	private boolean bIsNewTier;
	
	/**
	 * XXX.
	 */
	private JCheckBox bIsGMT;
	
	/**
	 * XXX.
	 */
	private Point oLocation;
	
	/**
	 * XXX.
	 */
	private JTextField oTxtTierName;
	
	/**
	 * XXX.
	 */
	private JTextField oTxtHowManyTierPerNode;
	
	/**
	 * XXX.
	 */
	private JTextField oTxtConfigFileLocation;
	
	/**
	 * XXX.
	 */
	private GIPSYTier oGIPSYTier;

 
	/**
	 * Class Constructor.
	 * 
	 * @param poLocation
	 *            the parent of this dialog box.
	 */
	public TierPropertyDialog(Point poLocation)
	{
		super(ApplicationStarter.getMainFrame(), true);
		this.bIsCanceled = false;
		this.oLocation = poLocation;
		this.bIsNewTier = true;
		
		setTitle("Create New Tier");
		
		// --
		this.initComponents();
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(AppConstants.ICON_MAIN_FRMAE));
	}

	/**
	 * XXX.
	 * @param poFrame
	 * @param poGIPSYTier
	 */
	public TierPropertyDialog(JFrame poFrame, GIPSYTier poGIPSYTier)
	{
		super(poFrame, true);
		this.bIsCanceled = false;
		this.bIsNewTier = false;
		this.oGIPSYTier = poGIPSYTier;
		setTitle("Editing Tier: " + poGIPSYTier.getTierName());
		initComponents();
	}

	/**
	 * Creates and initializes the UI components.
	 */
	private void initComponents()
	{
		this.oTxtTierName = new JTextField();
		this.oTxtTierName.setSize(50, 25);
		this.oTxtConfigFileLocation = new JTextField();
		this.oTxtConfigFileLocation.setSize(75, 25);
		this.oTxtHowManyTierPerNode = new JTextField();
		this.oTxtHowManyTierPerNode.setSize(50, 25);
		
		createGIPSYNodesComboBox();
		createInstancesComboBox();
		createTierTypesComboBox();
		this.bIsGMT = new JCheckBox();
		
		this.oButtonSave = new JButton("Save");
		this.oButtonSave.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// get the inputed data.
					bIsCanceled = false;
					showValidateDialog();
				}
			}
		);
		
		this.oButtonBrowse = new JButton("...");
		this.oButtonBrowse.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{					
					browseConfigFile();
				}
			}
		);
		
		this.oButtonCancel = new JButton("Cancel");
		this.oButtonCancel.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// Dispose the state creation dialog.
					bIsCanceled = true;
					setVisible(false);
				}
			}
		);
		
		/*
		 * Load the tier information in order to edit its properties.
		 */
		if(oGIPSYTier != null)
		{
			// Load the state info into the dialog.
			this.oTxtTierName.setText(this.oGIPSYTier.getTierName());
			this.oTxtHowManyTierPerNode.setText(this.oGIPSYTier.getHowManyTierPerNode());
			this.bIsGMT.setSelected(this.oGIPSYTier.isGMT());
			this.oGIPSYTierTypesComboBox.setSelectedItem(this.oGIPSYTier.getTierType());
			this.oTxtConfigFileLocation.setText(this.oGIPSYTier.getConfigFilePath());
		}
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		
		add(buildPanel());
		// XXX: hardcoded size
		setSize(550, 375);
		setLocationRelativeTo(ApplicationStarter.getMainFrame());
		setVisible(true);
	}

	/**
	 * XXX: review comments for GIPSY:
	 * Creates a combo box for the countires.
	 * The list comes from the GameMap object.
	 * 
	 * @see GlobalInstance
	 */
	private void createGIPSYNodesComboBox()
	{
		GIPSYPhysicalNode oSelectedNode = null;
		this.oGIPSYNodesComboBox = new JComboBox();
		this.oGIPSYNodesComboBox.addItem(new String("Please Select"));
		
		for(GIPSYPhysicalNode oGIPSYNode: GlobalInstance.getInstance().getGIPSYNodesList())
		{
			if(this.oGIPSYTier != null)
			{
				if(this.oGIPSYTier.getGIPSYNodeID().equals(oGIPSYNode.getNodeID()))
				{
					oSelectedNode = oGIPSYNode;
				}
			}
			
			this.oGIPSYNodesComboBox.addItem(oGIPSYNode);
		}
		
		if(oSelectedNode != null)
		{
			this.oGIPSYNodesComboBox.setSelectedItem(oSelectedNode);
		}
	}

	/**
	 * Creates a combobox for the instances list.
	 * The list comes from the singleton object.
	 * 
	 * @see GlobalInstance
	 */
	private void createInstancesComboBox()
	{
		GIPSYInstance oSelectedInstance = null;
		this.oGIPSYInstancesComboBox = new JComboBox();
		this.oGIPSYInstancesComboBox.addItem(new String("Please Select"));
		
		for(GIPSYInstance oGIPSYInstance: GlobalInstance.getInstance().getGIPSYInstancesList())
		{
			if(this.oGIPSYTier != null)
			{
				if(this.oGIPSYTier.getGIPSYInstanceID().equals(oGIPSYInstance.getInstanceID()))
				{
					oSelectedInstance = oGIPSYInstance;
				}
			}

			this.oGIPSYInstancesComboBox.addItem(oGIPSYInstance);
		}

		if(oSelectedInstance != null)
		{
			this.oGIPSYInstancesComboBox.setSelectedItem(oSelectedInstance);
		}
	}

	/**
	 * Creates a combobox of tier types.
	 * 
	 * @see AppConstants
	 */
	private void createTierTypesComboBox()
	{
		this.oGIPSYTierTypesComboBox = new JComboBox();
	
		for(String strTierType: AppConstants.TIER_TYPES)
		{
			this.oGIPSYTierTypesComboBox.addItem(strTierType);
		}
	}

	/**
	 * Builds the panel which holds the dialog's components.
	 * 
	 * @return the built panel
	 */
	public JComponent buildPanel()
	{
		// XXX: what's the syntax?
		FormLayout oLayout = new FormLayout
		(
			  "right:[40dlu,pref], 3dlu, 70dlu, 7dlu, "
			+ "right:[40dlu,pref], 3dlu, 70dlu",
			  "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, "
			+ "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, "
			+ "p, 3dlu, p, 3dlu, p, 3dlu, p"
		);
		
		PanelBuilder oBuilder = new PanelBuilder(oLayout);
		oBuilder.setDefaultDialogBorder();
		
		// Fill the table with labels and components.
		CellConstraints oConstraints = new CellConstraints();
		oBuilder.addSeparator("Tier Properties ", oConstraints.xyw(1, 1, 7));
		oBuilder.addLabel("Tier Name:", oConstraints.xy(1, 3));
		oBuilder.add(this.oTxtTierName, oConstraints.xyw(3, 3, 3));
		oBuilder.addLabel("How many:", oConstraints.xy(1, 5));
		oBuilder.add(this.oTxtHowManyTierPerNode, oConstraints.xyw(3, 5, 3));
		oBuilder.addLabel("Instance:", oConstraints.xy(1, 7));
		oBuilder.add(this.oGIPSYInstancesComboBox, oConstraints.xy(3, 7));
		oBuilder.addLabel("Node:", oConstraints.xy(1, 9));
		oBuilder.add(this.oGIPSYNodesComboBox, oConstraints.xy(3, 9));
		oBuilder.addLabel("Tier type:", oConstraints.xy(1, 11));
		oBuilder.add(this.oGIPSYTierTypesComboBox, oConstraints.xy(3, 11));
		oBuilder.addLabel("Config File:", oConstraints.xy(1, 13));
		oBuilder.add(this.oTxtConfigFileLocation, oConstraints.xyw(3, 13, 3));
		oBuilder.add(this.oButtonBrowse, oConstraints.xy(7, 13));
		oBuilder.addLabel("Is GMT:", oConstraints.xy(1, 15));
		oBuilder.add(this.bIsGMT, oConstraints.xy(3, 15));
		oBuilder.add(this.oButtonSave, oConstraints.xy(3, 17));
		oBuilder.add(this.oButtonCancel, oConstraints.xy(5, 17));
		
		return oBuilder.getPanel();
	}

	/**
	 * @return boolean which tells if the dialog has been cancelled or not.
	 */
	public boolean isCanceled()
	{
		return this.bIsCanceled;
	}

	/**
	 * @return boolean which determines whether if the data in the dialog box is
	 *         valid or not.
	 */
	private boolean isTierInfoAreValid()
	{
		boolean bIsValid = true;
		
		String strErrorMessage = "Please correct the following error(s):\n";
		
		if(this.oTxtTierName.getText().trim().isEmpty())
		{
			strErrorMessage += "- You must enter a tier name!";
			bIsValid = false;
		}
		else if(this.oTxtHowManyTierPerNode.getText().trim().isEmpty())
		{
			strErrorMessage += "- You must enter how many tiers you want to allocate per GIPSY Node!";
			bIsValid = false;
		}
		else if(this.oTxtConfigFileLocation.getText().trim().isEmpty())
		{
			strErrorMessage += "- You must select a config file!";
			bIsValid = false;
		}
		else if(this.oGIPSYNodesComboBox.getSelectedIndex() == 0)
		{
			strErrorMessage += "You must select a GIPSY Node!";
			bIsValid = false;
		}
		else if(this.oGIPSYTierTypesComboBox.getSelectedIndex() == 0)
		{
			strErrorMessage += "- You must select a GIPSY Tier type!";
			bIsValid = false;
		}
		else if(this.oGIPSYInstancesComboBox.getSelectedIndex() == 0)
		{
			strErrorMessage += "- You must select a GIPSY Instance!";
			bIsValid = false;
		}
		else if(!this.oTxtConfigFileLocation.getText().trim().contains(".config"))
		{
			strErrorMessage += "- The config file must end with .config!";
			bIsValid = false;
		}
		else if
		(
			GlobalInstance.getInstance().getGIPSYTierByName(this.oTxtTierName.getText().trim()) != null
			&& this.bIsNewTier == true
		)
		{
			strErrorMessage += "- The tier name you entered exist already!";
			bIsValid = false;
			this.oTxtTierName.requestFocus();
		}
		
		if(bIsValid == false)
		{
			MessageBoxWrapper.displayErrorMsg(null, strErrorMessage, getTitle());
		}
		
		if(this.bIsNewTier == false && bIsValid == true)
		{
			// Validates if the tier name exist already.
			// Validates node ID if it already exists
			GIPSYTier oGIPSYTierToUpdate = GlobalInstance.getInstance().getGIPSYTierByName(this.oGIPSYTier.getTierName());
			GIPSYTier oExistingGIPSYTier = GlobalInstance.getInstance().getGIPSYTierByName(this.oTxtTierName.getText().trim());
			if(oExistingGIPSYTier != null && oExistingGIPSYTier != oGIPSYTierToUpdate)
			{
				MessageBoxWrapper.displayErrorMsg(null, "\n- The tier name you entered exist already!", getTitle());
				bIsValid = false;
				this.oTxtTierName.requestFocus();
			}
		}
		
		if(bIsValid == true)
		{
			try
			{
				Integer.parseInt(oTxtHowManyTierPerNode.getText().trim());
			}
			catch(NumberFormatException e)
			{
				MessageBoxWrapper.displayErrorMsg(null, "How many Tiers per Node must be an integer!", getTitle());
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
		if(isTierInfoAreValid())
		{
			setVisible(false);
			if(this.bIsNewTier == false)
			{
				saveTierProperties(this.oGIPSYTier);
			}
		}
		else
		{
			this.bIsCanceled = true;
		}
	}

	/**
	 * Creates a XXX GeoState object, extract and validate the data from entered in
	 * the dialog box.
	 * 
	 * @return XXX GeoState object which contains the data from the dialog box.
	 */
	public GIPSYTier getDialogData()
	{
		GIPSYTier oNewGIPSYTier = null;

		try
		{
			if(isTierInfoAreValid())
			{
				oNewGIPSYTier = new GIPSYTier();
				saveTierProperties(oNewGIPSYTier);
				// Set up the coordinates of the new tier.
				oNewGIPSYTier.setPosX(this.oLocation.x);
				oNewGIPSYTier.setPosY(this.oLocation.y);
			}
		}
		catch(Exception e)
		{
			MessageBoxWrapper.showException(null, "An error occured while saving tier information.", e);
		}
		
		return oNewGIPSYTier;
	}

	/**
	 * XXX.
	 * @param poGIPSYTier
	 */
	private void saveTierProperties(GIPSYTier poGIPSYTier)
	{
		try
		{
			if(isTierInfoAreValid())
			{
				poGIPSYTier.setTierName(this.oTxtTierName.getText().toUpperCase());
				poGIPSYTier.setHowManyTierPerNode(this.oTxtHowManyTierPerNode.getText().trim());
				GIPSYInstance oGIPSYInstance = (GIPSYInstance)this.oGIPSYInstancesComboBox.getSelectedItem();
				Object oSelectedItem = this.oGIPSYNodesComboBox.getSelectedItem();
				
				/*
				 * Save selected node.
				 */
				if(oSelectedItem instanceof GIPSYPhysicalNode)
				{
					GIPSYPhysicalNode oGIPSYNode = (GIPSYPhysicalNode)this.oGIPSYNodesComboBox.getSelectedItem();
					poGIPSYTier.setGIPSYNodeID(oGIPSYNode.getNodeID());
				}
				
				String strTierType = this.oGIPSYTierTypesComboBox.getSelectedItem().toString();
				if(strTierType.equals(AppConstants.PLEASE_SELECT))
				{
					poGIPSYTier.setTierType(strTierType);
				}
				
				poGIPSYTier.setIsGMT(this.bIsGMT.isSelected());
				poGIPSYTier.setGIPSYInstanceID(oGIPSYInstance.getInstanceID());
				poGIPSYTier.setTierType(this.oGIPSYTierTypesComboBox.getSelectedItem().toString());
				poGIPSYTier.setConfigFilePath(this.oTxtConfigFileLocation.getText().trim());
			}
		}
		catch(Exception e)
		{
			MessageBoxWrapper.showException(null, "An error occured while saving tier information.", e);
		}
	}

	/**
	 * XXX.
	 */
	private void browseConfigFile()
	{
		try
		{
			FileManager oFileManager = new FileManager
			(
				this,
				"Browse Config File",
				AppConstants.DST_CONFIG_FILE_DIR,
				new FileFilterDialog
				(
					AppConstants.CONFIG_FILE_EXT,
					AppConstants.CONFIG_FILE_DESCR
				)
			);
			 
			oFileManager.setCurrentAction("Openning");
			String strFileName = oFileManager.getFileName();
			this.oTxtConfigFileLocation.setText(strFileName);
			
			/*File oFileInfo = oFileManager.getSelectedFileInfo();
			if(oFileInfo != null)
			{
				this.oTxtConfigFileLocation.setText(oFileInfo.getPath());
			}*/
		}
		catch(Exception e)
		{
			MessageBoxWrapper.showException
			(
				null,
				"An error occured while trying to get the config file path.",
				e
			);
		}
	}
}

// EOF
