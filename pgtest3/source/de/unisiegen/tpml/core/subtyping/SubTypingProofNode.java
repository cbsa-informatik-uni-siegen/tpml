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
	 
	public ProofNode getChildAt(int childIndex);

}
