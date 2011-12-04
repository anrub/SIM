package devhood.im.sim;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;

import devhood.im.sim.model.Message;
import devhood.im.sim.service.ServiceLocator;
import devhood.im.sim.service.interfaces.MessageService;
import devhood.im.sim.ui.MainFrame;

/**
 * Main Class.
 * 
 * Possible commandline arguments:
 * 
 * -n nickname -f databasefile
 * 
 * 
 * @author flo
 * 
 */
public class SimMain {

	final private static MessageService messageService = ServiceLocator
			.getInstance().getMessageService();

	private static Logger log = Logger.getLogger(SimMain.class.toString());

	/**
	 * Main method, starts the program. <br />
	 * 
	 * @param args
	 *            arguments
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if ("-n".equals(args[i]) && i + 1 < args.length) {
					Sim.username = args[i + 1];
				}
				if ("-f".equals(args[i]) && i + 1 < args.length) {
					Sim.dbPath = args[i + 1];
				}
			}

		}

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			log.log(Level.WARNING,
					"Konnte System default Look n Feel nicht laden, benutzte Java default.",
					e);
		}
		MainFrame mainFrame = new MainFrame();
		mainFrame.initMainFrame();

		SimMain.startSimulation();
	}

	/**
	 * startet messaging simulaton, erzeugt 10 nachrichten.
	 */
	public static void startSimulation() {
		final Timer t = new Timer();
		TimerTask task = new TimerTask() {
			volatile int cnt = 0;

			@Override
			public void run() {

				if (cnt < 10) {
					Message m = new Message();
					int userid = (int) Math.floor(Math.random() * 10);
					m.setSender("User " + userid);
					m.setReceiver(Sim.getUsername());

					m.setText("Dies ist eine Nachricht öäüß " + Math.random());

					messageService.receiveMessage(m);

					cnt++;
				} else {
					t.cancel();
				}

			}
		};

		t.schedule(task, 1000, 5000);

	}
}
