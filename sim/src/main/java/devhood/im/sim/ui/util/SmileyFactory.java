package devhood.im.sim.ui.util;

/**
 * Die SmileyFactory sorg dafuer, dass die Smiley eingabe zu eine img tag wird.
 * @author flo
 *
 */
public interface SmileyFactory {


	/**
	 * Smiley einfuegen.
	 * 
	 * @param c text.
	 * @return c mit img.
	 */
	public String applySmiles(String c);
}
