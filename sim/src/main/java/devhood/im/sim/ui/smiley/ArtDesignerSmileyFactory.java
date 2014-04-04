package devhood.im.sim.ui.smiley;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Named;

/**
 * Nutzt die Smileys von Artdesigner - http://artdesigner.lv/me
 * 
 * @see http://artdesigner.lv/me
 * @author flo
 */
@Named("artdesignerSmileyFactory")
public class ArtDesignerSmileyFactory implements SmileyFactory {

	private static Logger log = Logger.getLogger(ArtDesignerSmileyFactory.class
			.toString());

	/* Hier sind zu jedem Smiley-Kürzel die Grafik-Pfade hinterlegt. */
	private Map<String[], String> smileys = new HashMap<String[], String>();

	/**
	 * Private Smileys, durch Trick einzufuegen.
	 */
	private Map<String[], String> privateSmileys = new HashMap<String[], String>();

	private String imagePath = "/images/artdesigner_smileys/";

	private ApplySmiley applySmileyCmd = new ApplySmiley();

	@Override
	public String applySmiles(String c) {
		String applied = applySmileyCmd.applySmiles(c, smileys);
		if (c.equals(applied)) {
			applied = applySmileyCmd.applySmiles(applied, privateSmileys);
		}
		return applied;
	}

	public Map<String[], String> getSmileys() {
		return smileys;
	}

	/**
	 * Die Initialisierung führt das Pairing der Kurzschreibweisen zu den
	 * Bildnamen durch.
	 */
	public void init() {
		try {
			File f = new File(getClass().getResource(imagePath + "32/").toURI());

			for (String smileyName : f.list()) {
				smileys.put(new String[] { ":" + smileyName + ":" }, imagePath
						+ "32/" + smileyName);

				privateSmileys.put(new String[] { ":" + smileyName + ":!" },
						imagePath + "64/" + smileyName);

				privateSmileys.put(new String[] { ":" + smileyName + ":!!" },
						imagePath + "128/" + smileyName);

			}
		} catch (URISyntaxException e) {
			log.warning("Konnte Smiley nicht hinzufuegen e.getMessage:"
					+ e.getMessage());
			e.printStackTrace();
		}
	}
}
