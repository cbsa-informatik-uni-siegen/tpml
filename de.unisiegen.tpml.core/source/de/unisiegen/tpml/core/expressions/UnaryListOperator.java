package de.unisiegen.tpml.core.expressions;


/**
 * Abstract base class for the various list operators, that have to be handled
 * by the interpreters and the type checker.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Id$
 * @see UnaryOperator
 */
public abstract class UnaryListOperator extends UnaryOperator
{

  /**
   * The assign operator exception string.
   */
  private static final String HANDLED = "list operator must be handled by the interpreter"; //$NON-NLS-1$


  /**
   * The unused string.
   */
  private static final String UNUSED = "unused"; //$NON-NLS-1$


  /**
   * Allocates a new <code>UnaryListOperator</code> with the string
   * representation given in <code>text</code>.
   * 
   * @param pText the string representation of the list operator.
   * @see UnaryOperator#UnaryOperator(String)
   */
  protected UnaryListOperator ( String pText )
  {
    super ( pText );
  }


  /**
   * {@inheritDoc} Throws {@link UnsupportedOperationException} on every
   * invocation, because the list operators must be handled by the interpreters
   * in a special way.
   * 
   * @throws UnsupportedOperationException on every invocation.
   * @see UnaryOperator#applyTo(Expression)
   */
  @SuppressWarnings ( UNUSED )
  @Override
  public Expression applyTo ( @SuppressWarnings ( UNUSED )
  Expression pExpression ) throws UnaryOperatorException
  {
    throw new UnsupportedOperationException ( HANDLED );
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @Override
  public abstract UnaryListOperator clone ();


  /**
   * {@inheritDoc}
   */
  @Override
  public abstract String getCaption ();
}
