package devhood.im.sim.model;

public class Receiver {
	private String name;

	/**
	 * Ist der Empfänger ein Raum.
	 */
	private boolean room;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRoom() {
		return room;
	}

	public void setRoom(boolean room) {
		this.room = room;
	}
}
