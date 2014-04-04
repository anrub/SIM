package devhood.im.sim.ui.smiley;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

/**
 * Nutzt die Smileys von Yahoo Messenger.
 * 
 * @author Schiefele.Andreas
 */
@Named("yahooSmileyFactory")
public class YahooSmileyFactory implements SmileyFactory {

	/* Hier sind zu jedem Smiley-Kürzel die Grafik-Pfade hinterlegt. */
	private Map<String[], String> smileys = new HashMap<String[], String>();

	private String imagePath = "/images/yahoo_smileys/";

	private ApplySmiley applySmileyCmd = new ApplySmiley();

	@Override
	public String applySmiles(String c) {
		return applySmileyCmd.applySmiles(c, smileys);
	}

	public Map<String[], String> getSmileys() {
		return smileys;
	}

	/**
	 * Die Initialisierung führt das Pairing der Kurzschreibweisen zu den
	 * Bildnamen durch.
	 */
	public void init() {

		smileys.put(new String[] { ":)", ":-)" }, imagePath + "01.gif");
		smileys.put(new String[] { ":(", ":-(" }, imagePath + "02.gif");
		smileys.put(new String[] { ";)", ";-)" }, imagePath + "03.gif");
		smileys.put(new String[] { ":-D", ":D" }, imagePath + "04.gif");
		smileys.put(new String[] { ";;)", ";;-)" }, imagePath + "05.gif");
		smileys.put(new String[] { ":-\\", ":-/" }, imagePath + "06.gif");
		smileys.put(new String[] { ":-x", ":x", ":-X", ":X" }, imagePath
				+ "07.gif");
		smileys.put(new String[] { ":&quot;&gt;", ":-&quot;&gt;" }, imagePath
				+ "08.gif");
		smileys.put(new String[] { ":-p", ":-P", ";-p", ";-P", ":p", ":P",
				";p", ";P", ":-)p", ":-)P", ";-)p", ";-)P", ":)p", ":)P",
				";)p", ";)P" }, imagePath + "09.gif");
		smileys.put(new String[] { ":*", ":-*" }, imagePath + "10.gif");
		smileys.put(new String[] { ":O", ":-O", ":o", ":-o" }, imagePath
				+ "11.gif");
		smileys.put(new String[] { "X-(", "X(", "x-(", "x(" }, imagePath
				+ "12.gif");
		smileys.put(new String[] { ":-&gt;", ":&gt;" }, imagePath + "13.gif");
		smileys.put(new String[] { "B-)", "B)" }, imagePath + "14.gif");
		smileys.put(new String[] { ":s", ":-s", ":S", ":-S" }, imagePath
				+ "15.gif");
		smileys.put(new String[] { "&gt;:)", "&gt;:-)" }, imagePath + "16.gif");
		smileys.put(new String[] { ":-((", ":((" }, imagePath + "17.gif");
		smileys.put(new String[] { ":-))", ":))" }, imagePath + "18.gif");
		smileys.put(new String[] { ":-|", ":|" }, imagePath + "19.gif");
		smileys.put(new String[] { "/:)", "/:-)" }, imagePath + "20.gif");
		smileys.put(new String[] { "0:)", "0:-)", "O:)", "O:-)" }, imagePath
				+ "21.gif");
		smileys.put(new String[] { ":-B", ":B", "8)", "8-)" }, imagePath
				+ "22.gif");
		smileys.put(new String[] { "=;-", "=;" }, imagePath + "23.gif");
		smileys.put(new String[] { "|-)", "|)", "I-)", "I)", "zzz" }, imagePath
				+ "24.gif");
		smileys.put(new String[] { "8-|", "8|" }, imagePath + "25.gif");
		smileys.put(new String[] { ":-&amp;", ":&amp;" }, imagePath + "26.gif");
		smileys.put(new String[] { ":-$", ":$" }, imagePath + "27.gif");
		smileys.put(new String[] { "[-(" }, imagePath + "28.gif");
		smileys.put(new String[] { ":o)" }, imagePath + "29.gif");
		smileys.put(new String[] { "8-}" }, imagePath + "30.gif");
		smileys.put(new String[] { ":|", ":-|" }, imagePath + "31.gif");
		smileys.put(new String[] { "=P~" }, imagePath + "32.gif");
		smileys.put(new String[] { ":-?", ":?" }, imagePath + "33.gif");
		smileys.put(new String[] { "#-o", "#-O" }, imagePath + "34.gif");
		smileys.put(new String[] { "=D&gt;" }, imagePath + "35.gif");
		smileys.put(new String[] { ":@)" }, imagePath + "36.gif");
		smileys.put(new String[] { "3:-O" }, imagePath + "37.gif");
		smileys.put(new String[] { ":(|)" }, imagePath + "38.gif");
		smileys.put(new String[] { "~:&gt;" }, imagePath + "39.gif");
		smileys.put(new String[] { "@};-" }, imagePath + "40.gif");
		smileys.put(new String[] { "%%-" }, imagePath + "41.gif");
		smileys.put(new String[] { "**==" }, imagePath + "42.gif");
		smileys.put(new String[] { "(~~)" }, imagePath + "43.gif");
		smileys.put(new String[] { "~o)" }, imagePath + "44.gif");
		smileys.put(new String[] { "*-:)" }, imagePath + "45.gif");
		smileys.put(new String[] { "8-X", "8-x" }, imagePath + "46.gif");
		smileys.put(new String[] { "=:)", "=:-)" }, imagePath + "47.gif");
		smileys.put(new String[] { "&gt;-)" }, imagePath + "48.gif");
		smileys.put(new String[] { ":-L" }, imagePath + "49.gif");
		smileys.put(new String[] { "&lt;):)", "&lt;):-)" }, imagePath
				+ "50.gif");
		smileys.put(new String[] { "[-o&lt;", "[o&lt;" }, imagePath + "51.gif");
		smileys.put(new String[] { "@-)" }, imagePath + "52.gif");
		smileys.put(new String[] { "$-)" }, imagePath + "53.gif");
		smileys.put(new String[] { ":-&quot;" }, imagePath + "54.gif");
		smileys.put(new String[] { ":^o" }, imagePath + "55.gif");
		smileys.put(new String[] { "b-(" }, imagePath + "56.gif");
		smileys.put(new String[] { ":)&gt;-", ":-)&gt;-" }, imagePath
				+ "57.gif");
		smileys.put(new String[] { "[-X", "[-x" }, imagePath + "58.gif");
		smileys.put(new String[] { "\\:D/", "\\:-D/" }, imagePath + "59.gif");
		smileys.put(new String[] { "&gt;:D&lt;", "&gt;:-D&lt;" }, imagePath
				+ "60.gif");
		smileys.put(new String[] { "o-&gt;)" }, imagePath + "61.gif");
		smileys.put(new String[] { "o=&gt;" }, imagePath + "62.gif");
		smileys.put(new String[] { "o-+" }, imagePath + "63.gif");
		smileys.put(new String[] { "(%)" }, imagePath + "64.gif");
		smileys.put(new String[] { "^:)^", "^:-)^" }, imagePath + "77.gif");
		smileys.put(new String[] { ":-j", ":-J" }, imagePath + "78.gif");
		smileys.put(new String[] { "(*)" }, imagePath + "79.gif");
		smileys.put(new String[] { ":)]" }, imagePath + "100.gif");
		smileys.put(new String[] { ":-c", ":-C" }, imagePath + "101.gif");
		smileys.put(new String[] { "~X(", "~x(", "~X-(", "~x-(" }, imagePath
				+ "102.gif");
		smileys.put(new String[] { ":-h", ":-)h" }, imagePath + "103.gif");
		smileys.put(new String[] { ":-t" }, imagePath + "104.gif");
		smileys.put(new String[] { "8-&gt;" }, imagePath + "105.gif");
		smileys.put(new String[] { ":-??" }, imagePath + "106.gif");
		smileys.put(new String[] { "%-(" }, imagePath + "107.gif");
		smileys.put(new String[] { ":o3" }, imagePath + "108.gif");
		smileys.put(new String[] { "x_x" }, imagePath + "109.gif");
		smileys.put(new String[] { ":!!" }, imagePath + "110.gif");
		smileys.put(new String[] { "\\m/" }, imagePath + "111.gif");
		smileys.put(new String[] { ":-q" }, imagePath + "112.gif");
		smileys.put(new String[] { ":-bd", ":-)bd", ":)bd", "b:)d", "b:-)d",
				";-bd", ";-)bd", ";)bd", "b;)d", "b;-)d" }, "113.gif");
		smileys.put(new String[] { "^#(^", "#(^^" }, imagePath + "114.gif");
		smileys.put(new String[] { ":bz", ":)bz", ":-bz", ":-)bz" }, imagePath
				+ "115.gif");
		smileys.put(new String[] { ":ar!" }, imagePath + "116.gif");
		smileys.put(new String[] { "~^o^~", ":-)" }, imagePath + "120.gif");
		smileys.put(new String[] { "'@^@|||" }, imagePath + "121.gif");
		smileys.put(new String[] { "[]---" }, imagePath + "122.gif");
		smileys.put(new String[] { "^o^||3" }, imagePath + "123.gif");
		smileys.put(new String[] { ":-(||&gt;" }, imagePath + "124.gif");
		smileys.put(new String[] { "'+_+" }, imagePath + "125.gif");
		smileys.put(new String[] { ":::^^:::" }, imagePath + "126.gif");
		smileys.put(new String[] { "o|^_^|o" }, imagePath + "127.gif");
		smileys.put(new String[] { ":puke!" }, imagePath + "128.gif");
		smileys.put(new String[] { "o|\\~" }, imagePath + "129.gif");
		smileys.put(new String[] { "o|:-)", "o|:)" }, imagePath + "130.gif");
		smileys.put(new String[] { "[]==[]" }, imagePath + "131.gif");
		smileys.put(new String[] { ":-)/\\:-)", ":)/\\:)" }, imagePath
				+ "132.gif");
		smileys.put(new String[] { ":(game)" }, imagePath + "133.gif");
		smileys.put(new String[] { "@-@" }, imagePath + "134.gif");
		smileys.put(new String[] { ":-&gt;~~" }, imagePath + "135.gif");
		smileys.put(new String[] { "?@_@?" }, imagePath + "136.gif");
		smileys.put(new String[] { ":(tv)" }, imagePath + "137.gif");
		smileys.put(new String[] { "&amp;[]" }, imagePath + "138.gif");
		smileys.put(new String[] { "%||:-{", "%||:{" }, imagePath + "139.gif");
		smileys.put(new String[] { "%*-{", "%*{" }, imagePath + "140.gif");
		smileys.put(new String[] { ":(fight)" }, imagePath + "141.gif");
		smileys.put(new String[] { "[..]" }, imagePath + "142.gif");
	}
}
