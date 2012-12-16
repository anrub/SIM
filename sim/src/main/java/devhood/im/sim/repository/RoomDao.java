package devhood.im.sim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import devhood.im.sim.model.Room;
import devhood.im.sim.model.User;

public interface RoomDao extends CrudRepository<Room, Long> {

	@Transactional(readOnly = true)
	@Query("select r.users from Room r where r.name = ?1")
	public List<User> getUsers(String room);

	@Transactional(readOnly = true)
	@Query("from Room r where r.name = ?1")
	public Room findByName(String roomName);
}
