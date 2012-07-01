// $Id: ColorGradientMapper.java,v 1.1 2008/06/06 15:30:13 grossb Exp $
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
package org.mskcc.mondrian.internal.colorgradient;

import org.mskcc.mondrian.internal.configuration.ConfigurationChangedEvent;
import org.mskcc.mondrian.internal.configuration.MondrianConfigurationListener;

//import cytoscape.Cytoscape;
//import cytoscape.visual.VisualStyle;
//import cytoscape.view.CyNetworkView;
//import cytoscape.visual.VisualMappingManager;
//
//import org.mskcc.mondrian.data_types.DataTypeMatrix;
//import org.mskcc.mondrian.configuration.MondrianConfiguration;
//import org.mskcc.mondrian.configuration.ConfigurationChangedEvent;
//import org.mskcc.mondrian.configuration.MondrianConfigurationListener;
//import org.mskcc.mondrian.colorgradient.cytoscape.NodeViewUtil;
//
//import java.awt.Color;

/**
 * Maps color gradient onto network views.
 *
 * @author Ethan Cerami, Benjamin Gross
 */
public class ColorGradientMapper implements MondrianConfigurationListener {

	@Override
	public void configurationChanged(ConfigurationChangedEvent evt) {
		// TODO Auto-generated method stub
		
	}

//	/**
//	 * Method to get instance of ColorGradientMapper.
//	 *
//	 * @return ColorGradientMapper
//	 */
//	public static ColorGradientMapper getInstance() {
//		return new ColorGradientMapper();
//	}
//
//	/**
//	 * Method is called whenever the configuration changes
//	 *
//	 * @param evt ConfigurationChangedEvent
//	 */
//	public void configurationChanged(ConfigurationChangedEvent evt) {
//
//		if (evt.rangeChanged() || evt.networkChanged() || evt.colorThemeChanged() ||
//			evt.dataTypeChanged() || evt.heatmapPanelConfigurationChanged() || evt.applyVizStyle()) {
//
//			MondrianConfiguration mondrianConfiguration = (MondrianConfiguration)evt.getSource();
//			if (mondrianConfiguration.getDataTypeMatrix() == null) {
//				final CyNetworkView nv = Cytoscape.getCurrentNetworkView();
//				if (nv != null) nv.redrawGraph(false, true);
//			}
//			else {
//				// get ref to visual mapping manager
//				final VisualMappingManager manager = Cytoscape.getVisualMappingManager();
//				final VisualStyleFactory vsFactory = VisualStyleFactory.getInstance(mondrianConfiguration);
//				final VisualStyle style = vsFactory.getVisualStyle();
//				final CyNetworkView nv = Cytoscape.getCurrentNetworkView();
//				if (nv != null) {
//					if (style != null) {
//						nv.setVisualStyle(style.getName());
//						manager.setVisualStyle(style);
//						nv.applyVizmapper(style);
//					}
//					NodeViewUtil.updateCustomNodes(mondrianConfiguration);
//					nv.redrawGraph(false, true);
//					NodeViewUtil.setNetworkToolTips(mondrianConfiguration); // must come after redraw graph to render properly
//				}
//			}
//        }
//    }
//
//	/**
//	 * Gets color gradient (as hex string) for given gene/sample/data type.
//	 *
//     * @param mondrianConfiguration MondrianConfiguration
//	 * @param gene String
//	 * @param measurement Double
//	 * @param matrix DataTypeMatrix
//	 * @return Color
//	 */
//	public static Color getColorGradient(MondrianConfiguration mondrianConfiguration, String gene, Double measurement, DataTypeMatrix matrix) {
//
//		// sparse matrix files will contain NaN value when no data is available
//		if (measurement.equals(Double.NaN)) return mondrianConfiguration.getColorTheme().getNoDataColor();
//
//		// sanity check
//		final cytoscape.visual.mappings.ContinuousMapping continuousMapping =
//			org.mskcc.mondrian.color_gradient.VisualStyleFactory.getContinuousMapping(mondrianConfiguration.getColorTheme(),
//																						 mondrianConfiguration.getColorGradientRange(matrix));
//		if (continuousMapping == null) return mondrianConfiguration.getColorTheme().getNoDataColor();
//		final java.util.Map<String, Double> attrBundle = new java.util.HashMap<String, Double>();
//		attrBundle.put(gene, measurement);
//		final cytoscape.visual.mappings.continuous.ContinuousRangeCalculator calculator =
//			new cytoscape.visual.mappings.continuous.ContinuousRangeCalculator(continuousMapping.getAllPoints(),
//																			   continuousMapping.getInterpolator(),
//																			   attrBundle);
//		return (Color)calculator.calculateRangeValue(gene);
//	}
//
//	/**
//	 * Gets color gradient (as hex string) for given gene/sample/data type.
//	 *
//     * @param mondrianConfiguration MondrianConfiguration
//	 * @param gene String
//	 * @param measurement Double
//	 * @param matrix DataTypeMatrix
//	 * @return String
//	 */
//	public static String getColorGradientAsString(MondrianConfiguration mondrianConfiguration, String gene, Double measurement, DataTypeMatrix matrix) {
//
//		final Color color = getColorGradient(mondrianConfiguration, gene, measurement, matrix);
//		final int red  = color.getRed();
//		String redColor = Integer.toHexString(red);
//		if (red <= 9) redColor = "0" + redColor;
//		final int green = color.getGreen();
//		String greenColor = Integer.toHexString(green);
//		if (green <= 9) greenColor = "0" + greenColor;
//		final int blue = color.getBlue();
//		String blueColor = Integer.toHexString(blue);
//		if (blue <= 9) blueColor = "0" + blueColor;
//
//		// outta here
//		return (redColor + greenColor + blueColor);
//	}
//
//    /**
//     * Constructor (private).
//     */
//    private ColorGradientMapper() {
//    }
}
