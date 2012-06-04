package org.mskcc.mondrian.internal.configuration;

/**
 * Class which encapsulates changes the current configuration.
 * 
 * @author Benjamin Gross
 * @author Dazhi Jiao
 */
public class ConfigurationChangedEvent extends java.util.EventObject {
	private static final long serialVersionUID = 5674382874417010725L;
	// refs to states
	private final boolean rangeChanged;
	private final boolean networkChanged;
	private final boolean colorThemeChanged;
	private final boolean dataTypeChanged;
	private final boolean heatmapPanelConfigurationChanged;
	private final boolean clinicalDataChanged;
	private final boolean applyVizStyle;

	/**
	 * Constructor.
	 * 
	 * @param source
	 *            MondrianConfiguration
	 * @param rangeChanged
	 *            boolean
	 * @param networkChanged
	 *            boolean
	 * @param colorThemeChanged
	 *            boolean
	 * @param dataTypeChanged
	 *            boolean
	 * @param heatmapPanelConfigurationChanged
	 *            boolean
	 * @param clinicalDataChanged
	 *            boolean
	 * @param applyVizStyle
	 *            boolean - mutually exclusive with other args
	 */
	public ConfigurationChangedEvent(MondrianConfiguration source,
			boolean rangeChanged, boolean networkChanged,
			boolean colorThemeChanged, boolean dataTypeChanged,
			boolean heatmapPanelConfigurationChanged,
			boolean clinicalDataChanged, boolean applyVizStyle) {

		// pass the source object to the superclass
		super(source);

		// init members
		this.rangeChanged = rangeChanged;
		this.networkChanged = networkChanged;
		this.colorThemeChanged = colorThemeChanged;
		this.dataTypeChanged = dataTypeChanged;
		this.heatmapPanelConfigurationChanged = heatmapPanelConfigurationChanged;
		this.clinicalDataChanged = clinicalDataChanged;
		this.applyVizStyle = applyVizStyle;
	}

	/**
	 * Method called to determine if the configuration range property has
	 * changed.
	 * 
	 * @return boolean
	 */
	public boolean rangeChanged() {
		return rangeChanged;
	}

	/**
	 * Method called to determine if the configuration CyNetwork property has
	 * changed.
	 * 
	 * @return boolean
	 */
	public boolean networkChanged() {
		return networkChanged;
	}

	/**
	 * Method called to determine if the configuration color theme has changed.
	 * 
	 * @return boolean
	 */
	public boolean colorThemeChanged() {
		return colorThemeChanged;
	}

	/**
	 * Method called to determine if the configuration data type has changed.
	 * 
	 * @return boolean
	 */
	public boolean dataTypeChanged() {
		return dataTypeChanged;
	}

	/**
	 * Method called to determine if the configuration - heatmap panel
	 * configuration has changed.
	 * 
	 * @return boolean
	 */
	public boolean heatmapPanelConfigurationChanged() {
		return heatmapPanelConfigurationChanged;
	}

	/**
	 * Method called to determine if clinical data has changed.
	 * 
	 * @return boolean
	 */
	public boolean clinicalDataChanged() {
		return clinicalDataChanged;
	}

	/**
	 * Method called to determine if the viz style needs to be reapply to
	 * network view. Motivated by integration with pathways common plugin. See
	 * NetworkEventListener for more info.
	 * 
	 * @return boolean
	 */
	public boolean applyVizStyle() {
		return applyVizStyle;
	}
}
