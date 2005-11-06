package ui;

import java.awt.*;

import javax.swing.*;

public class SmallStepGUI extends JDialog {
	
	private SSComponent ssComponent;
	
	public SmallStepGUI(Frame owner, String title, boolean modal, SmallStepModel model) {
		super(owner, title, modal);
		getContentPane ().setLayout(new BoxLayout (getContentPane (), 
				BoxLayout.X_AXIS));

		ssComponent = new SSComponent(model);
//		JPanel panel = new JPanel(new GridLayout(0,1));
//		panel.add(ssComponent);
		
		setBackground (Color.white);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(ssComponent);
		scrollPane.getViewport().setBackground(Color.white);
		getContentPane().add(scrollPane);
		// panel.setBorder(BorderFactory.createEmptyBorder (10, 10, 10, 10));
		// panel.add(scrollPane);
		add(scrollPane);
		setSize(800, 600);
	}

}
