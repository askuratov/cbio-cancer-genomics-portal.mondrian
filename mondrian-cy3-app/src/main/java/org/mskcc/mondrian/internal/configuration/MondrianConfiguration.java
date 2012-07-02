package org.mskcc.mondrian.internal.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.SUIDFactory;
import org.mskcc.mondrian.client.CaseList;
import org.mskcc.mondrian.client.DataTypeMatrix;
import org.mskcc.mondrian.client.GeneticProfile;
import org.mskcc.mondrian.internal.MondrianApp;
import org.mskcc.mondrian.internal.colorgradient.ColorGradientRange;
import org.mskcc.mondrian.internal.colorgradient.ColorGradientTheme;
import org.mskcc.mondrian.internal.configuration.ConfigurationChangedEvent.Type;

/**
 * Maintains the current configuration for the plugin.
 * 
 * @author Benjamin Gross
 * @author Dazhi Jiao
 */
public class MondrianConfiguration {

	private final List<MondrianConfigurationListener> listeners;
	private ColorGradientTheme colorGradientTheme;

	private boolean networkZoom = true;
	private boolean networkZoomKey;
	private boolean displayMultipleDataTypeNodes;
	
	/**
	 * The attribute in the network that stores the geneSymbolAttribute
	 */
	private Map<Long, String> networkGeneSymbolAttrMap = new HashMap<Long, String>();
	/**
	 * List of genetic profile objects imported for each network
	 */
	private Map<Long, List<GeneticProfile>> networkProfileMap = new HashMap<Long, List<GeneticProfile>>();
	/**
	 * List of samples imported for each network
	 */
	private Map<Long, CaseList> networkCaseListMap = new HashMap<Long, CaseList>();

	public MondrianConfiguration() {
		colorGradientTheme = ColorGradientTheme.BLUE_RED_GRADIENT_THEME;
		listeners = new ArrayList<MondrianConfigurationListener>();
	}

	public synchronized void addConfigurationListener(
			MondrianConfigurationListener listener) {

		// check args
		if (listener == null)
			return;

		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removedConfigurationListener(
			MondrianConfigurationListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}

	}

	public void setColorGradientRange(ColorGradientRange colorGradientRange) {
		// check args
		if (colorGradientRange == null)
			return;

		// if (dataTypeMatrix != null) {
		// ColorGradientRange stored =
		// dataTypeMatrixToRangeMap.get(dataTypeMatrix);
		// stored.setMinValue(colorGradientRange.getMinValue());
		// stored.setCenterLowValue(colorGradientRange.getCenterLowValue());
		// stored.setCenterHighValue(colorGradientRange.getCenterHighValue());
		// stored.setMaxValue(colorGradientRange.getMaxValue());
		// notifyConfigurationChanged(this, true, false, false, false, false,
		// false, false);
		// }

	}

	public ColorGradientRange getColorGradientRange() {
		// TODO Auto-generated method stub
		return null;
	}

	public ColorGradientRange getColorGradientRange(
			DataTypeMatrix dataTypeMatrix) {
		// TODO Auto-generated method stub
		return null;
	}

	public ColorGradientTheme getColorTheme() {
		return colorGradientTheme;
	}

	public void setColorTheme(ColorGradientTheme colorGradientTheme) {
		// check args
		if (colorGradientTheme == null)
			return;

		this.colorGradientTheme = colorGradientTheme;
		notifyConfigurationChanged(Type.COLOR_THEME_CHANGED);
	}

	public String getDataTypeMatrixType() {
		// TODO Auto-generated method stub
		return null;
	}

	public DataTypeMatrix getDataTypeMatrix() {
		// TODO Auto-generated method stub
		return null;
	}

	public void remapCyNetwork() {
		// TODO Auto-generated method stub

	}

	public void setNetworkZoom(boolean networkZoom) {
		this.networkZoom = networkZoom;
	}

	public boolean getNetworkZoom() {
		return networkZoom;
	}

	public void setNetworkZoomKey(boolean networkZoomKey) {
		this.networkZoomKey = networkZoomKey;
	}

	public boolean getNetworkZoomKey() {
		return networkZoomKey;
	}

	public void setDisplayMultipleDataTypeNodes(
			boolean displayMultipleDataTypeNodes) {
		this.displayMultipleDataTypeNodes = displayMultipleDataTypeNodes;
	}

	public boolean getDisplayMultipleDataTypeNodes() {
		return displayMultipleDataTypeNodes;
	}
	
	public void notifyConfigurationChanged(ConfigurationChangedEvent.Type type) {
		ConfigurationChangedEvent evt = new ConfigurationChangedEvent(this, type);
		// copy vector to prevent changing hile firing events
		List<MondrianConfigurationListener> list = new ArrayList<MondrianConfigurationListener>();
		synchronized (this) {
			Collections.copy(listeners, list);
		}

		// fire events to all listeners
		for (MondrianConfigurationListener listener : list) {
			listener.configurationChanged(evt);
		}		
	}

	public void setNetworkGeneSymbolAttr(Long suid, String attr) {
		this.networkGeneSymbolAttrMap.put(suid, attr);
	}
	
	public String getNetworkGeneSymbolAttr(Long suid) {
		return this.networkGeneSymbolAttrMap.get(suid);
	}
	
	/**
	 * Returns a gene-to-node(suid) map given a network
	 * @param suid
	 * @return
	 */
	public Map<String, Long> getGeneNodeMap(Long suid) {
		MondrianApp app = MondrianApp.getInstance();
		CyNetwork network = app.getNetworkManager().getNetwork(suid);
		CyTable defaultTable = network.getDefaultNodeTable();
		List<CyRow> rows = defaultTable.getAllRows();
		Map<String, Long> geneSymbolMap = new HashMap<String, Long>(); 
		String geneSymbolField = getNetworkGeneSymbolAttr(suid);
		if (geneSymbolField != null) {
			for (CyRow cyRow : rows) {
				String geneSymbol = cyRow.get(geneSymbolField, String.class);
				Long nodeSuid = cyRow.get(CyIdentifiable.SUID, Long.class);
				geneSymbolMap.put(geneSymbol, nodeSuid);
			}
		}
		return geneSymbolMap;
	}
	
	public CaseList getNetworkCaseList(Long suid) {
		return this.networkCaseListMap.get(suid);
	}
	
	public List<GeneticProfile> getNetworkGeneticProfiles(Long suid) {
		return this.networkProfileMap.get(suid);
	}
	
	public void cbioDataImport(Long suid, List<GeneticProfile> profiles, CaseList caseList) {
		List<GeneticProfile> list = this.networkProfileMap.get(suid);
		list = (list == null) ? new ArrayList<GeneticProfile>() : list;
		for (GeneticProfile profile: profiles) {
			if (!list.contains(profile)) list.add(profile);
		}
		this.networkProfileMap.put(suid, list);
		this.networkCaseListMap.put(suid, caseList);
		this.notifyConfigurationChanged(Type.CBIO_DATA_IMPORTED);
	}
	
	/*
	private void notifyConfigurationChanged(MondrianConfiguration source,
			boolean rangeChanged, boolean networkChanged,
			boolean colorThemeChanged, boolean dataTypeChanged,
			boolean heatmapPanelConfigChanged, boolean clinicalDataChanged,
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
	*/
}
