package org.mskcc.mondrian.internal.gui.heatmap;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
import org.mskcc.mondrian.client.CancerStudy;
import org.mskcc.mondrian.client.GeneticProfile;
import org.mskcc.mondrian.internal.MondrianApp;
import org.mskcc.mondrian.internal.configuration.ConfigurationChangedEvent;
import org.mskcc.mondrian.internal.configuration.ConfigurationChangedEvent.Type;
import org.mskcc.mondrian.internal.configuration.MondrianConfiguration;
import org.mskcc.mondrian.internal.configuration.MondrianConfigurationListener;
import org.mskcc.mondrian.internal.configuration.MondrianCyTable;
import org.mskcc.mondrian.internal.gui.heatmap.HeatmapPanelConfiguration.PROPERTY_TYPE;

@SuppressWarnings("serial")
public class HeatmapPanel extends JPanel implements MondrianConfigurationListener, 
TableAddedListener, TableDeletedListener, RowsSetListener, CytoPanelComponent, ActionListener {
	
	private JTable table_1;
	private JComboBox constantPropertyTypeComboBox;
	private JComboBox constantPropertyComboBox;
	private JCheckBox hideGenesCheckBox;
	private HeatmapTable heatmapTable;
	private JComboBox cancerStudyComboBox;

	/**
	 * Create the panel.
	 */
	public HeatmapPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel headerPane = new JPanel();
		add(headerPane);
		headerPane.setLayout(new BoxLayout(headerPane, BoxLayout.X_AXIS));
		
		constantPropertyTypeComboBox = new JComboBox(){
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
		constantPropertyTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogTaskManager taskManager = MondrianApp.getInstance().getTaskManager();
				taskManager.execute(new TaskIterator(new UpdateConstantTypeTask()));					
			}
		});;
		
		cancerStudyComboBox = new JComboBox();
		headerPane.add(cancerStudyComboBox);
		constantPropertyTypeComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		constantPropertyTypeComboBox.setModel(new DefaultComboBoxModel(org.mskcc.mondrian.internal.gui.heatmap.HeatmapPanelConfiguration.PROPERTY_TYPE.values()));
		headerPane.add(constantPropertyTypeComboBox);
		
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
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);
		
		table_1 = new JTable(20, 10);
		table_1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_1.getColumnModel().getColumn(0).setPreferredWidth(150);
		scrollPane.setViewportView(table_1);
		heatmapTable = new HeatmapTable(1, scrollPane);
		
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
	
	public void updatePanelData(MondrianCyTable table) {
		//updateConstantPropertyComboBox();
		// update data for 
		MondrianConfiguration mondrianConfig = MondrianApp.getInstance().getMondrianConfiguration();
		CyNetwork network = MondrianApp.getInstance().getAppManager().getCurrentNetwork();
		System.out.println(mondrianConfig.getCancerStudies(network).size());
		cancerStudyComboBox.setModel(new DefaultComboBoxModel(mondrianConfig.getCancerStudies(network).toArray()));
		cancerStudyComboBox.setSelectedItem(table.getStudy());
		
		// update constant property combobox
		if (constantPropertyTypeComboBox.getSelectedItem() == PROPERTY_TYPE.DATA_TYPE) {
			
		}
	
		this.validate();
	}
	
	public void loadStudyData(CancerStudy study) {
		
	}

	public void udateConstantPropertyComboBox(CancerStudy study) {
		MondrianApp app = MondrianApp.getInstance();
		MondrianConfiguration config = app.getMondrianConfiguration();
		List<String> list = new ArrayList<String>();
		CyNetwork network = app.getAppManager().getCurrentNetwork();
		PROPERTY_TYPE cannedConfig = (PROPERTY_TYPE)this.constantPropertyTypeComboBox.getSelectedItem();
		switch(cannedConfig) {
		case GENE:
			Map<String, Long> geneNodeMap = config.getGeneNodeMap(network.getSUID());
			list = new ArrayList<String>(geneNodeMap.keySet());
			Collections.sort(list);
			break;
		case DATA_TYPE:
			List<GeneticProfile> profiles = config.getGeneticProfiles(network, study);
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
			this.updatePanelData((MondrianCyTable)evt.getSource());
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
		if (src == constantPropertyTypeComboBox) {
			
		}
	}
	
	/**
	 * Returns the current selected constant property type
	 * @return
	 */
	public PROPERTY_TYPE getConstantPropertyType() {
		return (PROPERTY_TYPE)this.constantPropertyTypeComboBox.getSelectedItem();
	}
	
	/**
	 * Sets the constant property type
	 * @param propertyType
	 */
	public void setConstantPropertyType(PROPERTY_TYPE propertyType) {
		this.constantPropertyTypeComboBox.setSelectedItem(propertyType);
		DialogTaskManager taskManager = MondrianApp.getInstance().getTaskManager();
		taskManager.execute(new TaskIterator(new UpdateConstantTypeTask()));			
	}
	
	class UpdateConstantTypeTask extends AbstractTask {

		@Override
		public void run(TaskMonitor arg0) throws Exception {
			MondrianApp app = MondrianApp.getInstance();
			MondrianConfiguration config = app.getMondrianConfiguration();
			CyNetwork network = app.getAppManager().getCurrentNetwork();
			PROPERTY_TYPE cannedConfig = (PROPERTY_TYPE)constantPropertyTypeComboBox.getSelectedItem();
			CancerStudy study = (CancerStudy)cancerStudyComboBox.getSelectedItem();
			switch(cannedConfig) {
			case GENE:
				Map<String, Long> geneNodeMap = config.getGeneNodeMap(network.getSUID());
				List<String> list = new ArrayList<String>(geneNodeMap.keySet());
				Collections.sort(list);
				constantPropertyComboBox.setModel(new DefaultComboBoxModel(list.toArray()));				
				break;
			case DATA_TYPE:
				List<GeneticProfile> profiles = config.getGeneticProfiles(network, study);
				constantPropertyComboBox.setModel(new DefaultComboBoxModel(profiles.toArray()));
				break;
			case SAMPLE:
				//CaseList caseList = config.getNetworkCaseList(network.getSUID());
				//constantPropertyComboBox.setModel(new DefaultComboBoxModel(caseList.getCases()));
				break;
			}
		}
		
	}
}
