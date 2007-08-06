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
   * The equal string.
   */
  private static final String EQUAL = "=" ; //$NON-NLS-1$


  /**
   * The greater equal string.
   */
  private static final String GREATER_EQUAL = ">=" ; //$NON-NLS-1$


  /**
   * The greater string.
   */
  private static final String GREATER = ">" ; //$NON-NLS-1$


  /**
   * The lower equal string.
   */
  private static final String LOWER_EQUAL = "<=" ; //$NON-NLS-1$


  /**
   * The lower string.
   */
  private static final String LOWER = "<" ; //$NON-NLS-1$


  /**
   * String for the case that e1 is null.
   */
  private static final String E1_NULL = "e1 is null" ; //$NON-NLS-1$


  /**
   * String for the case that e2 is null.
   */
  private static final String E2_NULL = "e2 is null" ; //$NON-NLS-1$


  /**
   * The inconsistent exception string.
   */
  private static final String INCONSISTENT = "inconsistent arithmetic operator class" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( RelationalOperator.class ) ;


  /**
   * Returns the equals operator.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @return the equals operator.
   */
  public static final RelationalOperator newEquals ( int pParserStartOffset ,
      int pParserEndOffset )
  {
    return new RelationalOperator ( EQUAL , pParserStartOffset ,
        pParserEndOffset ) ;
  }


  /**
   * Returns the greater-equal operator.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @return the greater-equal operator.
   */
  public static final RelationalOperator newGreaterEqual (
      int pParserStartOffset , int pParserEndOffset )
  {
    return new RelationalOperator ( GREATER_EQUAL , pParserStartOffset ,
        pParserEndOffset ) ;
  }


  /**
   * Returns the greater-than operator.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @return the greater-than operator.
   */
  public static final RelationalOperator newGreaterThan (
      int pParserStartOffset , int pParserEndOffset )
  {
    return new RelationalOperator ( GREATER , pParserStartOffset ,
        pParserEndOffset ) ;
  }


  /**
   * Returns the lower-equal operator.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @return the lower-equal operator.
   */
  public static final RelationalOperator newLowerEqual (
      int pParserStartOffset , int pParserEndOffset )
  {
    return new RelationalOperator ( LOWER_EQUAL , pParserStartOffset ,
        pParserEndOffset ) ;
  }


  /**
   * Returns the lower-than operator.
   * 
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @return the lower-than operator.
   */
  public static final RelationalOperator newLowerThan ( int pParserStartOffset ,
      int pParserEndOffset )
  {
    return new RelationalOperator ( LOWER , pParserStartOffset ,
        pParserEndOffset ) ;
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
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
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
      throw new NullPointerException ( E1_NULL ) ;
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( E2_NULL ) ;
    }
    try
    {
      // determine the numeric values of the operands
      int n1 = ( ( IntegerConstant ) pExpression1 ).intValue ( ) ;
      int n2 = ( ( IntegerConstant ) pExpression2 ).intValue ( ) ;
      // perform the requested comparison
      if ( EQUAL.equals ( getText ( ) ) )
      {
        return new BooleanConstant ( n1 == n2 ) ;
      }
      else if ( LOWER.equals ( getText ( ) ) )
      {
        return new BooleanConstant ( n1 < n2 ) ;
      }
      else if ( GREATER.equals ( getText ( ) ) )
      {
        return new BooleanConstant ( n1 > n2 ) ;
      }
      else if ( LOWER_EQUAL.equals ( getText ( ) ) )
      {
        return new BooleanConstant ( n1 <= n2 ) ;
      }
      else if ( GREATER_EQUAL.equals ( getText ( ) ) )
      {
        return new BooleanConstant ( n1 >= n2 ) ;
      }
      else
      {
        // programming error
        throw new IllegalStateException ( INCONSISTENT ) ;
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
    return CAPTION ;
  }
}
