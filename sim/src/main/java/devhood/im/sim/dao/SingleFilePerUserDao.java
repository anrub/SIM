package devhood.im.sim.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;

import devhood.im.sim.dao.interfaces.UserDao;
import devhood.im.sim.model.User;

/**
 * Dieses Dao verwendet ein File pro User.
 * 
 * @author flo
 * 
 */
@Named("singleFilePerUserDao")
public class SingleFilePerUserDao implements UserDao {
	private static Logger log = Logger.getLogger(SingleFilePerUserDao.class
			.toString());

	/**
	 * Das ist das Datenverzeichnis, hier wird ein File pro User erstellt.
	 */

	@Value("#{systemProperties['sim.dbPath']}")
	private String dataFolder = "";

	@Override
	public User getUser(String name) {

		User u = null;
		ObjectInputStream os = null;
		try {
			File f = getFile(name);

			os = new ObjectInputStream(new FileInputStream(f));

			u = (User) os.readObject();

		} catch (Exception e) {
			log.log(Level.SEVERE, "User konnte nicht gelesen werden.");
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				log.log(Level.SEVERE, "Konnte File nicht schließen");
			}
		}
		return u;
	}

	@Override
	public List<User> getUsers() {
		String[] content = getDataFolder().list();
		List<User> users = new ArrayList<User>();
		if (content != null && content.length > 0) {
			for (String line : content) {
				users.add(getUser(line));
			}
		}

		return users;
	}

	protected File getFile(String username) {
		File userFile = new File(dataFolder + "/" + username);

		if (!userFile.exists()) {
			try {
				userFile.createNewFile();
			} catch (IOException e) {
				log.log(Level.SEVERE, "Userfile konnte nicht angelegt werden!",
						e);
			}
		}

		userFile.deleteOnExit();

		return userFile;
	}

	protected File getDataFolder() {
		return new File(dataFolder);
	}

	@Override
	public void refresh(User user) {
		ObjectOutputStream os = null;
		try {
			File userFile = getFile(user.getName());
			os = new ObjectOutputStream(new FileOutputStream(userFile));

			os.writeObject(user);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				log.log(Level.SEVERE, "Konnte FIle nicht schliessen", e);
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	protected synchronized PublicKey getPublicKey(byte[] publicKeyRaw) {
		PublicKey publicKey = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyRaw);
			publicKey = keyFactory.generatePublic(publicKeySpec);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Public Key konnte nicht erzeugt werden", e);
		}
		return publicKey;
	}

	@Override
	public void logout(String username) {
		File f = getFile(username);
		f.delete();
	}

	@Override
	public void purgeOfflineUsers() {
		String[] content = getDataFolder().list();

		if (content != null && content.length > 0) {
			for (String line : content) {
				File f = getFile(line);
				try {
					if (f.lastModified() <= new Date().getTime()
							- (60 * 1000 * 5)) {
						f.delete();
					}
				} catch (Exception e) {
					log.log(Level.SEVERE, "Konnte userfile nicht loeschen", e);
				}
			}
		}
	}

}
