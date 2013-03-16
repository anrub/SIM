package devhood.im.sim.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

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

	private List<User> users = new CopyOnWriteArrayList<User>();

	private static Logger log = Logger
			.getLogger(UserValidator.class.toString());

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
		boolean valid = false;

		if (user == null) {
			return valid;
		} else if (inCache(user)) {
			User u = fromCache(user);
			return u.isValid();
		} else {
			addToCache(user);
		}

		try {
			String output = resolveRemoteUser(user);
			valid = validate(user, output);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		return valid;
	}

	/**
	 * Ruft "cmd /c " + validationCommand auf und gibt die Ausgabe zurueck.
	 *
	 * @param user
	 *            User
	 * @return ausgabe
	 * @throws IOException
	 *             excep
	 * @throws InterruptedException
	 *             excep
	 */
	public String resolveRemoteUser(User user) throws IOException,
			InterruptedException {
		String ip = getIp(user);
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

		log.info("Identify User: " + user.getName() + " IP: " + ip
				+ " Wmic run..output:" + builder.toString());
		return builder.toString();
	}

	private String getIp(User user) {
		String ip = user.getAddress();
		try {
			InetAddress address = InetAddress.getByName(user.getAddress());
			ip = address.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

	private boolean validate(User toValidate, String resolvedRemoteUsername) {
		boolean valid = false;

		if (toValidate.getName() != null
				&& resolvedRemoteUsername.contains(toValidate.getName())) {
			valid = true;
		}

		return valid;
	}

	private void addToCache(User user) {
		for (User u : users) {
			if (user.getName().equals(u.getName())) {
				users.remove(u);
			}
		}
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
