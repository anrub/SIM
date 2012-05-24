package devhood.im.sim.ui.util;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import devhood.im.sim.ui.util.Splitter;

public class SplitterTest {

	@Before
	public void setup() {
	}

	@Test
	public void testGetParts() {
		String s = "abcdefg";
		List<String> parts = Splitter.getParts(s, 3);

		assertEquals(parts.get(0), "abc");
		assertEquals(parts.get(1), "def");
		assertEquals(parts.get(2), "g");

	}
}
