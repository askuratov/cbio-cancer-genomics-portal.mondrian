package org.mskcc.mondrian.internal.gui.heatmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;

/**
 * A heatmap table model that wraps a CyTable object. Data are based
 * on the CyTable. The SUID column is used as the row haeder. 
 * 
 * @author Dazhi Jiao
 */
@SuppressWarnings("serial")
public class CyTableHeatmapTableModel extends HeatmapTableModel {
	private CyTable cyTable;
	private List<String> columnNames = new ArrayList<String>();
	
	public CyTableHeatmapTableModel(CyTable cyTable) {
		super();
		this.cyTable = cyTable;
		Collection<CyColumn> cols = cyTable.getColumns();
		for (CyColumn cyColumn : cols) {
			if (!cyColumn.getName().equals(CyIdentifiable.SUID)) {
				columnNames.add(cyColumn.getName());
			}
		}
		Collections.sort(columnNames);
	}
	
	@Override
	public int getRowCount() {
		return cyTable.getRowCount();
	}

	@Override
	public int getColumnCount() {
		return cyTable.getColumns().size() - 1;  // skip SUID column
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		CyRow row = cyTable.getAllRows().get(rowIndex);
		return row.getRaw(columnNames.get(columnIndex));
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames.get(column);
	}
	
	@Override
	public String getRowName(int row) {
		CyRow cyRow = cyTable.getAllRows().get(row);
		return String.valueOf(cyRow.get(CyIdentifiable.SUID, Long.class));
	}
}
