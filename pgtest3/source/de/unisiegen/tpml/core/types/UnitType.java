package de.unisiegen.tpml.core.types ;


/**
 * This class represents the <tt>unit</tt> type in our type system. Only a
 * single instance of this class exists at all times.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:277 $
 */
public final class UnitType extends PrimitiveType
{
  /**
   * Allocates a new <code>UnitType</code> instance.
   */
  public UnitType ( )
  {
    super ( "unit" ) ; //$NON-NLS-1$
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
    return "Unit-Type" ; //$NON-NLS-1$
  }
}
