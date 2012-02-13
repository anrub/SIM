package devhood.im.sim.ui.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.model.Color;

/**
 * Die UserColorFactory hält für die User eine Farbe vor. Über die Methode
 * getOrReserveUserColor(userName) kann man sich zu einem User den gespeicherten
 * Farbwert ausgeben lassen oder einen noch verfügbaren reservieren.
 * 
 * @author Schiefele.Andreas
 */
@Named("userColorFactory")
public class UserColorFactory {

	/**
	 * Liste der Farben.
	 */
	private List<Color> colors = new ArrayList<Color>();

	/**
	 * Map von Name des Users -> Farbe.
	 */
	private Map<String, Color> userColors = new HashMap<String, Color>();

	/**
	 * Konfigurationsklasse.
	 */
	@Inject
	private SimConfiguration simConfiguration;

	public UserColorFactory() {
		// Verfügbare Farben eintragen (außer Schwarz).
		for (Color color : Color.values()) {
			if (!color.equals(Color.BLACK)) {
				colors.add(color);
			}
		}

		// Farbenreihenfolge zufällig vergeben.
		Collections.shuffle(colors);
	}

	/**
	 * Gibt für den User die reservierte Farbe zurück. Wenn keine Farbe bisher
	 * reserviert wurde wird dies nun getan.
	 * 
	 * @param userName
	 * @return
	 */
	public Color getOrReserveUserColor(String userName) {

		if (userColors.containsKey(userName)) {
			// Wenn dieser User bereits eine Farbe besitzt.
			return userColors.get(userName);
		} else {
			// Wenn es noch keine Farbe für diesen User gibt dann eine
			// reservieren.
			Color userColor = generateUserColor(userName);
			userColors.put(userName, userColor);

			return userColor;
		}
	}

	/**
	 * Gibt eine verfügbare Farbe zurück.
	 * 
	 * @param userName
	 *            Der Name des Users.
	 * @return Color Eine freie Farbe.
	 */
	private Color generateUserColor(String userName) {

		// Anhand der Anzahl an verfügbaren und reservierten Farben ermitteln
		// wie oft eine Farbe vorkommen darf.
		int colorRepetition = userColors.size() / colors.size();

		for (Color color : colors) {
			if (color.getHexValue().equalsIgnoreCase(
					simConfiguration.getUserColor())
					|| isColorSimilarToOwn(color)) {
				continue;
			}

			Color newUserColor = Color.BLACK;

			int colorCounter = 0;

			// Prüfen ob schon ein User eingetragen wurde.
			if (!userColors.isEmpty()) {
				for (Color userColor : userColors.values()) {
					if (userColor.equals(color)) {
						// Wenn Farbe schon mal gewählt wurde.
						colorCounter++;
					}
				}

				// Prüfen ob die Farbe benutzt werden darf.
				if (colorRepetition == colorCounter) {
					newUserColor = color;

					return newUserColor;
				}

			} else {
				// Wenn noch kein User eine Farbe hat.
				return color;
			}
		}

		return Color.BLACK;
	}

	/**
	 * Gibt zurueck, die die uebergebene Farbe der eigenen Farbe zu ähnlich ist,
	 * um richtig unterschieden zu werden.
	 * 
	 * @param c
	 *            Color
	 * @return true/false
	 */
	public boolean isColorSimilarToOwn(Color c) {
		int[] rgbOwnColor = simConfiguration.getUserColorRgb();
		int[] rgbColor = simConfiguration.getUserColorRgb(c.getHexValue());
		int rgb = rgbColor[0] + rgbColor[1] + rgbColor[2];

		int diff = new Double(Math.pow(Math.pow((rgbOwnColor[0] - rgbColor[0]), 2) + Math.pow((rgbOwnColor[1] - rgbColor[1]), 2) + Math.pow((rgbOwnColor[2] - rgbColor[2]), 2), 0.5)).intValue();

		boolean isColorSimilar = false;
		if (diff < simConfiguration.getMinColorDiff()) {
			isColorSimilar = true;
		}

		return isColorSimilar;
	}
}
