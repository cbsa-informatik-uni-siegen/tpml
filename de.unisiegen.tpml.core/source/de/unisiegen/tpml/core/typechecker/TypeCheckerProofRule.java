package de.unisiegen.tpml.core.typechecker;


import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;


/**
 * Base interface for the proof rules used within the type checker. This
 * interface is very similar to the
 * {@link de.unisiegen.tpml.core.bigstep.BigStepProofRule} used within the big
 * step interpreter.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * @see de.unisiegen.tpml.core.ProofRule
 */
public interface TypeCheckerProofRule extends ProofRule
{

  //
  // Primitives
  //
  /**
   * Applies this type checker proof rule to the specified <code>node</code>
   * via the given <code>context</code>.
   * 
   * @param context the type checker proof context via which the application of
   *          this rule to the <code>node</code> should be performed.
   * @param node the type checker proof node to which to apply this rule.
   * @throws NullPointerException if either <code>context</code> or
   *           <code>node</code> is <code>null</code>.
   * @throws ProofRuleException if this rule cannot be applied to the
   *           <code>node</code>.
   */
  public void apply ( TypeCheckerProofContext context, TypeCheckerProofNode node )
      throws ProofRuleException;


  /**
   * Updates the specified <code>node</code> as part of a previous rule
   * application for <code>context</code>. This method is only interesting
   * for special rules like <b>(P-LET)</b>, that cannot be fully handled via
   * the unification algorithm.
   * 
   * @param context the main proof context, which was previously specified as
   *          parameter to an
   *          {@link #apply(TypeCheckerProofContext, TypeCheckerProofNode)}
   *          invokation on another proof node, possibly with another proof
   *          rule.
   * @param node the {@link TypeCheckerProofNode} that may need to be updated.
   * @throws NullPointerException if <code>context</code> or <code>node</code>
   *           is <code>null</code>.
   */
  public void update ( TypeCheckerProofContext context,
      TypeCheckerProofNode node );
}
