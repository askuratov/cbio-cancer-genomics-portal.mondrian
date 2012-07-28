package org.mskcc.mondrian.internal.gui.heatmap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.naming.OperationNotSupportedException;

import org.cytoscape.model.CyColumn;
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
	private double min = Double.NaN;
	private double max = Double.NaN;
	private double mean = Double.NaN;
	
	public MondrianHeatmapTableModel(List<MondrianCyTable> tables, PROPERTY_TYPE type, Object value) {
		this.tables = tables;
		this.propertyType = type;
		this.propertyValue = value;
	}
	
	public void setProperty(PROPERTY_TYPE propertyType, Object propertyValue) {
		this.propertyType = propertyType;
		this.propertyValue = propertyValue;
		// reset values
		this.min = Double.NaN;
		this.max = Double.NaN;
		this.mean = Double.NaN;
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

	@Override
	public CyRow getCyRow(final int row, final int col) {
		return new CyRow() {

			@Override
			public <T> T get(String arg0, Class<? extends T> arg1) {
				return arg1.cast(getValueAt(row, col));
			}

			@Override
			public <T> T get(String arg0, Class<? extends T> arg1, T arg2) {
				throw new UnsupportedOperationException();
			}

			@Override
			public Map<String, Object> getAllValues() {
				throw new UnsupportedOperationException();
			}

			@Override
			public <T> List<T> getList(String arg0, Class<T> arg1) {
				throw new UnsupportedOperationException();
			}

			@Override
			public <T> List<T> getList(String arg0, Class<T> arg1, List<T> arg2) {
				throw new UnsupportedOperationException();
			}

			@Override
			public Object getRaw(String arg0) {
				throw new UnsupportedOperationException();
			}

			@Override
			public CyTable getTable() {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean isSet(String arg0) {
				return true;
			}

			@Override
			public <T> void set(String arg0, T arg1) {
				throw new UnsupportedOperationException();
			}
			
		};
	}

	@Override
	public Double getMin() {
		if (Double.isNaN(this.min)) summary();
		return this.min;
	}

	@Override
	public Double getMax() {
		if (Double.isNaN(this.max)) summary();
		return this.max;
	}	

	@Override
	public Double getMean() {
		if (Double.isNaN(this.mean)) summary();
		return this.mean;
	}

	private void summary() {
		int n = 0;
		Double total = 0.0d;
		Double max = Double.MIN_VALUE;
		Double min = Double.MAX_VALUE;			
		switch(propertyType) {
		case GENE:
			for (MondrianCyTable mtable: tables) {
				CyTable cyTable = mtable.getTable();
				CyRow cyRow = cyTable.getRow(propertyValue);
				Collection<Object> values = cyRow.getAllValues().values();
				for (Object object : values) {
					if (object instanceof Long) continue; // SUID column
					else if (object instanceof String) continue; // MUTATION
					Double v = (Double)object;
					if (!Double.isNaN(v)) {
						if (v > max) max = v;
						if (v < min) min = v;
						total += v;
						n ++;
					}
				}				
			}
			break;
		case DATA_TYPE: 
			for (MondrianCyTable mtable : tables) {
				if (mtable.getProfile().getId().equals(((GeneticProfile)propertyValue).getId())) {
					CyTable cyTable = mtable.getTable();
					List<CyRow> cyRows = cyTable.getAllRows();
					for (CyRow cyRow : cyRows) {
						Collection<Object> values = cyRow.getAllValues().values();
						for (Object object : values) {
							if (object instanceof Long) continue; // SUID column
							else if (object instanceof String) continue; // MUTATION
							Double v = (Double)object;
							if (!Double.isNaN(v)) {
								if (v > max) max = v;
								if (v < min) min = v;
								total += v;
								n ++;
							}
						}
					}
				}
			}		
			break;
		case SAMPLE:
			for (MondrianCyTable mtable : tables) {
				CyTable cyTable = mtable.getTable();
				CyColumn cyColumn = cyTable.getColumn((String)propertyValue);
				List<Object> values = cyColumn.getValues(cyColumn.getType());
				for (Object object : values) {
					if (object instanceof String) continue;
					Double v = (Double)object;
					if (!Double.isNaN(v)) {
						if (v > max) max = v;
						if (v < min) min = v;
						total += v;
						n ++;
					}					
				}
			}		
			break;
		}	
		this.max = max;
		this.min = min;
		this.mean = n == 0 ? 0.0 : total/n;		
	}	
}
