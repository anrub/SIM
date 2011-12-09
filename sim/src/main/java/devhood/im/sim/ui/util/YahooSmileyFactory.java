package devhood.im.sim.ui.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Nutzt die Smileys von Yahoo Messenger.
 * 
 * @author Schiefele.Andreas
 */
public class YahooSmileyFactory implements SmileyFactory {

	/**
	 * Tauscht die Shorts gegen die Smiley-Grafiken aus.
	 * @param String c
	 */
	public String applySmiles(String c) {

		String smile = c;
		String icon = "";

		if (c.equals(":)") || c.equals(":-)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/01.gif").toString();
		}
		if (c.equals(":-(") || c.equals(":(")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/02.gif").toString();
		}
		if (c.equals(";)") || c.equals(";-)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/03.gif").toString();
		}
		if (c.equals(":-D") || c.equals(":D")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/04.gif").toString();
		}
		if (c.equals(";;)") || c.equals(";;-)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/05.gif").toString();
		}
		if (c.equals(":\\") || c.equals(":/") || c.equals(":-\\") || c.equals(":-/")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/06.gif").toString();
		}
		if (c.equals(":-x") || c.equals(":x")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/07.gif").toString();
		}
		if (c.equals(":\"&gt;") || c.equals(":-\"&gt;")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/08.gif").toString();
		}
		if (c.equals(":-p") || c.equals(":p") || c.equals(";-p") || c.equals(";p")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/09.gif").toString();
		}
		if (c.equals(":*") || c.equals(":-*")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/10.gif").toString();
		}
		if (c.equals(":O") || c.equals(":-O")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/11.gif").toString();
		}
		if (c.equals("X(") || c.equals("X-(")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/12.gif").toString();
		}
		if (c.equals(":-&gt;") || c.equals(":&gt;")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/13.gif").toString();
		}
		if (c.equalsIgnoreCase("B-)") || c.equalsIgnoreCase("B)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/14.gif").toString();
		}
		if (c.equals(":-s") || c.equals(":s")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/15.gif").toString();
		}
		if (c.equals("&gt;:)") || c.equals("&gt;:-)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/16.gif").toString();
		}
		if (c.equals(":-((") || c.equals(":((")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/17.gif").toString();
		}
		if (c.equals(":-))") || c.equals(":))")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/18.gif").toString();
		}
		if (c.equals(":-|") || c.equals(":|")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/19.gif").toString();
		}
		if (c.equals("/:)") || c.equals("/:-)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/20.gif").toString();
		}
		if (c.equals("0:-)") || c.equals("0:)") || c.equals("O:-)") || c.equals("O:)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/21.gif").toString();
		}
		if (c.equals(":-B") || c.equals(":B") || c.equals("8)") || c.equals("8-)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/22.gif").toString();
		}
		if (c.equals("=;") || c.equals("=;-")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/23.gif").toString();
		}
		if (c.equals("|-)") || c.equals("|)") || c.equals("zzz") || c.equals("zzz")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/24.gif").toString();
		}
		if (c.equals("8-|") || c.equals("8|")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/25.gif").toString();
		}
		if (c.equals(":-&") || c.equals(":&")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/26.gif").toString();
		}
		if (c.equals(":-$") || c.equals(":$")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/27.gif").toString();
		}
		if (c.equals("[-(")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/28.gif").toString();
		}
		if (c.equals(":o)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/29.gif").toString();
		}
		if (c.equals("8-}")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/30.gif").toString();
		}
		if (c.equals("(:|") || c.equals("(:-|")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/31.gif").toString();
		}
		if (c.equals("=P~")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/32.gif").toString();
		}
		if (c.equals(":-?") || c.equals(":?")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/33.gif").toString();
		}
		if (c.equals("#-o") || c.equals("#o")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/34.gif").toString();
		}
		if (c.equals("=D&gt;")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/35.gif").toString();
		}
		if (c.equals(":@)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/36.gif").toString();
		}
		if (c.equals("3:-O")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/37.gif").toString();
		}
		if (c.equals(":(|)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/38.gif").toString();
		}
		if (c.equals("~:&gt;")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/39.gif").toString();
		}
		if (c.equals("@};-")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/40.gif").toString();
		}
		if (c.equals("%%-")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/41.gif").toString();
		}
		if (c.equals("**==")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/42.gif").toString();
		}
		if (c.equals("(~~)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/43.gif").toString();
		}
		if (c.equals("~o)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/44.gif").toString();
		}
		if (c.equals("*-:)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/45.gif").toString();
		}
		if (c.equals("8-X")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/46.gif").toString();
		}
		if (c.equals("=:)") || c.equals("=:-)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/47.gif").toString();
		}
		if (c.equals("&gt;-)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/48.gif").toString();
		}
		if (c.equals(":-L")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/49.gif").toString();
		}
		if (c.equals("&lt;):)") || c.equals("&lt;):-)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/50.gif").toString();
		}
		if (c.equals("[-o&lt;") || c.equals("[o&lt;")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/51.gif").toString();
		}
		if (c.equals("@-)") || c.equals("@)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/52.gif").toString();
		}
		if (c.equals("$-)") || c.equals("$)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/53.gif").toString();
		}
		if (c.equals(":-\"")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/54.gif").toString();
		}
		if (c.equals(":^o")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/55.gif").toString();
		}
		if (c.equalsIgnoreCase("b-(")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/56.gif").toString();
		}
		if (c.equals(":)&gt;-") || c.equals(":-)&gt;-")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/57.gif").toString();
		}
		if (c.equals("[-X")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/58.gif").toString();
		}
		if (c.equals("\\:D/") || c.equals("\\:-D/")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/59.gif").toString();
		}
		if (c.equals("&gt;:D&lt;") || c.equals("&gt;:-D&lt;")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/60.gif").toString();
		}
		if (c.equals("o-&gt;)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/61.gif").toString();
		}
		if (c.equals("o=&gt;")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/62.gif").toString();
		}
		if (c.equals("o-+")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/63.gif").toString();
		}
		if (c.equals("(%)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/64.gif").toString();
		}
		if (c.equals(":-@")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/76.gif").toString();
		}
		if (c.equals("^:)^") || c.equals("^:-)^")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/77.gif").toString();
		}
		if (c.equals(":-j")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/78.gif").toString();
		}
		if (c.equals("(*)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/79.gif").toString();
		}
		if (c.equals(":)]") || c.equals(":-)]")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/100.gif").toString();
		}if (c.equals(":-c")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/101.gif").toString();
		}
		if (c.equals("~X(") || c.equals("~X-(")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/102.gif").toString();
		}
		if (c.equals(":-h") || c.equals(":-)h")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/103.gif").toString();
		}
		if (c.equals(":-t")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/104.gif").toString();
		}
		if (c.equals("8-&gt;")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/105.gif").toString();
		}
		if (c.equals(":-??")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/106.gif").toString();
		}
		if (c.equals("%-(")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/107.gif").toString();
		}
		if (c.equals(":o3")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/108.gif").toString();
		}
		if (c.equals("x_x")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/109.gif").toString();
		}
		if (c.equals(":!!")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/110.gif").toString();
		}
		if (c.equals("\\m/")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/111.gif").toString();
		}
		if (c.equals(":-q")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/112.gif").toString();
		}
		if (c.equals(":-bd") || c.equals(":)bd") || c.equals(":-)bd") || c.equals("b:)d") || c.equals("b:-)d")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/113.gif").toString();
		}
		if (c.equalsIgnoreCase("^#(^") || c.equals("#(^^")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/114.gif").toString();
		}
		if (c.equals(":ar!")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/116.gif").toString();
		}
		if (c.equals("~^o^~")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/120.gif").toString();
		}
		if (c.equals("'@^@|||")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/121.gif").toString();
		}
		if (c.equals("[]---")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/122.gif").toString();
		}
		if (c.equals("^o^||3")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/123.gif").toString();
		}
		if (c.equals(":-(||&gt;")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/124.gif").toString();
		}
		if (c.equals("'+_+")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/125.gif").toString();
		}
		if (c.equals(":::^^:::")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/126.gif").toString();
		}
		if (c.equals("o|^_^|o")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/127.gif").toString();
		}
		if (c.equals(":puke!")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/128.gif").toString();
		}
		if (c.equals("o|\\~")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/129.gif").toString();
		}
		if (c.equals("o|:-)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/130.gif").toString();
		}
		if (c.equals("[]==[]")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/131.gif").toString();
		}
		if (c.equals(":-)/\\:-)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/132.gif").toString();
		}
		if (c.equals(":(game)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/133.gif").toString();
		}
		if (c.equals("@-@")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/134.gif").toString();
		}
		if (c.equals(":-&gt;~~")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/135.gif").toString();
		}
		if (c.equals("?@_@?")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/136.gif").toString();
		}
		if (c.equals(":(tv)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/137.gif").toString();
		}
		if (c.equals("&[]")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/138.gif").toString();
		}
		if (c.equals("%||:-{")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/139.gif").toString();
		}
		if (c.equals("%*-{")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/140.gif").toString();
		}
		if (c.equals(":(fight)")) {
			icon = getClass().getResource(
					"/images/yahoo_smileys/141.gif").toString();
		}

		return smile;
	}
}
