package devhood.im.sim.ui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.springframework.context.annotation.Scope;

import devhood.im.sim.messages.MessagingException;
import devhood.im.sim.messages.interfaces.FileMessageHandler;
import devhood.im.sim.messages.model.FileSendRequestMessage;

/**
 * Frame bei dem Empfang einer Datei.
 *
 * @author flo
 *
 */
@Named
@Scope("prototype")
public class ReceiveFileFrame extends JFrame {

	private JPanel panel = new JPanel();
	private JPanel buttons = new JPanel();
	private JPanel container = new JPanel();

	private String id;
	private FileSendRequestMessage fileSendRequestMessage;
	private long sizeToReceive;

	private Timer progressTimer = new Timer();
	private TimerTask updateProgressBarTask;

	@Inject
	private FileMessageHandler messageSender;

	@PostConstruct
	public void init() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				updateProgressBarTask.cancel();
				progressTimer.cancel();
			}
		});

	}

	public void showFrame() {
		setTitle(fileSendRequestMessage.getFilename() + " von "
				+ fileSendRequestMessage.getSender() + " empfangen...");

		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

		final JLabel tryLabel = new JLabel(fileSendRequestMessage.getSender()
				+ " möchte Ihnen folgende Datei schicken: "
				+ fileSendRequestMessage.getFilename() + ", Dateigröße: "
				+ fileSendRequestMessage.getSize());

		final JProgressBar bar = new JProgressBar();
		bar.setIndeterminate(true);
		bar.setValue(0);
		bar.setMaximum(100);

		final JCheckBox openFileCheckBox = new JCheckBox("Datei öffnen");
		final JButton ok = new JButton("Datei empfangen");
		final JButton close = new JButton("Schließen");
		close.setVisible(false);
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				closeFrame();
			}
		});

		final JButton reject = new JButton("Abbrechen");

		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();

				fc.setFileSelectionMode(JFileChooser.SAVE_DIALOG);

				File f = new File(fileSendRequestMessage.getFilename());
				fc.setSelectedFile(f);

				int returnVal = fc.showSaveDialog(panel);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					final File file = fc.getSelectedFile();
					id = fileSendRequestMessage.getId();

					safeAcceptFileMessage(file);
					ok.setVisible(false);

					sizeToReceive = fileSendRequestMessage.getSize();

					updateProgressBarTask = new TimerTask() {

						@Override
						public void run() {
							try {
								if (bar.isIndeterminate() && bar.getValue() > 0) {
									bar.setIndeterminate(false);
								}

								if (bar.getValue() < 100) {
									int perc = (int) ((messageSender
											.getProgress(id) * 100) / sizeToReceive);
									bar.setValue(perc);
									if (bar.getValue() != 0) {
										tryLabel.setText("Empfange "
												+ fileSendRequestMessage
														.getFilename()
												+ " von "
												+ fileSendRequestMessage
														.getSender());
									}
								} else {
									close.setVisible(true);
									reject.setVisible(false);
									if (openFileCheckBox.isSelected()) {
										Desktop.getDesktop()
												.open(new File(
														file.getAbsoluteFile()
																+ "/"
																+ fileSendRequestMessage
																		.getFilename()));
										closeFrame();
									}
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					progressTimer.schedule(updateProgressBarTask, 0, 500);

				}
			}

			private void safeAcceptFileMessage(final File file) {
				try {
					messageSender.acceptFileMessage(
							fileSendRequestMessage.getSender(),
							fileSendRequestMessage.getId(),
							file.getAbsolutePath());
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		reject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				safeSendRejectFileMessage();
				dispose();
			}

			private void safeSendRejectFileMessage() {
				try {
					messageSender.rejectFileMessage(
							fileSendRequestMessage.getId(),
							fileSendRequestMessage.getSender());
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		panel.add(tryLabel);

		buttons.add(ok);
		buttons.add(close);
		buttons.add(reject);
		buttons.add(openFileCheckBox);

		container.add(panel);
		container.add(bar);

		container.add(buttons);
		add(container);

		pack();
		setVisible(true);
	}

	/**
	 * Schliesst diesen Frame.
	 */
	public void closeFrame() {
		updateProgressBarTask.cancel();
		progressTimer.cancel();
		dispose();
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public FileSendRequestMessage getFileSendRequestMessage() {
		return fileSendRequestMessage;
	}

	public void setFileSendRequestMessage(
			FileSendRequestMessage fileSendRequestMessage) {
		this.fileSendRequestMessage = fileSendRequestMessage;
	}

	public FileMessageHandler getMessageSender() {
		return messageSender;
	}

	public void setMessageSender(FileMessageHandler messageSender) {
		this.messageSender = messageSender;
	}
}
