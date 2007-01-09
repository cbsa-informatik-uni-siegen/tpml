/**
 * 
 */
package de.unisiegen.tpml.core.typeinference;

import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;


/**
 * @author Benjamin Mies
 *
 */
public interface TypeInferenceProofRule extends ProofRule {
	
	 public void apply(TypeInferenceProofContext context, TypeInferenceProofNode node) throws ProofRuleException;
	 
	 
	 public void update(TypeInferenceProofContext context, TypeInferenceProofNode node);

}
