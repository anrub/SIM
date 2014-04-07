package devhood.im.sim.ui.smiley;

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import devhood.im.sim.ui.smiley.module.Mapping;
import devhood.im.sim.ui.smiley.module.SmileyPack;

/**
 * Nutzt die Smileys von Artdesigner - http://artdesigner.lv/me
 * 
 * @see http://artdesigner.lv/me
 * @author flo
 */
public class ArtDesignerSmileyFactory implements SmileyFactory {

	private static Logger log = Logger.getLogger(ArtDesignerSmileyFactory.class
			.toString());

	/* Hier sind zu jedem Smiley-Kürzel die Grafik-Pfade hinterlegt. */
	private SmileyPack smileys = new SmileyPack();

	/**
	 * Private Smileys, durch Trick einzufuegen.
	 */
	private SmileyPack privateSmileys = new SmileyPack();

	private String imagePath = "/smilies/artdesigner/images/";

	private ApplySmiley applySmileyCmd = new ApplySmiley();

	/**
	 * Die Initialisierung führt das Pairing der Kurzschreibweisen zu den
	 * Bildnamen durch.
	 */
	public ArtDesignerSmileyFactory() {
		try {
			File f = new File(getClass().getResource(imagePath + "32/").toURI());

			for (String smileyName : f.list()) {

				Mapping m = new Mapping(
						new String[] { "#" + smileyName + "#" }, imagePath
								+ "32/" + smileyName);
				smileys.addMapping(m);

				m = new Mapping(new String[] { "#" + smileyName + "#!" },
						imagePath + "64/" + smileyName);
				privateSmileys.addMapping(m);

				m = new Mapping(new String[] { "#" + smileyName + "#!!" },
						imagePath + "128/" + smileyName);
				privateSmileys.addMapping(m);

			}
		} catch (URISyntaxException e) {
			log.warning("Konnte Smiley nicht hinzufuegen e.getMessage:"
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public String applySmiles(String c) {
		String applied = applySmileyCmd.applySmiles(c, smileys);
		if (c.equals(applied)) {
			applied = applySmileyCmd.applySmiles(applied, privateSmileys);
		}
		return applied;
	}

	public SmileyPack getSmileys() {
		return smileys;
	}

}
