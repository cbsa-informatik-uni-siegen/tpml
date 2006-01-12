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
	private JButton				buttonSugar;
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
		ssComponent 			= new SmallStepComponent();
		buttonAutocomplete	= new JButton ("Autocomplete");
		buttonNextStep 		= new JButton ("NextStep");
		buttonClose    		= new JButton ("Close");
		buttonSugar			= new JButton ("Sugar");
		underlineBox		= new JCheckBox ("Unterstreichung", true);
		justaxiomsBox		= new JCheckBox ("Nur Axiomregeln", true);
		
		ssComponent.setModel(model);
		
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		this.scrollPane = new JScrollPane();
		this.scrollPane.setViewportView(ssComponent);
		this.scrollPane.getViewport().setBackground(Color.white);
		mainPanel.add(scrollPane);
		
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
	
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout (new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(buttonSugar);
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
//		
//		this.ssComponent.addSmallStepEventListener(new SmallStepEventListener () {
//			public void smallStepResized(EventObject o) { jumpToTail(); }
//			public void smallStepResolved(EventObject o) { jumpToTail(); }
//			public void mouseFocusEvent(SmallStepEvent e) { }
//			public void releaseSyntacticalSugar(EventObject o) { }
//		});
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose ();
			}
		});
		buttonNextStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ssComponent.getModel().completeLastStep();
				jumpToTail();
			}
		});
		buttonAutocomplete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ssComponent.getModel().completeAllSteps();
				jumpToTail();
			}
		});
		buttonSugar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ssComponent.getModel().releaseSyntacticalSugar();
				ssComponent.setNotUpToDate();
				ssComponent.repaint();
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
				ssComponent.getModel().setJustAxioms(box.isSelected());
			}
		});
		
		model.addSmallStepEventListener(new SmallStepEventListener() {
			public void stepEvaluated(EventObject o) { 
				if (ssComponent.getModel().getNumberOfSteps() > 2) {
					buttonSugar.setEnabled(false);
				}
				else {
					buttonSugar.setEnabled(true);
				}
			}
			public void contentsChanged(EventObject o) { }
		});
		
		addComponentListener(new ComponentListener() {
			public void componentHidden(ComponentEvent e) { }
			public void componentMoved(ComponentEvent e) { }
			public void componentResized(ComponentEvent e) {
				ssComponent.setMaxWidth(scrollPane.getWidth());
				ssComponent.setNotUpToDate();
				ssComponent.repaint();
				}
			public void componentShown(ComponentEvent e) { }
		});
		getContentPane().add(mainPanel);
		setSize(800, 600);
		
		
	}

	public void jumpToTail() {
		System.out.println("ssComponent: " + ssComponent.getHeight());
		//this.scrollPane.getViewport().setViewPosition(new Point(this.scrollPane.getViewport().getViewPosition().x, ssComponent.getHeight()));
		System.out.println("jumpToTail and reapint");
		JScrollBar bar = this.scrollPane.getVerticalScrollBar();
		System.out.println("max: " + bar.getMaximum());
		bar.setValue(bar.getValue() + bar.getMaximum());
	}
}
