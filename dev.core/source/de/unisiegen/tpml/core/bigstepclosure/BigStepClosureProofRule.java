package de.unisiegen.tpml.core.bigstepclosure;

import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;


/**
 * TODO
 *
 */
public interface BigStepClosureProofRule extends ProofRule
{
  public void apply ( BigStepClosureProofContext context, BigStepClosureProofNode node )
    throws ProofRuleException;

  public void update ( BigStepClosureProofContext context, BigStepClosureProofNode node );
}
