package org.mskcc.mondrian.internal.gui.heatmap;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.mappings.BoundaryRangeValues;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;
import org.mskcc.mondrian.internal.MondrianApp;
import org.mskcc.mondrian.internal.configuration.MondrianConfiguration;
import org.mskcc.mondrian.internal.gui.heatmap.HeatmapPanelConfiguration.CELL_DISPLAY;

/*
 *  The table that shows the Heatmap
 */
public class HeatmapTable implements ChangeListener, PropertyChangeListener {
	private JTable main;
	private JTable fixed;
	private JScrollPane scrollPane;
	private HeatmapTableModel model;
	private ColorGradientTheme colorTheme;
	private CELL_DISPLAY heatmapMode = CELL_DISPLAY.SINGLE;

	/*
	 * Specify the number of columns to be fixed and the scroll pane containing
	 * the table.
	 */
	public HeatmapTable(JScrollPane scrollPane, HeatmapTableModel model) {
		this.scrollPane = scrollPane;
		this.model = model;
		updateCyTable();
	}
	
	public void setHeatmapMode(CELL_DISPLAY mode) {
		this.heatmapMode = mode;
	}

	public CELL_DISPLAY getHeatmapMode() {
		return heatmapMode;
	}
	
	@SuppressWarnings("serial")
	public void updateCyTable() {
		//int nRow = cyTable.getRowCount();
		this.main = new JTable(model.getRowCount(), model.getColumnCount()) {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int c) {
				return Double.class;
			}
		};
		main.addPropertyChangeListener(this);

		// Use the existing table to create a new table sharing
		// the DataModel and ListSelectionModel
		main.setModel(this.model);
		scrollPane.getViewport().setView(main);

		// Add the fixed table to the scroll pane
		fixed = new RowHeaderTable(main);
		
