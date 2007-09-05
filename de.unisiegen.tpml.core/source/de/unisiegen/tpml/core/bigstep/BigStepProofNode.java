package de.unisiegen.tpml.core.bigstep;

import javax.swing.tree.TreeNode;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.InterpreterProofNode;

/**
 * Base interface to nodes in a
 * {@link de.unisiegen.tpml.core.bigstep.BigStepProofModel}.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.interpreters.InterpreterProofNode
 */
public interface BigStepProofNode extends InterpreterProofNode {
	//
	// Accessors
	//
	/**
	 * Returns <code>true</code> if the proof for the sub tree started by this
	 * node is finished, otherwise <code>false</code> is returned to indicate
	 * that one or more child nodes are still not proven.
	 * 
	 * @return <code>true</code> if the proof for the sub tree, whose root is
	 *         this node, is finished.
	 * @see ProofNode#isProven()
	 */
	public boolean isFinished ( );

	/**
	 * Returns the result of the evaluation of the expression at this node, which
	 * is <code>null</code> until the node is proven. Once the node is proven,
	 * this may be either a value with a store or an exception with a store.
	 * 
	 * @return the result for this node or <code>null</code>.
	 * @see Expression#isException()
	 * @see Expression#isValue()
	 */
	public BigStepProofResult getResult ( );

	/**
	 * Convenience wrapper for the {@link ProofNode} method, which returns the
	 * <code>BigStepProofRule</code> that was applied to this node or
	 * <code>null</code> if no rule was applied to this node so far.
	 * 
	 * @return the big step rule that was applied to this node, or
	 *         <code>null</code> if no rule was applied to this node.
	 * @see BigStepProofRule
	 * @see de.unisiegen.tpml.core.ProofStep
	 */
	public BigStepProofRule getRule ( );

	//
	// Primitives
	//
	/**
	 * {@inheritDoc}
	 * 
	 * @see ProofNode#getChildAt(int)
	 */
	public BigStepProofNode getChildAt ( int n );

	/**
	 * {@inheritDoc}
	 * 
	 * @see ProofNode#getParent()
	 */
	public BigStepProofNode getParent ( );

	//
	// Tree Queries
	//
	/**
	 * {@inheritDoc}
	 * 
	 * @see ProofNode#getRoot()
	 */
	public BigStepProofNode getRoot ( );

	//
	// Child Queries
	//
	/**
	 * {@inheritDoc}
	 * 
	 * @see ProofNode#getFirstChild()
	 */
	public BigStepProofNode getFirstChild ( );

	/**
	 * {@inheritDoc}
	 * 
	 * @see ProofNode#getLastChild()
	 */
	public BigStepProofNode getLastChild ( );

	/**
	 * {@inheritDoc}
	 * 
	 * @see ProofNode#getChildAfter(javax.swing.tree.TreeNode)
	 */
	public BigStepProofNode getChildAfter ( TreeNode aChild );

	/**
	 * {@inheritDoc}
	 * 
	 * @see ProofNode#getChildBefore(javax.swing.tree.TreeNode)
	 */
	public BigStepProofNode getChildBefore ( TreeNode aChild );

	//
	// Leaf Queries
	//
	/**
	 * {@inheritDoc}
	 * 
	 * @see ProofNode#getFirstLeaf()
	 */
	public BigStepProofNode getFirstLeaf ( );

	/**
	 * {@inheritDoc}
	 * 
	 * @see ProofNode#getLastLeaf()
	 */
	public BigStepProofNode getLastLeaf ( );
}
