package de.unisiegen.tpml.core.expressions ;


/**
 * The <code>cons</code> operator, which takes a pair, with an element and a
 * list, and constructs a new list.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.Constant
 */
public final class UnaryCons extends Constant
{
  //
  // Constructor
  //
  /**
   * Constructs a new <code>UnaryCons</code> instance.
   */
  public UnaryCons ( )
  {
    super ( "cons" ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Unary-Cons" ; //$NON-NLS-1$
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @ Override
  public UnaryCons clone ( )
  {
    return new UnaryCons ( ) ;
  }
}
