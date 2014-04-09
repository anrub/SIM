package devhood.im.sim.ui.smiley;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
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
		Path path = createPath(startDirectory);
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			private Pattern pattern = Pattern.compile(filePattern);

			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				boolean matches = pattern.matcher(file.toString()).matches();
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

	public Path createPath(URI startDirectory) throws IOException {
		Path path = null;
		if (startDirectory.toString().contains("jar:")) {
			final String[] array = startDirectory.toString().split("!");
			FileSystem fs = null;
			try {
				fs = FileSystems.getFileSystem(URI.create(array[0]));
			} catch (FileSystemNotFoundException fse) {
				final Map<String, String> env = new HashMap<>();
				fs = FileSystems.newFileSystem(URI.create(array[0]), env);
			}

			path = fs.getPath(array[1]);
		} else {
			path = Paths.get(startDirectory);
		}

		return path;
	}

	abstract public void evaluateFile(Path file) throws Exception;
}
