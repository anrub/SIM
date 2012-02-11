package devhood.im.sim.ui.util;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.config.SimConfiguration;

/**
 * Einfacher Cache, der aktiviert oder deaktiviert sein kann.
 * 
 * @author flo
 * 
 */
@Named("simpleTabCache")
public class SimpleTabCache {

	/**
	 * Cache des Timelineinhalts, tabName->timeline Inhalte.
	 */
	private Map<String, String> nameTimelineCache = new HashMap<String, String>();

	@Inject
	private SimConfiguration simConfiguration;

	/**
	 * Fuegt in den Cache ein, falls er aktiviert ist.
	 * 
	 * @param label
	 *            label
	 * @return Inhalt oder null.
	 */
	public String get(String label) {
		String value = null;
		if (isEnabled()) {
			value = nameTimelineCache.get(label);
		}
		return value;
	}

	/**
	 * Fuegt in den Cache ein, falls er aktiviert ist.
	 * 
	 * @param label
	 *            Label
	 * @param message
	 *            Message
	 */
	public void put(String label, String message) {
		if (isEnabled()) {
			nameTimelineCache.put(label, message);
		}
	}

	/**
	 * Gibt zurueck ob dieser Cache aktiviert ist oder nicht.
	 * 
	 * @return true/false
	 */
	public boolean isEnabled() {
		return simConfiguration.isTabCacheEnabled();
	}

}
