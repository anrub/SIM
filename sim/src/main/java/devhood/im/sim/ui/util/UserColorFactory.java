package devhood.im.sim.ui.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import devhood.im.sim.model.Color;

/**
 * Die UserColorFactory hält für die User eine Farbe vor. Über die Methode
 * getOrReserveUserColor(userName) kann man sich zu einem User den gespeicherten
 * Farbwert ausgeben lassen oder einen noch verfügbaren reservieren.
 * 
 * @author Schiefele.Andreas
 */
public class UserColorFactory {

	/**
	 * Liste der Farben.
	 */
	private List<Color> colors = new ArrayList<Color>();

	/**
	 * Map von Name des Users -> Farbe.
	 */
	private Map<String, Color> userColors = new HashMap<String, Color>();

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
			Color newUserColor = Color.BLACK;

			int colorCounter = 0;

			// Prüfen ob schon ein User eingetragen wurde.
			if (!userColors.isEmpty()) {
				for (Color userColor : userColors.values()) {
					if (userColor.equals(color)) {
						//Wenn Farbe schon mal gewählt wurde.
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
}
