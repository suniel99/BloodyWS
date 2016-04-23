/**
 * GraphPanel.java
 * 
 * SOEN6441 class - Simulation Of Total War Project.
 * 
 * Copyright(c) 2010, SOEN6441 Team 8.
 * $Id: GraphPanel.java,v 1.6 2012/06/20 21:36:21 s_rabah Exp $
 */

package gipsy.RIPE.editors.RunTimeGraphEditor.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import org.apache.commons.collections15.functors.ConstantTransformer;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import gipsy.RIPE.editors.RunTimeGraphEditor.ApplicationStarter;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYPhysicalNode;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GIPSYTier;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.GlobalInstance;
import gipsy.RIPE.editors.RunTimeGraphEditor.core.NodeConnection;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.MapMouseMenus;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.MapMouseMenus.GraphViewType;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.PopUpNodeEdgeMenuMousePlugin;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.GraphElements.EdgeFactory;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.GraphElements.GIPSYTierFactory;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.mouseplugin.CheckingGraphMousePlugin;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.tools.mouseplugin.GraphMouseSetter;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers.NodeTextTips;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers.SimGraphMousePlugin;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers.VertexColor;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers.VertexFontTransformer;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers.VertexShapeSizeAspect;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers.VertexStrokeHighlight;
import gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers.VerticesRegister;

/**
 * A class used to create the simulation graph visualizer. <br>
 * Each node
 * 
 * @author s_rabah
 * @version $Revision: 1.6 $ $Date: 2012/06/20 21:36:21 $
 */
