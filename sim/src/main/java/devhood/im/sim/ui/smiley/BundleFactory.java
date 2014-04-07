package devhood.im.sim.ui.smiley;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

abstract public class BundleFactory {

	/**
	 * Scannt ein Verzeichnis nach Dateien, die file Pattern entsprechen, und
	 * ruft evaluateFile auf.
	 * 
	 * @param startDirectory
	 *            Verzeichnis
	 * @param filePattern
	 *            File Pattern
	 * @throws IOException
	 *             im Fehlerfall.
	 */
	public void scanTree(URI startDirectory, final String filePattern)
			throws IOException {
		Files.walkFileTree(Paths.get(startDirectory),
				new SimpleFileVisitor<Path>() {
					private Pattern pattern = Pattern.compile(filePattern);

					@Override
					public FileVisitResult visitFile(Path file,
							BasicFileAttributes attrs) throws IOException {
						boolean matches = pattern.matcher(file.toString())
								.matches();
						if (matches) {
							try {
								evaluateFile(file);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						return FileVisitResult.CONTINUE;
					}

				});
	}

	abstract public void evaluateFile(Path file) throws Exception;
}
