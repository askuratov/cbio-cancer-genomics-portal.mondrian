// $Id: NodeViewUtil.java,v 1.4 2008/07/07 20:08:15 grossb Exp $
//------------------------------------------------------------------------------
/** Copyright (c) 2008 Memorial Sloan-Kettering Cancer Center.
 **
 ** This library is free software; you can redistribute it and/or modify it
 ** under the terms of the GNU Lesser General Public License as published
 ** by the Free Software Foundation; either version 2.1 of the License, or
 ** any later version.
 **
 ** This library is distributed in the hope that it will be useful, but
 ** WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 ** MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 ** documentation provided hereunder is on an "as is" basis, and
 ** Memorial Sloan-Kettering Cancer Center
 ** has no obligations to provide maintenance, support,
 ** updates, enhancements or modifications.  In no event shall
 ** Memorial Sloan-Kettering Cancer Center
 ** be liable to any party for direct, indirect, special,
 ** incidental or consequential damages, including lost profits, arising
 ** out of the use of this software and its documentation, even if
 ** Memorial Sloan-Kettering Cancer Center
 ** has been advised of the possibility of such damage.  See
 ** the GNU Lesser General Public License for more details.
 **
 ** You should have received a copy of the GNU Lesser General Public License
 ** along with this library; if not, write to the Free Software Foundation,
 ** Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 **/
package org.mskcc.mondrian.internal.colorgradient.cytoscape;

//import org.mskcc.mondrian.util.GlobalHotkeyManager;
//import org.mskcc.mondrian.heatmap.HeatmapUtil;
//import org.mskcc.mondrian.gui.HeatmapConfigDialog;
//import org.mskcc.mondrian.data_types.DataTypeMatrix;
//import org.mskcc.mondrian.data_types.DataTypeMatrixManager;
//import org.mskcc.mondrian.color_gradient.ColorGradientMapper;
//import org.mskcc.mondrian.configuration.MondrianConfiguration;
//import org.mskcc.mondrian.configuration.MondrianConfigurationImp;
//
//import cytoscape.CyNode;
//import cytoscape.Cytoscape;
//import cytoscape.view.CyNetworkView;
//import giny.view.NodeView;
//import ding.view.DNodeView;
//import ding.view.NodeContextMenuListener;
//import cytoscape.render.stateful.CustomGraphic;

import java.awt.Shape;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

/**
 * Creates multiple data type nodes.
 *
 * @author Benjamin Gross
 */
