package devhood.im.sim.dao.interfaces;

import java.util.List;

import devhood.im.sim.model.User;

public interface RoomDao {
	public List<User> getUsers(String room);
}
