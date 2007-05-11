package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.types.MonoType;

public interface SubTypingProofRule extends ProofRule {
	
	public void update(SubTypingProofContext context, SubTypingProofNode node);

	public void apply ( DefaultSubTypingProofContext context, DefaultSubTypingProofNode node ) throws ProofRuleException;



}
