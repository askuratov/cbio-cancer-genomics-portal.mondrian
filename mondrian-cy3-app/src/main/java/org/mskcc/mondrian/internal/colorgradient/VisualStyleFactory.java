// $Id: VisualStyleFactory.java,v 1.2 2008/07/09 20:44:02 grossb Exp $
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

// imports

import java.awt.Color;

/**
 * Utility for creating color gradients.
 *
 * @author Ethan Cerami, Benjamin Gross
 */
public class VisualStyleFactory {
//
//	// node attribute which is set to changes node size when focused
//	public static final String DATA_MAPPER_FOCUSED_NODE = GUIConfig.PLUGIN_NAME + " focused node";
//
//	// node height calculator name
//    private static final String DATA_TYPE_NODE_BORDER_COLOR_CALCULATOR_NAME =
//		"Data Type Node Border Color Calculator"
//		+ BioPaxVisualStyleUtil.VERSION_POST_FIX;
//
//	// node height calculator name
//    private static final String DATA_TYPE_NODE_BORDER_THICKNESS_CALCULATOR_NAME =
//		"Data Type Node Border Thickness Calculator"
//		+ BioPaxVisualStyleUtil.VERSION_POST_FIX;
//
//	// node color calculator name
//    private static final String DATA_TYPE_NODE_COLOR_CALCULATOR_NAME =
//		"Data Type Node Color Calculator"
//		+ BioPaxVisualStyleUtil.VERSION_POST_FIX;
//
//	// ref to visual style we will create
//    private final VisualStyle style;
//
//	// ref to data mapper config
//	private final MondrianConfiguration mondrianConfiguration;
//
//	/**
//	 * Method to get instance of VisualStyleFactory.
//	 *
//     * @param mondrianConfiguration MondrianConfiguration
//	 * @return VisualStyleFactory
//	 */
//	public static VisualStyleFactory getInstance(MondrianConfiguration mondrianConfiguration) {
//		return new VisualStyleFactory(mondrianConfiguration);
//	}
//
//    /**
//     * Gets the Visual Style, that has the newly created color gradient.
//     *
//     * @return Visual Style Object.
//     */
//    public VisualStyle getVisualStyle() {
//        return style;
//    }
//
//	/**
//	 * Method to contruct a ContinousMappingObject given a color gradient theme & range.
//	 *
//	 * @param colorGradientTheme ColorGradientTheme 
//	 * @param colorGradientRange ColorGrandientRange
//	 * @return ContinousMapping
//	 */
//	public static ContinuousMapping getContinuousMapping(ColorGradientTheme colorGradientTheme,
//														 ColorGradientRange colorGradientRange) {
//
//		// sanity check
//		if (colorGradientTheme == null || colorGradientRange == null) return null;
//
//        ContinuousMapping continuousMapping = new ContinuousMapping(Color.WHITE, ObjectMapping.NODE_MAPPING);
//		continuousMapping.setInterpolator(new LinearNumberToColorInterpolator());
//
//        final Color minColor = colorGradientTheme.getMinColor();
//        final Color medColor = colorGradientTheme.getCenterColor();
//        final Color maxColor = colorGradientTheme.getMaxColor();
//
//		final BoundaryRangeValues bv0 = new BoundaryRangeValues(minColor, minColor, minColor);
//		final BoundaryRangeValues bv1a = new BoundaryRangeValues(medColor, medColor, medColor);
//		final BoundaryRangeValues bv1b = new BoundaryRangeValues(medColor, medColor, medColor);
//		final BoundaryRangeValues bv2 = new BoundaryRangeValues(maxColor, maxColor, maxColor);
//
//		// add points to continuous mapper
//		continuousMapping.addPoint(colorGradientRange.getMinValue(), bv0);
//		continuousMapping.addPoint(colorGradientRange.getCenterLowValue(), bv1a);
//		continuousMapping.addPoint(colorGradientRange.getCenterHighValue(), bv1b);
//		continuousMapping.addPoint(colorGradientRange.getMaxValue(), bv2);
//
//		// outta here
//		return continuousMapping;
//	}
//
//    /**
//     * Constructor (private).
//     *
//     * @param mondrianConfiguration MondrianConfiguration
//     */
//    private VisualStyleFactory(MondrianConfiguration mondrianConfiguration) {
//
//		// init members
//        this.mondrianConfiguration = mondrianConfiguration;
//
//        // set the visual style
//		style = getVisualStyle(mondrianConfiguration.getCyNetwork());
//		if (style == null) return;
//		
//        //  get the nac and modifying for our liking
//        final NodeAppearanceCalculator nac = style.getNodeAppearanceCalculator();
//        //  set no data color
//		nac.getDefaultAppearance().set(VisualPropertyType.NODE_FILL_COLOR,
//									   mondrianConfiguration.getColorTheme().getNoDataColor());
//        //  create our custom node color calculator (gradient based on data values)
//        final BasicCalculator nodeColorCalculator = getCalculator(true);
//		// set the new node color calc in the nac
//        nac.setCalculator(nodeColorCalculator);
//		// node label color calculator
//		final BasicCalculator nodeLabelColorCalculator = getCalculator(false);
//		nac.setCalculator(nodeLabelColorCalculator);
//		// set node border color calculator
//		final BasicCalculator nodeBorderThicknessCalculator = getNodeBorderThicknessCalculator();
//		nac.setCalculator(nodeBorderThicknessCalculator);
//		// set node border color calculator
//		final BasicCalculator nodeBorderColorCalculator = getNodeBorderColorCalculator();
//		nac.setCalculator(nodeBorderColorCalculator);
//    }
//
//	/**
//	 * Method to return set visual style
//	 *
//	 * @param cyNetwork CyNetwork
//	 * @return VisualStyle
//	 */
//	private VisualStyle getVisualStyle(CyNetwork cyNetwork) {
//
//		Boolean res;
//		VisualStyle localStyle = null;
//	
//		// check args
//		if (cyNetwork == null) return null;
//
//		// get the network attributes
//		cytoscape.data.CyAttributes networkAttributes = Cytoscape.getNetworkAttributes();
//
//		// get network id
//		String networkID = cyNetwork.getIdentifier();
//
//		// is the biopax network attribute true ?
//		if ((res = networkAttributes.getBooleanAttribute(networkID, "DATA_MAPPER_NETWORK")) != null) {
//			// this should work, but doesnt
//			//CyNetworkView nv = Cytoscape.getNetworkView(cyNetwork.getIdentifier());
//            //style = nv.getVisualStyle();
//			// so we do this instead
//			if (res) localStyle = Cytoscape.getVisualMappingManager().getCalculatorCatalog().getVisualStyle(GUIConfig.PLUGIN_NAME);
//		}
//		else if ((res = networkAttributes.getBooleanAttribute(networkID, "BIOPAX_NETWORK")) != null) {
//			if (res) localStyle = BioPaxVisualStyleUtil.getBioPaxVisualStyle();
//		}
//		else if ((res = networkAttributes.getBooleanAttribute(networkID, "BINARY_NETWORK")) != null) {
//			if (res) localStyle = BinarySifVisualStyleUtil.getVisualStyle();
//		}
//
//		// set style name
//		if (localStyle != null) localStyle.setName(GUIConfig.PLUGIN_NAME);
//
//		// outta here
//		return localStyle;
//	}
//	
//    /**
//     * Defines the Continuous Color Calculator.
//	 *
//	 * @param nodeColor boolean (if true, we are calc node fill, else node label color)
//	 * @return BasicCalculator
//     */
//    private BasicCalculator getCalculator(boolean nodeColor) {
//
//		// setup color gradient colors
//        final ColorGradientTheme colorGradientTheme = mondrianConfiguration.getColorTheme();
//        final Color minColor = (nodeColor) ? colorGradientTheme.getMinColor() : colorGradientTheme.getMinLabelColor();
//        final Color medColor = colorGradientTheme.getCenterColor();
//        final Color maxColor = (nodeColor) ? colorGradientTheme.getMaxColor() : colorGradientTheme.getMaxLabelColor();
//
//        // create interpolator
//        final ContinuousMapping continuousMapping = new ContinuousMapping
//			(Color.WHITE, ObjectMapping.NODE_MAPPING);
//		continuousMapping.setInterpolator(new LinearNumberToColorInterpolator());
//			
//        //  set controlling attribute
//		final String controllingAttribute;
//		final HeatmapPanelConfiguration heatmapPanelConfiguration =
//			mondrianConfiguration.getActiveHeatmapPanelConfiguration();
//		if (heatmapPanelConfiguration != null) {
//			// determine "sample" property
//			if (heatmapPanelConfiguration.getConstantPropertyType() ==
//				HeatmapPanelConfiguration.PROPERTY_TYPE.SAMPLE) {
//				controllingAttribute = heatmapPanelConfiguration.getConstantProperty();
//			}
//			else if (heatmapPanelConfiguration.getColumnPropertyType() ==
//					 HeatmapPanelConfiguration.PROPERTY_TYPE.SAMPLE) {
//				controllingAttribute = heatmapPanelConfiguration.getColumnPropertiesFocus();
//			}
//			// must be row
//			else {
//				controllingAttribute = heatmapPanelConfiguration.getRowPropertiesFocus();
//			}
//		}
//		else {
//			DataTypeMatrix dataTypeMatrix = mondrianConfiguration.getDataTypeMatrix();
//			controllingAttribute = dataTypeMatrix.getSampleNamesVector().firstElement();
//		}
//        continuousMapping.setControllingAttributeName(controllingAttribute + MapDataTypeValuesToPathway.EXPRESSION_POST_FIX, //(sampleNames[sampleNameIndex]
//													  null, false);
//
//        //  set mapping data points
//		final ColorGradientRange colorGradientRange = mondrianConfiguration.getColorGradientRange();
//		if (nodeColor) {
//			//  create boundary conditions
//			final BoundaryRangeValues bv0 = new BoundaryRangeValues(minColor, minColor, minColor);
//			final BoundaryRangeValues bv1a = new BoundaryRangeValues(medColor, medColor, medColor);
//			final BoundaryRangeValues bv1b = new BoundaryRangeValues(medColor, medColor, medColor);
//			final BoundaryRangeValues bv2 = new BoundaryRangeValues(maxColor, maxColor, maxColor);
//			// add points to continuous mapper
//			continuousMapping.addPoint(colorGradientRange.getMinValue(), bv0);
//			continuousMapping.addPoint(colorGradientRange.getCenterLowValue(), bv1a);
//			continuousMapping.addPoint(colorGradientRange.getCenterHighValue(), bv1b);
//			continuousMapping.addPoint(colorGradientRange.getMaxValue(), bv2);
//		}
//		else {
//			// create boundary conditions
//			final BoundaryRangeValues bv = new BoundaryRangeValues(minColor, maxColor, maxColor);
//			continuousMapping.addPoint((colorGradientRange.getCenterLowValue() - Math.abs(colorGradientRange.getMinValue()) / 2.0), bv);
//		}
//
//		// outta here
//		String calculatorName = DATA_TYPE_NODE_COLOR_CALCULATOR_NAME + ((nodeColor) ? " (Node Color)" : " (Label Color)");
//		return new BasicCalculator(calculatorName, continuousMapping,
//								   (nodeColor) ? VisualPropertyType.NODE_FILL_COLOR : VisualPropertyType.NODE_LABEL_COLOR);
//    }
//
//    /**
//     * Defines a node border color calculator.
//	 *
//	 * @return BasicCalculator
//     */
//    private BasicCalculator getNodeBorderThicknessCalculator() {
//
//		DiscreteMapping discreteMapping = new DiscreteMapping(1.0,
//															  DATA_MAPPER_FOCUSED_NODE,
//															  ObjectMapping.NODE_MAPPING);
//
//		// if node is focused, double the size
//		discreteMapping.putMapValue(true, 5.0);
//
//		// create and set node width calculator in node appearance calculator
//		return new BasicCalculator(DATA_TYPE_NODE_BORDER_THICKNESS_CALCULATOR_NAME,
//								   discreteMapping,
//								   VisualPropertyType.NODE_LINE_WIDTH);
//	}
//
//    /**
//     * Defines a node border color calculator.
//	 *
//	 * @return BasicCalculator
//     */
//    private BasicCalculator getNodeBorderColorCalculator() {
//
//		DiscreteMapping discreteMapping = new DiscreteMapping(Color.BLACK,
//															  DATA_MAPPER_FOCUSED_NODE,
//															  ObjectMapping.NODE_MAPPING);
//
//		// if node is focused, double the size
//		discreteMapping.putMapValue(true, Color.ORANGE);
//
//		// create and set node width calculator in node appearance calculator
//		return new BasicCalculator(DATA_TYPE_NODE_BORDER_COLOR_CALCULATOR_NAME,
//								   discreteMapping,
//								   VisualPropertyType.NODE_BORDER_COLOR);
//	}
//    
}
