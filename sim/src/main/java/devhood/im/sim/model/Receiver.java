package devhood.im.sim.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Receiver {
	private String name;

	/**
	 * Ist der Empfaenger ein Raum.
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
