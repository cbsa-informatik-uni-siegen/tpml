package de.unisiegen.tpml.core.expressions ;


/**
 * Expression for type equations in the unification algorithm. Every type
 * formula has an expression, but type equation typical don't have one.
 * 
 * @author Benjamin Mies
 */
public class Unify extends Constant
{
  /**
   * Allocates a new <code>Unify</code>.
   */
  public Unify ( )
  {
    super ( "unify" ) ; //$NON-NLS-1$
  }


  /**
   * (@inheritDoc)
   * 
   * @see Constant#clone()
   */
  @ Override
  public Unify clone ( )
  {
    return new Unify ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Unify" ; //$NON-NLS-1$
  }
}
