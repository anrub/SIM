package devhood.im.sim.ui;

import java.awt.GridBagConstraints;
import java.awt.LayoutManager;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import devhood.im.sim.ui.util.SmileyFactory;
import devhood.im.sim.ui.util.UiUtil;

public class SmileyPanel extends JPanel {

	private MouseListener smileyLabelMouseListener;

	public SmileyPanel(LayoutManager layout, final SmileyFactory smileyFactory) {
		super(layout);

		SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				final GridBagConstraints c = new GridBagConstraints();

				int x = 0;
				int y = 0;
				Set<String[]> keySet = smileyFactory.getSmileys().keySet();

				Map<String, String[]> tempMapForSorting = new HashMap<String, String[]>();
				List<String> keyListForSorting = new ArrayList<String>();
				for (String[] keys : keySet) {
					tempMapForSorting.put(keys[0], keys);
					keyListForSorting.add(keys[0]);
				}

				Collections.sort(keyListForSorting);

				for (String key : keyListForSorting) {
					c.gridx = x;
					c.gridy = y;
					String smileyCode = key.replace("&gt;", ">")
							.replace("&lt;", "<").replace("&amp;", "&")
							.replace("&quot;", "\"");
					ImageIcon img = UiUtil.createImageIcon(
							"/images/yahoo_smileys/"
									+ smileyFactory.getSmileys().get(
											tempMapForSorting.get(key)),
							smileyCode);
					JLabel smileyLabel = new JLabel(img);
					smileyLabel.setToolTipText(smileyCode);

					smileyLabel.addMouseListener(smileyLabelMouseListener);

					if (y < 10) {
						y++;
					} else {
						y = 0;

						x++;
					}

					add(smileyLabel, c);
				}
				return null;
			}
		};
		w.execute();
	}

	public MouseListener getSmileyLabelMouseListener() {
		return smileyLabelMouseListener;
	}

	public void setSmileyLabelMouseListener(
			MouseListener smileyLabelMouseListener) {
		this.smileyLabelMouseListener = smileyLabelMouseListener;
	}
}
