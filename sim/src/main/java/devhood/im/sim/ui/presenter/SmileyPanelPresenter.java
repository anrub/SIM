package devhood.im.sim.ui.presenter;

import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import devhood.im.sim.ui.smiley.SmileyFactory;
import devhood.im.sim.ui.smiley.module.Mapping;
import devhood.im.sim.ui.util.UiUtil;
import devhood.im.sim.ui.view.SmileyView;

@Named
public class SmileyPanelPresenter {

	@Inject
	private SmileyView view;

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
		List<JLabel> smileyLabels = new ArrayList<JLabel>();

		List<String> addedPaths = new ArrayList<String>();
		for (Mapping m : smileyFactory.getSmileys().getMappings().getMapping()) {
			String imagePath = m.getIcon();
			String key = m.getShortcut()[0];

			addedPaths.add(imagePath);

			String smileyCode = key.replace("&gt;", ">").replace("&lt;", "<")
					.replace("&amp;", "&").replace("&quot;", "\"");
			ImageIcon img = UiUtil.createImageIcon(imagePath, smileyCode);
			
			JLabel smileyLabel = new JLabel(img);
			smileyLabel.setToolTipText(smileyCode);
			smileyLabel.addMouseListener(smileyIconMouseListener);

			smileyLabels.add(smileyLabel);
		}
		view.addSmileys(smileyLabels);

		view.revalidate();

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
