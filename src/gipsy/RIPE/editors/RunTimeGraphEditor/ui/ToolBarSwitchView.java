/**
 * ToolBarSwitchView.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.2 $ $Date: 2011/08/18 04:17:40 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: ToolBarSwitchView.java,v 1.2 2011/08/18 04:17:40 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui;

import java.awt.Graphics;

import javax.swing.JToolBar;

/**
 * An interface used to change the toolbar when a the user clicks on a tab in
 * the main Panel.
 * 
 * @author Sleiman Rabah
 * 
 */
public interface ToolBarSwitchView
{

    public JToolBar getToolBar();

}
