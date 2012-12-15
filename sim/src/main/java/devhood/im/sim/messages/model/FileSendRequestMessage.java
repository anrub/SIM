package devhood.im.sim.messages.model;

import java.io.Serializable;


/**
 * Eine Anfrage zum Dateitransfer. Das Protokoll des Filetransfers sieht so aus:
 *
 * <table border="1">
 * <tr>
 * <td>Sender</td>
 * <td>Richtung</td>
 * <td>Empfaenger</td>
 * </tr>
 * <tr>
 * <td><i>FileSendRequestMessage</i></td>
 * <td>-&gt;</td>
 * <td>Empfaenger entscheidetd ob Dateitransfer angenommen wird oder nicht</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>&lt;-</td>
 * <td><i>{@link FileSendAcceptMessage}</i> Empfaenger oeffnet Port zum Empfang des Streams, und teilt ihn dem Sender
 * mit</td>
 * </tr>
 * <tr>
 * <td>Sender streamt auf dem in <i>{@link FileSendAcceptMessage}</i>
 * spezifizierten Port.
 * <td>-&gt;</td>
 * <td>Empfaenger lauscht auf diesem Port, nimmt den Stream entgegen und
 * speichert ihn.</td>
 * </tr>
 * <tr>
 * <td colspan="3">oder</td>
 * </tr>
 * <tr>
 * <td>Senden wird abgebrochen</td>
 * <td>&lt;-</td>
 * <td><i>{@link FileSendRejectMessage}</i> Empfaenger lehnt/bricht ab.</td>
 * </tr>
 * </table>
 *
 * @author flo
 *
 */
public class FileSendRequestMessage extends SingleMessage implements Serializable {
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
		return "FileSendRequestMessage: Sender: " + getSender() + ", Receiver: " + getReceiver()
				+ ", Text: " + getText() + ", id:" + getId() + ", Filename:"
				+ getFilename() + ", Filetype: " + getFileType() + ", Size: "
				+ getSize();

	}
}
