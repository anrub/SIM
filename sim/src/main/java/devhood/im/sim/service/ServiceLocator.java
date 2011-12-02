package devhood.im.sim.service;

import devhood.im.sim.service.interfaces.RegistryService;

public class ServiceLocator {

	private RegistryService userService = new DummyRegistryService();

	private static ServiceLocator instance;

	public static ServiceLocator getInstance() {
		if (instance == null) {
			instance = new ServiceLocator();
		}

		return instance;
	}

	private ServiceLocator() {

	}

	/**
	 * Returns the UserService;.
	 * 
	 * @return UserService;
	 */
	public RegistryService getUserService() {
		return userService;
	}
}
