package ui.smallstep;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import smallstep.SmallStepProofModel;
import smallstep.SmallStepProofModelFactory;


public class SmallStepGUI extends JDialog {
	
	private SmallStepView			view;
	
	private	JButton					guessStep;
	
	private JScrollPane				scrollPane;
	
	private SmallStepProofModel		model;
	

	public SmallStepGUI (JFrame owner, String title, boolean modal, String program) {
		super(owner, title, modal);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout ());
		
		this.scrollPane = new JScrollPane();
		this.view = new SmallStepView ();
		this.scrollPane.getViewport().add(this.view);
		this.getContentPane().add(this.scrollPane, BorderLayout.CENTER);
		
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
				try {
					model.guess(view.getRootNode().getFirstLeaf());
				} catch (IllegalStateException exc) {
					JOptionPane.showMessageDialog(SmallStepGUI.this, exc.getMessage());
				}
			}
		});
		
		this.scrollPane.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent event) {
				view.setAvailableSize(new Dimension(scrollPane.getWidth() -
						scrollPane.getVerticalScrollBar().getWidth() * 2, scrollPane.getHeight()));
			}
		});
		
		this.scrollPane.setBackground(Color.WHITE);
		this.scrollPane.getViewport().setBackground(Color.WHITE);
		setSize (600, 800);
	}

	
}
