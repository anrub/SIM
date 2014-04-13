package devhood.im.nsim.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Eine Fehlermeldung fuer die UI.
 * 
 * @author flo
 * 
 */
public class ErrorMessage extends Message {

	private String title;
	private String text;

	public ErrorMessage() {

	}

	public ErrorMessage(Exception e) {
		extractException(e);
	}

	protected void extractException(Exception e) {
		title = e.getClass().toString();
		text = e.getMessage();
		StringUtils.abbreviate(text, 125);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
