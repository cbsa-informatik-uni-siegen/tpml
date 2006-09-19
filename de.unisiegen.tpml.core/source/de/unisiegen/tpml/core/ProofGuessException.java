package de.unisiegen.tpml.core;

/**
 * This exception is thrown when an attempt to guess the next proof rule for a given proof node failed.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.ProofRuleException
 */
public final class ProofGuessException extends Exception {
  //
  // Constants
  //
  
  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = 8678802810950005887L;

  
  
  //
  // Attributes
  //
  
  /**
   * The {@link ProofNode} for which the next proof step could not be guessed.
   * 
   * @see #getNode()
   */
  private ProofNode node;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>ProofGuessException</code> telling that the next proof step for the specified
   * <code>node</code> cannot be guessed.
   * 
   * @param node the node for which the guess failed.
   * 
   * @throws NullPointerException if <code>node</code> is <code>null</code>.
   * 
   * @see ProofModel#guess(ProofNode)
   */
  public ProofGuessException(ProofNode node) {
    this(node, null);
  }
  
  /**
   * Same as {@link #ProofGuessException(ProofNode)}, but also accepts an additional {@link Throwable}
   * which yields information about the cause of the exception.
   * 
   * @param node the node for which the guess failed.
   * @param cause the cause, which is saved for later retrieval by the {@link Throwable#getCause()} method.
   *              A <code>null</code> value is permitted, and indicates that the cause is nonexistent or
   *              unknown.
   * 
   * @throws NullPointerException if <code>node</code> is <code>null</code>.
   */
  public ProofGuessException(ProofNode node, Throwable cause) {
    super("Cannot guess next proof step for " + node, cause);
    if (node == null) {
      throw new NullPointerException("node is null");
    }
    this.node = node;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the proof node for which the next proof step could not be guessed.
   * 
   * @return the proof node.
   */
  public ProofNode getNode() {
    return this.node;
  }
}
