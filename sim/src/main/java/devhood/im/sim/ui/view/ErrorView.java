package devhood.im.sim.ui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ErrorView extends JDialog {
	private JLabel msgLabel = new JLabel();
	
	private JTextArea errorDetail = new JTextArea();

	private JButton exit = new JButton("Exit");

	public ErrorView() {
		super();

		this.exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(1);
			}
		});

		
		JPanel pane = new JPanel();
		BorderLayout l = new BorderLayout();
		pane.setLayout(l);

		pane.add(msgLabel, BorderLayout.NORTH);
		
		errorDetail.setColumns(50);
		errorDetail.setRows(20);
		
		JScrollPane scrollPane = new JScrollPane(errorDetail);
		Dimension preferredSize = new Dimension(1000, 300);
		scrollPane.setPreferredSize(preferredSize);
		
		pane.add(scrollPane, BorderLayout.CENTER);
		pane.add(exit, BorderLayout.SOUTH);

		setContentPane(pane);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public ErrorView(String error, String detail) {
		this();
		
		msgLabel.setText(error);
		
		errorDetail.setText(detail);
	}

	public ErrorView(String string, Exception e) {
		this();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		e.printStackTrace(new PrintStream(bos));
		errorDetail.setText(bos.toString());
	}

}
