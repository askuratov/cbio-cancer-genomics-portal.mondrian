package org.mskcc.mondrian.internal;

import java.util.Properties;

import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.service.util.AbstractCyActivator;
import org.osgi.framework.BundleContext;

/**
 * Cytoscape App Activator
 * 
 * @author Dazhi Jiao
 */
public class CyActivator extends AbstractCyActivator {
	@Override
	public void start(BundleContext context) throws Exception {
		CySwingApplication desktop = getService(context,CySwingApplication.class);
		
		// create our gui
		MondrianApp container = MondrianApp.getInstance(desktop);
		
		// TODO: populate the properties
		registerService(context,container.getDataTypesPane(),CytoPanelComponent.class, new Properties());
		registerService(context,container.getControlPane(),CytoPanelComponent.class, new Properties());		
		registerService(context,container,CyAction.class, new Properties());
    }
}
