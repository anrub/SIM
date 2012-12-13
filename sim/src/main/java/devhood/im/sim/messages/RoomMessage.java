package devhood.im.sim.messages;

/**
 * Nachricht an einen Raum.
 *
 * @author flo
 *
 */
public class RoomMessage extends Message {

	private long id;

	private String roomName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
}
