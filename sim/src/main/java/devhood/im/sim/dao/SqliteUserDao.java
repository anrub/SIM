package devhood.im.sim.dao;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.dao.interfaces.UserDao;
import devhood.im.sim.model.User;



/**
 * {@link UserDao} Implementierung via JDBC.
 * 
 * users Tabelle:
 * 
 * name String | address String | port int | lastaccess Long | publickey BLOB | statusType TEXT
 * 
 * @author flo
 * 
 */
@Named("sqliteServiceJdbc")
public class SqliteUserDao implements UserDao {
	private String username = "sa";
	private String password = "";

	private String driver = "org.sqlite.JDBC";

	@Value("#{systemProperties['sim.dbPath']}")
	private String dbPath;

	@Inject
	private SimConfiguration simConfiguration;

	private Logger log = Logger.getLogger(SqliteUserDao.class.toString());

	/**
	 * Sooft wird versucht, die schreibenden Anfragen durchzufuehren, jeweils
	 */
	private int retryIfFailed = 3;

	/**
	 * ms, die gewartet werden, bevor ein neuer Versuch gestartet wird.
	 */
	private int retryWait = 1000;



	public SqliteUserDao() {

	}



	/**
	 * Gibt die Connection zurueck.
	 * 
	 * @return Connection
	 * @throws SQLException
	 *             Im Fehlerfall.
	 */
	public synchronized Connection getConnection() throws SQLException {
		try {
			Class.forName(driver);
		} catch (Exception e) {
			log.log(Level.SEVERE, "ERROR: failed to load " + driver + " JDBCdriver.", e);
		}

		Connection connection = DriverManager.getConnection(getUrl(), username, password);

		return connection;
	}



