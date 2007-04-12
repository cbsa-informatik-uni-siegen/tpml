package de.unisiegen.tpml.core.types;

/**
 * 
 * type for the type equation in the unification algorithm
 *
 * @author Benjamin Mies
 *
 */
public class UnifyType extends PrimitiveType {

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
  public UnitType clone ( )
  {
    return new UnitType ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return " " ; //$NON-NLS-1$
  }
}