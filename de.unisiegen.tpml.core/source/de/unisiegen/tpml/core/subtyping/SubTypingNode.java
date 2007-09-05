package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.interfaces.ExpressionOrType;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * Interface for the proof node in the sub typing proof model. This interface is
 * used to realize sub typing and sub typing rec in one view.
 * 
 * @author Benjamin Mies
 */
public interface SubTypingNode extends ProofNode, ExpressionOrType {
	/**
	 * Get the first type of this node
	 * 
	 * @return MonoType the first type of this node
	 */
	public MonoType getLeft ( );

	/**
	 * Get the second type of this node
	 * 
	 * @return MonoType the second type of this node
	 */
	public MonoType getRight ( );

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.core.ProofNode#getLastLeaf()
	 */
	public SubTypingNode getLastLeaf ( );
}
