package devhood.im.sim.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.dao.interfaces.RoomDao;
import devhood.im.sim.dao.interfaces.UserDao;
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
