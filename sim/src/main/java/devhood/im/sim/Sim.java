package devhood.im.sim;

import java.net.InetAddress;
import java.net.UnknownHostException;

import devhood.im.sim.model.User;

/**
 * SIM - S Instant Messenger.
 * 
 * @author flo
 * 
 */
public class Sim {
	public static String dbPath = "C:/Daten/im/sqlite.db";

	public static String applicationName = "SIM - S Intstant Messenger";

	public static String username = "flo";

	public static String getUsername() {
		return username;
	}

	public static User getUser() {
		User u = new User();
		u.setName(username);
		u.setAddress(getCurrentIp());

		return u;

	}

	private static String getCurrentIp() {
		String hostName = null;
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InetAddress addrs[] = null;
		try {
			addrs = InetAddress.getAllByName(hostName);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String myIp = "UNKNOWN";
		for (InetAddress addr : addrs) {
			if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()) {
				myIp = addr.getHostAddress();
			}
		}

		return myIp;
	}
}
