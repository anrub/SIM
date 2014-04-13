package devhood.im.nsim.handler.sim;

import java.util.HashMap;
import java.util.Map;

import org.atmosphere.cpr.Broadcaster;

import devhood.im.sim.event.Events;

/**
 * Transformiert SIM {@link Events} zu Objects,und sendet sie ueber die API nach
 * aussen.
 * 
 * @author flo
 * 
 */
public class SimEventHandler implements ISimEventHandler {

	private Broadcaster broadcaster;

	private Map<Events, ISimEventHandler> map = new HashMap<Events, ISimEventHandler>();

	public SimEventHandler(Broadcaster broadcaster) {
		map.put(Events.MESSAGE_RECEIVED, new NewMessageHandler(broadcaster));
		map.put(Events.USER_ONLINE_NOTICE, new UserOnlineHandler(broadcaster));
		map.put(Events.USER_OFFLINE_NOTICE, new UserOfflineHandler(broadcaster));
		map.put(Events.UNREAD_MESSAGES, new UnreadMessagesHandler(broadcaster));
		map.put(Events.MESSAGE_FILE_REQUEST_RECEIVED,
				new FileRequestMessageHandler(broadcaster));
		map.put(Events.SEND_MESSAGE,
				new SendMessageHandler(broadcaster));
		// weitere Handler, je SIM core Event einfuegen.

		this.broadcaster = broadcaster;
	}

	public void handle(Events e, Object payload) {
		System.out.println("Event von SIM core received: " + e + ", Inhalt: "
				+ payload);
		ISimEventHandler h = map.get(e);
		if (h != null) {
			h.handle(e, payload);
		} else {
			new UmbrellaHandler(broadcaster).handle(e, payload);
		}
	}
}
