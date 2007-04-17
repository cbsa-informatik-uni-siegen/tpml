package de.unisiegen.tpml.core.expressions ;


/**
 * Abstract base class for the various list operators, that have to be handled
 * by the interpreters and the type checker.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see UnaryOperator
 */
public abstract class UnaryListOperator extends UnaryOperator
{
  /**
   * Allocates a new <code>UnaryListOperator</code> with the string
   * representation given in <code>text</code>.
   * 
   * @param pText the string representation of the list operator.
   * @see UnaryOperator#UnaryOperator(String)
   */
  protected UnaryListOperator ( String pText )
  {
    super ( pText ) ;
  }


  /**
   * {@inheritDoc} Throws {@link UnsupportedOperationException} on every
   * invocation, because the list operators must be handled by the interpreters
   * in a special way.
   * 
   * @throws UnsupportedOperationException on every invocation.
   * @see UnaryOperator#applyTo(Expression)
   */
  @ SuppressWarnings ( "unused" )
  @ Override
  public Expression applyTo ( @ SuppressWarnings ( "unused" )
  Expression pExpression ) throws UnaryOperatorException
  {
    throw new UnsupportedOperationException (
        "list operators must be handled by the interpreter" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public abstract UnaryListOperator clone ( ) ;


  /**
   * {@inheritDoc}
   */
  @ Override
  public abstract String getCaption ( ) ;
}