public class NodeViewUtil { // implements NodeContextMenuListener {
//
//	// some statics
//	private static final double CUSTOM_GRAPHIC_Y_OFFSET = 5.0D;
//	private static final double CUSTOM_GRAPHIC_CELL_WIDTH = 20.0D; // not including borders
//	private static final double CUSTOM_GRAPHIC_CELL_HEIGHT = 20.0D; // not including borders
//	private static final double CUSTOM_GRAPHIC_BORDER_THICKNESS = 1.0D;
//	private final static String DISPLAY_MULTIDATA_TYPE_NODES = "DISPLAY_MULTIDATA_TYPE_NODES";
//    private static final String CONTEXT_MENU_TITLE = "Configure heatmap";
//
//	// some required refs
//	private static final DataTypeMatrixManager dataTypeMatrixManager = DataTypeMatrixManager.getInstance();
//	private static final Map<NodeView, List<CustomGraphic>> customGraphicsMap = new HashMap<NodeView,  List<CustomGraphic>>();
//	private static final KeyStroke displayMultiDataTypeNodesPress = KeyStroke.getKeyStroke(KeyEvent.VK_F3, KeyEvent.SHIFT_MASK, false);
//
//	/**
//	 * Action called when DISPLAY_MULTIDATA_TYPE_NODES key press
//	 * keys are pressed.
//	 */
//	private static final Action displayMultiDataTypeNodesAction = new AbstractAction() {
//		public void actionPerformed(ActionEvent e) {
//			final MondrianConfiguration mondrianConfiguration = MondrianConfigurationImp.getInstance();
//			mondrianConfiguration.setDisplayMultipleDataTypeNodes(!mondrianConfiguration.getDisplayMultipleDataTypeNodes());
//			javax.swing.SwingUtilities.invokeLater(new Runnable() {
//				public void run(){
//					mondrianConfiguration.remapCyNetwork();
//				}
//			});
//		}
//	};
//
//	static {
//		GlobalHotkeyManager hotkeyManager = GlobalHotkeyManager.getInstance();
//		hotkeyManager.getInputMap().put(displayMultiDataTypeNodesPress, DISPLAY_MULTIDATA_TYPE_NODES);
//		hotkeyManager.getActionMap().put(DISPLAY_MULTIDATA_TYPE_NODES, displayMultiDataTypeNodesAction);
//	}
//	
//	/**
//	 * Method to get instance of NodeViewUtil.
//	 *
//	 * @return NodeViewUtil
//	 */
//	public static NodeViewUtil getInstance() {
//		return new NodeViewUtil();
//	}
//
//    /**
//     * Our implementation of NodeContextMenuListener.addNodeContextMenuItems(..).
//     */
//    public void addNodeContextMenuItems(final NodeView nodeView, JPopupMenu menu) {
//
//        // check if we have already added menu item
//        if (contextMenuExists(menu)) return;
//
//		final MondrianConfiguration mondrianConfiguration = MondrianConfigurationImp.getInstance();
//		if (mondrianConfiguration.getActiveHeatmapPanelConfiguration() == null) return;
//
//        // add new menu item
//        final javax.swing.JMenuItem item = new javax.swing.JMenuItem(new AbstractAction(CONTEXT_MENU_TITLE) {
//			public void actionPerformed(ActionEvent e) {
//                javax.swing.SwingUtilities.invokeLater(new Runnable() {
//                    public void run() {
//                        try {
//							final HeatmapConfigDialog dialog =
//								HeatmapConfigDialog.getInstance(Cytoscape.getDesktop(),
//																"Configure Heatmap",
//																true,
//																mondrianConfiguration.getActiveHeatmapPanelConfiguration().getHeatmapPanel(),
//																mondrianConfiguration.getActiveHeatmapPanelConfiguration(),
//                                        mondrianConfiguration,
//																nodeView);
//							dialog.setLocationRelativeTo(Cytoscape.getDesktop());
//							dialog.setVisible(true);
//                        }
//                        catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
//        menu.add(item);
//	}
//
//	/**
//	 * Method to set network - node tool tips
//	 *
//     * @param mondrianConfiguration MondrianConfiguration
//	 */
//	public static void setNetworkToolTips(MondrianConfiguration mondrianConfiguration) {
//
//		// used below
//		final java.text.NumberFormat formatter = new java.text.DecimalFormat("#,###,###.##");
//
//		// setup some refs
//		if (mondrianConfiguration.getCyNetwork() == null) return;
//		final CyNetworkView networkView = Cytoscape.getNetworkView(mondrianConfiguration.getCyNetwork().getIdentifier());
//
//		// some sanity checks
//		if (networkView == null) return;
//		if (mondrianConfiguration.getActiveHeatmapPanelConfiguration() == null) return;
//
//		// get sample
//		final String sample = mondrianConfiguration.getActiveHeatmapPanelConfiguration().getCurrentSample();
//
//		// get loaded data types
//		final Vector<String> dataTypes = dataTypeMatrixManager.getLoadedDataTypes(false);
//
//		// get cynode id vector from heatmap utils (this will filter out non-protein and related)
//		Vector<String> cyNodeIDVector = HeatmapUtil.getCyNodeIdVector(mondrianConfiguration.getCyNetwork());
//		for (String cyNodeID : cyNodeIDVector) {
//			CyNode node = Cytoscape.getCyNode(cyNodeID, false);
//			NodeView nodeView = networkView.getNodeView(node);
//			String toolTip = "<html>Sample: " + sample + "<br>";
//			// for each loaded data type, get measurement for give cynode & current sample
//			for (String dataType : dataTypes) {
//				DataTypeMatrix matrix = dataTypeMatrixManager.getDataTypeMatrix(dataType);
//				String gene = org.mskcc.mondrian.maps.GeneMapper.getGene(matrix, cyNodeID, Cytoscape.getNodeAttributes());
//				Double measurement = matrix.getMeasurement(gene, sample);
//				String measurementStr = (measurement == null) ? "None" : formatter.format(measurement);
//				measurementStr = (measurementStr.equalsIgnoreCase("NaN")) ? "None" : measurementStr;
//				toolTip += dataType + ": " + measurementStr + "<br>";
//			}
//			toolTip += "</html>";
//			if (nodeView != null) {
//				nodeView.setToolTip(toolTip);
//			}
//		}
//	}
//
//    /**
//     * Updates the multiple data type node.
//	 *
//     * @param mondrianConfiguration MondrianConfiguration
//     */
//    public static void updateCustomNodes(MondrianConfiguration mondrianConfiguration) {
//
//		// setup some refs
//		final CyNetworkView networkView = Cytoscape.getCurrentNetworkView();
//
//		// some santy checks
//		if (networkView == null) return;
//		if (mondrianConfiguration.getActiveHeatmapPanelConfiguration() == null) return;
//
//		// get vector of protein/related nodes only
//		final Vector<String> cyNodeIdVector = HeatmapUtil.getCyNodeIdVector(mondrianConfiguration.getCyNetwork());
//
//		// iterate over nodes
//		for (String cyNodeId : cyNodeIdVector) {
//
//			// get node view
//			final CyNode cyNode = Cytoscape.getCyNode(cyNodeId, false);
//			final NodeView nodeView = networkView.getNodeView(cyNode);
//			if (nodeView == null) continue;
//
//			// remove current graphics from DNodeView
//			removeCustomGraphics(nodeView);
//
//			if (mondrianConfiguration.getDisplayMultipleDataTypeNodes()) {
//				// get list of customGraphics
//				List<CustomGraphic> customGraphics = getCustomGraphicsList(cyNode, nodeView, mondrianConfiguration);
//				// update our map
//				addCustomGraphics(nodeView, customGraphics);
//			}
//		}
//    }
//	
//	/**
//	 * Method to remove custom graphics from DNodeView.
//	 *
//	 * @param nodeView NodeView
//	 */
//	private static void removeCustomGraphics(NodeView nodeView) {
//
//		if (customGraphicsMap.containsKey(nodeView)) {
//			List<CustomGraphic> customGraphicsList = customGraphicsMap.get(nodeView);
//			if (customGraphicsList == null) return;
//			for (CustomGraphic customGraphic : customGraphicsList) {
//				((DNodeView)nodeView).removeCustomGraphic(customGraphic);
//			}
//			customGraphicsMap.put(nodeView, null);
//		}
//	}
//
//	/**
//	 * Method to remove custom graphics from DNodeView.
//	 *
//	 * @param nodeView NodeView
//	 * @param customGraphicsList List<CustomGraphic>
//	 */
//	private static void addCustomGraphics(NodeView nodeView, List<CustomGraphic> customGraphicsList) {
//
//		// add custom graphics to DNodeView
//		for (CustomGraphic customGraphic : customGraphicsList) {
//			((DNodeView)nodeView).addCustomGraphic(customGraphic);
//		}
//
//		// add custom graphics to our map
//		customGraphicsMap.put(nodeView, customGraphicsList);
//	}
//
//	/**
//	 * Method which constructors a list of custom graphics objects
//	 *
//	 * @param cyNode CyNode
//	 * @param nodeView NodeView
//     * @param mondrianConfiguration MondrianConfiguration
//	 * @return List<CustomGraphic>
//	 */
//	private static List<CustomGraphic> getCustomGraphicsList(CyNode cyNode, NodeView nodeView, MondrianConfiguration mondrianConfiguration) {
//
//		final List<CustomGraphic> toReturn = new ArrayList<CustomGraphic>();
//
//		// get current sample
//		final String sample = mondrianConfiguration.getActiveHeatmapPanelConfiguration().getCurrentSample();
//
//		// interate over data types creating 
//		final int numMatrices = dataTypeMatrixManager.getNumberMatricesLoaded();
//		final Vector<String> dataTypes = dataTypeMatrixManager.getLoadedDataTypes(false);
//		for (String dataType : dataTypes) {
//			final DataTypeMatrix matrix = dataTypeMatrixManager.getDataTypeMatrix(dataType);
//			final String gene = org.mskcc.mondrian.maps.GeneMapper.getGene(matrix,
//																		cyNode.getIdentifier(),
//																		cytoscape.Cytoscape.getNodeAttributes());
//			final Double measurement = matrix.getMeasurement(gene, sample);
//			final Color color = (gene != null && measurement != null)  ?
//				ColorGradientMapper.getColorGradient(mondrianConfiguration, gene, measurement, matrix) :
//				mondrianConfiguration.getColorTheme().getNoDataColor();
//			final Shape customGraphicShape = getCustomGraphicShape(numMatrices, dataTypes.indexOf(dataType), nodeView);
//
//			// add new custom graphic shape to return list
//			toReturn.add(new CustomGraphic(customGraphicShape, color, cytoscape.render.stateful.NodeDetails.ANCHOR_CENTER));
//		}
//
//		// get border, add to begining of list
//		if (toReturn.size() > 0) {
//			toReturn.add(0, getCustomGraphicBorder(nodeView, toReturn));
//		}
//
//		// outta here
//		return toReturn;
//	}
//
//	/**
//	 * Method which constructors CustomGraphic Shape.
//	 *
//	 * @param int totalNumberMatrices
//	 * @param int currentMatrixIndex
//	 * @param nodeView NodeView
//	 * @return Shape
//	 */
//	private static Shape getCustomGraphicShape(int totalNumberMatrices, int currentMatrixIndex, NodeView nodeView) {
//
//		// compute offsets
//		final double totalCustomGraphicWidth = totalCustomGraphicWidth(totalNumberMatrices);
//		final double xOffset = ((-0.5D * totalCustomGraphicWidth) +
//								(CUSTOM_GRAPHIC_CELL_WIDTH * currentMatrixIndex) +
//								(CUSTOM_GRAPHIC_BORDER_THICKNESS * (currentMatrixIndex+1)));
//		final double yOffset = nodeView.getHeight() * 0.5D + CUSTOM_GRAPHIC_Y_OFFSET + CUSTOM_GRAPHIC_BORDER_THICKNESS;
//		return new Rectangle2D.Double(xOffset,
//									  yOffset,
//									  CUSTOM_GRAPHIC_CELL_WIDTH,
//									  CUSTOM_GRAPHIC_CELL_HEIGHT - CUSTOM_GRAPHIC_BORDER_THICKNESS * 2);
//	}
//
//	/**
//	 * Method which returns CustomGraphics for borders.
//	 *
//	 * @param nodeView NodeView
//	 * @param customGraphicsList List<CustomGraphic>
//	 * @return CustomGraphic
//	 */
//	private static CustomGraphic getCustomGraphicBorder(NodeView nodeView, List<CustomGraphic> customGraphicsList) {
//
//		// compute border width
//		final double borderWidth = totalCustomGraphicWidth(customGraphicsList.size());
//
//		// compute offsets
//		final double xOffset = -0.5D * borderWidth;
//		final double yOffset = nodeView.getHeight() * 0.5D + CUSTOM_GRAPHIC_Y_OFFSET;
//
//		// create shape
//		final Shape shape = new Rectangle2D.Double(xOffset, yOffset, borderWidth, CUSTOM_GRAPHIC_CELL_HEIGHT);
//
//		// outta here
//		return new CustomGraphic(shape, Color.BLACK, cytoscape.render.stateful.NodeDetails.ANCHOR_CENTER);
//	}
//
//	/**
//	 * Get total width of cells
//	 *
//	 * @param int totalNumberMatrices
//	 * @return double
//	 */
//	private static double totalCustomGraphicWidth(int totalNumberMatrices) {
//
//		return (totalNumberMatrices * CUSTOM_GRAPHIC_CELL_WIDTH) + (totalNumberMatrices + 1) * CUSTOM_GRAPHIC_BORDER_THICKNESS;
//	}
//
//    /**
//     * Method checks if we have already added a neighborhood map context menu
//     * to given menu.
//     *
//     * @param menu JPopupMenu
//     * @return boolean
//     */
//    private boolean contextMenuExists(JPopupMenu menu) {
//
//        for (javax.swing.MenuElement element : menu.getSubElements()) {
//            java.awt.Component component = element.getComponent();
//            if (component instanceof javax.swing.JMenuItem) {
//                String text = ((javax.swing.JMenuItem) component).getText();
//                if (text != null && text.equals(CONTEXT_MENU_TITLE)) return true;
//            }
//        }
//
//        // outta here
//        return false;
//    }
}