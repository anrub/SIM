package devhood.im.sim.ui.smiley;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;
import java.util.List;
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

	/**
	 * Smilies.
	 */
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
	public ArtDesignerSmileyFactory() throws Exception {
		try {
			URL url = getClass().getResource(imagePath + "32/");

			final List<String> names = new ArrayList<String>();

			Files.walkFileTree(BundleUtil.createPath(url.toURI()),
					new SimpleFileVisitor<Path>() {
						public java.nio.file.FileVisitResult visitFile(
								Path file,
								java.nio.file.attribute.BasicFileAttributes attrs)
								throws java.io.IOException {
								names.add(file.getFileName().toString());
							return FileVisitResult.CONTINUE;
						};
					});

			for (String smileyName : names) {

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

		smileys.setName("Artdesign Pack");
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
