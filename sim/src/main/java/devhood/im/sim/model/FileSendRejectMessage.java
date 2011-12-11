package devhood.im.sim.model;

import java.io.Serializable;

/**
 * Ablehnung der Transferanfrage.
 * 
 * @author flo
 * 
 */
public class FileSendRejectMessage extends Message implements Serializable {

	/**
	 * Eindeutige id.
	 */
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
