package devhood.im.nsim.handler.ui;

import java.util.ArrayList;
import java.util.List;

import org.atmosphere.cpr.Broadcaster;

import devhood.im.nsim.model.GetRoomlistResponse;
import devhood.im.nsim.model.Message;
import devhood.im.nsim.util.SimpleBroadcaster;
import devhood.im.sim.model.Room;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.UserService;

public class GetRoomlistHandler implements IUiEventHandler {
	private Broadcaster broadcaster;

	private UserService userService;

	public GetRoomlistHandler(Broadcaster broadcaster, UserService userService) {
		this.broadcaster = broadcaster;
		this.userService = userService;
	}

	@Override
	public void handle(Message m) {
		List<devhood.im.nsim.model.Room> roomsList = new ArrayList<devhood.im.nsim.model.Room>();

		List<Room> rooms = userService.getRooms();

		GetRoomlistResponse response = new GetRoomlistResponse();
		for (Room r : rooms) {
			devhood.im.nsim.model.Room room = new devhood.im.nsim.model.Room();
			room.setName(r.getName());
			for (User u : r.getUsers()) {
				devhood.im.nsim.model.User user = new devhood.im.nsim.model.User();
				user.setName(u.getName());
				room.getUsers().add(user);
			}
			roomsList.add(room);
		}
		response.setRooms(roomsList);

		new SimpleBroadcaster(broadcaster).broadcast(response);
	}
}
