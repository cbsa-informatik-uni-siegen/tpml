package de.unisiegen.tpml.core;

/**
 * This exception is thrown when an attempt to guess the next proof rule for a given proof node failed.
 *
 * @author Benedikt Meurer
 * @version $Id$
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
   * @see ProofModel#guess(ProofNode)
   */
  public ProofGuessException(ProofNode node) {
    super("Cannot guess next proof step for " + node);
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
