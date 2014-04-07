package devhood.im.sim.ui.smiley.module;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Mapping {

	private String[] shortcut;
	private String icon;

	public Mapping() {

	}

	public Mapping(String[] shortcut, String icon) {
		this.shortcut = shortcut;
		this.icon = icon;
	}

	public String[] getShortcut() {
		return shortcut;
	}

	public void setShortcut(String[] shortcut) {
		this.shortcut = shortcut;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}