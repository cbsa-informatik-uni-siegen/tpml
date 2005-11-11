package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SmallStepGUI extends JDialog {
	
	private SSComponent ssComponent;
	private JButton		buttonAutocomplete;
	private JButton 	buttonNextStep;
	private JButton		buttonClose;
	
	public SmallStepGUI(Frame owner, String title, boolean modal, SmallStepModel model) {
		super(owner, title, modal);
		getContentPane().setLayout (new BoxLayout (getContentPane(), BoxLayout.PAGE_AXIS));
		
		JPanel mainPanel	= new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		ssComponent 		= new SSComponent(model);
		buttonAutocomplete	= new JButton ("Autocomplete");
		buttonNextStep 		= new JButton ("NextStep");
		buttonClose    		= new JButton ("Close");

		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(ssComponent);
		scrollPane.getViewport().setBackground(Color.white);
		mainPanel.add(scrollPane);
		
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
	
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout (new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(buttonAutocomplete);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(buttonNextStep);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(buttonClose);
		mainPanel.add(buttonPanel);
		
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose ();
			}
		});
		buttonNextStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ssComponent.completeCurrentStep();
			}
		});
		buttonAutocomplete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ssComponent.completeAllSteps();
			}
		});
		
		getContentPane().add(mainPanel);
		setSize(800, 600);
	}

}
