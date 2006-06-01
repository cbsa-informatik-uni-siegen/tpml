package common;

/**
 * Base class for proof rules of the small step, big step
 * interpreter and the type checker.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class ProofRule {
  /**
   * <code>true</code> if this rule is an axiom, which
   * has no premises.
   * 
   * @see #isAxiom()
   */
  protected boolean axiom;
  
  /**
   * The user visible name of the proof rule.
   * 
   * @see #getName()
   */
  protected String name;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new {@link ProofRule} with the given
   * <code>name</code>. If <code>axiom</code> is <code>true</code>
   * the rule has no premises.
   *
   * @param axiom if the rule is an axiom with no premises.
   * @param name the name of the proof rule.
   */
  protected ProofRule(boolean axiom, String name) {
    this.axiom = axiom;
    this.name = name;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns <code>true</code> if this rule is
   * an axiom, whose number of premises is zero,
   * otherwise <code>false</code> is returned.
   * 
   * @return <code>true</code> if this rule is
   *         an axiom.
   */
  public boolean isAxiom() {
    return this.axiom;
  }
  
  /**
   * Returns the user visible name of the proof
   * rule.
   * 
   * @return the user visible rule name.
   */
  public String getName() {
    return this.name;
  }
}
