package de.unisiegen.tpml.graphics;

import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

/**
 * Abstract base class for all {@link de.unisiegen.tpml.graphics.ProofView}s.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.graphics.ProofView
 */
public abstract class AbstractProofView extends JComponent implements ProofView {
	//
	// Attributes
	//
	
	/**
	 * Whether to operate in advanced or beginner mode.
	 * 
	 * @see #isAdvanced()
	 * @see #setAdvanced(boolean)
	 */
	private boolean advanced;
	
	/**
	 * The currently active {@link Theme}.
	 * 
	 * Used to repaint the proof view whenever the current theme changes.
	 */
	private Theme theme = Theme.currentTheme();
	
	
	
	//
	// Constructor (protected)
	//
	
	/**
	 * Allocates a new <code>AbstractProofView</code>.
	 */
	protected AbstractProofView() {
		super();
		
		// repaint the proof view whenever the current theme changes
		this.theme.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(java.beans.PropertyChangeEvent evt) {
				if (evt.getPropertyName().endsWith("Color")) {
					repaint();
				}
			}
		});
	}
	
	
	
	//
	// Accessors
	//
	
	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.graphics.ProofView#isAdvanced()
	 */
	public boolean isAdvanced() {
		return this.advanced;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.graphics.ProofView#setAdvanced(boolean)
	 */
	public void setAdvanced(boolean advanced) {
		if (this.advanced != advanced) {
			boolean oldAdvanced = this.advanced;
			this.advanced = advanced;
			//TODO Testausgaben
			//  System.out.println("Advaced ändert sich (AbstractProofView)");
			firePropertyChange("advanced", oldAdvanced, advanced);
		}
	}
}
