package de.unisiegen.tpml.graphics.typechecker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JScrollPane;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.graphics.AbstractProofView;

/**
 * TODO Add documentation here.
 *
 * @author Marcell Fischbach
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 */
public class TypeCheckerView extends AbstractProofView {
	/**
	 * 
	 */
	private static final long serialVersionUID = -425214200136389228L;

	private TypeCheckerComponent	component;
	
	private JScrollPane						scrollPane;
	
	public TypeCheckerView (TypeCheckerProofModel model) {
		super ();
		
		setLayout (new BorderLayout ());
		
		this.scrollPane = new JScrollPane ();
		this.component = new TypeCheckerComponent (model);
		
		add (this.scrollPane, BorderLayout.CENTER);
		
		this.scrollPane.setViewportView(this.component);
		this.scrollPane.getViewport().setBackground(Color.WHITE);
		
		this.scrollPane.addComponentListener(new ComponentAdapter () {
			@Override
			public void componentResized (ComponentEvent event) {
				TypeCheckerView.this.component.setAvailableWidth(TypeCheckerView.this.scrollPane.getViewport ().getWidth());
			}
		});
	}
	
	public void guess () throws IllegalStateException, ProofGuessException {
		this.component.guess ();
	}
	
	public boolean wasGuessed () {
		return this.component.wasGuessed ();
	}
}
