package devhood.im.sim;

import java.awt.Image;
import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	public static String username = System.getProperty("user.name");

	public static String trayIconPath = "/images/trayIcon.gif";

	public static int port = 0;

	public static int senderThreads = 10;

	/**
	 * Name des Tabs in dem der Stream läuft.
	 */
	public static String streamTabName = "Stream";

	/**
	 * Systray Icon.
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
		User u = new User(username, getCurrentIp(), getPort(), new Date());
		return u;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int p) {
		port = p;
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

	public static String getBuildDate() {
		String buildDate = "";
		try {
			URL url = Sim.class.getResource("/");
			File f = new File(url.toURI());
			Date lastMod = new Date(f.lastModified());
			DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

			buildDate = df.format(lastMod);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return buildDate;
	}

}
