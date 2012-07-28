package org.mskcc.mondrian.internal.gui.heatmap;

import javax.swing.table.AbstractTableModel;

import org.cytoscape.model.CyRow;

@SuppressWarnings("serial")
public abstract class HeatmapTableModel extends AbstractTableModel {
	public abstract String getRowName(int row);
	public abstract CyRow getCyRow(int row, int col);
	public abstract Double getMin();
	public abstract Double getMax();
	public abstract Double getMean();
}	

