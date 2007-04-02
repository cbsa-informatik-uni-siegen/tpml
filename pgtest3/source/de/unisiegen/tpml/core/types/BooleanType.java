package de.unisiegen.tpml.core.types ;


/**
 * This class represents the <tt>bool</tt> type in our type system. Only a
 * single instance of this class exists at all times.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:277 $
 * @see PrimitiveType
 */
public final class BooleanType extends PrimitiveType
{
  /**
   * Allocates a new <code>BooleanType</code> instance.
   */
  public BooleanType ( )
  {
    super ( "bool" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Boolean-Type" ; //$NON-NLS-1$
  }
}
