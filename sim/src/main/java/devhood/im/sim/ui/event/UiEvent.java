package devhood.im.sim.ui.event;

import javax.swing.JComponent;

public class UiEvent<T> {

	public JComponent parent;

	public T payload;

	public UiEvent() {

	}

	public UiEvent(JComponent parent, T payload) {
		this.parent = parent;
		this.payload = payload;
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}

	public JComponent getParent() {
		return parent;
	}

	public void setParent(JComponent parent) {
		this.parent = parent;
	}
}
