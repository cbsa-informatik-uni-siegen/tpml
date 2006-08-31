package de.unisiegen.tpml.core;

/**
 * Abstract base class for classes implementing the <code>ProofRule</code> interface.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.ProofRule
 */
public abstract class AbstractProofRule implements ProofRule {
  //
  // Attributes
  //
  
  /**
   * The name of the proof rule.
   * 
   * @see #getName()
   */
  private String name;
  
  
  
  //
  // Accessors
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofRule#getName()
   */
  public String getName() {
    return this.name;
  }
}
