package devhood.im.sim.ui;

import java.util.List;

public class GroupChatModel {
	private String groupChatName;
	private String oldGroupChatName;
	private List<String> oldGroupChatUsers;
	private List<String> groupChatUsers;

	public String getGroupChatName() {
		return groupChatName;
	}

	public void setGroupChatName(String groupChatName) {
		this.groupChatName = groupChatName;
	}

	public List<String> getOldGroupChatUsers() {
		return oldGroupChatUsers;
	}

	public void setOldGroupChatUsers(List<String> oldGroupChatUsers) {
		this.oldGroupChatUsers = oldGroupChatUsers;
	}

	public List<String> getGroupChatUsers() {
		return groupChatUsers;
	}

	public void setGroupChatUsers(List<String> groupChatUsers) {
		this.groupChatUsers = groupChatUsers;
	}

	public String getOldGroupChatName() {
		return oldGroupChatName;
	}

	public void setOldGroupChatName(String oldGroupChatName) {
		this.oldGroupChatName = oldGroupChatName;
	}

}
