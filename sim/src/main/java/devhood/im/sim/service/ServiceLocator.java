package devhood.im.sim.service;

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

	private RegistryService userService = new DummyRegistryService();
	// private RegistryService userService = new RegistryServiceSqljet();

	private MessageService messageService = new DummyMessageService();

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
		return userService;
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
