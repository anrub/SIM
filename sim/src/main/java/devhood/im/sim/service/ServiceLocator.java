package devhood.im.sim.service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import devhood.im.sim.Sim;
import devhood.im.sim.service.interfaces.MessageService;
import devhood.im.sim.service.interfaces.RegistryService;

/**
 * ServiceLocator ermoeglicht den Lookup von bekannten Services in der
 * Applikation.
 * 
 * @author flo
 * 
 */
public class ServiceLocator {

	private Logger log = Logger.getLogger(ServiceLocator.class
			.toString());
	
	private RegistryService registryService = null;

	private MessageService messageService = null;

	private static ServiceLocator instance;

	/**
	 * Gibt eine Instanz von {@link ServiceLocator} zurueck.
	 * 
	 * @return {@link ServiceLocator}.
	 */
	public static ServiceLocator getInstance() {
		if (instance == null) {
			instance = new ServiceLocator();
		}

		return instance;
	}

	/**
	 * Singleton.
	 */
	private ServiceLocator() {

	}

	/**
	 * Returns the {@link RegistryService}.
	 * 
	 * @return {@link RegistryService}.
	 */
	public RegistryService getRegistryService() {
		if (registryService == null) {
			RegistryServiceJdbc jdbc = new RegistryServiceJdbc();
			jdbc.setDriver("org.sqlite.JDBC");
			jdbc.setUrl("jdbc:sqlite:" + Sim.dbPath);

			registryService = jdbc;
		}
		return registryService;
	}

	
}
