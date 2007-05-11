package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * TODO
 *
 * @author Benjamin Mies
 *
 */
public interface SubTypingProofNode extends ProofNode {
	
	public MonoType getType();
	
	public MonoType getType2();

}
