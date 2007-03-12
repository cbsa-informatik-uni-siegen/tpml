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
   * 
   * @see Expression#clone()
   */
  @ Override
  public abstract Value clone ( ) ;


  /**
   * {@inheritDoc}
   */
  @ Override
  public abstract String getCaption ( ) ;


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
