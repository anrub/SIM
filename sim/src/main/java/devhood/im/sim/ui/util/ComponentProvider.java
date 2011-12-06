package devhood.im.sim.ui.util;

import java.util.logging.Logger;

import devhood.im.sim.ui.SystemTrayManager;

/**
 * ComponentProvider dient als einzige Quelle für Oberflächenkomponenten.
 * 
 * @author flo
 * 
 */
public class ComponentProvider {

	private Logger log = Logger.getLogger(ComponentProvider.class.toString());

	private SystemTrayManager systemTrayManager = null;

	private static ComponentProvider instance;

	/**
	 * Gibt eine Instanz von {@link ComponentProvider} zurueck.
	 * 
	 * @return {@link ComponentProvider}.
	 */
	public static ComponentProvider getInstance() {
		if (instance == null) {
			instance = new ComponentProvider();
		}

		return instance;
	}

	/**
	 * Singleton.
	 */
	private ComponentProvider() {

	}

	public SystemTrayManager getSystemTrayManager() {
		if (systemTrayManager == null) {
			systemTrayManager = new SystemTrayManager();
		}
		return systemTrayManager;
	}

	public void setSystemTrayManager(SystemTrayManager systemTrayManager) {
		this.systemTrayManager = systemTrayManager;
	}

}
