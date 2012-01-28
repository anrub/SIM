package devhood.im.sim.config;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.springframework.beans.factory.annotation.Value;

import devhood.im.sim.model.User;
import devhood.im.sim.model.UserStatus;
import devhood.im.sim.ui.util.UiUtil;

/**
 * Konfigurationsklasse von SIM.
 * 
 * @author flo
 * 
 */
@Named("simConfiguration")
public class SimConfiguration {

	private Logger log = Logger.getLogger(SimConfiguration.class.toString());

	private String applicationName = "SIM - S Instant Messenger";
	
	private String projectGithuburl = "https://github.com/anrub/SIM";
	
	private String smileyOverview = "http://www.helpbytes.co.uk/smileys.php";

	/**
	 * Port auf dem der Server lauscht. 0 sucht einen freien Port.
	 */
	private int port = 0;

	/**
	 * Anzahl der Threads zum versenden der Messages.
	 */
	private int senderThreads = 10;

	/**
	 * Laedt User 1 Sekunden nach dem Start.
	 */
	private int userLoadDelay = 1000;

	/**
	 * Laedt User alle 10 Sekunden.
	 */
	private int userLoadPeriod = 10000;

	/**
	 * Interval, in dem der Nutzerstatus in refresh(User) in der DB aktualisiert
	 * wird.
	 */
	private int initRefreshUserStateInterval = 10000;

	/**
	 * Keypair zur Ver/Entschluesselung der Messages.
	 */
	private KeyPair keyPair;

	/**
	 * Name des Tabs in dem der Stream läuft.
	 */
	private String streamTabName = "Stream";

	/**
	 * Pfad zum Trayicon.
	 */
	private String trayIconPath = "/images/trayIcon.gif";

	/**
	 * Systray Icon.
	 */
	private Image trayIcon = UiUtil.createImage(trayIconPath);

	/**
	 * Icon der gelesenen Nachrichten.
	 */
	private ImageIcon readIcon = UiUtil.createImageIcon("/images/read.png",
			"Gelesene Nachrichten");

	/**
	 * Icon der ungelesenen Nachrichten.
	 */
	private ImageIcon unreadIcon = UiUtil.createImageIcon("/images/unread.png",
			"Ungelesene Nachrichten");

	/**
	 * Look and Feel - default System Laf.
	 */
	private String lookAndFeel = UIManager.getSystemLookAndFeelClassName();

	/**
	 * Eigener Benutzername.
	 */
	@Value("#{systemProperties['sim.username']}")
	private String username;

	/**
	 * Erweiterte Konfigurationen, die z.B bestimmte MessageProcessors nutzen.
	 */
	private Map<String, String> configuration;

	/**
	 * Sollen systray messages gezeigt werden oder nicht.
	 */
	private boolean showSystrayMessages = true;

	/**
	 * Zeigt die Stream Meldungen im systray an oder nicht.
	 */
	private boolean showStreamInTray = true;

	/**
	 * Zeigt die Statusmeldungen im Tray an oder nicht.
	 */
	private boolean showStatusInTray = true;

	/**
	 * Eigener User.
	 */
	private User currentUser;

	/**
	 * Gibt den eigenen Benutzer zurueck.
	 * 
	 * @return eigener User.
	 */
	public User getCurrentUser() {
		if (currentUser == null) {
			String x = UserStatus.AVAILABLE.toString();
			currentUser = new User(username, getCurrentIp(), getPort(),
					new Date(), getKeyPair().getPublic(),
					UserStatus.AVAILABLE.getText());
		} else {
			currentUser.setAddress(getCurrentIp());
			currentUser.setPort(getPort());
			currentUser.setPublicKey(getKeyPair().getPublic());
			currentUser.setName(username);
		}

		return currentUser;
	}

	/**
	 * Ermittelt die aktuelle IP..
	 * 
	 * @return ip
	 */
	protected String getCurrentIp() {
		String myIp = null;
		try {
			String hostName = InetAddress.getLocalHost().getHostName();

			InetAddress addrs[] = null;

			addrs = InetAddress.getAllByName(hostName);

			for (InetAddress addr : addrs) {
				if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()) {
					myIp = addr.getHostAddress();
				}
			}
		} catch (UnknownHostException e) {
			log.log(Level.SEVERE,
					"Lokale IP Adresse konnte nicht ermittelt werden!", e);
		}

