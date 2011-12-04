package devhood.im.sim.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.RegistryService;

/**
 * InMemory {@link RegistryService} mit Dummy Usern.
 * 
 * @author flo
 * 
 */
public class InMemoryMockDataRegistryService implements RegistryService {

	private Logger log = Logger.getLogger(InMemoryMockDataRegistryService.class
			.toString());

	/**
	 * inmem user liste.
	 */
	private List<User> users = new ArrayList<User>();

	/**
	 * Returns the List of available Users. Mock Data.
	 * 
	 * @return Users
	 */
	@Override
	public List<User> getUsers() {
		if (users == null || users.size() == 0) {
			for (int i = 0; i < 10; i++) {
				User u = new User();
				u.setName("User " + i);
				u.setLastaccess(new Date());
				users.add(u);
			}
		}
		return users;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh(User user) {
		if (users.contains(user)) {
			User dbUser = users.get(users.indexOf(user));
			dbUser.setLastaccess(new Date());

		} else {
			users.add(user);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logout(String username) {
		User u = new User();
		u.setName(username);

		users.remove(u);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void purgeOfflineUsers() {
		long fiveMinAgo = new Date().getTime() - (60 * 1000 * 1);
		Iterator<User> userIt = users.iterator();
		String userNotice = "";

		while (userIt.hasNext()) {
			User u = userIt.next();
			if (u.getLastaccess() != null
					&& u.getLastaccess().getTime() < fiveMinAgo) {
				userIt.remove();
				log.info(u.getName() + " ist offline");
				userNotice = userNotice + (userNotice.length() > 0 ? ", " : "")
						+ u.getName();
			}
		}

		if (userNotice.length() > 0) {
			EventDispatcher.fireEvent(Events.USER_OFFLINE_NOTICE, userNotice);
		}

	}
}
