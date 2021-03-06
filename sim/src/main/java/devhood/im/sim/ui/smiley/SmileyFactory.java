package devhood.im.sim.ui.smiley;

import devhood.im.sim.ui.smiley.module.SmileyPack;

/**
 * Die SmileyFactory sorg dafuer, dass die Smiley eingabe zu eine img tag wird.
 * 
 * @author flo
 * 
 */
public interface SmileyFactory {

	/**
	 * Smiley einfuegen.
	 * 
	 * @param c
	 *            text.
	 * @return c mit img.
	 */
	public String applySmiles(String c);

	/**
	 * Gibt die Smileys zurueck.
	 * 
	 * @return smileys.
	 */
	public SmileyPack getSmileys();
}
