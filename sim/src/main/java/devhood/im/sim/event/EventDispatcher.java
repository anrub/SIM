package devhood.im.sim.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Event Dispatcher, verteilt die Events an alle registierten
 * {@link EventObserver}.
 * 
 * @author flo
 * 
 */
public class EventDispatcher {

	/**
	 * Liste der registrierten Subscriber.
	 */
	private static List<EventObserver> observer = new CopyOnWriteArrayList<EventObserver>();

	/**
	 * Feuert Event.
	 * 
	 * @param event
	 *            Event.
	 * @param o
	 *            Object.
	 */
	public static void fireEvent(Events event, Object o) {
		for (EventObserver e : observer) {
			e.eventReceived(event, o);
		}
	}

	/**
	 * Fuegt Observer ein.
	 * 
	 * @param eventObserver
	 *            Observer.
	 */
	public static void add(EventObserver eventObserver) {
		if (observer.contains(eventObserver)) {
			return;
		}

		observer.add(eventObserver);
	}
}
