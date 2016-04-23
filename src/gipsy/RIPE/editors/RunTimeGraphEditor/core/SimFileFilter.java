/**
 * SimFileFilter.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.2 $ $Date: 2011/08/18 04:17:33 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: SimFileFilter.java,v 1.2 2011/08/18 04:17:33 s_rabah Exp $
 */
package gipsy.RIPE.editors.RunTimeGraphEditor.core;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * SimFileFilter.java
 * <p>
 * A class used to construct a custom file filter to use with JFileChooser.
 * <p>
 * This class extends Java Swing File Chooser in order to customize the file
 * filter.
 * 
 * @author Sleiman Rabah
 * @see FileManager
 */
public class SimFileFilter extends FileFilter
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
    public SimFileFilter(String ext, String description)
    {
        this.extension = ext;
        this.description = description;
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
