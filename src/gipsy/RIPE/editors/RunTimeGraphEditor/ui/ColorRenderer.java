/**
 * ColorRenderer.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 *   
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: ColorRenderer.java,v 1.3 2012/06/19 23:22:22 s_rabah Exp $
 */
package gipsy.RIPE.editors.RunTimeGraphEditor.ui;

import gipsy.RIPE.editors.RunTimeGraphEditor.core.AppConstants;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYPhysicalNode;

import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * A Class which manages graph nodes colors at run-time.
 * 
 * @author Sleiman Rabah
 * 
 * @version $Revision: 1.3 $ $Date: 2012/06/19 23:22:22 $
 */
public class ColorRenderer extends DefaultListCellRenderer implements
        Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 2372319165119921173L;

    /**
     * Sets the background of a given JList element to the color provided as
     * parameter.
     * 
     * @param list
     *            the list of the elements to render.
     * @param value
     *            the element to render.
     * @param index
     *            the index of the element to render.
     * @param isSelected
     *            boolean indicating whether the element is selected or not.
     * @param cellHasFocus
     *            if the cell has focus.
     * @return this a customized list cell render.
     */
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus)
    {

        super.getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);

        GIPSYPhysicalNode oColor = (GIPSYPhysicalNode) value;
        String strColor = (String) oColor.getNodeColor();
        setBackground(getColorCode(strColor));

        return this;
    }

    /**
     * This method is used to return a Java.awt Color code based on the string
     * received as parameter.
     * 
     * @param strColor
     *            the string indicating the color.
     * @return color the color code depending on what has been provided as
     *         color.
     */
    public static Color getColorCode(String strColor)
    {
       return GIPSYColors.getColorByName(strColor);
    }
}
