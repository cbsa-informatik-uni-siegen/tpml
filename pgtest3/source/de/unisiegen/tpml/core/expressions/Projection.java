package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent projections in the expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see UnaryOperator
 */
public class Projection extends UnaryOperator
{
  /**
   * The arity of the projection.
   * 
   * @see #getArity()
   */
  private int arity ;


  /**
   * The index of the projection.
   * 
   * @see #getIndex()
   */
  private int index ;


  /**
   * Allocates a new {@link Projection} with the given <code>arity</code> and
   * the <code>index</code> of the item that should be selected.
   * 
   * @param pArity the arity of the tuple to which this projection can be
   *          applied.
   * @param pIndex the index of the item to select from the tuple, starting with
   *          <code>1</code>.
   * @throws IllegalArgumentException if the <code>arity</code> or the
   *           <code>index</code> is invalid.
   */
  public Projection ( final int pArity , final int pIndex )
  {
    this ( pArity , pIndex , "#" + pArity + "_" + pIndex ) ; //$NON-NLS-1$//$NON-NLS-2$
  }


  /**
   * Allocates a new {@link Projection} with the given <code>arity</code> and
   * the <code>index</code> of the item that should be selected, and the
   * string representation <code>op</code>.
   * 
   * @param pArity the arity of the tuple to which this projection can be
   *          applied.
   * @param pIndex the index of the item to select from the tuple, starting with
   *          <code>1</code>.
   * @param pOp the string representation of the projectin.
   * @throws IllegalArgumentException if the <code>arity</code> or the
   *           <code>index</code> is invalid.
   */
  protected Projection ( final int pArity , final int pIndex , final String pOp )
  {
    super ( pOp ) ;
    // validate the settings
    if ( pArity <= 0 )
    {
      throw new IllegalArgumentException (
          "The arity of a projection must be greater than 0" ) ; //$NON-NLS-1$
    }
    else if ( ( pIndex <= 0 ) || ( pIndex > pArity ) )
    {
      throw new IllegalArgumentException (
          "The index of a projection must be greater than 0 and less than the arity" ) ; //$NON-NLS-1$
    }
    this.arity = pArity ;
    this.index = pIndex ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see UnaryOperator#applyTo(Expression)
   */
  @ Override
  public Expression applyTo ( final Expression pExpression )
      throws UnaryOperatorException
  {
    try
    {
      final Expression [ ] expressions = ( ( Tuple ) pExpression )
          .getExpressions ( ) ;
      if ( ! pExpression.isValue ( ) )
      {
        throw new UnaryOperatorException ( this , pExpression ) ;
      }
      if ( this.arity != expressions.length )
      {
        throw new UnaryOperatorException ( this , pExpression ) ;
      }
      return expressions [ this.index - 1 ] ;
    }
    catch ( final ClassCastException cause )
    {
      // cast of expression to tuple failed
      throw new UnaryOperatorException ( this , pExpression , cause ) ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Projection clone ( )
  {
    return new Projection ( this.arity , this.index , this.getText ( ) ) ;
  }


  /**
   * Returns the arity.
   * 
   * @return the arity.
   */
  public int getArity ( )
  {
    return this.arity ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Projection" ; //$NON-NLS-1$
  }


  /**
   * Returns the index.
   * 
   * @return the index.
   */
  public int getIndex ( )
  {
    return this.index ;
  }
}
