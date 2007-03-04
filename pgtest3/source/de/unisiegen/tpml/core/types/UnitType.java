package de.unisiegen.tpml.core.types ;


/**
 * This class represents the <tt>unit</tt> type in our type system. Only a
 * single instance of this class exists at all times.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:277 $
 * @see #UNIT
 */
public final class UnitType extends PrimitiveType
{
  /**
   * The single instance of the <code>UnitType</code> class, which represents
   * the <tt>unit</tt> type in our type system.
   */
  public static final UnitType UNIT = new UnitType ( ) ;


  /**
   * Allocates a new <code>UnitType</code> instance.
   * 
   * @see #UNIT
   */
  private UnitType ( )
  {
    super ( "unit" ) ; //$NON-NLS-1$
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
