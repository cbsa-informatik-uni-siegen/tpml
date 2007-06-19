package de.unisiegen.tpml.core.languages.l3;

import de.unisiegen.tpml.core.expressions.Tuple;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.languages.l2.L2MinimalTypingProofRuleSet;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingExpressionProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofContext;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TupleType;

/**
 * The minimal type proof rules for the <code>L1</code> language.
 * 
 * @author Benjamin Mies
 * 
 * @see de.unisiegen.tpml.core.minimaltyping.AbstractMinimalTypingProofRuleSet
 */
public class L3MinimalTypingProofRuleSet extends L2MinimalTypingProofRuleSet {
	/**
	 * Allocates a new <code>L1MinimalTypingProofRuleSet</code> for the specified
	 * <code>language</code>.
	 * 
	 * @param language the <code>L1</code> or a derived language.
	 * @throws NullPointerException if <code>language</code> is
	 *           <code>null</code>.
	 */
	public L3MinimalTypingProofRuleSet ( L1Language language ) {
		super ( language );
		// register the type rules
		registerByMethodName ( L2Language.L2,
				"TUPLE", "applyTuple", "updateTuple" );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	}

	/**
	 * Applies the <b>(TUPLE)</b> rule to the
	 * <code>node</node> using the <code>context</code>.
	 * 
	 * @param context the minimal typing proof context.
	 * @param pNode the minimal typing proof node.
	 */
	public void applyTuple ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Tuple tuple = ( Tuple ) node.getExpression ( );

		context.addProofNode ( node, node.getEnvironment ( ), tuple
				.getExpressions ( )[0] );
	}

	  /**
	   * Updates the <code>node</code> to which <b>(TUPLE)</b> was applied
	   * previously.
	   * 
	   * @param context the minimal typing proof context.
	   * @param pNode the node to update according to <b>(TUPLE)</b>.
	   */
	public void updateTuple ( MinimalTypingProofContext context,
			MinimalTypingProofNode pNode ) {
		MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode;
		Tuple tuple = ( Tuple ) node.getExpression ( );

		if ( node.getLastChild ( ).isFinished ( ) ) {

			if ( node.getChildCount ( ) == tuple.getExpressions ( ).length ) {
				MonoType [] types = new MonoType[node.getChildCount ( )];
				for (int i = 0; i< node.getChildCount ( ); i++){
					types[i]= node.getChildAt ( i ).getType ( );
				}
				TupleType type = new TupleType(types);
				context.setNodeType ( node, type );
				return;
			}
				// add next child
				context.addProofNode ( node, node.getEnvironment ( ), tuple
						.getExpressions ( )[node.getChildCount ( )] );
		}
	}

}
