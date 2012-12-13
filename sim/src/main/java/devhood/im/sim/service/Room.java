package devhood.im.sim.service;

import java.util.List;

import devhood.im.sim.model.User;

public class Room {
	private String name;

	private List<User> users;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
