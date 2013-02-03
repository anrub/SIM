package devhood.im.sim.messages.model;

/**
 * Nachricht wird versendet, wenn ein User den Raum verlaesst
 *
 * @author flo
 *
 */
public class RoomQuitMessage extends RoomMessage {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
