package devhood.im.sim.service;

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

	private RegistryService registryService = new InMemoryMockDataRegistryService();
	// private RegistryService userService = new RegistryServiceSqljet();
	// private RegistryService userService = new RegistryServiceJdbc();

	private MessageService messageService = new PeerToPeerMessageService();

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

	/**
	 * Gibt den {@link MessageService} zurueck.
	 * 
	 * @return {@link MessageService}
	 */
	public MessageService getMessageService() {
		return messageService;
	}
}
