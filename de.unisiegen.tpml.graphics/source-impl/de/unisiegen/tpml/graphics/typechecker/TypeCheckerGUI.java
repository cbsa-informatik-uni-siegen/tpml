package de.unisiegen.tpml.graphics.typechecker;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;

public class TypeCheckerGUI extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -425214200136389228L;

	private TypeCheckerComponent	component;
	
	private JScrollPane						scrollPane;
	
	public TypeCheckerGUI (TypeCheckerProofModel model) {
		super ();
		
		setLayout (new BorderLayout ());
		
		this.scrollPane = new JScrollPane ();
		this.component = new TypeCheckerComponent (model);
		
		add (scrollPane, BorderLayout.CENTER);
		
		this.scrollPane.setViewportView(component);
		
		this.scrollPane.addComponentListener(new ComponentAdapter () {
			public void componentResized (ComponentEvent event) {
				TypeCheckerGUI.this.component.setAvailableWidth(TypeCheckerGUI.this.scrollPane.getViewport ().getWidth());
			}
		});
	}
}
