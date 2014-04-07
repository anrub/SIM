package devhood.im.sim.ui.presenter;

import java.awt.event.MouseListener;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JComponent;

import devhood.im.sim.ui.smiley.SmileyFactory;
import devhood.im.sim.ui.smiley.module.BundleSmileyPack;
import devhood.im.sim.ui.smiley.module.SmileyPack;
import devhood.im.sim.ui.view.BundleSmileyView;

@Named
public class SmileyPanelPresenter {

	@Inject
	private BundleSmileyView view;

	@Inject
	@Named("bundleSmileyFactory")
	private SmileyFactory smileyFactory;

	private MouseListener smileyIconMouseListener;

	private boolean initialized;

	public void show(JComponent relativeTo) {
		if (!initialized) {
			initPanel();
			view.pack();
		}

		view.setVisible(true);
		view.setLocationRelativeTo(relativeTo);

		view.revalidate();
	}

	public void initPanel() {
		initialized = true;
		BundleSmileyPack bundle = (BundleSmileyPack) smileyFactory.getSmileys();
		for (SmileyPack pack : bundle) {
			view.addSmileyPack(pack, smileyIconMouseListener);
		}

	}

	public MouseListener getSmileyIconMouseListener() {
		return smileyIconMouseListener;
	}

	public void setSmileyIconMouseListener(MouseListener smileyIconMouseListener) {
		this.smileyIconMouseListener = smileyIconMouseListener;
	}

	public void hide() {
		view.dispose();

	}
}
