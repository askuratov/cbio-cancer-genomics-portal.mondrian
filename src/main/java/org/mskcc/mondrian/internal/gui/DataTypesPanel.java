package org.mskcc.mondrian.internal.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.mskcc.mondrian.internal.configuration.MondrianConfiguration;

/**
 * Panel in which to manage expression data.
 *
 * @author Benjamin Gross
 * @author Dazhi Jiao
 */
public class DataTypesPanel extends JPanel implements CytoPanelComponent {
	private static final long serialVersionUID = -7362884992020398542L;
	protected static DataTypesPanel instance;
	
	public static DataTypesPanel getInstance(MondrianConfiguration mondrianConfiguration) {
		if (instance == null) {
			instance = new DataTypesPanel(mondrianConfiguration);
		}
		return instance;
	}
	
	private DataTypesPanel(MondrianConfiguration mondrianConfiguration) {
		
		// add a dummy panel
		JEditorPane pane = new JEditorPane();
		setLayout(new BorderLayout());
		add(pane, BorderLayout.CENTER);
		this.setVisible(true);
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.EAST;
	}

	@Override
	public Icon getIcon() {
		URL url = this.getClass().getResource("/breakpoint_group.gif");
		return new ImageIcon(url);
	}

	@Override
	public String getTitle() {
		return "Mondrian";
	}
}
