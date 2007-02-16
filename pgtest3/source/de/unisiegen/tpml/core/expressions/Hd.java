package de.unisiegen.tpml.core.expressions ;


/**
 * The <code>hd</code> operator, which, when applied to a list, returns the
 * first item of the list, or raises an <code>empty_list</code> exception if
 * the list is empty.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see UnaryListOperator
 * @see Tl
 * @see IsEmpty
 */
public final class Hd extends UnaryListOperator
{
  /**
   * Allocates a new <code>Hd</code> instance.
   */
  public Hd ( )
  {
    super ( "hd" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Head" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Hd clone ( )
  {
    return new Hd ( ) ;
  }
}
