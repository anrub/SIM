package devhood.im.sim;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import devhood.im.sim.ui.presenter.MainPresenter;
import devhood.im.sim.ui.view.ErrorView;
import devhood.im.sim.ui.view.MainView;

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

	private static Logger log = Logger.getLogger(SimMain.class.toString());

	private static ApplicationContext applicationContext;

	/**
	 * Main method, starts the program. <br />
	 * 
	 * @param args
	 *            arguments
	 */
	public static void main(String[] args) {
		String username = System.getProperty("user.name");
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if ("-n".equals(args[i]) && i + 1 < args.length) {
					username = args[i + 1];
				}
			}

		}

		System.setProperty("sim.username", username);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			SimMain main = new SimMain();
			SimMain.applicationContext = main.startup();
		} catch (Exception e) {
			log.log(Level.WARNING, "Applikation konnte nicht gestartet werden",
					e);

			ErrorView error = new ErrorView(
					"SIM konnte nicht gestartet werden! Exception: ", e);
			error.setTitle("SIM S Instant Messenger");
			error.setAlwaysOnTop(true);
			error.pack();
			error.setVisible(true);

			System.exit(1);
		}

	}

	/**
	 * Startet den {@link ApplicationContext} und zeigt das {@link MainView} an.
	 */
	public ApplicationContext startup() {
		GenericXmlApplicationContext context = new GenericXmlApplicationContext();

		context.setValidating(false);
		context.getEnvironment().setActiveProfiles("production");
		context.load("classpath:/devhood/im/sim/applicationContext.xml");
		context.refresh();
		MainPresenter presenter = context.getBean(MainPresenter.class);
		presenter.initMain();

		return context;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
