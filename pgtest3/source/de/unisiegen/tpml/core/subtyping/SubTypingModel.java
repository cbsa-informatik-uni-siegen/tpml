package de.unisiegen.tpml.core.subtyping;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofModel;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;

public interface SubTypingModel extends ProofModel {

	ProofRule[] getRules ( );

	public void prove ( ProofRule rule, ProofNode proofNode )throws ProofRuleException;

	public void guess ( ProofNode proofNode ) throws ProofGuessException;

	public void complete ( ProofNode proofNode ) throws ProofGuessException;
	
	public SubTypingNode getRoot();

}