	/**
	 * Erzeugt die Table, falls sie nicht vorhanden ist.
	 */
	public synchronized void createTable() {
		int tries = 0;
		boolean success = false;

		while (tries <= retryIfFailed && !success) {
			tries++;
			String createTableDdl = "CREATE TABLE IF NOT EXISTS users (name TEXT PRIMARY KEY NOT NULL, address TEXT NOT NULL, port INTEGER NOT NULL, lastaccess INTEGER NOT NULL, publickey BLOB NOT NULL, statusType TEXT NOT NULL)";

			Connection con = null;
			PreparedStatement pstmt;
			try {

				if (tries > 1) {
					Thread.sleep(retryWait);
				}

				con = getConnection();
				con.setAutoCommit(false);
				pstmt = con.prepareStatement(createTableDdl);
				pstmt.executeUpdate();

				con.commit();

				pstmt.close();
				success = true;
			} catch (Exception e) {
				log.log(Level.SEVERE, "Konnte Tabelle nicht anlegen", e);
			} finally {
				if (con != null)
					try {
						con.close();
					} catch (SQLException e) {
						log.log(Level.SEVERE, "Connection konnte nicht geschlossen werden", e);
					}
			}
		}
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized User getUser(String name) {
		if (name.equals(simConfiguration.getUsername())) {
			return simConfiguration.getCurrentUser();
		}
		createTable();

		User u = null;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = getConnection();
			con.setAutoCommit(false);

			pstmt = con.prepareStatement("SELECT * FROM users WHERE name=?");
			pstmt.setString(1, name);
			ResultSet resultSet = pstmt.executeQuery();
			con.commit();
			if (resultSet.next()) {

				u = new User(resultSet.getString("name"), resultSet.getString("address"), resultSet.getInt("port"),
						new Date(Long.valueOf(resultSet.getLong("lastaccess"))),
						getPublicKey(resultSet.getBytes("publickey")), resultSet.getString("statusType"));
			}
			resultSet.close();
			pstmt.close();

		} catch (Exception e) {
			log.log(Level.SEVERE, "User " + username + " konnte nicht abgefragt werden.", e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.log(Level.SEVERE, "Connection jonnte nicht geschlossen werden", e);
				}
		}

		return u;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized List<User> getUsers() {
		createTable();
		List<User> users = new ArrayList<User>();

		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = getConnection();
			con.setAutoCommit(false);

			pstmt = con.prepareStatement("SELECT * FROM users");
			ResultSet resultSet = pstmt.executeQuery();
			con.commit();
			if (resultSet.next()) {
				do {
					User u = new User(resultSet.getString("name"), resultSet.getString("address"),
							resultSet.getInt("port"), new Date(Long.valueOf(resultSet.getLong("lastaccess"))),
							getPublicKey(resultSet.getBytes("publickey")), resultSet.getString("statusType"));

					users.add(u);
				} while (resultSet.next());
			}
			resultSet.close();
			pstmt.close();

		} catch (Exception e) {
			log.log(Level.SEVERE, "Users konnten nicht ermittelt werden", e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.log(Level.SEVERE, "Connection konnte nicht geschlossen werden", e);
				}
		}
		return users;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void logout(String username) {
		createTable();
		int tries = 0;
		boolean success = false;

		while (tries <= retryIfFailed && !success) {
			tries++;
			Connection con = null;
			PreparedStatement pstmt;
			try {
				if (tries > 1) {
					Thread.sleep(retryWait);
				}
				con = getConnection();
				con.setAutoCommit(false);
				pstmt = con.prepareStatement("DELETE FROM users WHERE name=?");
				pstmt.setString(1, username);
				pstmt.executeUpdate();

				con.commit();

				pstmt.close();

				success = true;
			} catch (Exception e) {
				log.log(Level.SEVERE, "Konnte logout von " + username + "nicht druchfuehren", e);
			} finally {
				if (con != null)
					try {
						con.close();
					} catch (SQLException e) {
						log.log(Level.SEVERE, "Connection konnte nicht geschlossen werden", e);
					}
			}
		}
	}



	/**
	 * {@inheritDoc}
	 */
	public synchronized void purgeOfflineUsers() {
		createTable();
		int tries = 0;
		boolean success = false;

		while (tries <= retryIfFailed &&  !success) {
			tries++;

			Connection con = null;
			PreparedStatement pstmt;
			try {
				if (tries > 1) {
					Thread.sleep(retryWait);
				}

				con = getConnection();
				con.setAutoCommit(false);
				pstmt = con.prepareStatement("DELETE FROM users where lastaccess < ?");
				pstmt.setLong(1, new Date().getTime() - (60 * 5000 * 1));
				pstmt.executeUpdate();

				con.commit();

				pstmt.close();
				success = true;
			} catch (Exception e) {
				log.log(Level.SEVERE, "User konnten nicht geloescht werden", e);
			} finally {
				if (con != null)
					try {
						con.close();
					} catch (SQLException e) {
						log.log(Level.SEVERE, "Connection konnte nicht geschlossen werden");
					}
			}
		}
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void refresh(User user) {
		int tries = 0;
		boolean success = false;

		while (tries <= retryIfFailed && !success) {
			tries++;
			Connection con = null;
			PreparedStatement pstmt;
			try {
				if (tries > 1) {
					Thread.sleep(retryWait);
				}

				con = getConnection();
				con.setAutoCommit(false);
				pstmt = con
						.prepareStatement("UPDATE users SET lastaccess=?, address=?, port=?, publickey=?, statusType=? WHERE name=?");
				pstmt.setLong(1, user.getLastaccess().getTime());
				pstmt.setString(2, user.getAddress());
				pstmt.setInt(3, user.getPort());
				pstmt.setBytes(4, user.getPublicKey().getEncoded());
				pstmt.setString(5, user.getStatusType().getText());

				pstmt.setString(6, user.getName());

				int rowCount = pstmt.executeUpdate();

				con.commit();
				pstmt.close();

				if (rowCount == 0) {
					pstmt = con.prepareStatement("INSERT INTO users VALUES (?, ? ,?, ?, ?,?)");
					pstmt.setString(1, user.getName());
					pstmt.setString(2, user.getAddress());
					pstmt.setInt(3, user.getPort());
					pstmt.setLong(4, user.getLastaccess().getTime());
					pstmt.setBytes(5, user.getPublicKey().getEncoded());
					pstmt.setString(6, user.getStatusType().getText());

					pstmt.executeUpdate();

					con.commit();
					pstmt.close();
				}
				success = true;
			} catch (Exception e) {
				log.log(Level.SEVERE, "Konnte Update/Insert des Users nicht durchfuehren", e);
			} finally {
				if (con != null)
					try {
						con.close();
					} catch (SQLException e) {
						log.log(Level.SEVERE, "Connection konnte nicht geschlossen werden", e);
					}
			}
		}
	}



	/**
	 * {@inheritDoc}
	 */
	private synchronized PublicKey getPublicKey(byte[] publicKeyRaw) {
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



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getUrl() {
		return "jdbc:sqlite:" + dbPath;
	}



	public String getDbPath() {
		return dbPath;
	}



	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}
}
