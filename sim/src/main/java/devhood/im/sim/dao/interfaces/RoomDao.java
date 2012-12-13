package devhood.im.sim.dao.interfaces;

import java.util.List;

import devhood.im.sim.model.User;
import devhood.im.sim.service.Room;

public interface RoomDao {
	public List<User> getUsers(String room);

	public void saveOrUpdate(Room r);
}
