package devhood.im.sim.service.interfaces;

import java.util.List;

import devhood.im.sim.model.User;

/**
 * Service zum Zugriff auf User Daten.
 * 
 * @author flo
 * 
 */
public interface UserService {

	/**
	 * Gibt zurueck ob der User aktuell online ist.
	 * 
	 * @param user
	 *            Username
	 * @return true/false
	 */
	public boolean isUserOnline(String user);

	/**
	 * Fuegt einen {@link UserChangeListener} ein.
	 * 
	 * @param listener
	 *            listener
	 */
	public void addUserChangeListener(UserChangeListener listener);

	/**
	 * Loescht inaktive User aus dem System.
	 */
	public void purgeOfflineUsers();

	/**
	 * Gibt die aktuellen User im System zurueck.
	 * 
	 * @return Aktuelle User.
	 */
	public List<User> getCurrentUsers();

	/**
	 * Aktualisiert die Daten der User im System.
	 */
	public void refreshUsers();

	/**
	 * Aktualisiert den User.
	 * 
	 * @param u
	 *            User.
	 */
	public void refresh(User u);

	/**
	 * Gibt den User mit dem Namen zurueck.
	 * 
	 * @param name
	 *            Name.
	 * @return
	 */
	public User getUser(String name);

	/**
	 * Gibt alle User zurueck.
	 * 
	 * @return User.
	 */
	public List<User> getUsers();

	/**
	 * Loggt den User aus.
	 * 
	 * @param username
	 *            username
	 */
	public void logout(String username);
}