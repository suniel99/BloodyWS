/**
 * EdgeMenuListener.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.2 $ $Date: 2011/08/18 04:17:47 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: EdgeMenuListener.java,v 1.2 2011/08/18 04:17:47 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools;

import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * An interface for menu items that are interested in knowing the currently
 * selected edge and its visualization component context. Used with
 * PopupStateEdgeMenuMousePlugin.
 * 
 * @author Sleiman Rabah based on Dr. Greg M. Bernstein's code.
 */
public interface EdgeMenuListener<E>
{
    /**
     * Used to set the edge and visualization component.
     * 
     * @param e
     *            The connection which connects two nodes.
     * @param visView
     *            The Visualizer which holds the graph and its elements.
     */
    void setEdgeAndView(E e, VisualizationViewer visView);

}
