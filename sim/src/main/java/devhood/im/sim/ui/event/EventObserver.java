package devhood.im.sim.ui.event;

/**
 * EventObserver - wird bei Events in der Applikation von
 * {@link EventDispatcher} benachrichtigt.
 * 
 * @author flo
 * 
 */
public interface EventObserver {

	/**
	 * Wird vom EventDispatcher aufgerufen.
	 * 
	 * @param event
	 *            Event.
	 * @param o
	 *            payload.
	 */
	public void eventReceived(Events event, Object o);
}
