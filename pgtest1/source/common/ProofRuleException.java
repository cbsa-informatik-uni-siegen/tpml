package common;

/**
 * This exception is thrown when an trying to apply a
 * rule that cannot be applied to a node.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ProofRuleException extends Exception {
  /**
   * The serial version id. 
   */
  private static final long serialVersionUID = -765882201403684253L;
  
  /**
   * The node to which the rule was about to be applied.
   * 
   * @see #getNode()
   */
  private ProofNode node;
  
  /**
   * The rule that turned out to be invalid.
   * 
   * @see #getRule()
   */
  private ProofRule rule;
  
  

  //
  // Constructor
  //
  
  /**
   * Allocates a new {@link ProofRuleException} telling that
   * the specified <code>rule</code> could not be applied to
   * the given <code>node</code>.
   * 
   * @param node the {@link ProofNode}.
   * @param rule the {@link ProofRule} that could not be
   *             applied to the <code>node</code>.
   */
  public ProofRuleException(ProofNode node, ProofRule rule) {
    super("Cannot apply " + rule + " to " + node);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns the node on which a rule application failed.
   * 
   * @return the node on which a rule application failed.
   */
  public ProofNode getNode() {
    return this.node;
  }
  
  /**
   * Returns the rule that failed to apply.
   * 
   * @return the rule that failed to apply.
   */
  public ProofRule getRule() {
    return this.rule;
  }
}
