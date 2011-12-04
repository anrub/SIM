package devhood.im.sim;

import java.awt.Image;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import devhood.im.sim.model.User;
import devhood.im.sim.ui.util.UiUtil;

/**
 * SIM - S Instant Messenger.<br />
 * 
 * Dies ist die zentrale Konfigurationsklasse.
 * 
 * @author flo
 * 
 */
public class Sim {
	/**
	 * Pfad zur Registry db.
	 */
	public static String dbPath = "C:/Daten/im/sqlite.db";

	public static String applicationName = "SIM - S Intstant Messenger";

	public static String username = "flo";

	public static String trayIconPath = "/images/trayIcon.gif";
	/**
	 * Icon der gelesenen Nachrichten.
	 */
	public static Image trayIcon = UiUtil.createImage(trayIconPath);

	/**
	 * Icon der gelesenen Nachrichten.
	 */
	public static ImageIcon readIcon = UiUtil.createImageIcon(
			"/images/read.png", "Gelesene Nachrichten");

	/**
	 * Icon der ungelesenen Nachrichten.
	 */
	public static ImageIcon unreadIcon = UiUtil.createImageIcon(
			"/images/unread.png", "Ungelesene Nachrichten");

	/**
	 * Look and Feel - default System Laf.
	 */
	public static String lookAndFeel = UIManager
			.getSystemLookAndFeelClassName();

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