		return myIp;
	}

	/**
	 * Gibt das KeyPair fuer die Verschluesselung der Messages zurueck.
	 * 
	 * @return KeyPair
	 */
	public KeyPair getKeyPair() {
		if (keyPair == null) {
			KeyPairGenerator kpg;
			try {
				kpg = KeyPairGenerator.getInstance("RSA");
				kpg.initialize(2048);
				keyPair = kpg.generateKeyPair();
			} catch (NoSuchAlgorithmException e) {
				log.log(Level.SEVERE, "Konnte Schluesselpaar nicht generieren",
						e);
			}
		}

		return keyPair;
	}

	/**
	 * Versucht die Versionsnummer aus der Datei /SIM_VERSION.txt aus dem
	 * classpath zu lesen.
	 * 
	 * @return Versionsnummer oder ""
	 */
	public String getVersion() {
		String version = "";
		InputStream is = null;
		try {
			is = getClass().getResourceAsStream("/SIM_VERSION.txt");
			BufferedReader read = new BufferedReader(new InputStreamReader(is));
			version = read.readLine();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Konnte Versionfile nicht lesen", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				log.log(Level.SEVERE, "Konnte Inputstream nicht schliessen", e);
			}
		}

		return version;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Map<String, String> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Map<String, String> configuration) {
		this.configuration = configuration;
	}

	public int getSenderThreads() {
		return senderThreads;
	}

	public void setSenderThreads(int senderThreads) {
		this.senderThreads = senderThreads;
	}

	public void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
	}

	public String getStreamTabName() {
		return streamTabName;
	}

	public void setStreamTabName(String streamTabName) {
		this.streamTabName = streamTabName;
	}

	public String getTrayIconPath() {
		return trayIconPath;
	}

	public void setTrayIconPath(String trayIconPath) {
		this.trayIconPath = trayIconPath;
	}

	public Image getTrayIcon() {
		return trayIcon;
	}

	public void setTrayIcon(Image trayIcon) {
		this.trayIcon = trayIcon;
	}

	public ImageIcon getReadIcon() {
		return readIcon;
	}

	public void setReadIcon(ImageIcon readIcon) {
		this.readIcon = readIcon;
	}

	public ImageIcon getUnreadIcon() {
		return unreadIcon;
	}

	public void setUnreadIcon(ImageIcon unreadIcon) {
		this.unreadIcon = unreadIcon;
	}

	public String getLookAndFeel() {
		return lookAndFeel;
	}

	public void setLookAndFeel(String lookAndFeel) {
		this.lookAndFeel = lookAndFeel;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isShowSystrayMessages() {
		return showSystrayMessages;
	}

	public void setShowSystrayMessages(boolean showSystrayMessages) {
		this.showSystrayMessages = showSystrayMessages;
	}

	public boolean isShowStreamInTray() {
		return showStreamInTray;
	}

	public void setShowStreamInTray(boolean showStreamInTray) {
		this.showStreamInTray = showStreamInTray;
	}

	public boolean isShowStatusInTray() {
		return showStatusInTray;
	}

	public void setShowStatusInTray(boolean showStatusInTray) {
		this.showStatusInTray = showStatusInTray;
	}

	public int getUserLoadDelay() {
		return userLoadDelay;
	}

	public void setUserLoadDelay(int userLoadDelay) {
		this.userLoadDelay = userLoadDelay;
	}

	public int getUserLoadPeriod() {
		return userLoadPeriod;
	}

	public void setUserLoadPeriod(int userLoadPeriod) {
		this.userLoadPeriod = userLoadPeriod;
	}

	public int getInitRefreshUserStateInterval() {
		return initRefreshUserStateInterval;
	}

	public void setInitRefreshUserStateInterval(int initRefreshUserStateInterval) {
		this.initRefreshUserStateInterval = initRefreshUserStateInterval;
	}

	public String getProjectGithuburl() {
		return projectGithuburl;
	}

	public void setProjectGithuburl(String projectGithuburl) {
		this.projectGithuburl = projectGithuburl;
	}

	public String getSmileyOverview() {
		return smileyOverview;
	}

	public void setSmileyOverview(String smileyOverview) {
		this.smileyOverview = smileyOverview;
	}
}
