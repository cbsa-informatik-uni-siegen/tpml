
package de.unisiegen.tpml.core.minimaltyping;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.AbstractExpressionProofNode;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.expressions.Expression;

/**
 * Default implementation of the <code>MinimalTypingProofNode</code> interface. The class for nodes
 * in a {@link de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel}.
 *
 * @author Benedikt Meurer
 * @version $Rev:878M $
 *
 * @see de.unisiegen.tpml.core.AbstractExpressionProofNode
 * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode
 */
abstract class AbstractMinimalTypingProofNode extends AbstractExpressionProofNode implements MinimalTypingProofNode {

	//
	// Constructor (package)
	//
	/**
	 * Allocates a new <code>DefaultMinimalTypingProofContext</code>.
	 * 
	 * @param pExpression the expression for this proof node
	 */
	protected AbstractMinimalTypingProofNode ( Expression pExpression ) {
		super ( pExpression );
	}

	//
	// Accessors
	//

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAfter(javax.swing.tree.TreeNode)
	 */
	@Override
	public AbstractMinimalTypingProofNode getChildAfter ( TreeNode aChild ) {
		return ( AbstractMinimalTypingProofNode ) super.getChildAfter ( aChild );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildAt(int)
	 */
	@Override
	public AbstractMinimalTypingProofNode getChildAt ( int childIndex ) {
		return ( AbstractMinimalTypingProofNode ) super.getChildAt ( childIndex );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getChildBefore(javax.swing.tree.TreeNode)
	 */
	@Override
	public AbstractMinimalTypingProofNode getChildBefore ( TreeNode aChild ) {
		return ( AbstractMinimalTypingProofNode ) super.getChildBefore ( aChild );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getFirstChild()
	 */
	@Override
	public AbstractMinimalTypingProofNode getFirstChild ( ) {
		return ( AbstractMinimalTypingProofNode ) super.getFirstChild ( );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getFirstLeaf()
	 */
	@Override
	public AbstractMinimalTypingProofNode getFirstLeaf ( ) {
		return ( AbstractMinimalTypingProofNode ) super.getFirstLeaf ( );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastChild()
	 */
	@Override
	public AbstractMinimalTypingProofNode getLastChild ( ) {
		return ( AbstractMinimalTypingProofNode ) super.getLastChild ( );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getLastLeaf()
	 */
	@Override
	public AbstractMinimalTypingProofNode getLastLeaf ( ) {
		return ( AbstractMinimalTypingProofNode ) super.getLastLeaf ( );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getParent()
	 */
	@Override
	public AbstractMinimalTypingProofNode getParent ( ) {
		return ( AbstractMinimalTypingProofNode ) super.getParent ( );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofNode#getRoot()
	 */
	@Override
	public AbstractMinimalTypingProofNode getRoot ( ) {
		return ( AbstractMinimalTypingProofNode ) super.getRoot ( );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode#getRule()
	 */
	public MinimalTypingProofRule getRule ( ) {
		ProofStep[] steps = getSteps ( );
		if ( steps.length > 0 ) {
			return ( MinimalTypingProofRule ) steps[0].getRule ( );
		}
		return null;
	}

	//
	// Leaf Queries
	//

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode#isFinished()
	 */
	public boolean isFinished ( ) {
		if ( !isProven ( ) ) {
			return false;
		}
		for ( int n = 0; n < getChildCount ( ); ++n ) {
			if ( !getChildAt ( n ).isFinished ( ) ) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.core.ProofNode#isProven()
	 */
	public boolean isProven ( ) {
		return ( getSteps ( ).length > 0 );
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	abstract public String toString ( );

}
