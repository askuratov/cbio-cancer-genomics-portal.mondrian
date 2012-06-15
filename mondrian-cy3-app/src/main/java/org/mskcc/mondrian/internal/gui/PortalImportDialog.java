package org.mskcc.mondrian.internal.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.swing.DialogTaskManager;
import org.mskcc.mondrian.client.CBioPortalClient;
import org.mskcc.mondrian.client.CancerStudy;
import org.mskcc.mondrian.client.CaseList;
import org.mskcc.mondrian.client.DataTypeMatrix;
import org.mskcc.mondrian.client.GeneticProfile;
import org.mskcc.mondrian.internal.MondrianApp;

public class PortalImportDialog extends JDialog {

	private static final long serialVersionUID = 8533443616371311272L;
	
	private static final Logger logger = Logger.getLogger(PortalImportDialog.class.getName());	
	
	private final JPanel contentPanel = new JPanel();
	private JComboBox cancerStudyComboBox;
	private JList profileList;
	private JComboBox caseSetComboBox;
	private CBioPortalClient portalClient;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PortalImportDialog dialog = new PortalImportDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static PortalImportDialog instance;
	private JComboBox geneSymbolComboBox;
	
	public static PortalImportDialog getInstance() {
		if (instance == null) {
			instance = new PortalImportDialog();
		}
		return instance;
	}