		scrollPane.setRowHeaderView(fixed);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixed.getTableHeader());

		// Synchronize scrolling of the row header with the main table
		scrollPane.getRowHeader().addChangeListener(this);
		
		// set our default renderer
		main.setDefaultRenderer(Double.class, new HeatmapTableCellRenderer(true));
		//JSparklinesBarChartTableCellRenderer renderer = new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, -100d, 100d);
		//renderer.showAsHeatMap(ColorGradient.RedBlackGreen);
		//main.setDefaultRenderer(Double.class, renderer);
		

		// we are not focusable
		main.setFocusable(false);

		// only allow single cell selection
		main.setCellSelectionEnabled(true);
		main.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

		// need to set autoresizing to off so that 
		// width of table is not forced to be size of viewport
		main.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		/*
		if (columnWidths.size() == 0) {
			setAllColumnWidths(getMaxColumnWidth());
		}
		else {
			setAllColumnWidths(columnWidths);
		}
		
		// set selected
		if (selectedCell.x >= 0 && selectedCell.y >= 0) {
			changeSelection(selectedCell.y, selectedCell.x, false, false);
		}
		*/		
	}

	/*
	 * Return the table being used in the row header
	 */
	public JTable getFixedTable() {
		return fixed;
	}
	
	public JScrollPane getScrollPane() {
		return this.scrollPane;
	}

	//
	// Implement the ChangeListener
	//
	public void stateChanged(ChangeEvent e) {
		// Sync the scroll pane scrollbar with the row header

		JViewport viewport = (JViewport) e.getSource();
		scrollPane.getVerticalScrollBar()
				.setValue(viewport.getViewPosition().y);
	}

	//
	// Implement the PropertyChangeListener
	//
	public void propertyChange(PropertyChangeEvent e) {
		// Keep the fixed table in sync with the main table

		if ("selectionModel".equals(e.getPropertyName())) {
			fixed.setSelectionModel(main.getSelectionModel());
		}

		if ("model".equals(e.getPropertyName())) {
			//fixed.setModel(main.getModel());
		}
	}
	
	/**
	 * Method used to get column widths for all columns in table.
	 *
	 * @return List<Integer>
	 */
	public List<Integer> getColumnWidths() {

		// to return
		List<Integer> toReturn = new java.util.ArrayList<Integer>();

		for (int lc = 0; lc < main.getColumnCount(); lc++) {
			toReturn.add(main.getColumnModel().getColumn(lc).getPreferredWidth());
		}

		// out of here
		return toReturn;
	}
	
	//**************************************************************************
	// HeatmapWidgetCellRenderer definition

	/**
	 * Inner class responsible for rendering heatmap widget cells.
	 * (class based of "How to Use Tables tutorial".
	 */
	@SuppressWarnings("serial")
	public class HeatmapTableCellRenderer extends JLabel implements javax.swing.table.TableCellRenderer {

		public static final int MAX_NUM_TRIANGLES = 4;
		// members
		private List<Color> backgroundColors;
		private final boolean isBordered;
		private final Color selectedColor;
		private Border selectedBorder = null;
		private Border unselectedBorder = null;
		private final java.awt.BasicStroke stroke = new java.awt.BasicStroke(0.5f);
		private final Color BORDER_COLOR = Color.GRAY;
		private final java.text.NumberFormat formatter = new java.text.DecimalFormat("#,###,###.##");
		private Color paint;

		/**
		 * Constructor.
		 *
		 * @param mondrianConfiguration MondrianConfiguration
		 * @param isBordered boolean
		 */
		public HeatmapTableCellRenderer(boolean isBordered) {

			// init members
			this.isBordered = isBordered;
			this.selectedColor = Color.ORANGE;
			// setup widget props
			setOpaque(true); // must do this for background to show up.
		}

		/**
		 * Our implementation of TableCellRenderer interface.
		 *
		 * @param table JTable
		 * @param object Object
		 * @param isSelected boolean
		 * @param hasFocus boolean
		 * @param row int
		 * @param column int
		 */
		public Component getTableCellRendererComponent(JTable table, Object object,
													   boolean isSelected, boolean hasFocus,
													   int row, int column) {

			if (object instanceof String) {
				String str = object.toString();
				javax.swing.table.DefaultTableCellRenderer renderer = new javax.swing.table.DefaultTableCellRenderer();
				return renderer.getTableCellRendererComponent(table, str, isSelected,hasFocus, row, column); 
			}

			// get ref to cell object
			//cellObject = (CellObject)object;

			// get range of values
			// 
			VisualMappingFunctionFactory vmfFactory = MondrianApp.getInstance().getContinuousVmfFactory();
			
			HeatmapTableModel model = (HeatmapTableModel)table.getModel();
			String colName = model.getColumnName(column);
			ContinuousMapping<Double, Paint> cMapping = (ContinuousMapping<Double, Paint>)vmfFactory.createVisualMappingFunction(colName, Double.class, BasicVisualLexicon.NODE_FILL_COLOR);
			ColorGradientTheme colorTheme = MondrianApp.getInstance().getMondrianConfiguration().getColorTheme();
			cMapping.addPoint(model.getMin(), new BoundaryRangeValues<Paint>(colorTheme.getMinColor(), colorTheme.getMinColor(), colorTheme.getMinColor()));
			cMapping.addPoint(model.getMean(), new BoundaryRangeValues<Paint>(colorTheme.getCenterColor(), colorTheme.getCenterColor(), colorTheme.getCenterColor()));
			cMapping.addPoint(model.getMax(), new BoundaryRangeValues<Paint>(colorTheme.getMaxColor(), colorTheme.getMaxColor(), colorTheme.getMaxColor()));
			this.paint = (Color)cMapping.getMappedValue(model.getCyRow(row, column));

			// swap/set borders
			if (isBordered) {
				if (isSelected) {
					if (selectedBorder == null) {
						selectedBorder = BorderFactory.createMatteBorder(3,3,3,3, selectedColor);
					}
					setBorder(selectedBorder);
				}
				else {	
					if (unselectedBorder == null) {
						unselectedBorder = BorderFactory.createEmptyBorder(0,0,0,0);
					}
					setBorder(unselectedBorder);
				}
			}
        
			// set tooltip
			//TODO: setToolTipText(getToolTipText(measurements));

			return this;
		}
		
		/**
		 * Method called when exporting image (like to a pdf).
		 *
		 * @param g Graphics
		 */
		public void export(Graphics g) {
			paintComponent(g);

			Graphics2D g2d = (Graphics2D)g;
			g2d.setPaint(Color.WHITE);
			g2d.drawRect(0,0,getWidth(),getHeight());
		}

		/**
		 * Our implementation of paint component.
		 *
		 * @param g Graphics
		 */
		protected void paintComponent(Graphics g) {

			super.paintComponent(g);
			
			Graphics2D g2d = (Graphics2D)g;
			g2d.setStroke(stroke);
			switch (heatmapMode) {
			    case SINGLE:
			    	g2d.setPaint(this.paint);
				    g2d.fillRect(0, 0, getWidth(), getHeight());
				    break;
				case TRIANGLES:
					g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
					displayTriangles(g2d);
					g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_OFF);
					break;
				case NUMERIC:
			    //case HEATSTRIP:
				//	displayHeatStrip(g2d);
				//	break;
			    //case TILES:
				//	displayTiles(g2d);
				//	break;
			}
		}

		/**
		 * Method to render heatstrips
		 *
		 * @param g2d Graphics2D
		 */
		private void displayHeatStrip(Graphics2D g2d) {

			double regionHeight = (backgroundColors.size() > 1) ? getHeight() * 0.5D : getHeight();
			double regionWidth = getWidth() / backgroundColors.size();
			boolean positive = false;
			int lc = -1;
			for (Color color : backgroundColors) {
				positive = !positive;
				g2d.setPaint(color);
				Shape shape = new java.awt.geom.Rectangle2D.Double(++lc*regionWidth, // positive x moves right
																   (positive) ? 0 : regionHeight, // postivie y moves down
																   regionWidth,
																   regionHeight);
				g2d.fill(shape);
			}
			if (backgroundColors.size() > 1) {
				// draw horizontal line down center
				g2d.setPaint(BORDER_COLOR);
				g2d.drawLine(0, (int)regionHeight, getWidth(), (int)regionHeight);
			}
		}

		/**
		 * Method to render triangles.
		 *
		 * @param g2d Graphics2D
		 */
		private void displayTriangles(Graphics2D g2d) {
			MondrianConfiguration mondrianConfiguration = MondrianApp.getInstance().getMondrianConfiguration();
			// if we only have 3 colors (data types) add a fourth, no data colored triangle
			if (backgroundColors.size() == MAX_NUM_TRIANGLES-1) {
				backgroundColors.add(mondrianConfiguration.getColorTheme().getNoDataColor());
			}

			int lc = -1;
			for (Color color : backgroundColors) {
				Shape shape = getShape(backgroundColors.size(), ++lc, 0, 0, getWidth(), getHeight());
				g2d.setPaint(color);
				g2d.fill(shape);
				// we dont attempt to draw more than 4 triangles
				if (lc == MAX_NUM_TRIANGLES-1) break;
			}
			g2d.setPaint(BORDER_COLOR);
			if (backgroundColors.size() > 1) {
				// draw line top right to lower left
				g2d.drawLine(getWidth(), 0, 0, getHeight());
			}
			if (backgroundColors.size() == 3) {
				// draw line center to lower right
				g2d.drawLine(getWidth() / 2, getHeight() / 2, getWidth(), getHeight());
			}
			if (backgroundColors.size() >= 4) {
				// draw another line top left, lower right
				g2d.drawLine(0, 0, getWidth(), getHeight());
			}
		}

		/** 
		 * Method to render tiles.
		 *
		 * @param g2d Graphics2D
		 */
		private void displayTiles(Graphics2D g2d) {
			MondrianConfiguration mondrianConfiguration = MondrianApp.getInstance().getMondrianConfiguration();
			int squareMatrixSize = getSquareMatrixSize(backgroundColors.size());

			// add squareMatrixSize - dataTypes no data color to background color collection
			int numColorsToAdd = (squareMatrixSize*squareMatrixSize) - backgroundColors.size();
			if (numColorsToAdd > 0) {
				Color noDataColor = mondrianConfiguration.getColorTheme().getNoDataColor();
				for (int lc = 0; lc < numColorsToAdd; lc++) {
					backgroundColors.add(noDataColor);
				}
			}

			int regionWidth = getWidth() / squareMatrixSize;
			int regionHeight = getHeight() / squareMatrixSize;

			for (int row = 0; row < squareMatrixSize; row++) {
				int yPos = row * regionHeight;
				for (int col = 0; col < squareMatrixSize; col++) {
					int xPos = col * regionWidth;
					Shape shape = new java.awt.geom.Rectangle2D.Double(xPos, // positive x moves right
																	   yPos, // postivie y moves down
																	   regionWidth,
																	   regionHeight);
					g2d.setPaint(backgroundColors.get(row * squareMatrixSize + col));
					g2d.fill(shape);
					if (col > 0) {
						// draw vertical line left edge
						g2d.setPaint(BORDER_COLOR);
						g2d.drawLine(xPos, yPos, xPos, yPos + regionHeight);
					}
				}
				// draw horizontal line across top
				if (row > 0) {
					g2d.setPaint(BORDER_COLOR);
					g2d.drawLine(0, yPos, getWidth(), yPos);
				}
			}
		}

		/**
		 * Method to get a shape to render for triangle displays.
		 *
		 * @param numDataTypes int
		 * @param dataTypeIndex int
		 * @param originX int
		 * @param originY int
		 * @param width int
		 * @param height int
		 * @return Shape
		 */
		public Shape getShape(int numDataTypes, int dataTypeIndex, int originX, int originY, int width, int height) {

			if (numDataTypes == 1) {
				// return rectangle
				return new java.awt.geom.Rectangle2D.Double(0, 0, width, height);
			}

			// path to return
			java.awt.geom.GeneralPath toReturn = new java.awt.geom.GeneralPath();
			if (numDataTypes == 2) {
				if (dataTypeIndex == 0) {
					// return triangle left hand side
					toReturn.moveTo(originX, originY);
					toReturn.lineTo(originX + width, originY);
					toReturn.lineTo(originX, originY + height);
				}
				else {
					// return triangle right hand side
					toReturn.moveTo(originX, originY + height);
					toReturn.lineTo(originX + width, originY);
					toReturn.lineTo(originX + width, originY + height);
				}
			}
			if (numDataTypes >= 3) {
				if (dataTypeIndex == 0) {
					// return triangle top
					toReturn.moveTo(originX, originY);
					toReturn.lineTo(originX + width, originY);
					toReturn.lineTo(originX + width / 2, originY + height / 2);
				}
				else if (dataTypeIndex == 1) {
					// return triangle right
					toReturn.moveTo(originX + width / 2, originY + height / 2);
					toReturn.lineTo(originX + width, originY);
					toReturn.lineTo(originX + width, originY + height);
				}
				else if (dataTypeIndex == 2) {
					// return triangle bottom
					toReturn.moveTo(originX + width / 2, originY + height / 2);
					toReturn.lineTo(originX + width, originY + height);
					toReturn.lineTo(originX, originY + height);
				}
				else {
					// return triangle left
					toReturn.moveTo(originX, originY);
					toReturn.lineTo(originX + width / 2, originY + height / 2);
					toReturn.lineTo(originX, originY + height);
				}
			}

			// close the shape
			toReturn.closePath();

			// outta here
			return toReturn;
		}

		/**
		 * Given the number of data types,
		 * returns size of square matrix required to display datatypes in tiled display.
		 *
		 * @param numDataTypes int
		 * @return int
		 */
		private int getSquareMatrixSize(int numDataTypes) {

			int lc = 0;
			while (numDataTypes > lc*lc) {
				++lc;
			}
			return lc;
		}

		/**
		 * Method to get tooltip text.
		 *
		 * @param measurements List<String>
		 * @return String
		 */
