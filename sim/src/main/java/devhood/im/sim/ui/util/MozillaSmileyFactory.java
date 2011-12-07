package devhood.im.sim.ui.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Nutzt Smileys von Mozilla Thunderbird.
 * 
 * @author flo
 * 
 */
public class MozillaSmileyFactory implements SmileyFactory {

	public String applySmiles(String c) {

		String smile = c;
		String icon = "";
		if (c.equals(";)") || c.equals(";-)")) {
			icon = getClass().getResource(
					"/images/mozilla_smiles/mozilla_wink.png").toString();
		}
		if (c.equals(":)") || c.equals(":-)")) {
			icon = getClass().getResource(
					"/images/mozilla_smiles/mozilla_smile.png").toString();

		}
		if (c.equals(":D") || c.equals(":-D")) {
			icon = getClass().getResource(
					"/images/mozilla_smiles/mozilla_laughing.png").toString();

		}
		if (c.equals(":P") || c.equals(":-P")) {
			icon = getClass().getResource(
					"/images/mozilla_smiles/mozilla_tongueout.png").toString();

		}
		if (!StringUtils.isEmpty(icon)) {
			smile = "<img src=\"" + icon + " \"></img>";
		}

		return smile;
	}
}
