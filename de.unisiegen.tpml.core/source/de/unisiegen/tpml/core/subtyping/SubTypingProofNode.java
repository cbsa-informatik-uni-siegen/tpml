package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.ExpressionProofNode;
import de.unisiegen.tpml.core.ProofRule;

/**
 * Base interface to nodes in a
 * {@link de.unisiegen.tpml.core.subtyping.SubTypingProofModel}.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.ProofNode
 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofModel
 */
public interface SubTypingProofNode extends SubTypingNode {
	/**
	 * Returns <code>true</code> if this node and all subnodes are finished. A
	 * node is finished if a {@link de.unisiegen.tpml.core.ProofRule} was applied
	 * and thereby a proper type is known.
	 * 
	 * @return <code>true</code> if finished.
	 */
	public boolean isFinished ( );

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.ProofNode#getChildAt(int)
	 */
	public SubTypingProofNode getChildAt ( int childIndex );

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.ProofNode#getLastLeaf()
	 */
	public SubTypingProofNode getLastLeaf ( );

	/**
	 * Convenience wrapper for the {@link ExpressionProofNode#getSteps()} method,
	 * which returns the <code>SubTypingProofRule</code> that was applied to
	 * this node or <code>null</code> if no rule was applied to this node so
	 * far.
	 * 
	 * @return the big step rule that was applied to this node, or
	 *         <code>null</code> if no rule was applied to this node.
	 * @see de.unisiegen.tpml.core.ProofStep
	 */
	public ProofRule getRule ( );
}
