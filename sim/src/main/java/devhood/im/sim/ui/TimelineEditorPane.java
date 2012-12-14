package devhood.im.sim.ui;

import java.awt.Font;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;

import devhood.im.sim.ui.util.UiUtil;

/**
 * TimelineEditorPane ist das JEditorPane, in dem der Inhalt und Verlauf einer
 * Konversation angezeigt wird.
 *
 * @author flo
 *
 */
public class TimelineEditorPane extends JEditorPane {
	/**
	 * Erzeugt das timeline pane incl inhalt.
	 *
	 * @param label
	 *            Label des Tabs.
	 * @param text
	 *            Textinhalt.
	 * @return
	 */
	public TimelineEditorPane(String label, String text) {

		super("text/html", "<html><head></head><body></body></html>");

		String oldText = getText();
		StringBuffer newMsg = new StringBuffer(oldText);

		if (oldText.contains("</body>")) {
			int index = oldText.indexOf("</body>");
			newMsg.insert(index, text);
		}

		setText(newMsg.toString());

		DefaultCaret caret = (DefaultCaret) getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		setEditable(false);
		setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

		addHyperlinkListener(new HyperlinkListener() {

			/**
			 * Bei Klick auf einen Link in der Timeline, wird der Browser
			 * geoeffnet.
			 *
			 * @param e
			 *            Event.
			 */
			@Override
			public void hyperlinkUpdate(final HyperlinkEvent e) {

				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					UiUtil.openUrlInBrowser(e.getURL().toString());
				}
			}
		});

	}
}
