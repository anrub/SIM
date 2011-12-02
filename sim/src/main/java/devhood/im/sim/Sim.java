package devhood.im.sim;

import java.util.List;

import devhood.im.sim.model.User;

/**
 * SIM - S Instant Messenger.
 * 
 * @author flo
 * 
 */
public class Sim {
	private List<User> selectedUsers;

	public List<User> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<User> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

}
