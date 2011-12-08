package devhood.im.sim.model;

import java.security.PublicKey;
import java.util.Date;

/**
 * User.
 * 
 * @author flo
 * 
 */
public class User {

	/**
	 * name of user. to send the messages to the user.
	 */
	private String name;

	/**
	 * letzter zugriff des users.
	 */
	private Date lastaccess;

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
	private PublicKey publicKey;

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	/**
	 * port f�r kommunikation
	 */
	private int port;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public User(String name, String address, int port, Date lastaccess,
			PublicKey publicKey, String statusType) {
		this.name = name;
		this.address = address;
		this.port = port;
		this.lastaccess = lastaccess;
		this.publicKey = publicKey;
		this.statusType = UserStatus.get(statusType);
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

	public Date getLastaccess() {
		return lastaccess;
	}

	public void setLastaccess(Date lastaccess) {
		this.lastaccess = lastaccess;
	}

	public UserStatus getStatusType() {
		return statusType;
	}

	public void setStatusType(UserStatus statusType) {
		this.statusType = statusType;
	}

}
