package devhood.im.sim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import devhood.im.sim.model.Room;
import devhood.im.sim.model.User;

public interface RoomRepository extends CrudRepository<Room, Long> {

	@Transactional(readOnly = true)
	@Query("select r.users from Room r where r.name = ?1")
	public List<User> getUsers(String room);

	@Transactional(readOnly = true)
	@Query("from Room r where r.name = ?1")
	public Room findByName(String roomName);



	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "delete from roomuser where user_id = ?1 and room_id = ?2")
	public void quitRoom(long userid, long roomid);
}
