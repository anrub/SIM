package devhood.im.sim.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import devhood.im.sim.Sim;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.RegistryService;

/**
 * RegistryService welcher auf eine SQLjet Db zugreift.
 * 
 * @author flo
 * 
 */
public class RegistryServiceSqljet implements RegistryService {
	private String path = Sim.dbPath;

	private SqlJetDb db;

	// CREATE TABLE users (`id` INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT
	// NULL, address TEXT NOT NULL)
	// CREATE INDEX full_name_index ON employees(first_name,second_name)
	// CREATE INDEX dob_index ON employees(date_of_birth)

	/**
	 * Erzeugt DB und Tabellen beim Konstruieren.
	 */
	public RegistryServiceSqljet() {
		String createTablesDdl = "CREATE TABLE users (name TEXT PRIMARY KEY NOT NULL, address TEXT NOT NULL, lastaccess INTEGER NOT NULL)";
		String createIndexDdl = "CREATE INDEX idx ON users(name,address,lastaccess)";

		try {
			SqlJetDb db = SqlJetDb.open(new File(path), true);

			try {
				db.getTable("users");
			} catch (Exception e) { // TODO programming by exception
				db.getOptions().setAutovacuum(true);
				db.beginTransaction(SqlJetTransactionMode.WRITE);
				try {
					db.getOptions().setUserVersion(1);
					db.createTable(createTablesDdl);
					db.createIndex(createIndexDdl);
				} finally {
					db.commit();
					db.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Datenbank konnte nicht geoeffnet werden!", e);
		}

	}

	@Override
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		try {
			SqlJetDb db = SqlJetDb.open(new File(path), false);
			try {
				db.beginTransaction(SqlJetTransactionMode.READ_ONLY);

				ISqlJetTable table = db.getTable("users");

				ISqlJetCursor cursor = table.open();
				do {
					String name = cursor.getString("name");

					User u = new User();
					u.setName(name);
					users.add(u);
				} while (cursor.next());
			} finally {
				db.commit();
				db.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("Konnte User Tabelle nicht auslesen!", e);
		}

		return users;
	}

	/**
	 * Refresht den Usereintrag anhand der uebergebenen Userdaten. Identifiert
	 * die Row anhand des username und address. Aktualisiert das feld
	 * lastaccess.
	 * 
	 * @param user
	 *            user obj.
	 */
	@Override
	public void refresh(User user) {
		try {

			SqlJetDb db = SqlJetDb.open(new File(path), true);
			db.beginTransaction(SqlJetTransactionMode.WRITE);

			ISqlJetTable table = db.getTable("users");

			ISqlJetCursor cur = table.scope(null,
					new Object[] { user.getName() },
					new Object[] { user.getName() });

			long count = cur.getRowCount();
			if (count > 0) {
				try {
					do {
						cur.update(user.getName(), user.getAddress(),
								new Date().getTime());
					} while (cur.next());
				} finally {
					db.commit();
					db.close();
				}
			} else {
				Map<String, Object> inserts = new HashMap<String, Object>();
				inserts.put("name", user.getName());
				inserts.put("address", user.getAddress());
				inserts.put("lastaccess", new Date().getTime());
				try {
					table.insertByFieldNames(inserts);
				} finally {
					db.commit();
					db.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Konnte User nicht in db eintragen!", e);
		}

	}

	/**
	 * Löscht die Einträge mit diesem Usernamen aus der DB.
	 * 
	 * @param username
	 *            username.
	 */
	@Override
	public void logout(String username) {
		try {

			SqlJetDb db = SqlJetDb.open(new File(path), true);
			db.beginTransaction(SqlJetTransactionMode.WRITE);

			ISqlJetTable table = db.getTable("users");

			ISqlJetCursor cur = table.scope(null, new Object[] { username },
					new Object[] { username });

			long count = cur.getRowCount();
			if (count > 0) {
				try {
					do {
						cur.delete();
					} while (cur.next());
				} finally {
					db.commit();
					db.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Konnte User nicht ausloggen!", e);
		}

	}

	@Override
	public void purgeOfflineUsers() {
		// TODO Auto-generated method stub

	}
}
