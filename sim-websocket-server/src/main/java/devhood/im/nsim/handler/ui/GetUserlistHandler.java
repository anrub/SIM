package devhood.im.nsim.handler.ui;

import java.util.ArrayList;
import java.util.List;

import org.atmosphere.cpr.Broadcaster;

import devhood.im.nsim.model.GetUserlistResponse;
import devhood.im.nsim.model.Message;
import devhood.im.nsim.model.Room;
import devhood.im.nsim.model.User;
import devhood.im.nsim.util.JacksonEncoder;
import devhood.im.sim.service.interfaces.UserService;

public class GetUserlistHandler implements IEventHandler {
	private Broadcaster broadcaster;

	private UserService userService;

	public GetUserlistHandler(Broadcaster broadcaster, UserService userService) {
		this.broadcaster = broadcaster;
		this.userService = userService;
	}

	@Override
	public void handle(Message m) {
		Iterable<devhood.im.sim.model.User> users = userService.getUsers();
		List<User> userList = new ArrayList<User>();
		for (devhood.im.sim.model.User user : users) {
			User u = new User();
			u.setAddress(user.getAddress());
			u.setLastaccess(user.getLastaccess());
			u.setName(user.getName());
			u.setPort(user.getPort());
			u.setStatusType(user.getStatusType());
			List<Room> rooms = new ArrayList<Room>();
			for (devhood.im.sim.model.Room r : user.getRooms()) {
				Room room = new Room();
				room.setName(r.getName());
				rooms.add(room);
			}
			u.setRooms(rooms);
			userList.add(u);

		}
		GetUserlistResponse response = new GetUserlistResponse();
		response.setUsers(userList);

		JacksonEncoder enc = new JacksonEncoder();
		String encoded = enc.encode(response);
		broadcaster.broadcast(encoded);
	}
}
