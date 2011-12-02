package devhood.im.sim.service;

import devhood.im.sim.service.interfaces.UserService;

public class ServiceLocator {

	private UserService userService = new DummyUserService();

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
	public UserService getUserService() {
		return userService;
	}
}
