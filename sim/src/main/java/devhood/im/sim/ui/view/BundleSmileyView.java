package devhood.im.sim.ui.view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import devhood.im.sim.ui.smiley.module.Mapping;
import devhood.im.sim.ui.smiley.module.SmileyPack;
import devhood.im.sim.ui.util.UiUtil;

@Named
public class BundleSmileyView extends JFrame {
	private JTabbedPane smileyBundle = new JTabbedPane();

	@PostConstruct
	public void init() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(UiUtil.createImage("/images/smile.gif"));

		add(smileyBundle);
	}

	public void addSmiley(JLabel smileyLabel, JPanel smileyPanel, int x, int y) {
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = x;
		c.gridy = y;
		smileyPanel.add(smileyLabel, c);
	}

	@Override
	public void revalidate() {
		super.revalidate();

		repaint();
		pack();
	}

	public void revalidate(Component component) {
		super.revalidate();

		component.repaint();
	}

	public void addSmileys(List<JLabel> smileyLabels, JPanel smileyPanel) {

		int x = 0;
		int y = 0;

		Collections.sort(smileyLabels, new Comparator<JLabel>() {
			@Override
			public int compare(JLabel o1, JLabel o2) {
				return o1.getToolTipText().compareTo(o2.getToolTipText());
			}
		});
		for (JLabel l : smileyLabels) {
			addSmiley(l, smileyPanel, x, y);

			if (y < 6) {
				y++;
			} else {
				y = 0;

				x++;
			}
		}

	}

	public void addSmileyPack(SmileyPack pack,
			MouseListener smileyIconMouseListener) {
		List<JLabel> smileyLabels = new ArrayList<JLabel>();
		for (Mapping m : pack.getMappings().getMapping()) {
			String imagePath = m.getIcon();
			String key = m.getShortcut()[0];

			String smileyCode = key.replace("&gt;", ">").replace("&lt;", "<")
					.replace("&amp;", "&").replace("&quot;", "\"");
			ImageIcon img = UiUtil.createImageIcon(imagePath, smileyCode);

			JLabel smileyLabel = new JLabel(img);
			smileyLabel.setToolTipText(smileyCode);
			smileyLabel.addMouseListener(smileyIconMouseListener);

			smileyLabels.add(smileyLabel);
		}
		JPanel smileyPanel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		smileyPanel.setLayout(layout);
		addSmileys(smileyLabels, smileyPanel);

		smileyBundle.add(smileyPanel);

		smileyBundle.add(pack.getName(), smileyPanel);

		revalidate();
	}
}
