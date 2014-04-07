package devhood.im.sim.ui.smiley;

import devhood.im.sim.ui.smiley.module.Mapping;
import devhood.im.sim.ui.smiley.module.SmileyPack;

public class ApplySmiley {

	/**
	 * Tauscht die Smiley-Kürzel gegen die Smiley-Grafiken aus.
	 * 
	 * @param String
	 *            text Der Text in dem die Smiley's ausgetausch werden sollen.
	 */
	public String applySmiles(String text, SmileyPack smileys) {
		Mapping mapping = smileys.getMappings().getMapping(text);

		if (mapping != null) {
			return "<img src=\"" + getClass().getResource(mapping.getIcon())
					+ " \" />";
		}

		return text;
	}
}
