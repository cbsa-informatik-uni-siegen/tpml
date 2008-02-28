package de.unisiegen.tpml.core.smallstep;


import de.unisiegen.tpml.core.ProofRule;


/**
 * Base interface for small step proof rules.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.ProofRule
 */
public interface SmallStepProofRule extends ProofRule
{

  //
  // Accessors
  //
  /**
   * Returns <code>true</code> if this rule is an axiom, which does not have
   * any premises. Otherwise, if the rule has exactly one premises (small step
   * rules cannot have more than one premise), <code>false</code> is returned
   * (this is called a meta rule then).
   * 
   * @return <code>true</code> if the proof rule has no premises.
   */
  public boolean isAxiom ();


  //
  // Primitives
  //
  /**
   * Returns the <b>EXN</b> rule for this meta rule. If this rule is an axiom
   * or an <b>EXN</b> in itself, this method will return a reference to this
   * rule instead (this does not really make sense, but it is convenient to
   * use).
   * 
   * @return the <b>EXN</b> rule for this meta rule.
   */
  public SmallStepProofRule toExnRule ();
}
