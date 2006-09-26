package de.unisiegen.tpml.graphics.bigstep;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.graphics.ProofView;

public class BigStepView extends JComponent implements ProofView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8529052541636149376L;
	
	private BigStepComponent			component;
	
	private JScrollPane						scrollPane;

	
	public BigStepView (BigStepProofModel model) {
		super ();
		
		setLayout (new BorderLayout ());
		
		this.scrollPane = new JScrollPane ();
		this.component = new BigStepComponent (model);
		
		add (scrollPane, BorderLayout.CENTER);
		
		this.scrollPane.setViewportView(component);
		this.scrollPane.getViewport().setBackground(Color.WHITE);
		
		this.scrollPane.addComponentListener(new ComponentAdapter () {
			public void componentResized (ComponentEvent event) {
				BigStepView.this.component.setAvailableWidth(BigStepView.this.scrollPane.getViewport ().getWidth());
			}
		});
	}

	public void guess() throws IllegalStateException, ProofGuessException {
		this.component.guess ();
	}
	
	
}
