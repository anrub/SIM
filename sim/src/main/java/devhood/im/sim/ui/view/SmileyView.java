package devhood.im.sim.ui.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import devhood.im.sim.ui.util.UiUtil;

@Named
public class SmileyView extends JFrame {

	private JPanel smileyPanel = new JPanel();

	private GridBagConstraints c = new GridBagConstraints();

	private int x = 0;
	private int y = 0;

	@PostConstruct
	public void init() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(UiUtil.createImage("/images/smile.gif"));
		GridBagLayout layout = new GridBagLayout();
		add(smileyPanel);
		smileyPanel.setLayout(layout);
	}

	public void addSmiley(JLabel smileyLabel) {
		c.gridx = x;
		c.gridy = y;
		smileyPanel.add(smileyLabel, c);

		if (y < 10) {
			y++;
		} else {
			y = 0;

			x++;
		}

	}

	@Override
	public void revalidate() {
		smileyPanel.revalidate();
		super.revalidate();

		repaint();
		pack();
	}

	public void addSmileys(List<JLabel> smileyLabels) {
		Collections.sort(smileyLabels, new Comparator<JLabel>() {
			@Override
			public int compare(JLabel o1, JLabel o2) {
				return o1.getToolTipText().compareTo(o2.getToolTipText());
			}
		});
		for (JLabel l : smileyLabels) {
			addSmiley(l);
		}
	}

}
