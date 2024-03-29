package bigstep;

import common.ProofRule;
import common.ProofRuleException;

/**
 * Abstract base class for big step proof rules.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class BigStepProofRule extends ProofRule {
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>BigStepProofRule</code> with
   * the specified <code>name</code>. If <code>axiom</code>
   * is <code>true</code> the rule has no premises.
   * 
   * @param axiom <code>true</code> if the rule is an axiom.
   * @param name the name of the rule.
   */
  protected BigStepProofRule(boolean axiom, String name) {
    super(axiom, name);
  }
  
  

  //
  // Primitives
  //
  
  /**
   * Applies this big step proof rule to the specified <code>node</code>
   * via the given <code>context</code>.
   * 
   * @param context the big step proof context via which the application
   *                of this rule to the <code>node</code> should be
   *                performed.
   * @param node the big step proof node to which to apply this rule.
   * @throws ProofRuleException if this rule cannot be applied to the
   *                            <code>node</code>.
   * @throws ClassCastException TODO
   * @throws ClassCastException if the <code>node</code>s expression is
   *                            an instance of an unsupported class or
   *                            a similar error condition. Will be
   *                            automatically converted into a
   *                            {@link ProofRuleException}.
   * @throws NullPointerException if either <code>context</code> or
   *                              <code>node</code> is <code>null</code>.                            
   */
  public abstract void apply(BigStepProofContext context, BigStepProofNode node) throws ProofRuleException, ClassCastException;
  
  /**
   * Updates the specified <code>node</code> as part of a previous rule
   * application for <code>context</code>. This method is only interesting
   * for non-axiom rules, like (APP) or (LET), that need to update their
   * created proof nodes even after applications of other proof rules to
   * subtrees.
   * 
   * This method is only invoked for proof nodes that are not already
   * proven (see {@link common.ProofNode#isProven()}). If <code>node</code>
   * is proven, this represents a bug in the big step proof model logic.
   * 
   * @param context the main proof context, which was previously specified
   *                as parameter to an {@link #apply(BigStepProofContext, BigStepProofNode)}
   *                invokation on another proof node, possibly with another
   *                proof rule.
   * @param node the {@link BigStepProofNode} that may need to be updated.
   */
  public void update(BigStepProofContext context, BigStepProofNode node) {
    // Nothing to do here, derived classes may implement this
    // method if required, for example the (LET) rule
  }
  
  
  
  //
  // Rule Allocation
  //
  
  /**
   * Allocates a new <code>BigStepProofRule</code> with the
   * specified <code>name</code>, that does nothing, meaning
   * that {@link #apply(BigStepProofContext, BigStepProofNode)}
   * and {@link #update(BigStepProofContext, BigStepProofNode)}
   * are noops.
   * 
   * @param name the name of the rule to allocate.
   * 
   * @return a newly allocated <code>BigStepProofRule</code>
   *         with the specified <code>name</code>, that does
   *         nothing.
   *         
   * @see #toExnRule(int)
   */
  protected BigStepProofRule newNoopRule(String name) {
    return new BigStepProofRule(false, name) {
      public void apply(BigStepProofContext context, BigStepProofNode node) throws ProofRuleException, ClassCastException {
        throw new ProofRuleException(node, this);
      }
    };
  }
  
  /**
   * Translates this big step proof rule to an appropriate
   * exception rule, with the given sub node index <code>n</code>.
   * For example, for <b>(APP)</b>, this generates <b>(APP-EXN-n)</b>.
   * 
   * @param n the index of the sub node, starting at <code>0</code>.
   * 
   * @return the new {@link BigStepProofRule} for the exception.
   * 
   * @throws IllegalArgumentException if <code>n</code> is negative.
   * @throws IllegalStateException if this rule is an axiom, for which
   *                               no exception rule can be generated.
   */
  BigStepProofRule toExnRule(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("n is negative");
    }
    if (isAxiom()) {
      throw new IllegalStateException("rule is an axiom");
    }
    return newNoopRule(getName() + "-EXN-" + (n + 1));
  }
}
