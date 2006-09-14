package de.unisiegen.tpml.core;

/**
 * This exception is thrown when an attempt to apply a
 * rule that cannot be applied to a node.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.ProofGuessException
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
   *              
   * @throws NullPointerException if <code>node</code> or <code>rule</code> is <code>null</code>.
   */
  public ProofRuleException(ProofNode node, ProofRule rule) {
    this(node, rule, null);
  }
  
  /**
   * Allocates a new {@link ProofRuleException} telling that
   * the specified <code>rule</code> could not be applied to
   * the given <code>node</code>.
   * 
   * @param node the {@link ProofNode}.
   * @param rule the {@link ProofRule} that could not be
   *             applied to the <code>node</code>.
   * @param cause the cause, which is saved for later retrieval by the
   *              {@link Throwable#getCause()} method. A <code>null</code>
   *              value is permitted, and indicates that the cause is
   *              nonexistent or unknown.
   *              
   * @throws NullPointerException if <code>node</code> or <code>rule</code> is <code>null</code>.
   */
  public ProofRuleException(ProofNode node, ProofRule rule, Throwable cause) {
    super("Cannot apply " + rule + " to " + node, cause);
    if (node == null) {
      throw new NullPointerException("node is null");
    }
    if (rule == null) {
      throw new NullPointerException("rule is null");
    }
    this.node = node;
    this.rule = rule;
  }
  
  
  
  //
  // Accessors
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
