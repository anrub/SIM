package devhood.im.sim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import devhood.im.sim.model.User;

/**
 * RegistryService - Liefert persistente Daten, z.B. User.
 *
 * @author flo
 *
 */
interface UserRepository extends CrudRepository<User, Long> {

	/**
	 * Such einen User anhand seines Namens
	 *
	 * @return User Benutzer oder null
	 */
	@Transactional(readOnly = true)
	@Query("from User u where u.name = ?1")
	public User findByTheUsersName(String name);

	@Modifying
	@Transactional
	@Query("delete from User u where u.name = ?1")
	public void deleteByUsername(String username);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "delete from roomuser where user_id = ?1")
	public void deleteRelationsToRoom(Long userid);

	/**
	 * Loescht die offline user aus der db.
	 */
	@Modifying
	@Transactional
	@Query("delete from User u where u.lastaccess <= ?1")
	public void purgeOfflineUsers(long dateTime);



	@Transactional(readOnly=true)
	@Query("from User u where u.lastaccess <= ?1")
	public List<User> getInactiveUser(long dateTime);

}
