package de.unisiegen.tpml.graphics;

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
	
	
	
	//
	// Constructor (protected)
	//
	
	/**
	 * Allocates a new <code>AbstractProofView</code>.
	 */
	protected AbstractProofView() {
		super();
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
			firePropertyChange("advanced", oldAdvanced, advanced);
		}
	}
	
}
