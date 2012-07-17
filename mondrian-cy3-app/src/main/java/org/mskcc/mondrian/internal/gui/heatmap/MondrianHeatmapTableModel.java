package org.mskcc.mondrian.internal.gui.heatmap;

import java.util.List;

import org.mskcc.mondrian.internal.configuration.MondrianCyTable;
import org.mskcc.mondrian.internal.gui.heatmap.HeatmapPanelConfiguration.PROPERTY_TYPE;

@SuppressWarnings("serial")
public class MondrianHeatmapTableModel extends HeatmapTableModel {
	private List<MondrianCyTable> tables;
	private PROPERTY_TYPE propertyType;
	
	public MondrianHeatmapTableModel(List<MondrianCyTable> tables, PROPERTY_TYPE type) {
		this.tables = tables;
		this.propertyType = type;
	}
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRowName(int row) {
		// TODO Auto-generated method stub
		return null;
	}

}
