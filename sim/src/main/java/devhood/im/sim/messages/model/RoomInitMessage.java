package devhood.im.sim.messages.model;

/**
 * Nachricht gibt bekannt, das ein Raum eingerichtet wurde.
 *
 * @author flo
 *
 */
public class RoomInitMessage extends RoomMessage {
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
