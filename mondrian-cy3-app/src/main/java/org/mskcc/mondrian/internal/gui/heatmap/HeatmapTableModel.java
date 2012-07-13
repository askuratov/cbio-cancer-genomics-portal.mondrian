package org.mskcc.mondrian.internal.gui.heatmap;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public abstract class HeatmapTableModel extends AbstractTableModel {
	public abstract String getRowName(int row);
}	

