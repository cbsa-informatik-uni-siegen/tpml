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
   * This default implementation simply throws <code>ProofRuleException</code>,
   * so derived classes will need to override this method to provide the
   * proper handling, but remember to verify the expression of the <code>node</code>
   * first.
   * 
   * @param context the big step proof context via which the application
   *                of this rule to the <code>node</code> should be
   *                performed.
   * @param node the big step proof node to which to apply this rule.
   * 
   * @throws NullPointerException if either <code>context</code> or
   *                              <code>node</code> is <code>null</code>.                            
   * @throws ProofRuleException if this rule cannot be applied to the
   *                            <code>node</code>.
   */
  public void apply(BigStepProofContext context, BigStepProofNode node) throws ProofRuleException {
    if (context == null) {
      throw new NullPointerException("context is null");
    }
    if (node == null) {
      throw new NullPointerException("node is null");
    }
    throw new ProofRuleException(node, this);
  }
  
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
}
