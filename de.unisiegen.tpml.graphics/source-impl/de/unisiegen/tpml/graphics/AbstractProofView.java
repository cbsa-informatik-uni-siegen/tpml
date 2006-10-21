package de.unisiegen.tpml.graphics;

import javax.swing.JComponent;

import de.unisiegen.tpml.graphics.theme.Theme;
import de.unisiegen.tpml.graphics.theme.ThemeManager;
import de.unisiegen.tpml.graphics.theme.ThemeManagerListener;

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
	 * The global {@link ThemeManager} instance.
	 * 
	 * Used to repaint the proof view whenever the current theme changes.
	 */
	private ThemeManager themeManager = ThemeManager.get();
	
	
	
	//
	// Constructor (protected)
	//
	
	/**
	 * Allocates a new <code>AbstractProofView</code>.
	 */
	protected AbstractProofView() {
		super();
		
		// repaint the proof view whenever the current theme changes
		this.themeManager.addThemeManagerListener(new ThemeManagerListener() {
			public void currentThemeChanged(Theme theme) {
				repaint();
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
			firePropertyChange("advanced", oldAdvanced, advanced);
		}
	}
}
