package devhood.im.nsim.model;

import java.util.List;

public class UnreadMessages extends Message {
	private List<String> ofUsers;

	public List<String> getOfUsers() {
		return ofUsers;
	}

	public void setOfUsers(List<String> ofUsers) {
		this.ofUsers = ofUsers;
	}

	@Override
	public String getText() {
		return "Unread: " + ofUsers.toString();
	}

	@Override
	public String getSender() {
		return "SIM";
	}
	
}
