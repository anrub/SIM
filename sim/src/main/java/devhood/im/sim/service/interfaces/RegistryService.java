package devhood.im.sim.service.interfaces;

import java.util.List;

import devhood.im.sim.model.User;

/**
 * RegistryService - Liefert persistente Daten, z.B. User.
 * 
 * @author flo
 * 
 */
public interface RegistryService {

	/**
	 * Gibt eine Liste von Usern zurueck.
	 * 
	 * @return Users
	 */
	public List<User> getUsers();

	/**
	 * Aktualisiert den Status des Users und der Adresse. User ist technisch
	 * verfuegbar.
	 * 
	 * @param user
	 *            User.
	 */
	public void refresh(User user);

	/**
	 * Loescht diesen User aus der Datenbank. Ist damit fuer andere nicht mehr
	 * sichtbar.
	 * 
	 * @param username
	 *            Username.
	 */
	public void logout(String username);
}
