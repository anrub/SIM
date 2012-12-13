package devhood.im.sim.service.interfaces;

import devhood.im.sim.messages.Message;

/**
 * Verarbeitet die eingehende Nachricht.
 * 
 * @author flo
 * 
 */
public interface MessageProcessor {
	/**
	 * Verarbeitet die eingehende Nachricht und gibt Sie modifiziert zurueck.
	 * 
	 * @return Modifizierte Message.
	 */
	public Message processMessage(Message message);
}
