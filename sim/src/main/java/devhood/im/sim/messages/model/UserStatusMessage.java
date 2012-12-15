package devhood.im.sim.messages.model;

import devhood.im.sim.model.UserStatus;

/**
 * Nachricht mit UserStatus.
 *
 * @author flo
 *
 */
public class UserStatusMessage extends BroadcastMessage {
	private UserStatus userStatus;

	public UserStatusMessage(UserStatus status) {
		this.userStatus = status;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}
}
