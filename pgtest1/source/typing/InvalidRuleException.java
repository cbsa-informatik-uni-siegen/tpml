package typing;

/**
 * Thrown if the user tries to apply an invalid
 * type rule for a {@link ProofNode}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class InvalidRuleException extends Exception {
  /**
   * Allocates a new invalid rule exception.
   * 
   * @param node the {@link ProofNode}.
   * @param rule the {@link Rule} that could not be applied
   *             for <code>node</code>.
   */
  InvalidRuleException(ProofNode node, Rule rule) {
    this.node = node;
    this.rule = rule;
  }
  
  /**
   * Returns the {@link ProofNode}.
   * 
   * @return the proof node.
   */
  public ProofNode getNode() {
    return this.node;
  }
  
  /**
   * Returns the {@link Rule}.
   * 
   * @return the rule.
   */
  public Rule getRule() {
    return this.rule;
  }
  
  // member attributes
  private ProofNode node;
  private Rule rule;
  
  // the unique serialization id
  private static final long serialVersionUID = -6281452959751685702L;
}
