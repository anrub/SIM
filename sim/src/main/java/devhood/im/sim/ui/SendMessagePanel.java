package devhood.im.sim.ui;

import java.awt.BorderLayout;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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


		JTabbedPane tabbedPane = new JTabbedPane();
			JPanel textPanel = new JPanel(new BorderLayout());
			
			JTextArea timelineTextArea = new JTextArea(5, 50);
			timelineTextArea.setWrapStyleWord(true);
			timelineTextArea.setLineWrap(true);
			timelineTextArea.setEditable(false);
			JScrollPane textScrollPane = new JScrollPane(timelineTextArea);
			textPanel.add(textScrollPane, BorderLayout.CENTER);
			
			JTextArea messageTextArea = new JTextArea(2,50);
			messageTextArea.setWrapStyleWord(true);
			messageTextArea.setLineWrap(true);
			JScrollPane messageScrollPane = new JScrollPane(messageTextArea);
			
			textPanel.add(messageScrollPane, BorderLayout.SOUTH);
			
			tabbedPane.addTab("Type..", textPanel);
		
		// Lay out the buttons from left to right.
		JPanel buttonPane = new JPanel();

		buttonPane.add(new JButton("Send"));
		buttonPane.add(new JButton("Cancel"));

		this.add(tabbedPane, BorderLayout.CENTER);
		this.add(buttonPane, BorderLayout.PAGE_END);
	}

}
