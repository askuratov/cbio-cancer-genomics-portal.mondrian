package org.mskcc.mondrian.internal.gui.heatmap;

import java.util.List;

import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.mskcc.mondrian.client.CaseList;
import org.mskcc.mondrian.client.GeneticProfile;
import org.mskcc.mondrian.internal.MondrianApp;
import org.mskcc.mondrian.internal.configuration.MondrianConfiguration;
import org.mskcc.mondrian.internal.configuration.MondrianCyTable;
import org.mskcc.mondrian.internal.gui.heatmap.HeatmapPanelConfiguration.PROPERTY_TYPE;

/**
 * A TableModel that is based on a list of MondrianCyTable objects. 
 * It returns values based on the PROPERTY_TYPE
 * 
 * If PROPERTY_TYPE is GENE, rows are profiles, columns are samples
 * If PROPERTY_TYPE is DATA_TYPE, rows are genes, columns are samples
 * If PROPERTY_TYPE is SAMPLE, rows are genes, columns are profiles 
 * 
 * @author djiao
 *
 *
 */
@SuppressWarnings("serial")
public class MondrianHeatmapTableModel extends HeatmapTableModel {
	private List<MondrianCyTable> tables;
	private PROPERTY_TYPE propertyType;
	private Object propertyValue;
	
	public MondrianHeatmapTableModel(List<MondrianCyTable> tables, PROPERTY_TYPE type, Object value) {
		this.tables = tables;
		this.propertyType = type;
		this.propertyValue = value;
	}
	
	public void setProperty(PROPERTY_TYPE propertyType, Object propertyValue) {
		this.propertyType = propertyType;
		this.propertyValue = propertyValue;
	}
	
	@Override
	public int getRowCount() {
		switch(propertyType) {
		case GENE:
			return tables.size();
		case DATA_TYPE: 
			return tables.get(0).getTable().getRowCount();
		case SAMPLE: 
			return tables.get(0).getTable().getRowCount();
		}
		return 0;
	}

	@Override
	public int getColumnCount() {
		MondrianCyTable table = tables.get(0);
		switch(propertyType) {
		case GENE: // ROWS: geneticProfile, COLS: sample
			return table.getCaseList().getCases().length;
		case DATA_TYPE:
			return table.getCaseList().getCases().length;
		case SAMPLE: 
			return tables.size();
		}
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		CyTable cyTable;
		CyRow row;
		switch(propertyType) {
		case GENE:
			MondrianCyTable table = tables.get(rowIndex);
			cyTable = table.getTable();
			row = cyTable.getRow(this.propertyValue);
			return row.getRaw(table.getCaseList().getCases()[columnIndex]);
		case DATA_TYPE: 
			for (MondrianCyTable mtable : tables) {
				if (mtable.getProfile().getId().equals(((GeneticProfile)propertyValue).getId())) {
					cyTable = mtable.getTable();
					row = cyTable.getAllRows().get(rowIndex);
					return row.getRaw(mtable.getCaseList().getCases()[columnIndex]);
				}
			}
			break;
		case SAMPLE: 
			cyTable = tables.get(columnIndex).getTable();
			row = cyTable.getAllRows().get(rowIndex);
			return row.getRaw((String)propertyValue);
		}
		return null;
	}

	@Override
	public String getRowName(int row) {
		MondrianConfiguration config = MondrianApp.getInstance().getMondrianConfiguration();
		CyNetwork network = MondrianApp.getInstance().getAppManager().getCurrentNetwork();
		switch(propertyType) {
		case GENE:
			return tables.get(row).getProfile().getName();
		case DATA_TYPE: 
		case SAMPLE: 
			CyTable table = tables.get(0).getTable();
			Long suid = table.getAllRows().get(row).get(CyIdentifiable.SUID, Long.class);
			String geneCol = config.getNetworkGeneSymbolAttr(network.getSUID());
			return network.getDefaultNodeTable().getRow(suid).get(geneCol, String.class);
		}
		return null;
	}
	
	@Override
	public String getColumnName(int column) {
		switch(propertyType) {
		case GENE:
			return tables.get(0).getCaseList().getCases()[column];
		case DATA_TYPE: 
			return tables.get(0).getCaseList().getCases()[column];
		case SAMPLE: 
			return tables.get(column).getProfile().getName();
		}
		return null;		
	}
}
