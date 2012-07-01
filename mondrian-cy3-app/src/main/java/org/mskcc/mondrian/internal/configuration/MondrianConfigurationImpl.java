package org.mskcc.mondrian.internal.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mskcc.mondrian.client.DataTypeMatrix;
import org.mskcc.mondrian.internal.colorgradient.ColorGradientRange;
import org.mskcc.mondrian.internal.colorgradient.ColorGradientTheme;

/**
 * Maintains the current configuration for the plugin.
 *
 * @author Benjamin Gross
 * @author Dazhi Jiao
 */
public class MondrianConfigurationImpl implements MondrianConfiguration {
	
	protected static MondrianConfiguration instance;
	private final List<MondrianConfigurationListener> listeners;
	private ColorGradientTheme colorGradientTheme;
	
	private boolean networkZoom = true;
	private boolean networkZoomKey;
	private boolean displayMultipleDataTypeNodes;	
	
	public MondrianConfigurationImpl() {
		colorGradientTheme = ColorGradientTheme.BLUE_RED_GRADIENT_THEME;
		listeners = new ArrayList<MondrianConfigurationListener>();
	}

	@Override
	public synchronized void addConfigurationListener(MondrianConfigurationListener listener) {

		// check args
		if (listener == null) return;

		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removedConfigurationListener(MondrianConfigurationListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
		
	}

	@Override
	public void setColorGradientRange(ColorGradientRange colorGradientRange) {
		// check args
		if (colorGradientRange == null) return;

//		if (dataTypeMatrix != null) {
//			ColorGradientRange stored = dataTypeMatrixToRangeMap.get(dataTypeMatrix);
//			stored.setMinValue(colorGradientRange.getMinValue());
//			stored.setCenterLowValue(colorGradientRange.getCenterLowValue());
//			stored.setCenterHighValue(colorGradientRange.getCenterHighValue());
//			stored.setMaxValue(colorGradientRange.getMaxValue());
//			notifyConfigurationChanged(this, true, false, false, false, false, false, false);
//		}
		
	}

	@Override
	public ColorGradientRange getColorGradientRange() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ColorGradientRange getColorGradientRange(
			DataTypeMatrix dataTypeMatrix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ColorGradientTheme getColorTheme() {
		return colorGradientTheme;
	}

	@Override
	public void setColorTheme(ColorGradientTheme colorGradientTheme) {
		// check args
		if (colorGradientTheme == null) return;

        this.colorGradientTheme = colorGradientTheme;
		notifyConfigurationChanged(this, false, false, true, false, false, false, false);
	}

	@Override
	public String getDataTypeMatrixType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataTypeMatrix getDataTypeMatrix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remapCyNetwork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNetworkZoom(boolean networkZoom) {
		this.networkZoom = networkZoom;
	}

	@Override
	public boolean getNetworkZoom() {
		return networkZoom;
	}

	@Override
	public void setNetworkZoomKey(boolean networkZoomKey) {
		this.networkZoomKey = networkZoomKey;
	}

	@Override
	public boolean getNetworkZoomKey() {
		return networkZoomKey;
	}

	@Override
	public void setDisplayMultipleDataTypeNodes(boolean displayMultipleDataTypeNodes) {
		this.displayMultipleDataTypeNodes = displayMultipleDataTypeNodes;
	}

	@Override
	public boolean getDisplayMultipleDataTypeNodes() {
		return displayMultipleDataTypeNodes;
	}


	private void notifyConfigurationChanged(MondrianConfiguration source,
			boolean rangeChanged,
			boolean networkChanged,
			boolean colorThemeChanged,
			boolean dataTypeChanged,
			boolean heatmapPanelConfigChanged,
			boolean clinicalDataChanged,
			boolean applyVizStyle) {

		// create event object
		ConfigurationChangedEvent evt = new ConfigurationChangedEvent(source,
				rangeChanged, networkChanged, colorThemeChanged,
				dataTypeChanged, heatmapPanelConfigChanged,
				clinicalDataChanged, applyVizStyle);

		// copy vector to prevent changing hile firing events
		List<MondrianConfigurationListener> l = new ArrayList<MondrianConfigurationListener>();
		synchronized (this) {
			Collections.copy(listeners, l);
		}

		// fire events to all listeners
		for (MondrianConfigurationListener listener : l) {
			listener.configurationChanged(evt);
		}
	}	
}
