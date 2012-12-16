package devhood.im.sim.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import devhood.im.sim.model.Room;
import devhood.im.sim.repository.RoomRepository;
import devhood.im.sim.service.interfaces.UserService;

/**
 * Dieser {@link UserService} aktualisiert sich selbst und den uebergebenen User
 * regelmaessg.
 *
 * @author flo
 *
 */
@Named
@Transactional
public class DeleteOrphanRooms {

	@Inject
	private RoomRepository roomRepository;

	/**
	 * Loescht leere Raeume.
	 */
	@Scheduled(cron = "0 * * * * *")
	public void deleteOrphans() {
		Iterable<Room> rooms = roomRepository.findAll();

		for (Room r : rooms) {
			if (r.getUsers() == null || r.getUsers().size() == 0) {
				roomRepository.delete(r);
			}
		}
	}

}
