package de.unisiegen.tpml.core.expressions ;


/**
 * The <code>is_empty</code> operator, which, when applied to a list, returns
 * <code>true</code> if the list is empty, or <code>false</code> if the list
 * contains atleast one item.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.UnaryListOperator
 * @see de.unisiegen.tpml.core.expressions.Hd
 * @see de.unisiegen.tpml.core.expressions.Tl
 */
public final class IsEmpty extends UnaryListOperator
{
  //
  // Constructor
  //
  /**
   * Allocates a new <code>IsEmpty</code> instance.
   */
  public IsEmpty ( )
  {
    super ( "is_empty" ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Is-Empty" ; //$NON-NLS-1$
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
  public IsEmpty clone ( )
  {
    return new IsEmpty ( ) ;
  }
}
