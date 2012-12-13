package devhood.im.sim.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.dao.interfaces.RoomDao;
import devhood.im.sim.dao.interfaces.UserDao;
import devhood.im.sim.model.Room;
import devhood.im.sim.model.User;

/**
 * RoomDao.
 *
 * @author flo
 *
 */
@Named("sqliteRoomDao")
public class SqliteRoomDao extends SqliteBaseDao implements RoomDao {
	Logger log = Logger.getLogger(SqliteRoomDao.class.toString());

	@Inject
	private UserDao userDao;

	public SqliteRoomDao() {

	}

	@Override
	public void saveOrUpdate(Room r) {
		createTable();
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			int i = -1;
			List<User> users = getUsers(r.getName());
			if (users.size() == 0) {
				pstmt = con.prepareStatement("DELTE FROM room WHERE name = ?");
				pstmt.setString(1, r.getName());
				i = pstmt.executeUpdate();
				con.commit();

				pstmt.close();

				insertUser(r, con);
			} else {
				boolean contains = false;
				for (User u : users) {
					for (User l : r.getUsers()) {
						if (l.getName().equals(u.getName())) {
							contains = true;
						}
					}
				}

				if (!contains) {
					updateUser(r, con, users);
				}
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "User " + username
					+ " konnte nicht abgefragt werden.", e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.log(Level.SEVERE,
							"Connection jonnte nicht geschlossen werden", e);
				}
		}
	}

	private void updateUser(Room r, Connection con, List<User> users)
			throws SQLException {
		List<User> newUsers = new ArrayList<User>();
		newUsers.addAll(r.getUsers());
		newUsers.addAll(users);
		String newUsersList = "";
		Iterator<User> u = newUsers.iterator();
		while (u.hasNext()) {
			User user = u.next();
			newUsersList = newUsersList + user.getName();
			if (u.hasNext()) {
				newUsersList = newUsersList + ",";
			}
		}

		PreparedStatement pstmt;
		int i;
		pstmt = con.prepareStatement("UPDATE room SET users=? where name=?");
		pstmt.setString(1, newUsersList);
		pstmt.setString(2, r.getName());
		i = pstmt.executeUpdate();
		con.commit();

		pstmt.close();
	}

	private void insertUser(Room r, Connection con) throws SQLException {
		PreparedStatement pstmt;
		int i;
		pstmt = con
				.prepareStatement("INSERT INTO room (name,password,users) VALUES (?,?,?)");
		pstmt.setString(1, r.getName());
		i = pstmt.executeUpdate();
		con.commit();

		pstmt.close();
	}

	@Override
	public synchronized List<User> getUsers(String room) {
		createTable();

		List<String> u = new ArrayList<String>();
		List<User> users = new ArrayList<User>();

		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = getConnection();
			con.setAutoCommit(false);

			pstmt = con.prepareStatement("SELECT * FROM room WHERE name=?");
			pstmt.setString(1, room);
			ResultSet resultSet = pstmt.executeQuery();
			con.commit();
			while (resultSet.next()) {
				String usernames = resultSet.getString("users");
				u.addAll(Arrays.asList(usernames.split(",")));
			}

			for (String username : u) {
				users.add(userDao.getUser(username));
			}

			resultSet.close();
			pstmt.close();

		} catch (Exception e) {
			log.log(Level.SEVERE, "User " + username
					+ " konnte nicht abgefragt werden.", e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.log(Level.SEVERE,
							"Connection jonnte nicht geschlossen werden", e);
				}
		}
		return users;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
