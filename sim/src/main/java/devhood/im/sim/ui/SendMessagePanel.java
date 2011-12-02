package devhood.im.sim.ui;

import java.awt.BorderLayout;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * Panel to send messages.
 * 
 * @author flo
 *
 */
public class SendMessagePanel extends JPanel {

	
	public SendMessagePanel() {
		super();
		setLayout(new BorderLayout());

		Label l = new Label("Just type...");
		this.add(l, BorderLayout.NORTH);

		JTabbedPane tabbedPane = new JTabbedPane();
		
			JTextArea msg = new JTextArea(5, 50);
			msg.setWrapStyleWord(true);
			msg.setLineWrap(true);
			JScrollPane textScrollPane = new JScrollPane(msg);

			tabbedPane.addTab("Type..", textScrollPane);
		
		// Lay out the buttons from left to right.
		JPanel buttonPane = new JPanel();

		buttonPane.add(new JButton("Send"));
		buttonPane.add(new JButton("Cancel"));

		this.add(tabbedPane, BorderLayout.CENTER);
		this.add(buttonPane, BorderLayout.PAGE_END);
	}

}
