package devhood.im.sim.ui.smiley.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BundleSmileyPack extends SmileyPack implements
		Iterable<SmileyPack> {

	private List<SmileyPack> smileyPacks = new ArrayList<SmileyPack>();

	private Map<String, SmileyPack> smileyPackMap = new HashMap<String, SmileyPack>();

	public void add(SmileyPack pack) {
		smileyPacks.add(pack);
		getMappings().getMapping().addAll(pack.getMappings().getMapping());

		smileyPackMap.put(pack.getName(), pack);
	}

	public SmileyPack get(String smileyPackName) {
		return smileyPackMap.get(smileyPackName);
	}

	@Override
	public Iterator<SmileyPack> iterator() {
		return new Iterator<SmileyPack>() {
			private int i = 0;

			@Override
			public SmileyPack next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}

				return smileyPacks.get(i++);
			}

			@Override
			public boolean hasNext() {
				return i < smileyPacks.size();
			}

			@Override
			public void remove() {
				throw new NotImplementedException();
			}
		};
	}
}
