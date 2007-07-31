package de.unisiegen.tpml.core.types ;


/**
 * Type for the type equation in the unification algorithm
 * 
 * @author Benjamin Mies
 */
public class UnifyType extends PrimitiveType
{
  /**
   * The keyword <code>bool</code>.
   */
  private static final String UNIFY = "unify" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = Type.getCaption ( UnifyType.class ) ;


  /**
   * Allocates a new <code>UnitType</code> instance.
   */
  public UnifyType ( )
  {
    super ( UNIFY ) ;
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
    return CAPTION ;
  }
}
