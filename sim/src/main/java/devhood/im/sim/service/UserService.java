package devhood.im.sim.service;

import java.util.List;

import devhood.im.sim.model.User;

/**
 * User related services.
 * 
 * @author flo
 * 
 */
public interface UserService {

	/**
	 * Returns the List of available Users.
	 * 
	 * @return Users
	 */
	public List<User> getUsers();
}
