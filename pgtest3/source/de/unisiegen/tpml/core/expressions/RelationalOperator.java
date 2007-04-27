package de.unisiegen.tpml.core.expressions ;


/**
 * Instances of this class represent relational operators in the expression
 * hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see BinaryOperator
 * @see BooleanConstant
 * @see IntegerConstant
 */
public final class RelationalOperator extends BinaryOperator
{
  /**
   * Returns the equals operator.
   * 
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   * @return the equals operator.
   */
  public static final RelationalOperator newEquals ( int pParserStartOffset ,
      int pParserEndOffset )
  {
    return new RelationalOperator ( "=" , pParserStartOffset , pParserEndOffset ) ; //$NON-NLS-1$
  }


  /**
   * Returns the greater-equal operator.
   * 
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   * @return the greater-equal operator.
   */
  public static final RelationalOperator newGreaterEqual (
      int pParserStartOffset , int pParserEndOffset )
  {
    return new RelationalOperator (
        ">=" , pParserStartOffset , pParserEndOffset ) ; //$NON-NLS-1$
  }


  /**
   * Returns the greater-than operator.
   * 
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   * @return the greater-than operator.
   */
  public static final RelationalOperator newGreaterThan (
      int pParserStartOffset , int pParserEndOffset )
  {
    return new RelationalOperator ( ">" , pParserStartOffset , pParserEndOffset ) ; //$NON-NLS-1$
  }


  /**
   * Returns the lower-equal operator.
   * 
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   * @return the lower-equal operator.
   */
  public static final RelationalOperator newLowerEqual (
      int pParserStartOffset , int pParserEndOffset )
  {
    return new RelationalOperator (
        "<=" , pParserStartOffset , pParserEndOffset ) ; //$NON-NLS-1$
  }


  /**
   * Returns the lower-than operator.
   * 
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   * @return the lower-than operator.
   */
  public static final RelationalOperator newLowerThan ( int pParserStartOffset ,
      int pParserEndOffset )
  {
    return new RelationalOperator ( "<" , pParserStartOffset , pParserEndOffset ) ; //$NON-NLS-1$
  }


  /**
   * Allocates a new <code>RelationalOperator</code> with the specified string
   * representation <code>text</code>.
   * 
   * @param pText the string representation.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */
  private RelationalOperator ( String pText )
  {
    super ( pText , PRIO_RELATIONAL_OPERATOR ) ;
  }


  /**
   * Allocates a new <code>RelationalOperator</code> with the specified string
   * representation <code>text</code>.
   * 
   * @param pText the string representation.
   * @param pParserStartOffset TODO
   * @param pParserEndOffset TODO
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */
  private RelationalOperator ( String pText , int pParserStartOffset ,
      int pParserEndOffset )
  {
    super ( pText , PRIO_RELATIONAL_OPERATOR ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see BinaryOperator#applyTo(Expression, Expression)
   */
  @ Override
  public Expression applyTo ( Expression pExpression1 , Expression pExpression2 )
      throws BinaryOperatorException
  {
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( "e2 is null" ) ; //$NON-NLS-1$
    }
    try
    {
      // determine the numeric values of the operands
      int n1 = ( ( IntegerConstant ) pExpression1 ).intValue ( ) ;
      int n2 = ( ( IntegerConstant ) pExpression2 ).intValue ( ) ;
      // perform the requested comparison
      if ( getText ( ) == "=" ) //$NON-NLS-1$
      {
        return new BooleanConstant ( n1 == n2 ) ;
      }
      else if ( getText ( ) == "<" ) //$NON-NLS-1$
      {
        return new BooleanConstant ( n1 < n2 ) ;
      }
      else if ( getText ( ) == ">" ) //$NON-NLS-1$
      {
        return new BooleanConstant ( n1 > n2 ) ;
      }
      else if ( getText ( ) == "<=" ) //$NON-NLS-1$
      {
        return new BooleanConstant ( n1 <= n2 ) ;
      }
      else if ( getText ( ) == ">=" ) //$NON-NLS-1$
      {
        return new BooleanConstant ( n1 >= n2 ) ;
      }
      else
      {
        // programming error
        throw new IllegalStateException (
            "inconsistent arithmetic operator class" ) ; //$NON-NLS-1$
      }
    }
    catch ( ClassCastException e )
    {
      // one of the Expression to IntegerConstant casts failed
      throw new BinaryOperatorException ( this , pExpression1 , pExpression2 ) ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public RelationalOperator clone ( )
  {
    return new RelationalOperator ( getText ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Relational-Operator" ; //$NON-NLS-1$
  }
}
