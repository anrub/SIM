package devhood.im.nsim;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import devhood.im.sim.SimMain;

/**
 * Startet SIM Swing UI, sowie WebSocket Server Port 8080 und Test Web-UI.
 * 
 * -p <Port> -r <Resource Base Path>
 * 
 * @see SimMain
 * @author flo
 * 
 */
public class SimWebsocketMain {

	public static void main(String[] args) throws Exception {
		startSimCore(args);
		startWebSocketServer(args);

	}

	/**
	 * Startet Webserver zum Anbieten der WebSocket SST, sowie der Dummy-Chat
	 * JS/HTML Impl.
	 * 
	 * @param args
	 * @throws Exception
	 */
	private static void startWebSocketServer(String[] args) throws Exception {
		String port = SimMain.getParam(args, "-p");
		Server server = null;

		if (port != null) {
			Integer portInt = Integer.parseInt(port);
			server = new Server(portInt);
		} else {
			server = new Server(8080);
		}

		String resoureceBase = SimMain.getParam(args, "-r");
		if (resoureceBase == null) {
			resoureceBase = "src/main/resources";
		}

		ServletContextHandler context = new ServletContextHandler();

		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setWelcomeFiles(new String[] { "index.html" });
		resource_handler.setResourceBase(resoureceBase);

		ServletHolder h = new ServletHolder();
		h.setServlet(new org.atmosphere.cpr.AtmosphereServlet());
		context.addServlet(h, "/chat/*");

		h.setInitParameter("org.atmosphere.cpr.packages",
				"devhood.im.nsim.websocket");

		HandlerList list = new HandlerList();
		list.addHandler(resource_handler);
		list.addHandler(context);

		server.setHandler(list);

		server.start();
		server.join();

	}

	/**
	 * Startet SIM core.
	 * 
	 * @param args
	 * @throws Exception
	 */
	private static void startSimCore(String[] args) throws Exception {
		SimMain.main(args);
	}
}
