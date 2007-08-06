package de.unisiegen.tpml.graphics;

import javax.swing.JComponent;

import de.unisiegen.tpml.core.ProofGuessException;

/**
 * Base interface to the proof views that operate on the {@link de.unisiegen.tpml.core.ProofModel}s
 * and display them to the user.
 * 
 * New proof views are allocated from the {@link de.unisiegen.tpml.graphics.ProofViewFactory} class.
 * 
 * @author Marcell Fischbach
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.ProofModel
 * @see de.unisiegen.tpml.graphics.ProofViewFactory
 */
public interface ProofView {
	//
	// Accessors
	//
	
	/**
	 * Returns <code>true</code> if the proof view is in advanced mode. This affects certains
	 * parts of the view behavior. For example, for the small step proof view, the rule menu
	 * displays only axiom rules.
	 * 
	 * @return <code>true</code> if advanced mode is being used.
	 * 
	 * @see #setAdvanced(boolean)
	 */
	public boolean isAdvanced();
	
	/**
	 * If <code>advanced</code> is <code>true</code>, the proof view will operate in advanced
	 * mode, otherwise in beginner mode.
	 * 
	 * @param advanced <code>true</code> to operate in advanced mode, <code>false</code> to
	 * 								 operate in beginner mode.
	 * 
	 * @see #isAdvanced()
	 */
	public void setAdvanced(boolean advanced);
	
	
	
	//
	// Primitives
	//

	/**
	 * Determines the first unproven node and tries to guess a proof rule to apply
	 * to that node.
	 * 
	 * @throws ProofGuessException if the proof model failes to guess the next rule.
	 */
	public void guess() throws IllegalStateException, ProofGuessException;
	
	/**
	 * Returns the part of the ProofView that shall be printed
	 * @return the JComponent to be printed
	 */
	public JComponent getPrintPart();
}
