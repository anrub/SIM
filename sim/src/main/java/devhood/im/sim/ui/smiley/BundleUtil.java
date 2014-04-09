package devhood.im.sim.ui.smiley;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BundleUtil {

	public static Path createPath(URI startDirectory) throws IOException {
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

}