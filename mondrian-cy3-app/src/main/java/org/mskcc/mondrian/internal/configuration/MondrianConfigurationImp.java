package org.mskcc.mondrian.internal.configuration;

/**
 * Maintains the current configuration for the plugin.
 *
 * @author Benjamin Gross
 * @author Dazhi Jiao
 */
public class MondrianConfigurationImp implements MondrianConfiguration {
	
	protected static MondrianConfiguration instance;
	
	public static MondrianConfiguration getInstance() {
		if (instance == null) {
			instance = new MondrianConfigurationImp();
		}
		return instance;
	}
	
	private MondrianConfigurationImp() {
		
	}

	@Override
	public void addConfigurationListener(MondrianConfigurationListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removedConfigurationListener(
			MondrianConfigurationListener listener) {
		// TODO Auto-generated method stub
		
	}

}
