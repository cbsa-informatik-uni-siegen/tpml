package de.unisiegen.tpml.core;

import de.unisiegen.tpml.core.expressions.Expression;

/**
 * A proof step represents an application of a {@link de.unisiegen.tpml.core.ProofRule} to an
 * {@link de.unisiegen.tpml.core.expressions.Expression}.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.expressions.Expression
 * @see de.unisiegen.tpml.core.ProofRule
 */
public final class ProofStep {
  //
  // Attributes
  //
  
  /**
   * The {@link Expression} to which the {@link ProofRule} was applied.
   * 
   * @see #getExpression()
   */
  private Expression expression;
  
  /**
   * The {@link ProofRule} that was applied to an {@link Expression} to advance in the proof.
   * 
   * @see #getRule()
   */
  private ProofRule rule;
  

  
  //
  // Constructor
  //
  
  /**
   * Allocates a new proof step with the given <code>expression</code> and the specified <code>rule</code>.
   * 
   * @param expression the {@link Expression}.
   * @param rule the {@link ProofRule} that was applied.
   * 
   * @throws NullPointerException if <code>expression</code> or <code>rule</code> is <code>null</code>.
   */
  public ProofStep(Expression expression, ProofRule rule) {
    if (rule == null) {
      throw new NullPointerException("rule is null");
    }
    if (expression == null) {
      throw new NullPointerException("expression is null");
    }
    this.expression = expression;
    this.rule = rule;
  }
  
  
  
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
  
  
  
  //
  // Base methods
  //
  
  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ProofStep) {
      ProofStep other = (ProofStep)obj;
      return (this.expression.equals(other.expression) || this.rule.equals(other.rule));
    }
    return false;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return this.expression.hashCode() + this.rule.hashCode();
  }
}
