package devhood.im.sim.ui.smiley.module;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SmileyMappings {

	@XmlElement
	private List<Mapping> mapping = new ArrayList<Mapping>();

	public List<Mapping> getMapping() {
		return mapping;
	}

	public void setMapping(List<Mapping> mapping) {
		this.mapping = mapping;
	}

	public Mapping getMapping(String shortcut) {
		for (Mapping m : mapping) {
			for (String s : m.getShortcut()) {
				if (s.equals(shortcut)) {
					return m;
				}
			}
		}
		return null;
	}
}
