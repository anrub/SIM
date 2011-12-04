package devhood.im.sim.ui.util;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public class UiUtil {

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
