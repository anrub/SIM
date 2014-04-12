package devhood.im.nsim.websocket;

import static org.atmosphere.cpr.ApplicationConfig.MAX_INACTIVE;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.Broadcaster;
import org.springframework.context.ApplicationContext;

import devhood.im.nsim.handler.sim.SimEventHandler;
import devhood.im.nsim.handler.ui.UiEventHandler;
import devhood.im.nsim.model.Message;
import devhood.im.nsim.util.JacksonDecoder;
import devhood.im.sim.SimMain;
import devhood.im.sim.ui.event.EventDispatcher;
import devhood.im.sim.ui.event.EventObserver;
import devhood.im.sim.ui.event.Events;

/**
 * Websocket Endpoint.
 * 
 * @author flo
 * 
 */
@ManagedService(path = "/chat", atmosphereConfig = MAX_INACTIVE + "=120000")
public class SimWebsocketEndpoint implements EventObserver {

	private final Logger logger = Logger.getLogger(getClass());

	private Broadcaster broadcaster;

	private SimEventHandler simEventHandler;

	private UiEventHandler uiEventHandler;

	private ApplicationContext applicationContext;

	public SimWebsocketEndpoint() {
		EventDispatcher.add(this);

		applicationContext = SimMain.getApplicationContext();
	}

	/**
	 * Wird aufgerufen, wenn Message vom Client empfangen wird.
	 * 
	 * @param message
	 * @return
	 * @throws IOException
	 */
	@org.atmosphere.config.service.Message
	public String onMessage(String message) throws IOException {
		JacksonDecoder dec = new JacksonDecoder();
		Message m;
		try {
			m = dec.decode(message);
		} catch (Exception e) {
			return "\"Error\"";
		}
		if (uiEventHandler == null) {
			uiEventHandler = new UiEventHandler(broadcaster, applicationContext);
		}
		uiEventHandler.handle(m);

		return message;
	}

	/**
	 * Empfaengt Events durch SIM core {@link EventDispatcher} und Broadcastet
	 * sie an einen verbundenen WebSocket Client.
	 */
	@Override
	public void eventReceived(Events event, Object o) {
		if (this.simEventHandler == null && broadcaster != null) {
			this.simEventHandler = new SimEventHandler(broadcaster);
		}
		simEventHandler.handle(event, o);
	}

	@Ready
	public void onReady(final AtmosphereResource r) {
		broadcaster = r.getBroadcaster();
		logger.info(String.format("Browser {} connected.", r.uuid()));

	}

	@Disconnect
	public void onDisconnect(AtmosphereResourceEvent event) {
		if (event.isCancelled()) {
			logger.info(String.format("Browser {} unexpectedly disconnected",
					event.getResource().uuid()));
		}
	}

}