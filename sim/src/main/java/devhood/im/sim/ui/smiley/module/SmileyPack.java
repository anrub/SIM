package devhood.im.sim.ui.smiley.module;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SmileyPack {
	private String name;

	/**
	 * Ordner wird automatisch nach smilies gescannt :filename: zugeordnet.
	 */
	private String autoScan;

	private String factory;

	@XmlElement
	private SmileyMappings mappings = new SmileyMappings();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SmileyMappings getMappings() {
		return mappings;
	}

	public void setMappings(SmileyMappings mappings) {
		this.mappings = mappings;
	}

	public String getAutoScan() {
		return autoScan;
	}

	public void setAutoScan(String autoScan) {
		this.autoScan = autoScan;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public void addMapping(Mapping m) {
		this.mappings.getMapping().add(m);

	}
}
