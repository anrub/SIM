package devhood.im.sim.service.interfaces;

import java.util.List;

import devhood.im.sim.model.User;

/**
 * User related services.
 * 
 * @author flo
 * 
 */
public interface RegistryService {

	/**
	 * Returns the List of available Users.
	 * 
	 * @return Users
	 */
	public List<User> getUsers();
}
