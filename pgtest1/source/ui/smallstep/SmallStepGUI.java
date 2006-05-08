package ui.smallstep;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import smallstep.SmallStepProofModel;
import smallstep.SmallStepProofModelFactory;


public class SmallStepGUI extends JDialog {
	
	private SmallStepView			view;
	
	private	JButton					guessStep;
	
	private SmallStepProofModel		model;
	

	public SmallStepGUI (JFrame owner, String title, boolean modal, String program) {
		super(owner, title, modal);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout ());
		view = new SmallStepView ();
		this.getContentPane().add(view, BorderLayout.CENTER);
		
		guessStep = new JButton ("Guess");
		this.getContentPane().add(guessStep, BorderLayout.SOUTH);
		
		
		SmallStepProofModelFactory sspmf = SmallStepProofModelFactory.newInstance();
		try {
			model = sspmf.newProofModel(program);
			view.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
			
			return;
		}
		
		this.guessStep.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				model.guess(view.getRootNode().getFirstLeaf());
			}
		});
		
		setSize (600, 800);
	}

	
}
