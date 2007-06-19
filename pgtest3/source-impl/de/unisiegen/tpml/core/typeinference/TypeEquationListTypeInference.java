package de.unisiegen.tpml.core.typeinference ;


/**
 * A list of <code>TypeEquation</code>s, in the same fashion as a list in
 * OCaml.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference
 */
public final class TypeEquationListTypeInference
{
  //
  // Constants
  //
  /**
   * The empty equation list.
   * 
   * @see #TypeEquationListTypeInference()
   */
  public static final TypeEquationListTypeInference EMPTY_LIST = new TypeEquationListTypeInference ( ) ;


  //
  // Attributes
  //
  /**
   * The first equation in the list.
   */
  private TypeEquationTypeInference first ;


  /**
   * The remaining equations or <code>null</code>.
   */
  private TypeEquationListTypeInference remaining ;


  //
  // Constructors (private)
  //
  /**
   * Allocates a new empty equation list.
   * 
   * @see #EMPTY_LIST
   */
  private TypeEquationListTypeInference ( )
  {
    super ( ) ;
  }


  /**
   * Allocates a new equation list, which basicly extends <code>remaining</code>
   * with a new {@link TypeEquationTypeInference} <code>first</code>.
   * 
   * @param pFirst the new {@link TypeEquationTypeInference}.
   * @param pRemaining an existing {@link TypeEquationListTypeInference}
   * @throws NullPointerException if <code>first</code> or
   *           <code>remaining</code> is <code>null</code>.
   */
  private TypeEquationListTypeInference (
      final TypeEquationTypeInference pFirst ,
      final TypeEquationListTypeInference pRemaining )
  {
    if ( pFirst == null )
    {
      throw new NullPointerException ( "First is null" ) ; //$NON-NLS-1$
    }
    if ( pRemaining == null )
    {
      throw new NullPointerException ( "Remaining is null" ) ; //$NON-NLS-1$
    }
    this.first = pFirst ;
    this.remaining = pRemaining ;
  }


  //
  // Primitives
  //
  /**
   * Allocates a new {@link TypeEquationListTypeInference}, which extends this
   * equation list with a new {@link TypeEquationTypeInference} for
   * <code>left</code> and <code>right</code>.
   * 
   * @param pTypeEquationTypeInference The new equation.
   * @return the extended {@link TypeEquationListTypeInference}.
   * @throws NullPointerException if <code>left</code> or <code>right</code>
   *           is <code>null</code>.
   */
  public TypeEquationListTypeInference extend (
      TypeEquationTypeInference pTypeEquationTypeInference )
  {
    return new TypeEquationListTypeInference ( pTypeEquationTypeInference ,
        this ) ;
  }


  /**
   * Applies the {@link Substitution} <code>s</code> to all equations
   * contained within this list.
   * 
   * @param s the {@link Substitution} to apply.
   * @return the resulting list of equations.
   * @see Equation#substitute(Substitution) public TypeEquationList
   *      substitute(final TypeSubstitution s) { // nothing to substitute on the
   *      empty list if (this == EMPTY_LIST) { return this; } // apply the
   *      substitution to the first and the remaining equations return new
   *      TypeEquationList(this.first.substitute((DefaultTypeSubstitution)s),
   *      this.remaining .substitute(s)); }
   */
  //
  // Base methods
  //
  /**
   * Returns the string representation of the equations contained in this list.
   * This method is mainly useful for debugging purposes.
   * 
   * @return the string representation.
   * @see TypeEquationTypeInference#toString()
   * @see java.lang.Object#toString()
   */
  @ Override
  public String toString ( )
  {
    final StringBuilder builder = new StringBuilder ( 128 ) ;
    builder.append ( '{' ) ;
    for ( TypeEquationListTypeInference list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      if ( list != this )
      {
        builder.append ( ", " ) ; //$NON-NLS-1$
      }
      builder.append ( list.first ) ;
    }
    builder.append ( '}' ) ;
    return builder.toString ( ) ;
  }


  //
  // Accessors
  //
  /**
   * get the head of the type equation list
   * 
   * @return TypeEquation first
   */
  public TypeEquationTypeInference getFirst ( )
  {
    return this.first ;
  }


  /**
   * return the tail of the type equation list
   * 
   * @return TypeEquationList remaining
   */
  public TypeEquationListTypeInference getRemaining ( )
  {
    return this.remaining ;
  }
}
