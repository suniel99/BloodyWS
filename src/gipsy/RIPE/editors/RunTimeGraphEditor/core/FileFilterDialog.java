/**
 * SimFileFilter.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.2 $ $Date: 2012/06/19 23:22:18 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: FileFilterDialog.java,v 1.2 2012/06/19 23:22:18 s_rabah Exp $
 */
package gipsy.RIPE.editors.RunTimeGraphEditor.core;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * DlgFileFilter.java
 * <p>
 * A class used to construct a custom file filter to use with JFileChooser.
 * <p>
 * This class extends Java Swing File Chooser in order to customize the file
 * filter.
 * 
 * @author Sleiman Rabah
 * @see FileManager
 */
public class FileFilterDialog extends FileFilter
{

    /**
     * A description to display with the file filter.
     */
    private String description;
    /**
     * A file extension which is displayed in the file filter.
     */
    private String extension;

    /**
     * Class Constructor
     * 
     * @param ext
     *            the extension to add to the File filter.
     * @param description
     */
    public FileFilterDialog(String ext, String description)
    {
        this.extension = ext;
        this.description = description;
        System.out.println("browse clicked");
    }

    /**
     * This method apply the files filter on the current directory.
     * 
     * @param file
     *            the file to show if the files filter has been applied.
     * @return a boolean which determines whether the file name ends with the
     *         wanted extension or not.
     */
    public boolean accept(File file)
    {
        String filename = file.getName();
        return filename.endsWith(extension);
    }

    /**
     * Gets the files filter description.
     * 
     * @return the description of the files filter, ex: (Xml files).
     */
    public String getDescription()
    {
        return description;
    }
}
