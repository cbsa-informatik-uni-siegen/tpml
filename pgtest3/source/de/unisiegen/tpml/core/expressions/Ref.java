package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent the <code>ref</code> operator in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Assign
 * @see Deref
 * @see UnaryOperator
 */
public final class Ref extends UnaryOperator
{
  /**
   * Allocates a new <code>Ref</code> instance.
   */
  public Ref ( )
  {
    super ( "ref" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc} This method always throws
   * {@link UnsupportedOperationException} to indicate that it should not be
   * called. Instead the <code>ref</code> operator requires special handling
   * in the interpreter.
   * 
   * @throws UnsupportedOperationException on every invocation, because the
   *           <code>ref</code> operator must be handled by the interpreter.
   * @see de.unisiegen.tpml.core.expressions.UnaryOperator#applyTo(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ SuppressWarnings ( "unused" )
  @ Override
  public Expression applyTo ( @ SuppressWarnings ( "unused" )
  final Expression pExpression ) throws UnaryOperatorException
  {
    throw new UnsupportedOperationException (
        "ref operator must be handled by the interpreter" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Ref clone ( )
  {
    return new Ref ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Ref" ; //$NON-NLS-1$
  }
}
