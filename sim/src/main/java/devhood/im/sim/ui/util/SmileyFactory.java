package devhood.im.sim.ui.util;

/**
 * Die SmileyFactory sorg daf�r, dass die Smiley eingabe zu eine img tag wird.
 * @author flo
 *
 */
public interface SmileyFactory {


	/**
	 * Smiley einf�gen.
	 * 
	 * @param c text.
	 * @return c mit img.
	 */
	public String applySmiles(String c);
}
