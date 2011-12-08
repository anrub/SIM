/**
 * 
 */
package devhood.im.sim.ui.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import devhood.im.sim.model.Color;

/**
 * 
 * 
 * @author Schiefele.Andreas
 */
public class UserColorFactory {

	/**
	 * Liste der Farben.
	 */
	private List<Color> colors = new ArrayList<Color>();

	/**
	 * Map von Name des Users(Name des Tabs) -> Farbe.
	 */
	private Map<String, Color> userColors = new HashMap<String, Color>();

	public UserColorFactory() {
		for (Color color: Color.values()){
			if (!color.equals(Color.BLACK)){
				colors.add(color);
			}
		}
		
		Collections.shuffle(colors);
	}

	/**
	 * Gibt für den User die reservierte Farbe zurück. Wenn keine Farbe bisher
	 * reserviert wurde wird dies nun getan.
	 * 
	 * @param userName
	 * @return
	 */
	public Color getOrReservUserColor(String userName) {

		if (userColors.containsKey(userName)) {
			return userColors.get(userName);
		} else {
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

		int colorRepetition = userColors.size() / colors.size();

		for (Color color : colors) {
			Color newUserColor = Color.BLACK;

			int colorCounter = 0;

			// Prüfen ob schon ein User eingetragen wurde.
			if (!userColors.isEmpty()) {
				for (Color userColor : userColors.values()) {
					if (userColor.equals(color)) {
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
