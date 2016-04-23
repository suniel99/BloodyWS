/**
 * MenuPointListener.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.2 $ $Date: 2011/08/18 04:17:47 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: MenuPointListener.java,v 1.2 2011/08/18 04:17:47 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools;

import java.awt.geom.Point2D;

/**
 * Used to set the point at which the mouse was clicked for those menu items
 * interested in this information. Useful, for example, if you want to bring up
 * a dialog box right at the point the mouse was clicked. The
 * PopupStateEdgeMenuMousePlugin checks to see if a menu component implements
 * this interface and if so calls it to set the point.
 * 
 * @author Sleiman Rabah based on Dr. Greg M. Bernstein's code.
 */
public interface MenuPointListener
{

    /**
     * Sets the point of the mouse click.
     * 
     * @param point
     *            which determines the location (x,y) where the mouse has been
     *            clicked.
     */
    void setPoint(Point2D point);

}
