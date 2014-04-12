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
 * 
 * @author flo
 * 
 */
public class SimWebsocketMain {

	public static void main(String[] args) throws Exception {
		startSimCore();
		startWebSocketServer();

	}

	private static void startWebSocketServer() throws Exception {
		Server server = new Server(8080);
		ServletContextHandler context = new ServletContextHandler();

		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setWelcomeFiles(new String[] { "index.html" });
		resource_handler.setResourceBase("src/main/resources");

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

	private static void startSimCore() throws Exception {
		SimMain.main(new String[] { "SimMain", "-n", "Testuser" });
	}
}
