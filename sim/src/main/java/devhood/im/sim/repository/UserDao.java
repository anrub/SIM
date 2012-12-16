package devhood.im.sim.repository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import devhood.im.sim.model.User;

/**
 * UserDao Wrapper, benoetigt um manytomany relation zu loeschen.
 *
 * @author flo
 *
 */
@Named
public class UserDao {
	@Inject
	private UserRepository userRepository;

	@PersistenceContext
	private EntityManager em;

	public void deleteByUsername(String username) {
		User u = userRepository.findByTheUsersName(username);
		deleteRelationsToRoom(u.getId());
		userRepository.deleteByUsername(username);
	}

	public User findByTheUsersName(String name) {
		return userRepository.findByTheUsersName(name);
	}

	public void deleteRelationsToRoom(Long userid) {
		userRepository.deleteRelationsToRoom(userid);
	}

	public <S extends User> S save(S entity) {
		return userRepository.save(entity);
	}

	public void purgeOfflineUsers(long dateTime) {
		List<User> users = userRepository.getInactiveUser(dateTime);
		for (User u : users) {
			deleteByUsername(u.getName());
		}
	}

	public <S extends User> Iterable<S> save(Iterable<S> entities) {
		return userRepository.save(entities);
	}

	public User findOne(Long id) {
		return userRepository.findOne(id);
	}

	public boolean exists(Long id) {
		return userRepository.exists(id);
	}

	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	public Iterable<User> findAll(Iterable<Long> ids) {
		return userRepository.findAll(ids);
	}

	public long count() {
		return userRepository.count();
	}

	public void delete(Long id) {
		userRepository.delete(id);
	}

	public void delete(User entity) {
		userRepository.delete(entity);
	}

	public void delete(Iterable<? extends User> entities) {
		userRepository.delete(entities);
	}

	public void deleteAll() {
		userRepository.deleteAll();
	}

}
