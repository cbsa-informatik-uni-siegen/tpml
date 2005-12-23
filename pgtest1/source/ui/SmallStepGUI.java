package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

import javax.swing.*;

public class SmallStepGUI extends JDialog {
	
	private SmallStepComponent 	ssComponent;
	private JButton				buttonAutocomplete;
	private JButton 			buttonNextStep;
	private JButton				buttonClose;
	private JCheckBox			underlineBox;
	private JCheckBox			justaxiomsBox;
	private JScrollPane			scrollPane;
	
	public SmallStepGUI(Frame owner, String title, boolean modal, SmallStepModel model) {
		super(owner, title, modal);
		getContentPane().setLayout (new BoxLayout (getContentPane(), BoxLayout.PAGE_AXIS));
		
		JPanel mainPanel	= new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		// setting the "justAxioms" true just for testing. This should be taken from 
		// a settings dialog later
		ssComponent 			= new SmallStepComponent(model, true, true);
		buttonAutocomplete	= new JButton ("Autocomplete");
		buttonNextStep 		= new JButton ("NextStep");
		buttonClose    		= new JButton ("Close");
		underlineBox		= new JCheckBox ("Unterstreichung", true);
		justaxiomsBox		= new JCheckBox ("Nur Axiomregeln", true);
		
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		this.scrollPane = new JScrollPane();
		this.scrollPane.setViewportView(ssComponent);
		this.scrollPane.getViewport().setBackground(Color.white);
		mainPanel.add(scrollPane);
		
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
	
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout (new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(underlineBox);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(justaxiomsBox);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(buttonAutocomplete);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(buttonNextStep);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(buttonClose);
		mainPanel.add(buttonPanel);
		
		this.ssComponent.addSmallStepEventListener(new SmallStepEventListener () {
			public void smallStepResized(EventObject o) { jumpToTail(); }
			public void smallStepResolved(EventObject o) { jumpToTail(); }
			public void mouseFocusEvent(SmallStepEvent e) { }
		});
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose ();
			}
		});
		buttonNextStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ssComponent.completeCurrentStep();
				jumpToTail();
			}
		});
		buttonAutocomplete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ssComponent.completeAllSteps();
				jumpToTail();
			}
		});
		
		underlineBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox box = (JCheckBox)e.getSource();
				ssComponent.setUnderlineExpressions(box.isSelected());
			}
		});
		
		justaxiomsBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox box = (JCheckBox)e.getSource();
				ssComponent.setJustAxioms(box.isSelected());
			}
		});
		
		getContentPane().add(mainPanel);
		setSize(800, 600);
		
	}

	public void jumpToTail() {
		this.scrollPane.getViewport().setViewPosition(new Point(this.scrollPane.getViewport().getViewPosition().x, ssComponent.getHeight()));
	}
}
