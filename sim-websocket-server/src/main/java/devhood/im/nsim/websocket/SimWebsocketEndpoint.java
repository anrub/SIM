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

import devhood.im.nsim.handler.sim.ISimEventHandler;
import devhood.im.nsim.handler.sim.SimEventHandler;
import devhood.im.nsim.handler.sim.SimpleBroadcaster;
import devhood.im.nsim.handler.ui.IUiEventHandler;
import devhood.im.nsim.handler.ui.UiEventHandler;
import devhood.im.nsim.model.ErrorMessage;
import devhood.im.nsim.model.Message;
import devhood.im.nsim.util.JacksonDecoder;
import devhood.im.sim.SimMain;
import devhood.im.sim.ui.event.EventDispatcher;
import devhood.im.sim.ui.event.EventObserver;
import devhood.im.sim.ui.event.Events;

/**
 * Websocket Endpoint. <br />
 * <br />
 * Empfaengt Messages der UI via {@link SimWebsocketEndpoint#onMessage(String)}
 * sowie Events von SIM core durch
 * {@link SimWebsocketEndpoint#eventReceived(Events, Object)}. <br />
 * Delegiert die Verarbeitung der SIM core {@link Events} an
 * {@link ISimEventHandler} bzw. {@link IUiEventHandler} zur Verarbeitung der
 * {@link Message}s der UI.
 * 
 * @see <a href="https://github.com/anrub/SIM/wiki/WebSocket-API">WebSocket-API
 *      Doku im Wiki</a>
 * @author flo
 * 
 */
@ManagedService(path = "/chat", atmosphereConfig = MAX_INACTIVE + "=120000")
public class SimWebsocketEndpoint implements EventObserver {

	private final Logger logger = Logger.getLogger(getClass());

	/**
	 * Broadcaster - Push Verbindung zum Client (UI).
	 */
	private Broadcaster broadcaster;

	/**
	 * Behandelt Events von SIM core
	 */
	private ISimEventHandler simEventHandler;

	/**
	 * Behandelt Events der UI.
	 */
	private IUiEventHandler uiEventHandler;

	/**
	 * SIM core {@link ApplicationContext} zum Bezug von SIM-core-Services.
	 */
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
	public void onMessage(String message) throws IOException {
		JacksonDecoder dec = new JacksonDecoder();
		Message m = null;
		try {
			m = dec.decode(message);
		} catch (Exception e) {
			handleException(e);
		}
		if (uiEventHandler == null) {
			uiEventHandler = new UiEventHandler(broadcaster, applicationContext);
		}
		uiEventHandler.handle(m);
	}

	private void handleException(Exception e) {
		ErrorMessage msg = new ErrorMessage(e);
		new SimpleBroadcaster(broadcaster).broadcast(msg);

		e.printStackTrace();
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