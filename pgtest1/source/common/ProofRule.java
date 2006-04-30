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
   * The user visible name of the proof rule.
   * 
   * @see #getName()
   */
  protected String name;
  
  /**
   * The number of premises of this proof rule.
   * 
   * @see #getPremises()
   */
  protected int premises;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new {@link ProofRule} with the given
   * <code>name</code> and number of <code>premises</code>.
   * 
   * @param name the name of the proof rule.
   * @param premises the number of premises.
   */
  protected ProofRule(String name, int premises) {
    this.name = name;
    this.premises = premises;
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
   *         
   * @see #getPremises()         
   */
  public boolean isAxiom() {
    return (getPremises() == 0);
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
  
  /**
   * Returns the number of premises for this
   * rule, which may be zero in case of axioms.
   * 
   * @return the number of premises for this
   *         proof rule.
   *         
   * @see #isAxiom()         
   */
  public int getPremises() {
    return this.premises;
  }
}
