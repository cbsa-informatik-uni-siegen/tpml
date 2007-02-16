package de.unisiegen.tpml.core.expressions ;


/**
 * Abstract base class for expression classes, whose instances are always
 * values, for example {@link Lambda} expressions.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Expression
 */
public abstract class Value extends Expression
{
  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Value" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc} The implementation in the <code>Value</code> always returns
   * <code>true</code> and cannot be overwritten by derived classes.
   * 
   * @see Expression#isValue()
   */
  @ Override
  public final boolean isValue ( )
  {
    return true ;
  }
}
