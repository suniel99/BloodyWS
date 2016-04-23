/**
 * VertexShapeSizeAspect.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 *  
 * Copyright(c) 2010, SOEN6441 Team 8.
 * 
 * $Id: VertexShapeSizeAspect.java,v 1.4 2012/06/20 21:36:25 s_rabah Exp $
 */
package gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers;

import java.awt.Shape;
import java.io.Serializable;
import java.util.HashMap;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.NodeConnection;

/**
 * 
 * A class used to transform/draw a shape of a given graph node.
 * 
 * @author Sleiman Rabah
 * 
 * @version $Revision: 1.4 $ $Date: 2012/06/20 21:36:25 $
 */
public class VertexShapeSizeAspect<V, E> extends
        AbstractVertexShapeTransformer<V> implements Transformer<V, Shape>,
        Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = -6378069502604960205L;
    /**
     * Flag to enable the customized shape drawing.
     */
    protected boolean customized_shapes = false;
    /**
     * Register which contains the graph node and their colors ans shapes.
     */
    private VerticesRegister verticesRegister;
    /**
     * A Hash Map containing states and their respective shape.
     */
    private HashMap<GIPSYTier, String> nodesShape;
    /**
     * The graph representing the game of risk.
     */
    protected Graph<GIPSYTier, NodeConnection> graph;

    /**
     * 
     * @param graphIn
     *            the graph containing the risk game graph.
     * @param verticesRegister
     *            data structure which contains the node shapes and the list of
     *            nodes to change their shape.
     */
    public VertexShapeSizeAspect(Graph<GIPSYTier, NodeConnection> graphIn,
            VerticesRegister verticesRegister)
    {

        this.graph = graphIn;
        this.nodesShape = verticesRegister.getNodesShape();

        setSizeTransformer(new Transformer<V, Integer>()
        {

            public Integer transform(V v)
            {

                return (int) (2 * 20) + 20;
            }
        });
    }

    /**
     * Enable the special shapes drawing.
     * 
     * @param use
     *            determines whether the customized shapes must be enabled or
     *            not.
     */
    public void enableShapes(boolean use)
    {
        this.customized_shapes = use;
    }

    /**
     * Transform the shape of a given node in order to draw a specific
     * shape(Star, Circle, etc).
     * 
     * @param v
     *            a generic graph node.
     * @return shapeObj a shape object representing the new shape to draw.
     */
    public Shape transform(V v)
    {

        Shape shapeObj = null;

        if (customized_shapes)
        {

            String shape = nodesShape.get(v);

            if (shape != null)
            {
                if (shape.equals("Circle"))
                {
                    shapeObj = factory.getEllipse(v);
                }
                else if (shape.equals("Star"))
                {
                    shapeObj = factory.getRegularStar(v, 7);
                }
                else if (shape.equals("Triangle"))
                {
                    shapeObj = factory.getRegularStar(v, 11);
                }
                else if (shape.equals("Pentagon"))
                {
                    shapeObj = factory.getRegularPolygon(v, 5);
                }
                else if (shape.equals("Octagon"))
                {
                    shapeObj = factory.getRegularPolygon(v, 8);
                }
                else if (shape.equals("Square"))
                {
                    shapeObj = factory.getRegularPolygon(v, 4);
                }
            }

        }
        return shapeObj;
    }
}