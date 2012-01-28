package devhood.im.sim.ui.util;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.SwingWorker;

public class UiUtil {

	/**
	 * Erzeugt einen SwingWorker, der die übergebene URL im Browser öffnet.
	 * 
	 * @param url
	 *            URL
	 * @return SwingWorker<Void,Void>
	 */
	public static void openUrlInBrowser(final String url) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {

				try {
					Desktop.getDesktop().browse(URI.create(url));
				} catch (Exception uri) {
					// TODO Fehlerhandling, wen URI / Browser nicht
					// geoeffnet werden konnte. Warnung anzeigen.
					uri.printStackTrace();
				}

				return null;
			}

			@Override
			protected void done() {
				// TODO Auto-generated method stub
				super.done();
			}
		};

		worker.execute();
	}

	/**
	 * Erzeugt ien ImageIcon aus dem uebergebenen path. path ist im classpath.
	 * 
	 * @param path
	 *            path aus classpath
	 * @param description
	 *            beschreibung
	 * @return imageicon
	 */
	public static ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = UiUtil.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Erzeugt ien Image aus dem uebergebenen path. path ist im classpath.
	 * 
	 * @param path
	 *            path aus classpath
	 * @param description
	 *            beschreibung
	 * @return imageicon
	 */
	public static Image createImage(String path) {
		java.net.URL imgURL = UiUtil.class.getResource(path);
		if (imgURL != null) {
			Image img = Toolkit.getDefaultToolkit().createImage(imgURL);
			return img;
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Erzeugt ein image aus filename.
	 * 
	 * @param path
	 *            filename
	 * @return image
	 */
	public static Image createImageFromFile(String path) {
		Image img = Toolkit.getDefaultToolkit().createImage(path);
		return img;
	}
}
