package devhood.im.sim.service.interfaces;

import java.util.List;

import devhood.im.sim.model.User;

/**
 * Listener wird benachrichtigt wenn Benutzer hinzukommen oder entfernt werden.
 * 
 * @author flo
 * 
 */
public interface UserChangeListener {
	/**
	 * Wenn User hinzu kommen.
	 * 
	 * @param user
	 *            neue User.
	 */
	public void userAdded(List<User> user);

	/**
	 * User wurden entfernt.
	 * 
	 * @param user
	 *            entfernte User.
	 */
	public void userRemoved(List<User> user);
}
