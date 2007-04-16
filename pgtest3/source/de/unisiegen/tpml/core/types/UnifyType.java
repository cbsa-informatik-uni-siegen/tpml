package de.unisiegen.tpml.core.types ;


/**
 * type for the type equation in the unification algorithm
 * 
 * @author Benjamin Mies
 */
public class UnifyType extends PrimitiveType
{
  /**
   * Allocates a new <code>UnitType</code> instance.
   */
  public UnifyType ( )
  {
    super ( "unify" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public UnifyType clone ( )
  {
    return new UnifyType ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Unify-Type" ; //$NON-NLS-1$
  }
}
