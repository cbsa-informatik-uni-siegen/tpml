package de.unisiegen.tpml.core.smallstep;

import de.unisiegen.tpml.core.AbstractProofRule;

/**
 * Default implementation of the <code>SmallStepProofRule</code> interface.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.AbstractProofRule
 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofRule
 */
final class DefaultSmallStepProofRule extends AbstractProofRule implements SmallStepProofRule {
  //
  // Attributes
  //
  
  /**
   * <code>true</code> if this small step proof rule is an axiom, and as such, has no premises.
   * The opposite is a meta rule, which has exactly one premise.
   * 
   * @see #isAxiom()
   */
  private boolean axiom;

  
  
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>DefaultSmallStepProofRule</code> with the specified <code>name</code>.
   * If <code>axiom</code> is <code>true</code>, the new rule has no premises, otherwise it has
   * exactly one premise.
   * 
   * @param name the name of the rule.
   * @param axiom <code>true</code> if the rule has no premises.
   * 
   * @throws NullPointerException if <code>name</code> is <code>null</code>.
   * 
   * @see #isAxiom()
   */
  DefaultSmallStepProofRule(String name, boolean axiom) {
    super(name);
    this.axiom = axiom;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofRule#isAxiom()
   */
  public boolean isAxiom() {
    return this.axiom;
  }

  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofRule#toExnRule()
   */
  public SmallStepProofRule toExnRule() {
    if (!isAxiom()) {
      return new DefaultSmallStepProofRule(getName() + "-EXN", true);
    }
    else {
      return this;
    }
  }
}
