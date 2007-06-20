package de.unisiegen.tpml.core.languages.l2;

import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l1.L1MinimalTypingProofRuleSet;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingExpressionProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofContext;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode;
import de.unisiegen.tpml.core.minimaltyping.TypeEnvironment;

/**
 * The minimal type proof rules for the <code>L1</code> language.
 * 
 * @author Benjamin Mies
 * 
 * @see de.unisiegen.tpml.core.minimaltyping.AbstractMinimalTypingProofRuleSet
 */
public class L2MinimalTypingProofRuleSet extends
 L1MinimalTypingProofRuleSet {
	/**
	 * Allocates a new <code>L1MinimalTypingProofRuleSet</code> for the specified
	 * <code>language</code>.
	 * 
	 * @param language the <code>L1</code> or a derived language.
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L2MinimalTypingProofRuleSet ( L1Language language, boolean mode ) {
		super ( language, mode );
		// register the type rules
		registerByMethodName ( L2Language.L2,"REC", "applyRec", "updateRec" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	}

	/**
	 * Applies the <b>(REC)</b> rule to the
	 * <code>node</node> using the <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the minimal typing proof node.
	 */
	public void applyRec ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = (MinimalTypingExpressionProofNode) pNode;
		Recursion rec = (Recursion) node.getExpression ( );
		TypeEnvironment environment = node.getEnvironment ( );
		// check if the user entered a type
		
		if (rec.getTau ( )== null)
			throw new RuntimeException("Please enter type for " +rec.toString ( ));
		context.addProofNode ( node, environment.extend ( rec.getId ( ), rec.getTau ( ) ), rec.getE ( ) );
	}
	
	  /**
	   * Updates the <code>node</code> to which <b>(REC)</b> was applied
	   * previously.
	   * 
	   * @param context the minimal typing proof context.
	   * @param pNode the node to update according to <b>(REC)</b>.
	   */
	public void updateRec ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = (MinimalTypingExpressionProofNode) pNode;
		Recursion rec = (Recursion) node.getExpression ( );
	
		if ( node.getChildCount ( ) == 1 && node.getFirstChild ( ).isFinished ( ) ) {
			context.addProofNode ( node, node.getFirstChild ( ).getType ( ), rec.getTau ( ) );
		}
		
		if ( node.getChildCount ( ) == 2 && node.isFinished ( ) ) {
			context.setNodeType ( node, rec.getTau ( ) );
		}
	}

}
