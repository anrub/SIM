package devhood.im.sim.model;

/**
 * Eine Message.
 * 
 * @author flo
 * 
 */
public class Message {

	private String name;
	/**
	 * Text der Nachricht.
	 */
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
