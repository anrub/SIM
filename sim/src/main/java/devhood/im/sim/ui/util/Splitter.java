package devhood.im.sim.ui.util;

import java.util.ArrayList;
import java.util.List;

public class Splitter {
	public static List<String> getParts(String string, int partitionSize) {
		List<String> parts = new ArrayList<String>();
		int len = string.length();
		for (int i = 0; i < len; i += partitionSize) {
			parts.add(string.substring(i, Math.min(len, i + partitionSize)));
		}
		return parts;
	}
}
