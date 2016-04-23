/**
 * ContinentDialog.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 *   
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: GIPSYInstanceDialog.java,v 1.3 2011/08/18 04:17:53 s_rabah Exp $
 */
package gipsy.RIPE.editors.RunTimeGraphEditor.ui.dialogs;

import gipsy.RIPE.editors.RunTimeGraphEditor.ApplicationStarter;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.AppConstants;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * A class used to create a dialog box for continents creation. <br>
 * Also this class is used to edit a given continent's properties.
 * 
 * @author Sleiman Rabah
 * 
 * @version $Revision: 1.3 $ $Date: 2011/08/18 04:17:53 $
 */
public class GIPSYInstanceDialog extends JDialog
{

    private JButton btnCancel;
    private JButton btnCreate;
    private boolean isCanceled;
    private JTextField txtInstanceName;
    private GIPSYInstance oGIPSYInstance;

    /**
     * Class Constructor.
     */
    public GIPSYInstanceDialog()
    {

        super(ApplicationStarter.getMainFrame(), true);
        this.isCanceled = false;
        this.oGIPSYInstance = null;
        // --
        this.initComponents();
    }

    /**
     * Class constructor.
     * 
     * @param iContinent
     *            a reference to a continent.
     */
    public GIPSYInstanceDialog(GIPSYInstance iContinent)
    {
        super(ApplicationStarter.getMainFrame(), true);

        this.oGIPSYInstance = iContinent;
        this.isCanceled = false;
        // --
        this.initComponents();
    }

    /**
     * Gets the data entered in the dialog box and update the properties of the
     * continent with it.
     */
    public void getEditedData()
    {

        if (!txtInstanceName.getText().isEmpty())
        {

            this.oGIPSYInstance.setInstanceName((String) txtInstanceName
                    .getText().toUpperCase());
        }
    }

    /**
     * Creates a GeoState object, extract and validate the data from entered in
     * the dialog box.
     * 
     * @return GeoState object which contains the data from the dialog box.
     */
    public GIPSYInstance getDialogData()
    {

        GIPSYInstance oNewGIPSYInstance = null;
        if (!txtInstanceName.getText().isEmpty())
        {
            oNewGIPSYInstance = new GIPSYInstance();
            oNewGIPSYInstance.setInstanceName(txtInstanceName.getText()
                    .toUpperCase());
        }

        return oNewGIPSYInstance;
    }

    /**
     * Creates and initializes the UI components.
     */
    private void initComponents()
    {

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(
                AppConstants.ICON_MAIN_FRMAE));

        txtInstanceName = new JTextField();
        txtInstanceName.setSize(50, 25);

        if (this.oGIPSYInstance != null)
        {
            txtInstanceName.setText(this.oGIPSYInstance.getInstanceName());
            setTitle("Editing GIPSY Instance: "
                    + this.oGIPSYInstance.getInstanceName());
        }
        else
        {
            setTitle("Create New GIPSY Instance");
        }

        btnCreate = new JButton("Save");
        btnCreate.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // get the inputed data.
                isCanceled = false;
                showValidateDialog();

            }
        });
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // Dispose the state creation dialog.
                isCanceled = true;
                setVisible(false);
            }
        });
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        this.pack();
        this.add(buildPanel());
        this.setSize(300, 170);
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

        FormLayout layout = new FormLayout(
                "right:[40dlu,pref], 3dlu, 40dlu, 7dlu, "
                        + "right:[20dlu,pref], 3dlu, 30dlu",
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, "
                        + "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, "
                        + "p, 3dlu, p, 3dlu, p, 3dlu, p");

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        // Fill the table with labels and components.
        CellConstraints cc = new CellConstraints();
        builder.addSeparator("Instance Properties ", cc.xyw(1, 1, 7));
        builder.addLabel("Instance Name:", cc.xy(1, 3));
        builder.add(txtInstanceName, cc.xyw(3, 3, 3));
        builder.add(btnCreate, cc.xy(3, 15));
        builder.add(btnCancel, cc.xy(5, 15));

        return builder.getPanel();
    }

    /**
     * 
     * @return boolean which tells if the dialog has been canceled or not.
     */
    public boolean isCanceled()
    {
        return this.isCanceled;
    }

    /**
     * 
     * @return boolean which determines whether if the data in the dialog box is
     *         valid or not.
     */
    private boolean isValidStateDialog()
    {
        boolean bIsValid = true;

        if (txtInstanceName.getText().trim().isEmpty())
        {
            JOptionPane.showMessageDialog(this,
                    "You must enter an instance name!", this.getTitle(), 0,
                    new ImageIcon(AppConstants.ICON_ERROR));
            bIsValid = false;
        }
        else
        {
            if (GlobalInstance.getInstance().getGIPSYInstanceByName(
                    txtInstanceName.getText().trim()) != null)
            {
                JOptionPane.showMessageDialog(this,
                        "The instance name you entered exist already!", this
                                .getTitle(), 0, new ImageIcon(
                                AppConstants.ICON_ERROR));
                bIsValid = false;
            }
        }

        return bIsValid;
    }

    /**
     * Display the input dialog while the data is not valid.
     */
    private void showValidateDialog()
    {
        if (isValidStateDialog())
        {
            setVisible(false);
        }
    }
}
