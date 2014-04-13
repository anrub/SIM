package devhood.im.nsim.model;

import java.util.List;

public class GetRoomlistResponse extends Message {
	private List<Room> rooms;

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}
}
