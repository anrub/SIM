package devhood.im.sim.model;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * User.
 *
 * @author flo
 *
 */
@Entity
@Table(name = "UserWithRooms")
public class User extends AbstractPersistable<Long> {

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RoomUser", joinColumns = { @JoinColumn(nullable = true, name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(nullable = true, name = "room_id", referencedColumnName = "id") })
	@Column(nullable = true)
	private List<Room> rooms = new ArrayList<Room>();

	/**
	 * name of user. to send the messages to the user.
	 */
	@Column(unique = true)
	private String name;

	/**
	 * letzter zugriff des users.
	 */
	private long lastaccess;

	/**
	 * address.
	 */
	private String address;

	/**
	 * UserStatus des Users.
	 */
	private UserStatus statusType;

	/**
	 * public key eines users
	 */
	@Column(columnDefinition = "blob")
	private PublicKey publicKey;

	/**
	 * Port für kommunikation.
	 */
	private int port;

	public User() {

	}

	public User(String name, String address, int port, Date lastaccess,
			PublicKey publicKey, String statusType) {
		this.name = name;
		this.address = address;
		this.port = port;
		this.lastaccess = lastaccess.getTime();
		this.publicKey = publicKey;
		this.statusType = UserStatus.get(statusType);
	}

	/**
	 * Equals prueft auf die Gleichheit des Usernamens.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User u = (User) obj;
		boolean equal = false;

		if (name.equals(u.getName())) {
			equal = true;
		}

		return equal;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserStatus getStatusType() {
		return statusType;
	}

	public void setStatusType(UserStatus statusType) {
		this.statusType = statusType;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public long getLastaccess() {
		return lastaccess;
	}

	public void setLastaccess(long lastaccess) {
		this.lastaccess = lastaccess;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

}
