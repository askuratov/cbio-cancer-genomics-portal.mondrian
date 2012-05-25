package org.mskcc.mondrian.internal.configuration;

/**
 * Interface implemented by class which maintains configuration.
 *
 * @author Benjamin Gross
 */
public interface MondrianConfiguration {

	/**
	 * Method used to register a listener.
	 *
	 * @param listener MondrianConfigurationListener
	 */
	public void addConfigurationListener(MondrianConfigurationListener listener);

	/**
	 * Method used to remove a registered listener.
	 *
	 * @param listener MondrianConfigurationListener
	 */
	public void removedConfigurationListener(MondrianConfigurationListener listener);

//	/**
//	 * Method used to set color gradient range (min, center-low, center-high, max)
//	 * Note: "origX values will be ignored.
//	 *
//	 * @param colorGradientRange ColorGradientRange
//	 */
//	public void setColorGradientRange(ColorGradientRange colorGradientRange);
//
//	/**
//	 * Method used to get current gradient range.
//	 *
//	 * @return GradientRange
//	 */
//	public ColorGradientRange getColorGradientRange();
//
//	/**
//	 * Method used to get current gradient range.
//	 *
//	 * @param dataTypeMatrix DataTypeMatrix
//	 * @return GradientRange
//	 */
//	public ColorGradientRange getColorGradientRange(DataTypeMatrix dataTypeMatrix);
//
//    /**
//     * Method used to get the gradient color theme.
//     *
//     * @return ColorGradientTheme Object.
//     */
//    public ColorGradientTheme getColorTheme();
//
//    /**
//     * Method used to set the color theme.
//     *
//     * @param colorGradientTheme ColorGradientTheme
//     */
//    public void setColorTheme(ColorGradientTheme colorGradientTheme);
//
//    /**
//     * Method used to get the Data Type Selected DATA_TYPE.
//     *
//     * @return DataTypeMatrixMatrixManager.DATA_TYPE.
//     */
//    public String getDataTypeMatrixType();
//
//    /**
//     * Method used to get the data type matrix.
//     *
//     * @return DataTypeMatrix
//     */
//    public DataTypeMatrix getDataTypeMatrix();
//
//    /**
//     * Method used to set the data type matrix.
//     *
//     * @param dataTypeMatrix DataTypeMatrix
//     */
//    public void setDataTypeMatrix(DataTypeMatrix dataTypeMatrix);
//
//    /**
//     * Method used to unload the data type matrix.
//     *
//     * @param dataTypeMatrix DataTypeMatrix
//     */
//    public void unloadDataTypeMatrix(DataTypeMatrix dataTypeMatrix);
//
//    /**
//     * Method used to get the current CyNetwork.
//     *
//     * @return CyNetwork
//     */
//    public CyNetwork getCyNetwork();
//
//    /**
//     * Method used to set the CyNetwork.
//     *
//     * @param cyNetwork CyNetwork
//     */
//    public void setCyNetwork(CyNetwork cyNetwork);
//
//    /**
//     * Method used to remove the current CyNetwork reference.
//	 *
//	 * notifyListeners boolean
//     */
//    public void removeCyNetwork(boolean notifyListeners);
//
//    /**
//     * Method used to get the current heatmap panel configuration.
//     *
//     * @return HeatmapPanelConfiguration
//     */
//    public HeatmapPanelConfiguration getActiveHeatmapPanelConfiguration();
//
//    /**
//     * Method used to get a heatmap panel configuration given a HeatmapPanel.
//	 *
//     * @param heatmapPanel HeatmapPanel
//     * @return HeatmapPanelConfiguration
//     */
//    public HeatmapPanelConfiguration getHeatmapPanelConfiguration(HeatmapPanel heatmapPanel);
//
//    /**
//     * Method used to set a heatmap configuration.
//     *
//     * @param heatmapPanelConfiguration HeatmapPanelConfiguration
//     */
//    public void setHeatmapPanelConfiguration(HeatmapPanelConfiguration heatmapPanelConfiguration);
//
//	/**
//	 * Method used to fire event to remap viz style on cynetwork.
//	 * Motivated by integration with pathwaycommons, see NetworkEventListener for more info.
//	 */
//	public void remapCyNetwork();
//
//	/**
//	 * Method to set network zoom preference.
//	 * If true, network view will pan/zoom over gene of interest
//	 * as user navigates the heatmap panel.
//	 *
//	 * @param networkZoom boolean
//	 */
//	public void setNetworkZoom(boolean networkZoom);
//
//	/**
//	 * Method to get network zoom preference.
//	 *
//	 * @return boolean
//	 */
//	public boolean getNetworkZoom();
//
//	/**
//	 * Method to set network zoom key preference.
//	 * If true, network view will pan/zoom over gene of interest
//	 * as user navigates the heatmap panel on if shift key is being held.
//	 *
//	 * @param networkZoomKey boolean
//	 */
//	public void setNetworkZoomKey(boolean networkZoomKey);
//
//	/**
//	 * Method to get network zoom preference.
//	 *
//	 * @return boolean
//	 */
//	public boolean getNetworkZoomKey();
//
//	/**
//	 * Method to set custom node preference, display multiple data-
//	 * type on each node.
//	 *
//	 * @param displayMultipleDataTypeNodes boolean
//	 */
//	public void setDisplayMultipleDataTypeNodes(boolean displayMultipleDataTypeNodes);
//
//	/**
//	 * Method to get display multiple data type node.
//	 *
//	 * @return boolean
//	 */
//	public boolean getDisplayMultipleDataTypeNodes();
//
//	/**
//	 * Method is called to get heatstrip display boolean.
//	 *
//	 * @return HeatmapWidget.CELL_DISPLAY
//	 */
//	public HeatmapWidget.CELL_DISPLAY getHeatmapCellDisplay();
//
//	/**
//	 * Method is called to set heatstrip display boolean.
//	 *
//	 * @param HeatmapWidget.CELL_DISPLAY
//	 */
//	public void setHeatmapCellDisplay(HeatmapWidget.CELL_DISPLAY heatmapWidgetCellDisplay);
//
//	/**
//	 * Method is called to get clinical data.
//	 */
//	public Map<String, TCGAClinicalData> getClinicalData();
//
//	/**
//	 * Method is called to set clinical data.
//	 */
//	public void setClinicalData(Map<String, TCGAClinicalData> clinicalData);
}
