package devhood.im.nsim.model;

import java.util.List;

public class GetUserlistResponse extends Message {

	private List<User> users;

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return this.users;
	}

	@Override
	public String getText() {
		return "UserList: " + users;
	}

	@Override
	public String getSender() {
		return "SIM";
	}

	@Override
	public String toString() {
		return "GetUserlistResponse [users=" + users + "]";
	}

}
