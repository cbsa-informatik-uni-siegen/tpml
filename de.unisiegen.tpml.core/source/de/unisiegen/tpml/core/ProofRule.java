package de.unisiegen.tpml.core;

/**
 * Base interface for proof rules in the small and big step interpreters and the type checker.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.ProofRuleSet
 */
public interface ProofRule {
  //
  // Accessors
  //
  
  /**
   * Returns the user visible name of the proof rule, without the parenthesis. For example for the
   * big step proof rule <b>(APP)</b> the name is just <tt>"APP"</tt>.
   * 
   * @return the user visible rule name of the proof rule.
   */
  public String getName();
}
