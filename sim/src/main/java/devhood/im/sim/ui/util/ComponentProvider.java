package devhood.im.sim.ui.util;

import java.util.logging.Logger;

import devhood.im.sim.service.ServiceLocator;
import devhood.im.sim.ui.SystemTrayManager;
import devhood.im.sim.ui.UserPanel;

/**
 * ComponentProvider dient als einzige Quelle für Oberflächenkomponenten.
 * 
 * @author flo
 * 
 */
public class ComponentProvider {

	private Logger log = Logger.getLogger(ComponentProvider.class.toString());

	private SystemTrayManager systemTrayManager = null;

	private UserPanel userPanel = null;

	private SmileyFactory smileyFactory = null;

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

	public SmileyFactory getSmileyFactory() {
		if (smileyFactory == null) {
			smileyFactory = new MozillaSmileyFactory();
		}

		return smileyFactory;
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

	public UserPanel getUserPanel() {
		if (userPanel == null) {
			userPanel = new UserPanel(ServiceLocator.getInstance()
					.getRegistryService());
		}
		return userPanel;
	}

	public void setUserPanel(UserPanel userPanel) {
		this.userPanel = userPanel;
	}

}
