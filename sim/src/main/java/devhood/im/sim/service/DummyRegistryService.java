package devhood.im.sim.service;

import java.util.ArrayList;
import java.util.List;

import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.RegistryService;

/**
 * Dummy user service mock data.
 * 
 * @author flo
 * 
 */
public class DummyRegistryService implements RegistryService {

	/**
	 * Returns the List of available Users. Mock Data.
	 * 
	 * @return Users
	 */
	@Override
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();

		for (int i = 0; i < 10; i++) {
			User u = new User();
			u.setName("User " + i);
			u.setId(Integer.toString(i));
			users.add(u);
		}
		return users;
	}

	@Override
	public void refresh(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout(String username) {
		// TODO Auto-generated method stub

	}
}
