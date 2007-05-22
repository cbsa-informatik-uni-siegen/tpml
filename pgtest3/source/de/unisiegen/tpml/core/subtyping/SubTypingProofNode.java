package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * Base interface to nodes in a {@link de.unisiegen.tpml.core.subtyping.SubTypingProofModel}.
 *
 * @author Benjamin Mies
 *
 * @see de.unisiegen.tpml.core.ProofNode
 * @see de.unisiegen.tpml.core.subtyping.SubTypingProofModel
 */
public interface SubTypingProofNode extends ProofNode {
	
	/**
	 * 
	 * get the first type of this node
	 *
	 * @return MonoType the first type of this node
	 */
	public MonoType getType();
	
	/**
	 * 
	 * get the second type of this node
	 *
	 * @return MonoType the second type of this node
	 */
	public MonoType getType2();
	
  /**
   * Returns <code>true</code> if this node and all subnodes are finished. A node is finished if
   * a {@link de.unisiegen.tpml.core.ProofRule} was applied and thereby a proper type is known.
   * 
   * @return <code>true</code> if finished.
   */
	public boolean isFinished();
	 
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofNode#getChildAt(int)
   */
	public ProofNode getChildAt(int childIndex);

}
