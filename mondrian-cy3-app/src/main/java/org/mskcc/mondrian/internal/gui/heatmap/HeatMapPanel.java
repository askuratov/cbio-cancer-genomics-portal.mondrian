package org.mskcc.mondrian.internal.gui.heatmap;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.events.RowsSetEvent;
import org.cytoscape.model.events.RowsSetListener;
import org.cytoscape.model.events.TableAddedEvent;
import org.cytoscape.model.events.TableAddedListener;
import org.cytoscape.model.events.TableDeletedEvent;
import org.cytoscape.model.events.TableDeletedListener;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.swing.DialogTaskManager;
import org.mskcc.mondrian.client.CaseList;
import org.mskcc.mondrian.client.GeneticProfile;
import org.mskcc.mondrian.internal.MondrianApp;
import org.mskcc.mondrian.internal.configuration.ConfigurationChangedEvent;
import org.mskcc.mondrian.internal.configuration.ConfigurationChangedEvent.Type;
import org.mskcc.mondrian.internal.configuration.MondrianConfiguration;
import org.mskcc.mondrian.internal.configuration.MondrianConfigurationListener;
import javax.swing.DefaultComboBoxModel;

import org.mskcc.mondrian.internal.gui.heatmap.HeatMapPanel.CANNED_CONFIG;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class HeatMapPanel extends JPanel implements MondrianConfigurationListener, 
TableAddedListener, TableDeletedListener, RowsSetListener, CytoPanelComponent, ActionListener {
	/**
	 * Constant Element Enumeration
	 */
	public static enum CANNED_CONFIG {
		DATA_TYPE("Constant:datatype"),
		GENE("Constant:gene"),
		SAMPLE("Constant:sample");

		// string ref for readable name
		private String name;

		// constructor
		CANNED_CONFIG(String name) { this.name = name; }

		// method to get enum readable name
		public String toString() { return name; }
	}	
	
	private JTable table_1;
	private JTable table;
	private JComboBox cannedConfigComboBox;
	private JComboBox constantPropertyComboBox;
	private JCheckBox hideGenesCheckBox;

	/**
	 * Create the panel.
	 */
	public HeatMapPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel headerPane = new JPanel();
		add(headerPane);
		headerPane.setLayout(new BoxLayout(headerPane, BoxLayout.X_AXIS));
		
		cannedConfigComboBox = new JComboBox(){
            /** 
             * @inherited <p>
             */
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }

        };
		cannedConfigComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogTaskManager taskManager = MondrianApp.getInstance().getTaskManager();
				taskManager.execute(new TaskIterator(new UpdateConstantTypeTask()));					
			}
		});;
		cannedConfigComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		cannedConfigComboBox.setModel(new DefaultComboBoxModel(CANNED_CONFIG.values()));
		headerPane.add(cannedConfigComboBox);
		
		constantPropertyComboBox = new JComboBox();
		headerPane.add(constantPropertyComboBox);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		headerPane.add(horizontalGlue);
		
		hideGenesCheckBox = new JCheckBox("Hide Genes without Data");
		headerPane.add(hideGenesCheckBox);
		
		JButton configGradientButton = new JButton("Configure Color Gradient");
		headerPane.add(configGradientButton);
		
		JButton configHeatmapButton = new JButton("Configure Heatmap");
		headerPane.add(configHeatmapButton);
		
		JPanel columnPane = new JPanel();
		add(columnPane);
		columnPane.setLayout(new BoxLayout(columnPane, BoxLayout.X_AXIS));
		
		JButton columnBeginButton = new JButton("|<");
		columnPane.add(columnBeginButton);
		
		JButton columnLeftButton = new JButton("<");
		columnPane.add(columnLeftButton);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		columnPane.add(horizontalGlue_1);
		
		JButton columnRightButton = new JButton(">");
		columnPane.add(columnRightButton);
		
		JButton columnEndButton = new JButton(">|");
		columnPane.add(columnEndButton);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(splitPane);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		table_1 = new JTable();
		scrollPane.setViewportView(table_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);
		
		table = new JTable();
		scrollPane_1.setViewportView(table);
		
		JPanel rowPane = new JPanel();
		add(rowPane);
		rowPane.setLayout(new BoxLayout(rowPane, BoxLayout.X_AXIS));
		
		JButton rowBeginButton = new JButton("First Row");
		rowPane.add(rowBeginButton);
		
		JButton rowPreviousButton = new JButton("Previous Row");
		rowPane.add(rowPreviousButton);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		rowPane.add(horizontalGlue_2);
		
		JButton rowNextButton = new JButton("Next Row");
		rowPane.add(rowNextButton);
		
		JButton rowEndButton = new JButton("Last Row");
		rowPane.add(rowEndButton);

		//MondrianApp.getInstance().addConfigurationListener(this);
		//updatePanelData();
	}
	
	public void updatePanelData() {
		//updateConstantPropertyComboBox();
		// update data for 
		this.validate();
	}

	public void udateConstantPropertyComboBox() {
		MondrianApp app = MondrianApp.getInstance();
		MondrianConfiguration config = app.getMondrianConfiguration();
		List<String> list = new ArrayList<String>();
		CyNetwork network = app.getAppManager().getCurrentNetwork();
		CANNED_CONFIG cannedConfig = (CANNED_CONFIG)this.cannedConfigComboBox.getSelectedItem();
		switch(cannedConfig) {
		case GENE:
			Map<String, Long> geneNodeMap = config.getGeneNodeMap(network.getSUID());
			list = new ArrayList<String>(geneNodeMap.keySet());
			Collections.sort(list);
			break;
		case DATA_TYPE:
			List<GeneticProfile> profiles = config.getNetworkGeneticProfiles(network.getSUID());
			break;
		case SAMPLE:
			break;
		}
		constantPropertyComboBox.setModel(new DefaultComboBoxModel(list.toArray()));	
	}

	@Override
	public void handleEvent(TableDeletedEvent evt) {
		
	}

	@Override
	public void handleEvent(TableAddedEvent arg0) {
		// TODO Auto-generated method stub
		CyTable table = arg0.getTable();
	}

	@Override
	public void configurationChanged(ConfigurationChangedEvent evt) {
		if (evt.getType() == Type.CBIO_DATA_IMPORTED) {
			this.updatePanelData();
		}
	}

	@Override
	public void handleEvent(RowsSetEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.SOUTH;
	}

	@Override
	public Icon getIcon() {
		URL url = this.getClass().getResource("/breakpoint_group.gif");
		return new ImageIcon(url);
	}

	@Override
	public String getTitle() {
		return "Mondrian Control Panel";
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == cannedConfigComboBox) {
			
		}
	}
	
	class UpdateConstantTypeTask extends AbstractTask {

		@Override
		public void run(TaskMonitor arg0) throws Exception {
			MondrianApp app = MondrianApp.getInstance();
			MondrianConfiguration config = app.getMondrianConfiguration();
			CyNetwork network = app.getAppManager().getCurrentNetwork();
			CANNED_CONFIG cannedConfig = (CANNED_CONFIG)cannedConfigComboBox.getSelectedItem();
			switch(cannedConfig) {
			case GENE:
				Map<String, Long> geneNodeMap = config.getGeneNodeMap(network.getSUID());
				List<String> list = new ArrayList<String>(geneNodeMap.keySet());
				Collections.sort(list);
				constantPropertyComboBox.setModel(new DefaultComboBoxModel(list.toArray()));				
				break;
			case DATA_TYPE:
				List<GeneticProfile> profiles = config.getNetworkGeneticProfiles(network.getSUID());
				constantPropertyComboBox.setModel(new DefaultComboBoxModel(profiles.toArray()));
				break;
			case SAMPLE:
				CaseList caseList = config.getNetworkCaseList(network.getSUID());
				System.out.println(caseList.getCases());
				constantPropertyComboBox.setModel(new DefaultComboBoxModel(caseList.getCases()));
				break;
			}
		}
		
	}
}
