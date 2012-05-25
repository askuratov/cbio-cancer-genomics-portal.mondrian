package org.mskcc.mondrian.internal;

import java.awt.event.ActionEvent;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;
import org.mskcc.mondrian.internal.configuration.ConfigurationChangedEvent;
import org.mskcc.mondrian.internal.configuration.MondrianConfiguration;
import org.mskcc.mondrian.internal.configuration.MondrianConfigurationImp;
import org.mskcc.mondrian.internal.configuration.MondrianConfigurationListener;
import org.mskcc.mondrian.internal.gui.DataTypesPanel;
import org.mskcc.mondrian.internal.gui.MapControlPanel;

/**
 * The Mondrian App
 *
 * @author Dazhi Jiao
 */
public class MondrianApp extends AbstractCyAction implements MondrianConfigurationListener {
	private static final long serialVersionUID = 1935118515626512995L;
	private CySwingApplication desktopApp;
	private MondrianConfiguration mondrianConfiguration;
	private MapControlPanel controlPane;
	private DataTypesPanel dataTypesPane;
	
	protected static MondrianApp instance;
	
	public static MondrianApp getInstance(CySwingApplication desktopApp) {
		if (instance == null) {
			instance = new MondrianApp(desktopApp);
		}
		return instance;
	}

	private MondrianApp(CySwingApplication desktopApp) {
		super("Mondrian");
		setPreferredMenu("Apps");
		this.desktopApp = desktopApp;
		
		mondrianConfiguration = MondrianConfigurationImp.getInstance();

		this.controlPane = MapControlPanel.getInstance(mondrianConfiguration);
		this.dataTypesPane = DataTypesPanel.getInstance(mondrianConfiguration);
	}

	public void actionPerformed(ActionEvent e) {
		CytoPanel cytoPanelWest = this.desktopApp.getCytoPanel(CytoPanelName.EAST);
		// If the state of the cytoPanelWest is HIDE, show it
		if (cytoPanelWest.getState() == CytoPanelState.HIDE) {
			cytoPanelWest.setState(CytoPanelState.DOCK);
		}	

		// Select my panel
		int index = cytoPanelWest.indexOfComponent(dataTypesPane);
		if (index == -1) {
			return;
		}
		cytoPanelWest.setSelectedIndex(index);		
	}

	@Override
	public void configurationChanged(ConfigurationChangedEvent evt) {
		// TODO Auto-generated method stub
		
	}

	public MapControlPanel getControlPane() {
		return controlPane;
	}

	public void setControlPane(MapControlPanel controlPane) {
		this.controlPane = controlPane;
	}

	public DataTypesPanel getDataTypesPane() {
		return dataTypesPane;
	}

	public void setDataTypesPane(DataTypesPanel dataTypesPane) {
		this.dataTypesPane = dataTypesPane;
	}
	
}
