package de.unisiegen.tpml.core.expressions ;


/**
 * The constant for the empty list, represented as <code>[]</code>, which
 * must be handled differently from the <code>List</code> expressions, which
 * are syntactic sugar.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.Constant
 */
public final class EmptyList extends Constant
{
  //
  // Constructor
  //
  /**
   * Allocates a new <code>EmptyList</code> instance.
   */
  public EmptyList ( )
  {
    super ( "[]" ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Empty-List" ; //$NON-NLS-1$
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
  public Expression clone ( )
  {
    return new EmptyList ( ) ;
  }
}