public class GraphPanel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2292912671446815179L;
	protected VertexColor<GIPSYTier> oVertexColor;
	protected gipsy.RIPE.editors.RunTimeGraphEditor.ui.transformers.VertexStrokeHighlight<GIPSYTier, String> oVertexHighlight;
	protected VertexFontTransformer<GIPSYTier> oVertexfontTransfomer;
	protected VertexShapeSizeAspect<GIPSYTier, NodeConnection> oVertextShapeSizeAspect;
	protected VisualizationViewer<GIPSYTier, NodeConnection> graphVisualizer;
	protected DefaultModalGraphMouse<GIPSYTier, NodeConnection> graphMouse;
	// --
	private VerticesRegister oVerticesRegister;
	GlobalInstance oGlobalInstance = GlobalInstance.getInstance();
	private float fZoomValue;
	protected SparseMultigraph<GIPSYTier, NodeConnection> mapGraph;
	protected Layout<GIPSYTier, NodeConnection> layout;
	private String[] ostrShapes = { "Triangle", "Square", "Circle", "Pentagon",
			"Star", "Octagon" };
	private static GraphPanel soGraphPanel_INSTANCE;

	/**
	 * Class default constructor.
	 */
	private GraphPanel() {
		fZoomValue = 1.1f;
	}

	/**
	 * Creates the graph visualizer of the game of risk using the visualizer of
	 * the Jung library.
	 * 
	 * @param pfZoomValue
	 *            the value of the zoom: zoom in or zoom out.
	 * @return jp a panel containing the graph visualizer.
	 */
	public JPanel getGraphVisualizer(float pfZoomValue) {

		this.fZoomValue = pfZoomValue;

		oVerticesRegister = new VerticesRegister();
		mapGraph = new SparseMultigraph<GIPSYTier, NodeConnection>();
		layout = new FRLayout<GIPSYTier, NodeConnection>(mapGraph);
		graphVisualizer = new VisualizationViewer<GIPSYTier, NodeConnection>(
				layout);
		// -- Load the graph with data.
		mapGraph = getGraph();
		// --
		oVertexColor = new VertexColor<GIPSYTier>(oVerticesRegister);
		oVertexHighlight = new VertexStrokeHighlight<GIPSYTier, String>(
				oVerticesRegister);

		oVertexfontTransfomer = new VertexFontTransformer<GIPSYTier>();
		oVertexfontTransfomer.setBold(true);
		oVertextShapeSizeAspect = new VertexShapeSizeAspect<GIPSYTier, NodeConnection>(
				mapGraph, oVerticesRegister);
		oVertextShapeSizeAspect.enableShapes(true);

		graphVisualizer.getRenderContext().setVertexFillPaintTransformer(
				oVertexColor);
		graphVisualizer.getRenderContext().setVertexStrokeTransformer(
				oVertexHighlight);
		graphVisualizer.getRenderContext().setVertexFontTransformer(
				oVertexfontTransfomer);
		graphVisualizer.getRenderContext().setVertexShapeTransformer(
				oVertextShapeSizeAspect);
		graphVisualizer.getRenderContext().setEdgeShapeTransformer(
				new EdgeShape.Line<GIPSYTier, NodeConnection>());
		graphVisualizer.getRenderContext().setVertexLabelTransformer(
				new ToStringLabeller<GIPSYTier>());
		graphVisualizer.getRenderContext().setEdgeLabelTransformer(
				new ToStringLabeller<NodeConnection>());
//		graphVisualizer.getRenderer().getVertexLabelRenderer()
//				.setPosition(Renderer.VertexLabel.Position.CNTR);
		//  Set node text position.
		graphVisualizer.getRenderer().getVertexLabelRenderer()
		.setPosition(Renderer.VertexLabel.Position.N);
		graphVisualizer.getRenderContext().setArrowFillPaintTransformer(
				new ConstantTransformer(Color.lightGray));
		graphVisualizer
				.setVertexToolTipTransformer(new NodeTextTips<GIPSYTier>(
						this.oVerticesRegister));

		final ScalingControl scaler = new CrossoverScalingControl();

		scaler.scale(graphVisualizer, pfZoomValue, graphVisualizer.getCenter());
		// --
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		graphVisualizer.setBackground(Color.lightGray);
		graphVisualizer.setForeground(Color.BLACK);
		GraphZoomScrollPane scrollPane = new GraphZoomScrollPane(
				graphVisualizer);
		jp.add(scrollPane);

		// Create a mapGraph mouse and add it to the visualization viewer.
		// -- Prepare and set the plug-in with the nodes and edges factory.
		GraphMouseSetter graphMouse = new GraphMouseSetter(
				graphVisualizer.getRenderContext(),
				GIPSYTierFactory.getInstance(), EdgeFactory.getInstance());

		//
		CheckingGraphMousePlugin oPlugin = new CheckingGraphMousePlugin(
				GIPSYTierFactory.getInstance(), EdgeFactory.getInstance());

		// Remove current map graph mouse current plugin.
		GraphMousePlugin oldPlugin = graphMouse.getEditingPlugin();
		// Remove the jung default plugin.
		graphMouse.remove(oldPlugin);

		// Create the States/Edge mouse pop-up plugin.
		PopUpNodeEdgeMenuMousePlugin loGIPSYTierMenuPopUp = new PopUpNodeEdgeMenuMousePlugin();

		// Add the state popup plugin to the graph mouse controller.
		JPopupMenu edgeMenu = new MapMouseMenus.EdgeMenu();
		JPopupMenu vertexMenu = new MapMouseMenus.GIPSYTiersMenu(
				ApplicationStarter.getMainFrame(), GraphViewType.Operator);
		loGIPSYTierMenuPopUp.setEdgePopup(edgeMenu);
		loGIPSYTierMenuPopUp.setVertexPopup(vertexMenu);
		// Removes the default existing popup.
		graphMouse.remove(graphMouse.getPopupEditingPlugin());
		// Add our new plugin to the mouse
		graphMouse.add(loGIPSYTierMenuPopUp);
		// Set the customized plugin as mouse editing one.
		// NOTE: This plugin was added in order to avoid empty states
		// creation
		// in our case.
		// graphMouse.setEditingPlugin(oPlugin);

		// Add the customized graph mouse controller to the Visualizer.
		graphVisualizer.setGraphMouse(graphMouse);
		// Add a key listener to the editor to be able to switch the Editing
		// mode: Editing/Picking, etc...
		graphVisualizer.addKeyListener(graphMouse.getModeKeyListener());

		graphVisualizer.addGraphMouseListener(new SimGraphMousePlugin());

		graphVisualizer.setSize(900, 900);

		return jp;
	}

	public void refreshGraphView() {
		if (graphVisualizer != null) {
			graphVisualizer.repaint();
		}
	}

	/**
	 * Creates and returns a graph object based on the GameMap content.
	 * 
	 * @return mapGraph a graph containing the nodes and their connections.
	 */
	public SparseMultigraph<GIPSYTier, NodeConnection> getGraph() {

		try {

			// -- Load the continent shapes based on the continent count.
			loadInstanceShapes();

			for (GIPSYTier oGIPSYTier : oGlobalInstance.getGIPSYTiersList()) {

				// -- Add the state to the graph viewer.
				mapGraph.addVertex(oGIPSYTier);
				// --
				Point2D vertexLocation = new Point((int) oGIPSYTier.getPosX(),
						(int) oGIPSYTier.getPosY());
				// -- Set its location.
				graphVisualizer.getGraphLayout().setLocation(oGIPSYTier,
						vertexLocation);
				oVerticesRegister.getSeedVertices().put(oGIPSYTier, oGIPSYTier);
				// --
				oVerticesRegister.getNodesColor().put(
						oGIPSYTier,
						ColorRenderer.getColorCode(oGlobalInstance
								.getGIPSYNodeById(oGIPSYTier.getGIPSYNodeID())
								.getNodeColor()));
				// --
				VerticesRegister.getNodesShape().put(
						oGIPSYTier,
						oVerticesRegister.getInstanceShape().get(
								oGIPSYTier.getGIPSYInstanceID()));
			}

			// --
			for (NodeConnection edge : oGlobalInstance.getNodesConnectionList()) {

				GIPSYTier oGIPSYTierFrom = oGlobalInstance
						.getGIPSYTierByID(edge.getFromTierId());
				GIPSYTier oGIPSYTierTo = oGlobalInstance.getGIPSYTierByID(edge
						.getToTierId());
				// -- Link to node
				mapGraph.addEdge(edge, oGIPSYTierFrom, oGIPSYTierTo);
			}

		} catch (Exception e) {
			System.err
					.println("GraphPanel.java: A probelm has occurred while loading the map into the graph view.");
			e.getStackTrace();
		}

		return mapGraph;
	}

	public void highlightNode(GIPSYTier poGIPSYTier) {
		for (GIPSYTier oGIPSYTier : GlobalInstance.getInstance()
				.getGIPSYTiersList()) {

			if (poGIPSYTier != null) {
				if (oGIPSYTier.getTierID().equals(poGIPSYTier.getTierID())) {
					oVerticesRegister.getNodesToHighlight().put(oGIPSYTier,
							oGIPSYTier.getTierName());
					refreshGraphView();
				}
			}
		}
	}

	public void clearHighlightedNode(GIPSYTier poGIPSYTier) {
		for (GIPSYTier oGIPSYTier : GlobalInstance.getInstance()
				.getGIPSYTiersList()) {

			if (poGIPSYTier != null) {
				if (oGIPSYTier.getTierID().equals(poGIPSYTier.getTierID())) {
					oVerticesRegister.getNodesToHighlight().remove(oGIPSYTier);
					refreshGraphView();
				}
			}
		}
	}
	
	public void clearNodesToHighlight() {

		if (oVerticesRegister.getNodesToHighlight() != null) {
			oVerticesRegister.getNodesToHighlight().clear();
		}
	}

	/**
	 * Load the GIPSY Instance shapes into a Hash Map in order to use it when <br>
	 * rendering the graph layout.
	 * 
	 */
	private void loadInstanceShapes() {

		int j = 0;
		for (GIPSYInstance oGIPSYInstance : GlobalInstance.getInstance()
				.getGIPSYInstancesList()) {
			oVerticesRegister.getInstanceShape().put(
					oGIPSYInstance.getInstanceID(), ostrShapes[j]);
			j++;
		}
	}
	
	
	public static GraphPanel getInstance() {

		if (soGraphPanel_INSTANCE == null) {
			soGraphPanel_INSTANCE = new GraphPanel();
		}

		return soGraphPanel_INSTANCE;
	}
}
