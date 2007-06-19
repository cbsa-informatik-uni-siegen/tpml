package de.unisiegen.tpml.core.minimaltyping;

import de.unisiegen.tpml.core.types.MonoType;

public interface MinimalTypingTypesProofNode extends MinimalTypingProofNode {
	
	/**
	 * 
	 * Get the second type of this node
	 *
	 * @return type second type of the node
	 */
	public MonoType getType2();

}
