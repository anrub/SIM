package devhood.im.sim.ui.smiley;

import java.util.Map;

public class ApplySmiley {
	
	/**
	 * Tauscht die Smiley-Kürzel gegen die Smiley-Grafiken aus.
	 * 
	 * @param String
	 *            text Der Text in dem die Smiley's ausgetausch werden sollen.
	 */
	public String applySmiles(String text, Map<String[], String> smileys) {

		for (String[] keys : smileys.keySet()) {
			for (String key : keys) {
				if (text.equals(key)) {
					return "<img src=\""
							+ getClass().getResource(
									smileys.get(keys)).toString()
							+ " \" />";
				}
			}
		}

		return text;
	}
}
