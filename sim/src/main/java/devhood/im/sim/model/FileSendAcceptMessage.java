package devhood.im.sim.model;

import java.io.Serializable;

/**
 * Annahme des Dateitransfers.
 * 
 * @author flo
 * 
 */
public class FileSendAcceptMessage extends Message implements Serializable {

	private String id;

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
