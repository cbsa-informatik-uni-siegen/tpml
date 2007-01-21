package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent relational operators in the expression
 * hierarchy.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.BinaryOperator
 * @see de.unisiegen.tpml.core.expressions.BooleanConstant
 * @see de.unisiegen.tpml.core.expressions.IntegerConstant
 */
public final class RelationalOperator extends BinaryOperator
{
  //
  // Class methods
  //
  /**
   * Returns the equals operator.
   * 
   * @return the equals operator.
   */
  public static final RelationalOperator newEquals ( )
  {
    return new RelationalOperator ( "=" ) ;
  }


  /**
   * Returns the lower-than operator.
   * 
   * @return the lower-than operator.
   */
  public static final RelationalOperator newLowerThan ( )
  {
    return new RelationalOperator ( "<" ) ;
  }


  /**
   * Returns the greater-than operator.
   * 
   * @return the greater-than operator.
   */
  public static final RelationalOperator newGreaterThan ( )
  {
    return new RelationalOperator ( ">" ) ;
  }


  /**
   * Returns the lower-equal operator.
   * 
   * @return the lower-equal operator.
   */
  public static final RelationalOperator newLowerEqual ( )
  {
    return new RelationalOperator ( "<=" ) ;
  }


  /**
   * Returns the greater-equal operator.
   * 
   * @return the greater-equal operator.
   */
  public static final RelationalOperator newGreaterEqual ( )
  {
    return new RelationalOperator ( ">=" ) ;
  }


  //
  // Constructor (private)
  //
  /**
   * Allocates a new <code>RelationalOperator</code> with the specified string
   * representation <code>text</code>.
   * 
   * @param text the string representation.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */
  private RelationalOperator ( String text )
  {
    super ( text , PRIO_RELATIONAL_OPERATOR ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Relational-Operator" ; //$NON-NLS-1$
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.BinaryOperator#applyTo(de.unisiegen.tpml.core.expressions.Expression,
   *      de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public Expression applyTo ( Expression e1 , Expression e2 )
      throws BinaryOperatorException
  {
    if ( e1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ;
    }
    if ( e2 == null )
    {
      throw new NullPointerException ( "e2 is null" ) ;
    }
    try
    {
      // determine the numeric values of the operands
      int n1 = ( ( IntegerConstant ) e1 ).intValue ( ) ;
      int n2 = ( ( IntegerConstant ) e2 ).intValue ( ) ;
      // perform the requested comparison
      if ( getText ( ) == "=" )
      {
        return new BooleanConstant ( n1 == n2 ) ;
      }
      else if ( getText ( ) == "<" )
      {
        return new BooleanConstant ( n1 < n2 ) ;
      }
      else if ( getText ( ) == ">" )
      {
        return new BooleanConstant ( n1 > n2 ) ;
      }
      else if ( getText ( ) == "<=" )
      {
        return new BooleanConstant ( n1 <= n2 ) ;
      }
      else if ( getText ( ) == ">=" )
      {
        return new BooleanConstant ( n1 >= n2 ) ;
      }
      else
      {
        // programming error
        throw new IllegalStateException (
            "inconsistent arithmetic operator class" ) ;
      }
    }
    catch ( ClassCastException e )
    {
      // one of the Expression to IntegerConstant casts failed
      throw new BinaryOperatorException ( this , e1 , e2 ) ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @ Override
  public RelationalOperator clone ( )
  {
    return new RelationalOperator ( getText ( ) ) ;
  }
}
