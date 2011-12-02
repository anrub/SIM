package devhood.im.sim.ui.event;

public interface EventObserver {

	
	/**
	 * Wird vom EventDispatcher aufgerufen.
	 * 
	 * @param o Event.
	 */
	public void eventReceived(Events event, Object o);
}
