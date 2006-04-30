package common;

import expressions.Expression;

/**
 * A proof step represents an application of a {@link common.ProofRule}
 * to an {@link expressions.Expression}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ProofStep {
  /**
   * The {@link Expression} to which the {@link ProofRule} was
   * applied.
   * 
   * @see #getExpression()
   */
  protected Expression expression;
  
  /**
   * The {@link ProofRule} that was applied to an {@link Expression}
   * to advance in the proof.
   * 
   * @see #getRule()
   */
  protected ProofRule rule;
  

  
  //
  // Primitives
  //

  /**
   * Returns the {@link Expression} to which the {@link ProofRule}
   * was applied to advance in the proof.
   * 
   * @return the expression.
   */
  public Expression getExpression() {
    return this.expression;
  }
  
  /**
   * Returns the {@link ProofRule} that was applied to an
   * {@link Expression} to advance in the proof.
   * 
   * @return the proof rule that was applied in this step.
   * 
   * @see #getExpression()
   */
  public ProofRule getRule() {
    return this.rule;
  }
}
