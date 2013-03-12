package devhood.im.sim.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import devhood.im.sim.model.User;

/**
 * Validiert den User anhand eines Systemabhängigen Befehls.
 *
 * @author flo
 *
 */
@Named
public class UserValidator {

	private String validationCommand = "WMIC /Node:{0} ComputerSystem Get UserName";

	private List<User> users = new ArrayList<User>();

	/**
	 * Gibt zurueck ob der Benutzer valide ist. Prueft ob die validationCommand
	 * Ausgabe des den userPrefix + User.username entspricht. Wenn Ja dann
	 * entspricht der user dem in der Domaene bekannten User. Wenn nein
	 * entspricht der Benutzername im Userobjekt nicht dem in der Domaene
	 * bekannten Usernamen, er wurde dann vermutlich am Client geaendert.
	 *
	 * @param user
	 *            User
	 * @return true/false
	 */
	public boolean isValid(User user) {
		if (user == null) {
			return false;
		}
		if (inCache(user)) {
			User u = fromCache(user);
			return u.isValid();
		} else {
			addToCache(user);
		}

		boolean valid = false;
		try {
			String ip = user.getAddress();
			try {
				InetAddress address = InetAddress.getByName(user.getAddress());
				ip = address.getHostAddress();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String cmd = MessageFormat.format(validationCommand, ip);
			Process p = Runtime.getRuntime().exec("cmd /c " + cmd);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = reader.readLine();
			StringBuilder builder = new StringBuilder();
			while (line != null) {
				builder.append(line);
				line = reader.readLine();
			}

			valid = validate(user, builder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		return valid;
	}

	private boolean validate(User user, String string) {
		boolean valid = false;

		if (user.getName() != null && string.contains(user.getName())) {
			valid = true;
		}

		return valid;
	}

	private void addToCache(User user) {
		users.add(user);
	}

	private User fromCache(User user) {
		int index = users.indexOf(user);
		User u = users.get(index);

		return u;
	}

	private boolean inCache(User user) {
		boolean incache = false;
		if (users.contains(user)) {
			int index = users.indexOf(user);
			User u = users.get(index);
			if (!u.getAddress().equals(user.getAddress())
					|| u.getPort() != user.getPort()) {
				incache = false;
			} else {
				incache = true;
			}

		}

		return incache;
	}

	/**
	 * validationFormat, wird intern zu einem {@link MessageFormat}. Parameter
	 * {0} ist die IP Adresse des Users.
	 *
	 * @param validationCommand
	 *            validationCommand.
	 */
	public void setValidationCommand(String validationCommand) {
		this.validationCommand = validationCommand;
	}

}
