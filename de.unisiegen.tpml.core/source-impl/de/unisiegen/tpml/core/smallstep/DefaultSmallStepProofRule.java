package de.unisiegen.tpml.core.smallstep ;


import de.unisiegen.tpml.core.AbstractProofRule ;


/**
 * Default implementation of the <code>SmallStepProofRule</code> interface.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.AbstractProofRule
 * @see de.unisiegen.tpml.core.smallstep.SmallStepProofRule
 */
final class DefaultSmallStepProofRule extends AbstractProofRule implements
    SmallStepProofRule
{
  //
  // Attributes
  //
  /**
   * <code>true</code> if this small step proof rule is an axiom, and as such,
   * has no premises. The opposite is a meta rule, which has exactly one
   * premise.
   * 
   * @see #isAxiom()
   */
  private boolean axiom ;


  //
  // Constructor (package)
  //
  /**
   * Allocates a new <code>DefaultSmallStepProofRule</code> with the specified
   * <code>name</code>. If <code>axiom</code> is <code>true</code>, the
   * new rule has no premises, otherwise it has exactly one premise.
   * 
   * @param group the group id of the small step rule, see the description of
   *          the {@link AbstractProofRule#getGroup()} method for details.
   * @param name the name of the rule.
   * @param pAxiom <code>true</code> if the rule has no premises.
   * @throws NullPointerException if <code>name</code> is <code>null</code>.
   * @see #isAxiom()
   */
  DefaultSmallStepProofRule ( int group , String name , boolean pAxiom )
  {
    super ( group , name ) ;
    this.axiom = pAxiom ;
  }


  //
  // Accessors
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofRule#isAxiom()
   */
  public boolean isAxiom ( )
  {
    return this.axiom ;
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.smallstep.SmallStepProofRule#toExnRule()
   */
  public SmallStepProofRule toExnRule ( )
  {
    if ( ! isAxiom ( ) )
    {
      return new DefaultSmallStepProofRule ( getGroup ( ) , getName ( )
          + "-EXN" , false ) ; //$NON-NLS-1$
    }
    return this ;
  }
}