	/**
	 * Create the dialog.
	 */
	private PortalImportDialog() {
		
		setTitle("Load Data From cBio Portal");
		setBounds(100, 100, 450, 300);
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setVgap(2);
		borderLayout.setHgap(2);
		getContentPane().setLayout(borderLayout);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(5, 5));
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Select Genomic Profile(s)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				profileList = new JList() {
					public String getToolTipText(MouseEvent event) { // tooltip for each profile
						Point point = event.getPoint();
						// Get the item in the list box at the mouse location
						int index = this.locationToIndex(point);
						Object item = getModel().getElementAt(index);
						// Get the value of the item in the list
						return ((GeneticProfile) this.getModel().getElementAt(index)).getDescription();
					}
				};
				scrollPane.setViewportView(profileList);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			panel.setLayout(new GridLayout(2, 1, 0, 0));
			{
				JLabel lblNewLabel = new JLabel("Select a Cancer Study");
				panel.add(lblNewLabel);
			}
			cancerStudyComboBox = new JComboBox();
			panel.add(cancerStudyComboBox);
			cancerStudyComboBox.setRenderer(new BasicComboBoxRenderer(){
				public Component getListCellRendererComponent(JList list,
						Object value, int index, boolean isSelected,
						boolean cellHasFocus) {
					if (isSelected) {
						setBackground(list.getSelectionBackground());
						setForeground(list.getSelectionForeground());
						if (-1 < index) {
							list.setToolTipText("<html><body>" + ((CancerStudy)cancerStudyComboBox.getModel().getElementAt(index)).getDescription() + "</body></html>");
						}
					} else {
						setBackground(list.getBackground());
						setForeground(list.getForeground());
					}
					setFont(list.getFont());
					setText((value == null) ? "" : value.toString());
					return this;
				}		
			});
			{
				JPanel panel_1 = new JPanel();
				contentPanel.add(panel_1, BorderLayout.SOUTH);
				panel_1.setLayout(new GridLayout(4, 1, 0, 0));
				{
					JLabel lblNewLabel_1 = new JLabel("Select Patient/Case Set");
					panel_1.add(lblNewLabel_1);
				}
				{
					caseSetComboBox = new JComboBox();
					panel_1.add(caseSetComboBox);
					caseSetComboBox.setRenderer(new BasicComboBoxRenderer(){
						public Component getListCellRendererComponent(JList list,
								Object value, int index, boolean isSelected,
								boolean cellHasFocus) {
							if (isSelected) {
								setBackground(list.getSelectionBackground());
								setForeground(list.getSelectionForeground());
								if (-1 < index) {
									list.setToolTipText("<html><body>" + ((CaseList)caseSetComboBox.getModel().getElementAt(index)).getDescription() + "</body></html>");
								}
							} else {
								setBackground(list.getBackground());
								setForeground(list.getForeground());
							}
							setFont(list.getFont());
							setText((value == null) ? "" : value.toString());
							return this;
						}		
					});
				}
				{
					JLabel lblNewLabel_2 = new JLabel("Select Node Attribute for Gene Symbols");
					panel_1.add(lblNewLabel_2);
				}
				{
					geneSymbolComboBox = new JComboBox();
					panel_1.add(geneSymbolComboBox);
					// Get the node attributes from current network
					Collection<CyColumn> cols = MondrianApp.getInstance().getAppManager().getCurrentNetwork().getDefaultNodeTable().getColumns();
					List<String> attrs = new ArrayList<String>();
					final List<String> toolTips = new ArrayList<String>();
					int i = 0;
					for (CyColumn col: cols) {
						if (col.getType() != String.class) {
							continue;  // Looking for gene symbols, which should be String column
						}
						attrs.add(col.getName());
						List<Object> values = col.getValues(col.getType());
						String toolTip = "<html><body>";
						for (int j = 0; j < Math.min(values.size(), 3); j++) {
							toolTip += values.get(j).toString();
							if (j < Math.min(values.size(), 3)-1) toolTip += "<br/>";
						}
						if (values.size() > 3) toolTip += "<br/>...";
						toolTip += "</body></html>";
						toolTips.add(toolTip);
						i++;
					}
					geneSymbolComboBox.setModel(new DefaultComboBoxModel(attrs.toArray(new String[attrs.size()])));
					geneSymbolComboBox.setRenderer(new BasicComboBoxRenderer(){
						public Component getListCellRendererComponent(JList list,
								Object value, int index, boolean isSelected,
								boolean cellHasFocus) {
							if (isSelected) {
								setBackground(list.getSelectionBackground());
								setForeground(list.getSelectionForeground());
								if (-1 < index) {
									list.setToolTipText(toolTips.get(index));
								}
							} else {
								setBackground(list.getBackground());
								setForeground(list.getForeground());
							}
							setFont(list.getFont());
							setText((value == null) ? "" : value.toString());
							return this;
						}		
					});					
				}
			}
			cancerStudyComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					CancerStudy study = (CancerStudy)cancerStudyComboBox.getSelectedItem();
					if (!study.equals(portalClient.getCurrentCancerStudy())) {
						DialogTaskManager taskManager = MondrianApp.getInstance().getTaskManager();
						taskManager.execute(new TaskIterator(new LoadCancerStudyTask()));								
					}
				}
			});
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						PortalImportDialog.getInstance().setVisible(false);
						DialogTaskManager taskManager = MondrianApp.getInstance().getTaskManager();
						taskManager.execute(new TaskIterator(new ImportDataTask()));							
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						PortalImportDialog.getInstance().setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		DialogTaskManager taskManager = MondrianApp.getInstance().getTaskManager();
		taskManager.execute(new TaskIterator(new InitializeCancerStudyTask()));		
	}
	
	public CBioPortalClient getPortalClient() {
		return portalClient;
	}
	
	public void setPortalClient(CBioPortalClient client) {
		this.portalClient = client;
	}
	
	class InitializeCancerStudyTask extends AbstractTask {
		@Override
		public void run(TaskMonitor arg0) throws Exception {
			CBioPortalClient client = new CBioPortalClient();
			PortalImportDialog.getInstance().setPortalClient(client);
			List<CancerStudy> studies = client.getCancerStudies();
			cancerStudyComboBox.setModel(new DefaultComboBoxModel(studies.toArray()));
			// populate with the first study 
			caseSetComboBox.setModel(new DefaultComboBoxModel(client.getCaseListsForCurrentStudy().toArray()));
			profileList.setModel(new DefaultComboBoxModel(client.getGeneticProfilesForCurrentStudy().toArray()));
		}
	}
	
	class LoadCancerStudyTask extends AbstractTask {
		@Override
		public void run(TaskMonitor arg0) throws Exception {
			portalClient.setCurrentCancerStudy((CancerStudy)cancerStudyComboBox.getSelectedItem());
			portalClient.getGeneticProfilesForCurrentStudy();
			portalClient.getCaseListsForCurrentStudy();
			caseSetComboBox.setModel(new DefaultComboBoxModel(portalClient.getCaseListsForCurrentStudy().toArray()));
			profileList.setModel(new DefaultComboBoxModel(portalClient.getGeneticProfilesForCurrentStudy().toArray()));			
		}
	}
	
	class ImportDataTask extends AbstractTask {
		@Override
		public void run(TaskMonitor arg0) throws Exception {
			CaseList caseList = (CaseList)caseSetComboBox.getSelectedItem();
			Object[] profiles = profileList.getSelectedValues();
			String geneSymbolField = (String)geneSymbolComboBox.getSelectedItem();
			CyTable table = MondrianApp.getInstance().getAppManager().getCurrentNetwork().getDefaultNodeTable();
			CyColumn col = table.getColumn(geneSymbolField);
			List<String> genes = col.getValues((Class<String>)col.getType());
			
			List<CyRow> rows = table.getAllRows();
			// Extract data from web service
			for (Object profile: profiles) {
				// TODO: Need to check for duplicate column names in multiple data matrices
				DataTypeMatrix matrix = portalClient.getProfileData(caseList, (GeneticProfile)profile, genes);
				List<String> dataColNames = matrix.getDataColNames();
				for (String colName: dataColNames) {
					table.createColumn(colName, Double.class, false);
				}
				for (CyRow row: rows) {
					String geneSymbol = row.get(geneSymbolField, String.class);
					List<Double> dataRow = matrix.getDataRow(geneSymbol);
					for (int i = 0; i < dataRow.size(); i++) {
						row.set(dataColNames.get(i), dataRow.get(i));
					}
				}
			}
		}
	}
}
