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

		Timer t = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Message m = new Message();
				int userid = (int) Math.floor(Math.random() * 5);
				m.setName("User " + userid);
				
				m.setText("Dies ist eine Nachricht öäüß " + Math.random());
				EventDispatcher.fireEvent(Events.MESSAGE_RECEIVED, m);
				
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
