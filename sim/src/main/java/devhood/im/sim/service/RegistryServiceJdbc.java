package devhood.im.sim.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.RegistryService;

/**
 * {@link RegistryService} Implementierung via JDBC.
 * 
 * users Tabelle:
 * 
 * name String | address String | lastaccess Long
 * 
 * @author flo
 * 
 */
public class RegistryServiceJdbc implements RegistryService {
	private String username = "sa";
	private String password = "";
	private String driver;

	private String url;

	public RegistryServiceJdbc() {

	}

	public Connection getConnection() throws SQLException {
		try {
			Class.forName(driver);
		} catch (Exception e) {
			System.err.println("ERROR: failed to load " + driver
					+ " JDBCdriver.");
			e.printStackTrace();
		}

		Connection connection = DriverManager.getConnection(url, username,
				password);

		return connection;
	}

	/**
	 * Erzeugt die Table, falls sie nicht vorhanden ist.
	 */
	public void createTable() {
		String createTableDdl = "CREATE TABLE IF NOT EXISTS users (name TEXT PRIMARY KEY NOT NULL, address TEXT NOT NULL, port INTEGER NOT NULL, lastaccess INTEGER NOT NULL)";

		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(createTableDdl);
			pstmt.executeUpdate();

			con.commit();

			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getUser(String name) {
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
				u = new User(resultSet.getString("name"),
						resultSet.getString("address"), 
						resultSet.getInt("port"),
						new Date(
								Long.valueOf(resultSet
										.getLong("lastaccess"))));
			} else {
				
			}
			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return u;
	}
	
	@Override
	public List<User> getUsers() {
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
					User u = new User(resultSet.getString("name"),
							resultSet.getString("address"), 
							resultSet.getInt("port"),
							new Date(
									Long.valueOf(resultSet
											.getLong("lastaccess"))));

					users.add(u);
				} while (resultSet.next());
			}
			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return users;
	}

	@Override
	public void logout(String username) {
		createTable();
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement("DELETE FROM users WHERE name=?");
			pstmt.setString(1, username);
			pstmt.executeUpdate();

			con.commit();

			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public void purgeOfflineUsers() {
		createTable();

		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			pstmt = con
					.prepareStatement("DELETE FROM users where lastaccess < ?");
			pstmt.setLong(1, new Date().getTime() - (60 * 1000 * 1));
			pstmt.executeUpdate();

			con.commit();

			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	@Override
	public void refresh(User user) {
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			pstmt = con
					.prepareStatement("UPDATE users SET lastaccess=?, address=?, port=? WHERE name=?");
			pstmt.setLong(1, user.getLastaccess().getTime());
			pstmt.setString(2, user.getAddress());
			pstmt.setInt(3, user.getPort());
			pstmt.setString(4, user.getName());

			int rowCount = pstmt.executeUpdate();

			con.commit();
			pstmt.close();

			if (rowCount == 0) {
				pstmt = con
						.prepareStatement("INSERT INTO users VALUES (?, ? ,?, ?)");
				pstmt.setString(1, user.getName());
				pstmt.setString(2, user.getAddress());
				pstmt.setInt(3, user.getPort());
				pstmt.setLong(4, user.getLastaccess().getTime());
				pstmt.executeUpdate();

				con.commit();
				pstmt.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
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

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
