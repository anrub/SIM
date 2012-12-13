package devhood.im.sim.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;

import devhood.im.sim.config.SimConfiguration;

public class SqliteBaseDao {
	Logger log = Logger.getLogger(SqliteBaseDao.class.toString());

	protected String username = "sa";
	private String password = "";
	private String driver = "org.sqlite.JDBC";
	@Value("#{systemProperties['sim.dbPath']}")
	private String dbPath;
	@Inject
	protected SimConfiguration simConfiguration;
	/**
	 * Sooft wird versucht, die schreibenden Anfragen durchzufuehren, jeweils
	 */
	protected int retryIfFailed = 3;
	/**
	 * ms, die gewartet werden, bevor ein neuer Versuch gestartet wird.
	 */
	protected int retryWait = 1000;

	public SqliteBaseDao() {
		super();
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
			log.log(Level.SEVERE, "ERROR: failed to load " + driver
					+ " JDBCdriver.", e);
		}

		Connection connection = DriverManager.getConnection(getUrl(), username,
				password);

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
			String createUsersTableDdl = "CREATE TABLE IF NOT EXISTS users (name TEXT PRIMARY KEY NOT NULL, address TEXT NOT NULL, port INTEGER NOT NULL, lastaccess INTEGER NOT NULL, publickey BLOB NOT NULL, statusType TEXT NOT NULL)";
			String createRoomTableDdl = "CREATE TABLE IF NOT EXISTS room (name TEXT, password TEXT, users)";

			success = createTable(tries, createUsersTableDdl);

			success = createTable(tries, createRoomTableDdl);
		}
	}

	private boolean createTable(int tries, String createTableDdl) {
		boolean success = false;
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
					log.log(Level.SEVERE,
							"Connection konnte nicht geschlossen werden", e);
				}
		}
		return success;
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