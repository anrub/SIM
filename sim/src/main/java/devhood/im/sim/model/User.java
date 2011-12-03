package devhood.im.sim.model;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * address.
	 */
	private String address;

	/**
	 * Id des Users.
	 */
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
