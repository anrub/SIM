package devhood.im.sim.ui.smiley;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;
import javax.xml.bind.JAXBContext;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.ui.smiley.module.BundleSmileyPack;
import devhood.im.sim.ui.smiley.module.Mapping;
import devhood.im.sim.ui.smiley.module.SmileyPack;

@Named("bundleSmileyFactory")
public class BundleSmileyFactory extends BundleFactory implements SmileyFactory {
	/**
	 * Root der internen smilies.
	 */
	private static final String SMILIES = "/smilies";

	/**
	 * Pattern der Config-Files.
	 */
	private static final String SIM_XML = ".*.sim.xml";

	/**
	 * Command wendet Smilies auf Tokens an.
	 */
	private ApplySmiley applySmileyCmd = new ApplySmiley();

	/**
	 * Factories aus dem Factory-Element der Configfiles.
	 */
	private List<SmileyFactory> factories = new ArrayList<SmileyFactory>();

	private BundleSmileyPack bundleSmileyPack = new BundleSmileyPack();

	private static Logger LOG = Logger.getLogger(BundleSmileyFactory.class
			.toString());

	public BundleSmileyFactory() throws URISyntaxException, IOException {
		this(SMILIES);
	}

	/**
	 * Konstruktur.
	 * 
	 * @param smilieSource
	 *            Root, in dem nach Configfiles gesucht wird.
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public BundleSmileyFactory(String smilieSource) throws URISyntaxException,
			IOException {
		URI uri = getClass().getResource(smilieSource).toURI();
		scanTree(uri, SIM_XML);

		try {
			if (System.getProperty(SimConfiguration.SMILIE_DIR_KEY) != null) {
				URI startDirectory = getClass().getResource(
						System.getProperty(SimConfiguration.SMILIE_DIR_KEY))
						.toURI();
				scanTree(startDirectory, SIM_XML);
			}
		} catch (Exception e) {
			LOG.warning("Konnte Smilies aus Ordner " + smilieSource
					+ " nicht hinzufuegen.");
		}
	}

	@Override
	public String applySmiles(String c) {
		boolean applied = false;
		String c2 = c;
		for (SmileyFactory f : factories) {
			c2 = f.applySmiles(c);
			if (!c.equals(c2)) {
				applied = true;
				break;
			}
		}
		if (applied == false) {
			c2 = applySmileyCmd.applySmiles(c, bundleSmileyPack);
		}

		return c2;
	}

	@Override
	public BundleSmileyPack getSmileys() {
		return bundleSmileyPack;
	}

	public void evaluateFile(Path file) throws Exception {
		LOG.info("Lade File: " + file.toString());
		JAXBContext jaxbContext = JAXBContext
				.newInstance("devhood.im.sim.ui.smiley.module");
		file = createPath(file.toUri());
		Object o = jaxbContext.createUnmarshaller().unmarshal(file.toFile());
		final SmileyPack pack = (SmileyPack) o;

		if (pack.getAutoScan() != null && pack.getAutoScan().length() > 0) {
			autoScan(pack);
		}
		if (pack.getFactory() != null && pack.getFactory().length() > 0) {
			addFactories(pack);
		}
		bundleSmileyPack.add(pack);
	}

	/**
	 * Fuegt Smilies der Klasse {@link SmileyPack#getFactory()} hinzu. Dafuer
	 * wird eine Neue Instanz der Klasse erzeugt.
	 * 
	 * @param pack
	 *            SmileyPack.
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected void addFactories(final SmileyPack pack)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		Class c = Class.forName(pack.getFactory());
		SmileyFactory factory = (SmileyFactory) c.newInstance();
		factories.add(factory);

		for (SmileyFactory fac : factories) {
			for (Mapping m : fac.getSmileys().getMappings().getMapping()) {
				pack.addMapping(m);
			}
		}
	}

	/**
	 * Scannt {@link SmileyPack#getAutoScan()} nach allen Dateien und fuegt sie
	 * ein, nach dem Muster :Dateiname: z.B 01.gif -> :01.gif: erzeugt diesen
	 * Smilie.
	 * 
	 * @param pack
	 *            Pack
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	protected void autoScan(final SmileyPack pack) throws URISyntaxException,
			IOException {
		URI uri = getClass().getResource(pack.getAutoScan()).toURI();
		Path p = this.createPath(uri);

		Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				createAndAddMapping(pack, file);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * Erzeugt ein Mapping Smiley-Shortcut -> Pfad zum Icon und fuegt iehn ein.
	 * 
	 * @param pack
	 *            SmileyPack
	 * @param file
	 *            Path zum Icon
	 * @return Mapping.
	 */
	protected void createAndAddMapping(final SmileyPack pack, Path file) {
		Mapping m = new Mapping();
		m.setShortcut(new String[] { ":" + file.getFileName() + ":" });
		m.setIcon(pack.getAutoScan() + file.getFileName().toString());

		pack.getMappings().getMapping().add(m);
	}
}
