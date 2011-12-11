package devhood.im.sim.model;

import java.io.Serializable;

/**
 * Eine Anfrage zum Dateitransfer.
 * 
 * @author flo
 * 
 */
public class FileSendRequestMessage extends Message implements Serializable {
	/**
	 * Dateityp.
	 */
	private String fileType;

	/**
	 * Dateigroesse.
	 */
	private long size;
	/**
	 * Dateiname.
	 */
	private String filename;
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

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public String toString() {
		return "FileSendRequestMessage: MessageType:" + getMessageType()
				+ ", Sender: " + getSender() + ", Receiver: " + getReceiver()
				+ ", Text: " + getText() + ", id:" + getId() + ", Filename:"
				+ getFilename() + ", Filetype: " + getFileType() + ", Size: "
				+ getSize();

	}
}
