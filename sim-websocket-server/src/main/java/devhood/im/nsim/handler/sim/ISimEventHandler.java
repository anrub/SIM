package devhood.im.nsim.handler.sim;

import devhood.im.sim.event.Events;

/**
 * EventHandler, der Events von SIM core empfaengt und verarbeitet.
 * 
 * @author flo
 * 
 */
public interface ISimEventHandler {
	/**
	 * Behandle Event.
	 * 
	 * @param e
	 *            Event.
	 * @param o
	 *            Payload.
	 */
	public void handle(Events e, Object o);

}