//		private String getToolTipText(List<String> measurements) {
//			String rowProperty = cellObject.getRowName();
//			String columnProperty = cellObject.getColumnName();
//
//			String measurement = "";
//			if (mondrianConfiguration.getHeatmapCellDisplay() == CELL_DISPLAY.SINGLE) {
//				// do we have mutations ?
//				if (mondrianConfiguration.getDataTypeMatrix().isMutation()) {
//					measurement = getMutationString();
//				}
//				else {
//					Double m = cellObject.getMeasurement();
//					measurement = (m.toString().equalsIgnoreCase("NaN")) ? "None" : formatter.format(m);
//					measurement = "Measurement: " + measurement + "<br>";
//				}
//			}
//			else {
//				int lc = -1;
//				for (String mStr : measurements) {
//					if (++lc == MAX_NUM_TRIANGLES) break;
//					measurement += mStr + ((!mStr.endsWith("<br>")) ? "<br>" : "");
//				}
//			}
//
//			// outta here
//			return ("<html>Row: " + rowProperty + "<br>" +
//					"Column: " + columnProperty + "<br>" +
//					measurement + "</html>");
//		}

		/**
		 * Gets mutation string.
		 * 
		 * @return String
		 */
		private String getMutationString() {
			// string to return
			String toReturn = "";

			// setup some refs
//			HeatmapPanelConfiguration heatmapPanelConfiguration = mondrianConfiguration.getActiveHeatmapPanelConfiguration();
//			if (heatmapPanelConfiguration == null) return toReturn;
//			DataTypeMatrix matrix = mondrianConfiguration.getDataTypeMatrix();
//			Map<String, String> mutationsMap = matrix.getMutationMap();
//			
//			// we need a gene and sample to look up mutation string in mutationsMap
//			String gene;
//			if (heatmapPanelConfiguration.rowPropertyTypeIsGene()) {
//				gene = cellObject.getRowProperty();
//			}
//			else if (heatmapPanelConfiguration.columnPropertyTypeIsGene()) {
//				gene = cellObject.getColumnName();
//			}
//			else {
//				gene = heatmapPanelConfiguration.getConstantProperty();
//			}
//			gene = org.mskcc.mondrian.maps.GeneMapper.getGene(matrix,
//															  geneDescriptorsToGeneNamesMap.get(gene),
//															  Cytoscape.getNodeAttributes());
//			String sample;
//			if (heatmapPanelConfiguration.rowPropertyTypeIsSample()) {
//				sample = cellObject.getRowProperty();
//			}
//			else if (heatmapPanelConfiguration.columnPropertyTypeIsSample()) {
//				sample = cellObject.getColumnName();
//			}
//			else {
//				sample = heatmapPanelConfiguration.getConstantProperty();
//			}
//
//			// get mutation
//			String mutation = mutationsMap.get(gene + MutationUtil.MUTATION_MAP_KEY_DELIMETER + sample);
//			if (mutation == null || mutation.length() == 0) {
//				toReturn = "Mutations: None";
//			}
//			else {
//				toReturn = MutationUtil.getMutationHtml(mutation, false);
//			}

			// outta here
			return toReturn;
		}
	}	
	
	public Object getRowHeader(int row) {
		return null;
	}
	
	@SuppressWarnings("serial")
	class RowHeaderTable extends JTable {
		private JTable main;
		public RowHeaderTable(JTable main) {
			this.main = main;
			
			setFocusable( false );
			setAutoCreateColumnsFromModel( false );
			setSelectionModel( main.getSelectionModel() );
			
			TableColumn column = new TableColumn();
			column.setHeaderValue(" ");
			addColumn( column );
			column.setCellRenderer(new RowNumberRenderer());
			
			getColumnModel().getColumn(0).setPreferredWidth(150);
			setPreferredScrollableViewportSize(getPreferredSize());
		}
		
		/*
		 *  Delegate method to main table
		 */
		@Override
		public int getRowCount() {
			return main.getRowCount();
		}	
		
		@Override
		public int getRowHeight(int row) {
			return main.getRowHeight(row);
		}	
		
		@Override
		public Object getValueAt(int row, int column) {
			if (column >= 1) {
				return "";
			} else {
				return model.getRowName(row);
			}
		}
		
		/*
		 *  Don't edit data in the main TableModel by mistake
		 */
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}		
		
		/*
		 *  Borrow the renderer from JDK1.4.2 table header
		 */
		@SuppressWarnings("serial")
		private class RowNumberRenderer extends DefaultTableCellRenderer {
			public RowNumberRenderer() {
				setHorizontalAlignment(JLabel.CENTER);
			}

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				if (table != null) {
					JTableHeader header = table.getTableHeader();

					if (header != null) {
						setForeground(header.getForeground());
						setBackground(header.getBackground());
						setFont(header.getFont());
					}
				}

				if (isSelected) {
					setFont( getFont().deriveFont(Font.BOLD) );
				}

				setText((value == null) ? "" : value.toString());
				setBorder(UIManager.getBorder("TableHeader.cellBorder"));

				return this;
			}
		}		
	}
}


