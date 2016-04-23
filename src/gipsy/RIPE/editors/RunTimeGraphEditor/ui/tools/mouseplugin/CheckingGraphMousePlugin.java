/**
 * EditingCheckingGraphMousePlugin.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * @version $Revision: 1.4 $ $Date: 2012/06/19 23:22:30 $
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: CheckingGraphMousePlugin.java,v 1.4 2012/06/19 23:22:30 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.mouseplugin;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.apache.commons.collections15.Factory;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.util.ArrowFactory;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.AppLogger;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.GraphElements;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.GraphElements.EdgeFactory;

/**
 * A plugin that can create vertices, undirected edges, using mouse gestures.
 * This plugin allows for user defined behavior to determine whether a vertex or
 * edge should be added to the graph. In addition, parameters for a vertex or
 * edge can be interactively set here prior to being added to the graph.
 * 
 * @author Sleiman Rabah based on Dr. Greg M. Bernstein's code.
 * 
 */
public class CheckingGraphMousePlugin<V, E> extends
        EditingGraphMousePlugin<V, E> implements GraphMouseListener<GIPSYTier>
{

    /**
     * Used for the directed edge creation visual effect during mouse drag
     */
    class ArrowPaintable implements VisualizationServer.Paintable
    {

        public void paint(Graphics g)
        {
            if (arrowShape != null)
            {
                Color oldColor = g.getColor();
                g.setColor(Color.yellow);
                ((Graphics2D) g).fill(arrowShape);
                g.setColor(oldColor);
            }
        }

        public boolean useTransform()
        {
            return false;
        }
    }

    /**
     * Use with EditingCheckingGraphMousePlugin to allow checking of an edge
     * prior to graph addition. Note that the VisualizationViewer parameter is
     * furnished to allow for interactivity with the user.
     * 
     * @author Dr. Greg M. Bernstein
     */
    public interface EdgeChecker<V, E>
    {

        boolean checkEdge(Graph<V, E> g, VisualizationViewer<V, E> vv, E edge,
                V start, V end, EdgeType dir);
    }

    /**
     * Used for the edge creation visual effect during mouse drag
     */
    class EdgePaintable implements VisualizationServer.Paintable
    {

        public void paint(Graphics g)
        {
            if (edgeShape != null)
            {
                Color oldColor = g.getColor();
                g.setColor(Color.black);
                ((Graphics2D) g).draw(edgeShape);
                g.setColor(oldColor);
            }
        }

        public boolean useTransform()
        {
            return false;
        }
    }

    /**
     * Use with EditingCheckingGraphMousePlugin to allow checking of an vertex.
     * 
     * @author Sleiman Rabah
     */
    public interface VertexChecker<V, E>
    {
        // do not allow state creation if there are no countires in the game map
        // object.
        boolean checkCountryCount(VisualizationViewer<V, E> vv);

        boolean isValidStateName(Graph<V, E> mapGraph,
                VisualizationViewer<V, E> graphVisualizer, String stateName);

    }

    protected EdgeChecker<V, E> edgeChecker;

    protected VertexChecker<V, E> vertexChecker;

    public CheckingGraphMousePlugin(Factory<V> vertexFactory,
            Factory<E> edgeFactory)
    {
        this(MouseEvent.BUTTON1_MASK, vertexFactory, edgeFactory);
    }

    /**
     * create instance and prepare shapes for visual effects
     * 
     * @param modifiers
     */
    public CheckingGraphMousePlugin(int modifiers, Factory<V> vertexFactory,
            Factory<E> edgeFactory)
    {
        super(modifiers, vertexFactory, edgeFactory);
        this.vertexFactory = vertexFactory;
        this.edgeFactory = edgeFactory;
        rawEdge.setCurve(0.0f, 0.0f, 0.33f, 100, .66f, -50, 1.0f, 0.0f);
        rawArrowShape = ArrowFactory.getNotchedArrow(20, 16, 8);
        edgePaintable = new EdgePaintable();
        arrowPaintable = new ArrowPaintable();
        this.cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    }

    /**
     * overrided to be more flexible, and pass events with key combinations. The
     * default responds to both ButtonOne and ButtonOne+Shift
     */
    public boolean checkModifiers(MouseEvent e)
    {
        return (e.getModifiers() & modifiers) != 0;
    }

    public void graphClicked(GIPSYTier v, MouseEvent me)
    {
    }

    public void graphPressed(GIPSYTier arg0, MouseEvent arg1)
    {

    }

    public void graphReleased(GIPSYTier v, MouseEvent me)
    {
        v.setPosX(me.getX());
        v.setPosY(me.getY());
    }

    /**
     * If startVertex is non-null, stretch an edge shape between startVertex and
     * the mouse pointer to simulate edge creation
     */
    @SuppressWarnings("unchecked")
    public void mouseDragged(MouseEvent e)
    {
        if (checkModifiers(e))
        {
            if (startVertex != null)
            {
                transformEdgeShape(down, e.getPoint());
                if (edgeIsDirected == EdgeType.DIRECTED)
                {
                    // transformArrowShape(down, e.getPoint());
                }
            }
            VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e
                    .getSource();
            vv.repaint();
        }
    }

    /**
     * If the mouse is pressed in an empty area, create a new vertex there. If
     * the mouse is pressed on an existing vertex, prepare to create an edge
     * from that vertex to another
     */
    @SuppressWarnings("unchecked")
    public void mousePressed(MouseEvent e)
    {
        if (checkModifiers(e))
        {
            final VisualizationViewer<V, E> graphVisualizer = (VisualizationViewer<V, E>) e
                    .getSource();
            final Point2D p = e.getPoint();
            GraphElementAccessor<V, E> pickSupport = graphVisualizer
                    .getPickSupport();
            if (pickSupport != null)
            {
                Graph<V, E> graph = graphVisualizer.getModel().getGraphLayout()
                        .getGraph();
                // set default edge type
                if (graph instanceof DirectedGraph)
                {
                    edgeIsDirected = EdgeType.DIRECTED;
                }
                else
                {
                    edgeIsDirected = EdgeType.UNDIRECTED;
                }

                final V vertex = pickSupport.getVertex(graphVisualizer
                        .getModel().getGraphLayout(), p.getX(), p.getY());
                if (vertex != null)
                { // get ready to make an edge
                    startVertex = vertex;
                    down = e.getPoint();
                    transformEdgeShape(down, down);
                    graphVisualizer.addPostRenderPaintable(edgePaintable);
                    if ((e.getModifiers() & MouseEvent.SHIFT_MASK) != 0
                            && graphVisualizer.getModel().getGraphLayout()
                                    .getGraph() instanceof UndirectedGraph == false)
                    {
                        edgeIsDirected = EdgeType.DIRECTED;
                    }

                }
                else
                { // make a new vertex

                    // Creates state On double click only.
                    if (e.getClickCount() == 2)
                    {

                        boolean okToAdd = true;
                        if (vertexChecker != null)
                        {
                            okToAdd = vertexChecker
                                    .checkCountryCount(graphVisualizer);
                        }
                        if (okToAdd)
                        {
                            // Call the States factory and pops-up the creation
                            // dialog box at the right place.
                            GraphElements.GIPSYTierFactory.getInstance().setPoint(
                                    e.getX(), e.getY());
                            V newVertex = vertexFactory.create();
                            Layout<V, E> layout = graphVisualizer.getModel()
                                    .getGraphLayout();

                            if (newVertex != null)
                            {
                                /*
                                 * if (vertexChecker.isValidStateName(
                                 * graphVisualizer.getGraphLayout() .getGraph(),
                                 * graphVisualizer, ((GIPSYTier) newVertex)
                                 * .getTierName())) {
                                 */
                                // don't allow null state to be created on
                                // mouse
                                // click.
                                graph.addVertex(newVertex);
                                layout.setLocation(newVertex, graphVisualizer
                                        .getRenderContext()
                                        .getMultiLayerTransformer()
                                        .inverseTransform(e.getPoint()));
                                // }

                            }
                            else
                            {
                                AppLogger
                                        .consoleSubAction("Nothing to add!");
                            }
                        }
                    }
                }
            }
            graphVisualizer.repaint();
        }
    }

    /**
     * If startVertex is non-null, and the mouse is released over an existing
     * vertex, create an undirected edge from startVertex to the vertex under
     * the mouse pointer. If shift was also pressed, create a directed edge
     * instead.
     */
    @SuppressWarnings("unchecked")
    public void mouseReleased(MouseEvent e)
    {
        if (checkModifiers(e))
        {
            final VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e
                    .getSource();
            final Point2D p = e.getPoint();
            Layout<V, E> layout = vv.getModel().getGraphLayout();
            GraphElementAccessor<V, E> pickSupport = vv.getPickSupport();
            if (pickSupport != null)
            {
                final V vertex = pickSupport.getVertex(layout, p.getX(),
                        p.getY());
                if (vertex != null && startVertex != null)
                {
                    Graph<V, E> graph = (Graph<V, E>) vv.getGraphLayout()
                            .getGraph();
                    boolean okToAdd = true;
                    // --
                    // gathering info before creating a edge between to
                    // nodes.
                    // --
                    // Build the edge name.
                    GIPSYTier from = (GIPSYTier) startVertex;
                    GIPSYTier to = (GIPSYTier) vertex;
                    String edgeName = "[" + from.getTierName() + ","
                            + to.getTierName() + "]";

                    GraphElements.EdgeFactory.getInstance();
                    EdgeFactory.setEdgeName(edgeName);
                    EdgeFactory.setFromStateId(from.getTierID());
                    EdgeFactory.setToStateId(to.getTierID());

                    E edge = edgeFactory.create();
                    if (edgeChecker != null)
                    {
                        okToAdd = edgeChecker.checkEdge(graph, vv, edge,
                                startVertex, vertex, edgeIsDirected);
                    }
                    if (okToAdd)
                    {
                        graph.addEdge(edge, startVertex, vertex, edgeIsDirected);
                    }
                    vv.repaint();
                }
            }
            startVertex = null;
            down = null;
            edgeIsDirected = EdgeType.UNDIRECTED;
            vv.removePostRenderPaintable(edgePaintable);
            vv.removePostRenderPaintable(arrowPaintable);
        }
    }

    public void setEdgeChecker(EdgeChecker<V, E> edgeChecker)
    {
        this.edgeChecker = edgeChecker;
    }

    public void setVertexChecker(VertexChecker<V, E> vertexChecker)
    {
        this.vertexChecker = vertexChecker;
    }

    /**
     * code lifted from PluggableRenderer to move an edge shape into an
     * arbitrary position
     * 
     * @param down
     *            a point which determines where the mouse has been pressed.
     * @param out
     *            a point which determines where the mouse has been released.
     */
    private void transformEdgeShape(Point2D down, Point2D out)
    {
        float x1 = (float) down.getX();
        float y1 = (float) down.getY();
        float x2 = (float) out.getX();
        float y2 = (float) out.getY();

        AffineTransform xform = AffineTransform.getTranslateInstance(x1, y1);

        float dx = x2 - x1;
        float dy = y2 - y1;
        float thetaRadians = (float) Math.atan2(dy, dx);
        xform.rotate(thetaRadians);
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        xform.scale(dist / rawEdge.getBounds().getWidth(), 1.0);
        edgeShape = xform.createTransformedShape(rawEdge);
    }
}
