package de.unisiegen.tpml.graphics.bigstep;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JScrollPane;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.graphics.AbstractProofView;

/**
 * TODO Add documentation here.
 *
 * @author Marcell Fischbach
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 */
public class BigStepView extends AbstractProofView {

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
		
		add (this.scrollPane, BorderLayout.CENTER);
		
		this.scrollPane.setViewportView(this.component);
		this.scrollPane.getViewport().setBackground(Color.WHITE);
		
		this.scrollPane.addComponentListener(new ComponentAdapter () {
			@Override
			public void componentResized (ComponentEvent event) {
				BigStepView.this.component.setAvailableWidth(BigStepView.this.scrollPane.getViewport ().getWidth());
			}
		});
	}

	/**
	 * Guesses the first node with the tree that is not already prooven.
	 */
	public void guess() throws IllegalStateException, ProofGuessException {
		this.component.guess ();
	}
	
	public boolean wasGuessed () {
		return this.component.wasGuessed();
	}
}
