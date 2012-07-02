package org.mskcc.mondrian.internal.gui.heatmap;

import org.mskcc.mondrian.internal.colorgradient.ColorGradientMapper;

import java.awt.Color;
import java.awt.Point;
import java.util.Map;
import java.util.Vector;
import java.awt.Shape;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class which renders data type / sample values overview for a gene/protein.
 * 
 * @author Benjamin Gross
 */
@SuppressWarnings("serial")
public class HeatMapTable extends JTable {

	/**
	 * Cell Display Enumeration.
	 */
	public static enum CELL_DISPLAY {

		// data types
		SINGLE("Single"), TRIANGLES("Triangles");
		// HEATSTRIP("Heat Strips"),
		// TILES("Tiles");

		// string ref for readable name
		private String type;

		// constructor
		CELL_DISPLAY(String type) {
			this.type = type;
		}

		// method to get enum readable name
		public String toString() {
			return type;
		}
	}

	private static final String COLUMN_HEADER_TEXT = ("<html>Holding down a shift key while resizing<br>"
			+ "any column will resize all the columns<br></html>");
	
	public HeatMapTable() {
		super();
	}
}