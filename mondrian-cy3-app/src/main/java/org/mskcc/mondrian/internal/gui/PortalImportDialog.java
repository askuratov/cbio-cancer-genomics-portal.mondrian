package org.mskcc.mondrian.internal.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.swing.DialogTaskManager;
import org.mskcc.mondrian.client.CBioPortalClient;
import org.mskcc.mondrian.client.CancerStudy;
import org.mskcc.mondrian.client.CaseList;
import org.mskcc.mondrian.client.GeneticProfile;
import org.mskcc.mondrian.internal.MondrianApp;

public class PortalImportDialog extends JDialog {

	private static final long serialVersionUID = 8533443616371311272L;
	
	private static final Logger logger = Logger.getLogger(PortalImportDialog.class.getName());	
	
	private final JPanel contentPanel = new JPanel();
	private JComboBox cancerStudyComboBox;
	private JList geneicProfileList;
	private JComboBox clinicalCaseComboBox;
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
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Select Genomic Profile(s)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				geneicProfileList = new JList() {
					public String getToolTipText(MouseEvent event) { // tooltip for each profile
						Point point = event.getPoint();
						// Get the item in the list box at the mouse location
						int index = this.locationToIndex(point);
						Object item = getModel().getElementAt(index);
						// Get the value of the item in the list
						return ((GeneticProfile) this.getModel().getElementAt(index)).getDescription();
					}
				};
				scrollPane.setViewportView(geneicProfileList);
			}
		}
		{
			cancerStudyComboBox = new JComboBox();
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
			final CBioPortalClient finalClient = this.portalClient; 
			cancerStudyComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					CancerStudy study = (CancerStudy)cancerStudyComboBox.getSelectedItem();
					if (!study.equals(portalClient.getCurrentCancerStudy())) {
						DialogTaskManager taskManager = MondrianApp.getInstance().getTaskManager();
						taskManager.execute(new TaskIterator(new UpdateCancerStudyTask()));								
					}
				}
			});
			contentPanel.add(cancerStudyComboBox, BorderLayout.NORTH);
		}
		{
			clinicalCaseComboBox = new JComboBox();
			clinicalCaseComboBox.setRenderer(new BasicComboBoxRenderer(){
				public Component getListCellRendererComponent(JList list,
						Object value, int index, boolean isSelected,
						boolean cellHasFocus) {
					if (isSelected) {
						setBackground(list.getSelectionBackground());
						setForeground(list.getSelectionForeground());
						if (-1 < index) {
							list.setToolTipText("<html><body>" + ((CaseList)clinicalCaseComboBox.getModel().getElementAt(index)).getDescription() + "</body></html>");
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
			contentPanel.add(clinicalCaseComboBox, BorderLayout.SOUTH);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
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
		taskManager.execute(new TaskIterator(new CancerStudyTask()));		
	}
	
	public CBioPortalClient getPortalClient() {
		return portalClient;
	}
	
	public void setPortalClient(CBioPortalClient client) {
		this.portalClient = client;
	}
	
	class CancerStudyTask extends AbstractTask {
		@Override
		public void run(TaskMonitor arg0) throws Exception {
			CBioPortalClient client = new CBioPortalClient();
			PortalImportDialog.getInstance().setPortalClient(client);
			List<CancerStudy> studies = client.getCancerStudies();
			cancerStudyComboBox.setModel(new DefaultComboBoxModel(studies.toArray()));
			// populate with the first study 
			clinicalCaseComboBox.setModel(new DefaultComboBoxModel(client.getCaseListsForCurrentStudy().toArray()));
			geneicProfileList.setModel(new DefaultComboBoxModel(client.getGeneticProfilesForCurrentStudy().toArray()));
		}
	}
	
	class UpdateCancerStudyTask extends AbstractTask {
		@Override
		public void run(TaskMonitor arg0) throws Exception {
			portalClient.setCurrentCancerStudy((CancerStudy)cancerStudyComboBox.getSelectedItem());
			portalClient.getGeneticProfilesForCurrentStudy();
			portalClient.getCaseListsForCurrentStudy();
			clinicalCaseComboBox.setModel(new DefaultComboBoxModel(portalClient.getCaseListsForCurrentStudy().toArray()));
			geneicProfileList.setModel(new DefaultComboBoxModel(portalClient.getGeneticProfilesForCurrentStudy().toArray()));			
		}
	}
}
