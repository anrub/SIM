package devhood.im.nsim.handler.ui;

import devhood.im.nsim.model.Message;

/**
 * EventHandler, der Events der UI (in Form von {@link Message}s) erhaelt und
 * verarbeitet.
 * 
 * @author flo
 * 
 */
public interface IUiEventHandler {
	/**
	 * Behandle Message von UI.
	 * 
	 * @param m
	 *            Message.
	 */
	void handle(Message m);
}
