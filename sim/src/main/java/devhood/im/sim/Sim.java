package devhood.im.sim;

import java.awt.Image;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import devhood.im.sim.model.User;
import devhood.im.sim.model.UserStatus;
import devhood.im.sim.service.PeerToPeerMessageSender;
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

	private static Logger log = Logger.getLogger(PeerToPeerMessageSender.class
			.toString());

	/**
	 * Pfad zur Registry db.
	 */
	public static String dbPath = "C:/Daten/im/sqlite.db";

	public static String applicationName = "SIM - S Intstant Messenger";

	/**
	 * Pfad zum Trayicon.
	 */
	public static String trayIconPath = "/images/trayIcon.gif";

	/**
	 * Port auf dem der Server lauscht. 0 sucht einen freien Port.
	 */
	public static int port = 0;

	/**
	 * Anzahl der Threads zum versenden der Messages.
	 */
	public static int senderThreads = 10;

	/**
	 * Keypair zur Ver/Entschluesselung der Messages.
	 */
	public static KeyPair keyPair = null;

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

	/**
	 * Eigener User.
	 */
	private static User currentUser;

	/**
	 * Gibt den eigenen Benutzer zurueck.
	 * 
	 * @return eigener User.
	 */
	public static User getCurrentUser() {
		String username = System.getProperty("user.name");

		if (currentUser == null) {
			String x = UserStatus.AVAILABLE.toString();
			currentUser = new User(username, getCurrentIp(), getPort(),
					new Date(), getKeyPair().getPublic(),
					UserStatus.AVAILABLE.getText());
		} else {
			currentUser.setAddress(getCurrentIp());
			currentUser.setPort(getPort());
			currentUser.setPublicKey(getKeyPair().getPublic());
		}

		return currentUser;
	}

	/**
	 * Ermittelt die aktuelle IP..
	 * 
	 * @return ip
	 */
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

	/**
	 * Gibt das KeyPair fuer die Verschluesselung der Messages zurueck.
	 * 
	 * @return KeyPair
	 */
	public static KeyPair getKeyPair() {
		if (keyPair == null) {
			KeyPairGenerator kpg;
			try {
				kpg = KeyPairGenerator.getInstance("RSA");
				kpg.initialize(2048);
				keyPair = kpg.generateKeyPair();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		return keyPair;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int p) {
		port = p;
	}

}
