package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.interfaces.ExpressionOrType;
import de.unisiegen.tpml.core.types.MonoType;

public interface SubTypingNode extends ProofNode, ExpressionOrType {

	/**
	 * 
	 * get the first type of this node
	 *
	 * @return MonoType the first type of this node
	 */
	public MonoType getType ( );

	/**
	 * 
	 * get the second type of this node
	 *
	 * @return MonoType the second type of this node
	 */
	public MonoType getType2 ( );
	
	public SubTypingNode getLastLeaf();
}
