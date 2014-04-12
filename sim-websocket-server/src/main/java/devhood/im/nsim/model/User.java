package devhood.im.nsim.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import devhood.im.sim.model.UserStatus;

/**
 * User.
 * 
 * @author flo
 * 
 */
public class User {

	private List<Room> rooms = new ArrayList<Room>();

	/**
	 * name of user. to send the messages to the user.
	 */
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
	 * Port für kommunikation.
	 */
	private int port;

	public User() {

	}

	public User(String name, String address, int port, Date lastaccess,
			String statusType) {
		this.name = name;
		this.address = address;
		this.port = port;
		this.lastaccess = lastaccess.getTime();
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

	@Override
	public String toString() {
		return "User [rooms=" + rooms + ", name=" + name + ", lastaccess="
				+ lastaccess + ", address=" + address + ", statusType="
				+ statusType + ", port=" + port + "]";
	}

}
