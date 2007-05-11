/**
 * TODO
 */
package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * TODO
 *
 * @author Benjamin Mies
 *
 */
public class DefaultSubTypingProofContext implements SubTypingProofContext {
	
	private SubTypingProofModel model;
	
	private DefaultSubTypingProofNode node;
	
	
	public DefaultSubTypingProofContext(SubTypingProofModel pModel, SubTypingProofNode pNode){
		model = pModel;
		node = (DefaultSubTypingProofNode) pNode;
	}


	public void apply ( SubTypingProofRule rule, DefaultSubTypingProofNode pNode ) throws ProofRuleException {
		
		model.contextSetProofNodeRule ( this, pNode, rule );
//	 try to apply the rule to the node
		rule.apply ( this, pNode );
	}


	public void addProofNode ( SubTypingProofNode pNode, MonoType type, MonoType type2 ) {
		model.contextAddProofNode ( this, pNode, type, type2 );
		
	}
	
}
