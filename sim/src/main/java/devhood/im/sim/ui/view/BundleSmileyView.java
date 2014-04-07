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

	public void addSmileys(List<JLabel> smileyLabels, JPanel smileyPanel,
			int avgHeight, int avgWidth) {

		int x = 0;
		int y = 0;

		Collections.sort(smileyLabels, new Comparator<JLabel>() {
			@Override
			public int compare(JLabel o1, JLabel o2) {
				return o1.getToolTipText().compareTo(o2.getToolTipText());
			}
		});
		System.out.println("height:" + avgHeight + ", width:" + avgWidth);
		int cols = 5;
		if (avgHeight <= 20) {
			cols = 6;
		} else if (avgHeight <= 32) {
			cols = 4;
		} else if (avgHeight <= 40) {
			cols = 3;
		} else if (avgHeight <= 50) {
			cols = 2;
		}

		for (JLabel l : smileyLabels) {
			addSmiley(l, smileyPanel, x, y);

			if (y < cols) {
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
		int avgWidth = 0;
		int avgHeight = 0;

		for (Mapping m : pack.getMappings().getMapping()) {
			String imagePath = m.getIcon();
			String key = m.getShortcut()[0];

			ImageIcon img = UiUtil.createImageIcon(imagePath, key);
			avgWidth = (avgWidth + img.getIconWidth()) / 2;
			avgHeight = (avgHeight + img.getIconHeight()) / 2;

			JLabel smileyLabel = new JLabel(img);
			smileyLabel.setToolTipText(key);
			smileyLabel.addMouseListener(smileyIconMouseListener);

			smileyLabels.add(smileyLabel);
		}
		JPanel smileyPanel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		smileyPanel.setLayout(layout);
		addSmileys(smileyLabels, smileyPanel, avgHeight, avgWidth);

		smileyBundle.add(smileyPanel);

		smileyBundle.add(pack.getName(), smileyPanel);
		smileyBundle.insertTab(
				pack.getName(),
				UiUtil.createImageIcon(pack.getMappings().getMapping().get(0)
						.getIcon(), ""), smileyPanel, "",
				smileyBundle.getTabCount());

		revalidate();
	}
}
