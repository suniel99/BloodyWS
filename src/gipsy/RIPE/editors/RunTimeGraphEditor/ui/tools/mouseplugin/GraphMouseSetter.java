/**
 * EditingModalGraphMouseSetter.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.2 $ $Date: 2011/08/18 04:17:50 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: GraphMouseSetter.java,v 1.2 2011/08/18 04:17:50 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.mouseplugin;

import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.control.EditingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import org.apache.commons.collections15.Factory;

/**
 * A class to add add the customized Editing Grpah Mouse Plug-in to the Jung
 * Library Graph Mouse Manager.
 * 
 * @author Sleiman Rabah based on Dr. Greg M. Bernstein's code.
 */
public class GraphMouseSetter<V, E> extends EditingModalGraphMouse<V, E>
{

    /**
     * Class Constructor.
     * 
     * @param rc
     *            the render context provided to the Jung Library.
     * @param vertexFactory
     *            The vertex Factory which creates States in this project.
     * @param edgeFactory
     *            The Edge Factory which creates connection between states.
     */
    public GraphMouseSetter(RenderContext rc, Factory<V> vertexFactory,
            Factory<E> edgeFactory)
    {
        super(rc, vertexFactory, edgeFactory, 1.1f, 1 / 1.1f);
    }

    /**
     * Class Constructor.
     * 
     * @param rc
     *            the render context provided to the Jung Library.
     * @param vertexFactory
     *            The vertex Factory which creates States in this project.
     * @param edgeFactory
     *            The Edge Factory which creates connection between states.
     * @param in
     * @param out
     */
    public GraphMouseSetter(RenderContext rc, Factory<V> vertexFactory,
            Factory<E> edgeFactory, float in, float out)
    {
        super(rc, vertexFactory, edgeFactory, in, out);
    }

    /**
     * Sets current object mouse plug-in. A mouse plug-in is a customized Mouse
     * Listener which controls the mouse click and Graph Visualizer objects.
     * 
     * @param plug
     *            sets the new defined Editing graph mouse plug-in.
     */
    public void setEditingPlugin(EditingGraphMousePlugin plug)
    {
        editingPlugin = plug;
    }

}
