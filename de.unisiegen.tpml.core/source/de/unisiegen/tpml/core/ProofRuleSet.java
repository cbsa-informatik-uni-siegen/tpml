package de.unisiegen.tpml.core;

/**
 * A set of <code>ProofRule</code>s.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.ProofRule
 */
public interface ProofRuleSet extends Iterable<ProofRule> {
  //
  // Accessors
  //
  
  /**
   * Returns an array with all <code>ProofRule</code>s in this set of proof rules.
   * 
   * @return an array with all <code>ProofRule</code>s.
   */
  public ProofRule[] getRules();
}
