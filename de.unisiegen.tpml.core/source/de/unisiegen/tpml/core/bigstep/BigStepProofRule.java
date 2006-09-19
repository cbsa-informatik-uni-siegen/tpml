package de.unisiegen.tpml.core.bigstep;

import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;

/**
 * Base interface for the proof rules used within the big step interpreter. This interface is very similar
 * to the {@link de.unisiegen.tpml.core.typechecker.TypeCheckerProofRule} used within the type checker.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.ProofRule
 */
public interface BigStepProofRule extends ProofRule {
  //
  // Primitives
  //
  
  /**
   * Applies this big step proof rule to the specified <code>node</code> via the given
   * <code>context</code>.
   * 
   * @param context the big step proof context via which the application of this rule to the
   *                <code>node</code> should be performed.
   * @param node the big step proof node to which to apply this rule.
   * 
   * @throws NullPointerException if either <code>context</code> or <code>node</code> is <code>null</code>.                            
   * @throws ProofRuleException if this rule cannot be applied to the <code>node</code>.
   */
  public void apply(BigStepProofContext context, BigStepProofNode node) throws ProofRuleException;
  
  /**
   * Updates the specified <code>node</code> as part of a previous rule application for
   * <code>context</code>. This method is only interesting for non-axiom rules, like
   * <b>(APP)</b> or <b>(LET)</b>, that need to update their created proof nodes even after
   * applications of other proof rules to subtrees.
   * 
   * @param context the main proof context, which was previously specified as parameter to an
   *                {@link #apply(BigStepProofContext, BigStepProofNode)} invokation on another
   *                proof node, possibly with another proof rule.
   * @param node the {@link BigStepProofNode} that may need to be updated.
   * 
   * @throws NullPointerException if <code>context</code> or <code>node</code> is <code>null</code>.
   */
  public void update(BigStepProofContext context, BigStepProofNode node);
}
