package devhood.im.sim;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.UIManager;

import devhood.im.sim.model.Message;
import devhood.im.sim.ui.MainFrame;
import devhood.im.sim.ui.event.EventDispatcher;
import devhood.im.sim.ui.event.Events;

/**
 * Main Class.
 * 
 * @author flo
 * 
 */
public class SimMain {

	/**
	 * Main method, starts the program. <br />
	 * Possible commandline arguments:
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

		Timer t = new Timer();
		TimerTask task = new TimerTask() {
			volatile int cnt = 0;

			@Override
			public void run() {

				if (cnt < 5) {
					Message m = new Message();
					int userid = (int) Math.floor(Math.random() * 5);
					m.setSender("User " + userid);
					m.setReceiver(Sim.getUsername());

					m.setText("Dies ist eine Nachricht öäüß " + Math.random());
					EventDispatcher.fireEvent(Events.MESSAGE_RECEIVED, m);
					cnt++;
				}

			}
		};

		t.schedule(task, 1000, 5000);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		MainFrame mainFrame = new MainFrame();
		mainFrame.initMainFrame();

	}
}
