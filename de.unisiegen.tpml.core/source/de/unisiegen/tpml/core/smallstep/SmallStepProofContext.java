package de.unisiegen.tpml.core.smallstep ;


import de.unisiegen.tpml.core.ProofRule ;
import de.unisiegen.tpml.core.ProofStep ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.interpreters.Store ;


/**
 * Base interface to context related information and methods when determining
 * the next small steps in the implementation of the
 * {@link de.unisiegen.tpml.core.smallstep.AbstractSmallStepProofRuleSet}.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.smallstep.AbstractSmallStepProofRuleSet
 */
public interface SmallStepProofContext
{
  //
  // Accessors
  //
  /**
   * Returns the resulting expression. The resulting expression may be the same
   * as the initial expression if the evaluation got stuck at some point or the
   * proof is complete. During the evaluation in the
   * {@link AbstractSmallStepProofRuleSet} implementation this method will
   * always return the initial expression, and will return the resulting
   * expression only after the evaluation completed.
   * 
   * @return the resulting {@link Expression}.
   * @see Expression
   */
  public Expression getExpression ( ) ;


  /**
   * Returns the {@link ProofStep}s that were performed during the evaluation.
   * 
   * @return the evaluation steps.
   * @see ProofStep
   */
  public ProofStep [ ] getSteps ( ) ;


  /**
   * Returns the resulting store. The evaluation methods in the proof rule set
   * should modify the returned {@link Store} direly, i.e. to implement the
   * <tt>ref</tt> operator.
   * 
   * @return the resulting {@link Store}.
   * @see Store
   */
  public Store getStore ( ) ;


  //
  // Primitives
  //
  /**
   * Adds a new proof step with the specified <code>rule</code> for the given
   * <code>expression</code>.
   * 
   * @param rule the small step proof rule to add as proof step.
   * @param expression the {@link Expression} for the proof step.
   * @throws NullPointerException if <code>rule</code> or
   *           <code>expression</code> is <code>null</code>.
   * @see #getSteps()
   */
  public void addProofStep ( ProofRule rule , Expression expression ) ;
}
