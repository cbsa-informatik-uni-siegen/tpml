package de.unisiegen.tpml.graphics.smallstep;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.graphics.ProofView;

public class SmallStepView extends JComponent implements ProofView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8529052541636149376L;
	
	private SmallStepComponent			component;
	
	private JScrollPane							scrollPane;

	
	public SmallStepView (SmallStepProofModel model) {
		super ();
		
		setLayout (new BorderLayout ());
		
		this.scrollPane = new JScrollPane ();
		this.component = new SmallStepComponent (model);
		
		add (this.scrollPane, BorderLayout.CENTER);
		
		this.scrollPane.setViewportView(this.component);
		this.scrollPane.getViewport().setBackground(Color.WHITE);
		
		this.scrollPane.addComponentListener(new ComponentAdapter () {
			@Override
			public void componentResized (ComponentEvent event) {
				SmallStepView.this.component.setAvailableWidth(SmallStepView.this.scrollPane.getViewport ().getWidth());
			}
		});
	}

	public void guess() throws IllegalStateException, ProofGuessException {
		this.component.guess ();
	}
	
	
}
