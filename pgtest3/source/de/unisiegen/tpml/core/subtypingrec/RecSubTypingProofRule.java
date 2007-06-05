package de.unisiegen.tpml.core.subtypingrec;

import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;

/**
 * Base interface for the proof rules used within the subtyping algorithm. This interface is very similar to
 * the {@link de.unisiegen.tpml.core.bigstep.BigStepProofRule} used within the big step interpreter.
 *
 * @author Benjamin Mies
 *
 * @see de.unisiegen.tpml.core.ProofRule
 */
public interface RecSubTypingProofRule extends ProofRule {

	/**
	 * Applies this subtyping proof rule to the specified <code>node</code> via the given <code>context</code>.
	 * 
	 * @param context the subtyping proof context via which the application of this rule to the
	 *                <code>node</code> should be performed.
	 * @param node the subtyping proof node to which to apply this rule.
	 * 
	 * @throws NullPointerException if either <code>context</code> or <code>node</code> is <code>null</code>.                            
	 * @throws ProofRuleException if this rule cannot be applied to the <code>node</code>.
	 */
	public void apply ( DefaultRecSubTypingProofContext context,
			DefaultRecSubTypingProofNode node ) throws ProofRuleException;

}
