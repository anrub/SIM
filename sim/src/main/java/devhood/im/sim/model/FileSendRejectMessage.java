package devhood.im.sim.model;

import java.io.Serializable;

/**
 * Lehnt eine Anfrage zur Uebertraung ab, oder bricht eine laufende Uebertragung
 * ab.
 * 
 * @author flo
 * 
 */
public class FileSendRejectMessage extends Message implements Serializable {

	/**
	 * Eindeutige id des Dateitransfers.
	 */
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
