package devhood.im.sim.model;

import java.io.Serializable;

/**
 * Annahme des Dateitransfers.
 * 
 * @author flo
 * 
 */
public class FileSendAcceptMessage extends Message implements Serializable {

	/**
	 * Id der Dateiuebertragung.
	 */
	private String id;

	/**
	 * Port, auf dem die Datei empfangen werden kann.
	 */
	private int portToUser;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPortToUser() {
		return portToUser;
	}

	public void setPortToUser(int portToUser) {
		this.portToUser = portToUser;
	}
}
