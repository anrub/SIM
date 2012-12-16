package devhood.im.sim.ui.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import devhood.im.sim.ui.util.SmileyFactory;
import devhood.im.sim.ui.util.UiUtil;

@Named
public class SmileyView extends JFrame {

	private MouseListener smileyIconMouseListener;

	private JPanel smileyPanel = new JPanel();

	private GridBagConstraints c = new GridBagConstraints();

	private int x = 0;
	private int y = 0;

	@Inject
	public SmileyFactory smileyFactory;

	@PostConstruct
	public void init() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(UiUtil.createImage("/images/yahoo_smileys/01.gif"));
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
	}

}
