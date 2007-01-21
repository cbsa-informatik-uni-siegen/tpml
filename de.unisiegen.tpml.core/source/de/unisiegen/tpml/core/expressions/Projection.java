package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent projections in the expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.UnaryOperator
 */
public class Projection extends UnaryOperator
{
  //
  // Attributes
  //
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


  //
  // Constructors
  //
  /**
   * Allocates a new {@link Projection} with the given <code>arity</code> and
   * the <code>index</code> of the item that should be selected.
   * 
   * @param arity the arity of the tuple to which this projection can be
   *          applied.
   * @param index the index of the item to select from the tuple, starting with
   *          <code>1</code>.
   * @throws IllegalArgumentException if the <code>arity</code> or the
   *           <code>index</code> is invalid.
   */
  public Projection ( int arity , int index )
  {
    this ( arity , index , "#" + arity + "_" + index ) ;
  }


  /**
   * Allocates a new {@link Projection} with the given <code>arity</code> and
   * the <code>index</code> of the item that should be selected, and the
   * string representation <code>op</code>.
   * 
   * @param arity the arity of the tuple to which this projection can be
   *          applied.
   * @param index the index of the item to select from the tuple, starting with
   *          <code>1</code>.
   * @param op the string representation of the projectin.
   * @throws IllegalArgumentException if the <code>arity</code> or the
   *           <code>index</code> is invalid.
   */
  protected Projection ( int arity , int index , String op )
  {
    super ( op ) ;
    // validate the settings
    if ( arity <= 0 )
      throw new IllegalArgumentException (
          "The arity of a projection must be greater than 0" ) ;
    else if ( index <= 0 || index > arity )
      throw new IllegalArgumentException (
          "The index of a projection must be greater than 0 and less than the arity" ) ;
    this.arity = arity ;
    this.index = index ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Projection" ; //$NON-NLS-1$
  }


  //
  // Accessors
  //
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
   * Returns the index.
   * 
   * @return the index.
   */
  public int getIndex ( )
  {
    return this.index ;
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.UnaryOperator#applyTo(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public de.unisiegen.tpml.core.expressions.Expression applyTo ( Expression e )
      throws UnaryOperatorException
  {
    try
    {
      Expression [ ] expressions = ( ( Tuple ) e ).getExpressions ( ) ;
      if ( ! e.isValue ( ) )
      {
        throw new UnaryOperatorException ( this , e ) ;
      }
      if ( this.arity != expressions.length )
      {
        throw new UnaryOperatorException ( this , e ) ;
      }
      return expressions [ this.index - 1 ] ;
    }
    catch ( ClassCastException cause )
    {
      // cast of expression to tuple failed
      throw new UnaryOperatorException ( this , e , cause ) ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @ Override
  public Projection clone ( )
  {
    return new Projection ( this.arity , this.index , getText ( ) ) ;
  }
}
