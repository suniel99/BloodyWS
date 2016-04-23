/**
 * StateMenuListener.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.1 $ $Date: 2011/08/22 03:38:57 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: GIPSYTierMenuListener.java,v 1.1 2011/08/22 03:38:57 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools;

import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * Used to indicate that this class wishes to be told of a selected vertex along
 * with its visualization component context. Note that the VisualizationViewer
 * has full access to the graph and layout.
 * 
 * @author Sleiman Rabah based on Dr. Greg M. Bernstein's code.
 */
public interface GIPSYTierMenuListener<V>
{
    void setGIPSYTierAndView(V v, VisualizationViewer visView);
}
