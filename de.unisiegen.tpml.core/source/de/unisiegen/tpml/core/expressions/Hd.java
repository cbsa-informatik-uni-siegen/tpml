package de.unisiegen.tpml.core.expressions ;


/**
 * The <code>hd</code> operator, which, when applied to a list, returns the
 * first item of the list, or raises an <code>empty_list</code> exception if
 * the list is empty.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.UnaryListOperator
 * @see de.unisiegen.tpml.core.expressions.Tl
 * @see de.unisiegen.tpml.core.expressions.IsEmpty
 */
public final class Hd extends UnaryListOperator
{
  //
  // Constructor
  //
  /**
   * Allocates a new <code>Hd</code> instance.
   */
  public Hd ( )
  {
    super ( "hd" ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Head" ; //$NON-NLS-1$
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
  public Hd clone ( )
  {
    return new Hd ( ) ;
  }
}
