package ui;

import java.awt.*;

import javax.swing.*;

public class SmallStepGUI extends JDialog {
	
	private SSComponent ssComponent;
	
	public SmallStepGUI(Frame owner, String title, boolean modal, SmallStepModel model) {
		super(owner, title, modal);
		setLayout(new GridLayout(0,1));
		ssComponent = new SSComponent(model);
		JPanel panel = new JPanel(new GridLayout(0,1));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView(ssComponent);
		panel.setBorder(BorderFactory.createEmptyBorder (10, 10, 10, 10));
		panel.add(scrollPane);
		add(panel);
		pack();
		setSize(800, 600);
	}

}
