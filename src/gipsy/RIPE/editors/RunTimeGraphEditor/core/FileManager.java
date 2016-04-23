/**
 * FileManager.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * A Singleton class representing the File Menu.
 * 
 * @version $Revision: 1.3 $ $Date: 2012/06/19 23:22:18 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: FileManager.java,v 1.3 2012/06/19 23:22:18 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.core;

import gipsy.RIPE.editors.RunTimeGraphEditor.ui.AppLogger;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;

/**
 * A class used to pop up the JFile Chooser.
 * <p>
 * The File Chooser pops up and apply its File Filter attribute in order display	
 * only Xml files in the Dialog.
 * 
 * @author Sleiman Rabah
 * 
 */
public class FileManager
{

    private String currentAction;
    private String dialogTitle;
    private JFileChooser fileChooser;
    private FileFilterDialog fileFilter;
    private boolean isCanceled;
    private String mainFolder;
    private Component parentComponent;
    private File oSelectedFileInfo;

    public FileManager(Component parent, String iDialogTitle, String folder,
            FileFilterDialog filter)
    {
        this.dialogTitle = iDialogTitle;
        this.parentComponent = parent;
        this.mainFolder = folder;
        this.fileFilter = filter;
        this.isCanceled = false;
        oSelectedFileInfo = null;
    }

    /**
     * 
     * Creates a file chooser dialog and sets its informations. This can be used
     * to either open or save a file.
     * 
     * @return the file name to save or load.
     */
    public String getFileName()
    {

        String fileName = null;
        try
        {

            if (mainFolder != "")
            {
                File dir = new File(mainFolder);

                fileChooser = new JFileChooser(dir);
            }
            else
            {
                fileChooser = new JFileChooser();
            }

            fileChooser.setDialogTitle(dialogTitle);
            fileChooser.addChoosableFileFilter(fileFilter);

            int returnVal = fileChooser.showOpenDialog(parentComponent);
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = fileChooser.getSelectedFile();
                fileName = file.getName();
                oSelectedFileInfo = file;
                // Appends the folder name to the file name.
                fileName = mainFolder + fileName;
                AppLogger.consoleSubAction(currentAction + "  : " + fileName);

            }
            else
            {
                AppLogger.consoleSubAction(currentAction
                        + " command cancelled by user.");
                this.isCanceled = true;
            }
        }
        catch (Exception e)
        {
            AppLogger.consoleSubAction(currentAction + " failed.");
            AppLogger.consoleSubAction(e.getMessage());
            // --
            AppLogger.log(currentAction + " failed.");
            AppLogger.log(e.getMessage());
        }

        return fileName;
    }

    /**
     * Gets the current action of the JFile Chooser.
     * 
     * @return the currentAction.
     */
    public String getCurrentAction()
    {
        return currentAction;
    }

    /**
     * Sets the isCanceled flag.
     * 
     * @return the isCanceled, a flag that tells whether if the JFile chooser
     *         has been canceled or not.
     */
    public boolean isCanceled()
    {
        return isCanceled;
    }

    /**
     * Sets the current action/intention to use the JFile Chooser.
     * 
     * @param currentAction
     *            the currentAction to set, the current action may be Opening or
     *            Saving.
     */
    public void setCurrentAction(String currentAction)
    {
        this.currentAction = currentAction;
    }

    /**
     * @return the oSelectedFileInfo
     */
    public File getSelectedFileInfo()
    {
		return oSelectedFileInfo;
	}

}
