/**
 * GIPSYInstance.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.2 $ $Date: 2011/08/18 04:17:33 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: GIPSYInstance.java,v 1.2 2011/08/18 04:17:33 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.core;

import java.io.Serializable;

/**
 * A class representing a geographic continent.
 * 
 * @author Sleiman Rabah
 * 
 */
public class GIPSYInstance implements Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 1482614480950313591L;
    private String strInstanceID;
    private String strInstanceName;

    /**
     * Constructor
     * <p>
     * Initialize an object of continent
     */
    public GIPSYInstance()
    {
        this.strInstanceID = "";
        this.strInstanceName = "";
    }

    /**
     * Getter method to obtain the id of continent
     * 
     * @return the id of continent as a string
     */
    public String getInstanceID()
    {
        return strInstanceID;
    }

    /**
     * Setter method to assign an id for this continent
     * 
     * @param continentId
     *            the id of continent
     */
    public void setInstanceID(String continentId)
    {
        this.strInstanceID = continentId;
    }

    public String getInstanceName()
    {
        return strInstanceName;
    }

    public void setInstanceName(String strInstanceName)
    {
        this.strInstanceName = strInstanceName;
    }

    /**
     * Over-written method of toString() for this class
     * 
     * @return the name of the current continent as a String
     */
    public String toString()
    {
        return this.strInstanceName;
    }
}
