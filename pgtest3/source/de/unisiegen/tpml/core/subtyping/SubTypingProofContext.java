/**
 * TODO
 */
package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.types.MonoType;

/**
 * TODO
 *
 * @author Benjamin Mies
 *
 */
public interface SubTypingProofContext {
	
	public void addProofNode(SubTypingProofNode node, MonoType type, MonoType type2);

}
