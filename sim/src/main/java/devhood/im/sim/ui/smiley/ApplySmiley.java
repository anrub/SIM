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
		
		String smileyCode = text.replace("&gt;", ">").replace("&lt;", "<")
				.replace("&amp;", "&").replace("&quot;", "\"");
		
		Mapping mapping = smileys.getMappings().getMapping(smileyCode);

		if (mapping != null) {
			return "<img src=\"" + getClass().getResource(mapping.getIcon())
					+ " \" />";
		}

		return text;
	}
}
