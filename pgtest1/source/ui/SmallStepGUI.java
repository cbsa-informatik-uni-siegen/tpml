package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;
import java.util.prefs.*;

import javax.swing.*;

public class SmallStepGUI extends JPanel {
	
	private SmallStepComponent 	ssComponent;
	private JButton				buttonAutocomplete;
	private JButton 			buttonNextStep;
	private JButton				buttonClose;
	private JButton				buttonSugar;
	private JButton				buttonRestart;
	private JSpinner			spinnerStep;
	private JScrollPane			scrollPane;
	
	
	public SmallStepGUI(Frame owner, String title, boolean modal, SmallStepModel model) {
		//super(owner, title, modal);
		this.setLayout (new BoxLayout (this, BoxLayout.PAGE_AXIS));
		
		JPanel mainPanel	= new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		// setting the "justAxioms" true just for testing. This should be taken from 
		// a settings dialog later
		ssComponent 			= new SmallStepComponent();
		buttonAutocomplete	= new JButton ("Autocomplete");
		buttonNextStep 		= new JButton ("NextStep");
		buttonClose    		= new JButton ("Close");
		buttonSugar			= new JButton ("Sugar");
		buttonRestart		= new JButton ("Restart");
		spinnerStep			= new JSpinner (new SpinnerNumberModel(1, 1, 20, 1));
		
		ssComponent.setModel(model);
		
		
		Preferences prefs = Preferences.userNodeForPackage(SmallStepGUI.class);
		ssComponent.setUnderlineExpressions(prefs.getBoolean("ssUnderlineExpressions", true));

		/*
		 * Here all the Settings from the Settings file
		 */
		prefs.addPreferenceChangeListener(new PreferenceChangeListener() {
			public void preferenceChange(PreferenceChangeEvent event) {
				if (event.getKey().equals("ssUnderlineExpressions")) {
					ssComponent.setUnderlineExpressions(event.getNewValue().equals("true"));
				}
			}
		});
		
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
		buttonPanel.add(buttonAutocomplete);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(spinnerStep);
		buttonPanel.add(buttonNextStep);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(buttonRestart);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(buttonClose);
		
		FontMetrics fm = getFontMetrics(spinnerStep.getFont());
		spinnerStep.setMaximumSize(new Dimension(fm.stringWidth("XXXX"), fm.getHeight()*2));
		mainPanel.add(buttonPanel);
		
		//TODO this is done realy dirty by the smallsteplistener itself.
//		buttonClose.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				//dispose ();
//			}
//		});
		buttonNextStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int num = (Integer)spinnerStep.getValue();
				ssComponent.getModel().completeSteps(num);
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
				
		buttonRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleRestart();
			}
		});
	
		/*
		 * This will add the listener to the model; after a new on is created,
		 * the new one is not needed but its not importend, that there are two
		 * The first will be removed, so doesn't matter at all
		 */
		handleRestart();
		
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
		this.add(mainPanel);
		//setSize(1024, 600);
		
	}
	
	public void handleRestart() {
		SmallStepModel model = new SmallStepModel(ssComponent.getModel().getOrigExpression());
		model.setFont(ssComponent.getModel().getOrigFont());
		ssComponent.setModel(model);
		ssComponent.setNotUpToDate();
		ssComponent.repaint();
		buttonSugar.setEnabled(true);

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
	}

	public void jumpToTail() {
		//this.scrollPane.getViewport().setViewPosition(new Point(this.scrollPane.getViewport().getViewPosition().x, ssComponent.getHeight()));
		JScrollBar bar = this.scrollPane.getVerticalScrollBar();
		bar.setValue(bar.getValue() + bar.getMaximum());
	}

	public JButton getButtonClose() {
		return buttonClose;
	}
	
}
