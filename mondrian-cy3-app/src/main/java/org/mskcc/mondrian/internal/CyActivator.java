package org.mskcc.mondrian.internal;

import java.util.Properties;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.model.CyNetworkTableManager;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.work.swing.DialogTaskManager;
import org.osgi.framework.BundleContext;

/**
 * Cytoscape App Activator
 * 
 * @author Dazhi Jiao
 */
public class CyActivator extends AbstractCyActivator {
	@Override
	public void start(BundleContext bc) throws Exception {
		CySwingApplication desktop = getService(bc,CySwingApplication.class);
		DialogTaskManager taskManager = getService(bc, DialogTaskManager.class);
		CyApplicationManager manager = getService(bc,CyApplicationManager.class);
		CyTableFactory tableFactory = getService(bc, CyTableFactory.class);
		CyTableManager tableManager = getService(bc,CyTableManager.class);
		CyNetworkTableManager networkTableManager = getService(bc, CyNetworkTableManager.class);
		
		// create our gui
		MondrianApp container = MondrianApp.getInstance(desktop, taskManager, manager, tableFactory, tableManager, networkTableManager);
		
		// TODO: populate the properties
		registerService(bc,container.getDataTypesPane(),CytoPanelComponent.class, new Properties());
		registerService(bc,container.getControlPane(),CytoPanelComponent.class, new Properties());		
		registerService(bc,container,CyAction.class, new Properties());		
	}

}
