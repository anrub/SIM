package devhood.im.sim.ui.smiley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

@Named("bundleSmileyFactory")
public class BundleSmileyFactory implements SmileyFactory {

	@Inject
	private List<SmileyFactory> factories;

	@Override
	public String applySmiles(String c) {
		String c1 = c;

		for (SmileyFactory fac : factories) {
			c1 = applySmiley(c, fac);
			if (!c.equals(c1)) {
				break;
			}
		}
		return c1;
	}

	private String applySmiley(String c, SmileyFactory fac) {
		String c1 = fac.applySmiles(c);
		return c1;
	}

	@Override
	public Map<String[], String> getSmileys() {
		Map<String[], String> smileys = new HashMap<String[], String>();
		for (SmileyFactory fac : factories) {
			smileys.putAll(fac.getSmileys());
		}

		return smileys;
	}

}
