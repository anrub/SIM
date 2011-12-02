package devhood.im.sim;

import javax.swing.UIManager;

import devhood.im.sim.ui.MainFrame;

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

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		MainFrame mainFrame = new MainFrame();
		mainFrame.initMainFrame();
	}
}
